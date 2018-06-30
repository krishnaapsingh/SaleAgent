package com.trio.app.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.orhanobut.hawk.Hawk;
import com.trio.app.R;
import com.trio.app.appcontrollers.SavePref;

public class CreateInvoiceActivity extends AppCompatActivity {
    Toolbar toolbar;
    KProgressHUD hud;
    private boolean inBackground = false;
    boolean checkBackground = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_invoice);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.backarrow));
        toolbar.setTitle("Create Invoice");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateInvoiceActivity.this.finish();
            }
        });
        hud = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setDimAmount(0.5f);

    }


    @Override
    public void onResume() {
        inBackground = false;

        if (checkBackground) {
            alertDialogForSessionTimeOut();
        }
        super.onResume();
    }

    private void alertDialogForSessionTimeOut() {

        final AlertDialog.Builder dialog = new AlertDialog.Builder(this)
                .setTitle("Sesion timeout ")
                .setMessage("Oops !!! Your session has been expired. You have to re-login");
        final AlertDialog alert = dialog.create();
        alert.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (alert.isShowing()) {
                    alert.dismiss();
                    Hawk.deleteAll();
                    SavePref.saveLogin(false);
                    startActivity(new Intent(CreateInvoiceActivity.this, LoginActivity.class));
                    CreateInvoiceActivity.this.finish();
                }
            }
        }, 2000);

    }

    @Override
    public void onPause() {
        inBackground = true;
        new CountDownTimer(300000, 1000) {
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                if (inBackground) {
                    checkBackground = true;
                }
            }
        }.start();
        super.onPause();
    }
}
