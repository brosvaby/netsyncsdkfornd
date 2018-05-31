package com.inka.netsync.controler;

import com.inka.netsync.data.cache.db.dao.BookMarkDao;
import com.inka.netsync.data.cache.db.model.BookMarkCacheEntry;
import com.inka.netsync.logs.LogUtil;
import com.inka.netsync.model.BookmarkEntry;

import java.util.ArrayList;

public class BookmarkControler {

    private final String TAG = "BookmarkControler";

    private static volatile BookmarkControler defaultInstance;

    public static BookmarkControler getDefault() {
        if (defaultInstance == null) {
            synchronized (BookmarkControler.class) {
                if (defaultInstance == null) {
                    defaultInstance = new BookmarkControler();
                }
            }
        }
        return defaultInstance;
    }

    private BookmarkControler() {
    }

    public BookmarkEntry findBookmarkById(int bookmarkId) {
        BookmarkEntry bookmarkEntry = null;
        try {
            BookMarkCacheEntry bookMarkCacheEntry = BookMarkDao.getDefault().getBookmark(bookmarkId);
            bookmarkEntry = new BookmarkEntry(bookMarkCacheEntry);
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
        return bookmarkEntry;
    }


    public ArrayList<BookmarkEntry> loadBookmarkListInContent (int contentId) {
        ArrayList<BookmarkEntry> bookmarkEntries = new ArrayList<>();
        try {
            ArrayList<BookMarkCacheEntry> bookmarkCacheEntries = BookMarkDao.getDefault().getBookmarkArrayListUsingContentId(contentId);
            for (BookMarkCacheEntry entry : bookmarkCacheEntries) {
                BookmarkEntry bookmarkEntry = new BookmarkEntry(entry);
                bookmarkEntries.add(bookmarkEntry);
            }
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
        return bookmarkEntries;
    }


    public ArrayList<BookmarkEntry> loadBookmarkListInContentByPath (String path) {
        ArrayList<BookmarkEntry> bookmarkEntries = new ArrayList<>();
        try {
            ArrayList<BookMarkCacheEntry> bookmarkCacheEntries = BookMarkDao.getDefault().getBookmarkArrayListUsingContentPath(path);
            for (BookMarkCacheEntry entry : bookmarkCacheEntries) {
                BookmarkEntry bookmarkEntry = new BookmarkEntry(entry);
                bookmarkEntries.add(bookmarkEntry);
            }
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
        return bookmarkEntries;
    }


    public ArrayList<BookmarkEntry> loadBookmarkListInContentByContentName (String contentName) {
        ArrayList<BookmarkEntry> bookmarkEntries = new ArrayList<>();
        try {
            ArrayList<BookMarkCacheEntry> bookmarkCacheEntries = BookMarkDao.getDefault().getBookmarkArrayListUsingContentName(contentName);
            for (BookMarkCacheEntry entry : bookmarkCacheEntries) {
                BookmarkEntry bookmarkEntry = new BookmarkEntry(entry);
                bookmarkEntries.add(bookmarkEntry);
            }
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
        return bookmarkEntries;
    }


    public long addBookmark (BookmarkEntry bookmarkEntry) {
        long reslut = -1;
        try {
            reslut = BookMarkDao.getDefault().insertBookmark(bookmarkEntry.convertCacheEntry());
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
        return reslut;
    }


    public long deleteBookmarkById (int bookmarkId) {
        long reslut = -1;
        try {
            reslut = BookMarkDao.getDefault().deleteBookmarkById(bookmarkId);
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
        return reslut;
    }

}
