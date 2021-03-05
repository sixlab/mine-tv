package tech.minesoft.minetv.greendao;

import org.greenrobot.greendao.database.Database;

import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tech.minesoft.minetv.MineApplication;
import tech.minesoft.minetv.bean.MineMeta;
import tech.minesoft.minetv.bean.MineMovieInfo;
import tech.minesoft.minetv.bean.MineSiteInfo;
import tech.minesoft.minetv.bean.MineViewInfo;
import tech.minesoft.minetv.utils.Holder;
import tech.minesoft.minetv.utils.IOUtils;
import tech.minesoft.minetv.vo.UrlInfo;

public class DaoHelper {

    public static void upgrade(Database db, int oldVersion, int newVersion) {

        db.execSQL("UPDATE MINE_META set val = ? where meta = 'version'", new Object[]{newVersion});

        for (int i = oldVersion; i < newVersion; i++) {
            try {
                String sql = IOUtils.readAssets(MineApplication.context, "upgrade/" + oldVersion + ".sql");

                db.execSQL(sql);
            } catch (Exception e) {
            }
        }
    }

    public static String meta(String meta) {
        List<MineMeta> metas = Holder.daoSession.queryRaw(MineMeta.class, " where meta = ? ", meta);
        if (null != meta && metas.size() > 0) {
            return metas.get(0).getVal();
        }

        return "";
    }

    public static void clearHis() {
        Holder.daoSession.getMineMovieInfoDao().queryBuilder().buildDelete().executeDeleteWithoutDetachingEntities();
    }

    public static void clearUnStar(){
        //获取对象DAO
        MineMovieInfoDao infoDao = Holder.daoSession.getMineMovieInfoDao();

        infoDao.queryBuilder().where(
                MineMovieInfoDao.Properties.Star_flag.eq("0")
        ).buildDelete().executeDeleteWithoutDetachingEntities();
    }

    public static void clearStar() {
        //获取对象DAO
        MineMovieInfoDao infoDao = Holder.daoSession.getMineMovieInfoDao();

        //获取到所有实体类，并在内存中先处理好数据
        List<MineMovieInfo> infoList = infoDao.queryRaw(" where star_flag = ? ", "1");
        for (MineMovieInfo info : infoList) {
            info.setStar_flag(0);
        }

        // 在一次事物中提交全部实体类
        infoDao.updateInTx(infoList);
    }

    public static void clearViews() {
        Holder.daoSession.getMineViewInfoDao().queryBuilder().buildDelete().executeDeleteWithoutDetachingEntities();
    }

    public static long saveInfo(MineMovieInfo info) {

        MineMovieInfoDao infoDao = Holder.daoSession.getMineMovieInfoDao();

        List<MineMovieInfo> infoList = infoDao.queryRaw(" where api_code = ? and vod_id = ? ",
                info.getApi_code(), info.getVod_id().toString());

        long id;
        info.setLast_open(new Date());
        if (infoList.size() > 0) {
            MineMovieInfo oldInfo = infoList.get(0);
            id = oldInfo.getId();

            info.setId(oldInfo.getId());
            info.setStar_flag(oldInfo.getStar_flag());
            info.setVod_reverse(oldInfo.getVod_reverse());
            info.setStar_time(oldInfo.getStar_time());

            // info.setApi_code(oldInfo.getApi_code());
            // info.setApi_name(oldInfo.getApi_name());
            // info.setApi_url(oldInfo.getApi_url());

            infoDao.update(info);
        } else {
            info.setId(null);
            info.setStar_flag(0);
            info.setVod_reverse(0);

            id = infoDao.insert(info);
        }

        return id;
    }

    public static void saveSiteInfo(MineSiteInfo siteInfo) {
        MineSiteInfoDao siteInfoDao = Holder.daoSession.getMineSiteInfoDao();

        if(null == siteInfo.getId()){
            siteInfoDao.insert(siteInfo);
        }else{
            siteInfoDao.update(siteInfo);
        }
    }

    public static MineMovieInfo getInfo(long id) {
        return Holder.daoSession.getMineMovieInfoDao().load(id);
    }

    public static MineMovieInfo updateInfo(Long id, MineMovieInfo info) {
        MineMovieInfo oldInfo = Holder.daoSession.getMineMovieInfoDao().load(id);

        oldInfo.setVod_id(info.getVod_id());
        oldInfo.setVod_play_from(info.getVod_play_from());
        oldInfo.setVod_name(info.getVod_name());
        oldInfo.setVod_pic(info.getVod_pic());
        oldInfo.setType_name(info.getType_name());
        oldInfo.setVod_director(info.getVod_director());
        oldInfo.setVod_actor(info.getVod_actor());
        oldInfo.setVod_area(info.getVod_area());
        oldInfo.setVod_year(info.getVod_year());
        oldInfo.setVod_remarks(info.getVod_remarks());
        oldInfo.setVod_content(info.getVod_content());
        oldInfo.setVod_play_from(info.getVod_play_from());
        oldInfo.setVod_play_server(info.getVod_play_server());
        oldInfo.setVod_play_note(info.getVod_play_note());
        oldInfo.setVod_play_url(info.getVod_play_url());

        Holder.daoSession.getMineMovieInfoDao().update(oldInfo);

        return Holder.daoSession.getMineMovieInfoDao().load(id);
    }

