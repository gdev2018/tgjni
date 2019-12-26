package com.github.gdev2018.app;

import android.content.Context;

import com.github.gdev2018.master.di.BaseApplication;

//import org.liveseyinc.plabor.BuildVars;
//import org.liveseyinc.plabor.LocalAndroidUtilities;

///*import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.GooglePlayServicesUtil;
//import com.google.firebase.iid.FirebaseInstanceId;*/

//import com.frogermcs.androiddevmetrics.AndroidDevMetrics;

///*import timber.log.Timber;*/

//import org.liveseyinc.plabor.DaggerAppComponent;


/**
 * Created by Livesey Inc. on 09.11.2017.
 */

public class TestApplication extends BaseApplication {

    public static volatile Context mApplicationContext;

    @Override
    public void onCreate() {
        try {
            mApplicationContext = this.getApplicationContext();
        } catch (Throwable var2) {
        }

        super.onCreate();
        if (mApplicationContext == null) {
            mApplicationContext = this.getApplicationContext();
        }
    }

//
//    public static void postInitApplication() {
//
//        BaseApplication.postInitApplication();  // not super.postInitApplication();
//
//        if (mApplicationInited) {
//            return;
//        }
//
//        mApplicationInited = true;
//
//        // Localization settings
//        try {
//            LocaleController.getInstance();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        try {
//            final IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
//            filter.addAction(Intent.ACTION_SCREEN_OFF);
//            final BroadcastReceiver mReceiver = new ScreenReceiver();
//            mApplicationContext.registerReceiver(mReceiver, filter);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        try {
//            PowerManager pm = (PowerManager)PlaborApplication.mApplicationContext.getSystemService(Context.POWER_SERVICE);
//            isScreenOn = pm.isScreenOn();
//            if (BaseBuildVars.LOGS_ENABLED) {
//                FileLog.d("screen state = " + isScreenOn);
//            }
//        } catch (Exception e) {
//            FileLog.e(e);
//        }
//
//        SharedConfig.loadConfig();
//        for (int a = 0; a < BaseUserConfig.MAX_ACCOUNT_COUNT; a++) {
//            BaseUserConfig.getInstance(a).loadConfig();
//
//            // TODO: 2019-05-11 uncomment this
//            ////MessagesController.getInstance(a);
//            ////ConnectionsManager.getInstance(a);
//            TLRPC.User user = BaseUserConfig.getInstance(a).getCurrentUser();
//            if (user != null) {
//                MessagesController.getInstance(a).putUser(user, true);
//                MessagesController.getInstance(a).getBlockedUsers(true);
/////*                SendMessagesHelper.getInstance(a).checkUnsentMessages();*/
//            }
//        }
//
//        PlaborApplication app = (PlaborApplication)PlaborApplication.mApplicationContext;
//        app.initPlayServices();
//        if (BaseBuildVars.LOGS_ENABLED) {
//            FileLog.d("app initied");
//        }
//
/////*        MediaController.getInstance();
////        for (int a = 0; a < BaseUserConfig.MAX_ACCOUNT_COUNT; a++) {
////            ContactsController.getInstance(a).checkAppAccount();
////            DownloadController.getInstance(a);
////        }
////
////        WearDataLayerListenerService.updateWatchConnectionState();*/
//
//        LocalAndroidUtilities.runOnUIThread(PlaborApplication::startPushService);
//    }
//
//
//    public static void startPushService() {
/////*        SharedPreferences preferences = BaseApplication.getGlobalBroadcastingsSettings();*/
//       SharedPreferences preferences = mGlobalPreferences;
//
//        if (preferences.getBoolean("pushService", true)) {
//            try {
//                mApplicationContext.startService(new Intent(mApplicationContext, NotificationsService.class));
//            } catch (Throwable ignore) {
//
//            }
//        } else {
//            stopPushService();
//        }
//    }
//
//    public static void stopPushService() {
//        mApplicationContext.stopService(new Intent(mApplicationContext, NotificationsService.class));
//
//        PendingIntent pintent = PendingIntent.getService(mApplicationContext, 0, new Intent(mApplicationContext, NotificationsService.class), 0);
//        AlarmManager alarm = (AlarmManager)mApplicationContext.getSystemService(Context.ALARM_SERVICE);
//        alarm.cancel(pintent);
//    }
//
//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//        try {
//            LocaleController.getInstance().onDeviceConfigurationChange(newConfig);
//            LocalAndroidUtilities.checkDisplaySize(mApplicationContext, newConfig);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void initPlayServices() {
//        LocalAndroidUtilities.runOnUIThread(() -> {
//            if (checkPlayServices()) {
//                final String currentPushString = SharedConfig.pushString;
//                if (!TextUtils.isEmpty(currentPushString)) {
//                    if (BaseBuildVars.LOGS_ENABLED) {
//                        FileLog.d("GCM regId = " + currentPushString);
//                    }
//                } else {
//                    if (BaseBuildVars.LOGS_ENABLED) {
//                        FileLog.d("GCM Registration not found.");
//                    }
//                }
//                Utilities.globalQueue.postRunnable(() -> {
//                    try {
/////*                        String token = FirebaseInstanceId.getInstance().getToken();
////                        if (!TextUtils.isEmpty(token)) {
////                            GcmInstanceIDListenerService.sendRegistrationToServer(token);
////                        }*/
//                    } catch (Throwable e) {
//                        FileLog.e(e);
//                    }
//                });
//            } else {
//                if (BaseBuildVars.LOGS_ENABLED) {
//                    FileLog.d("No valid Google Play Services APK found.");
//                }
//            }
//        }, 1000);
//    }
//
//    private boolean checkPlayServices() {
//        try {
/////*            int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
////            return resultCode == ConnectionResult.SUCCESS;*/
//        } catch (Exception e) {
//            FileLog.e(e);
//        }
//        return true;
//    }
}
