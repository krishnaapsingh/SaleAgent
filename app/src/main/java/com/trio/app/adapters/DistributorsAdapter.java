package com.trio.app.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.trio.app.R;
import com.trio.app.models.DistributorsModel;

import java.util.List;

/**
 * Created by trio on 21/6/18.
 */

public class DistributorsAdapter extends RecyclerView.Adapter<DistributorsAdapter.MyLayout> {

    List<DistributorsModel> list;
    public DistributorsAdapter(List<DistributorsModel> obj) {
       list = obj;
    }

    @Override
    public MyLayout onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.distributors_view, parent, false);
        return new MyLayout(view);
    }

    @Override
    public void onBindViewHolder(MyLayout holder, int position) {

        holder.tvName.setText(list.get(position).Name);
        holder.tvEmail.setText(list.get(position).Email);
        holder.tvMobile.setText(list.get(position).Mobile);

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class MyLayout extends RecyclerView.ViewHolder {
        TextView tvName, tvMobile, tvEmail;
        public MyLayout(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvMobile = itemView.findViewById(R.id.tvMobile);
            tvEmail = itemView.findViewById(R.id.tvEmail);
        }
    }
}
