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
import org.mifos.mobile.presenters.RegistrationVerificationPresenter
import org.mifos.mobile.ui.activities.LoginActivity
import org.mifos.mobile.ui.activities.base.BaseActivity
import org.mifos.mobile.ui.fragments.base.BaseFragment
import org.mifos.mobile.ui.views.RegistrationVerificationView
import org.mifos.mobile.utils.Toaster
import javax.inject.Inject

/**
 * Created by dilpreet on 31/7/17.
 */
class NextOfKinRegistrationFragment(clientId: Long) : BaseFragment(), RegistrationVerificationView {

    @JvmField
    @BindView(R.id.et_request_id)
    var etRequestId: EditText? = null

    @JvmField
    @BindView(R.id.et_authentication_token)
    var etToken: EditText? = null
    var clientId: Long? = clientId


    @JvmField
    @Inject
    var presenter: RegistrationVerificationPresenter? = null
    private var rootView: View? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_next_of_kin_registration, container, false)
        (activity as BaseActivity?)?.activityComponent?.inject(this)
        ButterKnife.bind(this, rootView!!)
        presenter?.attachView(this)
        return rootView
    }

    @OnClick(R.id.next_of_kin_register)
    fun verifyClicked() {
        (activity as BaseActivity?)?.replaceFragment(NextOfKinIdUploadFragment.newInstance(22), true, R.id.container)

    }

    override fun showVerifiedSuccessfully() {
        (activity as BaseActivity?)?.replaceFragment(NextOfKinIdUploadFragment.newInstance(clientId!!), true, R.id.container)

        Toast.makeText(context, getString(R.string.id_submitted), Toast.LENGTH_SHORT).show()
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
        fun newInstance(clientId: Long): NextOfKinRegistrationFragment {
            return NextOfKinRegistrationFragment(clientId)
        }
    }
}