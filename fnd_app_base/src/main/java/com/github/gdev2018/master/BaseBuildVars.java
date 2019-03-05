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

    public static boolean DEBUG_VERSION = true;
    public static boolean DEBUG_PRIVATE_VERSION = false;
    public static boolean LOGS_ENABLED = true;

    public static boolean CHECK_UPDATES = false;
    public static int BUILD_VERSION = 1358;
    public static String BUILD_VERSION_STRING = "2.0.1";
    public static int APP_ID = 0; //obtain your own APP_ID at https://core.telegram.org/api/obtaining_api_id
    public static String APP_HASH = ""; //obtain your own APP_HASH at https://core.telegram.org/api/obtaining_api_id
    public static String HOCKEY_APP_HASH = "your-hockeyapp-api-key-here";
    public static String HOCKEY_APP_HASH_DEBUG = "your-hockeyapp-api-key-here";
    public static String PLAYSTORE_APP_URL = "";

    static {
        if (BaseApplication.mApplicationContext != null) {
            SharedPreferences sharedPreferences = BaseApplication.mApplicationContext.getSharedPreferences("systemConfig", Context.MODE_PRIVATE);
            LOGS_ENABLED = sharedPreferences.getBoolean("logsEnabled", DEBUG_VERSION);
        }
    }
}
