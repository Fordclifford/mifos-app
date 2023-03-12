package org.lspl.mobile.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.lspl.mobile.R;
import org.lspl.mobile.models.GuarantorModel;

import java.util.List;

import butterknife.ButterKnife;

/* renamed from: org.techsavanna.enkasacco.ui.adapters.GuarantorAdapter */
public class GuarantorAdapter extends RecyclerView.Adapter<GuarantorAdapter.GuarantorViewHolder> {
    Context context;
    List<GuarantorModel> list;

    public GuarantorAdapter(Context context2, List<GuarantorModel> list2) {
        this.context = context2;
        this.list = list2;
    }

    public GuarantorViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new GuarantorViewHolder(LayoutInflater.from(this.context).inflate(R.layout.guarantorlist, viewGroup, false));
    }

    public void onBindViewHolder(GuarantorViewHolder guarantorViewHolder, int i) {
        TextView textView = guarantorViewHolder.name;
        textView.setText("Name:         " + this.list.get(i).getFirstname() + " " + this.list.get(i).getLastname());
        TextView textView2 = guarantorViewHolder.phone;
        StringBuilder sb = new StringBuilder();
        sb.append("Phone:        ");
        sb.append(this.list.get(i).getMobile_no());
        textView2.setText(sb.toString());
        TextView textView3 = guarantorViewHolder.f853id;
        textView3.setText("ID No.:        " + this.list.get(i).getExternal_id());
    }

    public int getItemCount() {
        return this.list.size();
    }

    /* renamed from: org.techsavanna.enkasacco.ui.adapters.GuarantorAdapter$GuarantorViewHolder */
    public static class GuarantorViewHolder extends RecyclerView.ViewHolder {

        /* renamed from: id */
        TextView f853id;
        TextView name;
        TextView phone;

        public GuarantorViewHolder(View view) {
            super(view);
            ButterKnife.bind((Object) this, view);
        }
    }
}
