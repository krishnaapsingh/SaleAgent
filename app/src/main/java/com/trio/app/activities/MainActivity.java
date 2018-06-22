package com.trio.app.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.trio.app.R;
import com.trio.app.appcontrollers.SavePref;
import com.trio.app.fragments.DashboardFragment;
import com.trio.app.fragments.DistributorsFragment;
import com.trio.app.fragments.InvoicesFragment;
import com.trio.app.fragments.ProfileFragment;
import com.trio.app.fragments.ReportsFragment;
import com.trio.app.fragments.RoutesFragment;
import com.trio.app.fragments.ShopsFragment;
import com.trio.app.fragments.TransferCoinFragment;
import com.trio.app.models.Login;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    int check = 0;
    Toolbar toolbar;
    FrameLayout flContainer;
    DrawerLayout drawer;
    String title;
    Login login;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        flContainer = findViewById(R.id.fl_container);
        setSupportActionBar(toolbar);


        getView();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        CircleImageView imageView = navigationView.getHeaderView(0).findViewById(R.id.nav_iv_profile);
//        Login object = SavePref.getLoginData();

        TextView name = (TextView) navigationView.getHeaderView(0).findViewById(R.id.usrname);
        TextView usremail = (TextView) navigationView.getHeaderView(0).findViewById(R.id.usremail);
        name.setText(SavePref.getUserName());

//        usremail.setText(object.data.username);

    }

    private void getView() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_container, new DashboardFragment(this)).commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (check == 0) {
            exitByBackKey();
        } else {
            toolbar.setTitle("DashBoard");
            Fragment fragment = new DashboardFragment(this);
            check = 0;
            getSupportFragmentManager().beginTransaction().replace(R.id.fl_container, fragment).commit();
        }
    }

    private void exitByBackKey() {

        new AlertDialog.Builder(this)
                .setMessage("Do you want to exit application?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        String tag = null;
        Fragment fragment = null;

        switch (id) {
            case R.id.nav_distri:
                title = "Distributors";
                fragment = new DistributorsFragment(this);
                tag = DistributorsFragment.TAG;
                check = 1;
                fragmmentTrans(fragment, tag);

                break;
            case R.id.nav_routes:
                title = "Routes";
                fragment = new RoutesFragment(this);
                tag = RoutesFragment.TAG;
                check = 1;
                fragmmentTrans(fragment, tag);

                break;
            case R.id.nav_shops:
                title = "Shops";
                fragment = new ShopsFragment(this);
                tag = DistributorsFragment.TAG;
                check = 1;
                fragmmentTrans(fragment, tag);

                break;
            case R.id.nav_invoices:
//                title = "Invoices";
////                fragment = new ReportsFragment(this);
//                tag = ReportsFragment.TAG;
//                startActivity(new Intent(MainActivity.this, InvoicesActivity.class));
//                check = 1;
////                fragmmentTrans(fragment, tag);

                break;
            case R.id.nav_reports:
                title = "Reports";
                fragment = new ReportsFragment(this);
                tag = TransferCoinFragment.TAG;
                check = 1;
                fragmmentTrans(fragment, tag);

                break;
            case R.id.nav_profile:
                title = "Profile";
                fragment = new ProfileFragment(this);
                tag = InvoicesFragment.TAG;
                check = 1;
                fragmmentTrans(fragment, tag);

                break;
            default:
                fragment = new DashboardFragment(this);
                title = "Dashboard";
                tag = DashboardFragment.TAG;
                check = 0;
                fragmmentTrans(fragment, tag);
                break;

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void fragmmentTrans(Fragment fragment, String tag) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fl_container, fragment, tag)
                .commit();
        toolbar.setTitle(title);
    }


    public void onClick(int i) {
        int id = i;
        String tag;
        Fragment fragment = null;
        switch (id) {
            case 1:
                title = "Distributors";
                fragment = new DistributorsFragment(this);
                tag = DistributorsFragment.TAG;
                check = 1;
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fl_container, fragment, tag)
                        .commit();
                break;
            case 2:
                title = "Routes";
                fragment = new RoutesFragment(this);
                tag = RoutesFragment.TAG;
                check = 1;
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fl_container, fragment, tag)
                        .commit();
                break;
            case 3:
                title = "Shops";
                fragment = new ShopsFragment(this);
                tag = ShopsFragment.TAG;
                check = 1;
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fl_container, fragment, tag)
                        .commit();

                break;
            case 4:
                title = "Invoices ";
//                startActivity(new Intent(MainActivity.this, InvoicesActivity.class));
////                fragment = new InvoicesFragment(this);
//                tag = InvoicesFragment.TAG;
//                check = 1;

                break;
            case 5:
                title = "Reports";
                fragment = new ReportsFragment(this);
                tag = ReportsFragment.TAG;
                check = 1;
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fl_container, fragment, tag)
                        .commit();

                break;
            case 6:
                title = "Profile";
                fragment = new ProfileFragment(this);
                tag = InvoicesFragment.TAG;
                check = 1;
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fl_container, fragment, tag)
                        .commit();
                break;
            default:
                title = "Dashboard";
                fragment = new DashboardFragment(this);
                tag = DashboardFragment.TAG;
                check = 0;
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fl_container, fragment, tag)
                        .commit();
                break;
        }

        toolbar.setTitle(title);
    }


}
