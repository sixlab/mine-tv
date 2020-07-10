package com.ubtv66.minetv.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.gson.Gson;
import com.ubtv66.minetv.vo.VodInfo;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 2;

    public DbHelper(Context context) {
        super(context, DbEntry.DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DbEntry.SQL_CREATE_RECORD);
        db.execSQL(DbEntry.SQL_CREATE_VIEW);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion == 1) {
            upgradeFrom1(db);
        }
    }

    private void upgradeFrom1(SQLiteDatabase db) {
        db.execSQL(DbEntry.SQL_1);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // super.onDowngrade(db, oldVersion, newVersion);
    }

    public static void init(Context context) {
        DbHelper dbHelper = new DbHelper(context);
        dbHelper.getWritableDatabase(); // 会调用 升降级
        dbHelper.close();
    }

    public static boolean isStar(Context context, Integer vodId) {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(DbEntry.TABLE_RECORD, new String[]{DbEntry.RECORD_ID},
                DbEntry.RECORD_TYPE + " = ? and " + DbEntry.RECORD_VOD_ID + " = ? ",
                new String[]{DbEntry.TYPE_STAR, vodId.toString()}, null, null,
                DbEntry.RECORD_ID + " DESC ");

        boolean isStar = (cursor.getCount() > 0);
        cursor.close();
        dbHelper.close();

        return isStar;
    }

    public static void clearHis(Context context) {
        deleteRecord(context, DbEntry.TYPE_HIS);
    }

    public static void clearStar(Context context) {
        deleteRecord(context, DbEntry.TYPE_STAR);
    }

    private static void deleteRecord(Context context, String type) {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.execSQL("delete from " + DbEntry.TABLE_RECORD + " where " + DbEntry.RECORD_TYPE + " = ? ", new String[]{type});

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

        Cursor cursor = db.query(DbEntry.TABLE_RECORD, new String[]{DbEntry.RECORD_VOD_INFO}, DbEntry.RECORD_TYPE + " = ? ", new String[]{type}, null, null, DbEntry.RECORD_ID + " DESC ");

        List<VodInfo> vodInfoList = new ArrayList<>();
        while (cursor.moveToNext()) {
            String json = cursor.getString(cursor.getColumnIndex(DbEntry.RECORD_VOD_INFO));

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

        Cursor cursor = db.query(DbEntry.TABLE_RECORD, new String[]{DbEntry.RECORD_ID},
                DbEntry.RECORD_TYPE + " = ? and " + DbEntry.RECORD_VOD_ID + " = ? ",
                new String[]{type, info.getVod_id().toString()}, null, null,
                DbEntry.RECORD_ID + " DESC ");

        if (cursor.getCount() == 0) {
            ContentValues values = new ContentValues();
            values.put(DbEntry.RECORD_TYPE, type);
            values.put(DbEntry.RECORD_VOD_ID, info.getVod_id());
            values.put(DbEntry.RECORD_VOD_INFO, new Gson().toJson(info));

            db.insert(DbEntry.TABLE_RECORD, null, values);
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

        db.delete(DbEntry.TABLE_RECORD,
                DbEntry.RECORD_TYPE + " = ? and " + DbEntry.RECORD_VOD_ID + " = ? ",
                new String[]{type, vodId.toString()});

        db.close();
    }

    public static void updateInfo(Context context, VodInfo info) {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DbEntry.RECORD_VOD_INFO, new Gson().toJson(info));

        db.update(DbEntry.TABLE_RECORD, values,
                DbEntry.RECORD_VOD_ID + " = ? ",
                new String[]{info.getVod_id().toString()});

        db.close();
    }

    public static void insertView(Context context, Integer vodId, String from, String name) {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DbEntry.VIEW_VOD_ID, vodId);
        values.put(DbEntry.VIEW_FROM, from);
        values.put(DbEntry.VIEW_NAME, name);

        db.insert(DbEntry.TABLE_VIEW, null, values);

        db.close();
    }

    public static Map<String, Integer> queryView(Context context, Integer vodId) {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(DbEntry.TABLE_VIEW, new String[]{
                        DbEntry.VIEW_FROM,
                        DbEntry.VIEW_NAME,
                },
                DbEntry.VIEW_VOD_ID + " = ? ", new String[]{vodId.toString()},
                null, null,
                DbEntry.VIEW_ID + " DESC ");

        Map<String, Integer> viewed = new HashMap<>();
        while (cursor.moveToNext()) {
            String from = cursor.getString(cursor.getColumnIndex(DbEntry.VIEW_FROM));
            String name = cursor.getString(cursor.getColumnIndex(DbEntry.VIEW_NAME));

            String key = MessageFormat.format("{0}${1}", from, name);
            viewed.put(key, 1);
        }
        cursor.close();
        dbHelper.close();

        return viewed;
    }

    public static void delAllViews(Context context) {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.delete(DbEntry.TABLE_VIEW, null, null);

        db.close();
    }

    public static void delViews(Context context, Integer vodId) {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.delete(DbEntry.TABLE_VIEW,
                DbEntry.VIEW_VOD_ID + " = ? ",
                new String[]{vodId.toString()});

        db.close();
    }

    public static boolean needReverse(Context context, Integer vodId) {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(DbEntry.TABLE_RECORD, new String[]{DbEntry.RECORD_ID},
                DbEntry.RECORD_VOD_ID + " = ? ",
                new String[]{vodId.toString()}, null, null,
                DbEntry.RECORD_ID + " DESC ");

        boolean needReverse = false;
        if (cursor.moveToNext()) {
            int reverse = cursor.getInt(cursor.getColumnIndex(DbEntry.RECORD_VOD_REVERSE));
            needReverse = (1 == reverse);
        }

        cursor.close();
        dbHelper.close();

        return needReverse;
    }

    public static void updateReverse(Context context, Integer vodId, boolean reverse) {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DbEntry.RECORD_VOD_REVERSE, reverse ? 1 : 0);

        db.update(DbEntry.TABLE_RECORD, values,
                DbEntry.RECORD_VOD_ID + " = ? ",
                new String[]{vodId.toString()});

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
