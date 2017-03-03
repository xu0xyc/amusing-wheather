package com.charlie.wheather.pojo;

import java.util.List;

/**
 * Created by Administrator on 2017/3/2.
 */
public class HeWeather5Bean {
    public AqiBean aqi;
    public BasicBean basic;
    public NowBean now;
    public String status;
    public SuggestionBean suggestion;
    public List<AlarmsBean> alarms;
    public List<DailyForecastBean> daily_forecast;
    public List<HourlyForecastBean> hourly_forecast;
}
