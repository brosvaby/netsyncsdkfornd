package com.inka.netsync.data.cache.db.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.inka.netsync.data.cache.db.MetaData;

/**
 * Created by birdgang on 2018. 2. 19..
 */

public class LMSCacheEntry implements Parcelable {

    private int id;
    private int contentId;
    private String contentFilePath = "";
    private String contentName = "";
    private String section = "";
    private String rawSection = "";
    private String rate = "";

    public LMSCacheEntry() {
    }

    public LMSCacheEntry(Cursor cursor) {
        id = cursor.getInt(cursor.getColumnIndexOrThrow(MetaData.LMSColumns.LMS_ID));
        contentId = cursor.getInt(cursor.getColumnIndexOrThrow(MetaData.LMSColumns.CONTENT_ID));
        contentFilePath = cursor.getString(cursor.getColumnIndexOrThrow(MetaData.LMSColumns.CONTENT_FILE_PATH));
        contentName = cursor.getString(cursor.getColumnIndexOrThrow(MetaData.LMSColumns.CONTENT_NAME));
        section = cursor.getString(cursor.getColumnIndexOrThrow(MetaData.LMSColumns.SECTION));
        rawSection = cursor.getString(cursor.getColumnIndexOrThrow(MetaData.LMSColumns.RAW_SECTION));
        rate = cursor.getString(cursor.getColumnIndexOrThrow(MetaData.LMSColumns.RATE));
    }


    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(MetaData.LMSColumns.CONTENT_ID , contentId);
        values.put(MetaData.LMSColumns.CONTENT_FILE_PATH , contentFilePath);
        values.put(MetaData.LMSColumns.CONTENT_NAME , contentName);
        values.put(MetaData.LMSColumns.SECTION, section);
        values.put(MetaData.LMSColumns.RAW_SECTION , rawSection);
        values.put(MetaData.LMSColumns.RATE , rate);
        return values;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getContentId() {
        return contentId;
    }

    public void setContentId(int contentId) {
        this.contentId = contentId;
    }

    public String getContentFilePath() {
        return contentFilePath;
    }

    public void setContentFilePath(String contentFilePath) {
        this.contentFilePath = contentFilePath;
    }

    public String getContentName() {
        return contentName;
    }

    public void setContentName(String contentName) {
        this.contentName = contentName;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getRawSection() {
        return rawSection;
    }

    public void setRawSection(String rawSection) {
        this.rawSection = rawSection;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.contentId);
        dest.writeString(this.contentFilePath);
        dest.writeString(this.contentName);
        dest.writeString(this.section);
        dest.writeString(this.rawSection);
        dest.writeString(this.rate);
    }

    protected LMSCacheEntry(Parcel in) {
        this.id = in.readInt();
        this.contentId = in.readInt();
        this.contentFilePath = in.readString();
        this.contentName = in.readString();
        this.section = in.readString();
        this.rawSection = in.readString();
        this.rate = in.readString();
    }

    public static final Creator<LMSCacheEntry> CREATOR = new Creator<LMSCacheEntry>() {
        @Override
        public LMSCacheEntry createFromParcel(Parcel source) {
            return new LMSCacheEntry(source);
        }

        @Override
        public LMSCacheEntry[] newArray(int size) {
            return new LMSCacheEntry[size];
        }
    };

    @Override
    public String toString() {
        return "LMSCacheEntry{" +
                "id=" + id +
                ", contentId=" + contentId +
                ", contentFilePath='" + contentFilePath + '\'' +
                ", contentName='" + contentName + '\'' +
                ", section='" + section + '\'' +
                ", rawSection='" + rawSection + '\'' +
                ", rate='" + rate + '\'' +
                '}';
    }

}
