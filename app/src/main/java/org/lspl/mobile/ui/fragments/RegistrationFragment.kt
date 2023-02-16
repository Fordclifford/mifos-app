package org.lspl.mobile.ui.fragments

import android.graphics.PorterDuff
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import kotlinx.android.synthetic.main.fragment_registration.*

import org.lspl.mobile.R
import org.lspl.mobile.api.BaseApiManager
import org.lspl.mobile.api.BaseURL
import org.lspl.mobile.api.SelfServiceInterceptor
import org.lspl.mobile.models.register.ClientUserRegisterPayload
import org.lspl.mobile.models.register.IdentifierPayload
import org.lspl.mobile.presenters.RegistrationPresenter
import org.lspl.mobile.ui.activities.base.BaseActivity
import org.lspl.mobile.ui.fragments.base.BaseFragment
import org.lspl.mobile.ui.views.RegistrationView
import org.lspl.mobile.utils.Network
import org.lspl.mobile.utils.PasswordStrength
import org.lspl.mobile.utils.Toaster

import javax.inject.Inject

/**
 * Created by dilpreet on 31/7/17.
 */
class RegistrationFragment : BaseFragment(), RegistrationView {
    @JvmField
    @BindView(R.id.et_id_number)
    var idNumber: EditText? = null

    @JvmField
    @BindView(R.id.et_kra_pin)
    var kraPin: EditText? = null


    @JvmField
    @BindView(R.id.et_first_name)
    var etFirstName: EditText? = null

    @JvmField
    @BindView(R.id.et_last_name)
    var etLastName: EditText? = null

    @JvmField
    @BindView(R.id.et_phone_number)
    var etPhoneNumber: EditText? = null

    @JvmField
    @BindView(R.id.et_email)
    var etEmail: EditText? = null

    @JvmField
    @BindView(R.id.et_password)
    var etPassword: EditText? = null

    @JvmField
    @BindView(R.id.et_confirm_password)
    var etConfirmPassword: EditText? = null


    @JvmField
    @Inject
    var presenter: RegistrationPresenter? = null

    @JvmField
    @BindView(R.id.progressBar)
    var progressBar: ProgressBar? = null

