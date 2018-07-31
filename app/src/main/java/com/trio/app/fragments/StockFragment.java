package com.trio.app.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.trio.app.R;
import com.trio.app.activities.DistributorStockActivity;
import com.trio.app.activities.MainActivity;
import com.trio.app.adapters.DistributorStockAdapter;
import com.trio.app.adapters.DistributorsAdapter;
import com.trio.app.appcontrollers.SavePref;
import com.trio.app.models.DistributorStockModel;
import com.trio.app.models.DistributorsModel;
import com.trio.app.rest.ApiClient;
import com.trio.app.rest.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by User on 12-Jan-18.
 */

@SuppressLint("ValidFragment")
public class StockFragment extends Fragment {

    public static final String TAG = StockFragment.class.getSimpleName();

    MainActivity activity;
    DistributorStockAdapter adapter;
    KProgressHUD hud;

    TextView tvStockCount;
    RecyclerView recyclerView;
    LinearLayout llMain;
    RelativeLayout rlAddStock;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stock, container, false);
        llMain = view.findViewById(R.id.llMain);
        rlAddStock = view.findViewById(R.id.rlAddStock);
        tvStockCount = view.findViewById(R.id.tvStockCount);
        recyclerView = view.findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        //recyclerView.setItemAnimator(new DefaultItemAnimator());

        hud = KProgressHUD.create(getActivity())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setDimAmount(0.5f);
        getDistributorStock();
        return view;
    }

    private void getDistributorStock() {
        String licenceNo = SavePref.getLoginData().LicenseNumber;
        String Id = SavePref.getLoginData().ID;
        hud.show();

        String url = "http://manage.bytepaper.com/Mobile/Manufacturing/index.php?getDistributorStock&&"+licenceNo+"&&"+Id;
        ApiInterface apiInterface = ApiClient.getClient();
        Call<List<DistributorStockModel>> call = apiInterface.getDistributorStock(url);
        call.enqueue(new Callback<List<DistributorStockModel>>() {
            @Override
            public void onResponse(Call<List<DistributorStockModel>> call, Response<List<DistributorStockModel>> response) {
                List<DistributorStockModel> obj = response.body();
                if (obj.size()!=0){
                    llMain.setVisibility(View.VISIBLE);
                    adapter = new DistributorStockAdapter(obj);
                    recyclerView.setAdapter(adapter);
                    ItemCount(obj);
                }else {
                    Toast.makeText(getActivity(), "Distributor's Stock is Empty", Toast.LENGTH_SHORT).show();
                }
                hud.dismiss();
            }

            @Override
            public void onFailure(Call<List<DistributorStockModel>> call, Throwable t) {
                hud.dismiss();
            }
        });
    }

    private void ItemCount(List<DistributorStockModel> obj) {
        int item=0;
        int total=0;
        for (int i =0;i<obj.size(); i++){
            item = Integer.parseInt(obj.get(i).Stock);
            total = item+total;
        }
        String value = "Available Items "+String.valueOf(total);
        tvStockCount.setText(value);
    }


    @SuppressLint("ValidFragment")
    public StockFragment(MainActivity context) {
        this.activity=context;
    }






}
