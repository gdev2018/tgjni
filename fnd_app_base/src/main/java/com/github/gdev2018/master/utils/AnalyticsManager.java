package com.github.gdev2018.master.utils;

import android.app.Application;
import android.nfc.Tag;
import android.util.Log;

import com.github.gdev2018.master.BuildConfig;

///*import timber.log.Timber;*/

/**
 * Created by Livesey Inc. on 2017-11-10.
 */

public class AnalyticsManager {
    private static final String TAG = "->>" + "AnalyticsManager";

    private Application app;

    public AnalyticsManager(Application app) {
        this.app = app;
    }

    public void logScreenView(String screenName) {
        if (BuildConfig.DEBUG) Log.d(TAG, "Logged screen name: " + screenName);
///*        Timber.d("Logged screen name: " + screenName);*/
    }
}
