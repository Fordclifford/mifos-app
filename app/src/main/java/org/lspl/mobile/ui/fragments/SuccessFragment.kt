package org.lspl.mobile.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import org.lspl.mobile.R
import org.lspl.mobile.models.stkpush.StkpushResponse
import org.lspl.mobile.models.templates.client.FamilyMemberOptions
import org.lspl.mobile.models.templates.client.SecurityQuestionOptions
import org.lspl.mobile.presenters.RegistrationVerificationPresenter
import org.lspl.mobile.ui.activities.HomeActivity
import org.lspl.mobile.ui.activities.base.BaseActivity
import org.lspl.mobile.ui.fragments.base.BaseFragment
import org.lspl.mobile.ui.views.RegistrationVerificationView
import org.lspl.mobile.utils.Toaster
import org.lspl.mobile.widget.FirstFontMediumButton
import org.lspl.mobile.widget.FirstFontMediumTextView
import org.lspl.mobile.widget.FirstFontRegularTextView
import javax.inject.Inject

/**
 * Created by dilpreet on 31/7/17.
 */
class SuccessFragment : BaseFragment(),
    RegistrationVerificationView {

    @JvmField
    @BindView(R.id.btn_ok)
    var btnOk: FirstFontMediumButton? = null

    @JvmField
    @BindView(R.id.tv_status)
    var tvStatus: FirstFontMediumTextView? = null

    @JvmField
    @BindView(R.id.tv_success_des)
    var tvDesc: FirstFontRegularTextView? = null

    private var responseCode: String? = null

    @JvmField
    @Inject
    var presenter: RegistrationVerificationPresenter? = null
    private var rootView: View? = null

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        if (arguments != null) {
            responseCode = requireArguments().getString("RESPONSE_CODE")
        }
        setHasOptionsMenu(true)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.setup_activity_success, container, false)
        (activity as BaseActivity?)?.activityComponent?.inject(this)
        ButterKnife.bind(this, rootView!!)
        presenter?.attachView(this)
        if(responseCode.equals("0")){
            tvDesc!!.text=getString(R.string.success_prompt)
        }else{
            tvDesc!!.text=getString(R.string.error_prompt)
        }
        return rootView
    }

    @OnClick(R.id.btn_ok)
    fun verifyClicked() {
        startActivity(Intent(activity, HomeActivity::class.java))
    }

    override fun showVerifiedSuccessfully() {
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
        fun newInstance(responseBody: StkpushResponse): SuccessFragment {
            val successFragment = SuccessFragment()
            val bundle = Bundle()
            bundle.putString("RESPONSE_CODE", responseBody.ResponseCode)
            successFragment.arguments = bundle
           return successFragment
        }
    }
}