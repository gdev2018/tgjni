package gdev2018.github.com.fnd_app_base;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.PowerManager;
import android.text.TextUtils;

import com.github.gdev2018.master.FileLog;
import com.github.gdev2018.master.LocaleController;
import com.github.gdev2018.master.MessagesController;
import com.github.gdev2018.master.NotificationsService;
import com.github.gdev2018.master.ScreenReceiver;
import com.github.gdev2018.master.SharedConfig;
import com.github.gdev2018.master.UserConfig;
import com.github.gdev2018.master.Utilities;
import com.github.gdev2018.master.di.BaseApplication;
import com.github.gdev2018.master.tgnet.TLRPC;

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

    @Override
    public void onCreate() {
        super.onCreate();
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
//            if (BuildVars.LOGS_ENABLED) {
//                FileLog.d("screen state = " + isScreenOn);
//            }
//        } catch (Exception e) {
//            FileLog.e(e);
//        }
//
//        SharedConfig.loadConfig();
//        for (int a = 0; a < UserConfig.MAX_ACCOUNT_COUNT; a++) {
//            UserConfig.getInstance(a).loadConfig();
//
//            // TODO: 2019-05-11 uncomment this
//            ////MessagesController.getInstance(a);
//            ////ConnectionsManager.getInstance(a);
//            TLRPC.User user = UserConfig.getInstance(a).getCurrentUser();
//            if (user != null) {
//                MessagesController.getInstance(a).putUser(user, true);
//                MessagesController.getInstance(a).getBlockedUsers(true);
/////*                SendMessagesHelper.getInstance(a).checkUnsentMessages();*/
//            }
//        }
//
//        PlaborApplication app = (PlaborApplication)PlaborApplication.mApplicationContext;
//        app.initPlayServices();
//        if (BuildVars.LOGS_ENABLED) {
//            FileLog.d("app initied");
//        }
//
/////*        MediaController.getInstance();
////        for (int a = 0; a < UserConfig.MAX_ACCOUNT_COUNT; a++) {
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
/////*        SharedPreferences preferences = MessagesController.getGlobalNotificationsSettings();*/
//       SharedPreferences preferences = mMainPreferences;
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
//                    if (BuildVars.LOGS_ENABLED) {
//                        FileLog.d("GCM regId = " + currentPushString);
//                    }
//                } else {
//                    if (BuildVars.LOGS_ENABLED) {
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
//                if (BuildVars.LOGS_ENABLED) {
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
