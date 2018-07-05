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

import java.util.List;

/**
 * Created by trio on 15/6/18.
 */

public class ItemAdapter1 extends RecyclerView.Adapter<ItemAdapter1.ViewLayout> {
    Context mContext;
    List<DistributorStockModel> list;
    int itemCount = 1;
    int productPrice = 1;

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

        productPrice = Integer.valueOf(data.Product.replaceAll("[^0-9]", "")); // returns 123
        if (data.Product != null) {
            holder.tvProductName.setText(data.Product);
        }
        if (data.Stock != null) {
            holder.tvTotalStock.setText(data.Stock);
        }
//        holder.tvMinus.setVisibility(View.GONE);
        holder.tvCount.setText("1");
//        holder.tvTotalPrice.setText(productPrice);
//        CreateInvoiceActivity.totalPrice= String.valueOf(productPrice);
        CreateInvoiceActivity.quantity= String.valueOf("1");
        CreateInvoiceActivity.productName = data.Product;

        holder.tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemCount++;
                if (itemCount > 1) {
                    holder.tvMinus.setVisibility(View.VISIBLE);
                } else if (itemCount == Integer.valueOf(data.Stock)) {
                    holder.tvAdd.setVisibility(View.GONE);
                }
                holder.tvCount.setText(String.valueOf(itemCount));
                holder.tvTotalPrice.setText(String.valueOf(productPrice * itemCount));
                CreateInvoiceActivity.quantity= String.valueOf(itemCount);
//                CreateInvoiceActivity.productName = data.Product;

            }
        });


        holder.tvMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemCount--;
                if (itemCount == 1) {
                    holder.tvMinus.setVisibility(View.GONE);
                } else if (itemCount < Integer.valueOf(data.Stock)) {
                    holder.tvAdd.setVisibility(View.VISIBLE);
                }
                holder.tvCount.setText(String.valueOf(itemCount));
                holder.tvTotalPrice.setText(String.valueOf(productPrice * itemCount));
                CreateInvoiceActivity.quantity= String.valueOf(itemCount);
//                CreateInvoiceActivity.productName = data.Product;

            }
        });
    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewLayout extends RecyclerView.ViewHolder {
        TextView tvProductName, tvTotalStock, tvTotalPrice, tvCount, tvMinus, tvAdd;

        public ViewLayout(View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvTotalStock = itemView.findViewById(R.id.tvTotalStock);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
            tvCount = itemView.findViewById(R.id.tvCount);
            tvMinus = itemView.findViewById(R.id.tvMinus);
            tvAdd = itemView.findViewById(R.id.tvAdd);
        }
    }
}
