package com.trio.app.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.orhanobut.hawk.Hawk;
import com.trio.app.R;
import com.trio.app.adapters.RouteDetailsAdapter;
import com.trio.app.appcontrollers.SavePref;

public class RouteDetailsActivity extends AppCompatActivity {

    LinearLayout llFilter;
    TextView tvShopCount, tvFilterName;
    Spinner spnFilter;
    RecyclerView rvShop;
    RouteDetailsAdapter adapter;
    Toolbar toolbar;
    private boolean inBackground = false;
    boolean checkBackground = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_details);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.backarrow));
        toolbar.setTitle("Route Detail");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(InvoicesActivity.this, MainActivity.class));
//                InvoicesActivity.this.finish();
            }
        });
        tvShopCount = findViewById(R.id.tvShopCount);
        tvFilterName = findViewById(R.id.tvFilterName);
        spnFilter = findViewById(R.id.spnFilter);
        rvShop = findViewById(R.id.rvShop);
        llFilter = findViewById(R.id.llFilter);
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
//                    startActivity(new Intent(RouteDetailsActivity.this, LoginActivity.class));
//                    RouteDetailsActivity.this.finish();
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
//            public void onFinish() {
//                if (inBackground) {
//                    checkBackground = true;
//                }
//            }
//        }.start();
//        super.onPause();
//    }
}
