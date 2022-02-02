package tech.minesoft.minetv.request;

import tech.minesoft.minetv.bean.MineSiteInfo;
import tech.minesoft.minetv.greendao.DaoHelper;
import tech.minesoft.minetv.utils.MineCallback;
import tech.minesoft.minetv.utils.RetrofitHelper;
import tech.minesoft.minetv.utils.RetrofitService;


public class RequestService {
    private static RetrofitService service(){
        MineSiteInfo activeSite = activeSite();

        if (null != activeSite) {
            return RetrofitHelper.get(activeSite.getCode());
        }
        return null;
    }

    public static MineSiteInfo activeSite(){
        MineSiteInfo activeSite = DaoHelper.getPrimarySite();

        if (null != activeSite) {
            return activeSite;
        }
        return null;
    }

    public static void request(String keyword,int page, MineCallback callback){
        RetrofitService service = service();

        if (null != service) {
            service.detail(keyword, page).enqueue(callback);
        }
    }
}
