package com.inka.netsync.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.inka.netsync.data.cache.db.model.BookMarkCacheEntry;
import com.inka.netsync.view.model.BookMarkViewEntry;

public class BookmarkEntry implements BaseEntry, Parcelable {

    private int bookmarkId;
    private int contentId = -1;
    private String bookmarkContentPath = "";
    private String bookmarkContentName = "";
    private String bookmarkDate = "";
    private String bookmarkLocation = "";
    private String bookmarkMemo = "";

    public BookmarkEntry() {
    }

    public BookmarkEntry (BookMarkCacheEntry bookMarkCacheEntry) {
        setBookmarkId(bookMarkCacheEntry.getBookmarkId());
        setContentId(bookMarkCacheEntry.getContentId());
        setBookmarkContentPath(bookMarkCacheEntry.getBookmarkContentPath());
        setBookmarkContentName(bookMarkCacheEntry.getBookmarkContentName());
        setBookmarkDate(bookMarkCacheEntry.getBookmarkDate());
        setBookmarkLocation(bookMarkCacheEntry.getBookmarkLocation());
        setBookmarkMemo(bookMarkCacheEntry.getBookmarkMemo());
    }

    public BookMarkCacheEntry convertCacheEntry () {
        BookMarkCacheEntry bookMarkCacheEntry = new BookMarkCacheEntry();
        bookMarkCacheEntry.setBookmarkId(getBookmarkId());
        bookMarkCacheEntry.setContentId(getContentId());
        bookMarkCacheEntry.setBookmarkContentPath(getBookmarkContentPath());
        bookMarkCacheEntry.setBookmarkContentName(getBookmarkContentName());
        bookMarkCacheEntry.setBookmarkDate(getBookmarkDate());
        bookMarkCacheEntry.setBookmarkLocation(getBookmarkLocation());
        bookMarkCacheEntry.setBookmarkMemo(getBookmarkMemo());
        return bookMarkCacheEntry;
    }

    public BookMarkViewEntry convertViewEntry () {
        BookMarkViewEntry bookMarkViewEntry = new BookMarkViewEntry();
        bookMarkViewEntry.setBookmarkId(getBookmarkId());
        bookMarkViewEntry.setContentId(getContentId());
        bookMarkViewEntry.setBookmarkContentPath(getBookmarkContentPath());
        bookMarkViewEntry.setBookmarkContentName(getBookmarkContentName());
        bookMarkViewEntry.setBookmarkDate(getBookmarkDate());
        bookMarkViewEntry.setBookmarkLocation(getBookmarkLocation());
        bookMarkViewEntry.setBookmarkMemo(getBookmarkMemo());
        return bookMarkViewEntry;
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

    protected BookmarkEntry(Parcel in) {
        this.bookmarkId = in.readInt();
        this.contentId = in.readInt();
        this.bookmarkContentPath = in.readString();
        this.bookmarkContentName = in.readString();
        this.bookmarkDate = in.readString();
        this.bookmarkLocation = in.readString();
        this.bookmarkMemo = in.readString();
    }

    public static final Parcelable.Creator<BookmarkEntry> CREATOR = new Parcelable.Creator<BookmarkEntry>() {
        @Override
        public BookmarkEntry createFromParcel(Parcel source) {
            return new BookmarkEntry(source);
        }

        @Override
        public BookmarkEntry[] newArray(int size) {
            return new BookmarkEntry[size];
        }
    };

    @Override
    public String toString() {
        return "BookmarkEntry{" +
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
