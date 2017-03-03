package com.charlie.wheather.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/3/2.
 */
public class BasicBean {
    @SerializedName("city")
    public String cityX;
    public String cnty;
    public String id;
    public String lat;
    public String lon;
    public String prov;
    public UpdateBean update;
}
