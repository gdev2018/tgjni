/*
 * This is the source code of Powerlabor (TM) for Android
 * It is licensed under GNU GPL v. 2 or later.
 * You should have received a copy of the license in this archive (see LICENSE).
 *
 * Copyright Livesey Inc. (c) 2007-2019.
 */

package com.github.gdev2018.master;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;

import com.github.gdev2018.master.di.BaseApplication;

public class NotificationsService extends Service {

    @Override
    public void onCreate() {
        BaseApplication.postInitApplication();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onDestroy() {
///*        SharedPreferences preferences = MessagesController.getGlobalNotificationsSettings();
//        if (preferences.getBoolean("pushService", true)) {
//            Intent intent = new Intent("com.github.gdev2018.fnd.start");
//            sendBroadcast(intent);
//        }*/
    }
}
