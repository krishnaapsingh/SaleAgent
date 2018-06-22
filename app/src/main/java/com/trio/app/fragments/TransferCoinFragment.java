package com.trio.app.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.trio.app.activities.MainActivity;
import com.trio.app.appcontrollers.AppUtility;
import com.trio.app.R;
import com.trio.app.appcontrollers.SavePref;
import com.trio.app.models.FxCoinValue;
import com.trio.app.models.Login;
import com.trio.app.models.TransferFxCoin;
import com.trio.app.models.UpdateFxCoin;
import com.trio.app.rest.ApiClient;
import com.trio.app.rest.ApiInterface;
import com.wang.avi.AVLoadingIndicatorView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by User on 12-Jan-18.
 */

@SuppressLint("ValidFragment")
public class TransferCoinFragment extends Fragment {

    public static final String TAG = TransferCoinFragment.class.getSimpleName();
    TextView tvCoinValue, tvUserAvailableCoin;
    EditText etNoOfCoinTransfer, etcoinsRecieverUserName, etcoinsRecieverUserMobileNo, etOtp;
    RelativeLayout otpScreen;
    LinearLayout llMain;
    Button btnTransfer, btnOtpNext;
    AVLoadingIndicatorView avLoadingIndicatorView;
    String userId, userName, name, availableCoins, noOfTransferCoin, coinValue, recieverobileNo, recieverName, otp;
    MainActivity activity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transfer_coin, container, false);

        getView(view);
        avLoadingIndicatorView.hide();
//        dataFromSavePref();
        updateCoinValue();
        return view;
    }

    @SuppressLint("ValidFragment")
    public TransferCoinFragment(MainActivity context) {
        this.activity=context;
    }

    private void getView(final View view) {

//        tvCoinValue = view.findViewById(R.id.transfercoin_tv_coin_value);
//        tvUserAvailableCoin = view.findViewById(R.id.transfer_coin_tv_available_coin);
//        etNoOfCoinTransfer = view.findViewById(R.id.transfercoin_et_noofcoin_transfer);
//        etcoinsRecieverUserMobileNo = view.findViewById(R.id.transfercoin_et_transfer_mobile);
//        etcoinsRecieverUserName = view.findViewById(R.id.transfercoin_et_transfer_name);
//        btnTransfer = view.findViewById(R.id.transfer_btn_transfer);
//        llMain = view.findViewById(R.id.transfer_main);
//        otpScreen = view.findViewById(R.id.rl_otp_transfer);
//        avLoadingIndicatorView = view.findViewById(R.id.transfer_avi);


        btnTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noOfTransferCoin = etNoOfCoinTransfer.getText().toString().trim();
                recieverobileNo = etcoinsRecieverUserMobileNo.getText().toString().trim();
                recieverName = etcoinsRecieverUserName.getText().toString().trim();
                validation(view);
            }
        });
    }


    private void updateCoinValue() {

//        ApiInterface apiInterface = ApiClient.getClient();
//        Call<FxCoinValue> fxCoinValueCall = apiInterface.fxCoinValue(SavePref.getLoginData().data.id);
//        fxCoinValueCall.enqueue(new Callback<FxCoinValue>() {
//            @Override
//            public void onResponse(Call<FxCoinValue> call, Response<FxCoinValue> response) {
//                FxCoinValue fxCoinValue = response.body();
//                if (fxCoinValue.status) {
//                   SavePref.saveCoinValue(fxCoinValue);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<FxCoinValue> call, Throwable t) {
//
//                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//
//    }
//
//
//    private void dataFromSavePref() {
//        Login login = SavePref.getLoginData();
//        userId = login.data.id;
//        userName = login.data.username;
//        name = login.data.name;
//
//
//        coinValue = SavePref.fetchCoinValue().data.get(0).value;
//        availableCoins = SavePref.fetchCoinValue().user_coin.get(0).coin;
//        tvCoinValue.setText(coinValue);
//        tvUserAvailableCoin.setText(availableCoins);

    }

    private void validation(View view) {
        if (!TextUtils.isEmpty(etNoOfCoinTransfer.getText().toString().trim()) &&
                !TextUtils.isEmpty(etcoinsRecieverUserName.getText().toString().trim()) &&
                !TextUtils.isEmpty(etcoinsRecieverUserMobileNo.getText().toString().trim()) &&
                etcoinsRecieverUserMobileNo.getText().toString().trim().length() ==10)
        {
            avLoadingIndicatorView.show();
            disableView();
            transferCoin(view);
        }else
        {
            Toast.makeText(getActivity(), "Enter Correct Information", Toast.LENGTH_SHORT).show();
        }
    }
    private void disableView() {
        etNoOfCoinTransfer.setEnabled(true);
        etcoinsRecieverUserName.setEnabled(true);
        etcoinsRecieverUserMobileNo.setEnabled(true);
        btnTransfer.setEnabled(true);
    }

    private void transferCoin(final View view) {

        ApiInterface apiInterface = ApiClient.getClient();
        Call<TransferFxCoin> transferFxCoinCall = apiInterface.transferFxCoin(userId, userName, name, noOfTransferCoin, coinValue, recieverobileNo, recieverName);
        transferFxCoinCall.enqueue(new Callback<TransferFxCoin>() {
            @Override
            public void onResponse(Call<TransferFxCoin> call, Response<TransferFxCoin> response) {
                TransferFxCoin transferFxCoin = response.body();
                avLoadingIndicatorView.hide();
                enableView();
                if (transferFxCoin.status) {
                    llMain.setVisibility(View.GONE);
                    otpScreen.setVisibility(View.VISIBLE);
                    viewOtpScreen(view);
                }
                else
                {
                    Toast.makeText(getActivity(), transferFxCoin.message, Toast.LENGTH_SHORT).show();
                    avLoadingIndicatorView.hide();
                    enableView();
                }
            }

            @Override
            public void onFailure(Call<TransferFxCoin> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                avLoadingIndicatorView.hide();
                enableView();
            }
        });
    }

    private void enableView() {
        etNoOfCoinTransfer.setEnabled(true);
        etcoinsRecieverUserName.setEnabled(true);
        etcoinsRecieverUserMobileNo.setEnabled(true);
        btnTransfer.setEnabled(true);
    }

    private void viewOtpScreen(View view) {

//        etOtp = view.findViewById(R.id.transfer_et_otp);
//        btnOtpNext = view.findViewById(R.id.transfer_otp_btn);

        btnOtpNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otp = etOtp.getText().toString().trim();
                updateCoin();
            }
        });
    }

    private void updateCoin() {
        ApiInterface apiInterface = ApiClient.getClient();
        Call<UpdateFxCoin> updateFxCoinCall = apiInterface.updateFxCoin(otp, userId, userName,
                availableCoins, noOfTransferCoin, coinValue, AppUtility.getLocalIpAddress(), name, recieverName);
        updateFxCoinCall.enqueue(new Callback<UpdateFxCoin>() {
            @Override
            public void onResponse(Call<UpdateFxCoin> call, Response<UpdateFxCoin> response) {

                UpdateFxCoin fxCoin = response.body();
                if (fxCoin.status) {
                    otpScreen.setVisibility(View.GONE);
                    llMain.setVisibility(View.INVISIBLE);
//                    TransferFxCoinActivity.this.finish();
                }
            }

            @Override
            public void onFailure(Call<UpdateFxCoin> call, Throwable t) {

            }
        });
    }
}
