package com.inka.netsync.view.model;

import android.os.Parcel;
import android.os.Parcelable;

public class FavoriteViewEntry implements BaseViewEntry, Parcelable {

    private int favoriteId = -1;
    private int contentId = -1;
    private String contentName;
    private String contentPath;

    public FavoriteViewEntry() {
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

    protected FavoriteViewEntry(Parcel in) {
        this.favoriteId = in.readInt();
        this.contentId = in.readInt();
        this.contentName = in.readString();
        this.contentPath = in.readString();
    }

    public static final Creator<FavoriteViewEntry> CREATOR = new Creator<FavoriteViewEntry>() {
        @Override
        public FavoriteViewEntry createFromParcel(Parcel source) {
            return new FavoriteViewEntry(source);
        }

        @Override
        public FavoriteViewEntry[] newArray(int size) {
            return new FavoriteViewEntry[size];
        }
    };

    @Override
    public String toString() {
        return "FavoriteViewEntry{" +
                "favoriteId=" + favoriteId +
                ", contentId=" + contentId +
                ", contentName='" + contentName + '\'' +
                ", contentPath='" + contentPath + '\'' +
                '}';
    }

}