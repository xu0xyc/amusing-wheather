package com.charlie.wheather.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.charlie.wheather.fragment.CityWeatherFragment;
import com.charlie.wheather.pojo.WeatherEntity;

import java.util.List;

/**
 * 每个城市的天气详情
 * Created by Charlie on 2017/2/26.
 */
public class CityWeatherPagerAdapter extends FragmentPagerAdapter {

    private List<WeatherEntity> mWeatherEntities;

    public CityWeatherPagerAdapter(FragmentManager fm, List<WeatherEntity> infos) {
        super(fm);
        mWeatherEntities = infos;
    }

    @Override
    public Fragment getItem(int position) {
        return new CityWeatherFragment();
    }

    @Override
    public int getCount() {
        return mWeatherEntities == null ? 0 : mWeatherEntities.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return mWeatherEntities.get(position).city;
    }
}
