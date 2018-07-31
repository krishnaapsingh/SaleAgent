package com.trio.app.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
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
import com.trio.app.adapters.InvoicesAdapter;
import com.trio.app.appcontrollers.AdapterItemClick;
import com.trio.app.models.InvoiceModel;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by User on 12-Jan-18.
 */


@SuppressLint("ValidFragment")
public class InvoicesFragment extends Fragment implements AdapterItemClick {

    public static final String TAG = InvoicesFragment.class.getSimpleName();
    RecyclerView recyclerView;
    InvoicesAdapter adapter;
    Context activity;
    public static List<InvoiceModel> list = new ArrayList<>();
    public static List<InvoiceModel> list1 = new ArrayList<>();
    public static List<InvoiceModel> list2 = new ArrayList<>();
    int i ;

    @SuppressLint("ValidFragment")
    public InvoicesFragment(Context invoicesActivity, int i) {
//        list = obj;
        this.i = i;
        activity = invoicesActivity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_invoices, container, false);
        recyclerView = view.findViewById(R.id.transfer_cnf_rv);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        if (list.size()!=0){
            setAdapter();
        }



        return view;
    }

    private void setAdapter() {
        if (i==0){
            adapter = new InvoicesAdapter(this, list);
        }else  if (i==1){
            adapter = new InvoicesAdapter(this, list1);
        }else  if (i==2){
            adapter = new InvoicesAdapter(this, list2);
        }

        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void OnItemClick(String position, String Image) {
        Intent i = new Intent(activity, InvoiceItemsActivity.class);
        i.putExtra("invoiceno", position);
        i.putExtra("image", Image);
        startActivity(i);
    }
}
