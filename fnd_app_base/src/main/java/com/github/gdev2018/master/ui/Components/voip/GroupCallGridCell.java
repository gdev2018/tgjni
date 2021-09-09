package com.github.gdev2018.master.ui.Components.voip;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.github.gdev2018.master.BaseAccountInstance;
import com.github.gdev2018.master.AndroidUtilities;
import com.github.gdev2018.master.ChatObject;
///*import com.github.gdev2018.master.ui.GroupCallActivity;*/
///*import com.github.gdev2018.master.ui.GroupCallTabletGridAdapter;*/

public class GroupCallGridCell extends FrameLayout {

    public final static int CELL_HEIGHT = 165;
    public int spanCount;
    public int position;
///*    public GroupCallTabletGridAdapter gridAdapter;*/

///*    GroupCallMiniTextureView renderer;*/

    ChatObject.VideoParticipant participant;
    public boolean attached;
    private final boolean isTabletGrid;

    public GroupCallGridCell(@NonNull Context context, boolean isTabletGrid) {
        super(context);
        this.isTabletGrid = isTabletGrid;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (isTabletGrid) {
            float totalSpans = 6;
            float w = ((View) getParent()).getMeasuredWidth() / totalSpans * spanCount;
///*            super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(gridAdapter.getItemHeight(position), MeasureSpec.EXACTLY));*/
        } else {
///*            float spanCount = GroupCallActivity.isLandscapeMode ? 3f : 2f;*/
            float parentWidth;
            float h;
            if (getParent() != null) {
                parentWidth = ((View) getParent()).getMeasuredWidth();
            } else {
                parentWidth = MeasureSpec.getSize(widthMeasureSpec);
            }
///*            if (GroupCallActivity.isTabletMode) {
//                h = parentWidth / 2f;
//            } else {
//                h = parentWidth / spanCount;
//            }*/

///*
//            super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec((int) (h + AndroidUtilities.dp(4)), MeasureSpec.EXACTLY));
//*/
        }
    }

    public void setData(BaseAccountInstance accountInstance, ChatObject.VideoParticipant participant, ChatObject.Call call, int selfPeerId) {
        this.participant = participant;
    }

    public ChatObject.VideoParticipant getParticipant() {
        return participant;
    }

///*    public void setRenderer(GroupCallMiniTextureView renderer) {
//        this.renderer = renderer;
//    }
//
//    public GroupCallMiniTextureView getRenderer() {
//        return renderer;
//    }*/

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        attached = true;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        attached = false;
    }

    public float getItemHeight() {
///*        if (gridAdapter != null) {
//            return gridAdapter.getItemHeight(position);
//        } else {*/
            return getMeasuredHeight();
///*        }*/
    }
}