    @JvmField
    @BindView(R.id.password_strength)
    var strengthView: TextView? = null
    private var rootView: View? = null
    var clientId:Long?=null
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_registration, container, false)
        (activity as BaseActivity?)?.activityComponent?.inject(this)
        val rootView = this.rootView
        if (rootView != null) {
            ButterKnife.bind(this, rootView)
        }
        presenter?.attachView(this)
        progressBar?.visibility = View.GONE
        strengthView?.visibility = View.GONE
        etPassword?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if (charSequence.isEmpty()) {
                    progressBar?.visibility = View.GONE
                    strengthView?.visibility = View.GONE
                } else {
                    progressBar?.visibility = View.VISIBLE
                    strengthView?.visibility = View.VISIBLE
                    updatePasswordStrengthView(charSequence.toString())
                }
            }

            override fun afterTextChanged(editable: Editable) {}
        })
        return rootView
    }

    private fun updatePasswordStrengthView(password: String) {
        if (TextView.VISIBLE != strengthView?.visibility) return
        if (password.isEmpty()) {
            strengthView?.text = ""
            progressBar?.progress = 0
            return
        }
        val str = PasswordStrength.calculateStrength(password)
        strengthView?.text = str.getText(context)
        strengthView?.setTextColor(str.color)
        val mode = PorterDuff.Mode.SRC_IN
        progressBar?.progressDrawable?.setColorFilter(str.color, mode)
        if (str.getText(context) == getString(R.string.password_strength_weak)) {
            progressBar?.progress = 25
        } else if (str.getText(context) == getString(R.string.password_strength_medium)) {
            progressBar?.progress = 50
        } else if (str.getText(context) == getString(R.string.password_strength_strong)) {
            progressBar?.progress = 75
        } else {
            progressBar?.progress = 100
        }
    }

    @OnClick(R.id.btn_register)
    fun registerClicked() {
        if (areFieldsValidated()) {
            val payload = ClientUserRegisterPayload()
            val idPayload= IdentifierPayload("1")
            val kraPayload = IdentifierPayload("15")
            idPayload.documentKey=idNumber?.text.toString()
            kraPayload.documentKey = kraPin?.text.toString()
            payload.email = etEmail?.text.toString()
            payload.firstname = etFirstName?.text.toString()
            payload.lastname = etLastName?.text.toString()
            payload.mobileNo = etPhoneNumber?.text.toString()
            payload.username=etPhoneNumber?.text.toString().replace(" ", "")
            if (etPassword?.text.toString() != etConfirmPassword?.text.toString()) {
                Toaster.show(rootView, getString(R.string.error_password_not_match))
                return
            } else {
                payload.password = etPassword?.text.toString()
            }
            payload.password = etPassword?.text.toString()
            payload.repeatPassword= etPassword?.text.toString()
            if (Network.isConnected(context)) {
              presenter?.registerUser(kraPayload,idPayload,payload)
            } else {
                Toaster.show(rootView, getString(R.string.no_internet_connection))
            }
        }
    }


    private fun areFieldsValidated(): Boolean {
        if (kraPin?.text.toString().trim { it <= ' ' }.isEmpty()) {
            Toaster.show(rootView, getString(R.string.error_validation_blank, getString(R.string.kra_pin)))
            return false
        }
        if (idNumber?.text.toString().trim { it <= ' ' }.isEmpty()) {
            Toaster.show(rootView, getString(R.string.error_validation_blank, getString(R.string.id_number)))
            return false
        } else if (etFirstName?.text?.isEmpty() == true) {
            Toaster.show(rootView, getString(R.string.error_validation_blank, getString(R.string.first_name)))
            return false
        } else if (etLastName?.text.toString().trim { it <= ' ' }.isEmpty()) {
            Toaster.show(rootView, getString(R.string.error_validation_blank, getString(R.string.last_name)))
            return false
        } else if (etEmail?.text.toString().trim { it <= ' ' }.isEmpty()) {
            Toaster.show(rootView, getString(R.string.error_validation_blank, getString(R.string.email)))
            return false
        } else if (etPassword?.text.toString().trim { it <= ' ' }.isEmpty()) {
            Toaster.show(rootView, getString(R.string.error_validation_blank, getString(R.string.password)))
            return false
        } else if (etPassword?.text.toString().trim { it <= ' ' }.length
                < etPassword?.text.toString().length) {
            Toaster.show(rootView,
                    getString(R.string.error_validation_cannot_contain_leading_or_trailing_spaces,
                            getString(R.string.password)))
            return false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(etEmail?.text.toString().trim { it <= ' ' })
                        .matches()) {
            Toaster.show(rootView, getString(R.string.error_invalid_email))
            return false
        } else if (etPassword?.text.toString().trim { it <= ' ' }.length < 6) {
            Toaster.show(rootView, getString(R.string.error_validation_minimum_chars,
                    getString(R.string.password), resources.getInteger(R.integer.password_minimum_length)))
            return false
        }
        return true
    }

    override fun showRegisteredSuccessfully(clientId: Long) {
        (activity as BaseActivity?)?.replaceFragment(PassportPhotoUploadFragment.newInstance(clientId), true, R.id.container)
        Toast.makeText(context, getString(R.string.successful), Toast.LENGTH_SHORT).show()
    }

    override fun showError(msg: String?) {
        Toaster.show(rootView, msg)
    }

    override fun showProgress() {
        showMifosProgressDialog(getString(R.string.submitting))
    }

    override fun hideProgress() {
        hideMifosProgressDialog()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter?.detachView()
    }

    companion object {
        fun newInstance(): RegistrationFragment {
            return RegistrationFragment()
        }
    }
}