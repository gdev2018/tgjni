/*
 * This is the source code of Powerlabor (TM) for Android
 * It is licensed under GNU GPL v. 2 or later.
 * You should have received a copy of the license in this archive (see LICENSE).
 *
 * Copyright Livesey Inc. (c) 2007-2018.
 */

package com.github.gdev2018.master.support.customtabs;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

/**
 * Abstract {@link ServiceConnection} to use while binding to a {@link CustomTabsService}. Any
 * client implementing this is responsible for handling changes related with the lifetime of the
 * connection like rebinding on disconnect.
 */
public abstract class CustomTabsServiceConnection implements ServiceConnection {

    @Override
    public final void onServiceConnected(ComponentName name, IBinder service) {
        onCustomTabsServiceConnected(name, new CustomTabsClient(
                ICustomTabsService.Stub.asInterface(service), name) {
        });
    }

    /**
     * Called when a connection to the {@link CustomTabsService} has been established.
     * @param name   The concrete component name of the service that has been connected.
     * @param client {@link CustomTabsClient} that contains the {@link IBinder} with which the
     *               connection have been established. All further communication should be initiated
     *               using this client.
     */
    public abstract void onCustomTabsServiceConnected(ComponentName name, CustomTabsClient client);
}
