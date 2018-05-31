package com.inka.netsync.data.cache.db.migration;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.inka.netsync.data.cache.db.MetaData;
import com.inka.netsync.data.cache.db.SqlStorage;
import com.inka.netsync.data.cache.db.model.ContentCacheEntry;
import com.inka.netsync.logs.LogUtil;

import java.util.ArrayList;

/**
 * Created by birdgang on 2018. 3. 2..
 */
public class MigrationContentTable extends MigrationData {

    private final String TAG = "MigrationContentTable";


    public synchronized void migrationUpdateTableFromV2ToV3ContentData(SQLiteDatabase db) {
        try {
            createBackupTableFromV2ToV3ForMigration(db, MetaData.CONTENT_TABLE_BACKUP);

            ArrayList<ContentCacheEntry> contentEntries = loadContentListFromV2(db);
            MigrationContentManager.getDefault().setExistContentEntries(contentEntries);

            if (null == contentEntries || contentEntries.size() <= 0) {
                LogUtil.INSTANCE.info("birdgangdatabase", "migrationUpdateTableFromV2ToV3ContentData > null == contentEntries || contentEntries.size() <= 0");
                return;
            }

            LogUtil.INSTANCE.info("birdgangdatabase", "migrationUpdateTableFromV2ToV3ContentData > contentEntries size : " + contentEntries.size());
            for (ContentCacheEntry contentEntry : contentEntries) {
                long result = insertContentToTable(db, contentEntry, MetaData.CONTENT_TABLE_BACKUP);
                LogUtil.INSTANCE.info("birdgangdatabase", "migrationUpdateTableFromV2ToV3ContentData > result : " + result);
            }

            db.execSQL(SqlStorage.MIGRATION_DROP_CONTENT_TABLE);
            db.execSQL(SqlStorage.MIGRATION_RENAME_CONTENT_TABLE);
        } catch (Exception e) {
            LogUtil.INSTANCE.error("birdgangdatabase", e);
        }
    }


    public void createBackupTableFromV2ToV3ForMigration(SQLiteDatabase db, String tableName) {
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



    /***
     * load
     */
    private ArrayList<ContentCacheEntry> loadContentListFromV2(SQLiteDatabase db) {
        ArrayList<ContentCacheEntry> contentEntries = new ArrayList<ContentCacheEntry>();
        Cursor cursor = null;
        try {
            cursor = db.query(MetaData.CONTENT_TABLE, null, null, null, null, null, MetaData.ContentColumns.CONTENT_NAME, null);
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

                    LogUtil.INSTANCE.info("birdgangcontentdao" , "loadContentMaps > contentId : " + contentId);
                    if (contentId > 0) {
                        LogUtil.INSTANCE.info(TAG, "contentCacheEntry.toString() : " + contentCacheEntry.toString());
                        contentEntries.add(contentCacheEntry);
                    }
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
        return contentEntries;
    }


    /****
     * insert
     */
    private long insertContentToTable (SQLiteDatabase db, ContentCacheEntry contentCacheEntry, String tableName) throws SQLiteException {
        long result = -1;
        try {
            LogUtil.INSTANCE.info("birdgangdatabase", "tableName : " + tableName + " , insertContent > contentCacheEntry.toString() : " + contentCacheEntry.toString());

            int contentId = contentCacheEntry.getContentId();
            String filePath = contentCacheEntry.getContentFilePath();

            boolean hasExist = checkRecordExist(db, tableName, new String[]{MetaData.ContentColumns.CONTENT_FILE_PATH}, new String[]{ filePath });
            if (!hasExist) {
                result = db.insert(tableName, null, contentCacheEntry.toContentValues());
            }
            LogUtil.INSTANCE.debug("birdgangdatabase", "insertContent DAO insert > hasExist : " + hasExist + ", result : " + result);
        } catch(Exception e) {
            LogUtil.INSTANCE.error(getClass().getSimpleName(), e);
        }
        return result;
    }


}
