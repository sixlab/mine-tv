package com.ubtv66.minetv.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.gson.Gson;
import com.ubtv66.minetv.vo.VodInfo;

import java.util.ArrayList;
import java.util.List;

public class DbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;

    public DbHelper(Context context) {
        super(context, DbEntry.DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DbEntry.SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(DbEntry.SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public static void init(Context context) {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        dbHelper.onCreate(db);
        dbHelper.close();
    }

    public static void clearHis(Context context) {
        deleteRecord(context, DbEntry.TYPE_HIS);
    }

    public static void clearStar(Context context) {
        deleteRecord(context, DbEntry.TYPE_STAR);
    }

    private static void deleteRecord(Context context, String type){
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.execSQL("delete from " + DbEntry.TABLE_NAME + " where " + DbEntry.COLUMN_TYPE + " = ? "
                , new String[]{type});

        db.close();
    }

    public static List<VodInfo> loadHis(Context context) {
        return loadList(context, DbEntry.TYPE_HIS);
    }

    public static List<VodInfo> loadStar(Context context) {
        return loadList(context, DbEntry.TYPE_STAR);
    }

    private static List<VodInfo> loadList(Context context, String type) {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(DbEntry.TABLE_NAME, new String[]{DbEntry.COLUMN_VOD_INFO}, DbEntry.COLUMN_TYPE + " = ? ", new String[]{type}, null, null, DbEntry.COLUMN_ID + " DESC ");

        List<VodInfo> vodInfoList = new ArrayList<>();
        while (cursor.moveToNext()) {
            String json = cursor.getString(cursor.getColumnIndex(DbEntry.COLUMN_VOD_INFO));

            VodInfo vodInfo = new Gson().fromJson(json, VodInfo.class);

            if (null != vodInfo) {
                vodInfoList.add(vodInfo);
            }
        }
        cursor.close();
        dbHelper.close();

        return vodInfoList;
    }


    public static void insertHis(Context context, VodInfo info) {
        insertRecord(context, info, DbEntry.TYPE_HIS);
    }

    public static void insertStar(Context context, VodInfo info) {
        insertRecord(context, info, DbEntry.TYPE_STAR);
    }

    private static void insertRecord(Context context, VodInfo info, String type) {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor = db.query(DbEntry.TABLE_NAME, new String[]{DbEntry.COLUMN_ID},
                DbEntry.COLUMN_TYPE + " = ? and " + DbEntry.COLUMN_VOD_ID + " = ? ",
                new String[]{type, info.getVod_id().toString()}, null, null,
                DbEntry.COLUMN_ID + " DESC ");

        if (cursor.getCount() == 0) {
            ContentValues values = new ContentValues();
            values.put(DbEntry.COLUMN_TYPE, type);
            values.put(DbEntry.COLUMN_VOD_ID, info.getVod_id());
            values.put(DbEntry.COLUMN_VOD_INFO, new Gson().toJson(info));

            db.insert(DbEntry.TABLE_NAME, null, values);
        }

        db.close();
    }

    public static void delHis(Context context, Integer vodId) {
        delItem(context, vodId, DbEntry.TYPE_HIS);

    }

    public static void delStar(Context context, Integer vodId) {
        delItem(context, vodId, DbEntry.TYPE_STAR);
    }

    private static void delItem(Context context, Integer vodId, String type) {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.delete(DbEntry.TABLE_NAME,
                DbEntry.COLUMN_TYPE + " = ？ and " + DbEntry.COLUMN_VOD_ID + " = ? ",
                new String[]{type, vodId.toString()});

        db.close();
    }

    public static void updateInfo(Context context, VodInfo info) {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DbEntry.COLUMN_VOD_INFO, new Gson().toJson(info));

        db.update(DbEntry.TABLE_NAME, values,
                DbEntry.COLUMN_VOD_ID + " = ? ",
                new String[]{info.getVod_id().toString()});

        db.close();
    }


    // public static void main(String[] args) {
    //     DbHelper dbHelper = new DbHelper(null);
    //
    //     // Gets the data repository in write mode
    //     SQLiteDatabase db = dbHelper.getWritableDatabase();
    //
    //     // Create a new map of values, where column names are the keys
    //     ContentValues values = new ContentValues();
    //     values.put(DbEntry.COLUMN_TYPE, 1);
    //     values.put(DbEntry.COLUMN_INFO, "2");
    //
    //     // Insert the new row, returning the primary key value of the new row
    //     long newRowId = db.insert(DbEntry.TABLE_NAME, null, values);
    // }

}
