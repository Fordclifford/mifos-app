package org.lspl.mobile.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast

import androidx.appcompat.widget.AppCompatButton

import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick

import com.google.android.material.textfield.TextInputLayout

import org.lspl.mobile.R
import org.lspl.mobile.api.BaseApiManager
import org.lspl.mobile.api.BaseURL
import org.lspl.mobile.api.SelfServiceInterceptor
import org.lspl.mobile.models.payload.LoginPayload
import org.lspl.mobile.presenters.LoginPresenter
import org.lspl.mobile.ui.activities.base.BaseActivity
import org.lspl.mobile.ui.views.LoginView
import org.lspl.mobile.utils.Constants
import org.lspl.mobile.utils.Network
import org.lspl.mobile.utils.Toaster

import javax.inject.Inject

/**
 * @author Vishwajeet
 * @since 05/06/16
 */
class LoginActivity : BaseActivity(), LoginView {

    @JvmField
    @Inject
    var loginPresenter: LoginPresenter? = null

    @JvmField
    @BindView(R.id.btn_login)
    var btnLogin: AppCompatButton? = null

    @JvmField
    @BindView(R.id.til_username)
    var tilUsername: TextInputLayout? = null

    @JvmField
    @BindView(R.id.til_password)
    var tilPassword: TextInputLayout? = null

    @JvmField
    @BindView(R.id.ll_login)
    var llLogin: LinearLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        BaseApiManager.createService(
            BaseURL.PROTOCOL_HTTPS+ BaseURL.API_ENDPOINT,
            SelfServiceInterceptor.DEFAULT_TENANT,
           ""
        )
        super.onCreate(savedInstanceState)
        activityComponent?.inject(this)
        setContentView(R.layout.activity_login)
        ButterKnife.bind(this)
        loginPresenter?.attachView(this)
    }

    /**
     * Called when Login is user has successfully logged in
     */
    override fun onLoginSuccess() {
        loginPresenter?.loadClient()
    }

    /**
     * Shows ProgressDialog when called
     */
    override fun showProgress() {
        showProgressDialog(getString(R.string.progress_message_login))
    }

    /**
     * Hides the progressDialog which is being shown
     */
    override fun hideProgress() {
        hideProgressDialog()
    }

    /**
     * Starts [PassCodeActivity]
     */
    override fun showPassCodeActivity(clientName: String?) {
        showToast(getString(R.string.toast_welcome, clientName))
        startPassCodeActivity()
    }

    /**
     * It is called whenever any error occurs while executing a request
     *
     * @param errorMessage Error message that tells the user about the problem.
     */
    override fun showMessage(errorMessage: String?) {
        showToast(errorMessage!!, Toast.LENGTH_LONG)
        llLogin?.visibility = View.VISIBLE
    }

    override fun showUsernameError(error: String?) {
        tilUsername?.error = error
    }

    override fun showPasswordError(error: String?) {
        tilPassword?.error = error
    }

    override fun clearUsernameError() {
        tilUsername?.isErrorEnabled = false
    }

    override fun clearPasswordError() {
        tilPassword?.isErrorEnabled = false
    }

    /**
     * Called when Login Button is clicked, used for logging in the user
     */

    @OnClick(R.id.btn_login)
    fun onLoginClicked() {
        val username = tilUsername?.editText?.editableText.toString()
        val password = tilPassword?.editText?.editableText.toString()
        if (Network.isConnected(this)) {
            val payload = LoginPayload()
            payload.username = username
            payload.password = password
            loginPresenter?.login(payload)
        } else {
            Toaster.show(llLogin, getString(R.string.no_internet_connection))
        }
    }

    @OnClick(R.id.btn_register)
    fun onRegisterClicked() {
        startActivity(Intent(this@LoginActivity, RegistrationActivity::class.java))
    }

    @OnClick(R.id.btn_forgot)
    fun onForgotClicked() {
        startActivity(Intent(this, ForgotActivity::class.java))
    }

    override fun onDestroy() {
        super.onDestroy()
        loginPresenter?.detachView()
    }

    /**
     * Starts [PassCodeActivity] with `Constans.INTIAL_LOGIN` as true
     */

    private fun startPassCodeActivity() {
        val intent = Intent(this@LoginActivity, PassCodeActivity::class.java)
        intent.putExtra(Constants.INTIAL_LOGIN, true)
        startActivity(intent)
        finish()
    }
}