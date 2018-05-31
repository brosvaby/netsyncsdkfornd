package com.inka.netsync.data.cache.db.migration;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.inka.netsync.data.cache.db.MetaData;
import com.inka.netsync.data.cache.db.SqlStorage;
import com.inka.netsync.data.cache.db.model.BookMarkCacheEntry;
import com.inka.netsync.data.cache.db.model.ContentCacheEntry;
import com.inka.netsync.logs.LogUtil;

import java.util.ArrayList;

/**
 * Created by birdgang on 2018. 3. 2..
 */
public class MigrationBookMarkTable extends MigrationData {

    private final String TAG = "MigrationBookMarkTable";


    public synchronized void migrationUpdateTableFromV2ToV3BookMarkData(SQLiteDatabase db) {
        try {
            createTableFromV2ToV3BookMarkData(db, MetaData.BOOKMARK_TABLE_BACKUP);

            ArrayList<BookMarkCacheEntry> existBookmarkEntries = loadBookMarkContentListFromV2(db);

            if (null == existBookmarkEntries || existBookmarkEntries.size() <= 0) {
                LogUtil.INSTANCE.info("birdgangdatabase", "migrationUpdateTableFromV2ToV3BookMarkData > null == existBookmarkEntries || existBookmarkEntries.size() <= 0");
                return;
            }

            LogUtil.INSTANCE.info("birdgangdatabase", "migrationUpdateTableFromV2ToV3BookMarkData > existBookmarkEntries size : " + existBookmarkEntries.size());
            for (BookMarkCacheEntry bookMarkCacheEntry : existBookmarkEntries) {
                long result = insertBookMarkToTable(db, bookMarkCacheEntry, MetaData.BOOKMARK_TABLE_BACKUP);
                LogUtil.INSTANCE.info("birdgangdatabase", "migrationUpdateTableFromV2ToV3BookMarkData > result : " + result);
            }

            migrationUpdateSyncAdditionalDataTableFromV2ToV3BookMark(db);

            db.execSQL(SqlStorage.MIGRATION_DROP_BOOKMARK_TABLE);
            db.execSQL(SqlStorage.MIGRATION_RENAME_BOOKMARK_TABLE);
        } catch (Exception e) {
            LogUtil.INSTANCE.error("birdgangdatabase", e);
        }
    }



