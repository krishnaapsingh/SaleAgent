package com.trio.app.activities;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.orhanobut.hawk.Hawk;
import com.trio.app.BuildConfig;
import com.trio.app.R;
import com.trio.app.appcontrollers.GPSTracker;
import com.trio.app.appcontrollers.ImageFilePath;
import com.trio.app.appcontrollers.SavePref;
import com.trio.app.models.RouteModel;
import com.trio.app.models.ShopCreated;
import com.trio.app.rest.ApiClient;
import com.trio.app.rest.ApiInterface;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddShopActivity extends AppCompatActivity {

    Toolbar toolbar;
    KProgressHUD hud;
    String userChoosenTask;
    File myFile;
    File cameraFile;
    Uri uri;
    int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    AlertDialog alertDialog1;

    List<String> routeList = new ArrayList<>();
    String route = "";
    TextView tvRouteName, tvRouteChange, tvMonth;
    EditText etShopName, etMobile, etOwnerName;
    RelativeLayout rlAddShop;
    LinearLayout llAddItems, llSpn, llShopImage;
    Spinner spnMonth;
    int Count = 0;
    boolean checkBackground = false;
    List<RouteModel> routeModelList;
    private boolean inBackground = false;
    GPSTracker gps;
    Double lattitude;
    Double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shop);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.backarrow));
        toolbar.setTitle("Add Shop");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddShopActivity.this.finish();
            }
        });
        tvRouteName = findViewById(R.id.tvRouteName);
        tvRouteChange = findViewById(R.id.tvRouteChange);
        etShopName = findViewById(R.id.etShopName);
        etMobile = findViewById(R.id.etMobile);
        etOwnerName = findViewById(R.id.etOwnerName);
        llShopImage = findViewById(R.id.llShopImage);
        rlAddShop = findViewById(R.id.rlAddShop);
        llAddItems = findViewById(R.id.llAddItems);

        tvMonth = findViewById(R.id.tvMonth);
        llSpn = findViewById(R.id.llSpn);
        spnMonth = findViewById(R.id.spnMonth);

        llSpn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spnMonth.performClick();
            }
        });

        spnMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tvMonth.setText(parent.getItemAtPosition(position).toString());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        hud = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setDimAmount(0.5f);

        getRouteList();

        rlAddShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validation() == 0) {
                    createShop();
                }
            }
        });

        llAddItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validation() == 0) {
                    Intent i = new Intent(AddShopActivity.this, AddItemsActivity.class);
                    i.putExtra("shopname", etShopName.getText().toString().trim());
                    startActivity(i);
                }
            }
        });

        llShopImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadGallery();
            }
        });

        tvRouteChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogForRoute();
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 011);
            return;
        } else {
            gps = new GPSTracker(this);
            if (gps.canGetLocation()) {

                lattitude = gps.getLatitude();
                longitude = gps.getLongitude();

            } else {

                gps.showSettingsAlert();
            }
        }

    }

    @Override
    public void onBackPressed() {
        AddShopActivity.this.finish();
    }

    private void getRouteList() {
        String licenceNo = SavePref.getLoginData().LicenseNumber;
        String emailId = SavePref.getLoginData().EmailID;
        hud.show();
        String url = "http://manage.bytepaper.com/Mobile/Manufacturing/index.php?getMappedRoute&&" + licenceNo + "&&" + emailId;
        ApiInterface apiInterface = ApiClient.getClient();
        Call<List<RouteModel>> call = apiInterface.getRoutes(url);
        call.enqueue(new Callback<List<RouteModel>>() {
            @Override
            public void onResponse(Call<List<RouteModel>> call, Response<List<RouteModel>> response) {
                routeModelList = response.body();
                if (routeModelList.size() != 0) {
//                    SavePref.saveMappedRoute(obj);

                    for (int i = 0; i < routeModelList.size(); i++) {
                        routeList.add(routeModelList.get(i).RouteName);
                    }
                    alertDialogForRoute();
//                    adapter = new RouteAdapter(obj);
//                    rvRoute.setAdapter(adapter);
                }
                hud.dismiss();
            }

            @Override
            public void onFailure(Call<List<RouteModel>> call, Throwable t) {
                hud.dismiss();
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
//        final android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(this)
//                .setTitle("Sesion timeout ")
//                .setMessage("Oops !!! Your session has been expired. You have to re-login");
//        final android.app.AlertDialog alert = dialog.create();
//        alert.show();
//
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if (alert.isShowing()) {
//                    alert.dismiss();
//                    Hawk.deleteAll();
//                    startActivity(new Intent(AddShopActivity.this, LoginActivity.class));
//                    AddShopActivity.this.finish();
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


    private void createShop() {

        String licenceNo = SavePref.getLoginData().LicenseNumber;
        String emailId = SavePref.getLoginData().EmailID;
        String ownername = etOwnerName.getText().toString().trim();
        String shopName = etShopName.getText().toString().trim();
        String contactNo = etMobile.getText().toString().trim();

        hud.show();

//        String url = "http://manage.bytepaper.com/Mobile/Manufacturing/index.php?createShop&&" + licenceNo + "&&" + emailId + "&&" + route + "&&" + shopName + "&&" + ownername + "&&" + contactNo + "&&Picture";


        ApiInterface apiInterface = ApiClient.getClient();
        Call<ShopCreated> call = apiInterface.createShop(licenceNo, emailId,route, ownername, shopName, contactNo, lattitude.toString(), longitude.toString(), myFile);

        call.enqueue(new Callback<ShopCreated>() {
            @Override
            public void onResponse(Call<ShopCreated> call, Response<ShopCreated> response) {
                ShopCreated obj = response.body();
                if (obj.Status.equalsIgnoreCase("Shop Created")) {
                    AddShopActivity.this.finish();
                    Toast.makeText(AddShopActivity.this, "Shop Created SuccessFully", Toast.LENGTH_SHORT).show();
//                    adapter = new ShopsAdapter(getActivity(), obj);
//                    rvShops.setAdapter(adapter);
                }
                hud.dismiss();
            }

            @Override
            public void onFailure(Call<ShopCreated> call, Throwable t) {
                hud.dismiss();
            }
        });


    }

    private int validation() {
        int check = 0;
        if (route.equalsIgnoreCase("")) {
            check++;
            tvRouteName.setText("select route");
//            tvRouteName.setTextColor(getResources().getColor(R.color.red));
            Toast.makeText(this, "select route", Toast.LENGTH_SHORT).show();
        }
        if (etOwnerName.getText().toString().trim().equalsIgnoreCase("")) {
            etOwnerName.setError("enter shop owner name");
            check++;
            Toast.makeText(this, "enter shop owner name", Toast.LENGTH_SHORT).show();
        }
        if (etShopName.getText().toString().trim().equalsIgnoreCase("")) {
            etShopName.setError("enter shop name");
            Toast.makeText(this, "enter shop name", Toast.LENGTH_SHORT).show();
            check++;
        }
        if (etMobile.getText().toString().trim().equalsIgnoreCase("")) {
            etMobile.setError("enter shop owner contact number");
            Toast.makeText(this, "enter shop owner contact number", Toast.LENGTH_SHORT).show();
            check++;
        } else if (etMobile.getText().toString().length() <= 9) {
            etMobile.setError("enter correct shop owner contact number");
            Toast.makeText(this, "enter correct shop owner contact number", Toast.LENGTH_SHORT).show();
            check++;
        }
        return check;
    }

    private void alertDialogForRoute() {
        String[] array = new String[routeList.size()];
        routeList.toArray(array);

        AlertDialog.Builder builder = new AlertDialog.Builder(AddShopActivity.this);

        builder.setTitle("Select Your Route");

        builder.setSingleChoiceItems(array, -1, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int item) {
                route = routeList.get(item);
                alertDialog1.dismiss();
                tvRouteName.setText(route);
            }
        });
        alertDialog1 = builder.create();
        alertDialog1.show();
    }

    public void loadGallery() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE
                    , Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 011);
            return;
        } else {
            selectImage();
        }


    }

    private void selectImage() {

        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder((this));
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
//                boolean result = AppUtility.checkPermission(UploadDocumentActivity.this);
                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void cameraIntent() {

        Intent CamIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        cameraFile = new File(Environment.getExternalStorageDirectory(),
                "file" + String.valueOf(System.currentTimeMillis()) + ".jpg");
        myFile = cameraFile;
        uri = FileProvider.getUriForFile(this,
                BuildConfig.APPLICATION_ID + ".provider",
                cameraFile);
        CamIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, uri);
        CamIntent.putExtra("return-data", true);
        startActivityForResult(CamIntent, REQUEST_CAMERA);
    }

    private void galleryIntent() {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap = null;

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                onSelectFromGalleryResult(data);
            } else if (requestCode == REQUEST_CAMERA) {
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    onCaptureImageResult(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void onSelectFromGalleryResult(Intent data) {


        String realPath;
        realPath = ImageFilePath.getPath(this, data.getData());
        assert realPath != null;
        myFile = new File(realPath);

//        Bitmap bm = null;
//        try {
//            bm = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
//            ByteArrayOutputStream out = new ByteArrayOutputStream();
//            bm.compress(Bitmap.CompressFormat.PNG, 50, out);
//            Bitmap decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
//
//            Glide.clear(ivUserImage);
//            ivUserImage.setImageBitmap(decoded);
//            //    PrefrencesUtils.saveImageType("bitmap");
//            //   PrefrencesUtils.saveUserImageBitmap(decoded);
////            tvAddPhoto.setText(R.string.photoadded);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }

    private void onCaptureImageResult(Bitmap data) throws IOException {


        Bitmap thumbnailC = data;
//        File outFile = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpeg");
        myFile = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpeg");

//        outFile.createNewFile();
//        ByteArrayOutputStream bos = new ByteArrayOutputStream();
//        thumbnailC.compress(Bitmap.CompressFormat.PNG,0, bos);
//        byte[] bitmapData = bos.toByteArray();
//        FileOutputStream fos = new FileOutputStream(outFile);
//        fos.write(bitmapData);
//        fos.flush();
//        fos.close();
//        encodeFileToBase64(outFile);

//        try
//
//        {
//            fos = new FileOutputStream(outFile);
//            thumbnailC.compress(Bitmap.CompressFormat.JPEG, 50, fos);
////            tvAddPhoto.setText(R.string.photoadded);
//            Glide.clear(ivUserImage);
//            ivUserImage.setImageBitmap(data);
//            //PrefrencesUtils.saveImageType("bitmap");
//            // PrefrencesUtils.saveUserImageBitmap(data);
//            fos.flush();
//            fos.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}
