package com.charlie.wheather.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.charlie.wheather.MainActivity;
import com.charlie.wheather.R;
import com.charlie.wheather.adapter.CityRecyclerAdapter;
import com.charlie.wheather.pojo.City;
import com.charlie.wheather.util.LogUtil;
import com.charlie.wheather.widget.DividerItemDecoration;
import com.charlie.wheather.widget.flowlayout.TagAdapter;
import com.charlie.wheather.widget.flowlayout.TagFlowLayout;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Add city Fragment.
 * Created by Charlie on 2017/2/19.
 */
public class AddCityFragment extends Fragment implements TextWatcher, TextView.OnEditorActionListener, CityRecyclerAdapter.OnItemClickLitener {
    private static final String TAG = AddCityFragment.class.getSimpleName();

    private TextInputEditText input_text;
    private TagFlowLayout tag_flow;
    private TextView tv_null;
    private RecyclerView recycle_view;
    private LinearLayout ll_hot;

    private TagAdapter mTagAdapter;
    private LayoutInflater mInflater;
    private CityRecyclerAdapter mRecycleAdapter;

    private List<City> mCities;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LogUtil.d(TAG, "onCreateView");

        mInflater = inflater;

        View view = mInflater.inflate(R.layout.fragment_add_city, container, false);
        input_text = (TextInputEditText) view.findViewById(R.id.input_text);
        tag_flow = (TagFlowLayout) view.findViewById(R.id.tag_flow);
        tv_null = (TextView) view.findViewById(R.id.tv_null);
        recycle_view = (RecyclerView) view.findViewById(R.id.recycle_view);
        ll_hot = (LinearLayout) view.findViewById(R.id.ll_hot);

        mTagAdapter = new MyTagAdapter(getResources().getStringArray(R.array.hot_city));
        tag_flow.setTagAdapter(mTagAdapter);

        input_text.setOnEditorActionListener(this);
        input_text.addTextChangedListener(this);

        return view;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        LogUtil.d(TAG, "beforeTextChanged."+s+" "+start+" "+count+" "+after);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        LogUtil.d(TAG, "onTextChanged."+s+" "+start+" "+before+" "+count);
        if (!TextUtils.isEmpty(s)) {
            if(null != mCities) mCities.clear();

            mCities = DataSupport.where("city_en like ?", s+"%").find(City.class);
            mCities.addAll(DataSupport.where("city_zh like ?", s+"%").find(City.class));

            LogUtil.d(TAG, "mCities:"+mCities.size());
            if(!mCities.isEmpty()){
                tv_null.setVisibility(View.GONE);
                recycle_view.setVisibility(View.VISIBLE);
                ll_hot.setVisibility(View.GONE);

                refreshRecycleView();
            }else{
                tv_null.setVisibility(View.VISIBLE);
                recycle_view.setVisibility(View.GONE);
                ll_hot.setVisibility(View.GONE);
            }
        } else {
            tv_null.setVisibility(View.GONE);
            recycle_view.setVisibility(View.GONE);
            ll_hot.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        LogUtil.d(TAG, "afterTextChanged."+s);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        LogUtil.d(TAG, "onEditorAction, search action."+actionId);
        if(EditorInfo.IME_ACTION_SEARCH == actionId){

        }

        return false;
    }

    private void refreshRecycleView() {
        if (null == mRecycleAdapter) {
            mRecycleAdapter = new CityRecyclerAdapter(getActivity().getApplicationContext(), mCities);
            mRecycleAdapter.setOnItemClickLitener(this);

            recycle_view.setLayoutManager(new LinearLayoutManager(getActivity()));
            recycle_view.addItemDecoration(new DividerItemDecoration(getActivity().getApplicationContext(), DividerItemDecoration.VERTICAL_LIST));
            recycle_view.setAdapter(mRecycleAdapter);

//            recycle_view.setItemAnimator(new DefaultItemAnimator());
        }else{
            mRecycleAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        input_text.clearComposingText();
        Editable editableText = input_text.getEditableText();
        editableText.clear();

        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(input_text.getWindowToken(), 0);
        }

        ((MainActivity)getActivity()).closeDrawer(mCities.get(position));
    }

    @Override
    public void onItemLongClick(View view, int position) {

    }

    private class MyTagAdapter extends TagAdapter<String> {

        public MyTagAdapter(String... tags){
            super(tags);
        }

        @Override
        public View getView(TagFlowLayout parent, int position, String s) {
            View view = mInflater.inflate(R.layout.layout_tag, parent, false);
            TextView tv_city = (TextView) view.findViewById(R.id.tv_city);
            tv_city.setText(getItem(position));

            return view;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        input_text.removeTextChangedListener(this);
    }
}