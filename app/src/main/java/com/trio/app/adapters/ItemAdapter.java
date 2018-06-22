package com.trio.app.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.trio.app.R;

/**
 * Created by trio on 15/6/18.
 */

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewLayout>{
    @Override
    public ViewLayout onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view, parent, false);
        return new ViewLayout(v);
    }

    @Override
    public void onBindViewHolder(ViewLayout holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewLayout extends RecyclerView.ViewHolder {
        TextView tvCount, tvAdd, tvMinus;
        public ViewLayout(View itemView) {

            super(itemView);
        }
    }
}
