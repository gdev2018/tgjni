/*  * This is the source code of Telegram for Android v. 5.x.x.  * It is licensed under GNU GPL v. 2 or later.  * You should have received a copy of the license in this archive (see LICENSE).  *  * Copyright Nikolai Kudashov, 2013-2018.  */

package com.github.gdev2018.master.support.customtabs;

import android.os.Bundle;

/**
 * A callback class for custom tabs client to get messages regarding events in their custom tabs.
 */
public class CustomTabsCallback {
    /**
     * Sent when the tab has started loading a page.
     */
    public static final int NAVIGATION_STARTED = 1;

    /**
     * Sent when the tab has finished loading a page.
     */
    public static final int NAVIGATION_FINISHED = 2;

    /**
     * Sent when the tab couldn't finish loading due to a failure.
     */
    public static final int NAVIGATION_FAILED = 3;

    /**
     * Sent when loading was aborted by a user action before it finishes like clicking on a link
     * or refreshing the page.
     */
    public static final int NAVIGATION_ABORTED = 4;

    /**
     * Sent when the tab becomes visible.
     */
    public static final int TAB_SHOWN = 5;

    /**
     * Sent when the tab becomes hidden.
     */
    public static final int TAB_HIDDEN = 6;

    /**
     * To be called when a navigation event happens.
     *
     * @param navigationEvent The code corresponding to the navigation event.
     * @param extras Reserved for future use.
     */
    public void onNavigationEvent(int navigationEvent, Bundle extras) {}

    /**
     * Unsupported callbacks that may be provided by the implementation.
     *
     * <p>
     * <strong>Note:</strong>Clients should <strong>never</strong> rely on this callback to be
     * called and/or to have a defined behavior, as it is entirely implementation-defined and not
     * supported.
     *
     * <p> This can be used by implementations to add extra callbacks, for testing or experimental
     * purposes.
     *
     * @param callbackName Name of the extra callback.
     * @param args Arguments for the calback
     */
    public void extraCallback(String callbackName, Bundle args) {}

    /**
     * Called when {@link CustomTabsSession} has requested a postMessage channel through
     * {@link CustomTabsService#requestPostMessageChannel(
     * CustomTabsSessionToken, android.net.Uri)} and the channel
     * is ready for sending and receiving messages on both ends.
     *
     * @param extras Reserved for future use.
     */
    public void onMessageChannelReady(Bundle extras) {}

    /**
     * Called when a tab controlled by this {@link CustomTabsSession} has sent a postMessage.
     * If postMessage() is called from a single thread, then the messages will be posted in the
     * same order. When received on the client side, it is the client's responsibility to preserve
     * the ordering further.
     *
     * @param message The message sent.
     * @param extras Reserved for future use.
     */
    public void onPostMessage(String message, Bundle extras) {}
}
