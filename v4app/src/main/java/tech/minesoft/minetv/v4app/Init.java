package tech.minesoft.minetv.v4app;

import android.content.Context;

import java.util.List;
import java.util.Map;

import tech.minesoft.minetv.v4app.greendao.DaoHelper;
import tech.minesoft.minetv.v4app.utils.IOUtils;
import tech.minesoft.minetv.v4app.utils.JsonUtils;
import tech.minesoft.minetv.v4app.utils.LayoutUtils;

public class Init {

    public static void init(Context context) {
        LayoutUtils.init(context);

        String defaultJson = IOUtils.readAssets(context, "Api.json");
        List<Map> siteInfoList = JsonUtils.toBean(defaultJson, List.class);

        for (Map infoMap : siteInfoList) {
            String code = (String) infoMap.get("code");
            String url = (String) infoMap.get("url");
            int primary = ((Double) infoMap.get("primary")).intValue();

            DaoHelper.updateSite(code, url, primary);
        }
    }

}
