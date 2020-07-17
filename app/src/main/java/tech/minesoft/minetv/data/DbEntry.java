package tech.minesoft.minetv.data;

public class DbEntry {
    public static final String DATABASE_NAME = "MineTv.db";

    public static final String TABLE_INFO = "movie_info";
    public static final String TABLE_VIEW = "movie_view";

    public static final String INFO_ID = "id";
    public static final String INFO_STAR = "star_flag";
    public static final String INFO_VOD_ID = "vod_id";
    public static final String INFO_VOD_INFO = "vod_info";
    public static final String INFO_VOD_REVERSE = "vod_reverse";
    public static final String INFO_STAR_TIME = "star_time";
    public static final String INFO_LAST_OPEN = "last_open";

    public static final String VIEW_ID = "id";
    public static final String VIEW_VOD_ID = "vod_id";
    public static final String VIEW_FROM = "vod_from";
    public static final String VIEW_NAME = "vod_name";
    public static final String VIEW_TIME = "view_time";
    public static final String VIEW_POSITION = "view_position";

    public static final String SQL_CREATE_INFO =
            " CREATE TABLE IF NOT EXISTS " + TABLE_INFO + " ( " +
                    INFO_ID + " INTEGER " + "constraint " + TABLE_INFO + "_pk PRIMARY KEY autoincrement, " +
                    INFO_STAR + " INTEGER, " +
                    INFO_VOD_ID + " INTEGER, " +
                    INFO_VOD_INFO + " TEXT, " +
                    INFO_VOD_REVERSE + " INTEGER, " +
                    INFO_STAR_TIME + " INTEGER, " +
                    INFO_LAST_OPEN + " INTEGER) ";

    public static final String SQL_CREATE_VIEW =
            " CREATE TABLE IF NOT EXISTS " + TABLE_VIEW + " ( " +
                    VIEW_ID + " INTEGER " + "constraint " + TABLE_VIEW + "_pk PRIMARY KEY autoincrement, " +
                    VIEW_VOD_ID + " INTEGER, " +
                    VIEW_FROM + " TEXT, " +
                    VIEW_NAME + " TEXT, " +
                    VIEW_TIME + " INTEGER, " +
                    VIEW_POSITION + " INTEGER) ";

}