    public static void delInfo(long infoId) {
        Holder.daoSession.getMineMovieInfoDao().deleteByKey(infoId);
    }

    public static void delSite(Long infoId) {
        Holder.daoSession.getMineSiteInfoDao().deleteByKey(infoId);
    }

    public static Map<String, Integer> selectView(long infoId) {
        MineViewInfoDao viewDao = Holder.daoSession.getMineViewInfoDao();

        List<MineViewInfo> viewList = viewDao.queryRaw(" where info_id = ? ", infoId + "");

        Map<String, Integer> viewed = new HashMap<>();
        for (MineViewInfo viewInfo : viewList) {
            String from = viewInfo.getVod_from();
            String name = viewInfo.getVod_item_name();

            String key = MessageFormat.format("{0}${1}", from, name);
            viewed.put(key, 1);
        }

        return viewed;
    }

    public static MineMovieInfo changeReverse(long infoId) {
        MineMovieInfo info = getInfo(infoId);
        info.setVod_reverse(1 - info.getVod_reverse());
        Holder.daoSession.update(info);
        return getInfo(infoId);
    }

    public static MineMovieInfo changeStar(long infoId) {
        MineMovieInfo info = getInfo(infoId);
        info.setStar_flag(1 - info.getStar_flag());
        Holder.daoSession.update(info);
        return getInfo(infoId);
    }

    public static void delViews(long infoId) {
        MineViewInfoDao viewDao = Holder.daoSession.getMineViewInfoDao();
        viewDao.queryBuilder().where(
                MineViewInfoDao.Properties.Info_id.eq(infoId)
        ).buildDelete().executeDeleteWithoutDetachingEntities();
    }

    public static List<MineMovieInfo> loadAll() {
        MineMovieInfoDao infoDao = Holder.daoSession.getMineMovieInfoDao();

        if(Holder.showHidden){
            return infoDao.queryBuilder().orderDesc(MineMovieInfoDao.Properties.Last_open).list();
        }else{
            return infoDao.queryBuilder().where(
                    MineMovieInfoDao.Properties.Vod_hide.eq("0")
            ).orderDesc(MineMovieInfoDao.Properties.Last_open).list();
        }
    }

    public static List<MineMovieInfo> loadStar() {
        MineMovieInfoDao infoDao = Holder.daoSession.getMineMovieInfoDao();
    
        if(Holder.showHidden){
            return infoDao.queryBuilder().where(
                    MineMovieInfoDao.Properties.Star_flag.eq("1")
            ).orderDesc(MineMovieInfoDao.Properties.Last_open).list();
        }else{
            return infoDao.queryBuilder().where(
                    MineMovieInfoDao.Properties.Vod_hide.eq("0"),
                    MineMovieInfoDao.Properties.Star_flag.eq("1")
            ).orderDesc(MineMovieInfoDao.Properties.Last_open).list();
        }
    }

    public static List<MineMovieInfo> loadUnStar() {
        MineMovieInfoDao infoDao = Holder.daoSession.getMineMovieInfoDao();

        if(Holder.showHidden){
            return infoDao.queryBuilder().where(
                    MineMovieInfoDao.Properties.Star_flag.eq("0")
            ).orderDesc(MineMovieInfoDao.Properties.Last_open).list();
        }else{
            return infoDao.queryBuilder().where(
                    MineMovieInfoDao.Properties.Vod_hide.eq("0"),
                    MineMovieInfoDao.Properties.Star_flag.eq("0")
            ).orderDesc(MineMovieInfoDao.Properties.Last_open).list();
        }
    }

    public static List<MineSiteInfo> getActiveSites() {
        MineSiteInfoDao siteInfoDao = Holder.daoSession.getMineSiteInfoDao();

        return siteInfoDao.queryBuilder().where(
                MineSiteInfoDao.Properties.Status.eq(1)
        ).list();
    }

    public static MineSiteInfo getPrimarySite() {
        MineSiteInfoDao siteInfoDao = Holder.daoSession.getMineSiteInfoDao();

        MineSiteInfo siteInfo = siteInfoDao.queryBuilder().where(
                MineSiteInfoDao.Properties.Status.eq(1),
                MineSiteInfoDao.Properties.Primary.eq(1)
        ).limit(1).unique();

        return siteInfo;
    }

    public static MineSiteInfo getSite(String code) {
        MineSiteInfoDao siteInfoDao = Holder.daoSession.getMineSiteInfoDao();

        MineSiteInfo siteInfo = siteInfoDao.queryBuilder().where(
                MineSiteInfoDao.Properties.Code.eq(code)
        ).limit(1).unique();

        return siteInfo;
    }

    public static void addView(UrlInfo info) {
        MineViewInfo viewInfo = new MineViewInfo();
        viewInfo.setInfo_id(info.getInfoId());
        viewInfo.setVod_from(info.getGroupName());
        viewInfo.setVod_name(info.getVodName());
        viewInfo.setVod_item_name(info.getItemName());
        viewInfo.setVod_time(new Date());
        viewInfo.setView_position(0);

        Holder.daoSession.insert(viewInfo);
    }

