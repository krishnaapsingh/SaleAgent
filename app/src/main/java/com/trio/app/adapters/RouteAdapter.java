package com.trio.app.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.trio.app.R;
import com.trio.app.activities.MainActivity;
import com.trio.app.models.SaleConfirmData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.trio.app.fragments.ShopsFragment.amount;
import static com.trio.app.fragments.ShopsFragment.commission;
import static com.trio.app.fragments.ShopsFragment.name;
import static com.trio.app.fragments.ShopsFragment.noofsalecoin;
import static com.trio.app.fragments.ShopsFragment.saleamount;
import static com.trio.app.fragments.ShopsFragment.saleid;
import static com.trio.app.fragments.ShopsFragment.unitcost;
import static com.trio.app.fragments.ShopsFragment.username;

/**
 * Created by User on 15-Jan-18.
 */

public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.ViewHolder> {


    List<SaleConfirmData.InnerData> innerDataList;
    MainActivity activity;


    public RouteAdapter(List<SaleConfirmData.InnerData> data, MainActivity activity) {
        innerDataList = data;
        this.activity = activity;
    }

    @Override
    public RouteAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content_route, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RouteAdapter.ViewHolder holder, int position) {
        if (position%2==0){
            holder.rlStatus.setVisibility(View.GONE);
        }

    }




//    public String parseDateToddMMyyyy(String time) {
//        String inputPattern = "MM dd, YYYY, HH:mm a";
//        String outputPattern = "dd-MMM-yyyy h:mm a";
//        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
//        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);
//
//        Date date = null;
//        String str = null;
//
//        try {
//            date = inputFormat.parse(time);
//            str = outputFormat.format(date);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        return str;
//    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
      TextView tvStatus, tvRoute;
      RelativeLayout rlStatus;
        private ViewHolder(View itemView) {
            super(itemView);

            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvRoute = itemView.findViewById(R.id.tvRoute);
            rlStatus = itemView.findViewById(R.id.rlStatus);
        }
    }
}
