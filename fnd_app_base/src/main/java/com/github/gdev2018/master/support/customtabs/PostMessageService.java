/*
 * This is the source code of Powerlabor (TM) for Android
 * It is licensed under GNU GPL v. 2 or later.
 * You should have received a copy of the license in this archive (see LICENSE).
 *
 * Copyright Livesey Inc. (c) 2007-2018.
 */

package com.github.gdev2018.master.support.customtabs;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;

/**
 * A service to receive postMessage related communication from a Custom Tabs provider.
 */
public class PostMessageService extends Service {
    private IPostMessageService.Stub mBinder = new IPostMessageService.Stub() {

        @Override
        public void onMessageChannelReady(
                ICustomTabsCallback callback, Bundle extras) throws RemoteException {
            callback.onMessageChannelReady(extras);
        }

        @Override
        public void onPostMessage(ICustomTabsCallback callback,
                                  String message, Bundle extras) throws RemoteException {
            callback.onPostMessage(message, extras);
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
}
