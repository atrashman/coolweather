package com.example.coolweather.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coolweather.R;
import com.example.coolweather.activity.ChooseAreaFragment;
import com.example.coolweather.activity.WeatherActivity;
import com.example.coolweather.db.Bean;
import com.example.coolweather.db.County;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;


public class AreaAdapter  extends RecyclerView.Adapter<AreaAdapter.AreaViewHolder> {
    volatile List<Bean> data = new ArrayList<>();
    ChooseAreaFragment fragment;

    public AreaAdapter(List<Bean> data, ChooseAreaFragment fragment){
        this.data = data;
        this.fragment = fragment;
    }
    @NonNull
    @Override
    public AreaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.area_item, parent, false);
        AreaViewHolder viewHolder = new AreaViewHolder(view);
        viewHolder.text.setOnClickListener(v->{
            int position = viewHolder.getBindingAdapterPosition();
            Bean t = data.get(position);
            Method method = null;
            Integer id = null;
            try {
                //通过点击事件获取id和修改fragment的当前id一级status
                method = fragment.status.getMethod("getUid");
                id = (Integer) method.invoke(t);//这里可能会有类型错误
                fragment.setId(id);
                if (!fragment.nextStatus()) {
                    //跳转到新的界面
                    Intent intent = new Intent(fragment.getActivity(), WeatherActivity.class);
                    intent.putExtra("weather_id",((County)t).getWeatherId());
                    fragment.startActivity(intent);
                    return ;
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            ////从数据库中获取 找不到就另开线程网络请求
            fragment.listData(fragment.status);

        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AreaViewHolder holder, int position) {
        Bean bean = data.get(position);
        String text =null;
        try {
            Method method = bean.getClass().getMethod("getName");
            text = (String) method.invoke(bean);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        holder.text.setText(text);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<Bean> data) {
        this.data = data;
    }

    public static class AreaViewHolder  extends RecyclerView.ViewHolder {
        TextView text;
        Bean obj;
        public AreaViewHolder(View itemView){
            super(itemView);
            text=itemView.findViewById(R.id.name);
        }


    }



}
