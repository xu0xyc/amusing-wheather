package com.charlie.wheather.widget.flowlayout;

import android.content.Context;
import android.view.View;
import android.widget.Checkable;
import android.widget.FrameLayout;

/**
 * 流式布局中的Tag子View
 * Created by charlie on 2017/2/23.
 */
public class TagLayout extends FrameLayout implements Checkable {

    private boolean isChecked;
    private static final int[] STATUS_CHECKED = {android.R.attr.state_checked};

    public TagLayout(Context context) {
        super(context);
    }

    View getTagView(){
        return getChildAt(0);
    }

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        int[] drawableSates = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked) {
            mergeDrawableStates(drawableSates, STATUS_CHECKED);
        }

        return drawableSates;
    }

    @Override
    public void setChecked(boolean checked) {
        if(isChecked != checked){
            isChecked = checked;
            refreshDrawableState();
        }
    }

    @Override
    public boolean isChecked() {
        return isChecked;
    }

    @Override
    public void toggle() {
        setChecked(!isChecked);
    }
}
