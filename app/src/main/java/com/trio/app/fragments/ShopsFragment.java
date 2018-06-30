package com.trio.app.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.trio.app.R;
import com.trio.app.activities.AddShopActivity;
import com.trio.app.activities.MainActivity;
import com.trio.app.adapters.DistributorsAdapter;
import com.trio.app.adapters.ShopsAdapter;
import com.trio.app.appcontrollers.SavePref;
import com.trio.app.models.DistributorsModel;
import com.trio.app.models.ShopModel;
import com.trio.app.rest.ApiClient;
import com.trio.app.rest.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


@SuppressLint("ValidFragment")
public class ShopsFragment extends Fragment {

    public static final String TAG = ShopsFragment.class.getSimpleName();
    MainActivity activity;
    RecyclerView rvShops;
    FloatingActionButton fabCreateShop;
    RecyclerView.LayoutManager manager;
    KProgressHUD hud;
    ShopsAdapter adapter;

    @SuppressLint("ValidFragment")
    public ShopsFragment(MainActivity context) {
        this.activity = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shops, container, false);
        rvShops = view.findViewById(R.id.rvShops);
        fabCreateShop = view.findViewById(R.id.fabCreateShop);
        manager = new LinearLayoutManager(getActivity());
        rvShops.setLayoutManager(manager);
        hud = KProgressHUD.create(getActivity())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setDimAmount(0.5f);
        fabCreateShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddShopActivity.class));
            }
        });
        getShopsList();
        return view;
    }

    private void getShopsList() {
        String licenceNo = SavePref.getLoginData().LicenseNumber;
        String emailId = SavePref.getLoginData().EmailID;
        hud.show();
        String url = "http://manage.bytepaper.com/Mobile/Manufacturing/index.php?shopList&&"+licenceNo+"&&"+emailId;
        ApiInterface apiInterface = ApiClient.getClient();
        Call<List<ShopModel>> call = apiInterface.getShopsList(url);
        call.enqueue(new Callback<List<ShopModel>>() {
            @Override
            public void onResponse(Call<List<ShopModel>> call, Response<List<ShopModel>> response) {
                List<ShopModel> obj = response.body();
                if (obj.size()!=0){
                    adapter = new ShopsAdapter(getActivity(), obj);
                    rvShops.setAdapter(adapter);
                }else {
                    Toast.makeText(getActivity(), "Distributors not found", Toast.LENGTH_SHORT).show();
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
