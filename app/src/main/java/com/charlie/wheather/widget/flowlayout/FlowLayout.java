package com.charlie.wheather.widget.flowlayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.charlie.wheather.R;
import com.charlie.wheather.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 流式布局
 * Created by xyc on 2017/2/23.
 */
public class FlowLayout extends ViewGroup {

    private static final String TAG = FlowLayout.class.getSimpleName();
    private static final int LEFT = -1;
    private static final int CENTER = 0;
    private static final int RIGHT = 1;

    protected List<List<View>> mAllViews = new ArrayList<>();
    protected List<Integer> mLineHeight = new ArrayList<>();
    protected List<Integer> mLineWidth = new ArrayList<>();

    private List<View> mlineViews = new ArrayList<>();
    private int mGravity;

    public FlowLayout(Context context) {
        this(context, null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.FlowLayout);
        mGravity = ta.getInt(R.styleable.FlowLayout_fGravity, LEFT);
        ta.recycle();
    }

    @SuppressLint("NewApi")
    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.FlowLayout);
        mGravity = ta.getInt(R.styleable.FlowLayout_fGravity, LEFT);
        ta.recycle();
    }

    /*
     * 1、确定自己的大小
     * 2、确定子View的大小
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        LogUtil.d(TAG, "onMeasure:"+widthMeasureSpec+"-"+heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        // wrap_content
        int width = 0;
        int height = 0;

        int lineWidth = 0;
        int lineHeight = 0;

        int cCount = getChildCount();

        for (int i = 0; i < cCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == View.GONE) {
                if (i == cCount - 1) {
                    width = Math.max(lineWidth, width);
                    height += lineHeight;
                }
                continue;
            }
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();

            int childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            int childHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;

            if (lineWidth + childWidth > widthSize - getPaddingLeft() - getPaddingRight()) {
                width = Math.max(width, lineWidth);
                lineWidth = childWidth;
                height += lineHeight;
                lineHeight = childHeight;
            } else {
                lineWidth += childWidth;
                lineHeight = Math.max(lineHeight, childHeight);
            }
            if (i == cCount - 1) {
                width = Math.max(lineWidth, width);
                height += lineHeight;
            }
        }
        setMeasuredDimension(
                widthMode == MeasureSpec.EXACTLY ? widthSize : width + getPaddingLeft() + getPaddingRight(),
                heightMode == MeasureSpec.EXACTLY ? heightSize : height + getPaddingTop() + getPaddingBottom()
        );
    }

    /*
     * 1、确定自己的大小
     * 2、确定子View的大小
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        LogUtil.d(TAG, "onLayout:"+changed);
        mAllViews.clear();
        mlineViews.clear();
        mLineHeight.clear();
        mLineWidth.clear();

        int width = getWidth();

        int lineWidth = 0;
        int lineHeight = 0;

        int cCount = getChildCount();

        for (int i = 0; i < cCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == View.GONE) continue;
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();

            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();

            if (childWidth + lineWidth + lp.leftMargin + lp.rightMargin > width - getPaddingLeft() - getPaddingRight()) {
                mLineWidth.add(lineWidth);
                mLineHeight.add(lineHeight);
                mAllViews.add(mlineViews);

                lineWidth = 0;
                lineHeight = childHeight + lp.topMargin + lp.bottomMargin;
                mlineViews = new ArrayList<>();
            }
            lineWidth += childWidth + lp.leftMargin + lp.rightMargin;
            lineHeight = Math.max(lineHeight, childHeight + lp.topMargin + lp.bottomMargin);
            mlineViews.add(child);
        }
        mLineWidth.add(lineWidth);
        mLineHeight.add(lineHeight);
        mAllViews.add(mlineViews);


        int layLeft = getPaddingLeft();
        int layTop = getPaddingTop();

        int lineCount = mAllViews.size();

        for (int i = 0; i < lineCount; i++) {
            mlineViews = mAllViews.get(i);
            lineHeight = mLineHeight.get(i);

            // set gravity
            int currentLineWidth = this.mLineWidth.get(i);
            switch (this.mGravity) {
                case LEFT:
                    layLeft = getPaddingLeft();
                    break;
                case CENTER:
                    layLeft = (width - currentLineWidth)/2+getPaddingLeft();
                    break;
                case RIGHT:
                    layLeft = width - currentLineWidth + getPaddingLeft();
                    break;
            }

            for (int j = 0; j < mlineViews.size(); j++) {
                View child = mlineViews.get(j);
                if (child.getVisibility() == View.GONE) {
                    continue;
                }

                MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();

                int cl = layLeft + lp.leftMargin;
                int ct = layTop + lp.topMargin;
                int cr = cl + child.getMeasuredWidth();
                int cb = ct + child.getMeasuredHeight();

                child.layout(cl, ct, cr, cb);

                layLeft += child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            }

            layTop += lineHeight;
        }
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }
}
