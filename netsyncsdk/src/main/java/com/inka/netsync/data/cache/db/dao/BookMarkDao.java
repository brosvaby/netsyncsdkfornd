package com.inka.netsync.data.cache.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.inka.netsync.data.cache.db.DatabaseCacheOpenHelper;
import com.inka.netsync.data.cache.db.MetaData;
import com.inka.netsync.data.cache.db.SqlStorage;
import com.inka.netsync.data.cache.db.model.BookMarkCacheEntry;
import com.inka.netsync.logs.LogUtil;

import java.util.ArrayList;


public class BookMarkDao {

    private final String TAG = "BookMarkDao";

    private SQLiteDatabase db;
    private DatabaseCacheOpenHelper databaseOpenHelper = null;

    private static volatile BookMarkDao defaultInstance;

    public static BookMarkDao getDefault() {
        if (defaultInstance == null) {
            synchronized (BookMarkDao.class) {
                if (defaultInstance == null) {
                    defaultInstance = new BookMarkDao();
                }
            }
        }
        return defaultInstance;
    }


    private BookMarkDao() {
        databaseOpenHelper = DatabaseCacheOpenHelper.getDefault();
    }

    public BookMarkCacheEntry getBookmark (int bookmarkId) {
        BookMarkCacheEntry bookMarkCacheEntry = null;
        Cursor cursor = null;
        try {
            db = databaseOpenHelper.getReadableDatabase();
            cursor = db.query(MetaData.BOOKMARK_TABLE, null, getSelectionForCondition(MetaData.BookmarkColumns.BOOKMARK_ID, String.valueOf(bookmarkId)), null, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    bookMarkCacheEntry = new BookMarkCacheEntry(cursor);
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
        return bookMarkCacheEntry;
    }



    public BookMarkCacheEntry getBookmarkByContentName (String contentName) {
        BookMarkCacheEntry bookMarkCacheEntry = null;
        Cursor cursor = null;
        try {
            db = databaseOpenHelper.getReadableDatabase();
            cursor = db.query(MetaData.BOOKMARK_TABLE, null, getSelectionForCondition(MetaData.BookmarkColumns.BOOKMARK_CONTENT_NAME, contentName), null, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    bookMarkCacheEntry = new BookMarkCacheEntry(cursor);
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
        return bookMarkCacheEntry;
    }


    public ArrayList<BookMarkCacheEntry> getBookmarkArrayListUsingContentId(int contentId) throws Exception {
        ArrayList<BookMarkCacheEntry> bookMarkCacheEntries = new ArrayList<BookMarkCacheEntry>();
        Cursor cursor = null;
        try {
            String sql = String.format(SqlStorage.SELECT_SQL_BOOKMARK, "WHERE contentId="+contentId);
            LogUtil.INSTANCE.info("birdgangbookmark", "getBookmarkArrayListUsingContentId > sql : " + sql);
            db = databaseOpenHelper.getReadableDatabase();
            db.rawQuery(sql, null);
            cursor = db.rawQuery(sql, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    BookMarkCacheEntry bookMarkCacheEntry = new BookMarkCacheEntry(cursor);
                    bookMarkCacheEntries.add(bookMarkCacheEntry);
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
        return bookMarkCacheEntries;
    }


    public ArrayList<BookMarkCacheEntry> getBookmarkArrayListUsingContentPath(String path) throws Exception {
        ArrayList<BookMarkCacheEntry> bookMarkCacheEntries = new ArrayList<BookMarkCacheEntry>();
        Cursor cursor = null;
        try {
            String sql = String.format(SqlStorage.SELECT_SQL_BOOKMARK, "WHERE contentFilePath=" + "'" + path + "'");
            LogUtil.INSTANCE.info("birdgangbookmark", "getBookmarkArrayListUsingContentPath > sql : " + sql);
            db = databaseOpenHelper.getReadableDatabase();
            db.rawQuery(sql, null);
            cursor = db.rawQuery(sql, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    BookMarkCacheEntry bookMarkCacheEntry = new BookMarkCacheEntry(cursor);
                    bookMarkCacheEntries.add(bookMarkCacheEntry);
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
        return bookMarkCacheEntries;
    }



    public ArrayList<BookMarkCacheEntry> getBookmarkArrayListUsingContentName(String contentName) throws Exception {
        ArrayList<BookMarkCacheEntry> bookMarkCacheEntries = new ArrayList<BookMarkCacheEntry>();
        Cursor cursor = null;
        try {
            String sql = String.format(SqlStorage.SELECT_SQL_BOOKMARK, "WHERE contentName=" + "'" + contentName + "'");
            LogUtil.INSTANCE.info("birdgangbookmark", "getBookmarkArrayListUsingContentName > sql : " + sql);
            db = databaseOpenHelper.getReadableDatabase();
            db.rawQuery(sql, null);
            cursor = db.rawQuery(sql, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    BookMarkCacheEntry bookMarkCacheEntry = new BookMarkCacheEntry(cursor);
                    bookMarkCacheEntries.add(bookMarkCacheEntry);
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
        return bookMarkCacheEntries;
    }



    public ArrayList<BookMarkCacheEntry> loadBookMarkList () {
        ArrayList<BookMarkCacheEntry> bookMarkCacheEntries = new ArrayList<BookMarkCacheEntry>();
        Cursor cursor = null;
        try {
            db = databaseOpenHelper.getReadableDatabase();
            cursor = db.query(MetaData.BOOKMARK_TABLE, null, null, null, null, null, MetaData.BookmarkColumns.BOOKMARK_LOCATION, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    BookMarkCacheEntry bookMarkCacheEntry = new BookMarkCacheEntry(cursor);
                    bookMarkCacheEntries.add(bookMarkCacheEntry);
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
        return bookMarkCacheEntries;
    }


    public long insertBookmark (BookMarkCacheEntry bookMarkCacheEntry) throws SQLiteException {
        long result = -1;
        try {
            db = databaseOpenHelper.getWritableDatabase();
            if (null == db) {
                throw new SQLiteException();
            }
            int bookmarkId = bookMarkCacheEntry.getBookmarkId();
            boolean hasExist = checkRecordExist(db, new String[]{MetaData.BookmarkColumns.BOOKMARK_ID}, new String[]{ String.valueOf(bookmarkId) });
            if (!hasExist) {
                result = db.insert(MetaData.BOOKMARK_TABLE, null, bookMarkCacheEntry.toContentValues());
            }
            LogUtil.INSTANCE.debug("db", "bookmark DAO insert > hasExist : " + hasExist + ", result : " + result);
        } catch(Exception e) {
            LogUtil.INSTANCE.error(getClass().getSimpleName(), e);
        }
        return result;
    }

    public long updateBookmark(final BookMarkCacheEntry bookmarkEntry) {
        long _id = -1;
        try {
            db = databaseOpenHelper.getWritableDatabase();
            return updateBookmark(db, bookmarkEntry.toContentValues(), bookmarkEntry.getBookmarkId());
        } catch (Exception e) {
            LogUtil.INSTANCE.error(getClass().getSimpleName(), e);
        }
        return _id;
    }

    private long updateBookmark(SQLiteDatabase db, ContentValues values, int bookmarkId) throws Exception {
        LogUtil.INSTANCE.info(TAG, "updateBookmark :" + values.toString());
        return db.update(MetaData.BOOKMARK_TABLE, values, MetaData.BookmarkColumns.BOOKMARK_ID + "=" + "'"+bookmarkId+"'", null);
    }

    public int deleteBookmark() {
        int result = -1;
        try {
            db = databaseOpenHelper.getWritableDatabase();
            result = db.delete(MetaData.BOOKMARK_TABLE, null, null);
        } catch(Exception e) {
            LogUtil.INSTANCE.error(getClass().getSimpleName(), e);
        }
        return result;
    }

    public int deleteBookmarkById (int bookmarkId) {
        int result = -1;
        try {
            db = databaseOpenHelper.getWritableDatabase();
            result = db.delete(MetaData.BOOKMARK_TABLE, MetaData.BookmarkColumns.BOOKMARK_ID + "=" + "'"+ bookmarkId +"'", null);
        } catch(Exception e) {
            LogUtil.INSTANCE.error(getClass().getSimpleName(), e);
        }
        return result;
    }

    public int deleteBookmarkUsingContentId (int contentId) {
        int result = -1;
        try {
            db = databaseOpenHelper.getWritableDatabase();
            result = db.delete(MetaData.BOOKMARK_TABLE, MetaData.BookmarkColumns.CONTENT_ID + "=" + "'"+ contentId +"'", null);
        } catch(Exception e) {
            LogUtil.INSTANCE.error(getClass().getSimpleName(), e);
        }
        return result;
    }


    /**
     *
     * @param db
     * @param keys
     * @param values
     * @return
     */
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
            cursor = db.query(MetaData.BOOKMARK_TABLE, null, sb.toString(), null, null, null, null);
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
