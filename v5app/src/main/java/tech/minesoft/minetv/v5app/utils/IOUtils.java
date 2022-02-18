package tech.minesoft.minetv.v5app.utils;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class IOUtils {
    /**
     * 使用 assets 资源管理器 + IO流读取文件
     *
     * @param context  context
     * @param fileName 文件名
     * @return 内容
     */
    public static String readAssets(Context context, String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        //
        AssetManager assetManager = context.getAssets();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName), StandardCharsets.UTF_8));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
}
