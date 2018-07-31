package com.trio.app.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.orhanobut.hawk.Hawk;
import com.trio.app.R;
import com.trio.app.adapters.InvoiceItemAdapter;
import com.trio.app.adapters.PaymentAdapter;
import com.trio.app.appcontrollers.GPSTracker;
import com.trio.app.appcontrollers.SavePref;
import com.trio.app.models.InvoiceDetailModel;
import com.trio.app.models.PaymentDetailsModel;
import com.trio.app.models.ShopCreated;
import com.trio.app.rest.ApiClient;
import com.trio.app.rest.ApiInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.xm.weidongjian.popuphelper.PopupWindowHelper;
import cz.msebera.android.httpclient.Header;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bumptech.glide.load.resource.bitmap.TransformationUtils.rotateImage;

public class InvoiceItemsActivity extends AppCompatActivity {

    InvoiceItemAdapter adapter;
    Toolbar toolbar;
    RecyclerView rvItems;
    TextView tvInvoiceNo, tvDate, tvNetAmount, tvDistributors, tvRoute, tvAddress, tvAmtStatus, tvDuesAmount;
    KProgressHUD hud;
    String invoiceno = "";
    private boolean inBackground = false;
    boolean checkBackground = false;
    TextView tvAddPayment, tvPaymentHistory;
    PopupWindowHelper popupWindowHelper;
    View popView;
    LinearLayout llButtons, llPayment;
    EditText etPayment;
    Button btnPay;
    ImageView ivBack;
    RecyclerView recyclerView;
    PaymentAdapter adapterPayment;
    List<PaymentDetailsModel> obj;
    PopupWindowHelper popUp;
    ImageView ivInvoiceImage;
    RelativeLayout close;
    ImageView image;
    RelativeLayout rlAddImage;

