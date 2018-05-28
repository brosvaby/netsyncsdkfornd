package com.inka.netsync.data.cache.db.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.inka.netsync.data.cache.db.MetaData;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by birdgang on 2018. 1. 15..
 */

public class BookMarkCacheEntry implements BaseCacheEntry, Parcelable {

    private int bookmarkId;
    private int contentId = -1;
    private String bookmarkContentPath = "";
    private String bookmarkContentName = "";
    private String bookmarkDate = "";
    private String bookmarkLocation = "";
    private String bookmarkMemo = "";

    public BookMarkCacheEntry() {
    }

    public BookMarkCacheEntry(int inBookmarkId, int inContentId, String inBookmarkContentPath, String inBookmarkContentName, String inBookmarkDate, String inBookmarkLocation, String inBookmarkMemo ) {
        bookmarkId = inBookmarkId;
        contentId = inContentId;
        bookmarkContentPath = inBookmarkContentPath;
        bookmarkContentName = inBookmarkContentName;
        bookmarkDate = inBookmarkDate;
        bookmarkLocation = inBookmarkLocation;
        bookmarkMemo = inBookmarkMemo;
    }


    public BookMarkCacheEntry (Cursor cursor) {
        bookmarkId = cursor.getInt(cursor.getColumnIndexOrThrow(MetaData.BookmarkColumns.BOOKMARK_ID));
        contentId = cursor.getInt(cursor.getColumnIndexOrThrow(MetaData.BookmarkColumns.CONTENT_ID));
        bookmarkContentPath = cursor.getString(cursor.getColumnIndexOrThrow(MetaData.BookmarkColumns.BOOKMARK_CONTENT_FILE_PATH));
        bookmarkContentName = cursor.getString(cursor.getColumnIndexOrThrow(MetaData.BookmarkColumns.BOOKMARK_CONTENT_NAME));
        bookmarkDate = cursor.getString(cursor.getColumnIndexOrThrow(MetaData.BookmarkColumns.BOOKMARK_DATE));
        bookmarkLocation = cursor.getString(cursor.getColumnIndexOrThrow(MetaData.BookmarkColumns.BOOKMARK_LOCATION));
        bookmarkMemo = cursor.getString(cursor.getColumnIndexOrThrow(MetaData.BookmarkColumns.BOOKMARK_MEMO));
    }


    public ContentValues toContentValues() {
        initValues();

        ContentValues values = new ContentValues();
        values.put(MetaData.BookmarkColumns.CONTENT_ID , contentId);
        values.put(MetaData.BookmarkColumns.BOOKMARK_CONTENT_FILE_PATH , bookmarkContentPath);
        values.put(MetaData.BookmarkColumns.BOOKMARK_CONTENT_NAME , bookmarkContentName);
        values.put(MetaData.BookmarkColumns.BOOKMARK_DATE , bookmarkDate);
        safePut(values, MetaData.BookmarkColumns.BOOKMARK_LOCATION, bookmarkLocation);
        values.put(MetaData.BookmarkColumns.BOOKMARK_MEMO , bookmarkMemo);

        return values;
    }

    private void initValues () {
    }

    private static void safePut(ContentValues values, String key, String value) {
        if (StringUtils.isNotBlank(value)) {
            values.put(key, value);
        }
    }

    public int getBookmarkId() {
        return bookmarkId;
    }

    public void setBookmarkId(int bookmarkId) {
        this.bookmarkId = bookmarkId;
    }

    public int getContentId() {
        return contentId;
    }

    public void setContentId(int contentId) {
        this.contentId = contentId;
    }

    public String getBookmarkContentPath() {
        return bookmarkContentPath;
    }

    public void setBookmarkContentPath(String bookmarkContentPath) {
        this.bookmarkContentPath = bookmarkContentPath;
    }

    public String getBookmarkContentName() {
        return bookmarkContentName;
    }

    public void setBookmarkContentName(String bookmarkContentName) {
        this.bookmarkContentName = bookmarkContentName;
    }

    public String getBookmarkDate() {
        return bookmarkDate;
    }

    public void setBookmarkDate(String bookmarkDate) {
        this.bookmarkDate = bookmarkDate;
    }

    public String getBookmarkLocation() {
        return bookmarkLocation;
    }

    public void setBookmarkLocation(String bookmarkLocation) {
        this.bookmarkLocation = bookmarkLocation;
    }

    public String getBookmarkMemo() {
        return bookmarkMemo;
    }

    public void setBookmarkMemo(String bookmarkMemo) {
        this.bookmarkMemo = bookmarkMemo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.bookmarkId);
        dest.writeInt(this.contentId);
        dest.writeString(this.bookmarkContentPath);
        dest.writeString(this.bookmarkContentName);
        dest.writeString(this.bookmarkDate);
        dest.writeString(this.bookmarkLocation);
        dest.writeString(this.bookmarkMemo);
    }

    protected BookMarkCacheEntry(Parcel in) {
        this.bookmarkId = in.readInt();
        this.contentId = in.readInt();
        this.bookmarkContentPath = in.readString();
        this.bookmarkContentName = in.readString();
        this.bookmarkDate = in.readString();
        this.bookmarkLocation = in.readString();
        this.bookmarkMemo = in.readString();
    }

    public static final Creator<BookMarkCacheEntry> CREATOR = new Creator<BookMarkCacheEntry>() {
        @Override
        public BookMarkCacheEntry createFromParcel(Parcel source) {
            return new BookMarkCacheEntry(source);
        }

        @Override
        public BookMarkCacheEntry[] newArray(int size) {
            return new BookMarkCacheEntry[size];
        }
    };

    @Override
    public String toString() {
        return "BookMarkCacheEntry{" +
                "bookmarkId=" + bookmarkId +
                ", contentId=" + contentId +
                ", bookmarkContentPath='" + bookmarkContentPath + '\'' +
                ", bookmarkContentName='" + bookmarkContentName + '\'' +
                ", bookmarkDate='" + bookmarkDate + '\'' +
                ", bookmarkLocation='" + bookmarkLocation + '\'' +
                ", bookmarkMemo='" + bookmarkMemo + '\'' +
                '}';
    }

}
