package com.trio.app.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.trio.app.R;
import com.trio.app.adapters.ItemAdapter;
import com.trio.app.appcontrollers.SavePref;
import com.trio.app.models.CreateInvoiceModel;
import com.trio.app.models.DistributorStockModel;
import com.trio.app.rest.ApiClient;
import com.trio.app.rest.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddItemsActivity extends AppCompatActivity {

    public static String shopId = "";
    public static String quantity = "";
    RecyclerView rvItems;
    ItemAdapter adapter;
    String shopName;
    TextView tvShopName;
    Toolbar toolbar;
    KProgressHUD hud;
    RelativeLayout rlCreateInvoice;
    String productName = "";

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
//                startActivity(new Intent());
                AddItemsActivity.this.finish();
//                AddShopActivity.setValues();
            }
        });
        hud = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setDimAmount(0.5f);
        tvShopName = findViewById(R.id.tvShopName);
        tvShopName.setText(shopName);
        rvItems = findViewById(R.id.rvItems);

        rlCreateInvoice = findViewById(R.id.rlCreateInvoice);
        rlCreateInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                createInvoice();
                AddItemsActivity.this.finish();
            }
        });

        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        rvItems.setLayoutManager(manager);
        getDistributorStock();

    }

    private void createInvoice() {
        String shopId = "0";
        String productName = "";
        String quantity = "";
        String licenceNo = SavePref.getLoginData().LicenseNumber;
        String emailId = SavePref.getLoginData().EmailID;
        hud.show();
        String url = "http://manage.bytepaper.com/Mobile/Manufacturing/index.php?createInvoice&&" + licenceNo + "&&" + emailId + "&&" + shopId + "&&" + productName + "&-&" + quantity;
        ApiInterface apiInterface = ApiClient.getClient();
        Call<CreateInvoiceModel> call = apiInterface.createInvoice(url);
        call.enqueue(new Callback<CreateInvoiceModel>() {
            @Override
            public void onResponse(Call<CreateInvoiceModel> call, Response<CreateInvoiceModel> response) {
                CreateInvoiceModel obj = response.body();
                if (obj.Status.equalsIgnoreCase("Invoice Created")) {
                    AddShopActivity.totalItems = obj.InvoiceNumber;
                }
                hud.dismiss();
            }

            @Override
            public void onFailure(Call<CreateInvoiceModel> call, Throwable t) {
                hud.dismiss();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        AddShopActivity.setValues();
    }

    private void getDistributorStock() {
        String licenceNo = SavePref.getLoginData().LicenseNumber;
        String emailId = SavePref.getLoginData().EmailID;
        hud.show();
        String url = "http://manage.bytepaper.com/Mobile/Manufacturing/index.php?getDistributorStock&&" + licenceNo + "&&" + "5b226adba43a2c5c2c00002a";
        ApiInterface apiInterface = ApiClient.getClient();
        Call<List<DistributorStockModel>> call = apiInterface.getDistributorStock(url);
        call.enqueue(new Callback<List<DistributorStockModel>>() {
            @Override
            public void onResponse(Call<List<DistributorStockModel>> call, Response<List<DistributorStockModel>> response) {
                List<DistributorStockModel> obj = response.body();
                if (obj.size() != 0) {
                    adapter = new ItemAdapter(obj);
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
