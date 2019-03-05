/*
 * This is the source code of Powerlabor (TM) for Android
 * It is licensed under GNU GPL v. 2 or later.
 * You should have received a copy of the license in this archive (see LICENSE).
 *
 * Copyright Livesey Inc. (c) 2007-2018.
 */

package com.github.gdev2018.master.support.widget.helper;

import android.graphics.Canvas;
import android.support.v4.view.ViewCompat;
import android.view.View;

import com.github.gdev2018.master.support.widget.RecyclerView;

/**
 * Package private class to keep implementations. Putting them inside ItemTouchUIUtil makes them
 * public API, which is not desired in this case.
 */
class ItemTouchUIUtilImpl {
    static class Api21Impl extends BaseImpl {
        @Override
        public void onDraw(Canvas c, RecyclerView recyclerView, View view,
                float dX, float dY, int actionState, boolean isCurrentlyActive) {
            if (isCurrentlyActive) {
                Object originalElevation = view.getTag();
                if (originalElevation == null) {
                    originalElevation = ViewCompat.getElevation(view);
                    float newElevation = 1f + findMaxElevation(recyclerView, view);
                    ViewCompat.setElevation(view, newElevation);
                    view.setTag(originalElevation);
                }
            }
            super.onDraw(c, recyclerView, view, dX, dY, actionState, isCurrentlyActive);
        }

        private float findMaxElevation(RecyclerView recyclerView, View itemView) {
            final int childCount = recyclerView.getChildCount();
            float max = 0;
            for (int i = 0; i < childCount; i++) {
                final View child = recyclerView.getChildAt(i);
                if (child == itemView) {
                    continue;
                }
                final float elevation = ViewCompat.getElevation(child);
                if (elevation > max) {
                    max = elevation;
                }
            }
            return max;
        }

        @Override
        public void clearView(View view) {
            final Object tag = view.getTag();
            if (tag != null && tag instanceof Float) {
                ViewCompat.setElevation(view, (Float) tag);
            }
            view.setTag(null);
            super.clearView(view);
        }
    }

    static class BaseImpl implements ItemTouchUIUtil {

        @Override
        public void clearView(View view) {
            view.setTranslationX(0f);
            view.setTranslationY(0f);
        }

        @Override
        public void onSelected(View view) {

        }

        @Override
        public void onDraw(Canvas c, RecyclerView recyclerView, View view,
                float dX, float dY, int actionState, boolean isCurrentlyActive) {
            view.setTranslationX(dX);
            view.setTranslationY(dY);
        }

        @Override
        public void onDrawOver(Canvas c, RecyclerView recyclerView,
                View view, float dX, float dY, int actionState, boolean isCurrentlyActive) {

        }
    }
}
