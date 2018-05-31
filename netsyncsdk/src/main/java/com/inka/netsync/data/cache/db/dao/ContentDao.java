package com.inka.netsync.data.cache.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.v4.util.ArrayMap;

import com.inka.netsync.data.cache.db.DatabaseCacheOpenHelper;
import com.inka.netsync.data.cache.db.MetaData;
import com.inka.netsync.data.cache.db.SqlStorage;
import com.inka.netsync.data.cache.db.model.ContentCacheEntry;
import com.inka.netsync.logs.LogUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by birdgang on 2018. 1. 12..
 */
public class ContentDao {

    private final String TAG = "ContentDao";

    private SQLiteDatabase db;
    private DatabaseCacheOpenHelper databaseOpenHelper = null;

    private static volatile ContentDao defaultInstance;

    public static ContentDao getDefault() {
        if (defaultInstance == null) {
            synchronized (ContentDao.class) {
                if (defaultInstance == null) {
                    defaultInstance = new ContentDao();
                }
            }
        }
        return defaultInstance;
    }

    private ContentDao() {
        databaseOpenHelper = DatabaseCacheOpenHelper.getDefault();
    }

    public ArrayMap<String, ContentCacheEntry> loadContentMaps() {
        ArrayMap<String, ContentCacheEntry> contentsMaps = new ArrayMap<String, ContentCacheEntry>();
        Cursor cursor = null;
        try {
            db = databaseOpenHelper.getReadableDatabase();
            cursor = db.query(MetaData.CONTENT_TABLE, null, null, null, null, null, MetaData.ContentColumns.CONTENT_NAME, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    ContentCacheEntry contentEntry = new ContentCacheEntry(cursor);
                    contentsMaps.put(contentEntry.getContentFilePath(), contentEntry);
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
        return contentsMaps;
    }


    public ArrayMap<String, ContentCacheEntry> loadContentMapsKeyFilePath() {
        ArrayMap<String, ContentCacheEntry> contentsMaps = new ArrayMap<String, ContentCacheEntry>();
        Cursor cursor = null;
        try {
            db = databaseOpenHelper.getReadableDatabase();
            cursor = db.query(MetaData.CONTENT_TABLE, null, null, null, null, null, MetaData.ContentColumns.CONTENT_FILE_PATH, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    ContentCacheEntry contentEntry = new ContentCacheEntry(cursor);
                    contentsMaps.put(contentEntry.getContentFilePath(), contentEntry);
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
        return contentsMaps;
    }


    public ArrayList<ContentCacheEntry> loadContentList () {
        ArrayList<ContentCacheEntry> contentEntries = new ArrayList<ContentCacheEntry>();
        Cursor cursor = null;
        try {
            db = databaseOpenHelper.getReadableDatabase();
            cursor = db.query(MetaData.CONTENT_TABLE, null, null, null, null, null, MetaData.ContentColumns.CONTENT_NAME, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    ContentCacheEntry contentEntry = new ContentCacheEntry(cursor);
                    int contentId = contentEntry.getContentId();
                    LogUtil.INSTANCE.info("birdgangcontentdao" , "loadContentMaps > contentId : " + contentId);
                    if (contentId > 0) {
                        LogUtil.INSTANCE.info(TAG, "contentEntry.toString() : " + contentEntry.toString());
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


    public ArrayList<ContentCacheEntry> loadContentListOrderById () {
        ArrayList<ContentCacheEntry> contentEntries = new ArrayList<ContentCacheEntry>();
        Cursor cursor = null;
        try {
            db = databaseOpenHelper.getReadableDatabase();
            cursor = db.query(MetaData.CONTENT_TABLE, null, null, null, null, null, MetaData.ContentColumns.CONTENT_ID, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    ContentCacheEntry contentEntry = new ContentCacheEntry(cursor);
                    contentEntries.add(contentEntry);
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


    public long replaceContent(ContentCacheEntry content) throws SQLiteException {
        long result = -1;
        try {
            db = databaseOpenHelper.getWritableDatabase();
            if (null == db) {
                throw new SQLiteException();
            }
            result = db.replace(MetaData.CONTENT_TABLE, null, content.toContentValues());
        } catch(Exception e) {
            LogUtil.INSTANCE.error(getClass().getSimpleName(), e);
        }
        return result;
    }


    public long insertContent(ContentCacheEntry content) throws SQLiteException {
        long result = -1;
        try {
            db = databaseOpenHelper.getWritableDatabase();
            if (null == db) {
                throw new SQLiteException();
            }
            int contentId = content.getContentId();
            boolean hasExist = checkRecordExist(db, new String[]{MetaData.ContentColumns.CONTENT_ID}, new String[]{ String.valueOf(contentId) });
            if (!hasExist) {
                result = db.insert(MetaData.CONTENT_TABLE, null, content.toContentValues());
            }
            LogUtil.INSTANCE.debug("birdgangdatabase", "content DAO insert > hasExist : " + hasExist + ", result : " + result);
        } catch(Exception e) {
            LogUtil.INSTANCE.error(getClass().getSimpleName(), e);
        }
        return result;
    }



    public int bulkInsertContents (List<ContentCacheEntry> contentEntries) {
        try {
            db = databaseOpenHelper.getWritableDatabase();
            ContentValues[] contentValue = new ContentValues[contentEntries.size()];

            for (int i = 0; i < contentEntries.size(); i++) {
                ContentCacheEntry contentEntry = contentEntries.get(i);
                contentValue[i] = contentEntry.toContentValues();
                LogUtil.INSTANCE.info("db", "before > : contentEntry.getMessage()" + contentEntry.getContentId());
            }

            return insertContents(db, contentValue);
        } catch(Exception e) {
            LogUtil.INSTANCE.error(getClass().getSimpleName(), e);
        }

        return 0;
    }


    /**
     * ANR issue
     * @param db
     * @param allValues
     * @return
     */
    private int insertContents(SQLiteDatabase db, ContentValues[] allValues) {
        int rowsAdded = 0;
        long rowId;
        ContentValues values;

        try {
            db.beginTransaction();

            for (ContentValues initialValues : allValues) {
                values = initialValues == null ? new ContentValues() : new ContentValues(initialValues);
                rowId = insertContent(db, values);

                if (rowId > 0) {
                    rowsAdded++;
                }
            }

            db.setTransactionSuccessful();
        } catch (SQLException se) {
            LogUtil.INSTANCE.error(getClass().getSimpleName(), se);
        } catch (Exception e) {
            LogUtil.INSTANCE.error(getClass().getSimpleName(), e);
        } finally {
            db.endTransaction();
        }

        return rowsAdded;
    }

    private long insertContent(SQLiteDatabase db, ContentValues values) throws Exception {
        if (!values.containsKey(MetaData.ContentColumns.CONTENT_ID)) {
            throw new IllegalArgumentException("Missing message column '" + MetaData.ContentColumns.CONTENT_ID + "'");
        }

        return db.insert(MetaData.CONTENT_TABLE, null, values);
    }


    public ContentCacheEntry getContentByPath(String contentFilePath)  {
        ContentCacheEntry contentEntry = null;
        Cursor cursor = null;
        try {
            db = databaseOpenHelper.getReadableDatabase();
            cursor = db.query(MetaData.CONTENT_TABLE, null, getSelectionForCondition(MetaData.ContentColumns.CONTENT_FILE_PATH, contentFilePath), null, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    contentEntry = new ContentCacheEntry(cursor);
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
        return contentEntry;
    }

    public ContentCacheEntry getContent(int contentId)  {
        ContentCacheEntry contentEntry = null;
        Cursor cursor = null;
        try {
            db = databaseOpenHelper.getReadableDatabase();

            String sql = String.format(SqlStorage.SELECT_SQL_CONTENT, "WHERE contentId="+contentId);
            cursor = db.rawQuery(sql, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    contentEntry = new ContentCacheEntry(cursor);
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
        return contentEntry;
    }


    public ContentCacheEntry getContentByName (String name)  {
        ContentCacheEntry contentEntry = null;
        Cursor cursor = null;
        try {
            db = databaseOpenHelper.getReadableDatabase();
            cursor = db.query(MetaData.CONTENT_TABLE, null, getSelectionForCondition(MetaData.ContentColumns.CONTENT_NAME, name), null, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    contentEntry = new ContentCacheEntry(cursor);
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
        return contentEntry;
    }


    public ArrayList<ContentCacheEntry> getIsFavoriteContentListUsingCategoryId(int categoryId) throws Exception {
        ArrayList<ContentCacheEntry> contentEntries = new ArrayList<ContentCacheEntry>();
        Cursor cursor = null;
        try {
            db = databaseOpenHelper.getReadableDatabase();
            String condition1 = getSelectionForCondition(MetaData.ContentColumns.CATEGORY_ID, String.valueOf(categoryId));
            String condition2 = getSelectionForCondition(MetaData.ContentColumns.IS_FAVORITE_CONTENT, String.valueOf(1));

            cursor = db.query(MetaData.CONTENT_TABLE, null, condition1 + " AND " + condition2, null, null, null, MetaData.ContentColumns.CONTENT_NAME, null);
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


    public ArrayList<ContentCacheEntry> getIsFavoriteContentArrayList() throws Exception {
        ArrayList<ContentCacheEntry> contentEntries = new ArrayList<ContentCacheEntry>();
        Cursor cursor = null;
        try {
            db = databaseOpenHelper.getReadableDatabase();
            String sql = String.format(SqlStorage.SELECT_SQL_CONTENT, "WHERE isFavoriteContent = 1");
            db.rawQuery(sql, null);
            cursor = db.rawQuery(sql, null);
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


    public ArrayList<ContentCacheEntry> getContentArrayListUsingCategoryId(int categoryId) throws Exception {
        ArrayList<ContentCacheEntry> contentEntries = new ArrayList<ContentCacheEntry>();
        Cursor cursor = null;
        try {
            db = databaseOpenHelper.getReadableDatabase();
            cursor = db.query(MetaData.CONTENT_TABLE, null, getSelectionForCondition(MetaData.ContentColumns.CATEGORY_ID, String.valueOf(categoryId)), null, null, null, MetaData.ContentColumns.CONTENT_NAME, null);
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


    public ArrayList<ContentCacheEntry> getRecentlyPlayedContentList () {
        ArrayList<ContentCacheEntry> contentEntries = new ArrayList<ContentCacheEntry>();
        Cursor cursor = null;
        try {
            db = databaseOpenHelper.getReadableDatabase();
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


    public long updateContent(final ContentCacheEntry contentEntry) {
        long _id = -1;
        try {
            db = databaseOpenHelper.getWritableDatabase();
            //LogUtil.INSTANCE.debug("birdgangdatabase", "updateContent > contentEntry.toString() : " + contentEntry.toString());
            return updateContent(db, contentEntry.toContentValues(), contentEntry.getContentId());
        } catch (Exception e) {
            LogUtil.INSTANCE.error(getClass().getSimpleName(), e);
        }
        return _id;
    }


    public int updateContent(int contentId, String column, Object object) {
        if (contentId < 0) {
            return -1;
        }

        ContentValues values = new ContentValues();
        switch (column) {
            case MetaData.ContentColumns.LICENSE_INFO:
                if (object != null)
                    values.put(MetaData.ContentColumns.LICENSE_INFO, (String) object);
                break;
            case MetaData.ContentColumns.PLAY_DATE:
                if (object != null) {
                    values.put(MetaData.ContentColumns.PLAY_DATE, (String) object);
                }
                //LogUtil.INSTANCE.debug("birdgangplaydate", "updateContent > column :" + column + " , object : " + object.toString() + " , contentId : " + contentId);
                break;
            case MetaData.ContentColumns.CONTENT_LAST_PLAY_TIME:
                if (object != null) {
                    values.put(MetaData.ContentColumns.CONTENT_LAST_PLAY_TIME, (String) object);
                }
                break;
            case MetaData.ContentColumns.IS_FAVORITE_CONTENT:
                if (object != null)
                    values.put(MetaData.ContentColumns.IS_FAVORITE_CONTENT, (String) object);
                break;
            default:
                return -1;
        }
        return db.update(MetaData.CONTENT_TABLE, values, MetaData.ContentColumns.CONTENT_ID + "=?", new String[] { String.valueOf(contentId) });
    }


    public void removeContent(String localPath) {
        try {
            db.delete(MetaData.CONTENT_TABLE, MetaData.ContentColumns.CONTENT_FILE_PATH + "=?", new String[]{localPath});
        } catch (SQLiteException e) {
            // Some devices have weird issues with FTS table
        }
    }

    public void removeContents(Collection<String> filePaths) {
        db.beginTransaction();
        try {
            for (String path : filePaths) {
                removeContent(path);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public void removeContentsWrappers(Collection<ContentCacheEntry> contentEntries) {
        db.beginTransaction();
        try {
            for (ContentCacheEntry contentEntry : contentEntries) {
                removeContent(contentEntry.getContentFilePath());
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }


    private int updateContents(SQLiteDatabase db, Collection<ContentCacheEntry> contentEntries) {
        int rowsAdded = 0;
        long rowId;
        try {
            db.beginTransaction();
            for (ContentCacheEntry contentEntry : contentEntries) {
                rowId = updateContent(db, contentEntry.toContentValues(), contentEntry.getContentId());
                if (rowId > 0) {
                    rowsAdded++;
                }
            }
            db.setTransactionSuccessful();
        } catch (SQLException se) {
            LogUtil.INSTANCE.error(getClass().getSimpleName(), se);
        } catch (Exception e) {
            LogUtil.INSTANCE.error(getClass().getSimpleName(), e);
        } finally {
            db.endTransaction();
        }
        return rowsAdded;
    }


    private long updateContent(SQLiteDatabase db, ContentValues values, int contentId) throws Exception {
        int result = -1;
        try {
            result = db.update(MetaData.CONTENT_TABLE, values, MetaData.ContentColumns.CONTENT_ID + "=" + "'"+contentId+"'", null);
            if (result <= 0) {
                LogUtil.INSTANCE.debug("birdgangdatabase", "fail update > result : " + result + ", contentId : " + contentId + " , updateContent :" + values.toString());
            }
        } catch(Exception e) {
            LogUtil.INSTANCE.error(getClass().getSimpleName(), e);
        }
        return result;
    }


    public int bulkDeleteContentsByPath (Collection<String> filePaths) {
        LogUtil.INSTANCE.info("birdgangdatabase", "bulkDeleteContentsByPath > filePaths.size() : " + filePaths.size());

        try {
            db = databaseOpenHelper.getWritableDatabase();
            return deleteContents(db, filePaths);
        } catch(Exception e) {
            LogUtil.INSTANCE.error(getClass().getSimpleName(), e);
        }
        return 0;
    }

    private int deleteContents(SQLiteDatabase db, Collection<String> paths) {
        int rowsAdded = 0;
        long rowId;

        try {
            db.beginTransaction();

            for (String path : paths) {
                rowId = deleteContent(db, path);
                if (rowId > 0) {
                    rowsAdded++;
                }
            }

            db.setTransactionSuccessful();
        } catch (SQLException se) {
            LogUtil.INSTANCE.error(getClass().getSimpleName(), se);
        } catch (Exception e) {
            LogUtil.INSTANCE.error(getClass().getSimpleName(), e);
        } finally {
            db.endTransaction();
        }

        return rowsAdded;
    }


    public int deleteContent(SQLiteDatabase db, String filePath) {
        int result = -1;
        try {
            result = db.delete(MetaData.CONTENT_TABLE, MetaData.ContentColumns.CONTENT_FILE_PATH + "=" + "'"+ filePath +"'", null);
        } catch(Exception e) {
            LogUtil.INSTANCE.error(getClass().getSimpleName(), e);
        }
        return result;
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
            cursor = db.query(MetaData.CONTENT_TABLE, null, sb.toString(), null, null, null, null);
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
