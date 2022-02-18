package tech.minesoft.minetv.v3app;

import android.content.Context;

import java.util.List;
import java.util.Map;

import tech.minesoft.minetv.v3app.bean.MineSiteInfo;
import tech.minesoft.minetv.v3app.greendao.V3DaoHelper;
import tech.minesoft.minetv.v3app.greendao.DaoMaster;
import tech.minesoft.minetv.v3app.utils.Const;
import tech.minesoft.minetv.v3app.utils.IOUtils;
import tech.minesoft.minetv.v3app.utils.JsonUtils;
import tech.minesoft.minetv.v3app.utils.MineCallback;
import tech.minesoft.minetv.v3app.utils.RetrofitHelper;
import tech.minesoft.minetv.v3app.vo.InitVo;

public class Init {

    public static void init(Context context) {
        RetrofitHelper.add("github", Const.URL_INIT);

        String defaultJson = IOUtils.readAssets(context, "Api.json");
        List<Map> siteInfoList = JsonUtils.toBean(defaultJson, List.class);

        for (Map infoMap : siteInfoList) {
            MineSiteInfo siteInfo = new MineSiteInfo();
            siteInfo.setCode((String) infoMap.get("code"));
            siteInfo.setName((String) infoMap.get("name"));
            siteInfo.setUrl((String) infoMap.get("url"));
            siteInfo.setPrimary(((Double) infoMap.get("primary")).intValue());
            V3DaoHelper.updateSite(siteInfo);
        }

        RetrofitHelper.get("github").init().enqueue(new MineCallback<InitVo>(context) {
            @Override
            public void finish(boolean success, InitVo body, String message) {
                if (success) {
                    int newVersion = body.getVersion();
                    int appVersion = DaoMaster.SCHEMA_VERSION;

                    if (appVersion < newVersion) {
                        // 旧版本，提醒升级
                        update(context, body.getUpdateUrl());
                    }

                    List<MineSiteInfo> sites = body.getSites();
                    if (null != sites && sites.size() > 0) {
                        // 更新所有旧链接
                        for (MineSiteInfo info : sites) {
                            info.setPrimary(0);
                            V3DaoHelper.updateSite(info);
                        }

                        V3DaoHelper.updatePrimary(body.getPrimary());
                    }
                } else {
                    // new AlertDialog.Builder(context)
                    //         .setMessage("初始化失败，重试或退出？")
                    //         .setNegativeButton("重试", (dialog, id) -> {
                    //             init(context);
                    //         })
                    //         .setPositiveButton("退出", (dialog, which) -> {
                    //             System.exit(0);
                    //         })
                    //         .show();
                }

                List<MineSiteInfo> activeSites = V3DaoHelper.getActiveSites();
                for (MineSiteInfo info : activeSites) {
                    RetrofitHelper.add(info.getCode(), info.getUrl());
                }
            }
        });
    }

    private static void update(Context context, String updateUrl) {
        // new AlertDialog.Builder(context)
        //         .setMessage("已有新版本，是否前往升级？")
        //         .setNegativeButton("确定", (dialog, id) -> {
        //             Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse(updateUrl));
        //             it.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
        //             context.startActivity(it);
        //         })
        //         .setPositiveButton("取消", null)
        //         .show();
    }

    // private static void initApi(Context context, int version) {
    //     RetrofitHelper.get("github").api(version).enqueue(new MineCallback<Map>(context) {
    //         @Override
    //         public void finish(boolean success, Map body, String message) {
    //             if (success) {
    //             }
    //
    //             Toast.makeText(context, "接口初始化失败", Toast.LENGTH_LONG).show();
    //         }
    //     });
    // }
}
