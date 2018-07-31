package com.trio.app.activities;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Parcelable;
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

import com.kaopiz.kprogresshud.KProgressHUD;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.trio.app.BuildConfig;
import com.trio.app.R;
import com.trio.app.adapters.ShopsAdapter;
import com.trio.app.appcontrollers.GPSTracker;
import com.trio.app.appcontrollers.ImageFilePath;
import com.trio.app.appcontrollers.SavePref;
import com.trio.app.models.InvoiceModel;
import com.trio.app.models.RouteModel;
import com.trio.app.models.ShopModel;
import com.trio.app.rest.ApiClient;
import com.trio.app.rest.ApiInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import cz.msebera.android.httpclient.Header;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bumptech.glide.load.resource.bitmap.TransformationUtils.rotateImage;

public class AddShopActivity extends AppCompatActivity {

    public static String totalPrice = "0";
    public static String totalItems = "0";
    public static String invoiceno = "0";
    public static String completeInvoice = "0";
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
    GPSTracker gps;
    Double lattitude;
    Double longitude;
    LinearLayout llInvoice;
    TextView tvInvoiceNo, tvDate, tvTotalPrice, tvStatus;
    String cDate;
    int click = 0;
    private boolean inBackground = false;
    Bitmap myBitmap;
    Uri picUri;
    TextView tvSelected;
    String complete="0";
    String userType = SavePref.getLoginData().UserType;
    Spinner spnShopType;
    String shopType="";


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

        spnShopType = findViewById(R.id.spnShopType);
        tvRouteName = findViewById(R.id.tvRouteName);
        tvRouteChange = findViewById(R.id.tvRouteChange);
        etShopName = findViewById(R.id.etShopName);
        etMobile = findViewById(R.id.etMobile);
        etOwnerName = findViewById(R.id.etOwnerName);
        llShopImage = findViewById(R.id.llShopImage);
        rlAddShop = findViewById(R.id.rlAddShop);
        llAddItems = findViewById(R.id.llAddItems);
        llInvoice = findViewById(R.id.llInvoice);
        tvInvoiceNo = findViewById(R.id.tvInvoiceNo);
        tvDate = findViewById(R.id.tvDate);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        tvStatus = findViewById(R.id.tvStatus);
        llInvoice.setVisibility(View.GONE);
        tvMonth = findViewById(R.id.tvMonth);
        llSpn = findViewById(R.id.llSpn);
        spnMonth = findViewById(R.id.spnMonth);
        tvSelected = findViewById(R.id.tvSelected);
        tvSelected.setVisibility(View.INVISIBLE);
//        if (getIntent().getStringExtra("check").equalsIgnoreCase("1")){
//            ArrayList<String> list= getIntent().getExtras().getStringArrayList("data");
//            setValues(list.get(0), list.get(1), list.get(2));
//        }

        totalPrice = "0";
        totalItems = "0";
        invoiceno = "0";

        getCurrentDate();
//        getInvoiceNo();

//        spnShopType.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                spnMonth.performClick();
//            }
//        });

        spnShopType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                tvMonth.setText(parent.getItemAtPosition(position).toString());
                shopType = parent.getItemAtPosition(position).toString();

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

