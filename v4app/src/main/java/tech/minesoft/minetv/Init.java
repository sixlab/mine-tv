package tech.minesoft.minetv;

import android.content.Context;

import java.util.List;
import java.util.Map;

import tech.minesoft.minetv.bean.MineSiteInfo;
import tech.minesoft.minetv.greendao.DaoHelper;
import tech.minesoft.minetv.utils.IOUtils;
import tech.minesoft.minetv.utils.JsonUtils;

public class Init {

    public static void init(Context context) {
        String defaultJson = IOUtils.readAssets(context, "Api.json");
        List<Map> siteInfoList = JsonUtils.toBean(defaultJson, List.class);

        for (Map infoMap : siteInfoList) {
            MineSiteInfo siteInfo = new MineSiteInfo();
            siteInfo.setCode((String) infoMap.get("code"));
            siteInfo.setName((String) infoMap.get("name"));
            siteInfo.setUrl((String) infoMap.get("url"));
            siteInfo.setPrimary(((Double) infoMap.get("primary")).intValue());
            DaoHelper.updateSite(siteInfo);
        }
    }

}
