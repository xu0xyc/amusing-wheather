package com.charlie.wheather.widget.flowlayout;

import android.view.View;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Charlie on 2017/2/23.
 */
public abstract class TagAdapter<T> {

    private List<T> mTags;
    private Set<Integer> mCheckedSet = new HashSet<>();

    public TagAdapter(T... t) {

        this(Arrays.asList(t));
    }

    public TagAdapter(List<T> list) {
        mTags = list;
    }

    public int getCount() {
        return null == mTags ? 0 : mTags.size();
    }

    public T getItem(int position) {
        return mTags.get(position);
    }

    // 由子类实现
    public abstract View getView(TagFlowLayout parent, int position, T t);


    Set<Integer> getCheckedSet(){
        return mCheckedSet;
    }

    /**
     * 设置被选中的tag集合
     * @param positions
     */
    public void setCheckedSet(int... positions){
        if(null == positions) return;
        Set<Integer> set = new HashSet<>();
        for (int position : positions) {
            set.add(position);
        }
        setCheckedSet(set);
    }

    /**
     * 设置被选中的tag集合
     * @param set
     */
    public void setCheckedSet(Set<Integer> set){
        mCheckedSet.clear();
        if (null != set) {
            mCheckedSet.addAll(set);
        }
        notifyDataChanged();
    }

    /**
     * 通知TagFlowLayout,数据改变
     */
    public void notifyDataChanged() {
        mCheckedSet.clear();
        if (null != mDataChangedObserver) {
            mDataChangedObserver.onDataChanged();
        }
    }


    void registerDataChangedObserver(OnDataChangedObserver observer) {
        mDataChangedObserver = observer;
    }

    private OnDataChangedObserver mDataChangedObserver;

    interface OnDataChangedObserver {
        void onDataChanged();
    }
}
