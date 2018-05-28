package com.inka.netsync.data.cache.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.inka.netsync.data.cache.db.DatabaseCacheOpenHelper;
import com.inka.netsync.data.cache.db.MetaData;
import com.inka.netsync.data.cache.db.model.LMSCacheEntry;
import com.inka.netsync.logs.LogUtil;

/**
 * Created by birdgang on 2018. 1. 15..
 */
public class LMSDao {

    private final String TAG = "LMSDao";

    private SQLiteDatabase db;
    private DatabaseCacheOpenHelper databaseOpenHelper = null;

    private static volatile LMSDao defaultInstance;

    public static LMSDao getDefault() {
        if (defaultInstance == null) {
            synchronized (LMSDao.class) {
                if (defaultInstance == null) {
                    defaultInstance = new LMSDao();
                }
            }
        }
        return defaultInstance;
    }

    private LMSDao() {
        databaseOpenHelper = DatabaseCacheOpenHelper.getDefault();
    }


    public LMSCacheEntry getEntryByContentName (String contentName)  {
        LMSCacheEntry lmsEntry = null;
        Cursor cursor = null;
        try {
            db = databaseOpenHelper.getReadableDatabase();
            cursor = db.query(MetaData.LMS_TABLE, null, getSelectionForCondition(MetaData.LMSColumns.CONTENT_NAME, contentName), null, null, null, MetaData.LMSColumns.LMS_ID, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    lmsEntry = new LMSCacheEntry(cursor);
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
        return lmsEntry;
    }


    public LMSCacheEntry getEntryByContentPath (String contentPath)  {
        LMSCacheEntry lmsEntry = null;
        Cursor cursor = null;
        try {
            db = databaseOpenHelper.getReadableDatabase();
            cursor = db.query(MetaData.LMS_TABLE, null, getSelectionForCondition(MetaData.LMSColumns.CONTENT_FILE_PATH, contentPath), null, null, null, MetaData.LMSColumns.LMS_ID, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    lmsEntry = new LMSCacheEntry(cursor);
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
        return lmsEntry;
    }


    public LMSCacheEntry getEntryByContentId (int id)  {
        LMSCacheEntry lmsEntry = null;
        Cursor cursor = null;
        try {
            db = databaseOpenHelper.getReadableDatabase();
            cursor = db.query(MetaData.LMS_TABLE, null, getSelectionForCondition(MetaData.LMSColumns.CONTENT_ID, String.valueOf(id)), null, null, null, MetaData.LMSColumns.LMS_ID, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    lmsEntry = new LMSCacheEntry(cursor);
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
        return lmsEntry;
    }

    public long insertAndUpdateSync (LMSCacheEntry lmsEntry) throws SQLiteException {
        long result = -1;
        try {
            db = databaseOpenHelper.getWritableDatabase();
            if (null == db) {
                throw new SQLiteException();
            }
            int contentId = lmsEntry.getContentId();

            LogUtil.INSTANCE.debug("birdgangdatabase", "insertAndUpdateSync > contentId : " + contentId);
            boolean hasExist = checkRecordExist(db, new String[] { MetaData.LMSColumns.CONTENT_ID}, new String[]{ String.valueOf(contentId) });
            LogUtil.INSTANCE.debug("birdgangdatabase", "insertLms > hasExist : " + hasExist);

            if (!hasExist) {
                result = db.insert(MetaData.LMS_TABLE, null, lmsEntry.toContentValues());
            } else {
                result = updateLms(lmsEntry);
            }
        } catch(Exception e) {
            LogUtil.INSTANCE.error(getClass().getSimpleName(), e);
        }
        return result;
    }


    public long updateLms(final LMSCacheEntry lmsEntry) {
        long _id = -1;
        try {
            db = databaseOpenHelper.getWritableDatabase();
            LogUtil.INSTANCE.debug("birdgangdatabase", "updateLms > lmsEntry.toString() : " + lmsEntry.toString());
            return db.update(MetaData.LMS_TABLE, lmsEntry.toContentValues(), MetaData.LMSColumns.CONTENT_ID + "=" + "'"+lmsEntry.getContentId()+"'", null);
        } catch (Exception e) {
            LogUtil.INSTANCE.error(getClass().getSimpleName(), e);
        }
        return _id;
    }


    private long updateLms(SQLiteDatabase db, ContentValues values, int lmsId) throws Exception {
        return db.update(MetaData.LMS_TABLE, values, MetaData.LMSColumns.LMS_ID + "=" + "'"+lmsId+"'", null);
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
            cursor = db.query(MetaData.LMS_TABLE, null, sb.toString(), null, null, null, null);
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
