package com.trio.app.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.trio.app.R;
import com.trio.app.activities.AddShopActivity;
import com.trio.app.activities.CreateInvoiceActivity;
import com.trio.app.models.DistributorStockModel;
import com.trio.app.rest.ApiClient;

import java.util.List;

/**
 * Created by trio on 15/6/18.
 */

public class ItemAdapter1 extends RecyclerView.Adapter<ItemAdapter1.ViewLayout> {
    Context mContext;
    List<DistributorStockModel> list;

    public ItemAdapter1(List<DistributorStockModel> obj) {
//        mContext = addItemsActivity;
        list = obj;
    }

    @Override
    public ViewLayout onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view, parent, false);
        return new ViewLayout(v);
    }

    @Override
    public void onBindViewHolder(final ViewLayout holder, int position) {

        final DistributorStockModel data = list.get(position);

        // returns 123
        if (data.Product != null) {
            holder.tvProductName.setText(data.Product);
        }
        if (data.Stock != null) {
            holder.tvTotalStock.setText(data.Stock);
        }
        if (data.Unit != null) {
            holder.tvUnit.setText(data.Unit);
        }


        if (ApiClient.isEmptyString(data.quantity)) {
            holder.tvCount.setText("0");
        } else {
            holder.tvCount.setText(data.quantity);
            holder.tvTotalPrice.setText(String.valueOf(Integer.valueOf(data.Price) * Integer.valueOf(data.quantity)));
        }


        holder.tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int itemCount = 0;
                if (ApiClient.isEmptyString(data.quantity)) {
                    itemCount = 0;
                } else {
                    itemCount = Integer.valueOf(data.quantity);
                }
                itemCount++;
                data.quantity = String.valueOf(itemCount);
                if (itemCount > 0) {
                    holder.tvMinus.setVisibility(View.VISIBLE);
                } else if (itemCount == Integer.valueOf(data.Stock)) {
                    holder.tvAdd.setVisibility(View.GONE);
                }
                ItemAdapter1.this.notifyDataSetChanged();
                createCount();
//                CreateInvoiceActivity.productName = data.Product;

            }
        });


        holder.tvMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int itemCount = 0;
                if (ApiClient.isEmptyString(data.quantity)) {
                    itemCount = 0;
                } else {
                    itemCount = Integer.valueOf(data.quantity);
                }
                itemCount--;
                data.quantity = String.valueOf(itemCount);
                if (itemCount < 1) {
                    holder.tvMinus.setVisibility(View.GONE);
                } else if (itemCount < Integer.valueOf(data.Stock)) {
                    holder.tvAdd.setVisibility(View.VISIBLE);
                }
                ItemAdapter1.this.notifyDataSetChanged();
                createCount();

            }
        });
    }

    private void createCount() {
        int totalQuantity = 0;
        int totalAmount = 0;
        int totalQuantity1 = 0;
        int totalAmount1 = 0;


        for (int i = 0; i < list.size(); i++) {

            if (list.get(i).quantity != null) {
                if (!list.get(i).quantity.equals("")) {
                    totalQuantity = Integer.valueOf(list.get(i).quantity);
                    totalAmount = totalQuantity * Integer.valueOf(list.get(i).Price);
                    totalQuantity1 = totalQuantity + totalQuantity1;

                    totalAmount1 = totalAmount + totalAmount1;
                }
            }

        }

        CreateInvoiceActivity.setValuesToText(totalAmount1, totalQuantity1);


    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewLayout extends RecyclerView.ViewHolder {
        TextView tvProductName, tvTotalStock, tvTotalPrice, tvCount, tvMinus, tvAdd, tvUnit;

        public ViewLayout(View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvTotalStock = itemView.findViewById(R.id.tvTotalStock);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
            tvCount = itemView.findViewById(R.id.tvCount);
            tvMinus = itemView.findViewById(R.id.tvMinus);
            tvAdd = itemView.findViewById(R.id.tvAdd);
            tvUnit = itemView.findViewById(R.id.tvUnit);
        }
    }
}
