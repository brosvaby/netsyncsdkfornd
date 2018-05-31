package com.inka.netsync.data.cache.db.migration;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.inka.netsync.data.cache.db.MetaData;
import com.inka.netsync.data.cache.db.model.ContentCacheEntry;
import com.inka.netsync.logs.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by birdgang on 2018. 3. 2..
 */
public class MigrationData {

    /***
     *  common
     */
    protected boolean checkRecordExist(SQLiteDatabase db, String tableName, String[] keys, String [] values) throws SQLiteException {
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
            cursor = db.query(tableName, null, sb.toString(), null, null, null, null);
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


    /***
     *
     */
    protected ArrayList<ContentCacheEntry> loadContentListFromV3(SQLiteDatabase db) {
        ArrayList<ContentCacheEntry> contentEntries = new ArrayList<ContentCacheEntry>();
        Cursor cursor = null;
        try {
            cursor = db.query(MetaData.CONTENT_TABLE, null, null, null, null, null, MetaData.ContentColumns.CONTENT_NAME, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    ContentCacheEntry contentEntry = new ContentCacheEntry(cursor);
                    int contentId = contentEntry.getContentId();
                    LogUtil.INSTANCE.info("birdgangcontentdao" , "loadContentMaps > contentId : " + contentId);
                    if (contentId > 0) {
                        contentEntries.add(contentEntry);
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


    protected ContentCacheEntry getPrevContentById(int contentId)  {
        ContentCacheEntry resultContentCacheEntry = new ContentCacheEntry();

        try {
            List<ContentCacheEntry> contentCacheEntries = MigrationContentManager.getDefault().getExistContentEntries();
            LogUtil.INSTANCE.info("birdgangdatabase" , "getPrevContentById > contentId : " + contentId + " , contentCacheEntries size : " + contentCacheEntries.size());

            for (ContentCacheEntry contentCacheEntry : contentCacheEntries) {
                if (contentCacheEntry.getContentId() == contentId) {
                    resultContentCacheEntry = contentCacheEntry;
                }
            }
        } catch (Exception e) {
            LogUtil.INSTANCE.error("birdgangdatabase", e);
        }


        return resultContentCacheEntry;
    }

}
