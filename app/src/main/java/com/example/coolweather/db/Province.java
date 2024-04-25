package com.example.coolweather.db;


import com.example.coolweather.activity.ChooseAreaFragment;
import com.google.gson.annotations.SerializedName;

import org.litepal.LitePal;


import java.util.List;
import java.util.stream.Collectors;

public class Province extends Bean {
    public static final  String[] COLUMNS = {"id","name","uid"};
    @SerializedName("ignore_this_id")
    private int id;
    @SerializedName("id")
    private  int uid;




    private String name;

    private int provinceCode;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

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

    public int getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(int provinceCode) {
        this.provinceCode = provinceCode;
    }

//    @Override
//    public String getUrl() {
//        return Constants.POISTION_CONSTANT+"/"+id;
//    }


    public static  List<Bean> getFromDB(ChooseAreaFragment fragment) {
//        LitePal.getDatabase();
        List<Province> provinces = LitePal.select(COLUMNS).find(Province.class);
        List<Bean> beans = provinces.stream().map(o -> (Bean) o).collect(Collectors.toList());
        return beans;
    }
}


