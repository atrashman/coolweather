package com.example.coolweather.db;

import com.example.coolweather.activity.ChooseAreaFragment;
import com.google.gson.annotations.SerializedName;

import org.litepal.LitePal;

import java.util.List;
import java.util.stream.Collectors;

public class City extends Bean {
    public static final  String[] COLUMNS = {"id","name","uid"};

    @SerializedName("ignore_this_id")
    private int id;

    @SerializedName("id")
    private int uid;

    private String name;


    private int cityCode;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    private int provinceId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCityCode() {
        return cityCode;
    }

    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

//    @Override
//    public String getUrl() {
//        return Constants.POISTION_CONSTANT+"/"+provinceId+"/"+id;
//    }

    public static List<Bean> getFromDB(ChooseAreaFragment fragment) {
        List<City> cities = LitePal.select(COLUMNS).where("provinceid = ?",fragment.getProvinceId()+"").find(City.class);
        List<Bean> beans = cities.stream().map(o -> (Bean) o).collect(Collectors.toList());
        return beans;
    }
}


