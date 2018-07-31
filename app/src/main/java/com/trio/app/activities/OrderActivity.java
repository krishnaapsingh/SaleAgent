package com.trio.app.activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.trio.app.R;
import com.trio.app.adapters.ViewPagerAdapter;
import com.trio.app.appcontrollers.SavePref;
import com.trio.app.fragments.InvoicesFragment;
import com.trio.app.models.InvoiceModel;
import com.trio.app.rest.ApiClient;
import com.trio.app.rest.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderActivity extends AppCompatActivity {

    Toolbar toolbar;
    InvoicesFragment invoicesFragment;
    KProgressHUD hud;
    RelativeLayout rlViewPager;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    ViewPager viewpager;
    CardView cvNotFound;
    List<String> catNameList = new ArrayList<>();
    FloatingActionButton fab;
    List<InvoiceModel> obj;
    String userType = SavePref.getLoginData().UserType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.backarrow));
        toolbar.setTitle("Orders");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(InvoicesActivity.this, MainActivity.class));
                OrderActivity.this.finish();
            }
        });
        hud = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setDimAmount(0.5f);

        fab = findViewById(R.id.fab);
        rlViewPager = findViewById(R.id.rlViewPager);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        cvNotFound = findViewById(R.id.cvNotFound);
        cvNotFound.setVisibility(View.GONE);
        rlViewPager.setVisibility(View.GONE);

        getOrderList();

        viewPager.setOffscreenPageLimit(3);
        tabLayout.setupWithViewPager(viewPager);
        catNameList.add("All Orders");
        catNameList.add("Cleared");
        catNameList.add("Remaining");

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                viewPager.setCurrentItem(position, true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderActivity.this, CreateInvoiceActivity.class);
                intent.putExtra("shopname", "");
                intent.putExtra("actname", "OrderFragment");
                startActivity(intent);
            }
        });
    }

    private void getOrderList() {
        hud.show();
        String licenceNo = SavePref.getLoginData().LicenseNumber;
        String Id = SavePref.getLoginData().ID;
        String url = "http://manage.bytepaper.com/Mobile/Manufacturing/index.php?listOrder&&" + licenceNo + "&&" + Id;

        ApiInterface apiInterface = ApiClient.getClient();
        Call<List<InvoiceModel>> call = apiInterface.getOrderList(url);
        call.enqueue(new Callback<List<InvoiceModel>>() {
            @Override
            public void onResponse(Call<List<InvoiceModel>> call, Response<List<InvoiceModel>> response) {
                obj = response.body();
                if (obj.size() != 0) {
                    setupViewPager(viewPager, obj);
                    cvNotFound.setVisibility(View.GONE);
                    rlViewPager.setVisibility(View.VISIBLE);
                } else {
                    cvNotFound.setVisibility(View.VISIBLE);
                    rlViewPager.setVisibility(View.GONE);
                    Toast.makeText(OrderActivity.this, "Order not found ", Toast.LENGTH_SHORT).show();
                }
                hud.dismiss();
            }

            @Override
            public void onFailure(Call<List<InvoiceModel>> call, Throwable t) {
                cvNotFound.setVisibility(View.VISIBLE);
                rlViewPager.setVisibility(View.GONE);
                hud.dismiss();
            }
        });
    }

    private void setupViewPager(ViewPager viewPager, List<InvoiceModel> obj) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
//        invoicesFragment.list = obj;
        InvoicesFragment.list = obj;
        InvoicesFragment.list1 = listShort(obj);
        InvoicesFragment.list2 = listShort1(obj);

        for (int i = 0; i < catNameList.size(); i++) {
            invoicesFragment = new InvoicesFragment(OrderActivity.this, i);
            adapter.addFragment(invoicesFragment, catNameList.get(i));


        }
        adapter.notifyDataSetChanged();
        if (obj.size() != 0) {
            viewPager.setAdapter(adapter);
        }

    }

    public List<InvoiceModel> listShort(List<InvoiceModel> obj) {
        List<InvoiceModel> obj1 = new ArrayList<>();
        for (int i = 0; i < obj.size(); i++) {
            if (obj.get(i).OrderStaus.equalsIgnoreCase("Paid")) {
                obj1.add(obj.get(i));
            }
        }
        return obj1;
    }

    public List<InvoiceModel> listShort1(List<InvoiceModel> obj) {
        List<InvoiceModel> obj1 = new ArrayList<>();
        for (int i = 0; i < obj.size(); i++) {
            if (obj.get(i).OrderStaus.equalsIgnoreCase("Pending")) {
                obj1.add(obj.get(i));
            }
        }
        return obj1;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getOrderList();
    }
}