    public static void addView(UrlInfo info, String itemName) {
        MineViewInfo viewInfo = new MineViewInfo();
        viewInfo.setInfo_id(info.getInfoId());
        viewInfo.setVod_from(info.getGroupName());
        viewInfo.setVod_name(info.getVodName());
        viewInfo.setVod_item_name(itemName);
        viewInfo.setVod_time(new Date());
        viewInfo.setView_position(0);

        Holder.daoSession.insert(viewInfo);
    }

    public static void disableAllSite() {
        MineSiteInfoDao siteInfoDao = Holder.daoSession.getMineSiteInfoDao();
        List<MineSiteInfo> list = siteInfoDao.queryBuilder().where(
                MineSiteInfoDao.Properties.Status.eq("1")
        ).list();

        for (MineSiteInfo siteInfo : list) {
            siteInfo.setStatus(0);
        }

        siteInfoDao.updateInTx(list);
    }

    public static void updateSite(MineSiteInfo info) {
        MineSiteInfoDao siteInfoDao = Holder.daoSession.getMineSiteInfoDao();

        MineSiteInfo siteInfo = siteInfoDao.queryBuilder().where(
                MineSiteInfoDao.Properties.Code.eq(info.getCode())
        ).limit(1).unique();

        if (null == siteInfo) {
            info.setStatus(1);
            siteInfoDao.insert(info);
        } else {
            siteInfo.setStatus(1);
            siteInfo.setName(info.getName());
            siteInfo.setUrl(info.getUrl());
            siteInfoDao.update(siteInfo);
        }
    }

    public static void updatePrimary(String primary) {
        MineSiteInfoDao siteInfoDao = Holder.daoSession.getMineSiteInfoDao();
        List<MineSiteInfo> list = siteInfoDao.queryBuilder().where(
                MineSiteInfoDao.Properties.Primary.eq("1"),
                MineSiteInfoDao.Properties.Status.eq("1")
        ).list();

        if (list.size() == 0) {
            MineSiteInfo siteInfo = siteInfoDao.queryBuilder().where(
                    MineSiteInfoDao.Properties.Code.eq(primary)
            ).limit(1).unique();

            if (null != siteInfo) {
                siteInfo.setPrimary(1);
                siteInfoDao.update(siteInfo);
            }
        }
    }

    public static void updatePrimary(Long id) {
        MineSiteInfoDao siteInfoDao = Holder.daoSession.getMineSiteInfoDao();
        List<MineSiteInfo> list = siteInfoDao.queryBuilder().where(
                MineSiteInfoDao.Properties.Status.eq("1")
        ).list();

        for (MineSiteInfo siteInfo : list) {
            if (siteInfo.getId() == id.longValue()) {
                siteInfo.setPrimary(1);
                siteInfoDao.update(siteInfo);
            } else if (siteInfo.getPrimary() == 1) {
                siteInfo.setPrimary(0);
                siteInfoDao.update(siteInfo);
            }
        }
    }

    public static void clearViews(Long infoId, String groupName, String itemName) {
        MineViewInfoDao viewDao = Holder.daoSession.getMineViewInfoDao();
        viewDao.queryBuilder().where(
                MineViewInfoDao.Properties.Info_id.eq(infoId),
                MineViewInfoDao.Properties.Vod_from.eq(groupName),
                MineViewInfoDao.Properties.Vod_item_name.eq(itemName)
        ).buildDelete().executeDeleteWithoutDetachingEntities();
    }
    
    public static boolean showHidden() {
        MineMetaDao metaDao = Holder.daoSession.getMineMetaDao();
        
        MineMeta meta = metaDao.queryBuilder().where(
                MineMetaDao.Properties.Meta.eq("showHidden")
        ).limit(1).unique();
        
        if(meta == null){
            meta = new MineMeta();
            meta.setMeta("showHidden");
            meta.setVal("0");
            
            metaDao.insert(meta);
            
            return false;
        }else{
            return "1".equals(meta.getVal());
        }
    }
    
    public static boolean toggleHiddenDisplay() {
        MineMetaDao metaDao = Holder.daoSession.getMineMetaDao();
        
        MineMeta meta = metaDao.queryBuilder().where(
                MineMetaDao.Properties.Meta.eq("showHidden")
        ).limit(1).unique();
        
        if(meta == null){
            meta = new MineMeta();
            meta.setMeta("showHidden");
            meta.setVal("0");
            
            metaDao.insert(meta);
            
            return false;
        }else{
            boolean hidden = "0".equals(meta.getVal());
    
            meta.setVal(hidden?"1":"0");
            metaDao.update(meta);
            
            return hidden;
        }
    }
    
    public static MineMovieInfo toggleVodHidden(Long infoId) {
        MineMovieInfo info = getInfo(infoId);
        info.setVod_hide(1 - info.getVod_hide());
        Holder.daoSession.update(info);
        return info;
    }
}
