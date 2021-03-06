/*
 * This is the source code of Telegram for Android v. 3.x.x
 * It is licensed under GNU GPL v. 2 or later.
 * You should have received a copy of the license in this archive (see LICENSE).
 *
 * Copyright Nikolai Kudashov, 2013-2017.
 */

package com.github.gdev2018.master.ui.Cells;

import android.content.Context;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.gdev2018.master.AndroidUtilities;
import com.github.gdev2018.master.Emoji;
import com.github.gdev2018.master.EmojiSuggestion;
import com.github.gdev2018.master.UserObject;
import com.github.gdev2018.master.tgnet.TLRPC;
import com.github.gdev2018.master.ui.ActionBar.Theme;
import com.github.gdev2018.master.ui.Components.AvatarDrawableDeprecated;
import com.github.gdev2018.master.ui.Components.BackupImageViewDeprecated;
import com.github.gdev2018.master.ui.Components.LayoutHelper;

public class MentionCell extends LinearLayout {

    private BackupImageViewDeprecated imageView;
    private TextView nameTextView;
    private TextView usernameTextView;
    private AvatarDrawableDeprecated avatarDrawableDeprecated;

    public MentionCell(Context context) {
        super(context);

        setOrientation(HORIZONTAL);

        avatarDrawableDeprecated = new AvatarDrawableDeprecated();
        avatarDrawableDeprecated.setTextSize(AndroidUtilities.dp(12));

        imageView = new BackupImageViewDeprecated(context);
        imageView.setRoundRadius(AndroidUtilities.dp(14));
        addView(imageView, LayoutHelper.createLinear(28, 28, 12, 4, 0, 0));

        nameTextView = new TextView(context);
        nameTextView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText));
        nameTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
        nameTextView.setSingleLine(true);
        nameTextView.setGravity(Gravity.LEFT);
        nameTextView.setEllipsize(TextUtils.TruncateAt.END);
        addView(nameTextView, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.CENTER_VERTICAL, 12, 0, 0, 0));

        usernameTextView = new TextView(context);
        usernameTextView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText3));
        usernameTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
        usernameTextView.setSingleLine(true);
        usernameTextView.setGravity(Gravity.LEFT);
        usernameTextView.setEllipsize(TextUtils.TruncateAt.END);
        addView(usernameTextView, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.CENTER_VERTICAL, 12, 0, 8, 0));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(36), MeasureSpec.EXACTLY));
    }

    public void setUser(TLRPC.User user) {
        if (user == null) {
            nameTextView.setText("");
            usernameTextView.setText("");
            imageView.setImageDrawable(null);
            return;
        }
        avatarDrawableDeprecated.setInfo(user);
        if (user.photo != null && user.photo.photo_small != null) {
            imageView.setImage(user.photo.photo_small, "50_50", avatarDrawableDeprecated, user);
        } else {
            imageView.setImageDrawable(avatarDrawableDeprecated);
        }
        nameTextView.setText(UserObject.getUserName(user));
        if (user.username != null) {
            usernameTextView.setText("@" + user.username);
        } else {
            usernameTextView.setText("");
        }
        imageView.setVisibility(VISIBLE);
        usernameTextView.setVisibility(VISIBLE);
    }

    public void setText(String text) {
        imageView.setVisibility(INVISIBLE);
        usernameTextView.setVisibility(INVISIBLE);
        nameTextView.setText(text);
    }

    @Override
    public void invalidate() {
        super.invalidate();
        nameTextView.invalidate();
    }

    public void setEmojiSuggestion(EmojiSuggestion suggestion) {
        imageView.setVisibility(INVISIBLE);
        usernameTextView.setVisibility(INVISIBLE);
        StringBuilder stringBuilder = new StringBuilder(suggestion.emoji.length() + suggestion.label.length() + 3);
        stringBuilder.append(suggestion.emoji);
        stringBuilder.append("   ");
        stringBuilder.append(suggestion.label);
        nameTextView.setText(Emoji.replaceEmoji(stringBuilder, nameTextView.getPaint().getFontMetricsInt(), AndroidUtilities.dp(20), false));
    }

    public void setBotCommand(String command, String help, TLRPC.User user) {
        if (user != null) {
            imageView.setVisibility(VISIBLE);
            avatarDrawableDeprecated.setInfo(user);
            if (user.photo != null && user.photo.photo_small != null) {
                imageView.setImage(user.photo.photo_small, "50_50", avatarDrawableDeprecated, user);
            } else {
                imageView.setImageDrawable(avatarDrawableDeprecated);
            }
        } else {
            imageView.setVisibility(INVISIBLE);
        }
        usernameTextView.setVisibility(VISIBLE);
        nameTextView.setText(command);
        usernameTextView.setText(Emoji.replaceEmoji(help, usernameTextView.getPaint().getFontMetricsInt(), AndroidUtilities.dp(20), false));
    }

    public void setIsDarkTheme(boolean isDarkTheme) {
        if (isDarkTheme) {
            nameTextView.setTextColor(0xffffffff);
            usernameTextView.setTextColor(0xffbbbbbb);
        } else {
            nameTextView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText));
            usernameTextView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText3));
        }
    }
}

