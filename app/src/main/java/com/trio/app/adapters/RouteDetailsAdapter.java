package com.trio.app.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.trio.app.R;
import com.trio.app.activities.InvoicesActivity;
import com.trio.app.activities.RouteDetailsActivity;
import com.trio.app.models.ShopModel;

import java.util.List;

/**
 * Created by trio on 25/6/18.
 */

public class RouteDetailsAdapter extends RecyclerView.Adapter<RouteDetailsAdapter.MyView>{

    Context context;
    List<ShopModel> list;

    public RouteDetailsAdapter(RouteDetailsActivity routeDetailsActivity, List<ShopModel> obj1) {
        context = routeDetailsActivity;
        list = obj1;
    }

    @Override
    public MyView onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.route_view, parent, false);
        return new MyView(view);
    }

    @Override
    public void onBindViewHolder(MyView holder, int position) {
        final ShopModel data = list.get(position);
        if (data.Shop!=null){
            holder.tvStoreName.setText(data.Shop);
        }
        if (data.Contact!=null){
            holder.tvAmount.setText(data.Contact);
        }
        holder.llMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, InvoicesActivity.class);
                i.putExtra("shopname", data.Shop);
                i.putExtra("check", "1");
                i.putExtra("shopid", data.ID);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyView extends RecyclerView.ViewHolder {
        TextView tvStoreName, tvDateAdded, tvAmount;
        LinearLayout llMain;
        public MyView(View itemView) {
            super(itemView);
            tvStoreName = itemView.findViewById(R.id.tvStoreName);
            tvDateAdded = itemView.findViewById(R.id.tvDateAdded);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            llMain = itemView.findViewById(R.id.llMain);
        }
    }
}
