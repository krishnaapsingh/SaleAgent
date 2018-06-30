package com.trio.app.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.trio.app.R;
import com.trio.app.models.DistributorStockModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by trio on 25/6/18.
 */

public class DistributorStockAdapter extends RecyclerView.Adapter<DistributorStockAdapter.InvoiceView> {
    List<DistributorStockModel> list = new ArrayList<>();

    public DistributorStockAdapter(List<DistributorStockModel> obj) {
        list = obj;
    }

    @Override
    public InvoiceView onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.distributostock_view, parent, false);
        return new InvoiceView(view);
    }

    @Override
    public void onBindViewHolder(InvoiceView holder, int position) {
        DistributorStockModel data = list.get(position);
        if (data.Product != null) {
            holder.tvProductName.setText(data.Product);
        }
        if (data.Stock != null) {
            holder.tvStock.setText(data.Stock);
//            holder.tvStock1.setText(data.Stock);
        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class InvoiceView extends RecyclerView.ViewHolder {
        TextView tvProductName, tvStock;
        TextView tvStock1;

        public InvoiceView(View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvStock = itemView.findViewById(R.id.tvStock);
            tvStock1 = itemView.findViewById(R.id.tvStock1);
        }
    }
}
