package com.inka.netsync.data.cache.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.inka.netsync.data.BaseData;
import com.inka.netsync.logs.LogUtil;

public class DatabaseCacheOpenHelper extends SQLiteOpenHelper {

    private final String TAG = "DatabaseCacheOpenHelper";

    private static Context context;
    private static volatile DatabaseCacheOpenHelper defaultInstance;

    private static String DATABASE_NAME;
    private static int DATABASE_VERSION;

    public static DatabaseCacheOpenHelper getDefault() {
        return defaultInstance;
    }

    protected DatabaseCacheOpenHelper(Context context, String databaseName, int databaseVersion) {
        super(context, databaseName, null, databaseVersion);
        this.DATABASE_NAME = databaseName;
        this.DATABASE_VERSION = databaseVersion;
        this.context = context;
        this.defaultInstance = this;
        LogUtil.INSTANCE.info("birdgangdatabase", "DATABASE_NAME : " + databaseName + " , DB_VERSION : " + databaseVersion);
    }

    @Override
    public SQLiteDatabase getWritableDatabase() {
        SQLiteDatabase db;
        try {
            return super.getWritableDatabase();
        } catch (SQLiteException e) {
            try {
                db = SQLiteDatabase.openOrCreateDatabase(BaseData.DATABASE_NAME, null);
            } catch (SQLiteException e2) {
                Log.w(TAG, "SQLite database could not be created! XmfMedia library cannot be saved.");
                db = SQLiteDatabase.create(null);
            }
        }

        int version = db.getVersion();
        LogUtil.INSTANCE.info(TAG, "getWritableDatabase  > version : " + version);

        if (DATABASE_VERSION != version) {
            db.beginTransaction();
            try {
                if (version == 0) {
                    onCreate(db);
                } else {
                    onUpgrade(db, version, DATABASE_VERSION);
                }
                db.setVersion(DATABASE_VERSION);
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
        }
        return db;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        LogUtil.INSTANCE.info("birdgangdatabase", "onCreate");

        createBookmarkTableQuery(db, MetaData.BOOKMARK_TABLE);
        createCategoryTableQuery(db, MetaData.CATEGORY_TABLE);
        createContentTableQuery(db, MetaData.CONTENT_TABLE);
        createFavoriteTableQuery(db, MetaData.FAVORITE_TABLE);
        createRecentlyTableQuery(db, MetaData.RECENTLY_TABLE);
        createEnterpriseTableQuery(db, MetaData.ENTERPRISE_TABLE);
        createSerialTableQuery(db, MetaData.SERIAL_TABLE);
        createSettingTableQuery(db, MetaData.SETTING_TABLE);
        createTitleTableQuery(db, MetaData.TITLE_TABLE);
        createLMSTableQuery(db, MetaData.LMS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        LogUtil.INSTANCE.info("birdgangdatabase", "onUpgrade > oldVersion : " + oldVersion + " , newVersion : " + newVersion + " , MetaData.DB_VERSION : " + MetaData.DB_VERSION);

        if (oldVersion < 8) {
            // TYPE : SETTING
            // 이정보가 DB log 에 저장 되어야 하고 실제 logcat 에도 출력 되어야 함.
            // 추후 이정보는 암호화 되어야 하고 업체나 잉카측으로 보내져야 함.
            //Log.d(SmartNetsyncLog.LOG_TAG_SMARTNETSYNC, CLASS_NAME + ":onUpgrade " + "-oldVersion" + "\n" + SmartNetsyncLog.LOG_TAG_SETTING + "\n" + String.valueOf(oldVersion));

            db.execSQL(SqlStorage.ALTER_SQL_SETTING_TABLE);
            oldVersion = oldVersion + 1;
        }

        if (oldVersion < 9) {
            // TYPE : SETTING
            // 이정보가 DB log 에 저장 되어야 하고 실제 logcat 에도 출력 되어야 함.
            // 추후 이정보는 암호화 되어야 하고 업체나 잉카측으로 보내져야 함.
            //Log.d(SmartNetsyncLog.LOG_TAG_SMARTNETSYNC, CLASS_NAME + ":onUpgrade " + "-oldVersion" + "\n" + SmartNetsyncLog.LOG_TAG_SETTING + "\n" + String.valueOf(oldVersion));

            oldVersion = oldVersion + 1;
        }

        if (oldVersion < 10) {
            // TYPE : SETTING
            // 이정보가 DB log 에 저장 되어야 하고 실제 logcat 에도 출력 되어야 함.
            // 추후 이정보는 암호화 되어야 하고 업체나 잉카측으로 보내져야 함.
            //Log.d(SmartNetsyncLog.LOG_TAG_SMARTNETSYNC, CLASS_NAME + ":onUpgrade " + "-oldVersion" + "\n" + SmartNetsyncLog.LOG_TAG_SETTING + "\n" + String.valueOf(oldVersion));

            oldVersion = oldVersion + 1;
        }

        if (oldVersion < 11) {
            // TYPE : SETTING
            // 이정보가 DB log 에 저장 되어야 하고 실제 logcat 에도 출력 되어야 함.
            // 추후 이정보는 암호화 되어야 하고 업체나 잉카측으로 보내져야 함.
            //Log.d(SmartNetsyncLog.LOG_TAG_SMARTNETSYNC, CLASS_NAME + ":onUpgrade " + "-oldVersion" + "\n" + SmartNetsyncLog.LOG_TAG_SETTING + "\n" + String.valueOf(oldVersion));

            db.execSQL(SqlStorage.ALTER_SQL_CONTENT_TABLE);
            oldVersion = oldVersion + 1;
        }

        if (oldVersion < 12) {
            // TYPE : SETTING
            // 이정보가 DB log 에 저장 되어야 하고 실제 logcat 에도 출력 되어야 함.
            // 추후 이정보는 암호화 되어야 하고 업체나 잉카측으로 보내져야 함.
            //Log.d(SmartNetsyncLog.LOG_TAG_SMARTNETSYNC, CLASS_NAME + ":onUpgrade " + "-oldVersion" + "\n" + SmartNetsyncLog.LOG_TAG_SETTING + "\n" + String.valueOf(oldVersion));

            db.execSQL(SqlStorage.ALTER_SQL_CONTENT_TABLE_ADD_SERIAL);
            oldVersion = oldVersion + 1;
        }

        if (oldVersion < 13) {
            // TYPE : SETTING
            // 이정보가 DB log 에 저장 되어야 하고 실제 logcat 에도 출력 되어야 함.
            // 추후 이정보는 암호화 되어야 하고 업체나 잉카측으로 보내져야 함.
            //Log.d(SmartNetsyncLog.LOG_TAG_SMARTNETSYNC, CLASS_NAME + ":onUpgrade " + "-oldVersion" + "\n" + SmartNetsyncLog.LOG_TAG_SETTING + "\n" + String.valueOf(oldVersion));

            db.execSQL(SqlStorage.ALTER_SQL_CATEGORY_TABLE_ADD_TITLEID);
            oldVersion = oldVersion + 1;
        }

        if (oldVersion < 14) {
            // TYPE : SETTING
            // 이정보가 DB log 에 저장 되어야 하고 실제 logcat 에도 출력 되어야 함.
            // 추후 이정보는 암호화 되어야 하고 업체나 잉카측으로 보내져야 함.
            //Log.d(SmartNetsyncLog.LOG_TAG_SMARTNETSYNC, CLASS_NAME + ":onUpgrade " + "-oldVersion" + "\n" + SmartNetsyncLog.LOG_TAG_SETTING + "\n" + String.valueOf(oldVersion));

            db.execSQL(SqlStorage.ALTER_SQL_CONTENT_TABLE_ADD_LMSRAWSECTION);
            oldVersion = oldVersion + 1;
        }

        if (oldVersion < 15) {
            // TYPE : SETTING
            // 이정보가 DB log 에 저장 되어야 하고 실제 logcat 에도 출력 되어야 함.
            // 추후 이정보는 암호화 되어야 하고 업체나 잉카측으로 보내져야 함.
            //Log.d(SmartNetsyncLog.LOG_TAG_SMARTNETSYNC, CLASS_NAME + ":onUpgrade " + "-oldVersion" + "\n" + SmartNetsyncLog.LOG_TAG_SETTING + "\n" + String.valueOf(oldVersion));

            db.execSQL(SqlStorage.ALTER_SQL_CONTENT_TABLE_ADD_CONTENTIMAGE);
            oldVersion = oldVersion + 1;
        }

        if (oldVersion < 16) {
            // TYPE : SETTING
            // 이정보가 DB log 에 저장 되어야 하고 실제 logcat 에도 출력 되어야 함.
            // 추후 이정보는 암호화 되어야 하고 업체나 잉카측으로 보내져야 함.
            //Log.d(SmartNetsyncLog.LOG_TAG_SMARTNETSYNC, CLASS_NAME + ":onUpgrade " + "-oldVersion" + "\n" + SmartNetsyncLog.LOG_TAG_SETTING + "\n" + String.valueOf(oldVersion));

            db.execSQL(SqlStorage.ALTER_SQL_TITLE_TABLE_ADD_TITLEDESCRIPTION);
            oldVersion = oldVersion + 1;
        }

        if (oldVersion < 17) {
            db.execSQL(SqlStorage.ALTER_SQL_CONTENT_TABLE_ADD_DIRECTORY);
            oldVersion = oldVersion + 1;
        }
    }



    public void createSettingTableQuery(SQLiteDatabase db, String tableName) {
        String query = "CREATE TABLE IF NOT EXISTS "
                + tableName + " ("
                + MetaData.SettingColumns.SETTING_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MetaData.SettingColumns.INKA_LOG + " TEXT NOT NULL, "
                + MetaData.SettingColumns.NORMAL_TERMINATE + " BOOL NOT NULL, "
                + MetaData.SettingColumns.IS_DEVICE_REGISTER + " BOOL NOT NULL, "
                + MetaData.SettingColumns.USE_LOG + " BOOL NOT NULL, "
                + MetaData.SettingColumns.LTE + " BOOL NOT NULL, "
                + MetaData.SettingColumns.DOWNLOAD_DIR_TYPE + " INTEGER NOT NULL, "
                + MetaData.SettingColumns.PLAYER_TYPE + " INTEGER NOT NULL, "
                + MetaData.SettingColumns.SWX_PLAY + " INTEGER NOT NULL"
                + ");";

        db.execSQL(query);
    }


    public void createEnterpriseTableQuery(SQLiteDatabase db, String tableName) {
        String query = "CREATE TABLE IF NOT EXISTS "
                + tableName + " ("
                + MetaData.EnterpriseColumns.ENTERPRISE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MetaData.EnterpriseColumns.ENTERPRISE_CODE + " TEXT NOT NULL, "
                + MetaData.EnterpriseColumns.ENTERPRISE_NAME + " TEXT NOT NULL, "
                + MetaData.EnterpriseColumns.ENTERPRISE_ICON_PATH + " TEXT NOT NULL, "
                + MetaData.EnterpriseColumns.USER_ID + " TEXT NOT NULL, "
                + MetaData.EnterpriseColumns.PLAY_TYPE + " TEXT NOT NULL, "
                + MetaData.EnterpriseColumns.SERVICE_MANAGER + " TEXT NOT NULL, "
                + MetaData.EnterpriseColumns.AES256KEY + " TEXT NOT NULL, "
                + MetaData.EnterpriseColumns.AES256IV + " TEXT NOT NULL, "
                + MetaData.EnterpriseColumns.LOG + " TEXT NOT NULL, "
                + MetaData.EnterpriseColumns.LOG_TYPE + " TEXT NOT NULL, "
                + MetaData.EnterpriseColumns.IS_REGISTERED + " BOOL NOT NULL, "
                + MetaData.EnterpriseColumns.REGISTER_DATE + " DATE NOT NULL"
                + ");";

        db.execSQL(query);
        db.execSQL("CREATE INDEX IF NOT EXISTS " + tableName + "_index_enterprise_code ON " + tableName + "(" + MetaData.EnterpriseColumns.ENTERPRISE_CODE + ")");
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


    public void createTitleTableQuery(SQLiteDatabase db, String tableName) {
        String query = "CREATE TABLE IF NOT EXISTS "
                + tableName + " ("
                + MetaData.TitleColumns.TITLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MetaData.TitleColumns.ENTERPRISE_ID + " TEXT NOT NULL, "
                + MetaData.TitleColumns.TITLE_NAME + " TEXT NOT NULL, "
                + MetaData.TitleColumns.TITLE_ICON_PATH + " TEXT NOT NULL, "
                + MetaData.TitleColumns.TITLE_DESCRIPTION + " TEXT NOT NULL"
                + ");";

        db.execSQL(query);
    }


    public void createCategoryTableQuery(SQLiteDatabase db, String tableName) {
        String query = "CREATE TABLE IF NOT EXISTS "
                + tableName + " ("
                + MetaData.CategoryColumns.CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MetaData.CategoryColumns.ENTERPRISE_ID + " INTEGER, "
                + MetaData.CategoryColumns.CATEGORY_NAME + " TEXT NOT NULL, "
                + MetaData.CategoryColumns.CATEGORY_PERSON + " TEXT NOT NULL, "
                + MetaData.CategoryColumns.CATEGORY_IMAGE + " TEXT NOT NULL, "
                + MetaData.CategoryColumns.CATEGORY_ASSORTMENT + " TEXT NOT NULL, "
                + MetaData.CategoryColumns.LAST_PLAY_DATE + " TEXT NOT NULL, "
                + MetaData.CategoryColumns.TITLE_ID + " INTEGER"
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

//        CREATE UNIQUE INDEX pk_index ON "table1"("field1","field2");
//        db.execSQL("CREATE INDEX IF NOT EXISTS " + tableName + "_index_content_id ON " + tableName + "(" + MetaData.ContentColumns.CONTENT_ID + ")");
    }


    public void createBookmarkTableQuery(SQLiteDatabase db, String tableName) {
        String query = "CREATE TABLE IF NOT EXISTS "
                + tableName + " ("
                + MetaData.BookmarkColumns.BOOKMARK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MetaData.BookmarkColumns.CONTENT_ID + " INTEGER, "
                + MetaData.BookmarkColumns.BOOKMARK_CONTENT_FILE_PATH + " TEXT, "
                + MetaData.BookmarkColumns.BOOKMARK_CONTENT_NAME + " TEXT, "
                + MetaData.BookmarkColumns.BOOKMARK_DATE + " DATE NOT NULL, "
                + MetaData.BookmarkColumns.BOOKMARK_LOCATION + " TEXT NOT NULL, "
                + MetaData.BookmarkColumns.BOOKMARK_MEMO + " TEXT NOT NULL"
                + ");";

        db.execSQL(query);
    }


    public void createLMSTableQuery(SQLiteDatabase db, String tableName) {
        String query = "CREATE TABLE IF NOT EXISTS "
                + tableName + " ("
                + MetaData.LMSColumns.LMS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MetaData.LMSColumns.CONTENT_ID + " INTEGER, "
                + MetaData.LMSColumns.CONTENT_FILE_PATH + " DATE NOT NULL, "
                + MetaData.LMSColumns.CONTENT_NAME + " TEXT NOT NULL, "
                + MetaData.LMSColumns.SECTION + " TEXT NOT NULL, "
                + MetaData.LMSColumns.RAW_SECTION + " TEXT NOT NULL, "
                + MetaData.LMSColumns.RATE + " TEXT NOT NULL, "
                + "UNIQUE(" + MetaData.LMSColumns.LMS_ID + ", " + MetaData.LMSColumns.CONTENT_ID + ")"
                + ");";
        db.execSQL(query);
    }


}
