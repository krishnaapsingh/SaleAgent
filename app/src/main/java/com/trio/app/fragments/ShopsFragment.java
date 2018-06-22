package com.trio.app.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.trio.app.R;
import com.trio.app.activities.MainActivity;
import com.trio.app.appcontrollers.SavePref;
import com.trio.app.models.ConfirmSaleByUser;
import com.trio.app.models.FxCoinValue;
import com.trio.app.rest.ApiClient;
import com.trio.app.rest.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


@SuppressLint("ValidFragment")
public class ShopsFragment extends Fragment {
    public static final String TAG = ShopsFragment.class.getSimpleName();

    public static String name, username, noofsalecoin, unitcost, amount, commission, saleamount, spistatus, saleid;

    TextView tvName, tvUsername, tvnoofsale, tvUnitCost, tvTotalAmount, tvCommission, tvSaleAmount,
            tvTotalCoin, tvTotalUserAvilCoin;
    Spinner spinnerStatus;

    Button btnConfirm;
    MainActivity activity;
    int pos=0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sale_coin_confirm_manual, container, false);
        setLayout(view);
//        setValues();
//        updateCoinValue();
        return view;
    }

    private void setValues() {
        String availCoin = getResources().getString(R.string.totalavailcoin)+SavePref.fetchCoinValue().data.get(0).avl_coin;
        String userCoin = getResources().getString(R.string.fxcoin)+SavePref.fetchCoinValue().user_coin.get(0).coin;
        String coinVAlue = getResources().getString(R.string.fxcoin)+SavePref.fetchCoinValue().data.get(0).value;

        tvTotalCoin.setText(availCoin);
        tvTotalUserAvilCoin.setText(userCoin);
        tvName.setText(name);
        tvUsername.setText(username);
        tvnoofsale.setText(noofsalecoin);
        tvUnitCost.setText(coinVAlue);
        tvTotalAmount.setText(amount);
        tvCommission.setText(commission);
        tvSaleAmount.setText(saleamount);
    }

    private void updateCoinValue() {

//        ApiInterface apiInterface = ApiClient.getClient();
//        Call<FxCoinValue> fxCoinValueCall = apiInterface.fxCoinValue(SavePref.getLoginData().data.id);
//        fxCoinValueCall.enqueue(new Callback<FxCoinValue>() {
//            @Override
//            public void onResponse(Call<FxCoinValue> call, Response<FxCoinValue> response) {
//                FxCoinValue fxCoinValue = response.body();
//                if (fxCoinValue.status) {
//                    SavePref.saveCoinValue(fxCoinValue);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<FxCoinValue> call, Throwable t) {
//            }
//        });

    }

    private void setLayout(View view) {

        tvTotalCoin = view.findViewById(R.id.cnfsalemnl_tv_total_avail_coins);
        tvTotalUserAvilCoin = view.findViewById(R.id.cnfsalemnl_tv_user_available_coin);
        tvName = view.findViewById(R.id.cnfsalemnl_tv_name);
        tvUsername = view.findViewById(R.id.cnfsalemnl_tv_username);
        tvnoofsale = view.findViewById(R.id.cnfsalemnl_tv_noofsalecoin);
        tvUnitCost = view.findViewById(R.id.cnfsalemnl_tv_unitcost);
        tvTotalAmount = view.findViewById(R.id.cnfsalemnl_tv_amount);
        tvCommission = view.findViewById(R.id.cnfsalemnl_tv_commission);
        tvSaleAmount = view.findViewById(R.id.cnfsalemnl_tv_sale_amount);
        spinnerStatus = view.findViewById(R.id.cnfsalemnl_tv_status);

        btnConfirm = view.findViewById(R.id.cnfsalemnl_btn_confirm);
        spinnerStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    pos=0;
                }else {
                    pos=1;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pos==1) {
                    SaleConfirmManually();
                }else{
                    activity.onClick(3);
                }
            }
        });
    }

    private void SaleConfirmManually() {
//        ApiInterface apiInterface = ApiClient.getClient();
//        Call<ConfirmSaleByUser> confirmSaleByUserCall = apiInterface.saleConfirmByUser(SavePref.getLoginData().data.id, noofsalecoin,
//                saleid, spinnerStatus.getSelectedItem().toString());
//        confirmSaleByUserCall.enqueue(new Callback<ConfirmSaleByUser>() {
//            @Override
//            public void onResponse(Call<ConfirmSaleByUser> call, Response<ConfirmSaleByUser> response) {
//                ConfirmSaleByUser saleByUser = response.body();
//                if (saleByUser.status) {
//                    activity.onClick(3);
//                    Toast.makeText(getActivity(), saleByUser.message, Toast.LENGTH_SHORT).show();
//
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ConfirmSaleByUser> call, Throwable t) {
//
//            }
//        });
    }

    @SuppressLint("ValidFragment")
    public ShopsFragment(MainActivity context) {
        this.activity=context;
    }


}
