package com.trio.app.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.trio.app.R;
import com.trio.app.activities.MainActivity;
import com.trio.app.appcontrollers.SavePref;
import com.trio.app.models.ChangePasswordModel;
import com.trio.app.models.FxCoinValue;
import com.trio.app.models.Login;
import com.trio.app.rest.ApiClient;
import com.trio.app.rest.ApiInterface;
import com.wang.avi.AVLoadingIndicatorView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by User on 18-Jan-18.
 */

@SuppressLint("ValidFragment")
public class ChangePassword extends Fragment implements View.OnClickListener {

    public static final String TAG = ChangePassword.class.getSimpleName();
    TextView tvCoinValue, tvAvailCoin, tvUserAvailCoin;
    EditText etOldPw, etNewPw, etCnfNewPw;
    Button btnReset;
    AVLoadingIndicatorView avLoadingIndicatorView;
    MainActivity activity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_password, container, false);
        setView(view);
        return view;
    }

    @SuppressLint("ValidFragment")
    public ChangePassword(MainActivity context) {
        this.activity=context;
    }

    private void setView(View view) {
//        tvCoinValue = view.findViewById(R.id.cp_tv_coinvalue);
//        tvAvailCoin = view.findViewById(R.id.cp_tv_available_coin);
//        etOldPw = view.findViewById(R.id.cp_et_oldpw);
//        etNewPw = view.findViewById(R.id.cp_et_newpw);
//        etCnfNewPw = view.findViewById(R.id.cp_et_cnfpw);
//        btnReset = view.findViewById(R.id.cp_btn_reset);
//        avLoadingIndicatorView = view.findViewById(R.id.cp_avi);
        String coin = getActivity().getResources().getString(R.string.fxcoin)+SavePref.fetchCoinValue().data.get(0).value;
        String availCoin = getResources().getString(R.string.totalavailcoin)+SavePref.fetchCoinValue().data.get(0).avl_coin;

        tvCoinValue.setText(coin);
        tvAvailCoin.setText(availCoin);
        avLoadingIndicatorView.hide();
        btnReset.setOnClickListener(this);
        updateCoinValue();
    }

    @Override
    public void onClick(View v) {
//        if (v.getId() == R.id.cp_btn_reset) {
//            if (validateNewPassword()) {
//                avLoadingIndicatorView.show();
//                changePassword();
//            }
//        }
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
//
//                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });

    }

    private void changePassword() {

//        Login login
//                = SavePref.getLoginData();
//        ApiInterface apiInterface = ApiClient.getClient();
//        Call<ChangePasswordModel> call = apiInterface.changePassword(login.data.id,
//                etOldPw.getText().toString().trim(), etNewPw.getText().toString().trim());
//
//        call.enqueue(new Callback<ChangePasswordModel>() {
//            @Override
//            public void onResponse(Call<ChangePasswordModel> call, Response<ChangePasswordModel> response) {
//                ChangePasswordModel changePasswordModel = response.body();
//                if (changePasswordModel.status) {
//                    avLoadingIndicatorView.hide();
//                    Toast.makeText(getActivity(), changePasswordModel.message, Toast.LENGTH_SHORT).show();
//                    activity.onClick(8);
//                } else {
//                    Toast.makeText(getActivity(), changePasswordModel.message, Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ChangePasswordModel> call, Throwable t) {
//                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    private boolean validateNewPassword() {
        if (etNewPw.getText().toString().trim().equals(etCnfNewPw.getText().toString().trim())) {
            return true;
        } else {
            etCnfNewPw.setError("Password didn't matched");
            return false;
        }
    }
}
