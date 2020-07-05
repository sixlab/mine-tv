package com.ubtv66.minetv.data;

public class DbEntry {
    public static final String DATABASE_NAME = "MineTv.db";

    public static final String TABLE_RECORD = "movie_record";
    public static final String TABLE_VIEW = "movie_view";

    public static final String RECORD_ID = "id";
    public static final String RECORD_TYPE = "type";
    public static final String RECORD_VOD_ID = "vod_id";
    public static final String RECORD_VOD_INFO = "vod_info";


    public static final String VIEW_ID = "id";
    public static final String VIEW_VOD_ID = "vod_id";
    public static final String VIEW_FROM = "vod_from";
    public static final String VIEW_NAME = "vod_name";

    public static final String SQL_CREATE_RECORD =
            " CREATE TABLE IF NOT EXISTS " + TABLE_RECORD + " ( " + RECORD_ID + " INTEGER " +
                    "constraint " + TABLE_RECORD + "_pk PRIMARY KEY autoincrement, " +
                    RECORD_TYPE + " INTEGER, " + RECORD_VOD_ID + " INTEGER, " + RECORD_VOD_INFO + " TEXT) ";
    public static final String SQL_DELETE_RECORD = " DROP TABLE IF EXISTS " + TABLE_RECORD;

    public static final String SQL_CREATE_VIEW =
            " CREATE TABLE IF NOT EXISTS " + TABLE_VIEW + " ( " + VIEW_ID + " INTEGER " +
                    "constraint " + TABLE_VIEW + "_pk PRIMARY KEY autoincrement, " +
                    VIEW_VOD_ID + " INTEGER, " + VIEW_FROM + " TEXT, " + VIEW_NAME + " TEXT) ";
    public static final String SQL_DELETE_VIEW = " DROP TABLE IF EXISTS " + TABLE_VIEW;

    public static final String TYPE_HIS = "1";
    public static final String TYPE_STAR = "2";
    public static final String TYPE_CLICK = "3";
}
