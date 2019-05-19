/*  * This is the source code of Telegram for Android v. 5.x.x.  * It is licensed under GNU GPL v. 2 or later.  * You should have received a copy of the license in this archive (see LICENSE).  *  * Copyright Nikolai Kudashov, 2013-2018.  */

package com.github.gdev2018.master.support.util;

interface ThreadUtil<T> {

    interface MainThreadCallback<T> {

        void updateItemCount(int generation, int itemCount);

        void addTile(int generation, TileList.Tile<T> tile);

        void removeTile(int generation, int position);
    }

    interface BackgroundCallback<T> {

        void refresh(int generation);

        void updateRange(int rangeStart, int rangeEnd, int extRangeStart, int extRangeEnd,
                         int scrollHint);

        void loadTile(int position, int scrollHint);

        void recycleTile(TileList.Tile<T> tile);
    }

    MainThreadCallback<T> getMainThreadProxy(MainThreadCallback<T> callback);

    BackgroundCallback<T> getBackgroundProxy(BackgroundCallback<T> callback);
}
