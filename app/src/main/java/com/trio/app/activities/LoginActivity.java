package com.trio.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.trio.app.R;
import com.trio.app.appcontrollers.SavePref;
import com.trio.app.models.LoginModel;
import com.trio.app.rest.ApiClient;
import com.trio.app.rest.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity {


    EditText etLoginId, etPassword;
    Button btnLogin;
    KProgressHUD hud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        hud = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setDimAmount(0.5f);

        etLoginId = findViewById(R.id.etLoginId);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validation()==0) {
                    hud.show();
                    Login(etLoginId.getText().toString().trim(), etPassword.getText().toString().trim());

                }
            }
        });

    }


    private int validation() {
        int check=0;
        if (ApiClient.isEmptyString(etLoginId.getText().toString().trim())) {
            check++;
            etLoginId.setError("Please Enter Login Id");
            Toast.makeText(this, "Please Enter Login Id", Toast.LENGTH_SHORT).show();
        }
        if (ApiClient.isEmptyString(etPassword.getText().toString().trim())) {
            check++;
            etPassword.setError("Please Enter Valid Password");
            Toast.makeText(this, "Please Enter Valid Password", Toast.LENGTH_SHORT).show();
        }
        return check;

    }


    private void Login(String username, String password) {

        String url = "http://manage.bytepaper.com/Mobile/api.php?getuserdetail&&" + username + "&&" + password;

        ApiInterface apiInterface = ApiClient.getClient();
        Call<LoginModel> call = apiInterface.loginUser(url);
        call.enqueue(new Callback<LoginModel>() {
            @Override
            public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                LoginModel obj = response.body();
                if (obj.Status.equalsIgnoreCase("Success")) {
                    SavePref.saveLogin(true);
                    SavePref.setLoginData(obj);
                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                } else {
                    Toast.makeText(LoginActivity.this, obj.Status, Toast.LENGTH_SHORT).show();
                }
                hud.dismiss();

            }

            @Override
            public void onFailure(Call<LoginModel> call, Throwable t) {
                hud.dismiss();

            }
        });
    }

}
