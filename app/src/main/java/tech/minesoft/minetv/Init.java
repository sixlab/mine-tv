package tech.minesoft.minetv;

import android.content.Context;

import java.util.List;

import tech.minesoft.minetv.bean.MineSiteInfo;
import tech.minesoft.minetv.greendao.DaoHelper;
import tech.minesoft.minetv.greendao.DaoMaster;
import tech.minesoft.minetv.utils.Const;
import tech.minesoft.minetv.utils.IOUtils;
import tech.minesoft.minetv.utils.JsonUtils;
import tech.minesoft.minetv.utils.MineCallback;
import tech.minesoft.minetv.utils.RetrofitHelper;
import tech.minesoft.minetv.vo.InitVo;

public class Init {

    public static void init(Context context) {
        RetrofitHelper.add("github", Const.URL_GITHUB);

        String defaultJson = IOUtils.readAssets(context, "Api.json");
        MineSiteInfo siteInfo = JsonUtils.toBean(defaultJson, MineSiteInfo.class);
        siteInfo.setStatus(1);
        DaoHelper.updateSite(siteInfo);

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
                            DaoHelper.updateSite(info);

                            RetrofitHelper.add(info.getCode(), info.getUrl());
                        }

                        DaoHelper.updatePrimary(body.getPrimary());
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
