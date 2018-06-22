package com.trio.app.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.trio.app.R;
import com.trio.app.activities.MainActivity;
import com.trio.app.appcontrollers.SavePref;
import com.trio.app.models.AchievedModel;
import com.trio.app.models.FxCoinValue;
import com.trio.app.models.Login;
import com.trio.app.models.TargetModel;
import com.trio.app.rest.ApiClient;
import com.trio.app.rest.ApiInterface;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by User on 12-Jan-18.
 */

@SuppressLint("ValidFragment")
public class DashboardFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = DashboardFragment.class.getSimpleName();
    String id;

    MainActivity activity;
    TextView tvAchievedSale, tvTargetSale;
    CardView cvDistributors, cvRoutes, cvShops, cvInvoices, cvReports, cvMyProfile;
    //    private TabLayout tabLayout;
//    private ViewPager viewPager;
    KProgressHUD hud;
    String licenceNo, emailId, year, month;

    @SuppressLint("ValidFragment")
    public DashboardFragment(MainActivity context) {
        this.activity = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        Calendar c = Calendar.getInstance();
        year =String.valueOf(c.get(Calendar.YEAR));
        month=String.valueOf(c.get(Calendar.MONTH));
        licenceNo = SavePref.getLoginData().LicenseNumber;
        emailId = SavePref.getLoginData().EmailID;

        getView(view);

        getTargetSale();
        getAchievedSale();
//        dataFromSav.ePref();
//        updateCoinValue();
        return view;
    }

    private void getAchievedSale() {
        hud.show();
        String url = "http://manage.bytepaper.com/Mobile/Manufacturing/index.php?getEmployeeAchieved&&" + licenceNo + "&&" + year + "&&" + month + "&&" + emailId;
        ApiInterface apiInterface = ApiClient.getClient();
        Call<AchievedModel> call = apiInterface.getUserAchieved(url);
        call.enqueue(new Callback<AchievedModel>() {
            @Override
            public void onResponse(Call<AchievedModel> call, Response<AchievedModel> response) {
                AchievedModel obj = response.body();
                if (obj.AchievedAmount != null) {
                    tvAchievedSale.setText(obj.AchievedAmount);
                } else {
                    tvAchievedSale.setText("0");
                }
                hud.dismiss();
            }

            @Override
            public void onFailure(Call<AchievedModel> call, Throwable t) {
                hud.dismiss();
            }
        });
    }

    private void getTargetSale() {
        hud.show();
        String url = "http://manage.bytepaper.com/Mobile/Manufacturing/index.php?getEmployeeTarget&&" + licenceNo + "&&" + year + "&&" + month + "&&" + emailId;
        ApiInterface apiInterface = ApiClient.getClient();
        Call<TargetModel> call = apiInterface.getUserTarget(url);
        call.enqueue(new Callback<TargetModel>() {
            @Override
            public void onResponse(Call<TargetModel> call, Response<TargetModel> response) {
                TargetModel obj = response.body();
                if (obj.TargetAmount != null) {
                    tvTargetSale.setText(obj.TargetAmount);
                } else {
                    tvTargetSale.setText("0");
                }
                hud.dismiss();
            }

            @Override
            public void onFailure(Call<TargetModel> call, Throwable t) {
                hud.dismiss();
            }
        });
    }





    public void getView(View view) {

        hud = KProgressHUD.create(getActivity())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setDimAmount(0.5f);

        cvDistributors = view.findViewById(R.id.cvDistributors);
        cvRoutes = view.findViewById(R.id.cvRoutes);
        cvShops = view.findViewById(R.id.cvShops);
        cvInvoices = view.findViewById(R.id.cvInvoices);
        cvReports = view.findViewById(R.id.cvReports);
        cvMyProfile = view.findViewById(R.id.cvMyProfile);

        tvAchievedSale = view.findViewById(R.id.tvAchievedSale);
        tvTargetSale = view.findViewById(R.id.tvTargetSale);


        cvDistributors.setOnClickListener(this);
        cvRoutes.setOnClickListener(this);
        cvShops.setOnClickListener(this);
        cvInvoices.setOnClickListener(this);
        cvReports.setOnClickListener(this);
        cvMyProfile.setOnClickListener(this);

//        tabLayout = view.findViewById(R.id.tablayout);
//        viewPager = view.findViewById(R.id.viewpager);
//        viewPager.setOffscreenPageLimit(3);
//        tabLayout.setupWithViewPager(viewPager);


    }

    @Override
    public void onClick(View v) {

        int ID = v.getId();
        switch (ID) {
            case R.id.cvDistributors:
                activity.onClick(1);
                break;
            case R.id.cvRoutes:
                activity.onClick(2);
                break;
            case R.id.cvShops:
                activity.onClick(3);
                break;
            case R.id.cvInvoices:
                activity.onClick(4);
                break;
            case R.id.cvReports:
                activity.onClick(5);
                break;
            case R.id.cvMyProfile:
                activity.onClick(6);
                break;

        }
    }
}
