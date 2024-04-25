package com.example.coolweather.util;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class HttpUtil {
    public static void sendRequest(Request request, Callback callback){
        OkHttpClient  client = new OkHttpClient();
        client.newCall(request).enqueue(callback);
    }
}
