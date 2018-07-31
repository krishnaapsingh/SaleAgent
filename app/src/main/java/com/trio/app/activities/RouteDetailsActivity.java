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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.orhanobut.hawk.Hawk;
import com.trio.app.R;
import com.trio.app.adapters.RouteDetailsAdapter;
import com.trio.app.adapters.ShopsAdapter;
import com.trio.app.appcontrollers.SavePref;
import com.trio.app.models.ShopModel;
import com.trio.app.rest.ApiClient;
import com.trio.app.rest.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RouteDetailsActivity extends AppCompatActivity {

    LinearLayout llFilter;
    TextView tvShopCount, tvFilterName;
    Spinner spnFilter;
    RecyclerView rvShop;
    RouteDetailsAdapter adapter;
    Toolbar toolbar;
    private boolean inBackground = false;
    boolean checkBackground = false;
    String routeName;
    KProgressHUD hud;
    LinearLayout llMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_details);

        routeName = getIntent().getStringExtra("routename");
        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.backarrow));
        toolbar.setTitle(routeName);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(InvoicesActivity.this, MainActivity.class));
                RouteDetailsActivity.this.finish();
            }
        });
        hud = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setDimAmount(0.5f);
        llMain = findViewById(R.id.llMain);
        llMain.setVisibility(View.GONE);
        tvShopCount = findViewById(R.id.tvShopCount);
        tvFilterName = findViewById(R.id.tvFilterName);
        spnFilter = findViewById(R.id.spnFilter);
        rvShop = findViewById(R.id.rvShop);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        rvShop.setLayoutManager(mLayoutManager);
        llFilter = findViewById(R.id.llFilter);
        getShopsList();
    }


    private void getShopsList() {
        String userType = SavePref.getLoginData().UserType;
        String licenceNo = SavePref.getLoginData().LicenseNumber;
        String emailId = SavePref.getLoginData().EmailID;
        hud.show();
        String url = "";
        if (userType.equalsIgnoreCase("Sales Agent")) {
            url = "http://manage.bytepaper.com/Mobile/Manufacturing/index.php?shopList&&" + licenceNo + "&&" + emailId;

        } else if (userType.equalsIgnoreCase("Admin")) {
            url = "http://manage.bytepaper.com/Mobile/Manufacturing/index.php?AllshopList&&" + licenceNo;
        }

//        String url = "http://manage.bytepaper.com/Mobile/Manufacturing/index.php?shopList&&"+licenceNo+"&&"+emailId;
        ApiInterface apiInterface = ApiClient.getClient();
        Call<List<ShopModel>> call = apiInterface.getShopsList(url);
        call.enqueue(new Callback<List<ShopModel>>() {
            @Override
            public void onResponse(Call<List<ShopModel>> call, Response<List<ShopModel>> response) {
                List<ShopModel> obj = response.body();
                List<ShopModel> obj1 = new ArrayList<>();
                if (obj.size() != 0) {
                    for (int i = 0; i < obj.size(); i++) {
                        if (obj.get(i).Route.equalsIgnoreCase(routeName)) {
                            obj1.add(obj.get(i));
                        }
                    }
                    llMain.setVisibility(View.VISIBLE);
                    tvShopCount.setText(obj1.size() + " " + "Shops");
                    adapter = new RouteDetailsAdapter(RouteDetailsActivity.this, obj1);
                    rvShop.setAdapter(adapter);
                } else {
                    Toast.makeText(RouteDetailsActivity.this, "Shops not found", Toast.LENGTH_SHORT).show();
                }
                hud.dismiss();
            }

            @Override
            public void onFailure(Call<List<ShopModel>> call, Throwable t) {
                hud.dismiss();
            }
        });
    }
}
