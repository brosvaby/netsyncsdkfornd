package com.inka.netsync.view.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by birdgang on 2018. 1. 12..
 */

public class BookMarkViewEntry implements BaseViewEntry, Parcelable {

    private int bookmarkId;
    private int contentId = -1;
    private String bookmarkContentPath = "";
    private String bookmarkContentName = "";
    private String bookmarkDate = "";
    private String bookmarkLocation = "";
    private String bookmarkMemo = "";

    private boolean checked = false;

    public BookMarkViewEntry() {
    }

    public BookMarkViewEntry(int inBookmarkId, int inContentId, String inBookmarkContentPath, String inBookmarkContentName, String inBookmarkDate, String inBookmarkLocation, String inBookmarkMemo ) {
        bookmarkId = inBookmarkId;
        contentId = inContentId;
        bookmarkContentPath = inBookmarkContentPath;
        bookmarkContentName = inBookmarkContentName;
        bookmarkDate = inBookmarkDate;
        bookmarkLocation = inBookmarkLocation;
        bookmarkMemo = inBookmarkMemo;
    }

    private void initValues () {
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

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
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
        dest.writeByte(this.checked ? (byte) 1 : (byte) 0);
    }

    protected BookMarkViewEntry(Parcel in) {
        this.bookmarkId = in.readInt();
        this.contentId = in.readInt();
        this.bookmarkContentPath = in.readString();
        this.bookmarkContentName = in.readString();
        this.bookmarkDate = in.readString();
        this.bookmarkLocation = in.readString();
        this.bookmarkMemo = in.readString();
        this.checked = in.readByte() != 0;
    }

    public static final Creator<BookMarkViewEntry> CREATOR = new Creator<BookMarkViewEntry>() {
        @Override
        public BookMarkViewEntry createFromParcel(Parcel source) {
            return new BookMarkViewEntry(source);
        }

        @Override
        public BookMarkViewEntry[] newArray(int size) {
            return new BookMarkViewEntry[size];
        }
    };


    @Override
    public String toString() {
        return "BookMarkViewEntry{" +
                "bookmarkId=" + bookmarkId +
                ", contentId=" + contentId +
                ", bookmarkContentPath='" + bookmarkContentPath + '\'' +
                ", bookmarkContentName='" + bookmarkContentName + '\'' +
                ", bookmarkDate='" + bookmarkDate + '\'' +
                ", bookmarkLocation='" + bookmarkLocation + '\'' +
                ", bookmarkMemo='" + bookmarkMemo + '\'' +
                ", checked=" + checked +
                '}';
    }

}
