package tech.minesoft.minetv.v5app.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import tech.minesoft.minetv.v5app.bean.MineSiteInfo;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "MINE_SITE_INFO".
*/
public class MineSiteInfoDao extends AbstractDao<MineSiteInfo, Long> {

    public static final String TABLENAME = "MINE_SITE_INFO";

    /**
     * Properties of entity MineSiteInfo.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Code = new Property(1, String.class, "code", false, "CODE");
        public final static Property Url = new Property(2, String.class, "url", false, "URL");
        public final static Property Primary = new Property(3, Integer.class, "primary", false, "PRIMARY");
        public final static Property Status = new Property(4, Integer.class, "status", false, "STATUS");
    }


    public MineSiteInfoDao(DaoConfig config) {
        super(config);
    }
    
    public MineSiteInfoDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"MINE_SITE_INFO\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"CODE\" TEXT UNIQUE ," + // 1: code
                "\"URL\" TEXT," + // 2: url
                "\"PRIMARY\" INTEGER," + // 3: primary
                "\"STATUS\" INTEGER);"); // 4: status
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"MINE_SITE_INFO\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, MineSiteInfo entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String code = entity.getCode();
        if (code != null) {
            stmt.bindString(2, code);
        }
 
        String url = entity.getUrl();
        if (url != null) {
            stmt.bindString(3, url);
        }
 
        Integer primary = entity.getPrimary();
        if (primary != null) {
            stmt.bindLong(4, primary);
        }
 
        Integer status = entity.getStatus();
        if (status != null) {
            stmt.bindLong(5, status);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, MineSiteInfo entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String code = entity.getCode();
        if (code != null) {
            stmt.bindString(2, code);
        }
 
        String url = entity.getUrl();
        if (url != null) {
            stmt.bindString(3, url);
        }
 
        Integer primary = entity.getPrimary();
        if (primary != null) {
            stmt.bindLong(4, primary);
        }
 
        Integer status = entity.getStatus();
        if (status != null) {
            stmt.bindLong(5, status);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public MineSiteInfo readEntity(Cursor cursor, int offset) {
        MineSiteInfo entity = new MineSiteInfo( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // code
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // url
            cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3), // primary
            cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4) // status
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, MineSiteInfo entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setCode(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setUrl(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setPrimary(cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3));
        entity.setStatus(cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(MineSiteInfo entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(MineSiteInfo entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(MineSiteInfo entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
