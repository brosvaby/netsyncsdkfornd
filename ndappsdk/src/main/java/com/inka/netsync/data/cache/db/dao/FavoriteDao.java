package com.inka.netsync.data.cache.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.inka.netsync.data.cache.db.DatabaseCacheOpenHelper;
import com.inka.netsync.data.cache.db.MetaData;
import com.inka.netsync.data.cache.db.model.FavoriteCacheEntry;
import com.inka.netsync.logs.LogUtil;

import java.util.ArrayList;

/**
 * Created by birdgang on 2018. 1. 15..
 */
public class FavoriteDao {

    private final String TAG = "ContentDao";

    private SQLiteDatabase db;
    private DatabaseCacheOpenHelper databaseOpenHelper = null;

    private static volatile FavoriteDao defaultInstance;

    public static FavoriteDao getDefault() {
        if (defaultInstance == null) {
            synchronized (FavoriteDao.class) {
                if (defaultInstance == null) {
                    defaultInstance = new FavoriteDao();
                }
            }
        }
        return defaultInstance;
    }

    private FavoriteDao() {
        databaseOpenHelper = DatabaseCacheOpenHelper.getDefault();
    }


    public ArrayList<FavoriteCacheEntry> getIsFavoriteContentArrayList() throws Exception {
        ArrayList<FavoriteCacheEntry> contentEntries = new ArrayList<FavoriteCacheEntry>();
        Cursor cursor = null;
        try {
            db = databaseOpenHelper.getReadableDatabase();
            cursor = db.query(MetaData.FAVORITE_TABLE, null, null, null, null, null, MetaData.FavoriteColumns.FAVORITE_ID, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    FavoriteCacheEntry favoriteEntry = new FavoriteCacheEntry(cursor);
                    int contentId = favoriteEntry.getContentId();
                    LogUtil.INSTANCE.info(TAG , "getIsFavoriteContentArrayList > contentId : " + contentId);
                    if (contentId > 0) {
                        LogUtil.INSTANCE.info(TAG, "favoriteEntry.toString() : " + favoriteEntry.toString());
                        contentEntries.add(favoriteEntry);
                    }
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            LogUtil.INSTANCE.error(getClass().getSimpleName(), e);
        } finally {
            try {
                if (cursor != null) {
                    cursor.close();
                }
            } catch (Exception e) {
                LogUtil.INSTANCE.error(getClass().getSimpleName(), e);
            }
        }
        return contentEntries;
    }


    public FavoriteCacheEntry getFavoriteByPath (String path) {
        FavoriteCacheEntry favoriteEntry = null;
        Cursor cursor = null;
        try {
            db = databaseOpenHelper.getReadableDatabase();
            cursor = db.query(MetaData.FAVORITE_TABLE, null, getSelectionForCondition(MetaData.FavoriteColumns.CONTENT_FILE_PATH, path), null, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    favoriteEntry = new FavoriteCacheEntry(cursor);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            LogUtil.INSTANCE.error(getClass().getSimpleName(), e);
        } finally {
            try{
                if (cursor != null) {
                    cursor.close();
                }
            } catch(Exception e) {
                LogUtil.INSTANCE.error(getClass().getSimpleName(), e);
            }
        }
        return favoriteEntry;
    }


    public long insertFavorite (FavoriteCacheEntry favoriteEntry) throws SQLiteException {
        long result = -1;
        try {
            db = databaseOpenHelper.getWritableDatabase();
            if (null == db) {
                throw new SQLiteException();
            }
            int contentId = favoriteEntry.getContentId();
            boolean hasExist = checkRecordExist(db, new String[]{MetaData.FavoriteColumns.CONTENT_ID}, new String[]{ String.valueOf(contentId) });
            if (!hasExist) {
                result = db.insert(MetaData.FAVORITE_TABLE, null, favoriteEntry.toContentValues());
            }
            LogUtil.INSTANCE.debug("db", "favorite DAO insert > hasExist : " + hasExist + ", result : " + result);
        } catch(Exception e) {
            LogUtil.INSTANCE.error(getClass().getSimpleName(), e);
        }
        return result;
    }


    public int deleteFavoriteById(int favoriteId) {
        int result = -1;
        try {
            db = databaseOpenHelper.getWritableDatabase();
            result = db.delete(MetaData.FAVORITE_TABLE, MetaData.FavoriteColumns.FAVORITE_ID + "=" + "'"+ String.valueOf(favoriteId) +"'", null);
            LogUtil.INSTANCE.debug("db", "deleteFavoriteById > result : " + result);
        } catch(Exception e) {
            LogUtil.INSTANCE.error(getClass().getSimpleName(), e);
        }
        return result;
    }


    public int deleteFavoriteByContentName(String contentName) {
        int result = -1;
        try {
            db = databaseOpenHelper.getWritableDatabase();
            result = db.delete(MetaData.FAVORITE_TABLE, MetaData.FavoriteColumns.CONTENT_NAME + "=" + "'"+ contentName +"'", null);
            LogUtil.INSTANCE.debug("db", "deleteFavoriteByContentName > result : " + result);
        } catch(Exception e) {
            LogUtil.INSTANCE.error(getClass().getSimpleName(), e);
        }
        return result;
    }

    public int deleteFavoriteByContentId(int contentId) {
        int result = -1;
        try {
            db = databaseOpenHelper.getWritableDatabase();
            result = db.delete(MetaData.FAVORITE_TABLE, MetaData.FavoriteColumns.CONTENT_ID + "=" + "'"+ String.valueOf(contentId) +"'", null);
            LogUtil.INSTANCE.debug("db", "deleteFavoriteByContentId > result : " + result);
        } catch(Exception e) {
            LogUtil.INSTANCE.error(getClass().getSimpleName(), e);
        }
        return result;
    }


    public boolean checkFavoriteByContentName (String contentName) {
        boolean hasExist = false;
        try {
            db = databaseOpenHelper.getWritableDatabase();
            hasExist = checkRecordExist(db, new String[]{MetaData.FavoriteColumns.CONTENT_NAME}, new String[]{ contentName });
            LogUtil.INSTANCE.debug("db", "checkFavoriteByContentName > result : " + hasExist);
        } catch(Exception e) {
            LogUtil.INSTANCE.error(getClass().getSimpleName(), e);
        }
        return hasExist;
    }

    public boolean checkFavoriteByContentId (int contentId) {
        boolean hasExist = false;
        try {
            db = databaseOpenHelper.getWritableDatabase();
            hasExist = checkRecordExist(db, new String[]{MetaData.FavoriteColumns.CONTENT_ID}, new String[]{ String.valueOf(contentId) });
            LogUtil.INSTANCE.debug("db", "checkFavoriteByContentId > result : " + hasExist);
        } catch(Exception e) {
            LogUtil.INSTANCE.error(getClass().getSimpleName(), e);
        }
        return hasExist;
    }


    public boolean hasTable() {
        boolean result = false;
        try {
            db = databaseOpenHelper.getWritableDatabase();
            result = hasTable(db, MetaData.FAVORITE_TABLE);
        } catch(Exception e) {
            LogUtil.INSTANCE.error(getClass().getSimpleName(), e);
        }
        return result;
    }

    public boolean hasTable(SQLiteDatabase db, String tableName) {
        if (tableName == null || db == null || !db.isOpen()) {
            return false;
        }
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM sqlite_master WHERE type = ? AND name = ?", new String[] {"table", tableName});
        if (!cursor.moveToFirst()) {
            cursor.close();
            return false;
        }
        int count = cursor.getInt(0);
        cursor.close();
        return count > 0;
    }


    private boolean checkRecordExist(SQLiteDatabase db, String[] keys, String [] values) throws SQLiteException {
        boolean exists = false;
        Cursor cursor = null;
        StringBuilder sb = new StringBuilder();
        try {
            for (int i = 0; i < keys.length; i++) {
                sb.append(keys[i]).append("=\"").append(values[i]).append("\" ");
                if (i<keys.length-1) {
                    sb.append("AND ");
                }
            }
            cursor = db.query(MetaData.FAVORITE_TABLE, null, sb.toString(), null, null, null, null);
            if (cursor != null) {
                exists = (cursor.getCount() > 0);
            }
        } finally {
            try {
                if (cursor != null) {
                    cursor.close();
                }
            } catch (Exception e) {
                exists = false;
            }
        }
        return exists;
    }


    private String getSelectionForCondition(String selection, String condition) {
        StringBuilder builder = new StringBuilder();
        builder.append(selection);
        builder.append("='");
        builder.append(condition);
        builder.append("'");
        return builder.toString();
    }

}
