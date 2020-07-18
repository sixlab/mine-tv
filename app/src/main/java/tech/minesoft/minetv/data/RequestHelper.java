package tech.minesoft.minetv.data;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RequestHelper {
    public static final String[] BASE_URLs = {
        "https://api.okzy.tv/api.php/provide/vod/at/json/",
    };

    public static int urlIndex = 0;

    public static final List<String> PLAY_FROM = Arrays.asList(
            "ckm3u8",
            "zkm3u8",
            "33uuck"
    );

    public static RetrofitService service;
    public static OkHttpClient client;

    static {
        client = new OkHttpClient.Builder()
                .addInterceptor(new UrlInterceptor())
                // .addNetworkInterceptor(new StethoInterceptor())
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URLs[urlIndex])
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(RetrofitService.class);
    }
}
