package com.trio.app.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.orhanobut.hawk.Hawk;
import com.trio.app.R;
import com.trio.app.adapters.DistributorStockAdapter;
import com.trio.app.adapters.DistributorsAdapter;
import com.trio.app.appcontrollers.SavePref;
import com.trio.app.models.DistributorStockModel;
import com.trio.app.models.DistributorsModel;
import com.trio.app.rest.ApiClient;
import com.trio.app.rest.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DistributorStockActivity extends AppCompatActivity {

    Toolbar toolbar;
    String ID;
    KProgressHUD hud;
    DistributorStockAdapter adapter;
    RecyclerView rvItems;
    private boolean inBackground = false;
    private boolean checkBackground = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distributor_stock);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.backarrow));
        toolbar.setTitle("Distributors Stock");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(InvoicesActivity.this, MainActivity.class));
                DistributorStockActivity.this.finish();
            }
        });
        hud = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setDimAmount(0.5f);
        rvItems = findViewById(R.id.rvItems);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        rvItems.setLayoutManager(mLayoutManager);
        ID = getIntent().getStringExtra("distributorid");
        getDistributorStock();
    }

//    @Override
//    public void onResume() {
//        inBackground = false;
//
//        if (checkBackground) {
//            alertDialogForSessionTimeOut();
//        }
//        super.onResume();
//    }
//
//    private void alertDialogForSessionTimeOut() {
//
//        final AlertDialog.Builder dialog = new AlertDialog.Builder(this)
//                .setTitle("Sesion timeout ")
//                .setMessage("Oops !!! Your session has been expired. You have to re-login");
//        final AlertDialog alert = dialog.create();
//        alert.show();
//
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if (alert.isShowing()) {
//                    alert.dismiss();
//                    Hawk.deleteAll();
//                    startActivity(new Intent(DistributorStockActivity.this, LoginActivity.class));
//                    DistributorStockActivity.this.finish();
//                }
//            }
//        }, 2000);
//
//    }
//
//    @Override
//    public void onPause() {
//        inBackground = true;
//        new CountDownTimer(300000, 1000) {
//            public void onTick(long millisUntilFinished) {
//            }
//
//            public void onFinish() {
//                if (inBackground) {
//                    checkBackground = true;
//                }
//            }
//        }.start();
//        super.onPause();
//    }

    private void getDistributorStock() {
        String licenceNo = SavePref.getLoginData().LicenseNumber;
        String emailId = SavePref.getLoginData().EmailID;
        hud.show();
        String url = "http://manage.bytepaper.com/Mobile/Manufacturing/index.php?getDistributorStock&&"+licenceNo+"&&"+ID;
        ApiInterface apiInterface = ApiClient.getClient();
        Call<List<DistributorStockModel>> call = apiInterface.getDistributorStock(url);
        call.enqueue(new Callback<List<DistributorStockModel>>() {
            @Override
            public void onResponse(Call<List<DistributorStockModel>> call, Response<List<DistributorStockModel>> response) {
                List<DistributorStockModel> obj = response.body();
                if (obj.size()!=0){
                    adapter = new DistributorStockAdapter(obj);
                    rvItems.setAdapter(adapter);
                }
                hud.dismiss();
            }

            @Override
            public void onFailure(Call<List<DistributorStockModel>> call, Throwable t) {
                hud.dismiss();
            }
        });
    }
}
