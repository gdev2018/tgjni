/*
 * This is the source code of Telegram for Android v. 3.x.x.
 * It is licensed under GNU GPL v. 2 or later.
 * You should have received a copy of the license in this archive (see LICENSE).
 *
 * Copyright Nikolai Kudashov, 2013-2017.
 */

package com.github.gdev2018.master.ui.Cells;

import android.content.Context;
import android.graphics.Canvas;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.github.gdev2018.master.AndroidUtilities;
import com.github.gdev2018.master.ContactsController;
import com.github.gdev2018.master.LocaleController;
import com.github.gdev2018.master.tgnet.TLRPC;
import com.github.gdev2018.master.ui.ActionBar.Theme;
import com.github.gdev2018.master.ui.Components.AvatarDrawableDeprecated;
import com.github.gdev2018.master.ui.Components.BackupImageViewDeprecated;
import com.github.gdev2018.master.ui.Components.CheckBoxSquare;
import com.github.gdev2018.master.ui.Components.LayoutHelper;

public class CheckBoxUserCell extends FrameLayout {

    private TextView textView;
    private BackupImageViewDeprecated imageView;
    private CheckBoxSquare checkBox;
    private AvatarDrawableDeprecated avatarDrawableDeprecated;
    private boolean needDivider;

    private TLRPC.User currentUser;

    public CheckBoxUserCell(Context context, boolean alert) {
        super(context);

        textView = new TextView(context);
        textView.setTextColor(Theme.getColor(alert ? Theme.key_dialogTextBlack : Theme.key_windowBackgroundWhiteBlackText));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        textView.setLines(1);
        textView.setMaxLines(1);
        textView.setSingleLine(true);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        textView.setGravity((LocaleController.isRTL ? Gravity.RIGHT : Gravity.LEFT) | Gravity.CENTER_VERTICAL);
        addView(textView, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT, (LocaleController.isRTL ? Gravity.RIGHT : Gravity.LEFT) | Gravity.TOP, (LocaleController.isRTL ? 21 : 94), 0, (LocaleController.isRTL ? 94 : 21), 0));

        avatarDrawableDeprecated = new AvatarDrawableDeprecated();
        imageView = new BackupImageViewDeprecated(context);
        imageView.setRoundRadius(AndroidUtilities.dp(36));
        addView(imageView, LayoutHelper.createFrame(36, 36, (LocaleController.isRTL ? Gravity.RIGHT : Gravity.LEFT) | Gravity.TOP, 48, 7, 48, 0));

        checkBox = new CheckBoxSquare(context, alert);
        addView(checkBox, LayoutHelper.createFrame(18, 18, (LocaleController.isRTL ? Gravity.RIGHT : Gravity.LEFT) | Gravity.TOP, (LocaleController.isRTL ? 0 : 21), 16, (LocaleController.isRTL ? 21 : 0), 0));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(50) + (needDivider ? 1 : 0), MeasureSpec.EXACTLY));
    }

    public void setTextColor(int color) {
        textView.setTextColor(color);
    }

    public TLRPC.User getCurrentUser() {
        return currentUser;
    }

    public void setUser(TLRPC.User user, boolean checked, boolean divider) {
        currentUser = user;
        textView.setText(ContactsController.formatName(user.first_name, user.last_name));
        checkBox.setChecked(checked, false);
        TLRPC.FileLocation photo = null;
        avatarDrawableDeprecated.setInfo(user);
        if (user != null && user.photo != null) {
            photo = user.photo.photo_small;
        }
        imageView.setImage(photo, "50_50", avatarDrawableDeprecated, user);
        needDivider = divider;
        setWillNotDraw(!divider);
    }

    public void setChecked(boolean checked, boolean animated) {
        checkBox.setChecked(checked, animated);
    }

    public boolean isChecked() {
        return checkBox.isChecked();
    }

    public TextView getTextView() {
        return textView;
    }

    public CheckBoxSquare getCheckBox() {
        return checkBox;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (needDivider) {
            canvas.drawLine(LocaleController.isRTL ? 0 : AndroidUtilities.dp(20), getMeasuredHeight() - 1, getMeasuredWidth() - (LocaleController.isRTL ? AndroidUtilities.dp(20) : 0), getMeasuredHeight() - 1, Theme.dividerPaint);
        }
    }
}
