package org.lspl.mobile.ui.fragments.guarantors

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import butterknife.ButterKnife
import com.google.android.material.textfield.TextInputLayout
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONException
import org.lspl.mobile.R
import org.lspl.mobile.api.SelfServiceInterceptor
import java.io.IOException
import java.util.concurrent.TimeUnit

/* renamed from: org.techsavanna.enkasacco.ui.fragments.CreateGuarantorFragmentOne */
class CreateGuarantorFragmentOne() : Fragment() {
    private val clientId: String? = null
    var phone: TextInputLayout? = null

    /* access modifiers changed from: package-private */
    fun submit() {
        if ((phone!!.editText!!.text.toString().trim { it <= ' ' } == "")) {
            phone!!.error = "Guarantor's phone number is required"
        } else {
            loadClient()
        }
    }

    override fun onCreateView(
        layoutInflater: LayoutInflater,
        viewGroup: ViewGroup?,
        bundle: Bundle?
    ): View? {
        val inflate =
            layoutInflater.inflate(R.layout.fragment_create_guarantor_one, viewGroup, false)
        ButterKnife.bind((this as Any), inflate)
        return inflate
    }

    fun loadClient() {
        if (activity != null) {
            val progressDialog = ProgressDialog(activity)
            progressDialog.setCanceledOnTouchOutside(false)
            progressDialog.setMessage("Fetching Details...")
            progressDialog.show()
            val build = OkHttpClient().newBuilder().connectTimeout(40, TimeUnit.SECONDS)
                .writeTimeout(40, TimeUnit.SECONDS).readTimeout(40, TimeUnit.SECONDS).build()
            val builder = Request.Builder()
            build.newCall(builder.url("https://techsavanna.net:8443/enkaapis/public/api/clients/" + phone!!.editText!!
                .text.toString().trim { it <= ' ' }).method("GET", null as RequestBody?)
                .addHeader("Content-Type", "application/json").addHeader(
                SelfServiceInterceptor.HEADER_AUTH,
                "Bearer Q6Od9AhFDWeJGcimeD8oQ4yaw9DzWv5POJ7nktdxFiloJCzbxM"
            ).addHeader("Content-Type", "text/plain")
                .addHeader("Cookie", "PHPSESSID=1qn6rbmkose1pfrcnagfu79i83").build()
            ).enqueue(object : Callback {
                @SuppressLint("UseRequireInsteadOfGet")
                override fun onFailure(call: Call, iOException: IOException) {
                    if (this@CreateGuarantorFragmentOne.activity != null) {
                        this@CreateGuarantorFragmentOne.activity!!.runOnUiThread(Runnable {
                            progressDialog.dismiss()
                            Toast.makeText(
                                this@CreateGuarantorFragmentOne.activity,
                                "Request failed.",
                                Toast.LENGTH_SHORT
                            ).show()
                        })
                    }
                }

                @SuppressLint("UseRequireInsteadOfGet")
                @Throws(IOException::class)
                override fun onResponse(call: Call, response: Response) {
                    if (this@CreateGuarantorFragmentOne.activity != null) {
                        progressDialog.dismiss()
                        this@CreateGuarantorFragmentOne.activity!!.runOnUiThread(object : Runnable {
                            @SuppressLint("UseRequireInsteadOfGet")
                            override fun run() {
                                try {
                                    val jSONObject =
                                        JSONArray(response.body()!!.string()).getJSONObject(0)
                                    val bundle = Bundle()
                                    val beginTransaction =
                                        this@CreateGuarantorFragmentOne.fragmentManager!!
                                            .beginTransaction()
                                    bundle.putString("id", jSONObject.getString("id"))
                                    bundle.putString("name", jSONObject.getString("display_name"))
                                    bundle.putString("id_no", jSONObject.getString("external_id"))
                                    bundle.putString("phone", jSONObject.getString("mobile_no"))
                                    val createGuarantorFragmentTwo = CreateGuarantorFragmentTwo()
                                    createGuarantorFragmentTwo.arguments = bundle
                                    beginTransaction.replace(
                                        R.id.container,
                                        createGuarantorFragmentTwo
                                    )
                                    beginTransaction.commit()
                                    val printStream = System.out
                                    printStream.println("respppppp$jSONObject")
                                } catch (e: JSONException) {
                                    Toast.makeText(
                                        this@CreateGuarantorFragmentOne.activity,
                                        "Request failed.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    e.printStackTrace()
                                } catch (e: IOException) {
                                    Toast.makeText(
                                        this@CreateGuarantorFragmentOne.activity,
                                        "Request failed.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    e.printStackTrace()
                                }
                            }
                        })
                    }
                }
            })
        }
    }
}