package org.lspl.mobile.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.google.android.material.textfield.TextInputLayout
import org.lspl.mobile.R
import org.lspl.mobile.models.passwordreset.NewpasswordPayload
import org.lspl.mobile.models.templates.client.FamilyMemberOptions
import org.lspl.mobile.presenters.RegistrationVerificationPresenter
import org.lspl.mobile.ui.activities.LoginActivity
import org.lspl.mobile.ui.activities.base.BaseActivity
import org.lspl.mobile.ui.fragments.base.BaseFragment
import org.lspl.mobile.ui.views.RegistrationVerificationView
import org.lspl.mobile.utils.Toaster
import javax.inject.Inject

/**
 * Created by dilpreet on 31/7/17.
 */
class ResetPasswordFragment : BaseFragment(), RegistrationVerificationView {

    @JvmField
    @BindView(R.id.et_reset_token)
    var etResetToken: TextInputLayout? = null

    @JvmField
    @BindView(R.id.et_new_password)
    var etPassword: TextInputLayout? = null

    @JvmField
    @BindView(R.id.et_cpassword)
    var etCPassword: TextInputLayout? = null


    @JvmField
    @Inject
    var presenter: RegistrationVerificationPresenter? = null
    private var rootView: View? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.activity_new_password, container, false)
        (activity as BaseActivity?)?.activityComponent?.inject(this)
        ButterKnife.bind(this, rootView!!)
        presenter?.attachView(this)
        return rootView
    }

    @OnClick(R.id.btn_submit_data)
    fun verifyClicked() {
        val payload = NewpasswordPayload()
        if (etPassword?.editText!!.editableText.toString() != etCPassword?.editText!!.editableText.toString()) {
            Toaster.show(rootView, getString(R.string.error_password_not_match))
            return
        } else {
            payload.newPassword = etPassword?.editText!!.editableText.toString()
        }

        payload.token = etResetToken?.editText!!.editableText.toString()
        presenter?.resetPassword(payload)
    }

    override fun showVerifiedSuccessfully() {
        startActivity(Intent(activity, LoginActivity::class.java))
        Toast.makeText(context, getString(R.string.password_reset_success), Toast.LENGTH_SHORT).show()
        activity?.finish()
    }

    override fun showError(msg: String?) {
        Toaster.show(rootView, msg)
    }

    override fun showClientTemplate(clientsTemplate: FamilyMemberOptions?) {
        TODO("Not yet implemented")
    }

    override fun showProgress() {
        showMifosProgressDialog(getString(R.string.verifying))
    }

    override fun hideProgress() {
        hideMifosProgressDialog()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter?.detachView()
    }

    companion object {
        fun newInstance(): ResetPasswordFragment {
            return ResetPasswordFragment()
        }
    }
}