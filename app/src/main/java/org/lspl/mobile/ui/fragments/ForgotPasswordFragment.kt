package org.lspl.mobile.ui.fragments

import android.content.Context
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
import org.lspl.mobile.models.passwordreset.ResetPayload
import org.lspl.mobile.models.templates.client.FamilyMemberOptions
import org.lspl.mobile.models.templates.client.SecurityQuestionOptions
import org.lspl.mobile.presenters.RegistrationVerificationPresenter
import org.lspl.mobile.ui.activities.base.BaseActivity
import org.lspl.mobile.ui.fragments.base.BaseFragment
import org.lspl.mobile.ui.views.RegistrationVerificationView
import org.lspl.mobile.utils.Toaster
import javax.inject.Inject

/**
 * Created by dilpreet on 31/7/17.
 */
class ForgotPasswordFragment : BaseFragment(), RegistrationVerificationView {

    @JvmField
    @BindView(R.id.et_user_username)
    var etUsername: TextInputLayout? = null


    @JvmField
    @Inject
    var presenter: RegistrationVerificationPresenter? = null
    private var rootView: View? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.activity_forget, container, false)
        (activity as BaseActivity?)?.activityComponent?.inject(this)
        ButterKnife.bind(this, rootView!!)
        presenter?.attachView(this)
        return rootView
    }

    @OnClick(R.id.btn_client)
    fun verifyClicked() {
        val payload = ResetPayload()
        payload.username = etUsername?.editText!!.editableText.toString()
        presenter?.requestToken(payload)
    }

    override fun showVerifiedSuccessfully() {
        val preference = requireActivity().getSharedPreferences("RESET_USERNAME", Context.MODE_PRIVATE)
        val editor = preference.edit()
        editor.putString("USERNAME", etUsername?.editText!!.editableText.toString())
        editor.apply()

        (activity as BaseActivity?)?.replaceFragment(
            TokenFragment.newInstance(),
            true,
            R.id.container
        )
        Toast.makeText(context, getString(R.string.successful), Toast.LENGTH_SHORT).show()

    }

    override fun showError(msg: String?) {
        Toaster.show(rootView, msg)
    }

    override fun showClientTemplate(clientsTemplate: FamilyMemberOptions?) {
        TODO("Not yet implemented")
    }

    override fun showQuestions(clientsTemplate: List<SecurityQuestionOptions>?) {
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
        fun newInstance(): ForgotPasswordFragment {
            return ForgotPasswordFragment()
        }
    }
}