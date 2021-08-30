/*
 * This is the source code of Powerlabor (TM) for Android
 * It is licensed under GNU GPL v. 2 or later.
 * You should have received a copy of the license in this archive (see LICENSE).
 *
 * Copyright Livesey Inc. (c) 2007-2019.
 */

package com.github.gdev2018.master;

import android.content.Context;
import android.content.SharedPreferences;

import com.github.gdev2018.master.di.BaseApplication;

public class BaseBuildVars {
    public static boolean DEBUG_VERSION = false;
    public static boolean DEBUG_PRIVATE_VERSION = false;
    public static boolean LOGS_ENABLED = false;
    public static boolean USE_CLOUD_STRINGS = true;
    public static boolean CHECK_UPDATES = true;
    public static boolean TON_WALLET_STANDALONE = false;
    public static int BUILD_VERSION = 0;
    public static String BUILD_VERSION_STRING = "0.0.0";
    public static int APP_ID = 0;
    public static String APP_HASH = "";
    public static String APPCENTER_HASH = "";
    public static String APPCENTER_HASH_DEBUG = "";
    //
    public static String SMS_HASH = DEBUG_VERSION ? "" : "";
    public static String PLAYSTORE_APP_URL = "";

//    static {
//        if (BaseApplication.mApplicationContext != null) {
//            SharedPreferences sharedPreferences = BaseApplication.mApplicationContext.getSharedPreferences("systemConfig", Context.MODE_PRIVATE);
//            LOGS_ENABLED = sharedPreferences.getBoolean("logsEnabled", DEBUG_VERSION);
//        }
//    }
}
