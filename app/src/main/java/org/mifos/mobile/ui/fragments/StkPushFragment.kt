package org.mifos.mobile.ui.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import okhttp3.*
import org.mifos.mobile.R
import org.mifos.mobile.models.StkpushStatusResponse
import org.mifos.mobile.models.stkpush.StkpushRequestPayload
import org.mifos.mobile.presenters.StkpushPresenter
import org.mifos.mobile.ui.activities.HomeActivity
import org.mifos.mobile.ui.activities.base.BaseActivity
import org.mifos.mobile.ui.fragments.base.BaseFragment
import org.mifos.mobile.ui.views.StkpushView
import org.mifos.mobile.utils.Constants
import org.mifos.mobile.utils.Toaster
import javax.inject.Inject


class StkPushFragment : BaseFragment(), StkpushView {
    private var accountId: String? = null
    private var loanBal: Double? = null

    @JvmField
    @BindView(R.id.amount)
    var amountTxt: EditText? = null


    @JvmField
    @BindView(R.id.phoneno)
    var phoneTxt: EditText? = null

    @JvmField
    @BindView(R.id.accounttxt)
    var accountidTxt: TextView? = null


    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        if (arguments != null) {
            accountId = requireArguments().getString(Constants.ACCOUNT_ID)
            loanBal = requireArguments().getDouble(Constants.LOANBAL)
        }
        setHasOptionsMenu(true)
    }

    @JvmField
    @Inject
    var presenter: StkpushPresenter? = null
    private var rootView: View? = null

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_stk_push, container, false)
        (activity as BaseActivity?)?.activityComponent?.inject(this)
        ButterKnife.bind(this, rootView!!)
        presenter?.attachView(this)
        if (activity != null) {
            val sharedPreferences = requireActivity().getSharedPreferences("MyPrefsFile", 0)
            accountidTxt!!.text = sharedPreferences.getString(
                R.string.accounttobepaidfor.toString(),
                ""
            ) + " " + accountId
            sharedPreferences.getString("phonenoIndividual", null as String?)
            val string2 =
                sharedPreferences.getString("phonenoIndividual", getString(R.string.enterphoneno1))
            phoneTxt!!.setText(string2)
            amountTxt!!.setText("" + loanBal!!.toInt())

        }
        return rootView
    }

    @OnClick(R.id.paymentbtn)
    fun verifyClicked() {
        val replaceAll = accountId!!.replace("[^a-zA-Z]+".toRegex(), "")
        if (phoneTxt!!.text.toString().isEmpty() || amountTxt!!.text.toString().isEmpty()) {
            Toast.makeText(
                activity,
                "Please fill all details",
                Toast.LENGTH_SHORT
            ).show()
        } else if (replaceAll == "R" && amountTxt!!.text.toString().toLong() < 199) {
            Toast.makeText(
                activity,
                "Amount cannot be less 200",
                Toast.LENGTH_SHORT
            ).show()
        } else if (phoneTxt!!.text.toString().length < 8) {
            Toast.makeText(
                activity,
                "Phone number cannot be less than 8 digits!",
                Toast.LENGTH_SHORT
            ).show()
        } else if (amountTxt!!.text.toString() == "0") {
            Toast.makeText(
                activity,
                "Amount must be greater than zero!",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            val payload = StkpushRequestPayload()
            Log.d("Phone", phoneTxt!!.text.toString())
            val obj2: String = phoneTxt!!.text.toString()
            val phoneNo = if (obj2.length >= 9) obj2.substring(obj2.length - 9) else ""
            Log.d("Phone Input",phoneNo)
            payload.amount = amountTxt!!.text.toString()
            payload.accountReference = accountId
            payload.phone = "254$phoneNo".toLong()
            presenter!!.stkPush(payload)
        }
    }


    companion object {
        fun newInstance(str: String?, d: Double): StkPushFragment {
            val stkPushFragment = StkPushFragment()
            val bundle = Bundle()
            bundle.putString(Constants.ACCOUNT_ID, str)
            bundle.putDouble(Constants.LOANBAL, d)
            stkPushFragment.arguments = bundle
            return stkPushFragment
        }
    }

    override fun showError(msg: String?) {
        Toaster.show(rootView, msg)
    }

    override fun showProgress() {
        showMifosProgressDialog(getString(R.string.processing))
    }

    override fun hideProgress() {
        hideMifosProgressDialog()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter?.detachView()
    }

    override fun showSuccessfulStatus(responseBody: StkpushStatusResponse) {
        if(responseBody.ResultCode!! == "0"){
            startActivity(Intent(activity, HomeActivity::class.java))
            Toast.makeText(
                context,
                getString(R.string.successful),
                Toast.LENGTH_SHORT
            ).show()
        }else{
            Toast.makeText(
                context,
                getString(R.string.not_successful),
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    }