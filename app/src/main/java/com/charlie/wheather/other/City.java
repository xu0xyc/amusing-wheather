package com.charlie.wheather.other;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * 数据库映射pojo————市
 * Created by Charlie on 2017/3/1.
 */
public class City extends DataSupport {

    private String name;

    private String pinyin;

    private List<County> counties;

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

    public List<County> getCounties() {
        return counties;
    }

    public void setCounties(List<County> counties) {
        this.counties = counties;
    }

}
