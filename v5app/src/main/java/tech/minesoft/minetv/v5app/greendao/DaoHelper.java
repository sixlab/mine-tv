package tech.minesoft.minetv.v5app.greendao;

import org.greenrobot.greendao.database.Database;

import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tech.minesoft.minetv.v5app.MineApplication;
import tech.minesoft.minetv.v5app.bean.MineChannel;
import tech.minesoft.minetv.v5app.bean.MineMeta;
import tech.minesoft.minetv.v5app.bean.MineMovieInfo;
import tech.minesoft.minetv.v5app.bean.MineSiteInfo;
import tech.minesoft.minetv.v5app.bean.MineViewInfo;
import tech.minesoft.minetv.v5app.utils.Holder;
import tech.minesoft.minetv.v5app.utils.IOUtils;
import tech.minesoft.minetv.v5app.vo.UrlInfo;

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

    public static void updateMeta(String meta, String val) {
        MineMetaDao metaDao = Holder.daoSession.getMineMetaDao();

        MineMeta mineMeta = metaDao.queryBuilder().where(
                MineMetaDao.Properties.Meta.eq(meta)
        ).orderDesc(MineMetaDao.Properties.Id).limit(1).unique();
        if (null != mineMeta) {
            mineMeta.setVal(val);

            metaDao.update(mineMeta);
        }else{
            mineMeta = new MineMeta();
            mineMeta.setMeta(meta);
            mineMeta.setVal(val);
            metaDao.update(mineMeta);
        }
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
            info.setVod_hide(0);
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

    public static MineViewInfo selectLastView(Long infoId) {
        MineViewInfoDao viewDao = Holder.daoSession.getMineViewInfoDao();

        return viewDao.queryBuilder().where(
                MineViewInfoDao.Properties.Info_id.eq(infoId)
        ).orderDesc(MineViewInfoDao.Properties.Vod_time).limit(1).unique();
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

    public static MineViewInfo addView(UrlInfo info) {
        MineViewInfo prevViewInfo = selectViewInfo(info);
        if (prevViewInfo == null) {
            prevViewInfo = new MineViewInfo();
            prevViewInfo.setInfo_id(info.getInfoId());
            prevViewInfo.setVod_from(info.getGroupName());
            prevViewInfo.setVod_name(info.getVodName());
            prevViewInfo.setVod_item_name(info.getItemName());
            prevViewInfo.setVod_time(new Date());
            prevViewInfo.setView_position(0L);
            Holder.daoSession.insert(prevViewInfo);
        }

        return prevViewInfo;
    }

    public static void updateView(MineViewInfo viewInfo) {
        Holder.daoSession.update(viewInfo);
    }

    public static MineViewInfo selectViewInfo(UrlInfo urlInfo) {
        MineViewInfoDao viewDao = Holder.daoSession.getMineViewInfoDao();

        return viewDao.queryBuilder().where(
                MineViewInfoDao.Properties.Info_id.eq(urlInfo.getInfoId()),
                MineViewInfoDao.Properties.Vod_from.eq(urlInfo.getGroupName()),
                MineViewInfoDao.Properties.Vod_item_name.eq(urlInfo.getItemName())
        ).orderDesc(MineViewInfoDao.Properties.Vod_time).limit(1).unique();
    }

    public static void updateSite(String code, String url, int primary) {
        MineSiteInfoDao siteInfoDao = Holder.daoSession.getMineSiteInfoDao();

        MineSiteInfo siteInfo = siteInfoDao.queryBuilder().where(
                MineSiteInfoDao.Properties.Code.eq(code)
        ).limit(1).unique();

        if (null == siteInfo) {
            siteInfo = new MineSiteInfo();
            siteInfo.setCode(code);
            siteInfo.setUrl(url);
            siteInfo.setPrimary(primary);
            siteInfo.setStatus(1);
            siteInfoDao.insert(siteInfo);
        } else {
            siteInfo.setUrl(url);
            siteInfoDao.update(siteInfo);
        }
    }

    public static void updatePrimary(String code) {
        MineSiteInfoDao siteInfoDao = Holder.daoSession.getMineSiteInfoDao();

        MineSiteInfo siteInfo = siteInfoDao.queryBuilder().where(
                MineSiteInfoDao.Properties.Code.eq(code)
        ).limit(1).unique();

        if (null != siteInfo) {
            //获取到所有实体类，并在内存中先处理好数据
            List<MineSiteInfo> infoList = siteInfoDao.loadAll();
            for (MineSiteInfo info : infoList) {
                if(info.getCode().equals(code)){
                    info.setPrimary(1);
                }else{
                    info.setPrimary(0);
                }
            }

            // 在一次事物中提交全部实体类
            siteInfoDao.updateInTx(infoList);
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

    public static void saveChannel(String[] originGroups) {
        MineChannelDao channelDao = Holder.daoSession.getMineChannelDao();

        for (String group : originGroups) {
            MineChannel mineChannel = channelDao.queryBuilder().where(
                    MineChannelDao.Properties.Name.eq(group)
            ).limit(1).unique();

            if (null == mineChannel) {
                mineChannel = new MineChannel(null, group, 1, 1);
                channelDao.save(mineChannel);
            }
        }
    }

    public static List<MineChannel> statusChannels(int status) {
        MineChannelDao siteInfoDao = Holder.daoSession.getMineChannelDao();

        return siteInfoDao.queryBuilder().where(
                MineChannelDao.Properties.Status.eq(status)
        ).list();
    }

    public static int channelStatus(String group) {
        MineChannelDao channelDao = Holder.daoSession.getMineChannelDao();

        MineChannel mineChannel = channelDao.queryBuilder().where(
                MineChannelDao.Properties.Name.eq(group)
        ).limit(1).unique();

        if (null == mineChannel) {
            return 0;
        }

        return mineChannel.getStatus();
    }

    public static void updateChannel(MineChannel channel) {
        MineChannelDao channelDao = Holder.daoSession.getMineChannelDao();
        channelDao.update(channel);
    }

    public static void delChannel(Long id) {
        MineChannelDao channelDao = Holder.daoSession.getMineChannelDao();
        channelDao.deleteByKey(id);
    }
}
