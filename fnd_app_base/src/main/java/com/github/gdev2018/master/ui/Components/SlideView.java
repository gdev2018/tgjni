/*
 * This is the source code of Powerlabor (TM) for Android
 * It is licensed under GNU GPL v. 2 or later.
 * You should have received a copy of the license in this archive (see LICENSE).
 *
 * Copyright Livesey Inc. (c) 2007-2018.
 */

package com.github.gdev2018.master.ui.Components;

import android.content.Context;
import android.os.Bundle;
import android.widget.LinearLayout;

public class SlideView extends LinearLayout {

    public SlideView(Context context) {
        super(context);
    }

    public String getHeaderName() {
        return "";
    }

    public void setParams(Bundle params, boolean restore) {

    }

    public void onBackPressed() {

    }

    public void onShow() {

    }

    public void onDestroyActivity() {

    }

    public void onNextPressed() {

    }

    public void onCancelPressed() {

    }

    public void saveStateParams(Bundle bundle) {

    }

    public void restoreStateParams(Bundle bundle) {

    }

    public boolean needBackButton() {
        return false;
    }
}
