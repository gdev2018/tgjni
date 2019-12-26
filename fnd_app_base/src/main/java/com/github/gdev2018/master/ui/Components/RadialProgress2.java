/*
 * This is the source code of Telegram for Android v. 2.0.x.
 * It is licensed under GNU GPL v. 2 or later.
 * You should have received a copy of the license in this archive (see LICENSE).
 *
 * Copyright Nikolai Kudashov, 2013-2018.
 */

package com.github.gdev2018.master.ui.Components;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

import com.github.gdev2018.master.AndroidUtilities;
import com.github.gdev2018.master.ImageReceiver;
import com.github.gdev2018.master.tgnet.TLObject;
import com.github.gdev2018.master.ui.ActionBar.Theme;

import java.util.Locale;

public class RadialProgress2 {

    private RectF progressRect = new RectF();
    private View parent;

    private boolean previousCheckDrawable;

    private boolean drawMiniIcon;
    private int progressColor = 0xffffffff;
    private Paint miniProgressBackgroundPaint;

    private Paint overlayPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint circleMiniPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private MediaActionDrawable mediaActionDrawable;
    private MediaActionDrawable miniMediaActionDrawable;
    private int circleColor;
    private int circlePressedColor;
    private int iconColor;
    private int iconPressedColor;
    private String circleColorKey;
    private String circlePressedColorKey;
    private String iconColorKey;
    private String iconPressedColorKey;
    private ImageReceiver overlayImageView;
    private int circleRadius;
    private boolean isPressed;
    private boolean isPressedMini;

    private boolean drawBackground = true;

    private Bitmap miniDrawBitmap;
    private Canvas miniDrawCanvas;

    private float overrideAlpha = 1.0f;

    public RadialProgress2(View parentView) {
        miniProgressBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        parent = parentView;

        overlayImageView = new ImageReceiver(parentView);
        overlayImageView.setInvalidateAll(true);

        mediaActionDrawable = new MediaActionDrawable();
        mediaActionDrawable.setDelegate(parentView::invalidate);

        miniMediaActionDrawable = new MediaActionDrawable();
        miniMediaActionDrawable.setDelegate(parentView::invalidate);
        miniMediaActionDrawable.setMini(true);
        miniMediaActionDrawable.setIcon(MediaActionDrawable.ICON_NONE, false);

        circleRadius = AndroidUtilities.dp(22);
        overlayImageView.setRoundRadius(circleRadius);

        overlayPaint.setColor(0x64000000);
    }

    public void setCircleRadius(int value) {
        circleRadius = value;
        overlayImageView.setRoundRadius(circleRadius);
    }

    public void setImageOverlay(TLObject image, Object parentObject) {
///*        overlayImageView.setImage(image, image != null ? String.format(Locale.US, "%d_%d", circleRadius * 2, circleRadius * 2) : null, null, null, parentObject, 1);*/
    }

    public void setImageOverlay(String url) {
        overlayImageView.setImage(url, url != null ? String.format(Locale.US, "%d_%d", circleRadius * 2, circleRadius * 2) : null, null, null, -1);
    }

    public void onAttachedToWindow() {
        overlayImageView.onAttachedToWindow();
    }

    public void onDetachedFromWindow() {
        overlayImageView.onDetachedFromWindow();
    }

    public void setColors(int circle, int circlePressed, int icon, int iconPressed) {
        circleColor = circle;
        circlePressedColor = circlePressed;
        iconColor = icon;
        iconPressedColor = iconPressed;
        circleColorKey = null;
        circlePressedColorKey = null;
        iconColorKey = null;
        iconPressedColorKey = null;
    }

    public void setColors(String circle, String circlePressed, String icon, String iconPressed) {
        circleColorKey = circle;
        circlePressedColorKey = circlePressed;
        iconColorKey = icon;
        iconPressedColorKey = iconPressed;
    }

    public void setDrawBackground(boolean value) {
        drawBackground = value;
    }

    public void setProgressRect(int left, int top, int right, int bottom) {
        progressRect.set(left, top, right, bottom);
    }

    public RectF getProgressRect() {
        return progressRect;
    }

    public void setProgressColor(int color) {
        progressColor = color;
    }

    public void setMiniProgressBackgroundColor(int color) {
        miniProgressBackgroundPaint.setColor(color);
    }

    public void setProgress(float value, boolean animated) {
        if (drawMiniIcon) {
            miniMediaActionDrawable.setProgress(value, animated);
        } else {
            mediaActionDrawable.setProgress(value, animated);
        }
    }

    private void invalidateParent() {
        int offset = AndroidUtilities.dp(2);
        parent.invalidate((int) progressRect.left - offset, (int) progressRect.top - offset, (int) progressRect.right + offset * 2, (int) progressRect.bottom + offset * 2);
    }

    public int getIcon() {
        return mediaActionDrawable.getCurrentIcon();
    }

