/*
 * This is the source code of Powerlabor (TM) for Android
 * It is licensed under GNU GPL v. 2 or later.
 * You should have received a copy of the license in this archive (see LICENSE).
 *
 * Copyright Livesey Inc. (c) 2007-2019.
 */

package com.github.gdev2018.master.ui.activity;

import java.util.Date;

/**
 * Created by RET on 20.03.2018.
 */

public interface BaseViewItem {


    // --- getters ---
    String getItemString(String resName);

    Boolean getItemBoolean(String resName);

    // --- setters ---
    void setItemString(String resName, String value);

    void setItemBoolean(String resName, Boolean value);

    void setItemDateFormat(String resName, Date value, String format);

    void setItemSecondsFormat(String resName, Long seconds, String format);

}