                    try {
                        if (complete.equals("0")){
                            if (validation() == 0) {

                                createShop();
                            }
                        }else {
                            hud.show();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    complete = "0";
                                    hud.dismiss();
                                    AddShopActivity.this.finish();
                                }
                            },1000);
                        }



                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

            }
        });

        llAddItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    if (click != 0) {
                        Intent i = new Intent(AddShopActivity.this, CreateInvoiceActivity.class);
                        i.putExtra("shopname", etShopName.getText().toString().trim());
                        i.putExtra("actname", "AddShop");
                        startActivity(i);
                    } else {
                        Toast.makeText(AddShopActivity.this, "Please Add Shop First", Toast.LENGTH_SHORT).show();
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

//    private void setValues(String shop, String owner, String contact) {
//
//        etShopName.setText(shop);
//        etOwnerName.setText(owner);
//        etMobile.setText(contact);
//
//        etShopName.setEnabled(false);
//        etOwnerName.setEnabled(false);
//        etMobile.setEnabled(false);
//    }

//    private void getInvoiceNo() {
//        Random r = new Random(System.currentTimeMillis());
//        invoiceno = String.valueOf(10000 + r.nextInt(20000));
//    }

    private void getCurrentDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        cDate = dateFormat.format(date);

    }


    @Override
    protected void onResume() {
        super.onResume();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {

            if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED && checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED) {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 011);
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
        } else {
            gps = new GPSTracker(this);
            if (gps.canGetLocation()) {

                lattitude = gps.getLatitude();
                longitude = gps.getLongitude();

            } else {

                gps.showSettingsAlert();
            }
        }
        if (!completeInvoice.equalsIgnoreCase("0")) {

            getInvoiceList();
        }

    }

    private void getInvoiceList() {
        String licenceNo = SavePref.getLoginData().LicenseNumber;
        String emailId = SavePref.getLoginData().EmailID;
        String shopid = SavePref.fetchShopId();
        hud.show();
        String url="";
        if (userType.equalsIgnoreCase("Sales Agent")) {
            url = "http://manage.bytepaper.com/Mobile/Manufacturing/index.php?listInvoice&&" + licenceNo + "&&" + emailId + "&&" + shopid;

        } else if (userType.equalsIgnoreCase("Admin")) {
            url = "http://manage.bytepaper.com/Mobile/Manufacturing/index.php?listAllInvoice&&"+licenceNo+"&&"+shopid;
        }
//        String url = "http://manage.bytepaper.com/Mobile/Manufacturing/index.php?listInvoice&&" + licenceNo + "&&" + emailId + "&&" + shopid;
        ApiInterface apiInterface = ApiClient.getClient();
        Call<List<InvoiceModel>> call = apiInterface.getInvoiceList(url);
        call.enqueue(new Callback<List<InvoiceModel>>() {
            @Override
            public void onResponse(Call<List<InvoiceModel>> call, Response<List<InvoiceModel>> response) {
                List<InvoiceModel> obj = response.body();
                if (obj.size() != 0) {
                    llInvoice.setVisibility(View.VISIBLE);
                    tvInvoiceNo.setText(obj.get(0).InvoiceNumber);
                    tvDate.setText(obj.get(0).Date);
                    tvStatus.setText(obj.get(0).PaymentStaus);
                    tvTotalPrice.setText(obj.get(0).Amount);
                    complete ="1";
                    completeInvoice="0";

                    rlAddShop.setVisibility(View.VISIBLE);
                    rlAddShop.setEnabled(true);
                }

                hud.dismiss();
            }

            @Override
            public void onFailure(Call<List<InvoiceModel>> call, Throwable t) {
                hud.dismiss();
            }
        });
    }


    @Override
    public void onBackPressed() {
        AddShopActivity.this.finish();
    }

    private void getRouteList() {

        String licenceNo = SavePref.getLoginData().LicenseNumber;
        String emailId = SavePref.getLoginData().EmailID;
        hud.show();
        String url="";
        if (userType.equalsIgnoreCase("Sales Agent")) {
            url = "http://manage.bytepaper.com/Mobile/Manufacturing/index.php?getMappedRoute&&"+licenceNo+"&&"+emailId;

        } else if (userType.equalsIgnoreCase("Admin")) {
            url = "http://manage.bytepaper.com/Mobile/Manufacturing/index.php?getRoute&&"+licenceNo;
        }
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


    private void createShop() throws FileNotFoundException {


        String licenceNo = SavePref.getLoginData().LicenseNumber;
        String emailId = SavePref.getLoginData().EmailID;
        String ownername = etOwnerName.getText().toString().trim();
        String shopName = etShopName.getText().toString().trim();
        String contactNo = etMobile.getText().toString().trim();
        hud.show();

        RequestParams data = new RequestParams();
        data.put("LicenseNumber", licenceNo);
        data.put("EmployeeID", emailId);
        data.put("Route", route);
        data.put("OwnerName", ownername);
        data.put("ShopName", shopName);
        data.put("ContactNumber", contactNo);
        data.put("Lat", lattitude);
        data.put("Long", longitude);
        data.put("Picture", myFile);
        data.put("Type", shopType);

        AsyncHttpClient client = new AsyncHttpClient();
        client.post("http://manage.bytepaper.com/Mobile/Manufacturing/index.php", data, new TextHttpResponseHandler() {

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                hud.dismiss();
//                Toast.makeText(getContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    JSONObject object = new JSONObject(responseString);
                    if (object.getString("Status").contains("Shop Created")) {
//                        AddShopActivity.this.finish();
                        click++;
                        rlAddShop.setVisibility(View.INVISIBLE);
                        rlAddShop.setEnabled(false);

                        etMobile.setText("");
                        tvSelected.setVisibility(View.INVISIBLE);
                        getShopsList();
                        Toast.makeText(AddShopActivity.this, "Shop Created Successfully", Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                hud.dismiss();
            }
        });
    }

    private void getShopsList() {
        String licenceNo = SavePref.getLoginData().LicenseNumber;
        String emailId = SavePref.getLoginData().EmailID;
        hud.show();
        String url="";
        if (userType.equalsIgnoreCase("Sales Agent")) {
            url = "http://manage.bytepaper.com/Mobile/Manufacturing/index.php?shopList&&"+licenceNo+"&&"+emailId;

        } else if (userType.equalsIgnoreCase("Admin")) {
            url = "http://manage.bytepaper.com/Mobile/Manufacturing/index.php?AllshopList&&"+licenceNo;
        }

//        String url = "http://manage.bytepaper.com/Mobile/Manufacturing/index.php?shopList&&" + licenceNo + "&&" + emailId;
        ApiInterface apiInterface = ApiClient.getClient();
        Call<List<ShopModel>> call = apiInterface.getShopsList(url);
        call.enqueue(new Callback<List<ShopModel>>() {
            @Override
            public void onResponse(Call<List<ShopModel>> call, Response<List<ShopModel>> response) {
                List<ShopModel> obj = response.body();
                if (obj.size() != 0) {
                    SavePref.saveShopId(obj.get(obj.size() - 1).ID);
                }
                hud.dismiss();
            }

            @Override
            public void onFailure(Call<List<ShopModel>> call, Throwable t) {
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
        if (myFile==null){
//            tvSelected.setError("please select image");
            Toast.makeText(this, "please select image", Toast.LENGTH_SHORT).show();
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
//            selectImage();
            startActivityForResult(getPickImageChooserIntent(), 200);
        }
    }

    public Intent getPickImageChooserIntent() {

        // Determine Uri of camera image to save.
        Uri outputFileUri = getCaptureImageOutputUri();

        List<Intent> allIntents = new ArrayList<>();
        PackageManager packageManager = getPackageManager();

        // collect all camera intents
        Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            if (outputFileUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            }
            allIntents.add(intent);
        }

        // collect all gallery intents
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        List<ResolveInfo> listGallery = packageManager.queryIntentActivities(galleryIntent, 0);
        for (ResolveInfo res : listGallery) {
            Intent intent = new Intent(galleryIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            allIntents.add(intent);
        }

        // the main intent is the last in the list (fucking android) so pickup the useless one
        Intent mainIntent = allIntents.get(allIntents.size() - 1);
        for (Intent intent : allIntents) {
            if (intent.getComponent().getClassName().equals("com.android.documentsui.DocumentsActivity")) {
                mainIntent = intent;
                break;
            }
        }
        allIntents.remove(mainIntent);

        // Create a chooser from the main intent
        Intent chooserIntent = Intent.createChooser(mainIntent, "Select source");

        // Add all other intents
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toArray(new Parcelable[allIntents.size()]));

        return chooserIntent;
    }

    private Uri getCaptureImageOutputUri() {
        Uri outputFileUri = null;
        File getImage = getExternalCacheDir();
        if (getImage != null) {
            outputFileUri = Uri.fromFile(new File(getImage.getPath(), "profile.png"));
        }
        return outputFileUri;
    }

    String imageFilePath;



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap bitmap;
        if (resultCode == Activity.RESULT_OK) {

//            ImageView imageView = (ImageView) findViewById(R.id.imageView);

            if (getPickImageResultUri(data) != null) {
                picUri = getPickImageResultUri(data);
                tvSelected.setVisibility(View.VISIBLE);


                try {
                    myBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), picUri);
                    if (myBitmap != null) {
                        myBitmap = rotateImageIfRequired(myBitmap, picUri);
                        myBitmap = getResizedBitmap(myBitmap, 500);
                        onCaptureImageResult(myBitmap);
                        tvSelected.setTextColor(this.getResources().getColor(R.color.black));
                        tvSelected.setText("Image Selected");

                    } else {
                        tvSelected.setTextColor(this.getResources().getColor(R.color.red));
                        tvSelected.setText("Image Error");
                    }

//                    CircleImageView croppedImageView = (CircleImageView) findViewById(R.id.img_profile);
//                    croppedImageView.setImageBitmap(myBitmap);
//                    imageView.setImageBitmap(myBitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                }


            } else {


//                bitmap = (Bitmap) data.getExtras().get("data");
//
//                myBitmap = bitmap;
                onSelectFromGalleryResult(data);
//                CircleImageView croppedImageView = (CircleImageView) findViewById(R.id.img_profile);
//                if (croppedImageView != null) {
//                    croppedImageView.setImageBitmap(myBitmap);
//                }

//                imageView.setImageBitmap(myBitmap);

            }

        }
    }

    private void changeBitmapToFile(Bitmap myBitmap) throws IOException {
        Bitmap thumbnailC = myBitmap;
        myFile = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpeg");
        myFile.createNewFile();
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 0) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }


    private static Bitmap rotateImageIfRequired(Bitmap img, Uri selectedImage) throws IOException {

        ExifInterface ei = new ExifInterface(selectedImage.getPath());
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(img, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(img, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(img, 270);
            default:
                return img;
        }
    }

    public Uri getPickImageResultUri(Intent data) {
        boolean isCamera = true;
        if (data != null) {
            String action = data.getAction();
            isCamera = action != null && action.equals(MediaStore.ACTION_IMAGE_CAPTURE);
        }


        return isCamera ? getCaptureImageOutputUri() : data.getData();
    }

    private void onSelectFromGalleryResult(Intent data) {
        Uri selectedImage = data.getData();
        String[] filePathColumn = {MediaStore.Images.Media.DATA};

        Cursor cursor = getContentResolver().query(selectedImage,
                filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();

//        String realPath;
//        realPath = ImageFilePath.getPath(this, data.getData());
        assert picturePath != null;
        myFile = new File(picturePath);


    }

    private void onCaptureImageResult(Bitmap data) throws IOException {

        Bitmap thumbnailC = data;
        myFile = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpeg");
        myFile.createNewFile();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        thumbnailC.compress(Bitmap.CompressFormat.PNG, 50, bos);
        byte[] bitmapData = bos.toByteArray();
        FileOutputStream fos = new FileOutputStream(myFile);
        fos.write(bitmapData);
        fos.flush();
        fos.close();

    }

}
