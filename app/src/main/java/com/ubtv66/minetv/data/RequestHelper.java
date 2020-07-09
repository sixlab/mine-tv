package com.ubtv66.minetv.data;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RequestHelper {
    private static final String BASE_URL = "https://api.okzy.tv/api.php/provide/vod/at/json/";

    public static RetrofitService service;
    public static OkHttpClient client;

    static {
        client = new OkHttpClient.Builder()
                // .addNetworkInterceptor(new StethoInterceptor())
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(RetrofitService.class);
    }
}
