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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
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
import com.trio.app.adapters.DistributorStockAdapter;
import com.trio.app.adapters.ItemAdapter;
import com.trio.app.adapters.ItemAdapter1;
import com.trio.app.appcontrollers.GPSTracker;
import com.trio.app.appcontrollers.SavePref;
import com.trio.app.models.CreateInvoiceModel;
import com.trio.app.models.DistributorStockModel;
import com.trio.app.models.InvoiceModel;
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

import cz.msebera.android.httpclient.Header;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bumptech.glide.load.resource.bitmap.TransformationUtils.rotateImage;

public class CreateInvoiceActivity extends AppCompatActivity {
    public static String productName;
    RecyclerView rvItems;
    ItemAdapter1 adapter;
    String shopName;
    TextView tvShopName;
    Toolbar toolbar;
    KProgressHUD hud;
    RelativeLayout rlCreateInvoice;
    List<DistributorStockModel> obj;
    int total = 0;
    String productandQuantity = "";
    String initial = "";
    String shopNam = "";
    public static TextView tvTotalQuantity, tvTotalAmount;
    LinearLayout llMain;
    LinearLayout llShop;
    String userType = SavePref.getLoginData().UserType;
    String title="";
    RelativeLayout rlInvoiceImg;
    ImageView ivInvoiceImage;
    GPSTracker gps;
    Double lattitude;
    Double longitude;
    String licenceNo, imageInvoice;
    String invoiceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_invoice);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.backarrow));
        if (userType.equalsIgnoreCase("Distributor")){
            title = "Create Order";
        }else {
            title = "Create Invoice";
        }
        toolbar.setTitle(title);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateInvoiceActivity.this.finish();
            }
        });
        hud = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setDimAmount(0.5f);

        rlInvoiceImg = findViewById(R.id.rlInvoiceImg);
        ivInvoiceImage = findViewById(R.id.ivInvoiceImage);
        llMain = findViewById(R.id.llMain);
        tvShopName = findViewById(R.id.tvShopName);
        tvTotalQuantity = findViewById(R.id.tvTotalQuantity);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        llShop = findViewById(R.id.llShop);
        llShop.setVisibility(View.GONE);

        if (!userType.equalsIgnoreCase("Distributor")) {
            llShop.setVisibility(View.VISIBLE);
            shopNam = getIntent().getStringExtra("shopname");
            tvShopName.setText(shopNam);
        }

        rvItems = findViewById(R.id.rvItems);
        rlCreateInvoice = findViewById(R.id.rlCreateInvoice);

        rlInvoiceImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rlInvoiceImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                popUp.showFromBottom(v);
                        if (!userType.equalsIgnoreCase("Distributors") || !userType.equalsIgnoreCase("Distributors") ) {
                            loadGallery();
                        }

                    }
                });

            }
        });

        rlCreateInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validate(obj)) {
                    if (userType.equalsIgnoreCase("Distributor")) {
                        createOrder();
                    } else {
                        if (myFile!=null){
                            createInvoice();
                        }else {
                            Toast.makeText(CreateInvoiceActivity.this, "Please select image first", Toast.LENGTH_SHORT).show();
                        }

                    }

                } else {
                    Toast.makeText(CreateInvoiceActivity.this, "Select Item first", Toast.LENGTH_SHORT).show();
                }

