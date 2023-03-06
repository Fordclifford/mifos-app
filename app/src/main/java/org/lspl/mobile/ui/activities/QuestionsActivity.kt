package org.lspl.mobile.ui.activities

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
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
import org.lspl.mobile.models.payload.QuestionPayload
import org.lspl.mobile.models.templates.client.FamilyMemberOptions
import org.lspl.mobile.models.templates.client.SecurityQuestionOptions
import org.lspl.mobile.presenters.LoginPresenter
import org.lspl.mobile.presenters.RegistrationVerificationPresenter
import org.lspl.mobile.ui.activities.base.BaseActivity
import org.lspl.mobile.ui.fragments.*
import org.lspl.mobile.ui.views.LoginView
import org.lspl.mobile.ui.views.RegistrationVerificationView
import org.lspl.mobile.utils.Constants
import org.lspl.mobile.utils.MaterialDialog
import org.lspl.mobile.utils.Toaster
import javax.inject.Inject

class QuestionsActivity : BaseActivity(), LoginView {

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
    var loginPresenter: LoginPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_questions)

        activityComponent?.inject(this)
        ButterKnife.bind(this)
        loginPresenter?.attachView(this)

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

        loginPresenter!!.loadQuestions()
    }

    override fun onBackPressed() {
        MaterialDialog.Builder().init(this)
                .setTitle(getString(R.string.dialog_cancel_registration_title))
                .setMessage(getString(R.string.dialog_cancel_reset))
                .setPositiveButton(getString(R.string.cancel),
                        DialogInterface.OnClickListener { _, _ -> super.onBackPressed() })
                .setNegativeButton(R.string.continue_str,
                        DialogInterface.OnClickListener { dialog, _ -> dialog.dismiss() })
                .createMaterialDialog()
                .show()
    }

    @OnClick(R.id.btn_submit_data)
    fun verifyClicked() {

        val payload = QuestionPayload()

        if (questionIdOptions!!.isNotEmpty() && questionId != 0) {
            payload.questionId = questionId
            payload.answer = etAnswer?.editText!!.editableText.toString()
        }

        if (!isValidQuestion) {
            return
        }

        loginPresenter?.addQuestion(payload)
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
                e.notifyUserWithToast(this)
                result = false
            }
            return result
        }

    override fun onLoginSuccess() {
        val intent = Intent(this, PassCodeActivity::class.java)
        intent.putExtra(Constants.INTIAL_LOGIN, true)
        startActivity(intent)
        finish()
    }

    override fun showMessage(errorMessage: String?) {
        TODO("Not yet implemented")
    }

    override fun showUsernameError(error: String?) {
        TODO("Not yet implemented")
    }

    override fun showPasswordError(error: String?) {
        TODO("Not yet implemented")
    }

    override fun clearUsernameError() {
        TODO("Not yet implemented")
    }

    override fun clearPasswordError() {
        TODO("Not yet implemented")
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

    override fun showPassCodeActivity(clientName: String?) {
        TODO("Not yet implemented")
    }

    override fun showProgress() {
        showProgressDialog("Please Wait...")
    }

    override fun hideProgress() {
        hideProgressDialog()
    }

    override fun onDestroy() {
        super.onDestroy()
        loginPresenter?.detachView()
    }


}