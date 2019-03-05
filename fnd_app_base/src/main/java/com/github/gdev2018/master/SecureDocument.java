/*
 * This is the source code of Powerlabor (TM) for Android
 * It is licensed under GNU GPL v. 2 or later.
 * You should have received a copy of the license in this archive (see LICENSE).
 *
 * Copyright Livesey Inc. (c) 2007-2019.
 */

package com.github.gdev2018.master;

import com.github.gdev2018.master.tgnet.TLObject;
import com.github.gdev2018.master.tgnet.TLRPC;

public class SecureDocument extends TLObject {

    public SecureDocumentKey secureDocumentKey;
    public TLRPC.TL_secureFile secureFile;
    public String path;
    public TLRPC.TL_inputFile inputFile;
    public byte[] fileSecret;
    public byte[] fileHash;
    public int type;

    public SecureDocument(SecureDocumentKey key, TLRPC.TL_secureFile file, String p, byte[] fh, byte[] secret) {
        secureDocumentKey = key;
        secureFile = file;
        path = p;
        fileHash = fh;
        fileSecret = secret;
    }
}
