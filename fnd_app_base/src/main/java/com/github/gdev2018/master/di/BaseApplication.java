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
import android.os.Handler;

import com.github.gdev2018.master.BuildConfig;
import com.github.gdev2018.master.FileLog;
import com.github.gdev2018.master.NativeLoader;
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
    public static volatile Handler mApplicationHandler;
    public static SharedPreferences mMainPreferences;

    //    @SuppressLint("StaticFieldLeak")
    protected static volatile boolean mApplicationInited = false;
    public static volatile boolean isScreenOn = false;
    public static volatile boolean mainInterfacePaused = true;
    public static volatile boolean externalInterfacePaused = true;
    public static volatile boolean mainInterfacePausedStageQueue = true;
    public static volatile long mainInterfacePausedStageQueueTime;

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
        ////NativeLoader.initNativeLibs(mApplicationContext);
        ////ConnectionsManager.native_setJava(false);
        new ForegroundDetector(this);

        mApplicationHandler = new Handler(mApplicationContext.getMainLooper());

//        AndroidUtilities.runOnUIThread(BaseApplication::startPushService);

        mMainPreferences = mApplicationContext.getSharedPreferences("mainconfig", Activity.MODE_PRIVATE);

        // If Debug mode then show additional metrics
        if (BuildConfig.DEBUG) {
            // метрики, что-ли какие-то
//            AndroidDevMetrics.initWith(this);

///*            // logger
//            Timber.plant(new Timber.DebugTree());*/
        }
///*        initAppComponent();*/


    }

    public static void postInitApplication() {

    }

    public static SharedPreferences getGlobalMainSettings() {
        return mMainPreferences;
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
            // TODO: 2019-02-14 uncomment after implements filelog
            FileLog.e(e);
        }
        return new File("/data/data/com.github.gdev2018.master/files");
    }

}

