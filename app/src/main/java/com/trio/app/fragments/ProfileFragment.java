package com.trio.app.fragments;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.trio.app.R;
import com.trio.app.activities.MainActivity;
import com.trio.app.appcontrollers.SavePref;
import com.trio.app.models.LoginModel;
import com.trio.app.models.ShopCreated;
import com.trio.app.rest.ApiClient;
import com.trio.app.rest.ApiInterface;

import java.io.File;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by User on 11-Jan-18.
 */

@SuppressLint("ValidFragment")
public class ProfileFragment extends Fragment {

    public static final String TAG = ProfileFragment.class.getSimpleName();
    static Calendar myCalendar;
    EditText etUserName, etEmail, etMobile, etPassword;
    CircleImageView ivProfileImage;
    String userChoosenTask;
    int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    boolean result;
    File myFile;
    String img = "";
    Uri uri;
    String realPath;
    String encodedImage;
    MainActivity activity;
    KProgressHUD hud;
    RelativeLayout rlEdit;
    Button btnUpdate;
    int check=0;

    @SuppressLint("ValidFragment")
    public ProfileFragment(MainActivity context) {
        this.activity = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        hud = KProgressHUD.create(getActivity())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setDimAmount(0.5f)
                .show();

        btnUpdate = v.findViewById(R.id.btnUpdate);
        btnUpdate.setVisibility(View.GONE);
        etUserName = v.findViewById(R.id.etUserName);
        etEmail = v.findViewById(R.id.etEmail);
        etMobile = v.findViewById(R.id.etMobile);
        ivProfileImage = v.findViewById(R.id.ivProfileImage);
        rlEdit = v.findViewById(R.id.rlEdit);

        rlEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check++;
                if (check%2==0){
                    disableView();
                }else {
                    enableView();
                }
            }
        });

        setValues();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    updateUserProfile();
                }
            }
        });
        return v;
    }

    private boolean validate() {
        if (ApiClient.isEmptyString(etUserName.getText().toString().trim())) {
            Toast.makeText(activity, "Please Enter User Name", Toast.LENGTH_SHORT).show();
            return false;
        } else if (ApiClient.isEmptyString(etEmail.getText().toString().trim())) {
            Toast.makeText(activity, "Please Enter User Email", Toast.LENGTH_SHORT).show();
            return false;
        } else if (ApiClient.isEmptyString(etMobile.getText().toString().trim())) {
            Toast.makeText(activity, "Please Enter User Mobile", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void updateUserProfile() {
        String licenceNo = SavePref.getLoginData().LicenseNumber;
        String emailId = SavePref.getLoginData().EmailID;
        hud.show();
        String url = "http://manage.bytepaper.com/Mobile/Manufacturing/index.php?updateProfile&&" + licenceNo + "&&" + emailId + "&&" + etUserName.getText().toString().trim() + "&&" + etMobile.getText().toString().trim();
        ApiInterface apiInterface = ApiClient.getClient();
        Call<ShopCreated> call = apiInterface.updateProfile(url);
        call.enqueue(new Callback<ShopCreated>() {
            @Override
            public void onResponse(Call<ShopCreated> call, Response<ShopCreated> response) {
                ShopCreated obj = response.body();
                if (obj.Status.equalsIgnoreCase("Profile Updated")) {
                    disableView();
                    Toast.makeText(getActivity(), "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            LoginModel ob = SavePref.getLoginData();
                            ob.UserName = etUserName.getText().toString().trim();
                            ob.EmailID = etEmail.getText().toString().trim();
                            ob.contactnum = etMobile.getText().toString().trim();

                            SavePref.setLoginData(ob);
                            activity.onClick(0);
                        }
                    },1000);

                }
                hud.dismiss();
            }

            @Override
            public void onFailure(Call<ShopCreated> call, Throwable t) {
                hud.dismiss();
                disableView();
            }
        });

    }

    private void enableView() {
        etUserName.setEnabled(true);
        etEmail.setEnabled(true);
        etMobile.setEnabled(true);
        btnUpdate.setEnabled(true);
        btnUpdate.setVisibility(View.VISIBLE);
    }

    private void disableView() {
        etUserName.setEnabled(false);
        etEmail.setEnabled(false);
        etMobile.setEnabled(false);
        btnUpdate.setEnabled(false);
        btnUpdate.setVisibility(View.GONE);
    }


    private void setValues() {

        etUserName.setText(SavePref.getLoginData().UserName);
        etEmail.setText(SavePref.getLoginData().EmailID);
        etMobile.setText(SavePref.getLoginData().contactnum);
        Glide.with(getActivity())
                .load(SavePref.getLoginData().UserPic)
                .centerCrop()
//                .placeholder(R.drawable.profile1)
                .error(R.drawable.profile1)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH)
                .into(ivProfileImage);
        hud.dismiss();

    }

}
