package tech.minesoft.minetv.v4app.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import tech.minesoft.minetv.v4app.bean.MineMeta;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "MINE_META".
*/
public class MineMetaDao extends AbstractDao<MineMeta, Long> {

    public static final String TABLENAME = "MINE_META";

    /**
     * Properties of entity MineMeta.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Meta = new Property(1, String.class, "meta", false, "META");
        public final static Property Code = new Property(2, String.class, "code", false, "CODE");
        public final static Property Val = new Property(3, String.class, "val", false, "VAL");
    }


    public MineMetaDao(DaoConfig config) {
        super(config);
    }
    
    public MineMetaDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"MINE_META\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"META\" TEXT," + // 1: meta
                "\"CODE\" TEXT," + // 2: code
                "\"VAL\" TEXT);"); // 3: val
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"MINE_META\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, MineMeta entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String meta = entity.getMeta();
        if (meta != null) {
            stmt.bindString(2, meta);
        }
 
        String code = entity.getCode();
        if (code != null) {
            stmt.bindString(3, code);
        }
 
        String val = entity.getVal();
        if (val != null) {
            stmt.bindString(4, val);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, MineMeta entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String meta = entity.getMeta();
        if (meta != null) {
            stmt.bindString(2, meta);
        }
 
        String code = entity.getCode();
        if (code != null) {
            stmt.bindString(3, code);
        }
 
        String val = entity.getVal();
        if (val != null) {
            stmt.bindString(4, val);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public MineMeta readEntity(Cursor cursor, int offset) {
        MineMeta entity = new MineMeta( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // meta
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // code
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3) // val
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, MineMeta entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setMeta(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setCode(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setVal(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(MineMeta entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(MineMeta entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(MineMeta entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
