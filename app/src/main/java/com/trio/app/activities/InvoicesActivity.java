package com.trio.app.activities;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.trio.app.R;
import com.trio.app.adapters.InvoicesAdapter;
import com.trio.app.adapters.ViewPagerAdapter;
import com.trio.app.fragments.InvoicesFragment;

import java.util.ArrayList;
import java.util.List;

public class InvoicesActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    InvoicesAdapter transferCnfRecyclerAdapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    InvoicesFragment invoicesFragment;
    Toolbar toolbar;
    List<String> catNameList = new ArrayList<>();
    FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoices);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.backarrow));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(InvoicesActivity.this, "Wait..", Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView = findViewById(R.id.transfer_cnf_rv);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(InvoicesActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

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
                setupViewPager(viewPager, position);
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

    private void setupViewPager(ViewPager viewPager, int position) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        if (position==0){
            invoicesFragment = new InvoicesFragment();
            adapter.addFragment(invoicesFragment, catNameList.get(position));
        }else if (position==1){
            invoicesFragment = new InvoicesFragment();
            adapter.addFragment(invoicesFragment, catNameList.get(position));
        }else {
            invoicesFragment = new InvoicesFragment();
            adapter.addFragment(invoicesFragment, catNameList.get(position));
        }
    }
}
