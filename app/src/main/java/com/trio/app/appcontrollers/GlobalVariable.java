package com.trio.app.appcontrollers;

import android.app.Application;
import android.util.Log;

import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.LogInterceptor;



public class GlobalVariable extends Application {

    public static final String TAG = GlobalVariable.class.getSimpleName();

    private static GlobalVariable mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        timeHawkInit();
    }

    public static synchronized GlobalVariable getInstance() {
        return mInstance;
    }

    private void timeHawkInit() {
        long startTime = System.currentTimeMillis();

        Hawk.init(this).setLogInterceptor(new LogInterceptor() {
            @Override
            public void onLog(String message) {
                Log.d("HAWK", message);
            }
        }).build();

        long endTime = System.currentTimeMillis();
        System.out.println("Hawk.init: " + (endTime - startTime) + "ms");
    }

    public void setConnectivityListener(AppUtility.ConnectivityReceiver.ConnectivityReceiverListener listener) {
        AppUtility.ConnectivityReceiver.connectivityReceiverListener = listener;
    }

}
