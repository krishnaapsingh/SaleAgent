package com.trio.app.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.trio.app.R;
import com.trio.app.appcontrollers.AdapterItemClick;
import com.trio.app.appcontrollers.SavePref;
import com.trio.app.fragments.InvoicesFragment;
import com.trio.app.models.InvoiceModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 15-Jan-18.
 */

public class InvoicesAdapter extends RecyclerView.Adapter<InvoicesAdapter.ViewHolder> {

    AdapterItemClick adapterItemClick;
    Context context;
    List<InvoiceModel> obj = new ArrayList<>();
    String userType;


    public InvoicesAdapter(InvoicesFragment invoicesFragment, List<InvoiceModel> list) {
        adapterItemClick = invoicesFragment;
        obj = list;
        userType = SavePref.getLoginData().UserType;
        context = invoicesFragment.getContext();

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
        if (userType.equalsIgnoreCase("Distributor")){
            if (data.OrderNumber != null) {
                holder.tvInvoiceNo.setText(data.OrderNumber);
            }
            if (data.OrderStaus != null) {
                holder.tvStatus.setText(data.OrderStaus);
                if (data.OrderStaus.equalsIgnoreCase("Pending")){
                    holder.tvStatus.setTextColor(context.getResources().getColor(R.color.red));
                }else if (data.OrderStaus.equalsIgnoreCase("Paid")){
                    holder.tvStatus.setTextColor(context.getResources().getColor(R.color.green));
                }else {
                    holder.tvStatus.setTextColor(context.getResources().getColor(R.color.black));
                }
            }
        }else {
            if (data.InvoiceNumber != null) {
                holder.tvInvoiceNo.setText(data.InvoiceNumber);
            }
            if (data.PaymentStaus != null) {
                holder.tvStatus.setText(data.PaymentStaus);
                if (data.PaymentStaus.equalsIgnoreCase("Outstanding")){
                    holder.tvStatus.setTextColor(context.getResources().getColor(R.color.red));
                }else if (data.PaymentStaus.equalsIgnoreCase("Paid")){
                    holder.tvStatus.setTextColor(context.getResources().getColor(R.color.green));
                }else {
                    holder.tvStatus.setTextColor(context.getResources().getColor(R.color.black));
                }
            }
        }


        if (data.Date != null) {
            holder.tvDate.setText(data.Date);
        }
        if (data.Amount != null) {
            holder.tvNetAmount.setText(data.Amount);
        }

//        if (SavePref.getLoginData().UserType.equalsIgnoreCase("Distributor")){
//            holder.llCreatedBy.setVisibility(View.VISIBLE);
//            holder.tvCreatedBy.setText(data.CreatedBy);
//        }

        holder.ll1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userType.equalsIgnoreCase("Distributor")){
                    if (SavePref.getActName().equalsIgnoreCase("order")){
                        adapterItemClick.OnItemClick(data.OrderNumber, data.Picture);
                    }else {
                        adapterItemClick.OnItemClick(data.InvoiceNumber, data.Picture);
                    }

                }else {
                    adapterItemClick.OnItemClick(data.InvoiceNumber, data.Picture);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return obj.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll1, llCreatedBy;
        TextView tvInvoiceNo, tvInvoiceNo1, tvDate, tvNetAmount, tvCreatedBy, tvStatus;

        public ViewHolder(View itemView) {
            super(itemView);

            ll1 = itemView.findViewById(R.id.ll1);
            tvInvoiceNo = itemView.findViewById(R.id.tvInvoiceNo);
            tvInvoiceNo1 = itemView.findViewById(R.id.tvInvoiceNo1);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvNetAmount = itemView.findViewById(R.id.tvNetAmount);
            tvCreatedBy = itemView.findViewById(R.id.tvCreatedBy);
            llCreatedBy = itemView.findViewById(R.id.llCreatedBy);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            if (userType.equalsIgnoreCase("Distributor")){
                tvInvoiceNo1.setText("Order no. ");
            }else {
                tvInvoiceNo1.setText("Invoice no. ");
            }


        }
    }

}
