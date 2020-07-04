package com.ubtv66.minetv.data;

public class DbEntry {
    public static final String DATABASE_NAME = "MineTv.db";

    public static final String TABLE_NAME = "movie_record";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_VOD_ID = "vod_id";
    public static final String COLUMN_VOD_INFO = "vod_info";

    public static final String SQL_CREATE_ENTRIES =
            " CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ( " + COLUMN_ID + " INTEGER " +
                    "constraint " + TABLE_NAME + "_pk PRIMARY KEY autoincrement, " + COLUMN_TYPE + " INTEGER, " + COLUMN_VOD_ID + " INTEGER, " + COLUMN_VOD_INFO + " TEXT) ";

    public static final String SQL_DELETE_ENTRIES = " DROP TABLE IF EXISTS " + TABLE_NAME;

    public static final String TYPE_HIS = "1";
    public static final String TYPE_STAR = "2";
}
