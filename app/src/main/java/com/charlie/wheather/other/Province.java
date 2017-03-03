package com.charlie.wheather.other;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * 数据库映射pojo————省
 * Created by Charlie on 2017/3/1.
 */
public class Province extends DataSupport{

    @Column(unique = true, defaultValue = "unknown", nullable = false, ignore = false)
    private String name;

    private String pinyin;

    private List<City> cities;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public List<City> getCities() {
        return cities;
    }

    public void setCities(List<City> cities) {
        this.cities = cities;
    }

}
