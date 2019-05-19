/*  * This is the source code of Telegram for Android v. 5.x.x.  * It is licensed under GNU GPL v. 2 or later.  * You should have received a copy of the license in this archive (see LICENSE).  *  * Copyright Nikolai Kudashov, 2013-2018.  */
package com.github.gdev2018.master.support.util;

/**
 * An interface that can receive Update operations that are applied to a list.
 * <p>
 * This class can be used together with DiffUtil to detect changes between two lists.
 */
public interface ListUpdateCallback {
    /**
     * Called when {@code count} number of items are inserted at the given position.
     *
     * @param position The position of the new item.
     * @param count    The number of items that have been added.
     */
    void onInserted(int position, int count);

    /**
     * Called when {@code count} number of items are removed from the given position.
     *
     * @param position The position of the item which has been removed.
     * @param count    The number of items which have been removed.
     */
    void onRemoved(int position, int count);

    /**
     * Called when an item changes its position in the list.
     *
     * @param fromPosition The previous position of the item before the move.
     * @param toPosition   The new position of the item.
     */
    void onMoved(int fromPosition, int toPosition);

    /**
     * Called when {@code count} number of items are updated at the given position.
     *
     * @param position The position of the item which has been updated.
     * @param count    The number of items which has changed.
     */
    void onChanged(int position, int count, Object payload);
}
