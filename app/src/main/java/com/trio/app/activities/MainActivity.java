package com.trio.app.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.trio.app.R;
import com.trio.app.appcontrollers.SavePref;
import com.trio.app.fragments.HomeFragment;
import com.trio.app.fragments.DistributorsFragment;
import com.trio.app.fragments.InvoicesFragment;
import com.trio.app.fragments.ProfileFragment;
import com.trio.app.fragments.ReportsFragment;
import com.trio.app.fragments.RoutesFragment;
import com.trio.app.fragments.ShopsFragment;
import com.trio.app.models.DistributorsModel;
import com.trio.app.rest.ApiClient;
import com.trio.app.rest.ApiInterface;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    int check = 0;
    Toolbar toolbar;
    FrameLayout flContainer;
    DrawerLayout drawer;
    String title;
    private boolean inBackground = false;
    boolean checkBackground = false;

//    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        flContainer = findViewById(R.id.fl_container);
        setSupportActionBar(toolbar);

        getDistributorsList();

        getView();

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        CircleImageView imageView = navigationView.getHeaderView(0).findViewById(R.id.nav_iv_profile);
        Glide.with(MainActivity.this)
                .load(SavePref.getLoginData().UserPic)
                .centerCrop()
//                .placeholder(R.drawable.profile1)
                .error(R.drawable.profile1)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH)
                .into(imageView);

        TextView name = navigationView.getHeaderView(0).findViewById(R.id.usrname);
        TextView usremail = navigationView.getHeaderView(0).findViewById(R.id.usremail);
        name.setText(SavePref.getLoginData().UserName);
        usremail.setText(SavePref.getLoginData().EmailID);

    }

    private void getDistributorsList() {
        String licenceNo = SavePref.getLoginData().LicenseNumber;
        String emailId = SavePref.getLoginData().EmailID;
        String url = "http://manage.bytepaper.com/Mobile/Manufacturing/index.php?getMappedDsitributor&&"+licenceNo+"&&"+emailId;
        ApiInterface apiInterface = ApiClient.getClient();
        Call<List<DistributorsModel>> call = apiInterface.getDistributors(url);
        call.enqueue(new Callback<List<DistributorsModel>>() {
            @Override
            public void onResponse(Call<List<DistributorsModel>> call, Response<List<DistributorsModel>> response) {
                List<DistributorsModel> obj = response.body();
                if (obj.size()!=0){
                    SavePref.saveDistributorId(obj.get(0).ID);
                }

            }

            @Override
            public void onFailure(Call<List<DistributorsModel>> call, Throwable t) {
            }
        });
    }

//    @Override
//    public void onResume() {
//        inBackground = false;
//
//        if (checkBackground) {
//            alertDialogForSessionTimeOut();
//        }
//        super.onResume();
//    }
//
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
//                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
//                    MainActivity.this.finish();
//                }
//            }
//        }, 2000);
//
//    }
//
//    @Override
//    public void onPause() {
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

    private void getView() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_container, new HomeFragment(this)).commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (check == 0) {
            exitByBackKey();
        } else {
            toolbar.setTitle("Sales Agent");
            Fragment fragment = new HomeFragment(this);
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
                check = 0;
                Intent i = new Intent(MainActivity.this, InvoicesActivity.class);
                i.putExtra("check", "0");
                startActivity(i);
                break;
            case R.id.nav_reports:
                title = "Reports";
                fragment = new ReportsFragment(this);
                tag = ReportsFragment.TAG;
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
                fragment = new HomeFragment(this);
                title = "Sales Agent";
                tag = HomeFragment.TAG;
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
            case 0:
                title = "Sales Agent";
                fragment = new HomeFragment(this);
                tag = HomeFragment.TAG;
                check = 0;
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fl_container, fragment, tag)
                        .commit();
                toolbar.setTitle(title);
                break;
            case 1:
                title = "Distributors";
                fragment = new DistributorsFragment(this);
                tag = DistributorsFragment.TAG;
                check = 1;
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fl_container, fragment, tag)
                        .commit();
                toolbar.setTitle(title);
                break;
            case 2:
                title = "Routes";
                fragment = new RoutesFragment(this);
                tag = RoutesFragment.TAG;
                check = 1;
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fl_container, fragment, tag)
                        .commit();
                toolbar.setTitle(title);
                break;
            case 3:
                title = "Shops";
                fragment = new ShopsFragment(this);
                tag = ShopsFragment.TAG;
                check = 1;
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fl_container, fragment, tag)
                        .commit();
                toolbar.setTitle(title);
                break;
            case 4:
                check = 0;
                Intent i1 = new Intent(MainActivity.this, InvoicesActivity.class);
                i1.putExtra("check", "0");
                startActivity(i1);
                break;
            case 5:
                title = "Reports";
                fragment = new ReportsFragment(this);
                tag = ReportsFragment.TAG;
                check = 1;
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fl_container, fragment, tag)
                        .commit();
                toolbar.setTitle(title);

                break;
            case 6:
                title = "Profile";
                fragment = new ProfileFragment(this);
                tag = InvoicesFragment.TAG;
                check = 1;
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fl_container, fragment, tag)
                        .commit();
                toolbar.setTitle(title);
                break;
            default:
                title = "Sales Agent";
                fragment = new HomeFragment(this);
                tag = HomeFragment.TAG;
                check = 0;
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fl_container, fragment, tag)
                        .commit();
                toolbar.setTitle(title);
                break;
        }


    }


}
