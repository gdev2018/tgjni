/*  * This is the source code of Telegram for Android v. 5.x.x.  * It is licensed under GNU GPL v. 2 or later.  * You should have received a copy of the license in this archive (see LICENSE).  *  * Copyright Nikolai Kudashov, 2013-2018.  */

package com.github.gdev2018.master.SQLite;

import java.util.Date;

public interface PlaborDBRow {
    int getInteger(String name);
    Long getLong(String name);
    String getString(String name);
    boolean getBoolean(String name);
    Date getDate(String name);
}
