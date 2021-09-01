package com.github.gdev2018.master.ui.Components;

import android.text.TextPaint;
import android.text.style.CharacterStyle;
import android.text.style.UpdateAppearance;

import androidx.annotation.NonNull;

import com.github.gdev2018.master.ui.ActionBar.Theme;

public class ForegroundColorSpanThemable extends CharacterStyle implements UpdateAppearance {

    private int color;
    private String colorKey;

    public ForegroundColorSpanThemable(String colorKey) {
        this.colorKey = colorKey;
    }

    @Override
    public void updateDrawState(@NonNull TextPaint textPaint) {
        color = Theme.getColor(colorKey);
        if (textPaint.getColor() != color) {
            textPaint.setColor(color);
        }
    }
}
