package com.inka.netsync.data.cache.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.inka.netsync.common.utils.DateTimeUtil;
import com.inka.netsync.data.cache.db.DatabaseCacheOpenHelper;
import com.inka.netsync.data.cache.db.MetaData;
import com.inka.netsync.data.cache.db.model.RecentlyCacheEntry;
import com.inka.netsync.logs.LogUtil;

import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by birdgang on 2018. 1. 15..
 */

public class PlayedListDao {

    private final String TAG = "PlayedListDao";

    private SQLiteDatabase db;
    private DatabaseCacheOpenHelper databaseOpenHelper = null;

    private static volatile PlayedListDao defaultInstance;

    public static PlayedListDao getDefault() {
        if (defaultInstance == null) {
            synchronized (PlayedListDao.class) {
                if (defaultInstance == null) {
                    defaultInstance = new PlayedListDao();
                }
            }
        }
        return defaultInstance;
    }


    private PlayedListDao() {
        databaseOpenHelper = DatabaseCacheOpenHelper.getDefault();
    }


    public ArrayList<RecentlyCacheEntry> getRecentlyPlayedList() throws Exception {
        ArrayList<RecentlyCacheEntry> recentlyCacheEntries = new ArrayList<RecentlyCacheEntry>();
        Cursor cursor = null;
        try {
            db = databaseOpenHelper.getReadableDatabase();
            cursor = db.query(MetaData.RECENTLY_TABLE, null, null, null, null, null, MetaData.RecentlyColumns.RECENTLY_DATE + " desc", null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    RecentlyCacheEntry recentlyEntry = new RecentlyCacheEntry(cursor);
                    int contentId = recentlyEntry.getContentId();
                    LogUtil.INSTANCE.info(TAG , "getIsRecentlyPlayedContentArrayList > contentId : " + contentId);
                    if (contentId > 0) {
                        LogUtil.INSTANCE.info(TAG, "recentlyEntry.toString() : " + recentlyEntry.toString());
                        recentlyCacheEntries.add(recentlyEntry);
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
        return recentlyCacheEntries;
    }

    public long insertRecentlyPlay (RecentlyCacheEntry recentlyCacheEntry) throws SQLiteException {
        long result = -1;
        try {
            db = databaseOpenHelper.getWritableDatabase();
            if (null == db) {
                throw new SQLiteException();
            }
            String contentName = recentlyCacheEntry.getContentName();
            boolean hasExist = checkRecordExist(db, new String[] { MetaData.RecentlyColumns.CONTENT_NAME}, new String[]{ contentName });
            if (!hasExist) {
                result = db.insert(MetaData.RECENTLY_TABLE, null, recentlyCacheEntry.toContentValues());
            } else {
                SimpleDateFormat detailedDate = DateTimeUtil.getSimpleDateFormat();
                updateRecentlyPlay(contentName, MetaData.RecentlyColumns.RECENTLY_DATE, detailedDate.format(recentlyCacheEntry.getPlayDateToDate()));
            }
            LogUtil.INSTANCE.debug("birdgangdb", "insertRecentlyPlay DAO insert > hasExist : " + hasExist + ", result : " + result + " , contentName : " + contentName + " recentlyCacheEntry.toString() : " + recentlyCacheEntry.toString());
        } catch(Exception e) {
            LogUtil.INSTANCE.error(getClass().getSimpleName(), e);
        }
        return result;
    }


    public int updateRecentlyPlay(String contentName, String column, Object object) {
        if (StringUtils.isBlank(contentName)) {
            return -1;
        }

        ContentValues values = new ContentValues();
        switch (column) {
            case MetaData.RecentlyColumns.RECENTLY_DATE:
                if (object != null) {
                    values.put(MetaData.RecentlyColumns.RECENTLY_DATE, (String) object);
                }
                LogUtil.INSTANCE.debug("birdgangdb", "updateContent > column :" + column + " , object : " + object.toString() + " , contentName : " + contentName);
                break;
            default:
                return -1;
        }
        LogUtil.INSTANCE.debug("birdgangdatabase", "updateContent > contentName : " + contentName + ", column :" + column + " , object : " + object.toString());
        return db.update(MetaData.RECENTLY_TABLE, values, MetaData.RecentlyColumns.CONTENT_NAME + "=?", new String[] { contentName });
    }


    public int deleteFavoriteById(int favoriteId) {
        int result = -1;
        try {
            db = databaseOpenHelper.getWritableDatabase();
            result = db.delete(MetaData.RECENTLY_TABLE, MetaData.FavoriteColumns.FAVORITE_ID + "=" + "'"+ String.valueOf(favoriteId) +"'", null);
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
            result = db.delete(MetaData.RECENTLY_TABLE, MetaData.FavoriteColumns.CONTENT_NAME + "=" + "'"+ contentName +"'", null);
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
            result = db.delete(MetaData.RECENTLY_TABLE, MetaData.FavoriteColumns.CONTENT_ID + "=" + "'"+ String.valueOf(contentId) +"'", null);
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
            hasExist = checkRecordExist(db, new String[]{MetaData.RecentlyColumns.CONTENT_NAME}, new String[]{ contentName });
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
            hasExist = checkRecordExist(db, new String[]{MetaData.RecentlyColumns.CONTENT_ID}, new String[]{ String.valueOf(contentId) });
            LogUtil.INSTANCE.debug("db", "checkFavoriteByContentId > result : " + hasExist);
        } catch(Exception e) {
            LogUtil.INSTANCE.error(getClass().getSimpleName(), e);
        }
        return hasExist;
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
            cursor = db.query(MetaData.RECENTLY_TABLE, null, sb.toString(), null, null, null, null);
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
