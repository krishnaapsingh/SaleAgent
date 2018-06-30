package com.trio.app.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trio.app.R;
import com.trio.app.activities.InvoiceItemsActivity;
import com.trio.app.activities.InvoicesActivity;
import com.trio.app.adapters.InvoicesAdapter;
import com.trio.app.appcontrollers.AdapterItemClick;
import com.trio.app.models.InvoiceModel;

import java.util.List;


/**
 * Created by User on 12-Jan-18.
 */


@SuppressLint("ValidFragment")
public class InvoicesFragment extends Fragment implements AdapterItemClick{

    public static final String TAG = InvoicesFragment.class.getSimpleName();
    RecyclerView recyclerView;
    InvoicesAdapter adapter;
    InvoicesActivity activity;
    List<InvoiceModel> list;

    @SuppressLint("ValidFragment")
    public InvoicesFragment(List<InvoiceModel> obj, InvoicesActivity invoicesActivity) {
       list = obj;
       activity = invoicesActivity;

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_invoices, container, false);
        recyclerView = view.findViewById(R.id.transfer_cnf_rv);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new InvoicesAdapter(this, list);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
        return view;
    }


    @Override
    public void OnItemClick(String position) {
        Intent i = new Intent(activity, InvoiceItemsActivity.class);
        i.putExtra("invoiceno", position);
        startActivity(i);
    }
}
