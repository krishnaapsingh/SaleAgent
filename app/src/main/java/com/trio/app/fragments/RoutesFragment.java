package com.trio.app.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.trio.app.R;
import com.trio.app.activities.MainActivity;
import com.trio.app.adapters.DistributorsAdapter;
import com.trio.app.adapters.RouteAdapter;
import com.trio.app.appcontrollers.SavePref;
import com.trio.app.models.RouteModel;
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
public class RoutesFragment extends Fragment {

    public static final String TAG = RoutesFragment.class.getSimpleName();

    MainActivity activity;
    TextView tvMonth;
    Spinner spnMonth;
    LinearLayout llSpn;
    RecyclerView rvRoute;
    KProgressHUD hud;
    RouteAdapter adapter;


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
        llSpn = view.findViewById(R.id.llSpn);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity());
        rvRoute.setLayoutManager(manager);
        hud = KProgressHUD.create(getActivity())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setDimAmount(0.5f);

        llSpn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spnMonth.performClick();
            }
        });
        spnMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tvMonth.setText(parent.getItemAtPosition(position).toString().trim());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        getRouteList();

        return view;
    }

    private void getRouteList() {
        String licenceNo = SavePref.getLoginData().LicenseNumber;
        String emailId = SavePref.getLoginData().EmailID;
        hud.show();
        String url = "http://manage.bytepaper.com/Mobile/Manufacturing/index.php?getMappedRoute&&"+licenceNo+"&&"+emailId;
        ApiInterface apiInterface = ApiClient.getClient();
        Call<List<RouteModel>> call = apiInterface.getRoutes(url);
        call.enqueue(new Callback<List<RouteModel>>() {
            @Override
            public void onResponse(Call<List<RouteModel>> call, Response<List<RouteModel>> response) {
                List<RouteModel> obj = response.body();
                if (obj.size()!=0){
                    adapter = new RouteAdapter(activity, obj);
                    rvRoute.setAdapter(adapter);
                }else {
                    Toast.makeText(getActivity(), "Route not found ", Toast.LENGTH_SHORT).show();
                }
                hud.dismiss();
            }

            @Override
            public void onFailure(Call<List<RouteModel>> call, Throwable t) {
                hud.dismiss();
            }
        });
    }

}
