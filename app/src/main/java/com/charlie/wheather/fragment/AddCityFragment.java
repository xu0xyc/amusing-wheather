package com.charlie.wheather.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.charlie.wheather.R;
import com.charlie.wheather.utils.LogUtil;
import com.charlie.wheather.widget.flowlayout.TagAdapter;
import com.charlie.wheather.widget.flowlayout.TagFlowLayout;

/**
 * Add city Fragment.
 * Created by Charlie on 2017/2/19.
 */
public class AddCityFragment extends Fragment{

    private TextInputEditText input_text;
    private TagFlowLayout tag_flow;
    private TagAdapter mAdapter;
    private LayoutInflater mInflater;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mInflater = inflater;

        View view = mInflater.inflate(R.layout.fragment_add_city, container, false);
        input_text = (TextInputEditText) view.findViewById(R.id.input_text);
        tag_flow = (TagFlowLayout) view.findViewById(R.id.tag_flow);
        String[] hotCitys = getResources().getStringArray(R.array.hot_city);
        LogUtil.d("xyc", "length:"+hotCitys.length);

        mAdapter = new MyTagAdapter(hotCitys);
        tag_flow.setTagAdapter(mAdapter);
        return view;
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

}