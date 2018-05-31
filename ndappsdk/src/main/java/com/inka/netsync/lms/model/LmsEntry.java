package com.inka.netsync.lms.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.inka.netsync.data.cache.db.model.LMSCacheEntry;

/**
 * Created by birdgang on 2017. 10. 24..
 */

public class LmsEntry implements Parcelable {

    private int id;
    private int contentId;

    private String contentFilePath = "";
    private String contentName = "";
    private String contentLastPlayTime = "";

    private String section = "";
    private String rawSection = "";
    private String rate = "";

    private int duration = -1;

    public LmsEntry() {
        this.section = "";
        this.rawSection = "";
        this.duration = -1;
    }

    public LmsEntry(String section, String rawSection, int duration) {
        this.section = section;
        this.rawSection = rawSection;
        this.duration = duration;
    }

    public LmsEntry(LMSCacheEntry lmsCacheEntry) throws Exception {
        setId(lmsCacheEntry.getContentId());
        setContentId(lmsCacheEntry.getContentId());
        setContentFilePath(lmsCacheEntry.getContentFilePath());
        setContentName(lmsCacheEntry.getContentName());
        setSection(lmsCacheEntry.getSection());
        setRawSection(lmsCacheEntry.getRawSection());
        setRate(lmsCacheEntry.getRate());
    }

    public LMSCacheEntry toConvertCacheEntry () {
        LMSCacheEntry lmsCacheEntry = new LMSCacheEntry();
        lmsCacheEntry.setId(id);
        lmsCacheEntry.setContentId(contentId);
        lmsCacheEntry.setContentFilePath(contentFilePath);
        lmsCacheEntry.setContentName(contentName);
        lmsCacheEntry.setSection(section);
        lmsCacheEntry.setRawSection(rawSection);
        lmsCacheEntry.setRate(rate);
        return lmsCacheEntry;
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

    public String getContentLastPlayTime() {
        return contentLastPlayTime;
    }

    public void setContentLastPlayTime(String contentLastPlayTime) {
        this.contentLastPlayTime = contentLastPlayTime;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
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
        dest.writeString(this.contentLastPlayTime);
        dest.writeString(this.section);
        dest.writeString(this.rawSection);
        dest.writeString(this.rate);
        dest.writeInt(this.duration);
    }

    protected LmsEntry(Parcel in) {
        this.id = in.readInt();
        this.contentId = in.readInt();
        this.contentFilePath = in.readString();
        this.contentName = in.readString();
        this.contentLastPlayTime = in.readString();
        this.section = in.readString();
        this.rawSection = in.readString();
        this.rate = in.readString();
        this.duration = in.readInt();
    }

    public static final Creator<LmsEntry> CREATOR = new Creator<LmsEntry>() {
        @Override
        public LmsEntry createFromParcel(Parcel source) {
            return new LmsEntry(source);
        }

        @Override
        public LmsEntry[] newArray(int size) {
            return new LmsEntry[size];
        }
    };

    @Override
    public String toString() {
        return "LmsEntry{" +
                "id=" + id +
                ", contentId=" + contentId +
                ", contentFilePath='" + contentFilePath + '\'' +
                ", contentName='" + contentName + '\'' +
                ", contentLastPlayTime='" + contentLastPlayTime + '\'' +
                ", section='" + section + '\'' +
                ", rawSection='" + rawSection + '\'' +
                ", rate='" + rate + '\'' +
                ", duration=" + duration +
                '}';
    }

}
