package tech.minesoft.minetv.v4app.request;

import tech.minesoft.minetv.v4app.bean.MineSiteInfo;
import tech.minesoft.minetv.v4app.greendao.DaoHelper;
import tech.minesoft.minetv.v4app.utils.MineCallback;
import tech.minesoft.minetv.v4app.utils.RetrofitHelper;
import tech.minesoft.minetv.v4app.utils.RetrofitService;


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
