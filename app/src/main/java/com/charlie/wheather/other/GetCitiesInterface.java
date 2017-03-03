package com.charlie.wheather.other;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * 获取省市区的接口类
 * Created by Charlie on 2017/3/1.
 */
public interface GetCitiesInterface {

    String BASE_URL = "http://guolin.tech/api/";

    @GET("china")
    Observable<List<DataEntity>> getProvince();

    @GET("china/{province}")
    Observable<List<DataEntity>> getCities(@Path("province") int provinceId);

    @GET("china/{province}/{cityId}")
    Observable<List<DataEntity>> getCounties(@Path("province") int provinceId, @Path("cityId") int cityId);
}
