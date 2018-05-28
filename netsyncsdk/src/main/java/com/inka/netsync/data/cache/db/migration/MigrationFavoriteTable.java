package com.inka.netsync.data.cache.db.migration;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.inka.netsync.data.cache.db.MetaData;
import com.inka.netsync.data.cache.db.SqlStorage;
import com.inka.netsync.data.cache.db.model.ContentCacheEntry;
import com.inka.netsync.data.cache.db.model.FavoriteCacheEntry;
import com.inka.netsync.logs.LogUtil;

import java.util.ArrayList;

/**
 * Created by birdgang on 2018. 3. 2..
 */

public class MigrationFavoriteTable extends MigrationData {

    private final String TAG = "MigrationFavoriteTable";

    public synchronized void migrationSeperateTableFromV2ToV3FavoriteData(SQLiteDatabase db) {
        try {
            createFavoriteTableQuery(db, MetaData.FAVORITE_TABLE);

            ArrayList<ContentCacheEntry> contentEntries = loadFavoriteContentArrayList(db);
            if (null == contentEntries || contentEntries.size() <= 0) {
                LogUtil.INSTANCE.info("birdgangdatabase", "migrationFromV2ToV3FavoriteData > null == contentEntries || contentEntries.size() <= 0");
                return;
            }

            LogUtil.INSTANCE.info("birdgangdatabase", "migrationFavoriteData > contentEntries size : " + contentEntries.size());
            for (ContentCacheEntry contentEntry : contentEntries) {
                long result = insertFavorite(db, new FavoriteCacheEntry(contentEntry));
                LogUtil.INSTANCE.info("birdgangdatabase", "migrationFavoriteData > result : " + result);
            }
        } catch (Exception e) {
            LogUtil.INSTANCE.error("birdgangdatabase", e);
        }
    }


    /***
     * load
     */
    public ArrayList<ContentCacheEntry> loadFavoriteContentArrayList(SQLiteDatabase db) {
        ArrayList<ContentCacheEntry> contentEntries = new ArrayList<ContentCacheEntry>();
        Cursor cursor = null;
        try {
            String sql = String.format(SqlStorage.SELECT_SQL_CONTENT, "WHERE isFavoriteContent = 1");
            db.rawQuery(sql, null);
            cursor = db.rawQuery(sql, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int contentId = cursor.getInt(cursor.getColumnIndexOrThrow(MetaData.ContentColumns.CONTENT_ID));
                    String playDate = cursor.getString(cursor.getColumnIndexOrThrow(MetaData.ContentColumns.PLAY_DATE));
                    String contentDownloadDate = cursor.getString(cursor.getColumnIndexOrThrow(MetaData.ContentColumns.CONTENT_DOWNLOAD_DATE));
                    String contentName = cursor.getString(cursor.getColumnIndexOrThrow(MetaData.ContentColumns.CONTENT_NAME));
                    String contentFilePath = cursor.getString(cursor.getColumnIndexOrThrow(MetaData.ContentColumns.CONTENT_FILE_PATH));
                    String contentLastPlayTime = cursor.getString(cursor.getColumnIndexOrThrow(MetaData.ContentColumns.CONTENT_LAST_PLAY_TIME));
                    int isFavoriteContent = cursor.getInt(cursor.getColumnIndexOrThrow(MetaData.ContentColumns.IS_FAVORITE_CONTENT));
                    String licenseInfo = cursor.getString(cursor.getColumnIndexOrThrow(MetaData.ContentColumns.LICENSE_INFO));
                    String serial = cursor.getString(cursor.getColumnIndexOrThrow(MetaData.ContentColumns.SERIAL));

                    ContentCacheEntry contentCacheEntry = new ContentCacheEntry();
                    contentCacheEntry.setContentId(contentId);
                    contentCacheEntry.setPlayDate(playDate);
                    contentCacheEntry.setContentDownloadDate(contentDownloadDate);
                    contentCacheEntry.setContentName(contentName);
                    contentCacheEntry.setContentFilePath(contentFilePath);
                    contentCacheEntry.setContentLastPlayTime(contentLastPlayTime);
                    contentCacheEntry.setIsFavoriteContent(isFavoriteContent);
                    contentCacheEntry.setLicenseInfo(licenseInfo);
                    contentCacheEntry.setSerial(serial);

                    contentEntries.add(contentCacheEntry);
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
    public long insertFavorite (SQLiteDatabase db, FavoriteCacheEntry favoriteEntry) throws SQLiteException {
        long result = -1;
        try {
            LogUtil.INSTANCE.info("birdgangdatabase", "insertFavorite > favoriteEntry.toString() : " + favoriteEntry.toString());

            int contentId = favoriteEntry.getContentId();
            boolean hasExist = checkRecordExist(db, MetaData.FAVORITE_TABLE, new String[]{MetaData.FavoriteColumns.CONTENT_ID}, new String[]{ String.valueOf(contentId) });
            if (!hasExist) {
                result = db.insert(MetaData.FAVORITE_TABLE, null, favoriteEntry.toContentValues());
            }
            LogUtil.INSTANCE.debug("birdgangdatabase", "favorite DAO insert > hasExist : " + hasExist + ", result : " + result);
        } catch(Exception e) {
            LogUtil.INSTANCE.error(getClass().getSimpleName(), e);
        }
        return result;
    }


    /***
     *
     *
     * @param db
     * @param tableName
     */
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


}
