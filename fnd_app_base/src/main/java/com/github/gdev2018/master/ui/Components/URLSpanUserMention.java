/*
 * This is the source code of Powerlabor (TM) for Android
 * It is licensed under GNU GPL v. 2 or later.
 * You should have received a copy of the license in this archive (see LICENSE).
 *
 * Copyright Livesey Inc. (c) 2007-2018.
 */

package com.github.gdev2018.master.ui.Components;

import android.text.TextPaint;
import android.view.View;

import com.github.gdev2018.master.ui.ActionBar.Theme;

public class URLSpanUserMention extends URLSpanNoUnderline {

    private int currentType;

    public URLSpanUserMention(String url, int type) {
        super(url);
        currentType = type;
    }

    @Override
    public void onClick(View widget) {
        super.onClick(widget);
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        if (currentType == 2) {
            ds.setColor(0xffffffff);
        } else if (currentType == 1) {
            ds.setColor(Theme.getColor(Theme.key_chat_messageLinkOut));
        } else {
            ds.setColor(Theme.getColor(Theme.key_chat_messageLinkIn));
        }

        ds.setUnderlineText(false);
    }
}
