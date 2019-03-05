/*
 * This is the source code of Powerlabor (TM) for Android
 * It is licensed under GNU GPL v. 2 or later.
 * You should have received a copy of the license in this archive (see LICENSE).
 *
 * Copyright Livesey Inc. (c) 2007-2019.
 */

package com.github.gdev2018.master;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import com.github.gdev2018.master.di.BaseApplication;

public class GcmInstanceIDListenerService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        try {
            final String refreshedToken = FirebaseInstanceId.getInstance().getToken();
            AndroidUtilities.runOnUIThread(() -> {
                if (BaseBuildVars.LOGS_ENABLED) {
                    FileLog.d("Refreshed token: " + refreshedToken);
                }
//                BaseApplication.postInitApplication();
                sendRegistrationToServer(refreshedToken);
            });
        } catch (Throwable e) {
            FileLog.e(e);
        }
    }

    public static void sendRegistrationToServer(final String token) {
        Utilities.stageQueue.postRunnable(() -> {
            SharedConfig.pushString = token;
            for (int a = 0; a < UserConfig.MAX_ACCOUNT_COUNT; a++) {
                UserConfig userConfig = UserConfig.getInstance(a);
                userConfig.registeredForPush = false;
                userConfig.saveConfig(false);
                if (userConfig.getClientUserId() != 0) {
                    final int currentAccount = a;
///*                    AndroidUtilities.runOnUIThread(() -> MessagesController.getInstance(currentAccount).registerForPush(token));*/
                }
            }
        });
    }
}
