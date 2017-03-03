package com.charlie.wheather.pojo;

import java.util.List;

/**
 * 天气接口请求的天气信息
 * Created by Charlie on 2017/2/26.
 */
public class WeatherEntity {

    public List<HeWeather5Bean> HeWeather5;

    public String city;

    public WeatherEntity(String city) {
        this.city = city;
    }
}
