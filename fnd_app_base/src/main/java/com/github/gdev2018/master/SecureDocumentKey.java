/*
 * This is the source code of Powerlabor (TM) for Android
 * It is licensed under GNU GPL v. 2 or later.
 * You should have received a copy of the license in this archive (see LICENSE).
 *
 * Copyright Livesey Inc. (c) 2007-2019.
 */

package com.github.gdev2018.master;

public class SecureDocumentKey {

    public byte[] file_key;
    public byte[] file_iv;

    public SecureDocumentKey(byte[] key, byte[] iv) {
        file_key = key;
        file_iv = iv;
    }
}
