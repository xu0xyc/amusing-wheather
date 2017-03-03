package com.charlie.wheather;

import android.app.Application;

import com.charlie.wheather.utils.LogUtil;

import org.litepal.LitePal;

/**
 * 全局Application类
 * Created by charlie on 2017/2/23.
 */
public class CharlieApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //配置log
        LogUtil.configue(getApplicationContext(), LogUtil.PLACE_LOGCAT, android.util.Log.DEBUG);

        //配置LitePal数据库
        LitePal.initialize(this);
    }
}
