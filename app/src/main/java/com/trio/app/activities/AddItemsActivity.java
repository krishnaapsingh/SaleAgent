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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.orhanobut.hawk.Hawk;
import com.trio.app.R;
import com.trio.app.adapters.ItemAdapter;
import com.trio.app.appcontrollers.SavePref;

public class AddItemsActivity extends AppCompatActivity {

    RecyclerView rvItems;
    ItemAdapter adapter;
    String shopName;
    TextView tvShopName;
    Toolbar toolbar;
    KProgressHUD hud;
    RelativeLayout rlAddItems;

    private boolean inBackground = false;
    boolean checkBackground = false;
//    public void onResume() {
//        inBackground = false;
//
//        if (checkBackground) {
//            alertDialogForSessionTimeOut();
//        }
//        super.onResume();
//    }

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
//                    startActivity(new Intent(AddItemsActivity.this, LoginActivity.class));
//                    AddItemsActivity.this.finish();
//                }
//            }
//        }, 2000);
//
//    }
//
//    @Override
//    public void onPause()
//    {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_items);
        shopName = getIntent().getStringExtra("shopname");
        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.backarrow));
        toolbar.setTitle("Add Shop");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddItemsActivity.this.finish();
            }
        });
        hud = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setDimAmount(0.5f);
        tvShopName = findViewById(R.id.tvShopName);
        tvShopName.setText(shopName);
        rlAddItems = findViewById(R.id.rlAddItems);
        rvItems = findViewById(R.id.rvItems);

        rlAddItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        rvItems.setLayoutManager(manager);
    }
}
