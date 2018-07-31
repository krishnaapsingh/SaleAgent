package com.trio.app.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.trio.app.R;
import com.trio.app.activities.AddShopActivity;
import com.trio.app.activities.InvoicesActivity;
import com.trio.app.activities.MainActivity;
import com.trio.app.appcontrollers.SavePref;
import com.trio.app.models.ShopModel;

import java.util.List;

/**
 * Created by trio on 21/6/18.
 */

public class ShopsAdapter extends RecyclerView.Adapter<ShopsAdapter.MyLayout> {

    List<ShopModel> list;
    Context mContext;


    public ShopsAdapter(FragmentActivity activity, List<ShopModel> obj) {
        mContext = activity;
        list = obj;
    }

    @Override
    public MyLayout onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shops_view, parent, false);
        return new MyLayout(view);
    }

    @Override
    public void onBindViewHolder(MyLayout holder, final int position) {

        if (list.get(position).Shop != null) {
            holder.tvShopName.setText(list.get(position).Shop);
        }
        if (list.get(position).Owner != null) {
            holder.tvOwnerName.setText(list.get(position).Owner);
        }
        if (list.get(position).Contact != null) {
            holder.tvMobile.setText(list.get(position).Contact);
        }
        if (list.get(position).Route != null) {
            holder.tvRouteName.setText(list.get(position).Route);
        }
        if (list.get(position).Shop != null) {
            holder.tvShopName.setText(list.get(position).Shop);
        }
        if (list.get(position).AreaName != null) {
            holder.tvAreaName.setText(list.get(position).AreaName);
        }
        if (list.get(position).CityName != null) {
            holder.tvCity.setText(list.get(position).CityName);
        }
        if (list.get(position).StateName != null) {
            holder.tvState.setText(list.get(position).StateName);
        }
        holder.btnInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, InvoicesActivity.class);
                i.putExtra("shopname", list.get(position).Shop);
                i.putExtra("check", "1");
                i.putExtra("shopid", list.get(position).ID);
                mContext.startActivity(i);
            }
        });



//        if (list.get(position).Pictur != null) {
        Glide.with(mContext).load(list.get(position).Picture).into(holder.ivImage);


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyLayout extends RecyclerView.ViewHolder {
        ImageView ivImage;
        CardView cvShops;
        Button btnInvoice;
        TextView tvShopName, tvOwnerName, tvMobile, tvRouteName, tvAreaName, tvCity, tvState;

        public MyLayout(View itemView) {
            super(itemView);
            tvShopName = itemView.findViewById(R.id.tvShopName);
            tvOwnerName = itemView.findViewById(R.id.tvOwnerName);
            tvMobile = itemView.findViewById(R.id.tvMobile);
            tvRouteName = itemView.findViewById(R.id.tvRouteName);
            tvAreaName = itemView.findViewById(R.id.tvAreaName);
            tvCity = itemView.findViewById(R.id.tvCity);
            tvState = itemView.findViewById(R.id.tvState);
            ivImage = itemView.findViewById(R.id.ivImage);
            cvShops = itemView.findViewById(R.id.cvShops);
            btnInvoice = itemView.findViewById(R.id.btnInvoice);
        }
    }
}
