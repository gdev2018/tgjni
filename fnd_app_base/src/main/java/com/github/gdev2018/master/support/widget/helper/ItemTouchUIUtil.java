/*
 * This is the source code of Powerlabor (TM) for Android
 * It is licensed under GNU GPL v. 2 or later.
 * You should have received a copy of the license in this archive (see LICENSE).
 *
 * Copyright Livesey Inc. (c) 2007-2018.
 */

package com.github.gdev2018.master.support.widget.helper;

import android.graphics.Canvas;
import android.view.View;

import com.github.gdev2018.master.support.widget.RecyclerView;

/**
 * Utility class for {@link ItemTouchHelper} which handles item transformations for different
 * API versions.
 * <p/>
 * This class has methods that map to {@link ItemTouchHelper.Callback}'s drawing methods. Default
 * implementations in {@link ItemTouchHelper.Callback} call these methods with
 * {@link RecyclerView.ViewHolder#itemView} and {@link ItemTouchUIUtil} makes necessary changes
 * on the View depending on the API level. You can access the instance of {@link ItemTouchUIUtil}
 * via {@link ItemTouchHelper.Callback#getDefaultUIUtil()} and call its methods with the children
 * of ViewHolder that you want to apply default effects.
 *
 * @see ItemTouchHelper.Callback#getDefaultUIUtil()
 */
public interface ItemTouchUIUtil {

    /**
     * The default implementation for {@link ItemTouchHelper.Callback#onChildDraw(Canvas,
     * RecyclerView, RecyclerView.ViewHolder, float, float, int, boolean)}
     */
    void onDraw(Canvas c, RecyclerView recyclerView, View view,
                float dX, float dY, int actionState, boolean isCurrentlyActive);

    /**
     * The default implementation for {@link ItemTouchHelper.Callback#onChildDrawOver(Canvas,
     * RecyclerView, RecyclerView.ViewHolder, float, float, int, boolean)}
     */
    void onDrawOver(Canvas c, RecyclerView recyclerView, View view,
                    float dX, float dY, int actionState, boolean isCurrentlyActive);

    /**
     * The default implementation for {@link ItemTouchHelper.Callback#clearView(RecyclerView,
     * RecyclerView.ViewHolder)}
     */
    void clearView(View view);

    /**
     * The default implementation for {@link ItemTouchHelper.Callback#onSelectedChanged(
     * RecyclerView.ViewHolder, int)}
     */
    void onSelected(View view);
}

