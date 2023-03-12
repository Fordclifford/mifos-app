package org.lspl.mobile.ui.fragments.guarantors

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
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
import org.lspl.mobile.models.GuarantorLoanModel
import org.lspl.mobile.ui.activities.CreateGuarantorActivity
import org.lspl.mobile.ui.activities.LoanApplicationActivity
import org.lspl.mobile.ui.adapters.GuarantorLoanAdapter
import org.lspl.mobile.ui.enums.LoanState
import org.lspl.mobile.ui.fragments.LoanApplicationFragment
import org.lspl.mobile.utils.Constants
import java.io.IOException
import java.util.concurrent.TimeUnit

/* renamed from: org.techsavanna.enkasacco.ui.fragments.GuarantorLoanFragment */
class GuarantorLoanFragment() : Fragment() {
    /* access modifiers changed from: private */
    var adapter = GuarantorLoanAdapter(activity, ArrayList<GuarantorLoanModel>())
    var button: AppCompatButton? = null

    /* access modifiers changed from: private */
    var clientId: String? = null
    var floatingActionButton: FloatingActionButton? = null
    var guarantorlist = ArrayList<Any?>()

    /* access modifiers changed from: private */
    var linearLayout: LinearLayout? = null
    var loanId: String? = ""
    var recyclerView: RecyclerView? = null
    var refreshLayout: SwipeRefreshLayout? = null
    override fun onCreateView(
        layoutInflater: LayoutInflater,
        viewGroup: ViewGroup?,
        bundle: Bundle?
    ): View? {
        val inflate = layoutInflater.inflate(R.layout.fragment_loan_guarantor, viewGroup, false)
        if (activity != null) {
            loanId = requireActivity().getSharedPreferences("LoanIDPreference", 0)
                .getString(Constants.LOAN_ID, "")
        }
        val swipeRefreshLayout = inflate.findViewById<View>(R.id.SwipeRefresh) as SwipeRefreshLayout
        refreshLayout = swipeRefreshLayout
        swipeRefreshLayout.setColorSchemeResources(
            R.color.blue_light,
            R.color.green_light,
            R.color.orange_light,
            R.color.red_light
        )
        refreshLayout!!.setOnRefreshListener { displayGuarantor() }
        if (activity != null) {
            clientId =
                requireActivity().getSharedPreferences("ClientIDPreference", 0).getString("clientId", "")
            val printStream = System.out
            printStream.println("client id" + clientId)
        }
        linearLayout = inflate.findViewById<View>(R.id.empty_view) as LinearLayout
        floatingActionButton = inflate.findViewById<View>(R.id.AddGuarantor) as FloatingActionButton
        recyclerView = inflate.findViewById<View>(R.id.Recyclerview) as RecyclerView
        floatingActionButton!!.setOnClickListener {
            this@GuarantorLoanFragment.startActivity(
                Intent(
                    this@GuarantorLoanFragment.activity,
                    CreateGuarantorActivity::class.java
                )
            )
        }
        val appCompatButton = inflate.findViewById<AppCompatButton>(R.id.btn_loan_submit)
        button = appCompatButton
        appCompatButton.setOnClickListener { view: View? ->
            val selectedItems: List<GuarantorLoanModel> = adapter.getSelectedItems()
            if (selectedItems.size < 3) {
                Toast.makeText(
                    getActivity(),
                    "Please select at least 3 guarantors",
                    Toast.LENGTH_LONG
                ).show()
            } else if (getActivity() != null) {
                val edit: SharedPreferences.Editor = requireActivity()
                    .getSharedPreferences("GuarantorsArray", 0).edit()
                edit.putString("guarantorlist", Gson().toJson(selectedItems as Any?))
                edit.apply()
                requireFragmentManager().beginTransaction()
                    .replace(R.id.container, LoanApplicationFragment.newInstance(LoanState.CREATE))
                    .commit()
            }
        }
        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.layoutManager = LinearLayoutManager(activity)
        return inflate
    }

