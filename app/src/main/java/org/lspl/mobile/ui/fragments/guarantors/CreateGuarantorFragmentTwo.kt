package org.lspl.mobile.ui.fragments.guarantors

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import butterknife.ButterKnife
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import org.json.JSONException
import org.json.JSONObject
import org.lspl.mobile.R
import org.lspl.mobile.api.SelfServiceInterceptor
import org.lspl.mobile.api.local.PreferencesHelper
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/* renamed from: org.techsavanna.enkasacco.ui.fragments.CreateGuarantorFragmentTwo */
class CreateGuarantorFragmentTwo : Fragment() {
    private var clientId: String? = null
    private var guarantor_id: String? = null
    var id_no: TextView? = null
    var name: TextView? = null
    var phone_no: TextView? = null
    var preferencesHelper: PreferencesHelper? = null

    /* access modifiers changed from: package-private */
    fun submit() {
        createGuarantor()
    }

    override fun onCreateView(
        layoutInflater: LayoutInflater,
        viewGroup: ViewGroup?,
        bundle: Bundle?
    ): View? {
        val inflate =
            layoutInflater.inflate(R.layout.fragment_create_guarantor_two, viewGroup, false)
        ButterKnife.bind((this as Any), inflate)
        val arguments = arguments
        if (arguments != null) {
            guarantor_id = arguments.getString("id")
            name!!.text = arguments.getString("name")
            id_no!!.text = arguments.getString("id_no")
            phone_no!!.text = arguments.getString("phone")
        }
        if (activity != null) {
            clientId =
                requireActivity().getSharedPreferences("ClientIDPreference", 0).getString("clientId", "")
            val printStream = System.out
            printStream.println("client_id" + clientId)
        }
        return inflate
    }

    fun createGuarantor() {
        if (activity != null) {
            val progressDialog = ProgressDialog(activity)
            progressDialog.setCanceledOnTouchOutside(false)
            progressDialog.setMessage("Submitting Guarantor...")
            progressDialog.show()
            val jSONObject = JSONObject()
            try {
                jSONObject.put("relationshipId", "6")
                jSONObject.put("entityId", guarantor_id)
                jSONObject.put("clientId", clientId)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            OkHttpClient().newBuilder().readTimeout(40, TimeUnit.SECONDS)
                .writeTimeout(40, TimeUnit.SECONDS).connectTimeout(40, TimeUnit.SECONDS).build()
                .newCall(
                    Request.Builder()
                        .url("https://techsavanna.net:8443/enkaapis/public/api/guarantors/create")
                        .method(
                            "POST", RequestBody.create(
                                MediaType.parse("application/json,text/plain"),
                                jSONObject.toString()
                            )
                        ).addHeader("Content-Type", "application/json").addHeader(
                        SelfServiceInterceptor.HEADER_AUTH,
                        "Bearer Q6Od9AhFDWeJGcimeD8oQ4yaw9DzWv5POJ7nktdxFiloJCzbxM"
                    ).addHeader("Content-Type", "text/plain")
                        .addHeader("Cookie", "PHPSESSID=1qn6rbmkose1pfrcnagfu79i83").build()
                ).enqueue(object : Callback {
                @SuppressLint("UseRequireInsteadOfGet")
                override fun onFailure(call: Call, iOException: IOException) {
                    if (this@CreateGuarantorFragmentTwo.activity != null) {
                        this@CreateGuarantorFragmentTwo.activity!!.runOnUiThread {
                            if (this@CreateGuarantorFragmentTwo.activity != null) {
                                progressDialog.dismiss()
                                Toast.makeText(
                                    this@CreateGuarantorFragmentTwo.activity,
                                    "Create guarantor request failed",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }

                @SuppressLint("UseRequireInsteadOfGet")
                @Throws(IOException::class)
                override fun onResponse(call: Call, response: Response) {
                    if (this@CreateGuarantorFragmentTwo.activity != null) {
                        this@CreateGuarantorFragmentTwo.activity!!.runOnUiThread {
                            progressDialog.dismiss()
                            try {
                                val jSONObject = JSONObject(response.body()!!.string())
                                if (jSONObject.getString("success") == "1") {
                                    Toast.makeText(
                                        this@CreateGuarantorFragmentTwo.activity,
                                        jSONObject.getString("message"),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    this@CreateGuarantorFragmentTwo.fragmentManager!!.beginTransaction()
                                        .replace(R.id.container, CreateGuarantorFragmentOne())
                                        .commit()
                                }
                                if (jSONObject.getString("success") == "0" && this@CreateGuarantorFragmentTwo.activity != null) {
                                    Toast.makeText(
                                        this@CreateGuarantorFragmentTwo.activity,
                                        jSONObject.getString("message"),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } catch (e: JSONException) {
                                if (this@CreateGuarantorFragmentTwo.activity != null) {
                                    Toast.makeText(
                                        this@CreateGuarantorFragmentTwo.activity,
                                        "Create guarantor request failed",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                e.printStackTrace()
                            } catch (e2: IOException) {
                                if (this@CreateGuarantorFragmentTwo.activity != null) {
                                    Toast.makeText(
                                        this@CreateGuarantorFragmentTwo.activity,
                                        "Create guarantor request failed",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                e2.printStackTrace()
                            }
                        }
                    }
                }
            })
        }
    }
}