/*
 * This is the source code of Powerlabor (TM) for Android
 * It is licensed under GNU GPL v. 2 or later.
 * You should have received a copy of the license in this archive (see LICENSE).
 *
 * Copyright Livesey Inc. (c) 2007-2018.
 */

package com.github.gdev2018.master.SQLite;

import android.database.Cursor;

import com.github.gdev2018.master.AndroidUtilities;

import java.util.Date;


public class PlaborDBCursor implements PlaborDBRow {

    private Cursor cursor;

    public PlaborDBCursor(Cursor cursor) {
        this.cursor = cursor;
    }

    @Override
    public int getInteger(String name) {
        return cursor.getInt(cursor.getColumnIndexOrThrow(name));
    }

    @Override
    public Long getLong(String name) {
        return cursor.getLong(cursor.getColumnIndexOrThrow(name));
    }

    @Override
    public String getString(String name) {
        return cursor.getString(cursor.getColumnIndexOrThrow(name));
    }

    @Override
    public boolean getBoolean(String name) {
        return cursor.getInt(cursor.getColumnIndexOrThrow(name)) != 0;
    }

        @Override
    public Date getDate(String name) {
        String s_Date;
        s_Date =  cursor.getString(cursor.getColumnIndexOrThrow(name));

        Date dt_Date = AndroidUtilities.stringToDate(s_Date, "");    // if s_Date = null, then returns 1900-01-01, not the current time
        return dt_Date;
    }




    public boolean next() {
        return cursor.moveToNext();
    }

    public void close() {
        if (cursor != null) {
            cursor.close();
        }
    }

    public int getCount() {
        return cursor.getCount();
    }
}