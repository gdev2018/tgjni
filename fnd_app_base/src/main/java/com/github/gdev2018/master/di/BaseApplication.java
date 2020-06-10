/*
 * This is the source code of Powerlabor (TM) for Android
 * It is licensed under GNU GPL v. 2 or later.
 * You should have received a copy of the license in this archive (see LICENSE).
 *
 * Copyright Livesey Inc. (c) 2007-2019.
 */

package com.github.gdev2018.master.di;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.telephony.TelephonyManager;

import com.github.gdev2018.master.BuildConfig;
import com.github.gdev2018.master.FileLog;
import com.github.gdev2018.master.NativeLoader;
import com.github.gdev2018.master.StatsController;
import com.github.gdev2018.master.tgnet.ConnectionsManager;
import com.github.gdev2018.master.ui.Components.ForegroundDetector;

import java.io.File;

//import com.frogermcs.androiddevmetrics.AndroidDevMetrics;

///*import timber.log.Timber;*/

//import com.github.gdev2018.master.DaggerAppComponent;


/**
 * Created by Livesey Inc. on 09.11.2017.
 */

public class BaseApplication extends Application {

    @SuppressLint("StaticFieldLeak")
    public static volatile Context mApplicationContext;
    public static volatile NetworkInfo currentNetworkInfo;
    public static volatile boolean unableGetCurrentNetwork;
    public static volatile Handler mApplicationHandler;
    public static SharedPreferences mGlobalMainSettings;

    public static volatile SharedPreferences mGlobalBroadcastingsPreferences;
    public static volatile SharedPreferences mGlobalMainPreferences;
    public static volatile SharedPreferences mGlobalEmojiPreferences;

    protected static volatile boolean mApplicationInited = false;
    public static volatile boolean isScreenOn = false;
    public static volatile boolean mainInterfacePaused = true;
    public static volatile boolean externalInterfacePaused = true;
    public static volatile boolean mainInterfacePausedStageQueue = true;
    public static volatile long mainInterfacePausedStageQueueTime;
    private static ConnectivityManager connectivityManager;


    public static File getFilesDirFixed() {
        for (int a = 0; a < 10; a++) {
            File path = mApplicationContext.getFilesDir();
            if (path != null) {
                return path;
            }
        }
        try {
            ApplicationInfo info = mApplicationContext.getApplicationInfo();
            File path = new File(info.dataDir, "files");
            path.mkdirs();
            return path;
        } catch (Exception e) {
            FileLog.e(e);
        }
        return new File("/data/data/com.github.gdev2018.master/files");
    }

    public static void postInitApplication() {

    }

    public BaseApplication() {
        super();
    }

    @Override
    public void onCreate() {
        try {
            mApplicationContext = getApplicationContext();
        } catch (Throwable ignore) {

        }

        super.onCreate();

        if (mApplicationContext == null) {
            mApplicationContext = getApplicationContext();
        }

        // TODO: 2019-05-08 uncomment this
        NativeLoader.initNativeLibs(mApplicationContext);
        ConnectionsManager.native_setJava(false);
        new ForegroundDetector(this);

        mApplicationHandler = new Handler(mApplicationContext.getMainLooper());

//        AndroidUtilities.runOnUIThread(BaseApplication::startPushService);

        mGlobalBroadcastingsPreferences = mApplicationContext.getSharedPreferences("Broadcastings", Activity.MODE_PRIVATE);
        mGlobalMainPreferences = mApplicationContext.getSharedPreferences("mainconfig", Activity.MODE_PRIVATE);
        mGlobalEmojiPreferences = mApplicationContext.getSharedPreferences("emoji", Activity.MODE_PRIVATE);

        // If Debug mode then show additional metrics
        if (BuildConfig.DEBUG) {
            // метрики, что-ли какие-то
//            AndroidDevMetrics.initWith(this);

///*            // logger
//            Timber.plant(new Timber.DebugTree());*/
        }
///*        initAppComponent();*/


    }

    public static SharedPreferences getGlobalBroadcastingsSettings() {
        return mGlobalBroadcastingsPreferences;
    }
    public static SharedPreferences getGlobalMainSettings() {
        return mGlobalMainPreferences;
    }
    public static SharedPreferences getGlobalEmojiSettings() {
        return mGlobalEmojiPreferences;
    }

    public static BaseApplication get(Context context) {
        return (BaseApplication) context.getApplicationContext();
    }

///*    private void initAppComponent() {
//        appComponent = DaggerAppComponent.builder()
//                .appModule(new AppModule(this))
//                .build();
//    }
//
//    public UserComponent createUserComponent(User user) {
//        userComponent = appComponent.plus(new UserModule(user));
//        return userComponent;
//    }
//
//    public void releaseUserComponent() {
//        userComponent = null;
//    }
//
//    public AppComponent getAppComponent() {
//        return appComponent;
//    }
//
//    public UserComponent getUserComponent() {
//        return userComponent;
//    }*/






    public static boolean isRoaming() {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) BaseApplication.mApplicationContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            if (netInfo != null) {
                return netInfo.isRoaming();
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
        return false;
    }

    public static boolean isConnectedOrConnectingToWiFi() {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) BaseApplication.mApplicationContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            NetworkInfo.State state = netInfo.getState();
            if (netInfo != null && (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING || state == NetworkInfo.State.SUSPENDED)) {
                return true;
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
        return false;
    }

    public static boolean isConnectedToWiFi() {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) BaseApplication.mApplicationContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (netInfo != null && netInfo.getState() == NetworkInfo.State.CONNECTED) {
                return true;
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
        return false;
    }

    public static int getCurrentNetworkType() {
        if (isConnectedOrConnectingToWiFi()) {
            return StatsController.TYPE_WIFI;
        } else if (isRoaming()) {
            return StatsController.TYPE_ROAMING;
        } else {
            return StatsController.TYPE_MOBILE;
        }
    }

    public static boolean isConnectionSlow() {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) BaseApplication.mApplicationContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            if (netInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                switch (netInfo.getSubtype()) {
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_IDEN:
                        return true;
                }
            }
        } catch (Throwable ignore) {

        }
        return false;
    }

    public static boolean isNetworkOnline() {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) BaseApplication.mApplicationContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            if (netInfo != null && (netInfo.isConnectedOrConnecting() || netInfo.isAvailable())) {
                return true;
            }

            netInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                return true;
            } else {
                netInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                    return true;
                }
            }
        } catch (Exception e) {
            FileLog.e(e);
            return true;
        }
        return false;
    }

}

