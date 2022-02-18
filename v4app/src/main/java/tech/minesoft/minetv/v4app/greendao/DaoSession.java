package tech.minesoft.minetv.v4app.greendao;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import tech.minesoft.minetv.v4app.bean.MineChannel;
import tech.minesoft.minetv.v4app.bean.MineMeta;
import tech.minesoft.minetv.v4app.bean.MineMovieInfo;
import tech.minesoft.minetv.v4app.bean.MineSiteInfo;
import tech.minesoft.minetv.v4app.bean.MineViewInfo;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig mineChannelDaoConfig;
    private final DaoConfig mineMetaDaoConfig;
    private final DaoConfig mineMovieInfoDaoConfig;
    private final DaoConfig mineSiteInfoDaoConfig;
    private final DaoConfig mineViewInfoDaoConfig;

    private final MineChannelDao mineChannelDao;
    private final MineMetaDao mineMetaDao;
    private final MineMovieInfoDao mineMovieInfoDao;
    private final MineSiteInfoDao mineSiteInfoDao;
    private final MineViewInfoDao mineViewInfoDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        mineChannelDaoConfig = daoConfigMap.get(MineChannelDao.class).clone();
        mineChannelDaoConfig.initIdentityScope(type);

        mineMetaDaoConfig = daoConfigMap.get(MineMetaDao.class).clone();
        mineMetaDaoConfig.initIdentityScope(type);

        mineMovieInfoDaoConfig = daoConfigMap.get(MineMovieInfoDao.class).clone();
        mineMovieInfoDaoConfig.initIdentityScope(type);

        mineSiteInfoDaoConfig = daoConfigMap.get(MineSiteInfoDao.class).clone();
        mineSiteInfoDaoConfig.initIdentityScope(type);

        mineViewInfoDaoConfig = daoConfigMap.get(MineViewInfoDao.class).clone();
        mineViewInfoDaoConfig.initIdentityScope(type);

        mineChannelDao = new MineChannelDao(mineChannelDaoConfig, this);
        mineMetaDao = new MineMetaDao(mineMetaDaoConfig, this);
        mineMovieInfoDao = new MineMovieInfoDao(mineMovieInfoDaoConfig, this);
        mineSiteInfoDao = new MineSiteInfoDao(mineSiteInfoDaoConfig, this);
        mineViewInfoDao = new MineViewInfoDao(mineViewInfoDaoConfig, this);

        registerDao(MineChannel.class, mineChannelDao);
        registerDao(MineMeta.class, mineMetaDao);
        registerDao(MineMovieInfo.class, mineMovieInfoDao);
        registerDao(MineSiteInfo.class, mineSiteInfoDao);
        registerDao(MineViewInfo.class, mineViewInfoDao);
    }
    
    public void clear() {
        mineChannelDaoConfig.clearIdentityScope();
        mineMetaDaoConfig.clearIdentityScope();
        mineMovieInfoDaoConfig.clearIdentityScope();
        mineSiteInfoDaoConfig.clearIdentityScope();
        mineViewInfoDaoConfig.clearIdentityScope();
    }

    public MineChannelDao getMineChannelDao() {
        return mineChannelDao;
    }

    public MineMetaDao getMineMetaDao() {
        return mineMetaDao;
    }

    public MineMovieInfoDao getMineMovieInfoDao() {
        return mineMovieInfoDao;
    }

    public MineSiteInfoDao getMineSiteInfoDao() {
        return mineSiteInfoDao;
    }

    public MineViewInfoDao getMineViewInfoDao() {
        return mineViewInfoDao;
    }

}