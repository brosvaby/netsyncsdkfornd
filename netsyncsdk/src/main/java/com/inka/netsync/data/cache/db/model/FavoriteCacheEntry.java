package com.inka.netsync.data.cache.db.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.inka.netsync.data.cache.db.MetaData;

/**
 * Created by birdgang on 2018. 1. 15..
 */
public class FavoriteCacheEntry implements BaseCacheEntry, Parcelable {

    private int favoriteId = -1;
    private int contentId = -1;
    private String contentName;
    private String contentPath;

    public FavoriteCacheEntry() {
    }

    public FavoriteCacheEntry (ContentCacheEntry contentCacheEntry) {
        contentId = contentCacheEntry.getContentId();
        contentName = contentCacheEntry.getContentName();
        contentPath = contentCacheEntry.getContentFilePath();
    }

    public FavoriteCacheEntry (Cursor cursor) {
        favoriteId = cursor.getInt(cursor.getColumnIndexOrThrow(MetaData.FavoriteColumns.FAVORITE_ID));
        contentId = cursor.getInt(cursor.getColumnIndexOrThrow(MetaData.FavoriteColumns.CONTENT_ID));
        contentName = cursor.getString(cursor.getColumnIndexOrThrow(MetaData.FavoriteColumns.CONTENT_NAME));
        contentPath = cursor.getString(cursor.getColumnIndexOrThrow(MetaData.FavoriteColumns.CONTENT_FILE_PATH));
    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(MetaData.FavoriteColumns.CONTENT_ID , contentId);
        values.put(MetaData.FavoriteColumns.CONTENT_NAME , contentName);
        values.put(MetaData.FavoriteColumns.CONTENT_FILE_PATH , contentPath);
        return values;
    }

    private static void checkEmptyInt (ContentValues values, String key, int value) {
        if (value > 0) {
            values.put(key, value);
        }
    }

    public int getFavoriteId() {
        return favoriteId;
    }

    public void setFavoriteId(int favoriteId) {
        this.favoriteId = favoriteId;
    }

    public int getContentId() {
        return contentId;
    }

    public void setContentId(int contentId) {
        this.contentId = contentId;
    }

    public String getContentPath() {
        return contentPath;
    }

    public void setContentPath(String contentPath) {
        this.contentPath = contentPath;
    }

    public String getContentName() {
        return contentName;
    }

    public void setContentName(String contentName) {
        this.contentName = contentName;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.favoriteId);
        dest.writeInt(this.contentId);
        dest.writeString(this.contentName);
        dest.writeString(this.contentPath);
    }

    protected FavoriteCacheEntry(Parcel in) {
        this.favoriteId = in.readInt();
        this.contentId = in.readInt();
        this.contentName = in.readString();
        this.contentPath = in.readString();
    }

    public static final Creator<FavoriteCacheEntry> CREATOR = new Creator<FavoriteCacheEntry>() {
        @Override
        public FavoriteCacheEntry createFromParcel(Parcel source) {
            return new FavoriteCacheEntry(source);
        }

        @Override
        public FavoriteCacheEntry[] newArray(int size) {
            return new FavoriteCacheEntry[size];
        }
    };

    @Override
    public String toString() {
        return "FavoriteCacheEntry{" +
                "favoriteId=" + favoriteId +
                ", contentId=" + contentId +
                ", contentName='" + contentName + '\'' +
                ", contentPath='" + contentPath + '\'' +
                '}';
    }

}
