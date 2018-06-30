package com.trio.app.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.trio.app.R;
import com.trio.app.appcontrollers.AdapterItemClick;
import com.trio.app.fragments.InvoicesFragment;
import com.trio.app.models.InvoiceModel;

import java.util.List;

/**
 * Created by User on 15-Jan-18.
 */

public class InvoicesAdapter extends RecyclerView.Adapter<InvoicesAdapter.ViewHolder> {

    AdapterItemClick adapterItemClick;
    List<InvoiceModel> obj;


    public InvoicesAdapter(InvoicesFragment invoicesFragment, List<InvoiceModel> list) {
        adapterItemClick = invoicesFragment;
        obj = list;

    }

    @Override
    public InvoicesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.invoiceview, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(InvoicesAdapter.ViewHolder holder, final int position) {

        final InvoiceModel data = obj.get(position);

        if (data.InvoiceNumber != null) {
            holder.tvInvoiceNo.setText(data.InvoiceNumber);
        }
        if (data.Date != null) {
            holder.tvDate.setText(data.Date);
        }
        if (data.Amount != null) {
            holder.tvNetAmount.setText(data.Amount);
        }
        if (data.PaymentStaus != null) {
            holder.tvStatus.setText(data.PaymentStaus);
        }

        holder.ll1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterItemClick.OnItemClick(data.InvoiceNumber);
            }
        });

    }

    @Override
    public int getItemCount() {
        return obj.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll1;
        TextView tvInvoiceNo, tvDate, tvNetAmount, tvTotalItems, tvStatus;

        public ViewHolder(View itemView) {
            super(itemView);

            ll1 = itemView.findViewById(R.id.ll1);
            tvInvoiceNo = itemView.findViewById(R.id.tvInvoiceNo);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvNetAmount = itemView.findViewById(R.id.tvNetAmount);
            tvTotalItems = itemView.findViewById(R.id.tvTotalItems);
            tvStatus = itemView.findViewById(R.id.tvStatus);

        }
    }

}
