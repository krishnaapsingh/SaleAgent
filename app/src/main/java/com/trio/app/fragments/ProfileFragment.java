package com.trio.app.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.trio.app.BuildConfig;
import com.trio.app.R;
import com.trio.app.activities.MainActivity;
import com.trio.app.appcontrollers.AppUtility;
import com.trio.app.appcontrollers.ImageFilePath;
import com.trio.app.appcontrollers.SavePref;
import com.trio.app.models.Login;
import com.trio.app.models.UpdateProfile;
import com.trio.app.rest.ApiClient;
import com.trio.app.rest.ApiInterface;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Savepoint;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

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
    EditText etUserName, etEmail, etMobile,etPassword;
//            etAdd, etCompany, etPan, etAdhar, etBankAccNo, etIfsc, etBankName;
//    ImageView  ivCam;
    CircleImageView ivProfileImage;
//    Button btnUpdateProfile;
    String userChoosenTask;
    int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    boolean result;
    File myFile;
    String img = "";
    Uri uri;
    String realPath;
    String encodedImage;
    MainActivity activity;
    AVLoadingIndicatorView avLoadingIndicatorView;
    static Calendar myCalendar;

    @SuppressLint("ValidFragment")
    public ProfileFragment(MainActivity context) {
        this.activity = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        setLayout(v);
        setValues();
        setLayoutDisable();
        myCalendar = Calendar.getInstance();

        return v;
    }

    public void datePicker(View view) {

        DialogFragment dialogfragment = new DatePickerDialogTheme();
        dialogfragment.show(getFragmentManager(), "Theme");
    }

    public static class DatePickerDialogTheme extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        private static void setDate(final Calendar calendar) {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            if (calendar.getTime().before(myCalendar.getTime())) {
            }
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(getActivity(),
                    android.app.AlertDialog.THEME_HOLO_LIGHT, this, year, month, day);
            return dialog;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            Calendar cal = new GregorianCalendar(year, month, day);
            setDate(cal);
        }
    }

    private void setValues() {

        etUserName.setText(SavePref.getLoginData().UserName);
        etEmail.setText(SavePref.getLoginData().EmailID);
        etMobile.setText(SavePref.getLoginData().contactnum);

    }

    private void setLayout(View v) {

        etUserName = v.findViewById(R.id.etUserName);
        etEmail = v.findViewById(R.id.etEmail);
        etMobile = v.findViewById(R.id.etMobile);
        etPassword = v.findViewById(R.id.etPassword);

    }



    private void selectImage() {

        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder((getActivity()));
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = AppUtility.checkPermission(getActivity());

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void setLayoutEnable() {

//        ivCam.setVisibility(View.VISIBLE);
//        ivCam.setEnabled(true);
        etUserName.setEnabled(true);
        etEmail.setEnabled(true);
        etMobile.setEnabled(true);

    }

    private void setLayoutDisable() {

//        ivCam.setVisibility(View.INVISIBLE);


//        ivCam.setEnabled(false);
        etUserName.setEnabled(false);
        etEmail.setEnabled(false);
        etMobile.setEnabled(false);
//        etDob.setEnabled(false);
//          ivCalendar.setEnabled(false);
//        btnUpdateProfile.setEnabled(false);
    }

    private void cameraIntent() {

        Intent CamIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        File file = new File(Environment.getExternalStorageDirectory(),
                "file" + String.valueOf(System.currentTimeMillis()) + ".jpg");
        uri = FileProvider.getUriForFile(getActivity(),
                BuildConfig.APPLICATION_ID + ".provider",
                file);
        CamIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, uri);
        CamIntent.putExtra("return-data", true);
        startActivityForResult(CamIntent, REQUEST_CAMERA);
    }

    private void galleryIntent() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap = null;

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                onSelectFromGalleryResult(data);
            } else if (requestCode == REQUEST_CAMERA) {
                onCaptureImageResult();
            }
        }
    }

    private void onSelectFromGalleryResult(Intent data) {
        realPath = ImageFilePath.getPath(getActivity(), data.getData());
        myFile = new File(realPath);
        ivProfileImage.setImageBitmap(BitmapFactory.decodeFile(realPath));
        Bitmap bm = BitmapFactory.decodeFile(realPath);

        img = convertToBitmap(bm);
    }

    private void onCaptureImageResult() {
        Bitmap thumbnailC = null;
        try {
            thumbnailC = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ;
        File outFile = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpeg");
        FileOutputStream fos = null;
        myFile = outFile;
        realPath = myFile.getPath();
        //  ivProfileImage.setImageResource(0);
        ivProfileImage.setImageBitmap(thumbnailC);
        img = convertToBitmap(thumbnailC);
        try

        {
            fos = new FileOutputStream(outFile);
            thumbnailC.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case AppUtility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if (userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Sorry !");
                    builder.setMessage("Sorry You have to allow permission for Image!");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            result = AppUtility.checkPermission(getActivity());
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getActivity(), "Sorry", Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.show();
                }
                break;
        }
    }

    public String convertToBitmap(Bitmap bm) {


        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return imageString;
    }


}
