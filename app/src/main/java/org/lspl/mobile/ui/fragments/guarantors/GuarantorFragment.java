package org.lspl.mobile.ui.fragments.guarantors;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.lspl.mobile.R;
import org.lspl.mobile.api.BaseURL;
import org.lspl.mobile.models.GuarantorModel;
import org.lspl.mobile.ui.activities.CreateGuarantorActivity;
import org.lspl.mobile.ui.activities.LoanApplicationActivity;
import org.lspl.mobile.ui.adapters.GuarantorAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class GuarantorFragment extends Fragment {
    FloatingActionButton floatingActionButton;
    RecyclerView recyclerView;
    List<GuarantorModel> list;
    GuarantorAdapter adapter;
    private String clientId;
    String loanId = "";
    SwipeRefreshLayout refreshLayout;
    TextView textView;
    GuarantorAdapter guarantorAdapter = new GuarantorAdapter(getActivity(), new ArrayList<GuarantorModel>());


    public static GuarantorFragment newInstance() {
        GuarantorFragment fragment = new GuarantorFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_guarantor, container, false);
        floatingActionButton = view.findViewById(R.id.FloatingActionButt);
        textView = view.findViewById(R.id.empty_view);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), CreateGuarantorActivity.class));

            }
        });

        SharedPreferences sharedPref = getActivity().getSharedPreferences("ClientIDPreference", Context.MODE_PRIVATE);
        clientId = sharedPref.getString("clientId", "");
        System.out.println("client id" + clientId);

        refreshLayout = view.findViewById(R.id.SwipeRefresh);
        refreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        reloadGuarantors();
                    }
                }
        );
        recyclerView = view.findViewById(R.id.Recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        list = new ArrayList<>();
        displayGuarantor();

        return view;


    }

    public void displayGuarantor() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Fetching guarantors");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        list.clear();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, BaseURL.VIEW_GUARANTOR,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            String data = jsonObject.getString("data");
                            if (success.equals("1")) {
                                try {
                                    JSONArray jsonArray = new JSONArray(data);
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject object = jsonArray.getJSONObject(i);
                                        String firstname = object.getString("firstname");
                                        String lastname = object.getString("lastname");
                                        String address = object.getString("address_line_1");
                                        String id_number = object.getString("id_number");
                                        String mobile_number = object.getString("mobile_number");
                                        String id = object.getString("id");

                                        GuarantorModel guarantorModel = new GuarantorModel(id,firstname,lastname,id_number,mobile_number);
                                        list.add(guarantorModel);
                                    }
                                    adapter = new GuarantorAdapter(getActivity(), list);
                                    adapter.notifyDataSetChanged();
                                    recyclerView.setAdapter(adapter);
                                } catch (JSONException e) {
                                    Toast.makeText(getActivity(), "An error occurred"+e.toString(), Toast.LENGTH_LONG).show();
                                    e.printStackTrace();
                                }
                            } else {
                                recyclerView.setVisibility(View.GONE);
                                textView.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), "No connection", Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("client_id", clientId);
                return hashMap;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    private void reloadGuarantors() {
        // TODO implement a refresh
        refreshLayout.setRefreshing(false);// Disables the refresh icon
        displayGuarantor();
    }

    public void onResume() {
        super.onResume();
        if (getActivity()!=null){
            // Set title bar
            ((LoanApplicationActivity) getActivity())
                    .setActionBarTitle("Guarantors");
        }


    }

}