    public int getMiniIcon() {
        return miniMediaActionDrawable.getCurrentIcon();
    }

    public void setIcon(int icon, boolean ifSame, boolean animated) {
        if (ifSame && icon == mediaActionDrawable.getCurrentIcon()) {
            return;
        }
        mediaActionDrawable.setIcon(icon, animated);
        if (!animated) {
            parent.invalidate();
        } else {
            invalidateParent();
        }
    }

    public void setMiniIcon(int icon, boolean ifSame, boolean animated) {
        if (icon != MediaActionDrawable.ICON_DOWNLOAD && icon != MediaActionDrawable.ICON_CANCEL && icon != MediaActionDrawable.ICON_NONE) {
            return;
        }
        if (ifSame && icon == miniMediaActionDrawable.getCurrentIcon()) {
            return;
        }
        miniMediaActionDrawable.setIcon(icon, animated);
        drawMiniIcon = icon != MediaActionDrawable.ICON_NONE || miniMediaActionDrawable.getTransitionProgress() < 1.0f;
        if (drawMiniIcon) {
            initMiniIcons();
        }
        if (!animated) {
            parent.invalidate();
        } else {
            invalidateParent();
        }
    }

    public void initMiniIcons() {
        if (miniDrawBitmap == null) {
            try {
                miniDrawBitmap = Bitmap.createBitmap(AndroidUtilities.dp(48), AndroidUtilities.dp(48), Bitmap.Config.ARGB_8888);
                miniDrawCanvas = new Canvas(miniDrawBitmap);
            } catch (Throwable ignore) {

            }
        }
    }

    public boolean swapIcon(int icon) {
        if (mediaActionDrawable.setIcon(icon, false)) {
            return true;
        }
        return false;
    }

    public void setPressed(boolean value, boolean mini) {
        if (mini) {
            isPressedMini = value;
        } else {
            isPressed = value;
        }
        invalidateParent();
    }

    public void setOverrideAlpha(float alpha) {
        overrideAlpha = alpha;
    }

