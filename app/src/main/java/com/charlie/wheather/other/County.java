package com.charlie.wheather.other;

import org.litepal.crud.DataSupport;

/**
 * 数据库映射pojo————区/县
 * Created by Charlie on 2017/3/1.
 */
public class County extends DataSupport {

    private String name;

    private String pinyin;

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
}
