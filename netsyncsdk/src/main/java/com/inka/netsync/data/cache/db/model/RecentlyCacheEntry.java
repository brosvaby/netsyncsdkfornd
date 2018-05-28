package com.inka.netsync.data.cache.db.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.inka.netsync.common.utils.DateTimeUtil;
import com.inka.netsync.data.cache.db.MetaData;
import com.inka.netsync.logs.LogUtil;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by birdgang on 2018. 1. 15..
 */
public class RecentlyCacheEntry implements BaseCacheEntry, Parcelable {

    private int recentlyId = -1;
    private int contentId = -1;
    private String contentPath;
    private String contentName;
    private String playDate = "";

    public RecentlyCacheEntry () {
    }

    public RecentlyCacheEntry (ContentCacheEntry contentCacheEntry) {
        contentId = contentCacheEntry.getContentId();
        contentName = contentCacheEntry.getContentName();
        contentPath = contentCacheEntry.getContentFilePath();
        playDate = contentCacheEntry.getPlayDate();
    }

    public RecentlyCacheEntry (Cursor cursor) {
        recentlyId = cursor.getInt(cursor.getColumnIndexOrThrow(MetaData.RecentlyColumns.RECENTLY_ID));
        contentId = cursor.getInt(cursor.getColumnIndexOrThrow(MetaData.RecentlyColumns.CONTENT_ID));
        contentName = cursor.getString(cursor.getColumnIndexOrThrow(MetaData.RecentlyColumns.CONTENT_NAME));
        contentPath = cursor.getString(cursor.getColumnIndexOrThrow(MetaData.RecentlyColumns.CONTENT_FILE_PATH));
        playDate = cursor.getString(cursor.getColumnIndexOrThrow(MetaData.RecentlyColumns.RECENTLY_DATE));
    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(MetaData.RecentlyColumns.CONTENT_ID , contentId);
        values.put(MetaData.RecentlyColumns.CONTENT_FILE_PATH , contentPath);
        values.put(MetaData.RecentlyColumns.RECENTLY_DATE , playDate);
        values.put(MetaData.RecentlyColumns.CONTENT_NAME , contentName);
        return values;
    }

    public int getRecentlyId() {
        return recentlyId;
    }

    public void setRecentlyId(int recentlyId) {
        this.recentlyId = recentlyId;
    }

    public String getContentName() {
        return contentName;
    }

    public void setContentName(String contentName) {
        this.contentName = contentName;
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

    public String getPlayDate() {
        return playDate;
    }

    public void setPlayDate(String playDate) {
        this.playDate = playDate;
    }


    public Date getPlayDateToDate() {
        if (StringUtils.isBlank(playDate)) {
            return new Date();
        }

        String from = playDate;
        SimpleDateFormat detailedDate = DateTimeUtil.getSimpleDateFormat();
        Date to = null;
        try {
            to = detailedDate.parse(from);
        } catch (ParseException e) {
            LogUtil.INSTANCE.error("error", e);
        }
        return to;
    }

    public void setPlayDateToDate(Date date) {
        if (null == date) {
            return;
        }

        Date from = date;
        SimpleDateFormat detailedDate = DateTimeUtil.getSimpleDateFormat();
        playDate = detailedDate.format(from);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.recentlyId);
        dest.writeInt(this.contentId);
        dest.writeString(this.contentPath);
        dest.writeString(this.contentName);
        dest.writeString(this.playDate);
    }

    protected RecentlyCacheEntry(Parcel in) {
        this.recentlyId = in.readInt();
        this.contentId = in.readInt();
        this.contentPath = in.readString();
        this.contentName = in.readString();
        this.playDate = in.readString();
    }

    public static final Creator<RecentlyCacheEntry> CREATOR = new Creator<RecentlyCacheEntry>() {
        @Override
        public RecentlyCacheEntry createFromParcel(Parcel source) {
            return new RecentlyCacheEntry(source);
        }

        @Override
        public RecentlyCacheEntry[] newArray(int size) {
            return new RecentlyCacheEntry[size];
        }
    };

    @Override
    public String toString() {
        return "RecentlyCacheEntry{" +
                "recentlyId=" + recentlyId +
                ", contentId=" + contentId +
                ", contentPath='" + contentPath + '\'' +
                ", contentName='" + contentName + '\'' +
                ", playDate='" + playDate + '\'' +
                '}';
    }
}