    /***
     * load
     */
    public ArrayList<BookMarkCacheEntry> loadBookMarkContentListFromV2(SQLiteDatabase db) {
        ArrayList<BookMarkCacheEntry> bookMarkCacheEntries = new ArrayList<BookMarkCacheEntry>();
        Cursor cursor = null;
        try {
            cursor = db.query(MetaData.BOOKMARK_TABLE, null, null, null, null, null, MetaData.BookmarkColumns.BOOKMARK_LOCATION, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int bookmarkId = cursor.getInt(cursor.getColumnIndexOrThrow(MetaData.BookmarkColumns.BOOKMARK_ID));
                    int contentId = cursor.getInt(cursor.getColumnIndexOrThrow(MetaData.BookmarkColumns.CONTENT_ID));
                    String bookmarkDate = cursor.getString(cursor.getColumnIndexOrThrow(MetaData.BookmarkColumns.BOOKMARK_DATE));
                    String bookmarkLocation = cursor.getString(cursor.getColumnIndexOrThrow(MetaData.BookmarkColumns.BOOKMARK_LOCATION));
                    String bookmarkMemo = cursor.getString(cursor.getColumnIndexOrThrow(MetaData.BookmarkColumns.BOOKMARK_MEMO));

                    BookMarkCacheEntry bookMarkCacheEntry = new BookMarkCacheEntry();
                    bookMarkCacheEntry.setBookmarkId(bookmarkId);
                    bookMarkCacheEntry.setContentId(contentId);
                    bookMarkCacheEntry.setBookmarkDate(bookmarkDate);
                    bookMarkCacheEntry.setBookmarkLocation(bookmarkLocation);
                    bookMarkCacheEntry.setBookmarkMemo(bookmarkMemo);

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


    public ArrayList<BookMarkCacheEntry> loadBookMarkContentListFromV3(SQLiteDatabase db, String tableName) {
        ArrayList<BookMarkCacheEntry> bookMarkCacheEntries = new ArrayList<BookMarkCacheEntry>();
        Cursor cursor = null;
        try {
            cursor = db.query(tableName, null, null, null, null, null, MetaData.BookmarkColumns.BOOKMARK_LOCATION, null);
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


    /****
     * insert
     */
    private long insertBookMarkToTable (SQLiteDatabase db, BookMarkCacheEntry bookMarkCacheEntry, String tableName) throws SQLiteException {
        long result = -1;
        try {
            LogUtil.INSTANCE.info("birdgangdatabase", "insertBookMarkToTable > bookMarkCacheEntry.toString() : " + bookMarkCacheEntry.toString());

            int bookmarkId = bookMarkCacheEntry.getBookmarkId();
            boolean hasExist = checkRecordExist(db, tableName, new String[]{MetaData.BookmarkColumns.BOOKMARK_ID}, new String[]{ String.valueOf(bookmarkId) });
            if (!hasExist) {
                result = db.insert(tableName, null, bookMarkCacheEntry.toContentValues());
            }
            LogUtil.INSTANCE.debug("birdgangdatabase", "insertBookMarkToTable DAO insert > hasExist : " + hasExist + ", result : " + result);
        } catch(Exception e) {
            LogUtil.INSTANCE.error(getClass().getSimpleName(), e);
        }
        return result;
    }


    /**
     *
     * @param db
     * @param tableName
     */
    private long updateBookMarkToTable (SQLiteDatabase db, BookMarkCacheEntry bookMarkCacheEntry, String tableName) throws SQLiteException {
        long result = -1;
        try {
            LogUtil.INSTANCE.info("birdgangdatabase", "updateBookMarkToTable > bookMarkCacheEntry.toString() : " + bookMarkCacheEntry.toString());

            int bookmarkId = bookMarkCacheEntry.getBookmarkId();
            boolean hasExist = checkRecordExist(db, tableName, new String[]{MetaData.BookmarkColumns.BOOKMARK_ID}, new String[]{ String.valueOf(bookmarkId) });
            if (hasExist) {
                result = db.update(tableName, bookMarkCacheEntry.toContentValues(), MetaData.BookmarkColumns.BOOKMARK_ID + "=?", new String[] { String.valueOf(bookmarkId) });
            }
            LogUtil.INSTANCE.debug("birdgangdatabase", "updateBookMarkToTable DAO update > hasExist : " + hasExist + ", result : " + result);
        } catch(Exception e) {
            LogUtil.INSTANCE.error(getClass().getSimpleName(), e);
        }
        return result;
    }


    public void createTableFromV2ToV3BookMarkData(SQLiteDatabase db, String tableName) {
        String query = "CREATE TABLE IF NOT EXISTS "
                + tableName + " ("
                + MetaData.BookmarkColumns.BOOKMARK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MetaData.BookmarkColumns.CONTENT_ID + " INTEGER, "
                + MetaData.BookmarkColumns.BOOKMARK_CONTENT_FILE_PATH + " TEXT, "
                + MetaData.BookmarkColumns.BOOKMARK_CONTENT_NAME + " TEXT, "
                + MetaData.BookmarkColumns.BOOKMARK_DATE + " DATE NOT NULL, "
                + MetaData.BookmarkColumns.BOOKMARK_LOCATION + " TEXT NOT NULL, "
                + MetaData.BookmarkColumns.BOOKMARK_MEMO + " TEXT NOT NULL"
                + ");";

        db.execSQL(query);
        db.execSQL("CREATE INDEX IF NOT EXISTS " + tableName + "_index_bookmark_id ON " + tableName + "(" + MetaData.BookmarkColumns.BOOKMARK_ID + ")");
    }



    public void migrationUpdateSyncAdditionalDataTableFromV2ToV3BookMark(SQLiteDatabase db) {
        try {
            ArrayList<BookMarkCacheEntry> bookmarkEntries = loadBookMarkContentListFromV3(db, MetaData.BOOKMARK_TABLE_BACKUP);
            if (null == bookmarkEntries || bookmarkEntries.size() <= 0) {
                LogUtil.INSTANCE.info("birdgangdatabase", "migrationUpdateSyncAdditionalDataTableFromV2ToV3BookMark > null == bookmarkEntries || bookmarkEntries.size() <= 0");
                return;
            }

            for (int i=0; i<bookmarkEntries.size(); i++) {
                BookMarkCacheEntry bookMarkCacheEntry = bookmarkEntries.get(i);
                if (null == bookMarkCacheEntry) {
                    LogUtil.INSTANCE.info("birdgangdatabase", "migrationUpdateSyncAdditionalDataTableFromV2ToV3BookMark > null == bookMarkCacheEntry");
                    continue;
                }

                int contentId = bookMarkCacheEntry.getContentId();
                ContentCacheEntry contentCacheEntry = getPrevContentById(contentId);
                if (null == contentCacheEntry) {
                    LogUtil.INSTANCE.info("birdgangdatabase", "migrationUpdateSyncAdditionalDataTableFromV2ToV3BookMark > null == contentCacheEntry");
                    continue;
                }

                String filePath = contentCacheEntry.getContentFilePath();
                String name = contentCacheEntry.getContentName();
                bookMarkCacheEntry.setBookmarkContentPath(filePath);
                bookMarkCacheEntry.setBookmarkContentName(name);

                bookmarkEntries.set(i, bookMarkCacheEntry);

                long result = updateBookMarkToTable(db, bookMarkCacheEntry, MetaData.BOOKMARK_TABLE_BACKUP);
                LogUtil.INSTANCE.info("birdgangdatabase", "migrationUpdateSyncAdditionalDataTableFromV2ToV3BookMark > result : " + result);
            }
        } catch (Exception e) {
            LogUtil.INSTANCE.error("birdgangdatabase", e);
        }
    }


}
