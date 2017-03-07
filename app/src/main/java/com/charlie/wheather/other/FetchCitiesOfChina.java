package com.charlie.wheather.other;

import com.charlie.wheather.common.Constants;
import com.charlie.wheather.util.LogUtil;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 获取中国的城市数据
 * Created by Charlie on 2017/3/2.
 */
public class FetchCitiesOfChina {

    private int proviceId;
    private int cityId;

    public void work() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GetCitiesInterface.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final GetCitiesInterface getCitiesInterface = retrofit.create(GetCitiesInterface.class);

        getCitiesInterface
                .getProvince()
                .subscribeOn(Schedulers.io())
                .concatMap(new Function<List<DataEntity>, ObservableSource<DataEntity>>() {
                    @Override
                    public ObservableSource<DataEntity> apply(@NonNull List<DataEntity> provinceDatas) throws Exception {
                        LogUtil.d(Constants.LOG_TAG, "provinceDatas.size" + provinceDatas.size());

                        return Observable.fromIterable(provinceDatas);
                    }
                })
                .concatMap(new Function<DataEntity, ObservableSource<List<DataEntity>>>() {
                    @Override
                    public ObservableSource<List<DataEntity>> apply(@NonNull DataEntity provinceData) throws Exception {
                        LogUtil.d(Constants.LOG_TAG, "provinceData.id" + provinceData.id);

                        Province province = new Province();
                        province.setName(provinceData.name);
                        province.save();

                        proviceId = provinceData.id;
                        return getCitiesInterface.getCities(proviceId);
                    }
                })
                .concatMap(new Function<List<DataEntity>, ObservableSource<DataEntity>>() {
                    @Override
                    public ObservableSource<DataEntity> apply(@NonNull List<DataEntity> cityDatas) throws Exception {
                        LogUtil.d(Constants.LOG_TAG, "cityData.size" + cityDatas.size());

                        Province province = DataSupport.find(Province.class, proviceId);
                        List<City> cities = new ArrayList<City>();
                        for (DataEntity cityData : cityDatas) {
                            City city = new City();
                            city.setName(cityData.name);
                            city.save();

                            cities.add(city);
                        }
                        province.setCities(cities);
                        province.save();

                        return Observable.fromIterable(cityDatas);
                    }
                })
                .concatMap(new Function<DataEntity, ObservableSource<List<DataEntity>>>() {
                    @Override
                    public ObservableSource<List<DataEntity>> apply(@NonNull DataEntity cityData) throws Exception {
                        LogUtil.d(Constants.LOG_TAG, "cityData.id" + cityData.id);

                        cityId = cityData.id;
                        return getCitiesInterface.getCounties(proviceId, cityId);
                    }
                })
                .subscribe(
                        new Consumer<List<DataEntity>>() {
                            @Override
                            public void accept(@NonNull List<DataEntity> countyDatas) throws Exception {
                                LogUtil.d(Constants.LOG_TAG, "countyDatas" + countyDatas.size());
                                City city = DataSupport.find(City.class, cityId);
                                List<County> counties = new ArrayList<County>();
                                boolean isFirst = true;
                                for (DataEntity countyData : countyDatas) {
                                    if (!isFirst) {
                                        County county = new County();
                                        county.setName(countyData.name);
                                        county.save();

                                        counties.add(county);

                                    } else {

                                        isFirst = false;
                                    }
                                }

                                city.setCounties(counties);
                                city.save();
                            }
                        },

                        new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                LogUtil.d(Constants.LOG_TAG, throwable.toString());
                            }
                        },

                        new Action() {
                            @Override
                            public void run() throws Exception {
                                LogUtil.d(Constants.LOG_TAG, "complete");
                            }
                        }
                );
    }
}
