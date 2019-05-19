/*  * This is the source code of Telegram for Android v. 5.x.x.  * It is licensed under GNU GPL v. 2 or later.  * You should have received a copy of the license in this archive (see LICENSE).  *  * Copyright Nikolai Kudashov, 2013-2018.  */

package com.github.gdev2018.master.support.util;

import android.util.SparseArray;

import java.lang.reflect.Array;

/**
 * A sparse collection of tiles sorted for efficient access.
 */
class TileList<T> {

    final int mTileSize;

    // Keyed by start position.
    private final SparseArray<Tile<T>> mTiles = new SparseArray<Tile<T>>(10);

    Tile<T> mLastAccessedTile;

    public TileList(int tileSize) {
        mTileSize = tileSize;
    }

    public T getItemAt(int pos) {
        if (mLastAccessedTile == null || !mLastAccessedTile.containsPosition(pos)) {
            final int startPosition = pos - (pos % mTileSize);
            final int index = mTiles.indexOfKey(startPosition);
            if (index < 0) {
                return null;
            }
            mLastAccessedTile = mTiles.valueAt(index);
        }
        return mLastAccessedTile.getByPosition(pos);
    }

    public int size() {
        return mTiles.size();
    }

    public void clear() {
        mTiles.clear();
    }

    public Tile<T> getAtIndex(int index) {
        return mTiles.valueAt(index);
    }

    public Tile<T> addOrReplace(Tile<T> newTile) {
        final int index = mTiles.indexOfKey(newTile.mStartPosition);
        if (index < 0) {
            mTiles.put(newTile.mStartPosition, newTile);
            return null;
        }
        Tile<T> oldTile = mTiles.valueAt(index);
        mTiles.setValueAt(index, newTile);
        if (mLastAccessedTile == oldTile) {
            mLastAccessedTile = newTile;
        }
        return oldTile;
    }

    public Tile<T> removeAtPos(int startPosition) {
        Tile<T> tile = mTiles.get(startPosition);
        if (mLastAccessedTile == tile) {
            mLastAccessedTile = null;
        }
        mTiles.delete(startPosition);
        return tile;
    }

    public static class Tile<T> {
        public final T[] mItems;
        public int mStartPosition;
        public int mItemCount;
        Tile<T> mNext;  // Used only for pooling recycled tiles.

        public Tile(Class<T> klass, int size) {
            //noinspection unchecked
            mItems = (T[]) Array.newInstance(klass, size);
        }

        boolean containsPosition(int pos) {
            return mStartPosition <= pos && pos < mStartPosition + mItemCount;
        }

        T getByPosition(int pos) {
            return mItems[pos - mStartPosition];
        }
    }
}
