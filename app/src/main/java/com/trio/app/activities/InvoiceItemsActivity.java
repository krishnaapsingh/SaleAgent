package com.trio.app.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.orhanobut.hawk.Hawk;
import com.trio.app.R;
import com.trio.app.adapters.InvoiceItemAdapter;
import com.trio.app.appcontrollers.SavePref;
import com.trio.app.models.InvoiceDetailModel;
import com.trio.app.rest.ApiClient;
import com.trio.app.rest.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InvoiceItemsActivity extends AppCompatActivity {

    InvoiceItemAdapter adapter;
    Toolbar toolbar;
    RecyclerView rvItems;
    TextView tvInvoiceNo, tvDate, tvNetAmount, tvDistributors, tvRoute, tvAddress, tvAmtStatus;
    KProgressHUD hud;
    String invoiceno = "";
    private boolean inBackground = false;
    boolean checkBackground = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_items);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.backarrow));
        toolbar.setTitle("Invoice Items");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(InvoicesActivity.this, MainActivity.class));
                InvoiceItemsActivity.this.finish();
            }
        });
        hud = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setDimAmount(0.5f).show();
        invoiceno = getIntent().getStringExtra("invoiceno");

        tvInvoiceNo = findViewById(R.id.tvInvoiceNo);
        tvDate = findViewById(R.id.tvDate);
        tvNetAmount = findViewById(R.id.tvNetAmount);
        tvDistributors = findViewById(R.id.tvDistributors);
        tvRoute = findViewById(R.id.tvRoute);
        tvAddress = findViewById(R.id.tvAddress);
        tvAmtStatus = findViewById(R.id.tvAmtStatus);
        rvItems = findViewById(R.id.rvItems);


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        rvItems.setLayoutManager(mLayoutManager);
        getInvoiceDetails();

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
                    startActivity(new Intent(InvoiceItemsActivity.this, LoginActivity.class));
                    InvoiceItemsActivity.this.finish();
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

    private void getInvoiceDetails() {
        String licenceNo = SavePref.getLoginData().LicenseNumber;
        String emailId = SavePref.getLoginData().EmailID;
        hud.show();
        String url = "http://manage.bytepaper.com/Mobile/Manufacturing/index.php?InvoiceDetail&&" + licenceNo + "&&" + invoiceno;
        ApiInterface apiInterface = ApiClient.getClient();
        Call<List<InvoiceDetailModel>> call = apiInterface.getInvoiceDetails(url);
        call.enqueue(new Callback<List<InvoiceDetailModel>>() {
            @Override
            public void onResponse(Call<List<InvoiceDetailModel>> call, Response<List<InvoiceDetailModel>> response) {
                List<InvoiceDetailModel> obj = response.body();
                if (obj.size()==0){

                    Toast.makeText(InvoiceItemsActivity.this, "Invoices not found", Toast.LENGTH_SHORT).show();
                }
                setValues(obj);
                adapter = new InvoiceItemAdapter(obj);
                rvItems.setAdapter(adapter);

                hud.dismiss();
            }

            @Override
            public void onFailure(Call<List<InvoiceDetailModel>> call, Throwable t) {
                hud.dismiss();
            }
        });
    }

    private void setValues(List<InvoiceDetailModel> obj) {
        tvInvoiceNo.setText(obj.get(0).InvoiceNumber);
        tvNetAmount.setText(calculateAmt(obj));
        tvDistributors.setText(SavePref.fetchShopName());
        tvRoute.setText(SavePref.fetchRoute());
        tvAddress.setText(SavePref.fetchShopAddress());
    }

    private String calculateAmt(List<InvoiceDetailModel> obj) {
        int amt = 0;
        for (int i=0; i<obj.size(); i++){
            amt = amt+Integer.parseInt(obj.get(i).Amount);
        }
        return String.valueOf(amt);
    }
}
