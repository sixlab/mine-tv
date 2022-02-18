package tech.minesoft.minetv.v5app.utils;

import com.google.gson.Gson;

public class JsonUtils {

    public static <T> T toBean(String json, Class<T> type) {
        Gson gson = new Gson();
        return gson.fromJson(json, type);
    }
}
