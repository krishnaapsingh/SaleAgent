package com.trio.app.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.trio.app.R;
import com.trio.app.activities.MainActivity;

/**
 * Created by User on 12-Jan-18.
 */

@SuppressLint("ValidFragment")
public class RoutesFragment extends Fragment {

    public static final String TAG = RoutesFragment.class.getSimpleName();

    MainActivity activity;
    TextView tvMonth;
    Spinner spnMonth;
    RecyclerView rvRoute;


    @SuppressLint("ValidFragment")
    public RoutesFragment(MainActivity context) {
        this.activity = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_route, container, false);
        tvMonth = view.findViewById(R.id.tvMonth);
        spnMonth = view.findViewById(R.id.spnMonth);
        rvRoute = view.findViewById(R.id.rvRoute);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity());
        rvRoute.setLayoutManager(manager);

        return view;
    }

}
