package com.trio.app.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.trio.app.R;

/**
 * Created by trio on 25/6/18.
 */

public class RouteDetailsAdapter extends RecyclerView.Adapter<RouteDetailsAdapter.MyView>{




    @Override
    public MyView onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.route_view, parent, false);
        return null;
    }

    @Override
    public void onBindViewHolder(MyView holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class MyView extends RecyclerView.ViewHolder {
        TextView tvStoreName, tvDateAdded, tvAmount;
        public MyView(View itemView) {
            super(itemView);
            tvStoreName = itemView.findViewById(R.id.tvStoreName);
            tvDateAdded = itemView.findViewById(R.id.tvDateAdded);
            tvAmount = itemView.findViewById(R.id.tvAmount);
        }
    }
}
