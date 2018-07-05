package com.trio.app.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.orhanobut.hawk.Hawk;
import com.trio.app.R;
import com.trio.app.adapters.ViewPagerAdapter;
import com.trio.app.appcontrollers.SavePref;
import com.trio.app.fragments.InvoicesFragment;
import com.trio.app.models.InvoiceModel;
import com.trio.app.models.ShopModel;
import com.trio.app.rest.ApiClient;
import com.trio.app.rest.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InvoicesActivity extends AppCompatActivity {

    InvoicesFragment invoicesFragment;
    Toolbar toolbar;
    List<String> catNameList = new ArrayList<>();
    FloatingActionButton fab;
    RelativeLayout rlShopChange;
    KProgressHUD hud;
    List<String> routeList = new ArrayList<>();
    AlertDialog alertDialog1;
    String route = "";
    TextView tvShopName;
    List<ShopModel> obj = new ArrayList<>();
    String shopid1="";
    String shopid="";
    //    RecyclerView recyclerView;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    RelativeLayout rlViewPager;
    CardView cvNotFound;
    private boolean inBackground = false;
    boolean checkBackground = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoices);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.backarrow));
        toolbar.setTitle("Invoices");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(InvoicesActivity.this, MainActivity.class));
                InvoicesActivity.this.finish();
            }
        });
        hud = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setDimAmount(0.5f);
        tvShopName = findViewById(R.id.tvShopName);
        rlShopChange = findViewById(R.id.rlShopChange);
        fab = findViewById(R.id.fab);
        fab.setVisibility(View.GONE);

        String check = getIntent().getStringExtra("check");
        if (check.equalsIgnoreCase("0")){
            getShopsList();
        }else {
            fab.setVisibility(View.VISIBLE);
            rlShopChange.setVisibility(View.INVISIBLE);
            tvShopName.setText(getIntent().getStringExtra("shopname"));
            SavePref.saveShopName(getIntent().getStringExtra("shopname"));
            shopid = getIntent().getStringExtra("shopid");
            SavePref.saveShopId(shopid);
            getInvoiceList();

        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InvoicesActivity.this, CreateInvoiceActivity.class));
            }
        });
        rlViewPager = findViewById(R.id.rlViewPager);
        cvNotFound = findViewById(R.id.cvNotFound);
        rlViewPager.setVisibility(View.GONE);
        cvNotFound.setVisibility(View.GONE);


        rlShopChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogForRoute();
            }
        });

        tabLayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(3);
        tabLayout.setupWithViewPager(viewPager);

        catNameList.add("All Invoices");
        catNameList.add("Paid");
        catNameList.add("Outstanding");

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                viewPager.setCurrentItem(position, false);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }


    private void getInvoiceList() {
        String licenceNo = SavePref.getLoginData().LicenseNumber;
        String emailId = SavePref.getLoginData().EmailID;
        hud.show();
        String url = "http://manage.bytepaper.com/Mobile/Manufacturing/index.php?listInvoice&&" + licenceNo + "&&" + emailId + "&&" + shopid;
        ApiInterface apiInterface = ApiClient.getClient();
        Call<List<InvoiceModel>> call = apiInterface.getInvoiceList(url);
        call.enqueue(new Callback<List<InvoiceModel>>() {
            @Override
            public void onResponse(Call<List<InvoiceModel>> call, Response<List<InvoiceModel>> response) {
                List<InvoiceModel> obj = response.body();
                if (obj.size() != 0) {
                    rlViewPager.setVisibility(View.VISIBLE);
                    cvNotFound.setVisibility(View.GONE);
                    setupViewPager(viewPager, obj);
                } else {
                    cvNotFound.setVisibility(View.VISIBLE);
                    rlViewPager.setVisibility(View.GONE);
                    obj.clear();
//                    setupViewPager(viewPager, obj);
                    Toast.makeText(InvoicesActivity.this, "Invoices not found for this shop", Toast.LENGTH_SHORT).show();
                }

                hud.dismiss();
            }

            @Override
            public void onFailure(Call<List<InvoiceModel>> call, Throwable t) {
                hud.dismiss();
            }
        });
    }

    private void getShopsList() {
        String licenceNo = SavePref.getLoginData().LicenseNumber;
        String emailId = SavePref.getLoginData().EmailID;
        hud.show();
        String url = "http://manage.bytepaper.com/Mobile/Manufacturing/index.php?shopList&&" + licenceNo + "&&" + emailId;
        ApiInterface apiInterface = ApiClient.getClient();
        Call<List<ShopModel>> call = apiInterface.getShopsList(url);
        call.enqueue(new Callback<List<ShopModel>>() {
            @Override
            public void onResponse(Call<List<ShopModel>> call, Response<List<ShopModel>> response) {
                obj = response.body();
                if (obj.size() != 0) {
                    for (int i = 0; i < obj.size(); i++) {
                        routeList.add(obj.get(i).Shop);
                    }
                    alertDialogForRoute();
                }
                hud.dismiss();
            }

            @Override
            public void onFailure(Call<List<ShopModel>> call, Throwable t) {
                hud.dismiss();
            }
        });
    }

    private void alertDialogForRoute() {
        String[] array = new String[routeList.size()];
        routeList.toArray(array);

        AlertDialog.Builder builder = new AlertDialog.Builder(InvoicesActivity.this);

        builder.setTitle("Select Your Route");

        builder.setSingleChoiceItems(array, -1, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int item) {
                fab.setVisibility(View.VISIBLE);
                route = routeList.get(item);
                SavePref.saveShopName(route);
                SavePref.saveRoute(obj.get(item).Route);
                SavePref.saveShopAddress(obj.get(item).AreaName+" "+obj.get(item).CityName+" "+obj.get(item).StateName);
                shopid = obj.get(item).ID;
                SavePref.saveShopId(shopid);
                alertDialog1.dismiss();
                tvShopName.setText(route);
                if (!shopid1.equalsIgnoreCase(shopid)){
                    getInvoiceList();
                }
                shopid1 = shopid;

            }
        });
        alertDialog1 = builder.create();
        alertDialog1.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!shopid.equalsIgnoreCase("")){
            getInvoiceList();
        }
    }

    @Override
    public void onBackPressed() {
//        startActivity(new Intent(InvoicesActivity.this, MainActivity.class));
        InvoicesActivity.this.finish();
    }

    private void setupViewPager(ViewPager viewPager, List<InvoiceModel> obj) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        invoicesFragment.list = obj;

//        if (obj.size()!=0) {

            for (int i = 0; i < catNameList.size(); i++) {
                if (i == 0) {
                    invoicesFragment = new InvoicesFragment (InvoicesActivity.this);
                    invoicesFragment.list = obj;
                    adapter.addFragment(invoicesFragment, catNameList.get(i));
                } else if (i == 1) {
                    invoicesFragment = new InvoicesFragment( InvoicesActivity.this);
                    adapter.addFragment(invoicesFragment, catNameList.get(i));
                } else {
                    invoicesFragment = new InvoicesFragment(InvoicesActivity.this);
                    adapter.addFragment(invoicesFragment, catNameList.get(i));
                }
            }
            if (obj.size()!=0) {
                viewPager.setAdapter(adapter);
            }
//        }

    }
}
