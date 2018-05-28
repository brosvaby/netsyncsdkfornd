package com.inka.netsync.data.cache.db.migration;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.inka.netsync.common.utils.DateTimeUtil;
import com.inka.netsync.data.cache.db.MetaData;
import com.inka.netsync.data.cache.db.model.ContentCacheEntry;
import com.inka.netsync.data.cache.db.model.RecentlyCacheEntry;
import com.inka.netsync.logs.LogUtil;

import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by birdgang on 2018. 3. 2..
 */

public class MigrationRecentlyTable extends MigrationData {

    public synchronized void migrationSeperateTableFromV2ToV3RecentlyData(SQLiteDatabase db) {
        try {
            createRecentlyTableQuery(db, MetaData.RECENTLY_TABLE);

            List<ContentCacheEntry> contentEntries = loadRecentlyPlayedContentList(db);
            if (null == contentEntries || contentEntries.size() <= 0) {
                LogUtil.INSTANCE.info("birdgangdatabase", "migrationSeperateTableFromV2ToV3RecentlyData > null == contentEntries || contentEntries.size() <= 0");
                return;
            }

            LogUtil.INSTANCE.info("birdgangdatabase", "migrationSeperateTableFromV2ToV3RecentlyData > contentEntries size : " + contentEntries.size());
            for (ContentCacheEntry contentEntry : contentEntries) {
                long result = insertRecentlyPlay(db, new RecentlyCacheEntry(contentEntry));
                LogUtil.INSTANCE.info("birdgangdatabase", "migrationSeperateTableFromV2ToV3RecentlyData > result : " + result);
            }
        } catch (Exception e) {
            LogUtil.INSTANCE.error("birdgangdatabase", e);
        }
    }


    /***
     * load
     */

    public ArrayList<ContentCacheEntry> loadRecentlyPlayedContentList (SQLiteDatabase db) {
        ArrayList<ContentCacheEntry> contentEntries = new ArrayList<ContentCacheEntry>();
        Cursor cursor = null;
        try {
            String condition = MetaData.ContentColumns.PLAY_DATE + " != " + "''";
            cursor = db.query(MetaData.CONTENT_TABLE, null, condition, null, null, null, MetaData.ContentColumns.CONTENT_NAME, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    ContentCacheEntry contentEntry = new ContentCacheEntry(cursor);
                    contentEntries.add(contentEntry);
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


    /****
     * insert
     */

    public long insertRecentlyPlay (SQLiteDatabase db, RecentlyCacheEntry recentlyEntry) throws SQLiteException {
        long result = -1;
        try {
            LogUtil.INSTANCE.info("birdgangdatabase", "insertFavorite > recentlyEntry.toString() : " + recentlyEntry.toString());

            String contentName = recentlyEntry.getContentName();
            boolean hasExist = checkRecordExist(db, MetaData.RECENTLY_TABLE, new String[] { MetaData.RecentlyColumns.CONTENT_NAME}, new String[]{ contentName });
            if (!hasExist) {
                result = db.insert(MetaData.RECENTLY_TABLE, null, recentlyEntry.toContentValues());
            } else {
                SimpleDateFormat detailedDate = DateTimeUtil.getSimpleDateFormat();
                updateRecentlyPlay(db, contentName, MetaData.RecentlyColumns.RECENTLY_DATE, detailedDate.format(recentlyEntry.getPlayDateToDate()));
            }
            LogUtil.INSTANCE.debug("birdgangdatabase", "insertRecentlyPlay DAO insert > hasExist : " + hasExist + ", result : " + result + " , contentName : " + contentName + " recentlyEntry.toString() : " + recentlyEntry.toString());
        } catch(Exception e) {
            LogUtil.INSTANCE.error(getClass().getSimpleName(), e);
        }
        return result;
    }


    /****
     *
     * @param db
     * @param contentName
     * @param column
     * @param object
     * @return
     */
    public int updateRecentlyPlay(SQLiteDatabase db, String contentName, String column, Object object) {
        if (StringUtils.isBlank(contentName)) {
            return -1;
        }

        ContentValues values = new ContentValues();
        switch (column) {
            case MetaData.RecentlyColumns.RECENTLY_DATE:
                if (object != null) {
                    values.put(MetaData.RecentlyColumns.RECENTLY_DATE, (String) object);
                }
                break;
            default:
                return -1;
        }
        return db.update(MetaData.RECENTLY_TABLE, values, MetaData.RecentlyColumns.CONTENT_NAME + "=?", new String[] { contentName });
    }


    /***
     *
     *
     * @param db
     * @param tableName
     */
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
