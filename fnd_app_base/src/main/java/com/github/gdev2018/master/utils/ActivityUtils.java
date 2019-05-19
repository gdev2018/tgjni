/*  * This is the source code of Telegram for Android v. 5.x.x.  * It is licensed under GNU GPL v. 2 or later.  * You should have received a copy of the license in this archive (see LICENSE).  *  * Copyright Nikolai Kudashov, 2013-2018.  */

package com.github.gdev2018.master.utils;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.support.annotation.NonNull;
import android.view.View;

public class ActivityUtils {

    public static @NonNull Activity getActivity(View view) {
        Context context = view.getContext();

        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }

        throw new IllegalStateException("View " + view + " is not attached to an Activity");
    }


    // time functions

    public String secondsToString(int pTime) {
        final int min = pTime / 60;
        final int sec = pTime % 60;

        final String strMin = placeZeroIfNeeded(min);
        final String strSec = placeZeroIfNeeded(sec);
        return String.format("%s:%s",strMin,strSec);
    }

    private String placeZeroIfNeeded(int number) {
        return (number >=10)? Integer.toString(number):String.format("0%s",Integer.toString(number));
    }

    private String timeDescription(String pDescription,int pTime) {
        return putTimeInXX(pDescription,secondsToString(pTime));
    }

    private String putTimeInXX(String inputDescription,String pTime) {
        return inputDescription.replace("XX",pTime);
    }

}
