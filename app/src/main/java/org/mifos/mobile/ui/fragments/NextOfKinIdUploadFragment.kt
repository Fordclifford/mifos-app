package org.mifos.mobile.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import org.mifos.mobile.R
import org.mifos.mobile.models.register.UserVerify
import org.mifos.mobile.presenters.ClientIdUploadPresenter
import org.mifos.mobile.presenters.RegistrationVerificationPresenter
import org.mifos.mobile.ui.activities.LoginActivity
import org.mifos.mobile.ui.activities.base.BaseActivity
import org.mifos.mobile.ui.fragments.base.BaseFragment
import org.mifos.mobile.ui.views.ClientIdUploadView
import org.mifos.mobile.ui.views.RegistrationVerificationView
import org.mifos.mobile.utils.Toaster
import javax.inject.Inject

/**
 * Created by dilpreet on 31/7/17.
 */
class NextOfKinIdUploadFragment : BaseFragment(), ClientIdUploadView {

    @kotlin.jvm.JvmField
    @BindView(R.id.et_request_id)
    var etRequestId: EditText? = null

    @kotlin.jvm.JvmField
    @BindView(R.id.et_authentication_token)
    var etToken: EditText? = null

    @kotlin.jvm.JvmField
    @Inject
    var presenter: ClientIdUploadPresenter? = null
    private var rootView: View? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_upload_next_of_kin_id, container, false)
        (activity as BaseActivity?)?.activityComponent?.inject(this)
        ButterKnife.bind(this, rootView!!)
        presenter?.attachView(this)
        return rootView
    }

    @OnClick(R.id.next_of_kin_id_upload)
    fun verifyClicked() {
        (activity as BaseActivity?)?.replaceFragment(OtpVerificationFragment.newInstance(), true, R.id.container)

    }

    override fun showUploadedSuccessfully() {
        startActivity(Intent(activity, LoginActivity::class.java))
        Toast.makeText(context, getString(R.string.verified), Toast.LENGTH_SHORT).show()
        activity?.finish()
    }

    override fun showError(msg: String?) {
        Toaster.show(rootView, msg)
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
        fun newInstance(): NextOfKinIdUploadFragment {
            return NextOfKinIdUploadFragment()
        }
    }
}