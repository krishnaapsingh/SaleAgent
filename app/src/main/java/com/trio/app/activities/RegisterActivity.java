package com.trio.app.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.trio.app.appcontrollers.AppUtility;
import com.trio.app.R;
import com.trio.app.appcontrollers.SavePref;
import com.trio.app.models.OtpVerification;
import com.trio.app.models.Register;
import com.trio.app.rest.ApiClient;
import com.trio.app.rest.ApiInterface;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    EditText etName, etEmail, etMobile, etCaptcha, etOtp;
    ImageView ivBack, ivRefresh;
    TextView tvCaptcha, tvCoinValue, tvTotalAvailCoin;
    Button btnRegister;
    TextView btnOtpCancel, btnOtpNext;
    String captcha;
    AVLoadingIndicatorView avLoadingIndicatorView;
    RelativeLayout rlForOtp;
    int check = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

//        etName = findViewById(R.id.register_et_name);
//        etEmail = findViewById(R.id.register_et_mail);
//        etMobile = findViewById(R.id.register_et_mobile);
//        ivBack = findViewById(R.id.register_iv_back);
//        btnRegister = findViewById(R.id.register_btn_register);
//        etCaptcha = findViewById(R.id.register_et_catcha);
//        tvCaptcha = findViewById(R.id.register_tv_captcha);
//        ivRefresh = findViewById(R.id.register_iv_refresh);
//        rlForOtp = findViewById(R.id.rl_otp);
//        btnOtpNext = findViewById(R.id.register_otp_next);
//        btnOtpCancel = findViewById(R.id.register_otp_cancel);
//        etOtp = findViewById(R.id.register_et_otp);
//        tvCoinValue = findViewById(R.id.register_tv_coinvalue);
//        tvTotalAvailCoin = findViewById(R.id.register_tv_available_coin);

        tvCoinValue.setText(SavePref.fetchCoinRate());
        tvTotalAvailCoin.setText(SavePref.fetchTotalAvailCoin());

        ivRefresh.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        btnOtpNext.setOnClickListener(this);
        btnOtpCancel.setOnClickListener(this);

//        avLoadingIndicatorView = findViewById(R.id.avi_register);
        avLoadingIndicatorView.hide();

        createCaptcha();
    }


    @SuppressLint("DefaultLocale")
    private void createCaptcha() {
        Random random = new Random();
        captcha = String.format("%05d", random.nextInt(100000));
        tvCaptcha.setText(captcha);
        etCaptcha.setText("");
    }

    @Override
    public void onClick(View v) {
//        int Id = v.getId();
//        switch (Id) {
//            case R.id.register_btn_register:
//                if (matchCaptcha()) {
//                    validation();
//                } else {
//                    Toast.makeText(this, "Please Enter Correct Captcha", Toast.LENGTH_SHORT).show();
//                }
//                break;
//            case R.id.register_iv_refresh:
//                createCaptcha();
//                break;
//            case R.id.register_iv_back:
//                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
//                RegisterActivity.this.finish();
//                break;
//            case R.id.register_otp_next:
//                avLoadingIndicatorView.show();
//                etOtp.setEnabled(false);
//                btnOtpNext.setEnabled(false);
//                btnOtpCancel.setEnabled(false);
//                otpVerification();
//            case R.id.register_otp_cancel:
//                rlForOtp.setVisibility(View.INVISIBLE);
//                enableView();
//
//                break;
//        }
    }

    private void validation() {
        if (!TextUtils.isEmpty(etEmail.getText().toString().trim()) &&
                Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString().trim()).matches() &&
                !TextUtils.isEmpty(etName.getText().toString().trim())
                && !TextUtils.isEmpty(etMobile.getText().toString().trim()) &&
                etMobile.getText().toString().trim().length() == 10) {
            avLoadingIndicatorView.show();
            disableView();
            registerUser();
        } else {
            Toast.makeText(this, "Input Correct Information", Toast.LENGTH_SHORT).show();
        }


    }

    private void enableView() {
        etName.setEnabled(true);
        etEmail.setEnabled(true);
        etMobile.setEnabled(true);
        ivRefresh.setEnabled(true);
        etCaptcha.setEnabled(true);
        btnRegister.setEnabled(true);
        ivBack.setEnabled(true);
    }

    private void disableView() {
        etName.setEnabled(false);
        etEmail.setEnabled(false);
        etMobile.setEnabled(false);
        ivRefresh.setEnabled(false);
        etCaptcha.setEnabled(false);
        btnRegister.setEnabled(false);
        ivBack.setEnabled(false);
    }

    private boolean matchCaptcha() {

        return etCaptcha.getText().toString().trim().equalsIgnoreCase(captcha);
    }

    private void registerUser() {

        String Ip = AppUtility.getLocalIpAddress();

        ApiInterface apiInterface = ApiClient.getClient();
        Call<Register> userRegister = apiInterface.userRegister(etName.getText().toString().trim(),
                etEmail.getText().toString().trim(), etMobile.getText().toString().trim(), etCaptcha.getText().toString().trim(), Ip);

        userRegister.enqueue(new Callback<Register>() {
            @Override
            public void onResponse(Call<Register> call, Response<Register> response) {
                Register registerObj = response.body();
                SavePref.setRegisterData(response.body());
                if (registerObj.status) {
                    avLoadingIndicatorView.hide();
                    rlForOtp.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(RegisterActivity.this, registerObj.message, Toast.LENGTH_SHORT).show();
                    avLoadingIndicatorView.hide();
                    enableView();
                }
            }

            @Override
            public void onFailure(Call<Register> call, Throwable t) {

                avLoadingIndicatorView.hide();
                enableView();
                Toast.makeText(RegisterActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }


        });
    }

    private void otpVerification() {

        check = 1;
        ApiInterface apiInterface = ApiClient.getClient();
        final Call<OtpVerification> otpVerification = apiInterface.userOtpVerification(etOtp.getText().toString());
        otpVerification.enqueue(new Callback<OtpVerification>() {
            @Override
            public void onResponse(Call<OtpVerification> call, Response<OtpVerification> response) {
                OtpVerification otpObj = response.body();
                if (otpObj.status) {
                    rlForOtp.setVisibility(View.INVISIBLE);
                    avLoadingIndicatorView.hide();
                    etOtp.setEnabled(true);
                    btnOtpNext.setEnabled(true);
                    btnOtpCancel.setEnabled(true);
                    enableView();
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    RegisterActivity.this.finish();
                } else {
                    avLoadingIndicatorView.hide();
                    etOtp.setEnabled(true);
                    btnOtpNext.setEnabled(true);
                    btnOtpCancel.setEnabled(true);
                    enableView();
                    Toast.makeText(RegisterActivity.this, otpObj.message, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<OtpVerification> call, Throwable t) {
                rlForOtp.setVisibility(View.INVISIBLE);
                enableView();
                avLoadingIndicatorView.hide();
                etOtp.setEnabled(true);
                btnOtpNext.setEnabled(true);
                btnOtpCancel.setEnabled(true);
                Toast.makeText(RegisterActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });




}

    @Override
    public void onBackPressed() {

        if (check == 1) {
            enableView();
            rlForOtp.setVisibility(View.GONE);
        } else {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            this.finish();
        }
    }
}