    /* access modifiers changed from: private */
    fun displayGuarantor() {
        refreshLayout!!.isRefreshing = true
        guarantorlist.clear()
        val jSONObject = JSONObject()
        try {
            jSONObject.put("clientId", clientId)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        OkHttpClient().newBuilder().connectTimeout(40, TimeUnit.SECONDS)
            .writeTimeout(40, TimeUnit.SECONDS).readTimeout(40, TimeUnit.SECONDS).build().newCall(
            Request.Builder().url("https://techsavanna.net:8443/enkaapis/public/api/guarantors")
                .method(
                    "POST", RequestBody.create(
                        MediaType.parse("application/json,text/plain"), jSONObject.toString()
                    )
                ).addHeader("Content-Type", "application/json").addHeader(
                SelfServiceInterceptor.HEADER_AUTH,
                "Bearer Q6Od9AhFDWeJGcimeD8oQ4yaw9DzWv5POJ7nktdxFiloJCzbxM"
            ).addHeader("Content-Type", "text/plain")
                .addHeader("Cookie", "PHPSESSID=1qn6rbmkose1pfrcnagfu79i83").build()
        ).enqueue(object : Callback {
            @SuppressLint("UseRequireInsteadOfGet")
            override fun onFailure(call: Call, iOException: IOException) {
                if (this@GuarantorLoanFragment.activity != null) {
                    this@GuarantorLoanFragment.activity!!.runOnUiThread {
                        refreshLayout!!.isRefreshing = false
                        Toast.makeText(
                            this@GuarantorLoanFragment.activity,
                            "Guarantors request failed",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            @SuppressLint("UseRequireInsteadOfGet")
            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                if (this@GuarantorLoanFragment.activity != null) {
                    this@GuarantorLoanFragment.activity!!.runOnUiThread(Runnable
                    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0146, code lost:
                       r0 = move-exception;
                    */
                    /* JADX WARNING: Code restructure failed: missing block: B:22:0x014f, code lost:
                                               if (r1.this$1.this$0.getActivity() != null) goto L_0x0151;
                                            */
                    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0151, code lost:
                                               android.widget.Toast.makeText(r1.this$1.this$0.getActivity(), "Guarantors request failed", 0).show();
                                            */
                    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0160, code lost:
                                               r0.printStackTrace();
                                            */
                    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0164, code lost:
                                               r0 = move-exception;
                                            */
                    /* JADX WARNING: Code restructure failed: missing block: B:27:0x016d, code lost:
                                               if (r1.this$1.this$0.getActivity() != null) goto L_0x016f;
                                            */
                    /* JADX WARNING: Code restructure failed: missing block: B:28:0x016f, code lost:
                                               android.widget.Toast.makeText(r1.this$1.this$0.getActivity(), "Guarantors request failed", 0).show();
                                            */
                    /* JADX WARNING: Code restructure failed: missing block: B:29:0x017e, code lost:
                                               r0.printStackTrace();
                                            */
                    /* JADX WARNING: Code restructure failed: missing block: B:39:?, code lost:
                                               return;
                                            */
                    /* JADX WARNING: Code restructure failed: missing block: B:41:?, code lost:
                                               return;
                                            */
                    /* JADX WARNING: Failed to process nested try/catch */ /* JADX WARNING: Removed duplicated region for block: B:25:0x0164 A[ExcHandler: IOException (r0v2 'e' java.io.IOException A[CUSTOM_DECLARE]), Splitter:B:1:0x0010] */ /* Code decompiled incorrectly, please refer to instructions dump. */
                    { /*
                                r18 = this;
                                r1 = r18
                                java.lang.String r0 = "success"
                                java.lang.String r2 = "Guarantors request failed"
                                org.techsavanna.enkasacco.ui.fragments.GuarantorLoanFragment$4 r3 = org.techsavanna.enkasacco.p013ui.fragments.GuarantorLoanFragment.C13504.this
                                org.techsavanna.enkasacco.ui.fragments.GuarantorLoanFragment r3 = org.techsavanna.enkasacco.p013ui.fragments.GuarantorLoanFragment.this
                                android.support.v4.widget.SwipeRefreshLayout r3 = r3.refreshLayout
                                r4 = 0
                                r3.setRefreshing(r4)
                                org.json.JSONObject r3 = new org.json.JSONObject     // Catch:{ JSONException -> 0x0182, IOException -> 0x0164 }
                                okhttp3.Response r5 = r3     // Catch:{ JSONException -> 0x0182, IOException -> 0x0164 }
                                okhttp3.ResponseBody r5 = r5.body()     // Catch:{ JSONException -> 0x0182, IOException -> 0x0164 }
                                java.lang.String r5 = r5.string()     // Catch:{ JSONException -> 0x0182, IOException -> 0x0164 }
                                r3.<init>(r5)     // Catch:{ JSONException -> 0x0182, IOException -> 0x0164 }
                                java.io.PrintStream r5 = java.lang.System.out     // Catch:{ JSONException -> 0x0182, IOException -> 0x0164 }
                                java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ JSONException -> 0x0182, IOException -> 0x0164 }
                                r6.<init>()     // Catch:{ JSONException -> 0x0182, IOException -> 0x0164 }
                                java.lang.String r7 = "objectvv"
                                r6.append(r7)     // Catch:{ JSONException -> 0x0182, IOException -> 0x0164 }
                                r6.append(r3)     // Catch:{ JSONException -> 0x0182, IOException -> 0x0164 }
                                java.lang.String r6 = r6.toString()     // Catch:{ JSONException -> 0x0182, IOException -> 0x0164 }
                                r5.println(r6)     // Catch:{ JSONException -> 0x0182, IOException -> 0x0164 }
                                java.lang.String r5 = r3.getString(r0)     // Catch:{ JSONException -> 0x0146, IOException -> 0x0164 }
                                java.lang.String r6 = "1"
                                boolean r5 = r5.equals(r6)     // Catch:{ JSONException -> 0x0146, IOException -> 0x0164 }
                                if (r5 == 0) goto L_0x011a
                                org.json.JSONArray r5 = new org.json.JSONArray     // Catch:{ JSONException -> 0x0146, IOException -> 0x0164 }
                                java.lang.String r6 = "data"
                                java.lang.String r6 = r3.getString(r6)     // Catch:{ JSONException -> 0x0146, IOException -> 0x0164 }
                                r5.<init>(r6)     // Catch:{ JSONException -> 0x0146, IOException -> 0x0164 }
                                r6 = 0
                            L_0x004d:
                                int r7 = r5.length()     // Catch:{ JSONException -> 0x0146, IOException -> 0x0164 }
                                if (r6 >= r7) goto L_0x009b
                                org.json.JSONObject r7 = r5.getJSONObject(r6)     // Catch:{ JSONException -> 0x0146, IOException -> 0x0164 }
                                java.lang.String r8 = "id"
                                java.lang.String r16 = r7.getString(r8)     // Catch:{ JSONException -> 0x0146, IOException -> 0x0164 }
                                java.lang.String r8 = "firstname"
                                java.lang.String r12 = r7.getString(r8)     // Catch:{ JSONException -> 0x0146, IOException -> 0x0164 }
                                java.lang.String r8 = "lastname"
                                java.lang.String r13 = r7.getString(r8)     // Catch:{ JSONException -> 0x0146, IOException -> 0x0164 }
                                java.lang.String r8 = "external_id"
                                java.lang.String r11 = r7.getString(r8)     // Catch:{ JSONException -> 0x0146, IOException -> 0x0164 }
                                java.lang.String r8 = "mobile_no"
                                java.lang.String r10 = r7.getString(r8)     // Catch:{ JSONException -> 0x0146, IOException -> 0x0164 }
                                java.lang.String r8 = "entity_id"
                                java.lang.String r17 = r7.getString(r8)     // Catch:{ JSONException -> 0x0146, IOException -> 0x0164 }
                                org.techsavanna.enkasacco.models.GuarantorLoanModel r7 = new org.techsavanna.enkasacco.models.GuarantorLoanModel     // Catch:{ JSONException -> 0x0146, IOException -> 0x0164 }
                                org.techsavanna.enkasacco.ui.fragments.GuarantorLoanFragment$4 r8 = org.techsavanna.enkasacco.p013ui.fragments.GuarantorLoanFragment.C13504.this     // Catch:{ JSONException -> 0x0146, IOException -> 0x0164 }
                                org.techsavanna.enkasacco.ui.fragments.GuarantorLoanFragment r8 = org.techsavanna.enkasacco.p013ui.fragments.GuarantorLoanFragment.this     // Catch:{ JSONException -> 0x0146, IOException -> 0x0164 }
                                java.lang.String r14 = r8.clientId     // Catch:{ JSONException -> 0x0146, IOException -> 0x0164 }
                                org.techsavanna.enkasacco.ui.fragments.GuarantorLoanFragment$4 r8 = org.techsavanna.enkasacco.p013ui.fragments.GuarantorLoanFragment.C13504.this     // Catch:{ JSONException -> 0x0146, IOException -> 0x0164 }
                                org.techsavanna.enkasacco.ui.fragments.GuarantorLoanFragment r8 = org.techsavanna.enkasacco.p013ui.fragments.GuarantorLoanFragment.this     // Catch:{ JSONException -> 0x0146, IOException -> 0x0164 }
                                java.lang.String r15 = r8.loanId     // Catch:{ JSONException -> 0x0146, IOException -> 0x0164 }
                                r9 = r7
                                r9.<init>(r10, r11, r12, r13, r14, r15, r16, r17)     // Catch:{ JSONException -> 0x0146, IOException -> 0x0164 }
                                org.techsavanna.enkasacco.ui.fragments.GuarantorLoanFragment$4 r8 = org.techsavanna.enkasacco.p013ui.fragments.GuarantorLoanFragment.C13504.this     // Catch:{ JSONException -> 0x0146, IOException -> 0x0164 }
                                org.techsavanna.enkasacco.ui.fragments.GuarantorLoanFragment r8 = org.techsavanna.enkasacco.p013ui.fragments.GuarantorLoanFragment.this     // Catch:{ JSONException -> 0x0146, IOException -> 0x0164 }
                                java.util.List<org.techsavanna.enkasacco.models.GuarantorLoanModel> r8 = r8.guarantorlist     // Catch:{ JSONException -> 0x0146, IOException -> 0x0164 }
                                r8.add(r7)     // Catch:{ JSONException -> 0x0146, IOException -> 0x0164 }
                                int r6 = r6 + 1
                                goto L_0x004d
                            L_0x009b:
                                org.techsavanna.enkasacco.ui.fragments.GuarantorLoanFragment$4 r5 = org.techsavanna.enkasacco.p013ui.fragments.GuarantorLoanFragment.C13504.this     // Catch:{ JSONException -> 0x0146, IOException -> 0x0164 }
                                org.techsavanna.enkasacco.ui.fragments.GuarantorLoanFragment r5 = org.techsavanna.enkasacco.p013ui.fragments.GuarantorLoanFragment.this     // Catch:{ JSONException -> 0x0146, IOException -> 0x0164 }
                                org.techsavanna.enkasacco.ui.adapters.GuarantorLoanAdapter r6 = new org.techsavanna.enkasacco.ui.adapters.GuarantorLoanAdapter     // Catch:{ JSONException -> 0x0146, IOException -> 0x0164 }
                                org.techsavanna.enkasacco.ui.fragments.GuarantorLoanFragment$4 r7 = org.techsavanna.enkasacco.p013ui.fragments.GuarantorLoanFragment.C13504.this     // Catch:{ JSONException -> 0x0146, IOException -> 0x0164 }
                                org.techsavanna.enkasacco.ui.fragments.GuarantorLoanFragment r7 = org.techsavanna.enkasacco.p013ui.fragments.GuarantorLoanFragment.this     // Catch:{ JSONException -> 0x0146, IOException -> 0x0164 }
                                android.support.v4.app.FragmentActivity r7 = r7.getActivity()     // Catch:{ JSONException -> 0x0146, IOException -> 0x0164 }
                                org.techsavanna.enkasacco.ui.fragments.GuarantorLoanFragment$4 r8 = org.techsavanna.enkasacco.p013ui.fragments.GuarantorLoanFragment.C13504.this     // Catch:{ JSONException -> 0x0146, IOException -> 0x0164 }
                                org.techsavanna.enkasacco.ui.fragments.GuarantorLoanFragment r8 = org.techsavanna.enkasacco.p013ui.fragments.GuarantorLoanFragment.this     // Catch:{ JSONException -> 0x0146, IOException -> 0x0164 }
                                java.util.List<org.techsavanna.enkasacco.models.GuarantorLoanModel> r8 = r8.guarantorlist     // Catch:{ JSONException -> 0x0146, IOException -> 0x0164 }
                                r6.<init>(r7, r8)     // Catch:{ JSONException -> 0x0146, IOException -> 0x0164 }
                                org.techsavanna.enkasacco.p013ui.adapters.GuarantorLoanAdapter unused = r5.adapter = r6     // Catch:{ JSONException -> 0x0146, IOException -> 0x0164 }
                                org.techsavanna.enkasacco.ui.fragments.GuarantorLoanFragment$4 r5 = org.techsavanna.enkasacco.p013ui.fragments.GuarantorLoanFragment.C13504.this     // Catch:{ JSONException -> 0x0146, IOException -> 0x0164 }
                                org.techsavanna.enkasacco.ui.fragments.GuarantorLoanFragment r5 = org.techsavanna.enkasacco.p013ui.fragments.GuarantorLoanFragment.this     // Catch:{ JSONException -> 0x0146, IOException -> 0x0164 }
                                org.techsavanna.enkasacco.ui.adapters.GuarantorLoanAdapter r5 = r5.adapter     // Catch:{ JSONException -> 0x0146, IOException -> 0x0164 }
                                r5.notifyDataSetChanged()     // Catch:{ JSONException -> 0x0146, IOException -> 0x0164 }
                                org.techsavanna.enkasacco.ui.fragments.GuarantorLoanFragment$4 r5 = org.techsavanna.enkasacco.p013ui.fragments.GuarantorLoanFragment.C13504.this     // Catch:{ JSONException -> 0x0146, IOException -> 0x0164 }
                                org.techsavanna.enkasacco.ui.fragments.GuarantorLoanFragment r5 = org.techsavanna.enkasacco.p013ui.fragments.GuarantorLoanFragment.this     // Catch:{ JSONException -> 0x0146, IOException -> 0x0164 }
                                android.support.v7.widget.RecyclerView r5 = r5.recyclerView     // Catch:{ JSONException -> 0x0146, IOException -> 0x0164 }
                                org.techsavanna.enkasacco.ui.fragments.GuarantorLoanFragment$4 r6 = org.techsavanna.enkasacco.p013ui.fragments.GuarantorLoanFragment.C13504.this     // Catch:{ JSONException -> 0x0146, IOException -> 0x0164 }
                                org.techsavanna.enkasacco.ui.fragments.GuarantorLoanFragment r6 = org.techsavanna.enkasacco.p013ui.fragments.GuarantorLoanFragment.this     // Catch:{ JSONException -> 0x0146, IOException -> 0x0164 }
                                org.techsavanna.enkasacco.ui.adapters.GuarantorLoanAdapter r6 = r6.adapter     // Catch:{ JSONException -> 0x0146, IOException -> 0x0164 }
                                r5.setAdapter(r6)     // Catch:{ JSONException -> 0x0146, IOException -> 0x0164 }
                                org.techsavanna.enkasacco.ui.fragments.GuarantorLoanFragment$4 r5 = org.techsavanna.enkasacco.p013ui.fragments.GuarantorLoanFragment.C13504.this     // Catch:{ JSONException -> 0x0146, IOException -> 0x0164 }
                                org.techsavanna.enkasacco.ui.fragments.GuarantorLoanFragment r5 = org.techsavanna.enkasacco.p013ui.fragments.GuarantorLoanFragment.this     // Catch:{ JSONException -> 0x0146, IOException -> 0x0164 }
                                java.util.List<org.techsavanna.enkasacco.models.GuarantorLoanModel> r5 = r5.guarantorlist     // Catch:{ JSONException -> 0x0146, IOException -> 0x0164 }
                                int r5 = r5.size()     // Catch:{ JSONException -> 0x0146, IOException -> 0x0164 }
                                r6 = 8
                                if (r5 != 0) goto L_0x00fd
                                org.techsavanna.enkasacco.ui.fragments.GuarantorLoanFragment$4 r5 = org.techsavanna.enkasacco.p013ui.fragments.GuarantorLoanFragment.C13504.this     // Catch:{ JSONException -> 0x0146, IOException -> 0x0164 }
                                org.techsavanna.enkasacco.ui.fragments.GuarantorLoanFragment r5 = org.techsavanna.enkasacco.p013ui.fragments.GuarantorLoanFragment.this     // Catch:{ JSONException -> 0x0146, IOException -> 0x0164 }
                                android.widget.LinearLayout r5 = r5.linearLayout     // Catch:{ JSONException -> 0x0146, IOException -> 0x0164 }
                                r5.setVisibility(r4)     // Catch:{ JSONException -> 0x0146, IOException -> 0x0164 }
                                org.techsavanna.enkasacco.ui.fragments.GuarantorLoanFragment$4 r5 = org.techsavanna.enkasacco.p013ui.fragments.GuarantorLoanFragment.C13504.this     // Catch:{ JSONException -> 0x0146, IOException -> 0x0164 }
                                org.techsavanna.enkasacco.ui.fragments.GuarantorLoanFragment r5 = org.techsavanna.enkasacco.p013ui.fragments.GuarantorLoanFragment.this     // Catch:{ JSONException -> 0x0146, IOException -> 0x0164 }
                                android.support.v7.widget.AppCompatButton r5 = r5.button     // Catch:{ JSONException -> 0x0146, IOException -> 0x0164 }
                                r5.setVisibility(r6)     // Catch:{ JSONException -> 0x0146, IOException -> 0x0164 }
                                org.techsavanna.enkasacco.ui.fragments.GuarantorLoanFragment$4 r5 = org.techsavanna.enkasacco.p013ui.fragments.GuarantorLoanFragment.C13504.this     // Catch:{ JSONException -> 0x0146, IOException -> 0x0164 }
                                org.techsavanna.enkasacco.ui.fragments.GuarantorLoanFragment r5 = org.techsavanna.enkasacco.p013ui.fragments.GuarantorLoanFragment.this     // Catch:{ JSONException -> 0x0146, IOException -> 0x0164 }
                                android.support.design.widget.FloatingActionButton r5 = r5.floatingActionButton     // Catch:{ JSONException -> 0x0146, IOException -> 0x0164 }
                                r5.setVisibility(r4)     // Catch:{ JSONException -> 0x0146, IOException -> 0x0164 }
                                goto L_0x011a
                            L_0x00fd:
                                org.techsavanna.enkasacco.ui.fragments.GuarantorLoanFragment$4 r5 = org.techsavanna.enkasacco.p013ui.fragments.GuarantorLoanFragment.C13504.this     // Catch:{ JSONException -> 0x0146, IOException -> 0x0164 }
                                org.techsavanna.enkasacco.ui.fragments.GuarantorLoanFragment r5 = org.techsavanna.enkasacco.p013ui.fragments.GuarantorLoanFragment.this     // Catch:{ JSONException -> 0x0146, IOException -> 0x0164 }
                                android.widget.LinearLayout r5 = r5.linearLayout     // Catch:{ JSONException -> 0x0146, IOException -> 0x0164 }
                                r5.setVisibility(r6)     // Catch:{ JSONException -> 0x0146, IOException -> 0x0164 }
                                org.techsavanna.enkasacco.ui.fragments.GuarantorLoanFragment$4 r5 = org.techsavanna.enkasacco.p013ui.fragments.GuarantorLoanFragment.C13504.this     // Catch:{ JSONException -> 0x0146, IOException -> 0x0164 }
                                org.techsavanna.enkasacco.ui.fragments.GuarantorLoanFragment r5 = org.techsavanna.enkasacco.p013ui.fragments.GuarantorLoanFragment.this     // Catch:{ JSONException -> 0x0146, IOException -> 0x0164 }
                                android.support.v7.widget.AppCompatButton r5 = r5.button     // Catch:{ JSONException -> 0x0146, IOException -> 0x0164 }
                                r5.setVisibility(r4)     // Catch:{ JSONException -> 0x0146, IOException -> 0x0164 }
                                org.techsavanna.enkasacco.ui.fragments.GuarantorLoanFragment$4 r5 = org.techsavanna.enkasacco.p013ui.fragments.GuarantorLoanFragment.C13504.this     // Catch:{ JSONException -> 0x0146, IOException -> 0x0164 }
                                org.techsavanna.enkasacco.ui.fragments.GuarantorLoanFragment r5 = org.techsavanna.enkasacco.p013ui.fragments.GuarantorLoanFragment.this     // Catch:{ JSONException -> 0x0146, IOException -> 0x0164 }
                                android.support.design.widget.FloatingActionButton r5 = r5.floatingActionButton     // Catch:{ JSONException -> 0x0146, IOException -> 0x0164 }
                                r5.setVisibility(r6)     // Catch:{ JSONException -> 0x0146, IOException -> 0x0164 }
                            L_0x011a:
                                java.lang.String r0 = r3.getString(r0)     // Catch:{ JSONException -> 0x0146, IOException -> 0x0164 }
                                java.lang.String r5 = "0"
                                boolean r0 = r0.equals(r5)     // Catch:{ JSONException -> 0x0146, IOException -> 0x0164 }
                                if (r0 == 0) goto L_0x019f
                                org.techsavanna.enkasacco.ui.fragments.GuarantorLoanFragment$4 r0 = org.techsavanna.enkasacco.p013ui.fragments.GuarantorLoanFragment.C13504.this     // Catch:{ JSONException -> 0x0146, IOException -> 0x0164 }
                                org.techsavanna.enkasacco.ui.fragments.GuarantorLoanFragment r0 = org.techsavanna.enkasacco.p013ui.fragments.GuarantorLoanFragment.this     // Catch:{ JSONException -> 0x0146, IOException -> 0x0164 }
                                android.support.v4.app.FragmentActivity r0 = r0.getActivity()     // Catch:{ JSONException -> 0x0146, IOException -> 0x0164 }
                                if (r0 == 0) goto L_0x019f
                                org.techsavanna.enkasacco.ui.fragments.GuarantorLoanFragment$4 r0 = org.techsavanna.enkasacco.p013ui.fragments.GuarantorLoanFragment.C13504.this     // Catch:{ JSONException -> 0x0146, IOException -> 0x0164 }
                                org.techsavanna.enkasacco.ui.fragments.GuarantorLoanFragment r0 = org.techsavanna.enkasacco.p013ui.fragments.GuarantorLoanFragment.this     // Catch:{ JSONException -> 0x0146, IOException -> 0x0164 }
                                android.support.v4.app.FragmentActivity r0 = r0.getActivity()     // Catch:{ JSONException -> 0x0146, IOException -> 0x0164 }
                                java.lang.String r5 = "message"
                                java.lang.String r3 = r3.getString(r5)     // Catch:{ JSONException -> 0x0146, IOException -> 0x0164 }
                                android.widget.Toast r0 = android.widget.Toast.makeText(r0, r3, r4)     // Catch:{ JSONException -> 0x0146, IOException -> 0x0164 }
                                r0.show()     // Catch:{ JSONException -> 0x0146, IOException -> 0x0164 }
                                goto L_0x019f
                            L_0x0146:
                                r0 = move-exception
                                org.techsavanna.enkasacco.ui.fragments.GuarantorLoanFragment$4 r3 = org.techsavanna.enkasacco.p013ui.fragments.GuarantorLoanFragment.C13504.this     // Catch:{ JSONException -> 0x0182, IOException -> 0x0164 }
                                org.techsavanna.enkasacco.ui.fragments.GuarantorLoanFragment r3 = org.techsavanna.enkasacco.p013ui.fragments.GuarantorLoanFragment.this     // Catch:{ JSONException -> 0x0182, IOException -> 0x0164 }
                                android.support.v4.app.FragmentActivity r3 = r3.getActivity()     // Catch:{ JSONException -> 0x0182, IOException -> 0x0164 }
                                if (r3 == 0) goto L_0x0160
                                org.techsavanna.enkasacco.ui.fragments.GuarantorLoanFragment$4 r3 = org.techsavanna.enkasacco.p013ui.fragments.GuarantorLoanFragment.C13504.this     // Catch:{ JSONException -> 0x0182, IOException -> 0x0164 }
                                org.techsavanna.enkasacco.ui.fragments.GuarantorLoanFragment r3 = org.techsavanna.enkasacco.p013ui.fragments.GuarantorLoanFragment.this     // Catch:{ JSONException -> 0x0182, IOException -> 0x0164 }
                                android.support.v4.app.FragmentActivity r3 = r3.getActivity()     // Catch:{ JSONException -> 0x0182, IOException -> 0x0164 }
                                android.widget.Toast r3 = android.widget.Toast.makeText(r3, r2, r4)     // Catch:{ JSONException -> 0x0182, IOException -> 0x0164 }
                                r3.show()     // Catch:{ JSONException -> 0x0182, IOException -> 0x0164 }
                            L_0x0160:
                                r0.printStackTrace()     // Catch:{ JSONException -> 0x0182, IOException -> 0x0164 }
                                goto L_0x019f
                            L_0x0164:
                                r0 = move-exception
                                org.techsavanna.enkasacco.ui.fragments.GuarantorLoanFragment$4 r3 = org.techsavanna.enkasacco.p013ui.fragments.GuarantorLoanFragment.C13504.this
                                org.techsavanna.enkasacco.ui.fragments.GuarantorLoanFragment r3 = org.techsavanna.enkasacco.p013ui.fragments.GuarantorLoanFragment.this
                                android.support.v4.app.FragmentActivity r3 = r3.getActivity()
                                if (r3 == 0) goto L_0x017e
                                org.techsavanna.enkasacco.ui.fragments.GuarantorLoanFragment$4 r3 = org.techsavanna.enkasacco.p013ui.fragments.GuarantorLoanFragment.C13504.this
                                org.techsavanna.enkasacco.ui.fragments.GuarantorLoanFragment r3 = org.techsavanna.enkasacco.p013ui.fragments.GuarantorLoanFragment.this
                                android.support.v4.app.FragmentActivity r3 = r3.getActivity()
                                android.widget.Toast r2 = android.widget.Toast.makeText(r3, r2, r4)
                                r2.show()
                            L_0x017e:
                                r0.printStackTrace()
                                goto L_0x019f
                            L_0x0182:
                                r0 = move-exception
                                org.techsavanna.enkasacco.ui.fragments.GuarantorLoanFragment$4 r3 = org.techsavanna.enkasacco.p013ui.fragments.GuarantorLoanFragment.C13504.this
                                org.techsavanna.enkasacco.ui.fragments.GuarantorLoanFragment r3 = org.techsavanna.enkasacco.p013ui.fragments.GuarantorLoanFragment.this
                                android.support.v4.app.FragmentActivity r3 = r3.getActivity()
                                if (r3 == 0) goto L_0x019c
                                org.techsavanna.enkasacco.ui.fragments.GuarantorLoanFragment$4 r3 = org.techsavanna.enkasacco.p013ui.fragments.GuarantorLoanFragment.C13504.this
                                org.techsavanna.enkasacco.ui.fragments.GuarantorLoanFragment r3 = org.techsavanna.enkasacco.p013ui.fragments.GuarantorLoanFragment.this
                                android.support.v4.app.FragmentActivity r3 = r3.getActivity()
                                android.widget.Toast r2 = android.widget.Toast.makeText(r3, r2, r4)
                                r2.show()
                            L_0x019c:
                                r0.printStackTrace()
                            L_0x019f:
                                return
                            */
                        throw UnsupportedOperationException("Method not decompiled: org.techsavanna.enkasacco.p013ui.fragments.GuarantorLoanFragment.C13504.C13522.run():void")
                    })
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        if (activity != null) {
            displayGuarantor()
            (activity as LoanApplicationActivity?)!!.setActionBarTitle("Select Guarantors")
        }
    }
}