    int totalAmount = 0;
    GPSTracker gps;
    Double lattitude;
    Double longitude;
    String licenceNo, imageInvoice;
    int totalDueAmount;
    String userType = SavePref.getLoginData().UserType;
    RelativeLayout rlInvoiceImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_items);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.backarrow));
        toolbar.setTitle("Invoice Items");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(InvoicesActivity.this, MainActivity.class));
                InvoiceItemsActivity.this.finish();
            }
        });
        hud = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setDimAmount(0.5f);

        invoiceno = getIntent().getStringExtra("invoiceno");


        licenceNo = SavePref.getLoginData().LicenseNumber;
        View view = LayoutInflater.from(this).inflate(R.layout.imageview1, null);
        close = view.findViewById(R.id.close);
        image = view.findViewById(R.id.image);
        popUp = new PopupWindowHelper(view);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUp.dismiss();
            }
        });


        ivInvoiceImage = findViewById(R.id.ivInvoiceImage);
        rlInvoiceImg = findViewById(R.id.rlInvoiceImg);
        tvAddPayment = findViewById(R.id.tvAddPayment);
        tvAddPayment.setVisibility(View.GONE);

        tvPaymentHistory = findViewById(R.id.tvPaymentHistory);
        tvInvoiceNo = findViewById(R.id.tvInvoiceNo);
        tvDate = findViewById(R.id.tvDate);
        tvNetAmount = findViewById(R.id.tvNetAmount);
        tvDuesAmount = findViewById(R.id.tvDuesAmount);
        tvDistributors = findViewById(R.id.tvDistributors);
        tvRoute = findViewById(R.id.tvRoute);
        tvAddress = findViewById(R.id.tvAddress);
        tvAmtStatus = findViewById(R.id.tvAmtStatus);
        rvItems = findViewById(R.id.rvItems);
        llButtons = findViewById(R.id.llButtons);
        llPayment = findViewById(R.id.llPayment);
        etPayment = findViewById(R.id.etPayment);
        btnPay = findViewById(R.id.btnPay);
        llPayment.setVisibility(View.GONE);

        if (userType.equalsIgnoreCase("Distributor") || userType.equalsIgnoreCase("Admin")) {
            if (!getIntent().getStringExtra("image").equals("")) {
                imageInvoice = getIntent().getStringExtra("image");
                Glide.with(this).load(imageInvoice).into(ivInvoiceImage);
            } else {
                ivInvoiceImage.setImageDrawable(getResources().getDrawable(R.drawable.doc));
            }
            rlInvoiceImg.setEnabled(false);
        } else {
            if (!getIntent().getStringExtra("image").equals("")) {
                imageInvoice = getIntent().getStringExtra("image");
                Glide.with(this).load(imageInvoice).into(ivInvoiceImage);
                rlInvoiceImg.setEnabled(false);
            } else {
                ivInvoiceImage.setImageDrawable(getResources().getDrawable(R.drawable.doc));
                rlInvoiceImg.setEnabled(false);
            }
        }

        rlInvoiceImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                popUp.showFromBottom(v);
                if (!userType.equalsIgnoreCase("Distributors")) {
                    loadGallery();
                }

            }
        });


        popView = LayoutInflater.from(this).inflate(R.layout.imageview, null);
        ivBack = popView.findViewById(R.id.close);
        recyclerView = popView.findViewById(R.id.recyclerView);
        popupWindowHelper = new PopupWindowHelper(popView);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        rlAddImage = findViewById(R.id.rlAddImage);
        rlAddImage.setVisibility(View.GONE);

        rlAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    addImageToInvoice();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });


        if (userType.equalsIgnoreCase("Distributor")) {
            if (SavePref.getActName().equalsIgnoreCase("order")) {
                getOrderDetails();
                rlInvoiceImg.setVisibility(View.GONE);
                tvPaymentHistory.setVisibility(View.GONE);
                tvAddPayment.setVisibility(View.GONE);
                tvDuesAmount.setVisibility(View.GONE);

            } else {
                getInvoiceDetails();
            }
        }else {
            getInvoiceDetails();
        }


        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindowHelper.dismiss();
            }
        });


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        rvItems.setLayoutManager(mLayoutManager);


        tvAddPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llPayment.setVisibility(View.VISIBLE);
                llButtons.setVisibility(View.GONE);
            }
        });

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validation() == 0) {
                    addPayment();
                    etPayment.setText("");
                }
            }
        });

        tvPaymentHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (obj.size() != 0) {
                    popupWindowHelper.showFromBottom(v);
                } else {
                    Toast.makeText(InvoiceItemsActivity.this, "Details not Found", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void getOrderDetails() {
        hud.show();
        String url = "http://manage.bytepaper.com/Mobile/Manufacturing/index.php?OrderDetail&&" + licenceNo + "&&" + invoiceno;
        ApiInterface apiInterface = ApiClient.getClient();
        Call<List<InvoiceDetailModel>> call = apiInterface.getInvoiceDetails(url);
        call.enqueue(new Callback<List<InvoiceDetailModel>>() {
            @Override
            public void onResponse(Call<List<InvoiceDetailModel>> call, Response<List<InvoiceDetailModel>> response) {
                List<InvoiceDetailModel> obj = response.body();
                if (obj.size() == 0) {

                    Toast.makeText(InvoiceItemsActivity.this, "Details not found", Toast.LENGTH_SHORT).show();
                }
                getPaymentDetails();
                if (obj.size() != 0) {
                    setValues(obj);
                }

                adapter = new InvoiceItemAdapter(obj);
                rvItems.setAdapter(adapter);

                hud.dismiss();
            }

            @Override
            public void onFailure(Call<List<InvoiceDetailModel>> call, Throwable t) {
                hud.dismiss();
            }
        });
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


    }

    private void addImageToInvoice() throws FileNotFoundException {
        hud.show();

        RequestParams data = new RequestParams();
        data.put("Lat", lattitude);
        data.put("Long", longitude);
        data.put("Picture", myFile);
        data.put("InvoiceNumber", invoiceno);
        data.put("LicenseNumber", licenceNo);

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
                    if (object.getString("Status").equalsIgnoreCase("Invoice Updated")) {
//                        AddShopActivity.this.finish();
                        rlAddImage.setVisibility(View.GONE);
                        Toast.makeText(InvoiceItemsActivity.this, "Invoice Updated Successfully", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(InvoiceItemsActivity.this, "Something went Wrong", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                hud.dismiss();
            }
        });
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

    Uri picUri;
    Bitmap myBitmap;
    File myFile;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK) {
            if (getPickImageResultUri(data) != null) {
                rlAddImage.setVisibility(View.VISIBLE);
                picUri = getPickImageResultUri(data);
//                tvSelected.setVisibility(View.VISIBLE);
                try {
                    myBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), picUri);
                    if (myBitmap != null) {
                        myBitmap = rotateImageIfRequired(myBitmap, picUri);
                        myBitmap = getResizedBitmap(myBitmap, 500);
                        onCaptureImageResult(myBitmap);
                        Glide.clear(ivInvoiceImage);
                        ivInvoiceImage.setImageBitmap(myBitmap);
//                        tvSelected.setTextColor(this.getResources().getColor(R.color.black));
//                        tvSelected.setText("Image Selected");
                    } else {
//                        tvSelected.setTextColor(this.getResources().getColor(R.color.red));
//                        tvSelected.setText("Image Error");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                onSelectFromGalleryResult(data);
            }
        }
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
        assert picturePath != null;
        myFile = new File(picturePath);


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

    private void getPaymentDetails() {
        hud.show();
        String url = "http://manage.bytepaper.com/Mobile/Manufacturing/index.php?PaymentDetails&&" + licenceNo + "&&" + invoiceno;
        ApiInterface apiInterface = ApiClient.getClient();
        Call<List<PaymentDetailsModel>> call = apiInterface.getPaymentDetails(url);
        call.enqueue(new Callback<List<PaymentDetailsModel>>() {
            @Override
            public void onResponse(Call<List<PaymentDetailsModel>> call, Response<List<PaymentDetailsModel>> response) {
                obj = response.body();
                if (obj.size() != 0) {
                    adapterPayment = new PaymentAdapter(obj);
                    recyclerView.setAdapter(adapterPayment);
                    calculateDuesAmount();
                } else {
                    Toast.makeText(InvoiceItemsActivity.this, "Details Not Found", Toast.LENGTH_SHORT).show();
                    calculateDuesAmount();
                }

                hud.dismiss();
            }

            @Override
            public void onFailure(Call<List<PaymentDetailsModel>> call, Throwable t) {
                hud.dismiss();
            }
        });
    }

    private void calculateDuesAmount() {
        int data;
        int amount;
        int due = 0;
        totalAmount = Integer.valueOf(tvNetAmount.getText().toString().trim());
        if (obj.size() != 0) {
            for (int i = 0; i < obj.size(); i++) {
                data = Integer.valueOf(obj.get(i).Payment);
                due = data + due;
            }
            amount = totalAmount - due;
            totalDueAmount = amount;
        } else {
            amount = totalAmount;
            totalDueAmount = amount;
        }
        tvDuesAmount.setText(String.valueOf(amount));
        if (!SavePref.getLoginData().UserType.equalsIgnoreCase("Admin")) {
            if (totalAmount > due) {
                tvAddPayment.setVisibility(View.VISIBLE);
            }
        }
    }

    private void addPayment() {
        String email = SavePref.getLoginData().EmailID;
        String PaymentAmount = etPayment.getText().toString().trim();
        hud.show();
        String url = "http://manage.bytepaper.com/Mobile/Manufacturing/index.php?InvoicePayment&&" + licenceNo + "&&" + invoiceno + "&&" + PaymentAmount + "&&" + email;
        ApiInterface apiInterface = ApiClient.getClient();
        Call<ShopCreated> call = apiInterface.addPayment(url);
        call.enqueue(new Callback<ShopCreated>() {
            @Override
            public void onResponse(Call<ShopCreated> call, Response<ShopCreated> response) {
                ShopCreated obj = response.body();
                if (obj.Status.equalsIgnoreCase("Payment Added")) {
                    llButtons.setVisibility(View.VISIBLE);
                    llPayment.setVisibility(View.GONE);
                    Toast.makeText(InvoiceItemsActivity.this, "Payment Added Successfully", Toast.LENGTH_SHORT).show();
                    getPaymentDetails();
                } else {
                    Toast.makeText(InvoiceItemsActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
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
        int status = 0;
        if (ApiClient.isEmptyString(etPayment.getText().toString().trim())) {
            etPayment.setError("Please Enter Amount");
            status++;
        } else if (Integer.parseInt(etPayment.getText().toString().trim()) == 0) {
            status++;
            etPayment.setError("Please enter valid amount");
        } else if (Integer.parseInt(etPayment.getText().toString().trim()) > totalDueAmount) {
            status++;
            etPayment.setError("Please Enter valid Amount");
        }

        return status;

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
//        final AlertDialog.Builder dialog = new AlertDialog.Builder(this)
//                .setTitle("Sesion timeout ")
//                .setMessage("Oops !!! Your session has been expired. You have to re-login");
//        final AlertDialog alert = dialog.create();
//        alert.show();
//
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if (alert.isShowing()) {
//                    alert.dismiss();
//                    Hawk.deleteAll();
//                    startActivity(new Intent(InvoiceItemsActivity.this, LoginActivity.class));
//                    InvoiceItemsActivity.this.finish();
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

    private void getInvoiceDetails() {
        hud.show();
        String url = "http://manage.bytepaper.com/Mobile/Manufacturing/index.php?InvoiceDetail&&" + licenceNo + "&&" + invoiceno;
        ApiInterface apiInterface = ApiClient.getClient();
        Call<List<InvoiceDetailModel>> call = apiInterface.getInvoiceDetails(url);
        call.enqueue(new Callback<List<InvoiceDetailModel>>() {
            @Override
            public void onResponse(Call<List<InvoiceDetailModel>> call, Response<List<InvoiceDetailModel>> response) {
                List<InvoiceDetailModel> obj = response.body();
                if (obj.size() == 0) {

                    Toast.makeText(InvoiceItemsActivity.this, "Invoices not found", Toast.LENGTH_SHORT).show();
                }
                getPaymentDetails();
                if (obj.size() != 0) {
                    setValues(obj);
                }

                adapter = new InvoiceItemAdapter(obj);
                rvItems.setAdapter(adapter);

                hud.dismiss();
            }

            @Override
            public void onFailure(Call<List<InvoiceDetailModel>> call, Throwable t) {
                hud.dismiss();
            }
        });
    }

    private void setValues(List<InvoiceDetailModel> obj) {
        tvInvoiceNo.setText(obj.get(0).InvoiceNumber);
        tvNetAmount.setText(calculateAmt(obj));
//        tvDuesAmount.setText(calculateAmt(obj));
        tvDistributors.setText(SavePref.fetchShopName());
        tvRoute.setText(SavePref.fetchRoute());
        tvAddress.setText(SavePref.fetchShopAddress());
    }

    private String calculateAmt(List<InvoiceDetailModel> obj) {
        int amt = 0;
        for (int i = 0; i < obj.size(); i++) {
            amt = amt + Integer.parseInt(obj.get(i).Amount);
        }
        return String.valueOf(amt);
    }
}
