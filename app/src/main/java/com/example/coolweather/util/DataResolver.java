package com.example.coolweather.util;

import android.text.TextUtils;

import androidx.recyclerview.widget.RecyclerView;

import com.example.coolweather.db.Bean;
import com.example.coolweather.gson.Weather;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


import okhttp3.Response;

public class DataResolver {
    public static <T extends  Bean> List<T> resolveData(Response response, Class<T> clzz) {
        try {
            String responseData = response.body().string();
            if (TextUtils.isEmpty(responseData)) {
                throw  new DataException("response is empty");
            }
            //解析json
            Gson gson = new Gson();
            T[]  objs = (T[]) gson.fromJson(responseData, Array.newInstance(clzz, 0).getClass());
            if(objs!=null && objs.length!=0){
                return Arrays.stream(objs).collect(Collectors.toList());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public  static <T extends  Bean> boolean handleAndSaveData(List<T> objs){
        try {
            if (objs != null && objs.size() != 0) {
                for (T obj : objs) {
                    obj.save();
                }
            }
        }catch (Exception e){
            return false;
        }
        return true;
    }
    public  static <T extends Bean> boolean handleAndSaveData(Response response, Class clzz){
        List<T> objs = resolveData(response, clzz);
        return handleAndSaveData(objs);
    }
    public static Weather resolveWeatherData(String response){
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(response);
            JSONArray heWeathers = jsonObject.getJSONArray("HeWeather");
            String s = heWeathers.getJSONObject(0).toString();
            return  new Gson().fromJson(s, Weather.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  null;
    }
}
