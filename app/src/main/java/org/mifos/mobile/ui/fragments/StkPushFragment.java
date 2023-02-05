package org.mifos.mobile.ui.fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.mifos.mobile.R;
import org.mifos.mobile.api.SelfServiceInterceptor;
import org.mifos.mobile.ui.activities.HomeActivity;
import org.mifos.mobile.ui.fragments.base.BaseFragment;
import org.mifos.mobile.utils.Constants;

import java.io.IOException;
import java.io.PrintStream;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/* renamed from: org.techsavanna.enkasacco.ui.fragments.StkPushFragment */
public class StkPushFragment extends BaseFragment {
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    private static final String confirmUrl = "https://techsavanna.net:8181/enka/api/confirmstk.php";
    private static final String transStatus = "https://techsavanna.net:3000/tech/api/mpesa/stkpushstatus";
    private static final String urlAdress = "https://techsavanna.net:3000/tech/api/mpesa/stkpush";
    private String accountId;
    TextView accountidtxt;
    EditText amountedt;
    String link = "https://techsavanna.net:3000/tech/api/auth/signin";
    private Double loanBal;
    Button makepaymentbtn;
    EditText phoneedt;
    ProgressDialog progress;
    private ProgressDialog progressDialog;
    View rootView;

    public static StkPushFragment newInstance(String str, double d) {
        StkPushFragment stkPushFragment = new StkPushFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.ACCOUNT_ID, str);
        bundle.putDouble(Constants.LOANBAL, d);
        stkPushFragment.setArguments(bundle);
        return stkPushFragment;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (getArguments() != null) {
            this.accountId = getArguments().getString(Constants.ACCOUNT_ID);
            this.loanBal = getArguments().getDouble(Constants.LOANBAL);
        }
        setHasOptionsMenu(true);
    }

    @SuppressLint("SetTextI18n")
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.rootView = layoutInflater.inflate(R.layout.fragment_stk_push, viewGroup, false);
        if (getActivity() != null) {
            this.amountedt = (EditText) this.rootView.findViewById(R.id.amount);
            this.phoneedt = (EditText) this.rootView.findViewById(R.id.phoneno);
            this.accountidtxt = (TextView) this.rootView.findViewById(R.id.accounttxt);
            this.makepaymentbtn = (Button) this.rootView.findViewById(R.id.paymentbtn);
            TextView textView = this.accountidtxt;
            textView.setText(getString(R.string.accounttobepaidfor) + " " + this.accountId);
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefsFile", 0);
            String string = sharedPreferences.getString("phonenoIndividual", (String) null);
            String string2 = sharedPreferences.getString("phonenoIndividual", getString(R.string.enterphoneno1));
            PrintStream printStream = System.out;
            printStream.println("Phone no " + string);
            this.phoneedt.setText(string2);
            EditText editText = this.amountedt;
            editText.setText("" + this.loanBal.intValue());
            final String replaceAll = this.accountId.replaceAll("[^a-zA-Z]+", "");
            PrintStream printStream2 = System.out;
            printStream2.println("string only" + replaceAll);
            this.makepaymentbtn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    if (StkPushFragment.this.phoneedt.getText().toString().isEmpty() || StkPushFragment.this.amountedt.getText().toString().isEmpty()) {
                        Toast.makeText(StkPushFragment.this.getActivity(), "Please fill all details", Toast.LENGTH_SHORT).show();
                    } else if (replaceAll.equals("R") && Long.parseLong(StkPushFragment.this.amountedt.getText().toString()) < 199) {
                        Toast.makeText(StkPushFragment.this.getActivity(), "Amount cannot be less 200", Toast.LENGTH_SHORT).show();
                    } else if (StkPushFragment.this.phoneedt.getText().toString().length() < 8) {
                        Toast.makeText(StkPushFragment.this.getActivity(), "Phone number cannot be less than 8 digits!", Toast.LENGTH_SHORT).show();
                    } else if (Long.parseLong(StkPushFragment.this.amountedt.getText().toString()) == 0) {
                        Toast.makeText(StkPushFragment.this.getActivity(), "Amount must be greater than zero!", Toast.LENGTH_SHORT).show();
                    } else {
                        StkPushFragment.this.sendRequest();
                    }
                }
            });
        }
        return this.rootView;
    }

    public void sendRequest() {
        ProgressDialog progressDialog2 = new ProgressDialog(getActivity());
        this.progress = progressDialog2;
        progressDialog2.setMessage("Sending request...");
        this.progress.setCanceledOnTouchOutside(false);
        this.progress.show();
        String obj = this.amountedt.getText().toString();
        String obj2 = this.phoneedt.getText().toString();
        String substring = obj2.length() >= 9 ? obj2.substring(obj2.length() - 9) : "";
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("phone", "254" + substring);
            jSONObject.put("amount", obj);
            jSONObject.put("accountReference", this.accountId);
            jSONObject.put("transactionDesc", "Loan Repayment");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new OkHttpClient().newBuilder().connectTimeout(60, TimeUnit.SECONDS).writeTimeout(60, TimeUnit.SECONDS).readTimeout(60, TimeUnit.SECONDS).build().newCall(new Request.Builder().url("https://techsavanna.net:8443/enkaapis/public/api/mpesa/stkpush").method("POST", RequestBody.create(MediaType.parse("application/json,text/plain"), jSONObject.toString())).addHeader("Content-Type", "application/json").addHeader(SelfServiceInterceptor.HEADER_AUTH, "Bearer Q6Od9AhFDWeJGcimeD8oQ4yaw9DzWv5POJ7nktdxFiloJCzbxM").addHeader("Content-Type", "text/plain").addHeader("Cookie", "PHPSESSID=1qn6rbmkose1pfrcnagfu79i83").build()).enqueue(new Callback() {
            public void onFailure(Call call, IOException iOException) {
                if (StkPushFragment.this.getActivity() != null) {
                    StkPushFragment.this.getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            if (StkPushFragment.this.getActivity() != null) {
                                StkPushFragment.this.progress.dismiss();
                                Toast.makeText(StkPushFragment.this.getActivity(), "Request failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }

            public void onResponse(Call call, final Response response) throws IOException {
                if (StkPushFragment.this.getActivity() != null) {
                    StkPushFragment.this.getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            try {
                                final JSONObject jSONObject = new JSONObject(response.body().string());
                                PrintStream printStream = System.out;
                                printStream.println("respopopopoppo" + jSONObject);
                                if (jSONObject.getString("ResponseCode").equals("0")) {
                                    StkPushFragment.this.progress.setMessage("Confirming request...");
                                    new Thread() {
                                        public void run() {
                                            try {
                                                sleep(30000);
                                                try {
                                                    StkPushFragment.this.confirmRequest(jSONObject.getString("CheckoutRequestID"));
                                                } catch (JSONException e) {
                                                    Toast.makeText(StkPushFragment.this.getActivity(), "Error occurred...Kindly check the inputs", Toast.LENGTH_SHORT).show();
                                                    StkPushFragment.this.progress.dismiss();
                                                    e.printStackTrace();
                                                }
                                            } catch (InterruptedException e2) {
                                                Toast.makeText(StkPushFragment.this.getActivity(), "Error occurred...Kindly check the inputs", Toast.LENGTH_SHORT).show();
                                                StkPushFragment.this.progress.dismiss();
                                                e2.printStackTrace();
                                            }
                                        }
                                    }.start();
                                    return;
                                }
                                StkPushFragment.this.progress.dismiss();
                                Toast.makeText(StkPushFragment.this.getActivity(), "Request failed", Toast.LENGTH_SHORT).show();
                            } catch (IOException | JSONException e) {
                                Toast.makeText(StkPushFragment.this.getActivity(), "Request failed", Toast.LENGTH_SHORT).show();
                                StkPushFragment.this.progress.dismiss();
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });
    }

    public void confirmRequest(String str) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("checkoutRequestId", str);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new OkHttpClient().newBuilder().connectTimeout(60, TimeUnit.SECONDS).writeTimeout(60, TimeUnit.SECONDS).readTimeout(60, TimeUnit.SECONDS).build().newCall(new Request.Builder().url("https://techsavanna.net:8443/enkaapis/public/api/mpesa/confirm").method("POST", RequestBody.create(MediaType.parse("application/json,text/plain"), jSONObject.toString())).addHeader("Content-Type", "application/json").addHeader(SelfServiceInterceptor.HEADER_AUTH, "Bearer Q6Od9AhFDWeJGcimeD8oQ4yaw9DzWv5POJ7nktdxFiloJCzbxM").addHeader("Content-Type", "text/plain").addHeader("Cookie", "PHPSESSID=1qn6rbmkose1pfrcnagfu79i83").build()).enqueue(new Callback() {
            public void onFailure(Call call, IOException iOException) {
                if (StkPushFragment.this.getActivity() != null) {
                    StkPushFragment.this.getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            if (StkPushFragment.this.getActivity() != null) {
                                StkPushFragment.this.progress.dismiss();
                                Toast.makeText(StkPushFragment.this.getActivity(), "Confirmation failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }

            public void onResponse(Call call, final Response response) throws IOException {
                if (StkPushFragment.this.getActivity() != null) {
                    StkPushFragment.this.getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            try {
                                JSONObject jSONObject = new JSONObject(response.body().string());
                                StkPushFragment.this.progress.dismiss();
                                PrintStream printStream = System.out;
                                printStream.println("refdfdf" + jSONObject);
                                if (!jSONObject.has("ResultCode")) {
                                    return;
                                }
                                if (jSONObject.getString("ResultCode").equals("0")) {
                                    if (StkPushFragment.this.getActivity() != null) {
                                        Toast.makeText(StkPushFragment.this.getActivity(), "Payment confirmed successfully", Toast.LENGTH_LONG).show();
                                        StkPushFragment.this.progress.dismiss();
                                        StkPushFragment.this.startActivity(new Intent(StkPushFragment.this.getActivity(), HomeActivity.class));
                                        StkPushFragment.this.getActivity().finish();
                                    }
                                } else if (StkPushFragment.this.getActivity() != null) {
                                    Toast.makeText(StkPushFragment.this.getActivity(), "Processing Request Timeout!", Toast.LENGTH_LONG).show();
                                    StkPushFragment.this.progress.dismiss();
                                }
                            } catch (JSONException e) {
                                Toast.makeText(StkPushFragment.this.getActivity(), "An error occurred while processing your payment", 1).show();
                                StkPushFragment.this.progress.dismiss();
                                if (StkPushFragment.this.getActivity() != null) {
                                    StkPushFragment.this.startActivity(new Intent(StkPushFragment.this.getActivity(), HomeActivity.class));
                                    StkPushFragment.this.getActivity().finish();
                                }
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });
    }
}
