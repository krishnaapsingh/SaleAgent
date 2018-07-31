package com.trio.app.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.trio.app.R;
import com.trio.app.activities.AddShopActivity;
import com.trio.app.activities.MainActivity;
import com.trio.app.appcontrollers.SavePref;
import com.trio.app.models.AchievedModel;
import com.trio.app.models.TargetModel;
import com.trio.app.rest.ApiClient;
import com.trio.app.rest.ApiInterface;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by User on 12-Jan-18.
 */

@SuppressLint("ValidFragment")
public class HomeFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = HomeFragment.class.getSimpleName();
    String id;

    MainActivity activity;
    TextView tvAchievedSale, tvTargetSale, tvAchievedNo, tvTargetNo;
    CardView cvDistributors, cvRoutes, cvShops, cvInvoices, cvReports, cvMyProfile, cvStock, cvOrder, cvTeam;
    //    private TabLayout tabLayout;
//    private ViewPager viewPager;
    KProgressHUD hud;
    String licenceNo, emailId, year, month;
    RelativeLayout rlCancel;
    CardView cvDrop;
    Animation slide_down, slide_up;
    long target, achieve;
    int check = 0;
//    FloatingActionButton fab;
    double percentage = 0.0;
    TextView tvComment;
    String userType = SavePref.getLoginData().UserType;

    @SuppressLint("ValidFragment")
    public HomeFragment(MainActivity context) {
        this.activity = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view;
        if (userType.equalsIgnoreCase("Distributor")) {
            view = inflater.inflate(R.layout.fragment_home1, container, false);
            getView1(view);
        } else {
            view = inflater.inflate(R.layout.fragment_home, container, false);
            getView(view);
        }
        hud = KProgressHUD.create(getActivity())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setDimAmount(0.5f);
        cvInvoices = view.findViewById(R.id.cvInvoices);
        cvReports = view.findViewById(R.id.cvReports);
        cvMyProfile = view.findViewById(R.id.cvMyProfile);
        cvInvoices.setOnClickListener(this);
        cvReports.setOnClickListener(this);
        cvMyProfile.setOnClickListener(this);

        Calendar c = Calendar.getInstance();
        year = String.valueOf(c.get(Calendar.YEAR));
        SimpleDateFormat month_date = new SimpleDateFormat("MMMM");
        month = month_date.format(c.getTime());
        licenceNo = SavePref.getLoginData().LicenseNumber;
        emailId = SavePref.getLoginData().EmailID;


        if (!userType.equalsIgnoreCase("Distributor")) {
            getTargetSale();
        }


        slide_down = AnimationUtils.loadAnimation(getActivity(),
                R.anim.slide_down);

        slide_up = AnimationUtils.loadAnimation(getActivity(),
                R.anim.slide_up);


        return view;
    }

    private void getAchievedSale() {

        String url = "";
        if (userType.equalsIgnoreCase("Sales Agent")) {
            url = "http://manage.bytepaper.com/Mobile/Manufacturing/index.php?getEmployeeAchieved&&" + licenceNo + "&&" + year + "&&" + month + "&&" + emailId;

        } else if (userType.equalsIgnoreCase("Admin")) {
            url = "http://manage.bytepaper.com/Mobile/Manufacturing/index.php?getCompanyAchieved&&" + licenceNo + "&&" + year + "&&" + month;
        }
        ApiInterface apiInterface = ApiClient.getClient();
        Call<AchievedModel> call = apiInterface.getUserAchieved(url);
        call.enqueue(new Callback<AchievedModel>() {
            @Override
            public void onResponse(Call<AchievedModel> call, Response<AchievedModel> response) {
                AchievedModel obj = response.body();
                if (obj.AchievedAmount != null) {
                    tvAchievedSale.setText(obj.AchievedAmount);
                    tvAchievedNo.setText(obj.AchievedNumber);
                    achieve = Long.parseLong(obj.AchievedAmount);
                } else {
                    tvAchievedSale.setText("0");
                    tvAchievedNo.setText("0");
                    achieve = Long.parseLong(tvAchievedSale.getText().toString().trim());
                }
                if (target != 0.0) {
                    calculatePercentage();
                }


            }

            @Override
            public void onFailure(Call<AchievedModel> call, Throwable t) {
                hud.dismiss();
            }
        });
    }

    private void calculatePercentage() {
        percentage = achieve * 100 / target;

        if (percentage <= 50.0) {
//                    if (!SavePref.fetchAchieveSale().equalsIgnoreCase(String.valueOf(achieve))){
            if (getActivity() != null) {
                cvDrop.setCardBackgroundColor(getResources().getColor(R.color.lightred));
            }
            tvComment.setText("Loser !! Need to work hard");
            cvDrop.setVisibility(View.VISIBLE);
//            cvDrop.startAnimation(slide_down);
//                    }else {
//                        cvDrop.setVisibility(View.GONE);
//                    }

        } else if (percentage > 50.0 && percentage <= 80.0) {
            if (getActivity() != null) {
                cvDrop.setCardBackgroundColor(getResources().getColor(R.color.orange));
            }
            tvComment.setText("Doing Good !! Little more effort ");
            cvDrop.setVisibility(View.VISIBLE);
//            cvDrop.startAnimation(slide_down);
        } else if (percentage > 80.0 && percentage < 100.0) {
            if (getActivity() != null) {
                cvDrop.setCardBackgroundColor(getResources().getColor(R.color.yellow));
            }
            tvComment.setText("Great Job !! Just about to reach target");
            cvDrop.setVisibility(View.VISIBLE);
//            cvDrop.startAnimation(slide_down);
        } else {
            if (getActivity() != null) {
                cvDrop.setCardBackgroundColor(getResources().getColor(R.color.green));
            }

            tvComment.setText("Excellent !!");
            cvDrop.setVisibility(View.VISIBLE);
//            cvDrop.startAnimation(slide_down);
        }
    }

    private void getTargetSale() {
        String url = "";
        if (userType.equalsIgnoreCase("Sales Agent")) {
            url = "http://manage.bytepaper.com/Mobile/Manufacturing/index.php?getEmployeeTarget&&" + licenceNo + "&&" + year + "&&" + month + "&&" + emailId;

        } else if (userType.equalsIgnoreCase("Admin")) {
            url = "http://manage.bytepaper.com/Mobile/Manufacturing/index.php?getCompanyTarget&&" + licenceNo + "&&" + year + "&&" + month;
        }
        ApiInterface apiInterface = ApiClient.getClient();
        Call<TargetModel> call = apiInterface.getUserTarget(url);
        call.enqueue(new Callback<TargetModel>() {
            @Override
            public void onResponse(Call<TargetModel> call, Response<TargetModel> response) {
                TargetModel obj = response.body();
                if (obj.TargetAmount != null) {
                    tvTargetSale.setText(obj.TargetAmount);
                    tvTargetNo.setText(obj.TargetNumber);
                    target = Long.parseLong(obj.TargetAmount);


                } else {
                    tvTargetSale.setText("0");
                    tvTargetNo.setText("0");
                    target = Long.parseLong(tvTargetSale.getText().toString().trim());
                }
                getAchievedSale();
            }

            @Override
            public void onFailure(Call<TargetModel> call, Throwable t) {
                hud.dismiss();
            }
        });
    }


    public void getView(View view) {
        cvDistributors = view.findViewById(R.id.cvDistributors);
        cvRoutes = view.findViewById(R.id.cvRoutes);
        cvShops = view.findViewById(R.id.cvShops);

        tvAchievedSale = view.findViewById(R.id.tvAchievedSale);
        tvTargetSale = view.findViewById(R.id.tvTargetSale);
        tvAchievedNo = view.findViewById(R.id.tvAchievedNo);
        tvTargetNo = view.findViewById(R.id.tvTargetNo);

//        fab = view.findViewById(R.id.fab);
//        fab.setVisibility(View.GONE);

//        if (!userType.equalsIgnoreCase("Admin")) {
//            fab.setVisibility(View.VISIBLE);
//        }

        cvDrop = view.findViewById(R.id.cvDrop);
        tvComment = view.findViewById(R.id.tvComment);
        cvDrop = view.findViewById(R.id.cvDrop);
        rlCancel = view.findViewById(R.id.rlCancel);
        cvDrop.setVisibility(View.GONE);

        cvDistributors.setOnClickListener(this);
        cvRoutes.setOnClickListener(this);
        cvShops.setOnClickListener(this);
//        fab.setOnClickListener(this);
        rlCancel.setOnClickListener(this);

    }

    public void getView1(View view) {


        cvStock = view.findViewById(R.id.cvStock);
        cvOrder = view.findViewById(R.id.cvOrder);
        cvTeam = view.findViewById(R.id.cvTeam);

        cvStock.setOnClickListener(this);
        cvOrder.setOnClickListener(this);
        cvTeam.setOnClickListener(this);


//        tabLayout = view.findViewById(R.id.tablayout);
//        viewPager = view.findViewById(R.id.viewpager);
//        viewPager.setOffscreenPageLimit(3);
//        tabLayout.setupWithViewPager(viewPager);


    }

    @Override
    public void onClick(View v) {

        int ID = v.getId();
        switch (ID) {
            case R.id.cvStock:
                activity.onClick(1);
                break;
            case R.id.cvOrder:
                activity.onClick(2);
                break;
            case R.id.cvTeam:
                activity.onClick(3);
                break;
            case R.id.cvDistributors:
                activity.onClick(4);
                break;
            case R.id.cvRoutes:
                activity.onClick(5);
                break;
            case R.id.cvShops:
                activity.onClick(6);
                break;
            case R.id.cvInvoices:
                activity.onClick(7);
                break;
            case R.id.cvReports:
                activity.onClick(8);
                break;
            case R.id.cvMyProfile:
                activity.onClick(9);
                break;
            case R.id.rlCancel:
//                cvDrop.startAnimation(slide_up);
                cvDrop.setVisibility(View.GONE);
                break;
//            case R.id.fab:
//                startActivity(new Intent(getActivity(), AddShopActivity.class));
//                break;

        }
    }
}
