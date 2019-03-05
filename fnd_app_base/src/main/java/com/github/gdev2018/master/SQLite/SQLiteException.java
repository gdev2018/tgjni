/*
 * This is the source code of Powerlabor (TM) for Android
 * It is licensed under GNU GPL v. 2 or later.
 * You should have received a copy of the license in this archive (see LICENSE).
 *
 * Copyright Livesey Inc. (c) 2007-2018.
 */

package com.github.gdev2018.master.SQLite;

public class SQLiteException extends Exception {

	private static final long serialVersionUID = -2398298479089615621L;
	public final int errorCode;

	public SQLiteException(int errcode, String msg) {
		super(msg);
		errorCode = errcode;
	}

	public SQLiteException(String msg) {
		this(0, msg);
	}

	public SQLiteException() {
		errorCode = 0;
	}
}
