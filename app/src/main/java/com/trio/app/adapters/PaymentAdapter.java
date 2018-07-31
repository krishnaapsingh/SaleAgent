package com.trio.app.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.trio.app.R;
import com.trio.app.models.PaymentDetailsModel;
import com.trio.app.models.ShopCreated;

import java.util.List;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.PaymentAdapterView>{
    List<PaymentDetailsModel> obj;
    public PaymentAdapter(List<PaymentDetailsModel> obj) {
        this.obj = obj;
    }

    @Override
    public PaymentAdapterView onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.paymentadapterview, parent, false);
        return new PaymentAdapterView(v);
    }

    @Override
    public void onBindViewHolder(PaymentAdapterView holder, int position) {
        PaymentDetailsModel data = obj.get(position);
        if (data.Date!=null){
            holder.tvDAteAdded.setText(data.Date);
        }
        if (data.Payment!=null){
            holder.tvAmount.setText(data.Payment);
        }

    }

    @Override
    public int getItemCount() {
        return obj.size();
    }

    public class PaymentAdapterView extends RecyclerView.ViewHolder {
        TextView tvAmount, tvDAteAdded;
        public PaymentAdapterView(View itemView) {
            super(itemView);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvDAteAdded = itemView.findViewById(R.id.tvDAteAdded);
        }
    }
}
