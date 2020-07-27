package tech.minesoft.minetv;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.google.gson.Gson;

import org.apache.commons.collections4.MapUtils;

import java.util.List;
import java.util.Map;

import tech.minesoft.minetv.bean.MineSiteInfo;
import tech.minesoft.minetv.greendao.DaoMaster;
import tech.minesoft.minetv.utils.Const;
import tech.minesoft.minetv.utils.Holder;
import tech.minesoft.minetv.utils.MineCallback;
import tech.minesoft.minetv.utils.RetrofitHelper;

public class Init {

    public static void init(Context context) {
        RetrofitHelper.add("github", Const.URL_GITHUB);

        RetrofitHelper.get("github").version().enqueue(new MineCallback<Map>(context) {
            @Override
            public void finish(boolean success, Map body, String message) {
                if (success) {
                    int newVersion = MapUtils.getIntValue(body, "version");
                    int appVersion = DaoMaster.SCHEMA_VERSION;

                    if (appVersion < newVersion) {
                        // 旧版本，提醒升级
                        update(context, MapUtils.getString(body, "update-url"));
                    }

                    // 更新url
                    initApi(context, appVersion);
                }
            }
        });
    }

    private static void update(Context context, String updateUrl) {
        new AlertDialog.Builder(context)
                .setMessage("已有新版本，是否前往升级？")
                .setNegativeButton("确定", (dialog, id) -> {
                    Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse(updateUrl));
                    it.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
                    context.startActivity(it);
                })
                .setPositiveButton("取消", null)
                .show();
    }

    private static void initApi(Context context, int version) {
        RetrofitHelper.get("github").api(version).enqueue(new MineCallback<Map>(context) {
            @Override
            public void finish(boolean success, Map body, String message) {
                if (success) {
                    Object urlsObj = body.get("urls");
                    if (urlsObj instanceof List) {
                        List urls = (List) urlsObj;
                        if (urls.size() > 0) {
                            // 删除所有旧链接
                            Holder.daoSession.getMineSiteInfoDao().queryBuilder().buildDelete()
                                    .executeDeleteWithoutDetachingEntities();

                            int primary = 1;
                            for (Object urlInfo : urls) {

                                String json = new Gson().toJson(urlInfo);
                                MineSiteInfo info = new Gson().fromJson(json, MineSiteInfo.class);
                                info.setStatus(1);
                                info.setPrimary(primary);
                                Holder.daoSession.insert(info);

                                RetrofitHelper.add(info.getCode(), info.getUrl());

                                primary = 0;
                            }

                            return;
                        }
                    }
                }

                Toast.makeText(context, "接口初始化失败", Toast.LENGTH_LONG).show();
            }
        });
    }
}
