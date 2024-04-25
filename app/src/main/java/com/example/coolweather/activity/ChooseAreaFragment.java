package com.example.coolweather.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coolweather.Adapter.AreaAdapter;
import com.example.coolweather.R;
import com.example.coolweather.db.Bean;
import com.example.coolweather.db.City;
import com.example.coolweather.db.County;
import com.example.coolweather.db.Province;
import com.example.coolweather.util.Constants.Constants;
import com.example.coolweather.util.DataResolver;
import com.example.coolweather.util.HttpUtil;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class ChooseAreaFragment extends Fragment {
    public static final String TITLE="title";
    private static final String TAG = "ChooseAreaFragment";
    RecyclerView recyclerView;
    AreaAdapter areaAdapter;
    public Class status = Province.class;
    volatile List<Bean> data;
    int provinceId=-1;
    int cityId=-1;
    int countyId=-1;

    public int getProvinceId() {
        return provinceId;
    }

    public int getCityId() {
        return cityId;
    }

    public int getCountyId() {
        return countyId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = getLayoutInflater().inflate(R.layout.choose_area, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        areaAdapter = new AreaAdapter(new ArrayList<>(),this);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(areaAdapter);
        listData(status);
        return view;
    }
    public void setId(int id){
        if(status==Province.class){
            provinceId = id;
        }else if(status==City.class){
            cityId = id;
        }else if(status== County.class){
            countyId = id;
        }
    }
    public boolean nextStatus(){
        if(status==Province.class){
            status = City.class;
            return true;
        }else if(status==City.class) {
            status = County.class;
            return true;
        }else{
            return false;
        }
    }
    public boolean lastStatus(){
        if(status==City.class){
            status = Province.class;
            provinceId=-1;
            cityId=-1;
            countyId=-1;
            return true;
        }else if(status==County.class) {
            status = City.class;
            countyId=-1;
            cityId=-1;
            return true;
        }else{
            provinceId=-1;
            return false;
        }
    }
    public void listData(Class clzz){
        showProgressDialog("正在拉取数据");
        //拉取数据
        //先从数据库找
        boolean b = queryDataFromDB(status);
        if(b){
            closeProgressDialog();
            areaAdapter.setData(data);
            areaAdapter.notifyDataSetChanged();
            return;
        }
        //数据库找不到就访问网络
        String url = getCurrentUrl();
        Log.e(TAG, "listData: " +url);
        Request.Builder builder = new Request.Builder();
        Request request = builder.url(url).build();
        HttpUtil.sendRequest(request, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Log.e(TAG, "onFailure: " );
            }

            @Override
            public void onResponse(Call call, Response response)  {
                Log.e(TAG, "onResponse: " );
                //存进数据库
                List<Bean> beans = DataResolver.resolveData(response,status);
                boolean b = DataResolver.handleAndSaveData(beans);
                if(b){
                    //修改ui
                    getActivity().runOnUiThread(() -> {
                        closeProgressDialog();
                        data = beans;
                        areaAdapter.setData(data);
                        areaAdapter.notifyDataSetChanged();
                    });
                }
            }
        });

        //recyclerView.setAdapter(new AreaAdapter(new ArrayList<Bean>(0),this));
    }

    public void showProgressDialog(String showMsg){

    }
    public void closeProgressDialog(){

    }
    public boolean queryDataFromDB(Class<Bean> clzz) {
        //查出来的数据放在data上
        Method method = null;
        try {
            method = clzz.getMethod("getFromDB");
            List<Bean> invoke = (List<Bean>)method.invoke(null,this);
            data = invoke;
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return false;
        }
        if(data==null || data.isEmpty())
            return false;
        return true;
    }

    public String getCurrentUrl(){
        if(status.equals(Province.class)){
            return  Constants.POISTION_CONSTANT;
        }else if(status.equals(City.class)){
            return  Constants.POISTION_CONSTANT+"/"+provinceId;
        }else{
            return Constants.POISTION_CONSTANT+"/"+provinceId+"/"+cityId;
        }
    }

    public void showOnView() {

    }

}
