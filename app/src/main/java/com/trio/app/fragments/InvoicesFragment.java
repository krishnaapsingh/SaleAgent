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
import com.trio.app.adapters.InvoicesAdapter;
import com.trio.app.appcontrollers.SavePref;
import com.trio.app.models.TransferConfirmData;
import com.trio.app.rest.ApiClient;
import com.trio.app.rest.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by User on 12-Jan-18.
 */


public class InvoicesFragment extends Fragment {

    public static final String TAG = InvoicesFragment.class.getSimpleName();
    RecyclerView recyclerView;
    InvoicesAdapter transferCnfRecyclerAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_invoices, container, false);
        recyclerView = view.findViewById(R.id.transfer_cnf_rv);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        return view;
    }


    private void getSaleConfirmData() {

//        ApiInterface apiInterface = ApiClient.getClient();
//        final Call<TransferConfirmData> transferConfirmDataCall = apiInterface.transferConfirmData(SavePref.getLoginData().data.id);
//        transferConfirmDataCall.enqueue(new Callback<TransferConfirmData>() {
//            @Override
//            public void onResponse(Call<TransferConfirmData> call, Response<TransferConfirmData> response) {
//                TransferConfirmData transferConfirmData = response.body();
//                if (transferConfirmData.status) {
////                    transferCnfRecyclerAdapter = new InvoicesAdapter(transferConfirmData.data);
//                    transferCnfRecyclerAdapter = new InvoicesAdapter();
//                    recyclerView.setAdapter(transferCnfRecyclerAdapter);
//                    Toast.makeText(getActivity(), transferConfirmData.message, Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(getActivity(), transferConfirmData.message, Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<TransferConfirmData> call, Throwable t) {
//
//                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//
//
//        });
    }


}
