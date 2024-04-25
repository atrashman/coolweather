package com.example.coolweather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.coolweather.activity.ChooseAreaFragment;

import org.litepal.LitePal;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private ChooseAreaFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //设置toolbar
        Toolbar toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(fragment==null)
            fragment = (ChooseAreaFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_area);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy: ");
        LitePal.deleteDatabase("cool_weather");
    }

    @Override
    public void onBackPressed() {
        boolean b = fragment.lastStatus();
        if(!b) {
            super.onBackPressed();
            return;
        }
        fragment.listData(fragment.status);
    }
}
