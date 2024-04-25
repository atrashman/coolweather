package com.example.coolweather.db;


import com.example.coolweather.activity.ChooseAreaFragment;
import com.google.gson.annotations.SerializedName;

import org.litepal.LitePal;

import java.util.List;
import java.util.stream.Collectors;

public class County extends Bean {
    public static final  String[] COLUMNS = {"id","name","uid","weatherid"};

    @SerializedName("ignore_this_id")
    private int id;
    @SerializedName("id")
    private int uid;



    private String name;
    @SerializedName("weather_id")
    private String weatherId;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    private int provinceId;
    private int cityId;

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

    public String getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

//    @Override
//    public String getUrl() {
//        return Constants.POISTION_CONSTANT+"/"+provinceId+"/"+cityId+"/"+id;
//    }
//


    public static List<Bean> getFromDB(ChooseAreaFragment fragment) {
        List<County> counties = LitePal.select(COLUMNS).where("provinceid = ?",fragment.getProvinceId()+"",
                "cityid = ?",fragment.getCityId()+"" ).find(County.class);
        List<Bean> beans = counties.stream().map(o -> (Bean) o).collect(Collectors.toList());
        return beans;
    }
}


