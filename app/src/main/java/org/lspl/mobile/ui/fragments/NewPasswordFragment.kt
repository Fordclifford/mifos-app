package org.lspl.mobile.ui.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import org.lspl.mobile.R
import org.lspl.mobile.api.RequiredFieldException
import org.lspl.mobile.models.passwordreset.NewpasswordPayload
import org.lspl.mobile.models.passwordreset.PasswordPayload
import org.lspl.mobile.models.templates.client.FamilyMemberOptions
import org.lspl.mobile.models.templates.client.SecurityQuestionOptions
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
class NewPasswordFragment : BaseFragment(), RegistrationVerificationView {

    @JvmField
    @BindView(R.id.et_answer)
    var etAnswer: TextInputLayout? = null

    @JvmField
    @BindView(R.id.et_new_password)
    var etPassword: TextInputLayout? = null

    @JvmField
    @BindView(R.id.et_cpassword)
    var etCPassword: TextInputLayout? = null

    @JvmField
    @BindView(R.id.question)
    var spQuestion: MaterialAutoCompleteTextView? = null

    private var questionIdOptions: MutableList<String>? = ArrayList()

    private var securityQuestionOptions: List<SecurityQuestionOptions>? = null

    private var questionId = 0


    @JvmField
    @Inject
    var presenter: RegistrationVerificationPresenter? = null
    private var rootView: View? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_new_password, container, false)
        (activity as BaseActivity?)?.activityComponent?.inject(this)
        ButterKnife.bind(this, rootView!!)
        presenter?.attachView(this)

        //Question
        spQuestion?.setSimpleItems(questionIdOptions!!.toTypedArray())
        spQuestion?.setSimpleItems(questionIdOptions!!.toTypedArray())

        spQuestion?.setOnItemClickListener { _, _, position, _ ->
            securityQuestionOptions?.let {
                if (it.size > position) {
                    questionId = it[position].id
                }
            }
        }


        presenter!!.loadQuestions()

        return rootView
    }

    @OnClick(R.id.btn_submit_data)
    fun verifyClicked() {
        val preference = requireActivity().getSharedPreferences("RESET_USERNAME", Context.MODE_PRIVATE)

        val payload = NewpasswordPayload()
        if (etPassword?.editText!!.editableText.toString() != etCPassword?.editText!!.editableText.toString()) {
            Toaster.show(rootView, getString(R.string.error_password_not_match))
            return
        } else {
            payload.newPassword = etPassword?.editText!!.editableText.toString()
        }

        if (questionIdOptions!!.isNotEmpty() && questionId != 0) {
            val list: ArrayList<PasswordPayload> = ArrayList()
            list.add(PasswordPayload(questionId, etAnswer?.editText!!.editableText.toString()))
            payload.securityQuestionAnswers = list
        }

        if (!isValidQuestion) {
            return
        }


        payload.username = preference.getString("USERNAME", "")

        println("jfjffjh "+Gson().toJson(payload))
        presenter?.resetPassword(payload)
    }

    private val isValidQuestion: Boolean
        get() {
            var result = true
            try {
                if (TextUtils.isEmpty(questionId.toString()) || questionId==0) {
                    throw RequiredFieldException(
                        resources.getString(R.string.security_question),
                        resources.getString(R.string.error_cannot_be_empty)
                    )
                }
            } catch (e: RequiredFieldException) {
                e.notifyUserWithToast(activity)
                result = false
            }
            return result
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

    }

    override fun showQuestions(clientsTemplate: List<SecurityQuestionOptions>?) {
        this.securityQuestionOptions = clientsTemplate

        //Question
        if (securityQuestionOptions != null) {
            for ((_, name) in securityQuestionOptions!!) {
                questionIdOptions!!.add(name!!)
            }
            spQuestion?.setSimpleItems(questionIdOptions!!.toTypedArray())
        }
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
        fun newInstance(): NewPasswordFragment {
            return NewPasswordFragment()
        }
    }
}