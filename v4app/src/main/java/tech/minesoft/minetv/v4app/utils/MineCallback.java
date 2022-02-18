package tech.minesoft.minetv.v4app.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class MineCallback<T> implements Callback<T> {

    private Context mContext;

    public MineCallback(Context mContext) {
        this.mContext = mContext;
    }

    public abstract void finish(boolean success, T body, String message);

    private void success(T body) {
        finish(true, body, "");
    }

    private void fail(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();

        finish(false, null, message);
    }

    public void onResponse(Call<T> call, Response<T> response) {
        if (null != response) {
            T body = response.body();
            if (null != body) {
                success(body);
            } else {
                fail("resp body null");
            }
        } else {
            fail("resp null");
        }
    }

    public void onFailure(Call<T> call, Throwable t) {
        String msg = "请求失败：" + t.getMessage();
        Log.e("HTTP", msg, t);
        fail(msg);
    }
}
