package com.rosario.hp.goldrules;


import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.rosario.hp.goldrules.utils.Logger;

import io.fabric.sdk.android.Fabric;

public class BeaconApplication extends Application {

    private final String TAG = "BeaconApplication";


    @Override
    public void onCreate() {
        Logger.d(TAG, "Starting Application");
        super.onCreate();
        Fabric.with(this, new Crashlytics());
    }

}