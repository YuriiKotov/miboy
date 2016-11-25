package com.boco.miboy;

import android.app.Application;
import android.util.Log;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

public class MiboyApp extends Application {
    private static final String TAG = MiboyApp.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "=====================================================================");
        Log.i(TAG, "==================== Create Miboy Application ========================");
        FacebookSdk.sdkInitialize(this);
        AppEventsLogger.activateApp(this);
    }
}