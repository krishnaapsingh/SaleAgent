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
import android.widget.Toast;

import com.trio.app.R;
import com.trio.app.activities.MainActivity;
import com.trio.app.adapters.RouteAdapter;
import com.trio.app.appcontrollers.SavePref;
import com.trio.app.models.FxCoinValue;
import com.trio.app.models.SaleConfirmData;
import com.trio.app.rest.ApiClient;
import com.trio.app.rest.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by User on 12-Jan-18.
 */

@SuppressLint("ValidFragment")
public class ReportsFragment extends Fragment {

    public static final String TAG = ReportsFragment.class.getSimpleName();
    RecyclerView recyclerView;
    RouteAdapter saleConfirmDataListAdapter;
    MainActivity activity;

    @SuppressLint("ValidFragment")
    public ReportsFragment(MainActivity context) {
        this.activity = context;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sale_confirm, container, false);
        recyclerView = view.findViewById(R.id.sale_cnf_rv);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

//        getSaleConfirmData();
//        updateCoinValue();


        return view;
    }
    private void updateCoinValue() {
//
//        ApiInterface apiInterface = ApiClient.getClient();
//        Call<FxCoinValue> fxCoinValueCall = apiInterface.fxCoinValue(SavePref.getLoginData().data.id);
//        fxCoinValueCall.enqueue(new Callback<FxCoinValue>() {
//            @Override
//            public void onResponse(Call<FxCoinValue> call, Response<FxCoinValue> response) {
//                FxCoinValue fxCoinValue = response.body();
//                if (fxCoinValue.status) {
//                    SavePref.saveCoinValue(fxCoinValue);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<FxCoinValue> call, Throwable t) {
//
//            }
//        });

    }


    private void getSaleConfirmData() {

////        ApiInterface apiInterface = ApiClient.getClient();
//        final Call<SaleConfirmData> saleConfirmDataCall = apiInterface.saleConfirmData(SavePref.getLoginData().data.id);
//        saleConfirmDataCall.enqueue(new Callback<SaleConfirmData>() {
//            @Override
//            public void onResponse(Call<SaleConfirmData> call, Response<SaleConfirmData> response) {
//                SaleConfirmData saleConfirmData = response.body();
//                if (saleConfirmData.status) {
//                    saleConfirmDataListAdapter = new RouteAdapter(saleConfirmData.data, activity);
//                    recyclerView.setAdapter(saleConfirmDataListAdapter);
//                    Toast.makeText(getActivity(), saleConfirmData.message, Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(getActivity(), saleConfirmData.message, Toast.LENGTH_SHORT).show();
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<SaleConfirmData> call, Throwable t) {
//                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
    }


}


