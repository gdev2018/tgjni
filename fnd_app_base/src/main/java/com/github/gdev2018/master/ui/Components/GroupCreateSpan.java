/*
 * This is the source code of Telegram for Android v. 5.x.x.
 * It is licensed under GNU GPL v. 2 or later.
 * You should have received a copy of the license in this archive (see LICENSE).
 *
 * Copyright Nikolai Kudashov, 2013-2018.
 */

package com.github.gdev2018.master.ui.Components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.View;

import com.github.gdev2018.master.AndroidUtilities;
import com.github.gdev2018.master.ContactsController;
import com.github.gdev2018.master.ImageReceiver;
import com.github.gdev2018.master.R;
import com.github.gdev2018.master.UserObject;
import com.github.gdev2018.master.tgnet.TLRPC;
import com.github.gdev2018.master.ui.ActionBar.Theme;

public class GroupCreateSpan extends View {

    private int uid;
    private String key;
    private static TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    private static Paint backPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Drawable deleteDrawable;
    private RectF rect = new RectF();
    private ImageReceiver imageReceiver;
    private StaticLayout nameLayout;
    private AvatarDrawableDeprecated avatarDrawableDeprecated;
    private ContactsController.Contact currentContact;
    private int textWidth;
    private float textX;
    private float progress;
    private boolean deleting;
    private long lastUpdateTime;
    private int[] colors = new int[8];

    public GroupCreateSpan(Context context, TLRPC.User user) {
        this(context, user, null);
    }

    public GroupCreateSpan(Context context, ContactsController.Contact contact) {
        this(context, null, contact);
    }