//                CreateInvoiceActivity.this.finish();
            }
        });


        rlCreateInvoice.setVisibility(View.GONE);
        llMain.setVisibility(View.GONE);

        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        rvItems.setLayoutManager(manager);
        if (userType.equalsIgnoreCase("Distributor")) {
            getStock();
        } else {
            getDistributorStock();
        }
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
        data.put("InvoiceNumber", invoiceId);
        data.put("LicenseNumber", SavePref.getLoginData().LicenseNumber);

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
                    if (object.getString("Status").contains("Invoice Updated")) {
//                        AddShopActivity.this.finish();
//                        rlAddImage.setVisibility(View.GONE);
                        Toast.makeText(CreateInvoiceActivity.this, "Invoice Updated Successfully", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(CreateInvoiceActivity.this, "Something went Wrong", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                hud.dismiss();
                CreateInvoiceActivity.this.finish();
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
//                rlAddImage.setVisibility(View.VISIBLE);
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

    private void createOrder() {
        String input = productandQuantity;
        String product = input.substring(0, productandQuantity.length() - 3);


        String licenceNo = SavePref.getLoginData().LicenseNumber;
        String Id = SavePref.getLoginData().ID;
        hud.show();
        String url = "http://manage.bytepaper.com/Mobile/Manufacturing/index.php?createOrder&&" + licenceNo + "&&" + Id + "&&" + product;
        ApiInterface apiInterface = ApiClient.getClient();
        Call<CreateInvoiceModel> call = apiInterface.createInvoice(url);
        call.enqueue(new Callback<CreateInvoiceModel>() {
            @Override
            public void onResponse(Call<CreateInvoiceModel> call, Response<CreateInvoiceModel> response) {
                CreateInvoiceModel obj = response.body();
                if (obj.Status.equalsIgnoreCase("Order Created")) {
                    CreateInvoiceActivity.this.finish();
//                    AddShopActivity.totalItems = obj.InvoiceNumber;
                }
                hud.dismiss();
            }

            @Override
            public void onFailure(Call<CreateInvoiceModel> call, Throwable t) {
                hud.dismiss();
            }
        });
    }

    private void getStock() {
        String licenceNo = SavePref.getLoginData().LicenseNumber;
        String Id = SavePref.getLoginData().ID;
        hud.show();

        String url = "http://manage.bytepaper.com/Mobile/Manufacturing/index.php?getDistributorStock&&" + licenceNo + "&&" + Id;
        ApiInterface apiInterface = ApiClient.getClient();
        Call<List<DistributorStockModel>> call = apiInterface.getDistributorStock(url);
        call.enqueue(new Callback<List<DistributorStockModel>>() {
            @Override
            public void onResponse(Call<List<DistributorStockModel>> call, Response<List<DistributorStockModel>> response) {
                obj = response.body();
                if (obj.size() != 0) {
//                    llMain.setVisibility(View.VISIBLE);
//                    adapter = new DistributorStockAdapter(obj);
//                    recyclerView.setAdapter(adapter);
//                    ItemCount(obj);
                    llMain.setVisibility(View.VISIBLE);
                    rlCreateInvoice.setVisibility(View.VISIBLE);
                    adapter = new ItemAdapter1(obj);
//                    productName = obj.get(0).Name;
                    rvItems.setAdapter(adapter);
                } else {
                    Toast.makeText(CreateInvoiceActivity.this, "Distributor's Stock is Empty", Toast.LENGTH_SHORT).show();
                }
                hud.dismiss();
            }

            @Override
            public void onFailure(Call<List<DistributorStockModel>> call, Throwable t) {
                hud.dismiss();
            }
        });
    }


    private boolean validate(List<DistributorStockModel> obj) {
        int item = 0;
        for (DistributorStockModel model : obj) {
            if (model.quantity == null) {
                item = 0;
            } else {
                initial = model.Product + "&-&" + model.quantity + "---";
                productandQuantity = initial.concat(productandQuantity);
                item = Integer.parseInt(model.quantity);
            }

            total = item + total;
        }
        if (total == 0) {
            return false;
        } else {
            return true;
        }
    }

    private void getDistributorStock() {
        String licenceNo = SavePref.getLoginData().LicenseNumber;
        String emailId = SavePref.getLoginData().EmailID;
        hud.show();
        String url = "http://manage.bytepaper.com/Mobile/Manufacturing/index.php?getDistributorStock&&" + licenceNo + "&&" + SavePref.fetchDistributorId();
        ApiInterface apiInterface = ApiClient.getClient();
        Call<List<DistributorStockModel>> call = apiInterface.getDistributorStock(url);
        call.enqueue(new Callback<List<DistributorStockModel>>() {
            @Override
            public void onResponse(Call<List<DistributorStockModel>> call, Response<List<DistributorStockModel>> response) {
                obj = response.body();
                if (obj.size() != 0) {
                    llMain.setVisibility(View.VISIBLE);
                    rlCreateInvoice.setVisibility(View.VISIBLE);
                    adapter = new ItemAdapter1(obj);
//                    productName = obj.get(0).Name;
                    rvItems.setAdapter(adapter);
                } else {

                    Toast.makeText(CreateInvoiceActivity.this, "Stocks not found", Toast.LENGTH_SHORT).show();
                }
                hud.dismiss();
            }

            @Override
            public void onFailure(Call<List<DistributorStockModel>> call, Throwable t) {
                hud.dismiss();
            }
        });
    }

    private void createInvoice() {
        String input = productandQuantity;
        String product = input.substring(0, productandQuantity.length() - 3);


        String shopId = SavePref.fetchShopId();
        String licenceNo = SavePref.getLoginData().LicenseNumber;
        String emailId = SavePref.getLoginData().EmailID;
        hud.show();
//        String url = "http://manage.bytepaper.com/Mobile/Manufacturing/index.php?createInvoice&&" + licenceNo + "&&" + emailId + "&&" + shopId + "&&" + productName + "&-&" + total;
        String url = "http://manage.bytepaper.com/Mobile/Manufacturing/index.php?createInvoice&&" + licenceNo + "&&" + emailId + "&&" + shopId + "&&" + product;
        ApiInterface apiInterface = ApiClient.getClient();
        Call<CreateInvoiceModel> call = apiInterface.createInvoice(url);
        call.enqueue(new Callback<CreateInvoiceModel>() {
            @Override
            public void onResponse(Call<CreateInvoiceModel> call, Response<CreateInvoiceModel> response) {
                CreateInvoiceModel obj = response.body();
                if (obj.Status.equalsIgnoreCase("Invoice Created")) {
                    hud.dismiss();

//                    if (getIntent().getStringExtra("actname").equals("AddShop")) {
//                        AddShopActivity.completeInvoice = "1";
                        getInvoiceList();
//                    }
//                    AddShopActivity.totalItems = obj.InvoiceNumber;
                }
                hud.dismiss();
            }


            @Override
            public void onFailure(Call<CreateInvoiceModel> call, Throwable t) {
                hud.dismiss();
            }
        });
    }

    private void getInvoiceList() {
        String shopid = SavePref.fetchShopId();
        String licenceNo = SavePref.getLoginData().LicenseNumber;
        String emailId = SavePref.getLoginData().EmailID;
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
                    invoiceId = obj.get(obj.size()-1).InvoiceNumber;
                    try {
                        hud.dismiss();
                        addImageToInvoice();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
//                    rlViewPager.setVisibility(View.VISIBLE);
//                    cvNotFound.setVisibility(View.GONE);
//                    setupViewPager(viewPager, obj);
//                } else {
//                    cvNotFound.setVisibility(View.VISIBLE);
//                    rlViewPager.setVisibility(View.GONE);
//                    obj.clear();
////                    setupViewPager(viewPager, obj);
//                    Toast.makeText(InvoicesActivity.this, "Invoices not found for this shop", Toast.LENGTH_SHORT).show();
                }
                hud.dismiss();


            }

            @Override
            public void onFailure(Call<List<InvoiceModel>> call, Throwable t) {
//                cvNotFound.setVisibility(View.VISIBLE);
//                rlViewPager.setVisibility(View.GONE);
                hud.dismiss();
            }
        });
    }

    public static void setValuesToText(int totalAmount1, int totalQuantity1) {
        tvTotalQuantity.setText(String.valueOf(totalQuantity1));
        tvTotalAmount.setText(String.valueOf(totalAmount1));
    }
}
