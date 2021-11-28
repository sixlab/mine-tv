package tech.minesoft.minetv.utils;

import android.text.TextUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tech.minesoft.minetv.bean.MineSiteInfo;
import tech.minesoft.minetv.greendao.DaoHelper;

public class RetrofitHelper {
    public static List<String> PLAY_FROM = Arrays.asList(
            "88zym3u8",
            "dbm3u8",
            "kbm3u8",
            "tkm3u8",
            "ckm3u8",
            "zkm3u8",
            "yjm3u8",
            "123kum3u8"
    );

    public static void initPlayFrom() {
        String playFrom = DaoHelper.playFrom();
        if (!TextUtils.isEmpty(playFrom)) {
            PLAY_FROM = Arrays.asList(TextUtils.split(playFrom, ","));
        }
    }

    private static Map<String, RetrofitService> SERVICE = new HashMap<>();
    public static OkHttpClient client;

    static {
        client = new OkHttpClient.Builder()
                // .addInterceptor(new UrlInterceptor())
                // .addNetworkInterceptor(new StethoInterceptor())
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .build();
    }

    public static void add(String code, String url) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitService service = retrofit.create(RetrofitService.class);
        SERVICE.put(code, service);
    }

    public static RetrofitService get(String code) {
        if (!SERVICE.containsKey(code)) {
            MineSiteInfo site = DaoHelper.getSite(code);
            if (null != site) {
                add(code, site.getUrl());
            }
        }
        return SERVICE.get(code);
    }
}
