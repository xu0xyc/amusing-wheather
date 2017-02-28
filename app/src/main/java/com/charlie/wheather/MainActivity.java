package com.charlie.wheather;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.charlie.wheather.adapter.CityWeatherPagerAdapter;
import com.charlie.wheather.common.Constants;
import com.charlie.wheather.pojo.WeatherInfo;
import com.charlie.wheather.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DrawerLayout.DrawerListener, AppBarLayout.OnOffsetChangedListener {

    private Toolbar toolbar;
    private DrawerLayout drawer_layout;
    private AppBarLayout appbar_layout;
    private TabLayout tab_layout;
    private ViewPager view_pager;

    private ActionBarDrawerToggle mToggle;
    private FragmentPagerAdapter mAdapter;

    private boolean isDrawerOpened;
    private int mAppBarVerticalOffset;//appbar滑动的距离 全展开为0，全折叠为getTotalScrollRange

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        appbar_layout = (AppBarLayout) findViewById(R.id.appbar_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
        tab_layout = (TabLayout) findViewById(R.id.tab_layout);
        view_pager = (ViewPager) findViewById(R.id.view_pager);

        List<WeatherInfo> weatherInfos = new ArrayList<>();
        weatherInfos.add(new WeatherInfo("北京"));
        weatherInfos.add(new WeatherInfo("上海"));
        weatherInfos.add(new WeatherInfo("深圳"));
        weatherInfos.add(new WeatherInfo("广州"));
        weatherInfos.add(new WeatherInfo("郑州"));
        weatherInfos.add(new WeatherInfo("杭州"));
        weatherInfos.add(new WeatherInfo("乌鲁木齐"));
        weatherInfos.add(new WeatherInfo("吐鲁番"));

        mAdapter = new CityWeatherPagerAdapter(getSupportFragmentManager(), weatherInfos);
        view_pager.setAdapter(mAdapter);
        tab_layout.setupWithViewPager(view_pager);

        setSupportActionBar(toolbar);
        mToggle = new ActionBarDrawerToggle(this, drawer_layout, R.string.open_drawer, R.string.close_drawer);
        drawer_layout.addDrawerListener(mToggle);
        drawer_layout.addDrawerListener(this);
        appbar_layout.addOnOffsetChangedListener(this);

    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_setting:
//                Toast.makeText(MainActivity.this, "click settings!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                overridePendingTransition(R.anim.act_in_from_right, R.anim.act_out_from_right);
                return true;
        }
        return mToggle.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onDestroy() {
        drawer_layout.removeDrawerListener(mToggle);
        drawer_layout.removeDrawerListener(this);
        appbar_layout.removeOnOffsetChangedListener(this);

        super.onDestroy();
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {

    }

    @Override
    public void onDrawerOpened(View drawerView) {
        isDrawerOpened = true;
        toolbar.setTitle(R.string.drawer_title);
    }

    @Override
    public void onDrawerClosed(View drawerView) {
        isDrawerOpened = false;
        toolbar.setTitle(R.string.app_name);
    }

    @Override
    public void onDrawerStateChanged(int newState) {
        appbar_layout.setExpanded(false, true);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        LogUtil.d(Constants.LOG_TAG, "verticalOffset of appbar :"+verticalOffset);
        mAppBarVerticalOffset = -verticalOffset;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        LogUtil.d(Constants.LOG_TAG, "Touch event action of MainActivity :"+ev.getAction());

        if(!isDrawerOpened && MotionEvent.ACTION_UP == ev.getAction()){
            int totalScrollRange = appbar_layout.getTotalScrollRange();
            boolean needExpanded = mAppBarVerticalOffset>totalScrollRange/2 ? false : true;
            appbar_layout.setExpanded(needExpanded, true);
        }
        return super.dispatchTouchEvent(ev);
    }
}
