package org.lspl.mobile.ui.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import org.lspl.mobile.R;
import org.lspl.mobile.models.GuarantorLoanModel;
public class GuarantorLoanAdapter extends RecyclerView.Adapter<GuarantorLoanAdapter.GuarantorLOanViewHolder> {
    Context context;
    List<GuarantorLoanModel> list;
    private int selectedItemPosition = -1;
    private SparseBooleanArray selectedItems = new SparseBooleanArray();

    public GuarantorLoanAdapter(Context context2, List<GuarantorLoanModel> list2) {
        this.context = context2;
        this.list = list2;
    }

    public GuarantorLOanViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new GuarantorLOanViewHolder(LayoutInflater.from(this.context).inflate(R.layout.guarantorlistloan, viewGroup, false));
    }

    public void onBindViewHolder(GuarantorLOanViewHolder guarantorLOanViewHolder, @SuppressLint("RecyclerView") final int i) {
        TextView textView = guarantorLOanViewHolder.name;
        textView.setText(this.list.get(i).getFirstname() + " " + this.list.get(i).getLastname());
        guarantorLOanViewHolder.phone.setText(this.list.get(i).getMobile_number());
        guarantorLOanViewHolder.f854id.setText(this.list.get(i).getId_number());
        guarantorLOanViewHolder.checkBox.setChecked(this.selectedItems.get(i, false));
        guarantorLOanViewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                GuarantorLoanAdapter.this.toggleSelection(i);
            }
        });
    }

    /* access modifiers changed from: private */
    public void toggleSelection(int i) {
        this.selectedItemPosition = i;
        if (this.selectedItems.get(i, false)) {
            this.selectedItems.delete(i);
        } else {
            this.selectedItems.put(i, true);
        }
        notifyDataSetChanged();
    }

    public List<GuarantorLoanModel> getSelectedItems() {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < this.selectedItems.size(); i++) {
            arrayList.add(this.list.get(this.selectedItems.keyAt(i)));
        }
        return arrayList;
    }

    public int getItemCount() {
        return this.list.size();
    }

    /* renamed from: org.techsavanna.enkasacco.ui.adapters.GuarantorLoanAdapter$GuarantorLOanViewHolder */
    public class GuarantorLOanViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;

        /* renamed from: id */
        TextView f854id;
        TextView name;
        TextView phone;

        public GuarantorLOanViewHolder(View view) {
            super(view);
            this.name = (TextView) view.findViewById(R.id.GuarantorName);
            this.phone = (TextView) view.findViewById(R.id.GuarantorPhone);
            this.f854id = (TextView) view.findViewById(R.id.GuarantorID);
            this.checkBox = (CheckBox) view.findViewById(R.id.CheckBoxGuarantor);
        }
    }
}
