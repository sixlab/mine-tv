package tech.minesoft.minetv.v3app.greendao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.greenrobot.greendao.database.Database;

public class DaoOpenHelper extends DaoMaster.DevOpenHelper {
    public DaoOpenHelper(Context context, String name) {
        super(context, name);
    }

    public DaoOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        Log.i("greenDAO", "Upgrading schema from version " + oldVersion + " to " + newVersion + " by dropping all tables");
        // dropAllTables(db, true);
        // onCreate(db);
        V3DaoHelper.upgrade(db, oldVersion, newVersion);
    }
}
