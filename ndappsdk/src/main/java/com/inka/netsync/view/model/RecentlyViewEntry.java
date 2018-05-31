package com.inka.netsync.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.inka.netsync.common.utils.DateTimeUtil;
import com.inka.netsync.logs.LogUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

public class RecentlyViewEntry implements BaseViewEntry, Parcelable {

    private int recentlyId = -1;
    private int contentId = -1;
    private String contentPath;
    private String contentName;
    private String playDate = "";
    private String lmsRate = "";

    public RecentlyViewEntry() {
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

    public void setPlayDateToDate(Date date){
        Date from = date;
        SimpleDateFormat detailedDate = DateTimeUtil.getSimpleDateFormat();
        playDate = detailedDate.format(from);
    }

    public String getLmsRate() {
        return lmsRate;
    }

    public void setLmsRate(String lmsRate) {
        this.lmsRate = lmsRate;
    }

    // 이름순
    public static Comparator<RecentlyViewEntry> PlayDateComparatorAsc = new Comparator<RecentlyViewEntry>() {
        @Override
        public int compare(RecentlyViewEntry lhs, RecentlyViewEntry rhs) {
            String lhsPlayDate = lhs.getPlayDate();
            String rhsPlayDate = rhs.getPlayDate();
            return lhsPlayDate.compareTo(rhsPlayDate);
        }
    };

    // 이름역순
    public static Comparator<RecentlyViewEntry> PlayDateComparatorDes = new Comparator<RecentlyViewEntry>() {
        @Override
        public int compare(RecentlyViewEntry lhs, RecentlyViewEntry rhs) {
            String rhsPlayDate = rhs.getPlayDate();
            String lhsPlayDate = lhs.getPlayDate();
            return rhsPlayDate.compareTo(lhsPlayDate);
        }
    };

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
        dest.writeString(this.lmsRate);
    }

    protected RecentlyViewEntry(Parcel in) {
        this.recentlyId = in.readInt();
        this.contentId = in.readInt();
        this.contentPath = in.readString();
        this.contentName = in.readString();
        this.playDate = in.readString();
        this.lmsRate = in.readString();
    }

    public static final Creator<RecentlyViewEntry> CREATOR = new Creator<RecentlyViewEntry>() {
        @Override
        public RecentlyViewEntry createFromParcel(Parcel source) {
            return new RecentlyViewEntry(source);
        }

        @Override
        public RecentlyViewEntry[] newArray(int size) {
            return new RecentlyViewEntry[size];
        }
    };

    @Override
    public String toString() {
        return "RecentlyViewEntry{" +
                "recentlyId=" + recentlyId +
                ", contentId=" + contentId +
                ", contentPath='" + contentPath + '\'' +
                ", contentName='" + contentName + '\'' +
                ", playDate='" + playDate + '\'' +
                ", lmsRate='" + lmsRate + '\'' +
                '}';
    }
    
}
