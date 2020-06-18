/*
 * This is the source code of Telegram for Android v. 3.x.x
 * It is licensed under GNU GPL v. 2 or later.
 * You should have received a copy of the license in this archive (see LICENSE).
 *
 * Copyright Nikolai Kudashov, 2013-2017.
 */

package com.github.gdev2018.master.ui.Cells;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.gdev2018.master.AndroidUtilities;
import com.github.gdev2018.master.LocaleController;
import com.github.gdev2018.master.R;
import com.github.gdev2018.master.ui.ActionBar.Theme;
import com.github.gdev2018.master.ui.Components.LayoutHelper;
import com.github.gdev2018.master.ui.Components.RadialProgressView;

public class LocationLoadingCell extends FrameLayout {

    private RadialProgressView progressBar;
    private TextView textView;
    private ImageView imageView;

    public LocationLoadingCell(Context context) {
        super(context);

        progressBar = new RadialProgressView(context);
        addView(progressBar, LayoutHelper.createFrame(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.CENTER));

        imageView = new ImageView(context);
        imageView.setImageResource(R.drawable.location_empty);
        imageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_dialogEmptyImage), PorterDuff.Mode.MULTIPLY));
        addView(imageView, LayoutHelper.createFrame(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.CENTER, 0, 0, 0, 24));

        textView = new TextView(context);
        textView.setTextColor(Theme.getColor(Theme.key_dialogEmptyText));
        textView.setGravity(Gravity.CENTER);
        textView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 17);
        textView.setText(LocaleController.getString("NoPlacesFound", R.string.NoPlacesFound));
        addView(textView, LayoutHelper.createFrame(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.CENTER, 0, 34, 0, 0));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec((int) (AndroidUtilities.dp(56) * 2.5f), MeasureSpec.EXACTLY));
    }

    public void setLoading(boolean value) {
        progressBar.setVisibility(value ? VISIBLE : INVISIBLE);
        textView.setVisibility(value ? INVISIBLE : VISIBLE);
        imageView.setVisibility(value ? INVISIBLE : VISIBLE);
    }
}