    public void draw(Canvas canvas) {
        if (mediaActionDrawable.getCurrentIcon() == MediaActionDrawable.ICON_NONE && mediaActionDrawable.getTransitionProgress() >= 1.0f) {
            return;
        }

        int currentIcon = mediaActionDrawable.getCurrentIcon();
        int prevIcon = mediaActionDrawable.getPreviousIcon();

        float wholeAlpha;
        if ((currentIcon == MediaActionDrawable.ICON_CHECK || currentIcon == MediaActionDrawable.ICON_EMPTY) && prevIcon == MediaActionDrawable.ICON_NONE) {
            wholeAlpha = mediaActionDrawable.getTransitionProgress();
        } else {
            wholeAlpha = currentIcon != MediaActionDrawable.ICON_NONE ? 1.0f : 1.0f - mediaActionDrawable.getTransitionProgress();
        }

        if (isPressedMini) {
            if (iconPressedColorKey != null) {
                miniMediaActionDrawable.setColor(Theme.getColor(iconPressedColorKey));
            } else {
                miniMediaActionDrawable.setColor(iconPressedColor);
            }
            if (circlePressedColorKey != null) {
                circleMiniPaint.setColor(Theme.getColor(circlePressedColorKey));
            } else {
                circleMiniPaint.setColor(circlePressedColor);
            }
        } else {
            if (iconColorKey != null) {
                miniMediaActionDrawable.setColor(Theme.getColor(iconColorKey));
            } else {
                miniMediaActionDrawable.setColor(iconColor);
            }
            if (circleColorKey != null) {
                circleMiniPaint.setColor(Theme.getColor(circleColorKey));
            } else {
                circleMiniPaint.setColor(circleColor);
            }
        }

        int color;
        if (isPressed) {
            if (iconPressedColorKey != null) {
                mediaActionDrawable.setColor(color = Theme.getColor(iconPressedColorKey));
            } else {
                mediaActionDrawable.setColor(color = iconPressedColor);
            }
            if (circlePressedColorKey != null) {
                circlePaint.setColor(Theme.getColor(circlePressedColorKey));
            } else {
                circlePaint.setColor(circlePressedColor);
            }
        } else {
            if (iconColorKey != null) {
                mediaActionDrawable.setColor(color = Theme.getColor(iconColorKey));
            } else {
                mediaActionDrawable.setColor(color = iconColor);
            }
            if (circleColorKey != null) {
                circlePaint.setColor(Theme.getColor(circleColorKey));
            } else {
                circlePaint.setColor(circleColor);
            }
        }
        if (drawMiniIcon && miniDrawCanvas != null) {
            miniDrawBitmap.eraseColor(0);
        }

        int originalAlpha = circlePaint.getAlpha();
        circlePaint.setAlpha((int) (originalAlpha * wholeAlpha * overrideAlpha));
        originalAlpha = circleMiniPaint.getAlpha();
        circleMiniPaint.setAlpha((int) (originalAlpha * wholeAlpha * overrideAlpha));

        boolean drawCircle = true;
        int centerX;
        int centerY;
        if (drawMiniIcon && miniDrawCanvas != null) {
            centerX = (int) (progressRect.width() / 2);
            centerY = (int) (progressRect.height() / 2);
        } else {
            centerX = (int) progressRect.centerX();
            centerY = (int) progressRect.centerY();
        }

        if (overlayImageView.hasBitmapImage()) {
            float alpha = overlayImageView.getCurrentAlpha();
            overlayPaint.setAlpha((int) (0x64 * alpha * wholeAlpha * overrideAlpha));
            int c;
            if (alpha >= 1.0f) {
                drawCircle = false;
                c = 0xffffffff;
            } else {
                int r = Color.red(color);
                int g = Color.green(color);
                int b = Color.blue(color);
                int a = Color.alpha(color);

                int rD = (int) ((0xff - r) * alpha);
                int gD = (int) ((0xff - g) * alpha);
                int bD = (int) ((0xff - b) * alpha);
                int aD = (int) ((0xff - a) * alpha);
                c = Color.argb(a + aD, r + rD, g + gD, b + bD);
            }
            mediaActionDrawable.setColor(c);

            overlayImageView.setImageCoords(centerX - circleRadius, centerY - circleRadius, circleRadius * 2, circleRadius * 2);
        }

        if (drawCircle && drawBackground) {
            if (drawMiniIcon && miniDrawCanvas != null) {
                miniDrawCanvas.drawCircle(centerX, centerY, circleRadius, circlePaint);
            } else {
                if (currentIcon != MediaActionDrawable.ICON_NONE || wholeAlpha != 0) {
                    canvas.drawCircle(centerX, centerY, circleRadius * wholeAlpha, circlePaint);
                }
            }
        }
        if (overlayImageView.hasBitmapImage()) {
            overlayImageView.setAlpha(wholeAlpha * overrideAlpha);

            if (drawMiniIcon && miniDrawCanvas != null) {
                overlayImageView.draw(miniDrawCanvas);
                miniDrawCanvas.drawCircle(centerX, centerY, circleRadius, overlayPaint);
            } else {
                overlayImageView.draw(canvas);
                canvas.drawCircle(centerX, centerY, circleRadius, overlayPaint);
            }
        }
        mediaActionDrawable.setBounds(centerX - circleRadius, centerY - circleRadius, centerX + circleRadius, centerY + circleRadius);
        if (drawMiniIcon) {
            if (miniDrawCanvas != null) {
                mediaActionDrawable.draw(miniDrawCanvas);
            } else {
                mediaActionDrawable.draw(canvas);
            }
        } else {
            mediaActionDrawable.setOverrideAlpha(overrideAlpha);
            mediaActionDrawable.draw(canvas);
        }

        if (drawMiniIcon) {
            int offset;
            int size;
            float cx;
            float cy;
            if (Math.abs(progressRect.width() - AndroidUtilities.dp(44)) < AndroidUtilities.density) {
                offset = 0;
                size = 20;
                cx = progressRect.centerX() + AndroidUtilities.dp(16 + offset);
                cy = progressRect.centerY() + AndroidUtilities.dp(16 + offset);
            } else {
                offset = 2;
                size = 22;
                cx = progressRect.centerX() + AndroidUtilities.dp(18);
                cy = progressRect.centerY() + AndroidUtilities.dp(18);
            }
            int halfSize = size / 2;

            float alpha = miniMediaActionDrawable.getCurrentIcon() != MediaActionDrawable.ICON_NONE ? 1.0f : 1.0f - miniMediaActionDrawable.getTransitionProgress();
            if (alpha == 0.0f) {
                drawMiniIcon = false;
            }

            if (miniDrawCanvas != null) {
                miniDrawCanvas.drawCircle(AndroidUtilities.dp(18 + size + offset), AndroidUtilities.dp(18 + size + offset), AndroidUtilities.dp(halfSize + 1) * alpha, Theme.checkboxSquare_eraserPaint);
            } else {
                miniProgressBackgroundPaint.setColor(progressColor);
                canvas.drawCircle(cx, cy, AndroidUtilities.dp(12), miniProgressBackgroundPaint);
            }

            if (miniDrawCanvas != null) {
                canvas.drawBitmap(miniDrawBitmap, (int) progressRect.left, (int) progressRect.top, null);
            }

            canvas.drawCircle(cx, cy, AndroidUtilities.dp(halfSize) * alpha, circleMiniPaint);
            miniMediaActionDrawable.setBounds((int) (cx - AndroidUtilities.dp(halfSize) * alpha), (int) (cy - AndroidUtilities.dp(halfSize) * alpha), (int) (cx + AndroidUtilities.dp(halfSize) * alpha), (int) (cy + AndroidUtilities.dp(halfSize) * alpha));
            miniMediaActionDrawable.draw(canvas);
        }
    }
}
