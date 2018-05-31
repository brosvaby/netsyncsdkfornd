package com.inka.playermango.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.inka.netsync.data.cache.db.DatabaseCacheOpenHelper;
import com.inka.netsync.data.cache.db.MetaData;

/**
 * Created by birdgang on 2017. 8. 9..
 */
public class DatabaseOpenHelperEx extends DatabaseCacheOpenHelper {

    static {
        MetaData.DATABASE_NAME = "PlayerMango";
        MetaData.DB_VERSION = 1;
    }

    public DatabaseOpenHelperEx(Context context) {
        super(context, MetaData.DATABASE_NAME, MetaData.DB_VERSION);
    }

    public void init () {
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        super.onCreate(db);
        createContentTableQuery(db, MetaData.CONTENT_TABLE);
        createBookmarkTableQuery(db, MetaData.BOOKMARK_TABLE);
        createFavoriteTableQuery(db, MetaData.FAVORITE_TABLE);
        createRecentlyTableQuery(db, MetaData.RECENTLY_TABLE);
//        createCategoryTableQuery(db, MetaData.CATEGORY_TABLE);
//        createSettingTableQuery(db, MetaData.SETTING_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

}
