package com.inka.playerapple.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.inka.netsync.data.cache.db.DatabaseCacheOpenHelper;
import com.inka.netsync.data.cache.db.MetaData;
import com.inka.netsync.logs.LogUtil;

public class DatabaseOpenHelperEx extends DatabaseCacheOpenHelper {

    private final String TAG = "DatabaseOpenHelperEx";

    static {
        MetaData.DATABASE_NAME = "PlayerApple";
        MetaData.DB_VERSION = 1;
    }

    public DatabaseOpenHelperEx(Context context) {
        super(context, MetaData.DATABASE_NAME, MetaData.DB_VERSION);
    }

    public void init () {
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        LogUtil.INSTANCE.info("birdgangdatabase", "DatabaseOpenHelperEx > onCreate > MetaData.DB_VERSION : " + MetaData.DB_VERSION);

        createBookmarkTableQuery(db, MetaData.BOOKMARK_TABLE);
        createContentTableQuery(db, MetaData.CONTENT_TABLE);
        createFavoriteTableQuery(db, MetaData.FAVORITE_TABLE);
        createRecentlyTableQuery(db, MetaData.RECENTLY_TABLE);
        createSerialTableQuery(db, MetaData.SERIAL_TABLE);
        createSerialTableQuery(db, MetaData.SERIAL_TABLE);
        createLMSTableQuery(db, MetaData.LMS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        LogUtil.INSTANCE.info("birdgangdatabase", "DatabaseOpenHelperEx > onUpgrade > oldVersion : " + oldVersion + " , newVersion : " + newVersion + " , MetaData.DB_VERSION : " + MetaData.DB_VERSION);
    }

    public void onDelete(SQLiteDatabase db) throws SQLException {
    }

    public void createSerialTableQuery(SQLiteDatabase db, String tableName) {
        String query = "CREATE TABLE IF NOT EXISTS "
                + tableName + " ("
                + MetaData.SerialColumns.SERIAL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MetaData.SerialColumns.SERIAL_NUMBER + " TEXT NOT NULL, "
                + MetaData.SerialColumns.CID + " TEXT NOT NULL, "
                + MetaData.SerialColumns.PRODUCT_CODE + " TEXT NOT NULL, "
                + MetaData.SerialColumns.PRODUCT_NAME + " TEXT NOT NULL, "
                + MetaData.SerialColumns.PRODUCT_ICON_PATH + " TEXT NOT NULL"
                + ");";

        db.execSQL(query);
    }


    public void createContentTableQuery(SQLiteDatabase db, String tableName) {
        String query = "CREATE TABLE IF NOT EXISTS "
                + tableName + " ("
                + MetaData.ContentColumns.CONTENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MetaData.ContentColumns.CATEGORY_ID + " INTEGER, "
                + MetaData.ContentColumns.CONTENT_DIRECTORY + " TEXT, "
                + MetaData.ContentColumns.PLAY_DATE + " DATE, "
                + MetaData.ContentColumns.CONTENT_DOWNLOAD_DATE + " DATE, "
                + MetaData.ContentColumns.CONTENT_NAME + " TEXT NOT NULL, "
                + MetaData.ContentColumns.CONTENT_FILE_PATH + " TEXT NOT NULL, "
                + MetaData.ContentColumns.CONTENT_LAST_PLAY_TIME + " INTEGER, "
                + MetaData.ContentColumns.IS_FAVORITE_CONTENT + " BOOL NOT NULL, "
                + MetaData.ContentColumns.LICENSE_INFO + " TEXT, "
                + MetaData.ContentColumns.SERIAL + " TEXT"
                + ");";

        db.execSQL(query);
        db.execSQL("CREATE INDEX IF NOT EXISTS " + tableName + "_index_content_id ON " + tableName + "(" + MetaData.ContentColumns.CONTENT_ID + ")");
        db.execSQL("CREATE INDEX IF NOT EXISTS " + tableName + "_index_content_filepath ON " + tableName + "(" + MetaData.ContentColumns.CONTENT_FILE_PATH + ")");

    }


    public void createFavoriteTableQuery(SQLiteDatabase db, String tableName) {
        String query = "CREATE TABLE IF NOT EXISTS "
                + tableName + " ("
                + MetaData.FavoriteColumns.FAVORITE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MetaData.FavoriteColumns.CONTENT_ID + " INTEGER NOT NULL, "
                + MetaData.FavoriteColumns.CONTENT_NAME + " TEXT, "
                + MetaData.FavoriteColumns.CONTENT_FILE_PATH + " TEXT NOT NULL, "
                + "UNIQUE(" + MetaData.FavoriteColumns.FAVORITE_ID + ", " + MetaData.FavoriteColumns.CONTENT_ID + ")"
                + ");";
        db.execSQL(query);
    }


    public void createRecentlyTableQuery(SQLiteDatabase db, String tableName) {
        String query = "CREATE TABLE IF NOT EXISTS "
                + tableName + " ("
                + MetaData.RecentlyColumns.RECENTLY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MetaData.RecentlyColumns.CONTENT_ID + " INTEGER NOT NULL, "
                + MetaData.RecentlyColumns.CONTENT_FILE_PATH + " TEXT NOT NULL, "
                + MetaData.RecentlyColumns.CONTENT_NAME + " TEXT, "
                + MetaData.RecentlyColumns.RECENTLY_DATE + " DATE, "
                + "UNIQUE(" + MetaData.RecentlyColumns.RECENTLY_ID + ", " + MetaData.RecentlyColumns.CONTENT_ID + ")"
                + ");";
        db.execSQL(query);
    }

}