    public GroupCreateSpan(Context context, TLRPC.User user, ContactsController.Contact contact) {
        super(context);

        currentContact = contact;
        deleteDrawable = getResources().getDrawable(R.drawable.delete);
        textPaint.setTextSize(AndroidUtilities.dp(14));

        avatarDrawableDeprecated = new AvatarDrawableDeprecated();
        avatarDrawableDeprecated.setTextSize(AndroidUtilities.dp(12));
        if (user != null) {
            avatarDrawableDeprecated.setInfo(user);
            uid = user.id;
        } else {
            avatarDrawableDeprecated.setInfo(0, contact.first_name, contact.last_name, false);
            uid = contact.contact_id;
            key = contact.key;
        }

        imageReceiver = new ImageReceiver();
        imageReceiver.setRoundRadius(AndroidUtilities.dp(16));
        imageReceiver.setParentView(this);
        imageReceiver.setImageCoords(0, 0, AndroidUtilities.dp(32), AndroidUtilities.dp(32));

        int maxNameWidth;
        if (AndroidUtilities.isTablet()) {
            maxNameWidth = AndroidUtilities.dp(530 - 32 - 18 - 57 * 2) / 2;
        } else {
            maxNameWidth = (Math.min(AndroidUtilities.displaySize.x, AndroidUtilities.displaySize.y) - AndroidUtilities.dp(32 + 18 + 57 * 2)) / 2;
        }
        String firstName;
        if (user != null) {
            firstName = UserObject.getFirstName(user);
        } else {
            if (!TextUtils.isEmpty(contact.first_name)) {
                firstName = contact.first_name;
            } else {
                firstName = contact.last_name;
            }
        }
        CharSequence name = TextUtils.ellipsize(firstName.replace('\n', ' '), textPaint, maxNameWidth, TextUtils.TruncateAt.END);
        nameLayout = new StaticLayout(name, textPaint, 1000, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        if (nameLayout.getLineCount() > 0) {
            textWidth = (int) Math.ceil(nameLayout.getLineWidth(0));
            textX = -nameLayout.getLineLeft(0);
        }

        TLRPC.FileLocation photo = null;
        if (user != null && user.photo != null) {
            photo = user.photo.photo_small;
        }
///*        imageReceiver.setImage(photo, "50_50", avatarDrawableDeprecated, null, null, 0, null, user, 1);*/
        updateColors();
    }

    public void updateColors() {
        int color = Theme.getColor(Theme.key_avatar_backgroundActionBarBlue);
        int back = Theme.getColor(Theme.key_groupcreate_spanBackground);
        int text = Theme.getColor(Theme.key_groupcreate_spanText);
        int delete = Theme.getColor(Theme.key_groupcreate_spanDelete);
        colors[0] = Color.red(back);
        colors[1] = Color.red(color);
        colors[2] = Color.green(back);
        colors[3] = Color.green(color);
        colors[4] = Color.blue(back);
        colors[5] = Color.blue(color);
        colors[6] = Color.alpha(back);
        colors[7] = Color.alpha(color);
        textPaint.setColor(text);
        deleteDrawable.setColorFilter(new PorterDuffColorFilter(delete, PorterDuff.Mode.MULTIPLY));
        backPaint.setColor(back);
        avatarDrawableDeprecated.setColor(AvatarDrawableDeprecated.getColorForId(5));
    }

    public boolean isDeleting() {
        return deleting;
    }

    public void startDeleteAnimation() {
        if (deleting) {
            return;
        }
        deleting = true;
        lastUpdateTime = System.currentTimeMillis();
        invalidate();
    }

    public void cancelDeleteAnimation() {
        if (!deleting) {
            return;
        }
        deleting = false;
        lastUpdateTime = System.currentTimeMillis();
        invalidate();
    }

    public int getUid() {
        return uid;
    }

    public String getKey() {
        return key;
    }

    public ContactsController.Contact getContact() {
        return currentContact;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(AndroidUtilities.dp(32 + 25) + textWidth, AndroidUtilities.dp(32));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (deleting && progress != 1.0f || !deleting && progress != 0.0f) {
            long newTime = System.currentTimeMillis();
            long dt = newTime - lastUpdateTime;
            if (dt < 0 || dt > 17) {
                dt = 17;
            }
            if (deleting) {
                progress += dt / 120.0f;
                if (progress >= 1.0f) {
                    progress = 1.0f;
                }
            } else {
                progress -= dt / 120.0f;
                if (progress < 0.0f) {
                    progress = 0.0f;
                }
            }
            invalidate();
        }
        canvas.save();
        rect.set(0, 0, getMeasuredWidth(), AndroidUtilities.dp(32));
        backPaint.setColor(Color.argb(colors[6] + (int) ((colors[7] - colors[6]) * progress), colors[0] + (int) ((colors[1] - colors[0]) * progress), colors[2] + (int) ((colors[3] - colors[2]) * progress), colors[4] + (int) ((colors[5] - colors[4]) * progress)));
        canvas.drawRoundRect(rect, AndroidUtilities.dp(16), AndroidUtilities.dp(16), backPaint);
        imageReceiver.draw(canvas);
        if (progress != 0) {
            int color = avatarDrawableDeprecated.getColor();
            float alpha = Color.alpha(color) / 255.0f;
            backPaint.setColor(color);
            backPaint.setAlpha((int) (255 * progress * alpha));
            canvas.drawCircle(AndroidUtilities.dp(16), AndroidUtilities.dp(16), AndroidUtilities.dp(16), backPaint);
            canvas.save();
            canvas.rotate(45 * (1.0f - progress), AndroidUtilities.dp(16), AndroidUtilities.dp(16));
            deleteDrawable.setBounds(AndroidUtilities.dp(11), AndroidUtilities.dp(11), AndroidUtilities.dp(21), AndroidUtilities.dp(21));
            deleteDrawable.setAlpha((int) (255 * progress));
            deleteDrawable.draw(canvas);
            canvas.restore();
        }
        canvas.translate(textX + AndroidUtilities.dp(32 + 9), AndroidUtilities.dp(8));
        nameLayout.draw(canvas);
        canvas.restore();
    }
}
