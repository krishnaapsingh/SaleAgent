package com.trio.app.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.trio.app.R;
import com.trio.app.activities.DateWiseReportActivity;
import com.trio.app.activities.DistributorStockActivity;
import com.trio.app.activities.MainActivity;
import com.trio.app.models.DistributorsModel;
import com.trio.app.models.ReportModel;
import com.trio.app.models.RouteModel;

import java.util.List;

/**
 * Created by trio on 21/6/18.
 */

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.MyLayout> {

    List<ReportModel> list;
    Context context;

    public ReportAdapter(Context activity, List<ReportModel> obj) {
        list = obj;
        context = activity;
    }

    @Override
    public MyLayout onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.report_view, parent, false);
        return new MyLayout(view);
    }

    @Override
    public void onBindViewHolder(MyLayout holder, final int position) {

        ReportModel model = list.get(position);

        if (model.Product != null) {
            holder.tvProductName.setText(model.Product);
        }
        if (model.TotalQty != null) {
            holder.tvTotalQuantity.setText(model.TotalQty);
        }
        if (model.TotalAmount != null) {
            holder.tvTotalAmount.setText(model.TotalAmount);
        }




    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyLayout extends RecyclerView.ViewHolder {
        TextView tvProductName, tvTotalQuantity, tvTotalAmount;
        LinearLayout llMain;

        private MyLayout(View itemView) {
            super(itemView);
            tvTotalAmount = itemView.findViewById(R.id.tvTotalAmount);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvTotalQuantity = itemView.findViewById(R.id.tvTotalQuantity);
            llMain = itemView.findViewById(R.id.llMain);
        }
    }
}
