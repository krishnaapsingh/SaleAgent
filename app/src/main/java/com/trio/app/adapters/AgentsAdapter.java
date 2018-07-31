package com.trio.app.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.trio.app.R;
import com.trio.app.activities.DistributorStockActivity;
import com.trio.app.activities.MainActivity;
import com.trio.app.models.AgentsModel;
import com.trio.app.models.DistributorsModel;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by trio on 21/6/18.
 */

public class AgentsAdapter extends RecyclerView.Adapter<AgentsAdapter.MyLayout> {

    List<AgentsModel> list;
    Context context;
    public AgentsAdapter(Context activity, List<AgentsModel> obj) {
       list = obj;
       context = activity;
    }

    @Override
    public MyLayout onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.agents_view, parent, false);
        return new MyLayout(view);
    }

    @Override
    public void onBindViewHolder(MyLayout holder, final int position) {

        holder.tvName.setText(list.get(position).Name);
        holder.tvEmail.setText(list.get(position).EmailID);
        holder.tvContact.setText(list.get(position).ContactNumber);
        Glide.with(context).load(list.get(position).ProfilePic).into(holder.imageView);
//        holder.rlAvailStock.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(context, DistributorStockActivity.class);
//                i.putExtra("distributorid", list.get(position).ID);
//                context.startActivity(i);
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyLayout extends RecyclerView.ViewHolder {
        TextView tvName, tvContact, tvEmail;
        CircleImageView imageView;
        RelativeLayout rlAvailStock;
        public MyLayout(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvContact = itemView.findViewById(R.id.tvContact);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            imageView = itemView.findViewById(R.id.imageView);
//            rlAvailStock = itemView.findViewById(R.id.rlAvailStock);
        }
    }
}
