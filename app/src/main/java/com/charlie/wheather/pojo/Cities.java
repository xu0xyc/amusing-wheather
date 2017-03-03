package com.charlie.wheather.pojo;

import org.litepal.crud.DataSupport;

/**
 * 根据china-city-list.json生成china.db需要的litepal的类
 * Created by Chalie on 2017/3/3.
 */
public class Cities extends DataSupport{

    private String weather_id;

    private String city_en;

    private String city_zh;

    private String leader_zh;

    private String province_zh;

    public String getWeather_id() {
        return weather_id;
    }

    public void setWeather_id(String weather_id) {
        this.weather_id = weather_id;
    }

    public String getCity_en() {
        return city_en;
    }

    public void setCity_en(String city_en) {
        this.city_en = city_en;
    }

    public String getCity_zh() {
        return city_zh;
    }

    public void setCity_zh(String city_zh) {
        this.city_zh = city_zh;
    }

    public String getLeader_zh() {
        return leader_zh;
    }

    public void setLeader_zh(String leader_zh) {
        this.leader_zh = leader_zh;
    }

    public String getProvince_zh() {
        return province_zh;
    }

    public void setProvince_zh(String province_zh) {
        this.province_zh = province_zh;
    }
}
