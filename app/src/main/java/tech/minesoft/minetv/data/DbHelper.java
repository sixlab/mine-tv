package tech.minesoft.minetv.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.gson.Gson;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tech.minesoft.minetv.utils.Const;
import tech.minesoft.minetv.bean.UrlInfo;
import tech.minesoft.minetv.bean.VodInfo;

public class DbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 3;

    public DbHelper(Context context) {
        super(context, DbEntry.DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DbEntry.SQL_CREATE_INFO);
        db.execSQL(DbEntry.SQL_CREATE_VIEW);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion){
            case 1:
                Log.i(Const.LOG_TAG,"oldVersion 1");
                break;
            case 2:
                Log.i(Const.LOG_TAG, "oldVersion 2");
                break;
        }
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

    public static void saveInfo(Context context, VodInfo info) {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor = db.query(DbEntry.TABLE_INFO, new String[]{DbEntry.INFO_ID},
                DbEntry.INFO_VOD_ID + " = ? ",
                new String[]{info.getVod_id().toString()}, null, null,
                DbEntry.INFO_ID + " DESC ");

        ContentValues values = new ContentValues();
        values.put(DbEntry.INFO_LAST_OPEN, new Date().getTime());

        if (cursor.getCount() == 0) {
            values.put(DbEntry.INFO_STAR, 0);
            values.put(DbEntry.INFO_VOD_ID, info.getVod_id());
            values.put(DbEntry.INFO_VOD_INFO, new Gson().toJson(info));
            values.put(DbEntry.INFO_VOD_REVERSE, 0);
            values.put(DbEntry.INFO_STAR_TIME, 0);
            db.insert(DbEntry.TABLE_INFO, null, values);
        } else {
            db.update(DbEntry.TABLE_INFO, values, DbEntry.INFO_VOD_ID + " = ? ", new String[]{info.getVod_id().toString()});
        }

        db.close();
    }

    public static void updateInfo(Context context, VodInfo info) {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DbEntry.INFO_VOD_INFO, new Gson().toJson(info));

        db.update(DbEntry.TABLE_INFO, values,
                DbEntry.INFO_VOD_ID + " = ? ",
                new String[]{info.getVod_id().toString()});

        db.close();
    }

    public static void delInfo(Context context, Integer vodId)  {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.delete(DbEntry.TABLE_INFO,
                DbEntry.INFO_VOD_ID + " = ? ",
                new String[]{vodId.toString()});

        db.close();
    }

    public static List<VodInfo> loadHis(Context context) {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(DbEntry.TABLE_INFO, new String[]{DbEntry.INFO_VOD_INFO},
                null, null, null, null,
                DbEntry.INFO_LAST_OPEN + " DESC ");

        List<VodInfo> vodInfoList = new ArrayList<>();
        while (cursor.moveToNext()) {
            String json = cursor.getString(cursor.getColumnIndex(DbEntry.INFO_VOD_INFO));

            VodInfo vodInfo = new Gson().fromJson(json, VodInfo.class);

            if (null != vodInfo) {
                vodInfoList.add(vodInfo);
            }
        }
        cursor.close();
        dbHelper.close();

        return vodInfoList;
    }

    public static List<VodInfo> loadStar(Context context) {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(DbEntry.TABLE_INFO, new String[]{DbEntry.INFO_VOD_INFO},
                DbEntry.INFO_STAR + " = 1 ", null, null, null,
                DbEntry.INFO_LAST_OPEN + " DESC ");

        List<VodInfo> vodInfoList = new ArrayList<>();
        while (cursor.moveToNext()) {
            String json = cursor.getString(cursor.getColumnIndex(DbEntry.INFO_VOD_INFO));

            VodInfo vodInfo = new Gson().fromJson(json, VodInfo.class);

            if (null != vodInfo) {
                vodInfoList.add(vodInfo);
            }
        }
        cursor.close();
        dbHelper.close();

        return vodInfoList;
    }

    public static void changeStar(Context context, Integer vodId) {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor = db.query(DbEntry.TABLE_INFO, new String[]{DbEntry.INFO_STAR},
                DbEntry.INFO_VOD_ID + " = ? ",
                new String[]{vodId.toString()}, null, null,
                DbEntry.INFO_ID + " DESC ");

        if (cursor.moveToNext()) {
            int star = cursor.getInt(cursor.getColumnIndex(DbEntry.INFO_STAR));

            ContentValues values = new ContentValues();
            if(star==0){
                values.put(DbEntry.INFO_STAR, 1);
                values.put(DbEntry.INFO_STAR_TIME, new Date().getTime());
            }else{
                values.put(DbEntry.INFO_STAR, 0);
                values.put(DbEntry.INFO_STAR_TIME, 0);
            }

            db.update(DbEntry.TABLE_INFO, values, DbEntry.INFO_VOD_ID + " = ? ", new String[]{vodId.toString()});
        }

        cursor.close();
        dbHelper.close();
    }

    public static void addView(Context context, UrlInfo info) {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DbEntry.VIEW_VOD_ID, info.getVodId());
        values.put(DbEntry.VIEW_FROM, info.getGroupName());
        values.put(DbEntry.VIEW_NAME, info.getItemName());
        values.put(DbEntry.VIEW_TIME, new Date().getTime());
        values.put(DbEntry.VIEW_POSITION, 0);

        db.insert(DbEntry.TABLE_VIEW, null, values);

        db.close();
    }

    public static Map<String, Integer> selectView(Context context, Integer vodId) {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(DbEntry.TABLE_VIEW, new String[]{
                        DbEntry.VIEW_FROM,
                        DbEntry.VIEW_NAME,
                },
                DbEntry.VIEW_VOD_ID + " = ? ", new String[]{vodId.toString()},
                DbEntry.VIEW_FROM+","+DbEntry.VIEW_NAME, null,
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

    public static void clearHis(Context context) {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.delete(DbEntry.TABLE_INFO, null, null);

        db.close();
    }

    public static void clearStar(Context context) {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DbEntry.INFO_STAR, 0);
        values.put(DbEntry.INFO_STAR_TIME, 0);

        db.update(DbEntry.TABLE_INFO, values, null, null);

        db.close();
    }

    public static void clearViews(Context context) {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.delete(DbEntry.TABLE_VIEW, null, null);

        db.close();
    }

    public static boolean isStar(Context context, Integer vodId) {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(DbEntry.TABLE_INFO, new String[]{DbEntry.INFO_ID},
                DbEntry.INFO_STAR + " = 1 and " + DbEntry.INFO_VOD_ID + " = ? ",
                new String[]{vodId.toString()}, null, null,
                DbEntry.INFO_ID + " DESC ");

        boolean isStar = (cursor.getCount() > 0);
        cursor.close();
        dbHelper.close();

        return isStar;
    }

    public static void delViews(Context context, Integer vodId) {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.delete(DbEntry.TABLE_VIEW,
                DbEntry.VIEW_VOD_ID + " = ? ",
                new String[]{vodId.toString()});

        db.close();
    }

    // -- - - -- - -- - -- -

    public static boolean needReverse(Context context, Integer vodId) {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(DbEntry.TABLE_INFO, new String[]{DbEntry.INFO_VOD_REVERSE},
                DbEntry.INFO_VOD_ID + " = ? ",
                new String[]{vodId.toString()}, null, null,
                DbEntry.INFO_ID + " DESC ");

        boolean needReverse = false;
        if (cursor.moveToNext()) {
            int reverse = cursor.getInt(cursor.getColumnIndex(DbEntry.INFO_VOD_REVERSE));
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
        values.put(DbEntry.INFO_VOD_REVERSE, reverse ? 1 : 0);

        db.update(DbEntry.TABLE_INFO, values,
                DbEntry.INFO_VOD_ID + " = ? ",
                new String[]{vodId.toString()});

        db.close();
    }

}
