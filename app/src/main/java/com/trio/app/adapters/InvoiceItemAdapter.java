package com.trio.app.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.trio.app.R;
import com.trio.app.models.InvoiceDetailModel;

import java.util.List;

/**
 * Created by trio on 25/6/18.
 */

public class InvoiceItemAdapter extends RecyclerView.Adapter<InvoiceItemAdapter.InvoiceView>{
    List<InvoiceDetailModel> list;
    public InvoiceItemAdapter(List<InvoiceDetailModel> obj) {
        list = obj;
    }

    @Override
    public InvoiceView onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.invoiceitemdetail_view, parent, false);
        return new InvoiceView(view);
    }

    @Override
    public void onBindViewHolder(InvoiceView holder, int position) {
        InvoiceDetailModel data = list.get(position);
        if (data.Product!=null){
            holder.tvProductName.setText(data.Product);
        }
        if (data.Rate!=null){
            holder.tvRate.setText(data.Rate);
        }
        if (data.Qty!=null){
            holder.tvQty.setText(data.Qty);
        }
        if (data.Amount!=null){
            holder.tvAmount.setText(data.Amount);
        }
        if (data.Qty!=null){
            holder.tvStock.setText(data.Qty);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class InvoiceView extends RecyclerView.ViewHolder {
        TextView tvProductName, tvRate, tvQty, tvAmount, tvStock;
        public InvoiceView(View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvRate = itemView.findViewById(R.id.tvRate);
            tvQty = itemView.findViewById(R.id.tvQty);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvStock = itemView.findViewById(R.id.tvStock);
        }
    }
}
