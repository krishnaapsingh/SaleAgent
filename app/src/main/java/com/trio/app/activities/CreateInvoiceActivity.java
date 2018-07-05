package com.trio.app.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
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
import com.trio.app.adapters.ItemAdapter1;
import com.trio.app.appcontrollers.SavePref;
import com.trio.app.models.CreateInvoiceModel;
import com.trio.app.models.DistributorStockModel;
import com.trio.app.rest.ApiClient;
import com.trio.app.rest.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateInvoiceActivity extends AppCompatActivity {
    public static String productName;
    RecyclerView rvItems;
    ItemAdapter1 adapter;
    String shopName;
    TextView tvShopName;
    Toolbar toolbar;
    KProgressHUD hud;
    RelativeLayout rlCreateInvoice;
    public static String quantity;
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
        tvShopName = findViewById(R.id.tvShopName);
        tvShopName.setText(SavePref.fetchShopName());
        rvItems = findViewById(R.id.rvItems);

        rlCreateInvoice = findViewById(R.id.rlCreateInvoice);
        rlCreateInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createInvoice();
//                CreateInvoiceActivity.this.finish();
            }
        });

        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        rvItems.setLayoutManager(manager);
        getDistributorStock();

    }

    private void getDistributorStock() {
        String licenceNo = SavePref.getLoginData().LicenseNumber;
        String emailId = SavePref.getLoginData().EmailID;
        hud.show();
        String url = "http://manage.bytepaper.com/Mobile/Manufacturing/index.php?getDistributorStock&&" + licenceNo + "&&" + SavePref.fetchDistributorId();
        ApiInterface apiInterface = ApiClient.getClient();
        Call<List<DistributorStockModel>> call = apiInterface.getDistributorStock(url);
        call.enqueue(new Callback<List<DistributorStockModel>>() {
            @Override
            public void onResponse(Call<List<DistributorStockModel>> call, Response<List<DistributorStockModel>> response) {
                List<DistributorStockModel> obj = response.body();
                if (obj.size() != 0) {
                    adapter = new ItemAdapter1(obj);
                    productName = obj.get(0).Name;
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

    private void createInvoice() {
        String shopId = SavePref.fetchShopId();
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
                    CreateInvoiceActivity.this.finish();
//                    AddShopActivity.totalItems = obj.InvoiceNumber;
                }
                hud.dismiss();
            }

            @Override
            public void onFailure(Call<CreateInvoiceModel> call, Throwable t) {
                hud.dismiss();
            }
        });
    }
}
