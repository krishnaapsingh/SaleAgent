package com.trio.app.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.trio.app.R;
import com.trio.app.models.TransferConfirmData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by User on 15-Jan-18.
 */

public class InvoicesAdapter extends RecyclerView.Adapter<InvoicesAdapter.ViewHolder>{

    List<TransferConfirmData.InneTransferData> list;
//    public InvoicesAdapter(List<TransferConfirmData.InneTransferData> data) {
//        list = data;
//    }

    @Override
    public InvoicesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.invoiceview, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(InvoicesAdapter.ViewHolder holder, int position) {

        final TransferConfirmData.InneTransferData innerData = list.get(position);

//        holder.tvUCost.setText("1 Fx Coin =  ₹ "+innerData.ucost);
//        holder.tvTDate.setText("Date: "+innerData.rdate);
//        holder.tvTName.setText(innerData.tname);
//        holder.tvCoin.setText(innerData.tcoin);
//        holder.tvTCost.setText(innerData.tcost);
//        holder.tvSattus.setText(innerData.status);
    }

    @Override
    public int getItemCount() {
        return 10;
//        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvUCost, tvTDate, tvTName, tvCoin, tvTCost, tvSattus;
        public ViewHolder(View itemView) {
            super(itemView);

//            tvUCost = itemView.findViewById(R.id.contenttransfer_tv_unitcost);
//            tvTDate = itemView.findViewById(R.id.contenttransfer_tv_tdate);
//            tvTName = itemView.findViewById(R.id.contenttransfer_tv_tname);
//            tvCoin = itemView.findViewById(R.id.contenttransfer_tv_coin);
//            tvTCost = itemView.findViewById(R.id.contenttransfer_tv_tcost);
//            tvSattus = itemView.findViewById(R.id.contenttransfer_tv_status);
        }
    }

    public String parseDateToddMMyyyy(String time) {
        String inputPattern = "MM dd, YYYY, HH:mm a";
        String outputPattern = "dd-MMM-yyyy h:mm a";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }
}
