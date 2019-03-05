/*
 * This is the source code of Powerlabor (TM) for Android
 * It is licensed under GNU GPL v. 2 or later.
 * You should have received a copy of the license in this archive (see LICENSE).
 *
 * Copyright Livesey Inc. (c) 2007-2018.
 */

package com.github.gdev2018.master.ui.Components;

import android.net.Uri;
import android.text.style.URLSpan;
import android.view.View;

import com.github.gdev2018.master.browser.Browser;

public class URLSpanReplacement extends URLSpan {

    public URLSpanReplacement(String url) {
        super(url);
    }

    @Override
    public void onClick(View widget) {
        Uri uri = Uri.parse(getURL());
        Browser.openUrl(widget.getContext(), uri);
    }
}
