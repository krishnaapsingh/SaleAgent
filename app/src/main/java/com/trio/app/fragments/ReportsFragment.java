package com.trio.app.fragments;

import android.annotation.SuppressLint;
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
import com.trio.app.activities.MainActivity;
import com.trio.app.adapters.RouteAdapter;

/**
 * Created by User on 12-Jan-18.
 */

@SuppressLint("ValidFragment")
public class ReportsFragment extends Fragment {

    public static final String TAG = ReportsFragment.class.getSimpleName();
    RecyclerView recyclerView;
    MainActivity activity;

    @SuppressLint("ValidFragment")
    public ReportsFragment(MainActivity context) {
        this.activity = context;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reports, container, false);
        recyclerView = view.findViewById(R.id.sale_cnf_rv);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        return view;
    }

}


