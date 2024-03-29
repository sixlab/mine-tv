package tech.minesoft.minetv.v3app.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import tech.minesoft.minetv.v3app.bean.MineViewInfo;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "MINE_VIEW_INFO".
*/
public class MineViewInfoDao extends AbstractDao<MineViewInfo, Long> {

    public static final String TABLENAME = "MINE_VIEW_INFO";

    /**
     * Properties of entity MineViewInfo.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Info_id = new Property(1, Long.class, "info_id", false, "INFO_ID");
        public final static Property Vod_name = new Property(2, String.class, "vod_name", false, "VOD_NAME");
        public final static Property Vod_from = new Property(3, String.class, "vod_from", false, "VOD_FROM");
        public final static Property Vod_item_name = new Property(4, String.class, "vod_item_name", false, "VOD_ITEM_NAME");
        public final static Property View_position = new Property(5, Integer.class, "view_position", false, "VIEW_POSITION");
        public final static Property Vod_time = new Property(6, java.util.Date.class, "vod_time", false, "VOD_TIME");
    }


    public MineViewInfoDao(DaoConfig config) {
        super(config);
    }
    
    public MineViewInfoDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"MINE_VIEW_INFO\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"INFO_ID\" INTEGER," + // 1: info_id
                "\"VOD_NAME\" TEXT," + // 2: vod_name
                "\"VOD_FROM\" TEXT," + // 3: vod_from
                "\"VOD_ITEM_NAME\" TEXT," + // 4: vod_item_name
                "\"VIEW_POSITION\" INTEGER," + // 5: view_position
                "\"VOD_TIME\" INTEGER);"); // 6: vod_time
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"MINE_VIEW_INFO\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, MineViewInfo entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Long info_id = entity.getInfo_id();
        if (info_id != null) {
            stmt.bindLong(2, info_id);
        }
 
        String vod_name = entity.getVod_name();
        if (vod_name != null) {
            stmt.bindString(3, vod_name);
        }
 
        String vod_from = entity.getVod_from();
        if (vod_from != null) {
            stmt.bindString(4, vod_from);
        }
 
        String vod_item_name = entity.getVod_item_name();
        if (vod_item_name != null) {
            stmt.bindString(5, vod_item_name);
        }
 
        Integer view_position = entity.getView_position();
        if (view_position != null) {
            stmt.bindLong(6, view_position);
        }
 
        java.util.Date vod_time = entity.getVod_time();
        if (vod_time != null) {
            stmt.bindLong(7, vod_time.getTime());
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, MineViewInfo entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Long info_id = entity.getInfo_id();
        if (info_id != null) {
            stmt.bindLong(2, info_id);
        }
 
        String vod_name = entity.getVod_name();
        if (vod_name != null) {
            stmt.bindString(3, vod_name);
        }
 
        String vod_from = entity.getVod_from();
        if (vod_from != null) {
            stmt.bindString(4, vod_from);
        }
 
        String vod_item_name = entity.getVod_item_name();
        if (vod_item_name != null) {
            stmt.bindString(5, vod_item_name);
        }
 
        Integer view_position = entity.getView_position();
        if (view_position != null) {
            stmt.bindLong(6, view_position);
        }
 
        java.util.Date vod_time = entity.getVod_time();
        if (vod_time != null) {
            stmt.bindLong(7, vod_time.getTime());
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public MineViewInfo readEntity(Cursor cursor, int offset) {
        MineViewInfo entity = new MineViewInfo( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1), // info_id
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // vod_name
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // vod_from
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // vod_item_name
            cursor.isNull(offset + 5) ? null : cursor.getInt(offset + 5), // view_position
            cursor.isNull(offset + 6) ? null : new java.util.Date(cursor.getLong(offset + 6)) // vod_time
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, MineViewInfo entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setInfo_id(cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1));
        entity.setVod_name(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setVod_from(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setVod_item_name(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setView_position(cursor.isNull(offset + 5) ? null : cursor.getInt(offset + 5));
        entity.setVod_time(cursor.isNull(offset + 6) ? null : new java.util.Date(cursor.getLong(offset + 6)));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(MineViewInfo entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(MineViewInfo entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(MineViewInfo entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
