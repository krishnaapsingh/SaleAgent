package com.trio.app.adapters;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.trio.app.R;
import com.trio.app.activities.DistributorStockActivity;
import com.trio.app.activities.MainActivity;
import com.trio.app.models.DistributorsModel;

import java.util.List;

/**
 * Created by trio on 21/6/18.
 */

public class DistributorsAdapter extends RecyclerView.Adapter<DistributorsAdapter.MyLayout> {

    List<DistributorsModel> list;
    MainActivity context;
    public DistributorsAdapter(MainActivity activity, List<DistributorsModel> obj) {
       list = obj;
       context = activity;
    }

    @Override
    public MyLayout onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.distributors_view, parent, false);
        return new MyLayout(view);
    }

    @Override
    public void onBindViewHolder(MyLayout holder, final int position) {

        holder.tvName.setText(list.get(position).Name);
        holder.tvEmail.setText(list.get(position).Email);
        holder.tvMobile.setText(list.get(position).Mobile);
        holder.rlAvailStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, DistributorStockActivity.class);
                i.putExtra("distributorid", list.get(position).ID);
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyLayout extends RecyclerView.ViewHolder {
        TextView tvName, tvMobile, tvEmail;
        RelativeLayout rlAvailStock;
        public MyLayout(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvMobile = itemView.findViewById(R.id.tvMobile);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            rlAvailStock = itemView.findViewById(R.id.rlAvailStock);
        }
    }
}
