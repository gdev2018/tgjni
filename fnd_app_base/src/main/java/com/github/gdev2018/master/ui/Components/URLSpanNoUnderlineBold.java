/*  * This is the source code of Telegram for Android v. 5.x.x.  * It is licensed under GNU GPL v. 2 or later.  * You should have received a copy of the license in this archive (see LICENSE).  *  * Copyright Nikolai Kudashov, 2013-2018.  */

package com.github.gdev2018.master.ui.Components;

import android.text.TextPaint;

import com.github.gdev2018.master.AndroidUtilities;

public class URLSpanNoUnderlineBold extends URLSpanNoUnderline {
    public URLSpanNoUnderlineBold(String url) {
        super(url);
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        ds.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        ds.setUnderlineText(false);
    }
}