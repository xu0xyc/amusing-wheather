package com.charlie.wheather.widget.flowlayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.charlie.wheather.R;

import java.util.Set;

/**
 * 流式布局的一种实现
 * Created by Charlie on 2017/2/23.
 */
public class TagFlowLayout extends FlowLayout implements TagAdapter.OnDataChangedObserver {

    private int maxCheckedCount = -1;//最多可以选择的个数，-1表示不限制
    private boolean mCheckedAutoEffect = true;//被选择时，是否使用自动化效果，即根据TagView的drawableState变化而变化

    private TagAdapter mAdapter;
    private MotionEvent mUpEvent;

    public TagFlowLayout(Context context) {
        this(context, null);
    }

    public TagFlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TagFlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TagFlowLayout);
        maxCheckedCount = ta.getInt(R.styleable.TagFlowLayout_maxCheckedCount, -1);
        mCheckedAutoEffect = ta.getBoolean(R.styleable.TagFlowLayout_checkedAutoEffect, true);
        ta.recycle();
        if(mCheckedAutoEffect){
            setClickable(true);
        }
    }

    public TagFlowLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TagFlowLayout);
        maxCheckedCount = ta.getInt(R.styleable.TagFlowLayout_maxCheckedCount, -1);
        mCheckedAutoEffect = ta.getBoolean(R.styleable.TagFlowLayout_checkedAutoEffect, true);
        ta.recycle();
        if(mCheckedAutoEffect){
            setClickable(true);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        for (int i = getChildCount()-1; i>=0; i--) {
            TagLayout child = (TagLayout) getChildAt(i);
            if (View.GONE == child.getTagView().getVisibility()) {
                child.setVisibility(View.GONE);
            }else{
                child.setVisibility(View.VISIBLE);
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 设置Adapter
     * @param adapter
     */
    public void setTagAdapter(TagAdapter adapter){
        mAdapter = adapter;

        if(null != mAdapter){
            mAdapter.registerDataChangedObserver(this);
        }

        refreshTagFlowLayout();
    }

    //刷新View
    private void refreshTagFlowLayout() {
        removeAllViews();

        if(null != mAdapter){
            Set<Integer> checkedSet = mAdapter.getCheckedSet();
            int count = mAdapter.getCount();
            for (int i=0; i<count; i++) {
                TagLayout tl = new TagLayout(getContext());
                View v = mAdapter.getView(this, i, mAdapter.getItem(i));
                if (null != v) {
                    if(mCheckedAutoEffect) v.setDuplicateParentStateEnabled(true);

                    if (v.getLayoutParams() != null) {
                        tl.setLayoutParams(v.getLayoutParams());
                    } else {
                        tl.setLayoutParams(new MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                    }

                    tl.addView(v);
                    if(checkedSet.contains(i)) tl.setChecked(true);
                    this.addView(tl);
                }
            }
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (MotionEvent.ACTION_UP == event.getAction()) {
            mUpEvent = MotionEvent.obtain(event);
        }

        return super.onTouchEvent(event);
    }

    @Override
    public boolean performClick() {
        if(null == mUpEvent) return super.performClick();
        int x = (int) mUpEvent.getX();
        int y = (int) mUpEvent.getY();
        mUpEvent = null;
        if(canFindChild(x, y)){
            return true;
        }else{
            return super.performClick();
        }

    }

    // 找到那个被点击的tag
    private boolean canFindChild(int x, int y) {
        int cCount = getChildCount();
        Set<Integer> checkedSet = mAdapter.getCheckedSet();
        for (int i=0; i<cCount; i++) {
            TagLayout child = (TagLayout) getChildAt(i);
            if (View.GONE == child.getVisibility()) continue;

            Rect rect = new Rect();
            child.getHitRect(rect);

            if(rect.contains(x, y)){
                if(-1 == maxCheckedCount || checkedSet.size()<maxCheckedCount){
                    if(checkedSet.contains(i)){
                        checkedSet.remove(i);
                        if(mCheckedAutoEffect) child.setChecked(false);
                    } else {
                        checkedSet.add(i);
                        if(mCheckedAutoEffect) child.setChecked(true);
                    }
                } else if (child.isChecked()) {
                    checkedSet.remove(i);
                    if(mCheckedAutoEffect) child.setChecked(false);
                }

                if (null != mOnTagCheckedListener) {
                    mOnTagCheckedListener.onTagChecked(this, child.getTagView(), i, checkedSet);
                }
                return true;
            }
        }

        return false;
    }

    @Override
    public void onDataChanged() {
        refreshTagFlowLayout();
    }


    /**
     * 设置某个tag被点击时的监听
     * @param listener
     */
    public void setOnTagCheckedListener(OnTagCheckedListener listener){
        mOnTagCheckedListener = listener;
        setClickable(true);
    }

    private OnTagCheckedListener mOnTagCheckedListener;
    public interface OnTagCheckedListener {
        void onTagChecked(TagFlowLayout parent, View child, int position, Set<Integer> checkedSet);
    }


    /********************** 处理对状态的保存和回复  *********************/

    private static final String KEY_DEFAULT = "key_default";
    private static final String KEY_CHECKED_SET = "key_checked_set";

    @Override
    protected Parcelable onSaveInstanceState() {
        if(null != mAdapter){
            Set<Integer> checkedSet = mAdapter.getCheckedSet();
            if(!checkedSet.isEmpty()){
                Bundle bundle = new Bundle();
                bundle.putParcelable(KEY_DEFAULT, super.onSaveInstanceState());
                int[] saveInts = new int[checkedSet.size()];

                int i = 0;
                for (Integer pos : checkedSet) {
                    saveInts[i++] = pos;
                }

                bundle.putIntArray(KEY_CHECKED_SET, saveInts);

                return bundle;
            }
        }

        return super.onSaveInstanceState();
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if(state instanceof Bundle){
            Bundle bundle = (Bundle)state;
            super.onRestoreInstanceState(bundle.getParcelable(KEY_DEFAULT));
            if (mAdapter != null) {
                int[] saveInts = bundle.getIntArray(KEY_CHECKED_SET);
                for(int i=0; i<saveInts.length; i++) {
                    mAdapter.getCheckedSet().add(saveInts[i]);
                }

                refreshTagFlowLayout();
            }
        }else{
            super.onRestoreInstanceState(state);
        }
    }
}
