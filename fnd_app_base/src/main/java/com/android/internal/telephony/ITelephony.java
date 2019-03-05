/*
 * This is the source code of Powerlabor (TM) for Android
 * It is licensed under GNU GPL v. 2 or later.
 * You should have received a copy of the license in this archive (see LICENSE).
 *
 * Copyright Livesey Inc. (c) 2007-2019.
 */

package com.android.internal.telephony;

public interface ITelephony {
    boolean endCall();

    void answerRingingCall();

    void silenceRinger();
}
