/*
 * This is the source code of Powerlabor (TM) for Android
 * It is licensed under GNU GPL v. 2 or later.
 * You should have received a copy of the license in this archive (see LICENSE).
 *
 * Copyright Livesey Inc. (c) 2007-2019.
 */

package com.github.gdev2018.master;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.github.gdev2018.master.di.BaseApplication;

public class NotificationCallbackReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null) {
            return;
        }
        BaseApplication.postInitApplication();
        int currentAccount = intent.getIntExtra("currentAccount", BaseUserConfig.selectedAccount);
        long did = intent.getLongExtra("did", 777000);
        byte[] data = intent.getByteArrayExtra("data");
        int mid = intent.getIntExtra("mid", 0);
///*        SendMessagesHelper.getInstance(currentAccount).sendNotificationCallback(did, mid, data);*/
    }
}
