package tech.minesoft.minetv;

import android.content.Context;

import java.util.List;
import java.util.Map;

import tech.minesoft.minetv.greendao.DaoHelper;
import tech.minesoft.minetv.utils.IOUtils;
import tech.minesoft.minetv.utils.JsonUtils;
import tech.minesoft.minetv.utils.LayoutUtils;

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
