package com.inka.netsync.model;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

import com.inka.netsync.data.cache.db.MetaData;
import com.inka.netsync.data.cache.db.model.FavoriteCacheEntry;
import com.inka.netsync.view.model.FavoriteViewEntry;

public class FavoriteEntry implements BaseEntry, Parcelable {

    private int favoriteId = -1;
    private int contentId = -1;
    private String contentName;
    private String contentPath;

    public FavoriteEntry() {
    }

    public FavoriteEntry (FavoriteCacheEntry favoriteCacheEntry) {
        setContentId(favoriteCacheEntry.getContentId());
        setContentName(favoriteCacheEntry.getContentName());
        setContentPath(favoriteCacheEntry.getContentPath());
        setFavoriteId(favoriteCacheEntry.getFavoriteId());
    }

    public FavoriteEntry (ContentEntry contentEntry) {
        contentId = contentEntry.getContentId();
        contentName = contentEntry.getContentName();
        contentPath = contentEntry.getContentFilePath();
    }

    public FavoriteCacheEntry convertCacheEntry () {
        FavoriteCacheEntry favoriteCacheEntry = new FavoriteCacheEntry();
        favoriteCacheEntry.setFavoriteId(favoriteId);
        favoriteCacheEntry.setContentId(contentId);
        favoriteCacheEntry.setContentName(contentName);
        favoriteCacheEntry.setContentPath(contentPath);
        return favoriteCacheEntry;
    }

    public FavoriteViewEntry convertViewEntry () {
        FavoriteViewEntry favoriteViewEntry = new FavoriteViewEntry();
        favoriteViewEntry.setFavoriteId(favoriteId);
        favoriteViewEntry.setContentId(contentId);
        favoriteViewEntry.setContentName(contentName);
        favoriteViewEntry.setContentPath(contentPath);
        return favoriteViewEntry;
    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(MetaData.FavoriteColumns.CONTENT_ID , contentId);
        values.put(MetaData.FavoriteColumns.CONTENT_NAME , contentName);
        values.put(MetaData.FavoriteColumns.CONTENT_FILE_PATH , contentPath);
        return values;
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

    protected FavoriteEntry(Parcel in) {
        this.favoriteId = in.readInt();
        this.contentId = in.readInt();
        this.contentName = in.readString();
        this.contentPath = in.readString();
    }

    public static final Parcelable.Creator<FavoriteEntry> CREATOR = new Parcelable.Creator<FavoriteEntry>() {
        @Override
        public FavoriteEntry createFromParcel(Parcel source) {
            return new FavoriteEntry(source);
        }

        @Override
        public FavoriteEntry[] newArray(int size) {
            return new FavoriteEntry[size];
        }
    };

    @Override
    public String toString() {
        return "FavoriteEntry{" +
                "favoriteId=" + favoriteId +
                ", contentId=" + contentId +
                ", contentName='" + contentName + '\'' +
                ", contentPath='" + contentPath + '\'' +
                '}';
    }

}