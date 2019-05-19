/*  * This is the source code of Telegram for Android v. 5.x.x.  * It is licensed under GNU GPL v. 2 or later.  * You should have received a copy of the license in this archive (see LICENSE).  *  * Copyright Nikolai Kudashov, 2013-2018.  */

package com.github.gdev2018.master.ui.Components;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;

import com.github.gdev2018.master.AndroidUtilities;
import com.github.gdev2018.master.LocaleController;
import com.github.gdev2018.master.support.widget.RecyclerView;
import com.github.gdev2018.master.ui.ActionBar.Theme;

public class GroupCreateDividerItemDecoration extends RecyclerView.ItemDecoration {

    private boolean searching;
    private boolean single;

    public void setSearching(boolean value) {
        searching = value;
    }

    public void setSingle(boolean value) {
        single = value;
    }

    @Override
    public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        int width = parent.getWidth();
        int top;
        int childCount = parent.getChildCount() - (single ? 0 : 1);
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(child);
            top = child.getBottom();
            canvas.drawLine(LocaleController.isRTL ? 0 : AndroidUtilities.dp(72), top, width - (LocaleController.isRTL ? AndroidUtilities.dp(72) : 0), top, Theme.dividerPaint);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        /*int position = parent.getChildAdapterPosition(view);
        if (position == 0 || !searching && position == 1) {
            return;
        }*/
        outRect.top = 1;
    }
}
