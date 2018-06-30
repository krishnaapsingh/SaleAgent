package com.trio.app.appcontrollers;

/**
 * Created by Trieffects on 25-08-2016.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

public class GPSTracker extends Service implements LocationListener, ActivityCompat.OnRequestPermissionsResultCallback {

    private final Context mContext;

    // flag for GPS status
    boolean isGPSEnabled = false;

    // flag for network status
    boolean isNetworkEnabled = false;
    boolean isWiFiEnabled = false;
    // flag for GPS status
    boolean canGetLocation = false;

    Location location; // location
    double latitude; // latitude
    double longitude; // longitude
    String StateName, CityName, CountryName, LocalAddress, Throughfare, Subthroughfare;
    int i;
    private static final int PERMISSION_REQUEST_CODE = 1;
    String[] PermissionsLocation =
            {
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
            };
    private View mLayout;


    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

    // Declaring a Location Manager
    protected LocationManager locationManager;

    public GPSTracker(Context mContext) {
        this.mContext = mContext;
        getLocation();
    }

    public Location getLocation() {
        try {
            locationManager = (LocationManager) mContext
                    .getSystemService(LOCATION_SERVICE);

            // getting GPS status
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);
//
            // getting network status
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

//            WifiManager wifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);

//
            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
            } else {
                this.canGetLocation = true;
                // First get location from Network Provider
                if (isNetworkEnabled) {

                    Log.e("BUILD_Version=", String.valueOf(Build.VERSION.SDK_INT));
//                    if ((int) Build.VERSION.SDK_INT >= 23) {
//                        checkPermission_Location();

                    if (ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED
                            && ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.

                        Log.d("PERMISSION ", "Not_Granted");
                        ActivityCompat.requestPermissions((Activity) mContext, PermissionsLocation, PERMISSION_REQUEST_CODE);
//                            requestStoragePermission();
                    } else {

                        Log.d("PERMISSION ", "Granted");
                        get_LocationDetails();
                    }
//                    } else {
//                        get_LocationDetails();
//                    }

                }
                // if GPS Enabled get lat/long using GPS Services
                else if (isGPSEnabled) {
//                    Toast.makeText(this,"BUILD VERSION="+Build.VERSION.SDK_INT,Toast.LENGTH_LONG).show();
//
                    if (ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED
                            && ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {

                        Log.d("PERMISSION ", "Not_Granted");
                        ActivityCompat.requestPermissions((Activity) mContext, PermissionsLocation, PERMISSION_REQUEST_CODE);
//                        requestStoragePermission();
                    } else {

                        Log.d("PERMISSION ", "Granted");
                        get_LocationDetails();
                    }
                }


            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return location;
    }


    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // All good!
                    Toast.makeText(this, "It's good", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Need your location!", Toast.LENGTH_SHORT).show();
                }

                break;
        }

    }


    public void get_LocationDetails() {

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                MIN_TIME_BW_UPDATES,
                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
        Log.d("Network", "Network");
        if (locationManager != null) {
            location = locationManager
                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }
        }
    }


    /**
     * Stop using GPS listener
     * Calling this function will stop using GPS in your app
     */
    public void stopUsingGPS() {
        if (locationManager != null) {
            if (ActivityCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            locationManager.removeUpdates(GPSTracker.this);
        }
    }

    /**
     * Function to get latitude
     */
    public double getLatitude() {
        if (location != null) {
            latitude = location.getLatitude();
        }

        // return latitude
        return latitude;
    }

    /**
     * Function to get longitude
     */
    public double getLongitude() {
        if (location != null) {
            longitude = location.getLongitude();
        }

        // return longitude
        return longitude;
    }

    public String Address() {
        try {
// Getting address from found locations.
            Log.e("ENTER IN GEOCODER =", String.valueOf(StateName));
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(mContext, Locale.getDefault());
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(),1);
            Log.e("ADDRESSES IN GEOCODER =", String.valueOf(addresses));
            if (addresses != null && addresses.size() > 0) {

                StateName = addresses.get(0).getAdminArea();
                CountryName = addresses.get(0).getCountryName();

                Throughfare = addresses.get(0).getAddressLine(0);
                Subthroughfare = addresses.get(0).getAddressLine(1);
                CityName = addresses.get(0).getAddressLine(2);
//                LocalAddress = addresses.get(0).getAddressLine()
//                Address returnAddress = addresses.get(0);
//                for (i = 0; i < returnAddress.getMaxAddressLineIndex(); i++) {
                LocalAddress = (Throughfare + "," + Subthroughfare + "," + CityName);
//                returnAddress.getAddressLine(i) }
                // you can get more details other than this . like country code, state code, etc.
                Log.e("StateName =", String.valueOf(StateName));
                Log.e("CityName =", String.valueOf(CityName));
                Log.e("CountryName =", String.valueOf(CountryName));
                Log.e("LocalAddress =", String.valueOf(LocalAddress));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return LocalAddress;
    }

    /**
     * Function to check GPS/wifi enabled
     *
     * @return boolean
     */
    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    /**
     * Function to show settings alert dialog
     * On pressing Settings button will lauch Settings Options
     */
    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        // Setting Dialog Title
        alertDialog.setTitle("GPS Settings");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }


    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }


}


