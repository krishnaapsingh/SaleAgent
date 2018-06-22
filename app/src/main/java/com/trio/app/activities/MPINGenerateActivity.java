package com.trio.app.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.trio.app.R;
import com.trio.app.models.ChangePasswordModel;
import com.trio.app.rest.ApiClient;
import com.trio.app.rest.ApiInterface;
import com.wang.avi.AVLoadingIndicatorView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MPINGenerateActivity extends AppCompatActivity {

    EditText etMobile, etUsername, etMpin, etConfirmPin;
    Button btnPinGen;
    TextView tvLogin;
    LinearLayout llMain;
    AVLoadingIndicatorView avi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mpingenerate);

        etMobile = findViewById(R.id.etMobile);
        etUsername = findViewById(R.id.etUsername);
        etMpin = findViewById(R.id.etMpin);
        etConfirmPin = findViewById(R.id.etConfirmPin);
        btnPinGen = findViewById(R.id.btnPinGen);
        tvLogin = findViewById(R.id.tvLogin);
        llMain = findViewById(R.id.llMain);
        avi = findViewById(R.id.avi);
        avi.hide();

        btnPinGen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validation()){
                    avi.show();
                    llMain.setVisibility(View.GONE);
                    generateMPin();
                }
            }
        });

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MPINGenerateActivity.this, LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });
    }

    private void generateMPin() {
        ApiInterface apiInterface = ApiClient.getClient();
        Call<ChangePasswordModel> call = apiInterface.generateMpin(etMobile.getText().toString().trim(),
                etUsername.getText().toString().trim(), etMpin.getText().toString().trim());
        call.enqueue(new Callback<ChangePasswordModel>() {
            @Override
            public void onResponse(Call<ChangePasswordModel> call, Response<ChangePasswordModel> response) {
                ChangePasswordModel obj = response.body();
                if (obj.status){
                    avi.hide();
                    llMain.setVisibility(View.VISIBLE);
                    Toast.makeText(MPINGenerateActivity.this, obj.message, Toast.LENGTH_SHORT).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent i = new Intent(MPINGenerateActivity.this, LoginActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                        }
                    },1000);
                }else {
                    avi.hide();
                    llMain.setVisibility(View.VISIBLE);
                    Toast.makeText(MPINGenerateActivity.this, obj.message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ChangePasswordModel> call, Throwable t) {
                avi.hide();
                llMain.setVisibility(View.VISIBLE);
                Toast.makeText(MPINGenerateActivity.this, "Something went wrong...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validation() {
        if (ApiClient.isEmptyString(etMobile.getText().toString()) || etMobile.getText().toString().trim().length()<10){
            Toast.makeText(this, "Please Enter Valid Phone Number", Toast.LENGTH_SHORT).show();
            return false;
        }else  if (ApiClient.isEmptyString(etUsername.getText().toString())){
            Toast.makeText(this, "Please Enter Valid Username", Toast.LENGTH_SHORT).show();
            return false;
        }else  if (ApiClient.isEmptyString(etMpin.getText().toString()) || etMpin.getText().toString().trim().length()<4){
            Toast.makeText(this, "Please Enter Valid MPIN Number", Toast.LENGTH_SHORT).show();
            return false;
        }else  if (!etMpin.getText().toString().equalsIgnoreCase(etConfirmPin.getText().toString().trim())){
            Toast.makeText(this, "Mpin is not matching", Toast.LENGTH_SHORT).show();
            return false;
        }else{
            return true;
        }

    }

    public void disableView(){

    }


}
