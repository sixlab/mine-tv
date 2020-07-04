package com.ubtv66.minetv.utils;

import android.util.Log;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public interface MineCallback<T> extends Callback<T> {

    void success(T body);

    default void onResponse(Call<T> call, Response<T> response){
        if(null!=response){
            T body = response.body();
            if(null!= body){
                success(body);
            }else{
                Toast.makeText(null, "resp body null", Toast.LENGTH_LONG);
            }
        }else{
            Toast.makeText(null, "resp null", Toast.LENGTH_LONG);
        }
    }

    default void onFailure(Call<T> call, Throwable t){
        String msg = "请求失败：" + t.getMessage();
        Log.e("HTTP", msg,t);
        Toast.makeText(null, msg, Toast.LENGTH_LONG);
    }
}
