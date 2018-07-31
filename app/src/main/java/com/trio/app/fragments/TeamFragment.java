package com.trio.app.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.trio.app.R;
import com.trio.app.activities.MainActivity;
import com.trio.app.adapters.AgentsAdapter;
import com.trio.app.adapters.DistributorsAdapter;
import com.trio.app.appcontrollers.SavePref;
import com.trio.app.models.AgentsModel;
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
public class TeamFragment extends Fragment {

    public static final String TAG = TeamFragment.class.getSimpleName();

    MainActivity activity;
    RecyclerView rvDistributors;
    AgentsAdapter adapter;
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
        getTeamList();
        return view;
    }


    private void getTeamList() {
        String userType = SavePref.getLoginData().UserType;
        String licenceNo = SavePref.getLoginData().LicenseNumber;
        String emailId = SavePref.getLoginData().EmailID;
        hud.show();
        String url = "http://manage.bytepaper.com/Mobile/Manufacturing/index.php?getDistributorTeam&&" + licenceNo + "&&" + emailId;
        ApiInterface apiInterface = ApiClient.getClient();
        Call<List<AgentsModel>> call = apiInterface.getAgents(url);
        call.enqueue(new Callback<List<AgentsModel>>() {
            @Override
            public void onResponse(Call<List<AgentsModel>> call, Response<List<AgentsModel>> response) {
                List<AgentsModel> obj = response.body();
                if (obj.size() != 0) {
                    adapter = new AgentsAdapter(getActivity(), obj);
                    rvDistributors.setAdapter(adapter);
                } else {
                    Toast.makeText(getActivity(), "Agents not found", Toast.LENGTH_SHORT).show();
                }
                hud.dismiss();
            }

            @Override
            public void onFailure(Call<List<AgentsModel>> call, Throwable t) {
                hud.dismiss();
            }
        });
    }


    @SuppressLint("ValidFragment")
    public TeamFragment(MainActivity context) {
        this.activity = context;
    }


}
