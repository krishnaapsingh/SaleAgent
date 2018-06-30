package com.trio.app.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.trio.app.activities.MainActivity;
import com.trio.app.adapters.DistributorsAdapter;
import com.trio.app.R;
import com.trio.app.appcontrollers.SavePref;
import com.trio.app.models.DistributorsModel;
import com.trio.app.rest.ApiClient;
import com.trio.app.rest.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by User on 12-Jan-18.
 */

@SuppressLint("ValidFragment")
public class DistributorsFragment extends Fragment {

    public static final String TAG = DistributorsFragment.class.getSimpleName();

    MainActivity activity;
    RecyclerView rvDistributors;
    DistributorsAdapter adapter;
    KProgressHUD hud;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_distributors, container, false);
        rvDistributors = view.findViewById(R.id.rvDistributors);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rvDistributors.setLayoutManager(mLayoutManager);
        //rvDistributors.setItemAnimator(new DefaultItemAnimator());

        hud = KProgressHUD.create(getActivity())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setDimAmount(0.5f);
        getDistributorsList();
        return view;
    }

    private void getDistributorsList() {
        String licenceNo = SavePref.getLoginData().LicenseNumber;
        String emailId = SavePref.getLoginData().EmailID;
        hud.show();
        String url = "http://manage.bytepaper.com/Mobile/Manufacturing/index.php?getMappedDsitributor&&"+licenceNo+"&&"+emailId;
        ApiInterface apiInterface = ApiClient.getClient();
        Call<List<DistributorsModel>> call = apiInterface.getDistributors(url);
        call.enqueue(new Callback<List<DistributorsModel>>() {
            @Override
            public void onResponse(Call<List<DistributorsModel>> call, Response<List<DistributorsModel>> response) {
                List<DistributorsModel> obj = response.body();
               if (obj.size()!=0){
                adapter = new DistributorsAdapter(activity, obj);
                rvDistributors.setAdapter(adapter);
               }else {
                   Toast.makeText(getActivity(), "Distributors not found", Toast.LENGTH_SHORT).show();
               }
                hud.dismiss();
            }

            @Override
            public void onFailure(Call<List<DistributorsModel>> call, Throwable t) {
                hud.dismiss();
            }
        });
    }


    @SuppressLint("ValidFragment")
    public DistributorsFragment(MainActivity context) {
        this.activity=context;
    }






}
