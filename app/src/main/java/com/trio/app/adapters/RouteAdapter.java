package com.trio.app.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.trio.app.R;
import com.trio.app.activities.MainActivity;
import com.trio.app.activities.RouteDetailsActivity;
import com.trio.app.models.RouteModel;

import java.util.List;


/**
 * Created by User on 15-Jan-18.
 */

public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.ViewHolder> {


    List<RouteModel> list;
    Context mContext;

    public RouteAdapter(MainActivity activity, List<RouteModel> obj) {
        list = obj;
        mContext = activity;
    }

    @Override
    public RouteAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content_route, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RouteAdapter.ViewHolder holder, int position) {
        final RouteModel model = list.get(position);
        holder.tvRouteName.setText(model.RouteName);
        holder.tvAreaName.setText(model.AreaName);
        holder.tvStateName.setText(model.StateName);
        holder.tvCityName.setText(model.CityName);
        holder.cvShops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, RouteDetailsActivity.class);
                i.putExtra("routename", model.RouteName);
                mContext.startActivity(i);
            }
        });

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvRouteName, tvAreaName, tvStateName, tvCityName;
 CardView cvShops;
        private ViewHolder(View itemView) {
            super(itemView);

            tvRouteName = itemView.findViewById(R.id.tvRouteName);
            tvAreaName = itemView.findViewById(R.id.tvAreaName);
            tvStateName = itemView.findViewById(R.id.tvStateName);
            tvCityName = itemView.findViewById(R.id.tvCityName);
            cvShops = itemView.findViewById(R.id.cvShops);
        }
    }
}
