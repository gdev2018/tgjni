package com.github.gdev2018.master.utils;

import android.text.TextUtils;

/**
 * Created by Livesey Inc. on 10.11.2017.
 */

public class Validator {
    public Validator() {
    }

    public boolean validUsername(String username) {
        return !TextUtils.isEmpty(username);
    }
}
