package com.inka.netsync.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.inka.netsync.common.utils.DateTimeUtil;
import com.inka.netsync.logs.LogUtil;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by birdgang on 2018. 1. 18..
 */
public class ContentViewEntry implements BaseViewEntry, Parcelable {

    public enum ContentType {
        VIDEO("VIDEO"),
        AUDIO("AUDIO"),
        DOC("DOC"),
        GROUP("GROUP");

        private String type;

        ContentType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    private int storageType;
    private String mediaType;
    private String directory;
    private String data;
    private String fileCapacity;

    private int contentId = -1;
    private int categoryId = -1;
    private String playDate = "";
    private String contentDownloadDate = "";
    private String contentName = "";
    private String parentsFilePath = "";
    private String contentFilePath = "";
    private String contentLastPlayTime = "";
    private int isFavoriteContent = 0;
    private String licenseInfo = "";
    private String serial = "";
    private String lmsRate = "";

    private int progressbarDrawableForLms = -1;
    private int textRateDimColorForLms = -1;
    private int textRateColorForLms = -1;

    private boolean hasMore = false;
    private int depth = 0;
    private int childCount = 0;


    public ContentViewEntry () {}

    public void copyContentViewEntry (ContentViewEntry entry) {
        setContentId(entry.getContentId());
        setDirectory(entry.getDirectory());
        setCategoryId(entry.getCategoryId());
        setPlayDate(entry.getPlayDate());
        setContentDownloadDate(entry.getContentDownloadDate());
        setContentName(entry.getContentName());
        setContentFilePath(entry.getContentFilePath());
        setMediaType(entry.getMediaType());
        setContentLastPlayTime(entry.getContentLastPlayTime());
        setIsFavoriteContent(entry.getIsFavoriteContent());
        setLicenseInfo(entry.getLicenseInfo());
        setSerial(entry.getSerial());
        setParentsFilePath(entry.getParentsFilePath());
        setHasMore(entry.isHasMore());
        setChildCount(entry.getChildCount());
    }

    public int getStorageType() {
        return storageType;
    }

    public void setStorageType(int storageType) {
        this.storageType = storageType;
    }

    public String getFileCapacity() {
        return fileCapacity;
    }

    public void setFileCapacity(String fileCapacity) {
        this.fileCapacity = fileCapacity;
    }

    public int getContentId() {
        return contentId;
    }

    public void setContentId(int contentId) {
        this.contentId = contentId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getPlayDate() {
        return playDate;
    }

    public void setPlayDate(String playDate) {
        this.playDate = playDate;
    }

    public String getContentDownloadDate() {
        return contentDownloadDate;
    }

    public void setContentDownloadDate(String contentDownloadDate) {
        this.contentDownloadDate = contentDownloadDate;
    }

    public String getContentName() {
        return contentName;
    }

    public void setContentName(String contentName) {
        this.contentName = contentName;
    }

    public String getParentsFilePath() {
        return parentsFilePath;
    }

    public void setParentsFilePath(String parentsFilePath) {
        this.parentsFilePath = parentsFilePath;
    }

    public String getContentFilePath() {
        return contentFilePath;
    }

    public void setContentFilePath(String contentFilePath) {
        this.contentFilePath = contentFilePath;
    }

    public String getContentLastPlayTime() {
        return contentLastPlayTime;
    }

    public void setContentLastPlayTime(String contentLastPlayTime) {
        this.contentLastPlayTime = contentLastPlayTime;
    }

    public int getIsFavoriteContent() {
        return isFavoriteContent;
    }

    public void setIsFavoriteContent(int isFavoriteContent) {
        this.isFavoriteContent = isFavoriteContent;
    }

    public String getLicenseInfo() {
        return licenseInfo;
    }

    public void setLicenseInfo(String licenseInfo) {
        this.licenseInfo = licenseInfo;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getLmsRate() {
        return lmsRate;
    }

    public void setLmsRate(String lmsRate) {
        this.lmsRate = lmsRate;
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


    public boolean isHasMore() {
        return hasMore;
    }

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public int getChildCount() {
        return childCount;
    }

    public void setChildCount(int childCount) {
        this.childCount = childCount;
    }

    public int getProgressbarDrawableForLms() {
        return progressbarDrawableForLms;
    }

    public void setProgressbarDrawableForLms(int progressbarDrawableForLms) {
        this.progressbarDrawableForLms = progressbarDrawableForLms;
    }

    public int getTextRateDimColorForLms() {
        return textRateDimColorForLms;
    }

    public void setTextRateDimColorForLms(int textRateDimColorForLms) {
        this.textRateDimColorForLms = textRateDimColorForLms;
    }

    public int getTextRateColorForLms() {
        return textRateColorForLms;
    }

    public void setTextRateColorForLms(int textRateColorForLms) {
        this.textRateColorForLms = textRateColorForLms;
    }

    // 이름순
    public static class SortContentNameAscCompare implements Comparator<ContentViewEntry> {
        @Override
        public int compare(ContentViewEntry media1, ContentViewEntry media2) {
            String lhsName = media1.getContentName();
            String rhsName = media2.getContentName();
            return lhsName.compareTo(rhsName);
        }
    }

    // 이름역순
    public static class SortContentNameDescCompare implements Comparator<ContentViewEntry> {
        @Override
        public int compare(ContentViewEntry media1, ContentViewEntry media2) {
            String lhsName = media1.getContentName();
            String rhsName = media2.getContentName();
            return rhsName.compareTo(lhsName);
        }
    }

    // 날짜순
    public static class SortContentDateAscCompare implements Comparator<ContentViewEntry> {
        @Override
        public int compare(ContentViewEntry media1, ContentViewEntry media2) {
            Date lhsDate = media1.getPlayDateToDate();
            Date rhsDate = media2.getPlayDateToDate();

            return lhsDate.compareTo(rhsDate);
        }
    }

    // 날짜 역순
    public static class SortContentDateDescCompare implements Comparator<ContentViewEntry> {
        @Override
        public int compare(ContentViewEntry media1, ContentViewEntry media2) {
            Date lhsDate = media1.getPlayDateToDate();
            Date rhsDate = media2.getPlayDateToDate();
            return rhsDate.compareTo(lhsDate);
        }
    }


    // 파일크기순
    public static class SortContentFileSizeAscCompare implements Comparator<ContentViewEntry> {
        @Override
        public int compare(ContentViewEntry media1, ContentViewEntry media2) {
            File file = new File(media1.getContentFilePath());
            long size = file != null ? file.length() : 0;
            File file2 = new File(media2.getContentFilePath());
            long size2 = file2 != null ? file2.length() : 0;
            return (size > size2 ? 1 : (size == size2 ? 0 : -1));
        }
    }

    // 파일크기 역순
    public static class SortContentFileSizeDescCompare implements Comparator<ContentViewEntry> {
        @Override
        public int compare(ContentViewEntry media1, ContentViewEntry media2) {
            File file = new File(media1.getContentFilePath());
            long size = file != null ? file.length() : 0;
            File file2 = new File(media2.getContentFilePath());
            long size2 = file2 != null ? file2.length() : 0;
            return -(size > size2 ? 1 : (size == size2 ? 0 : -1));
        }
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.storageType);
        dest.writeString(this.mediaType);
        dest.writeString(this.directory);
        dest.writeString(this.data);
        dest.writeString(this.fileCapacity);
        dest.writeInt(this.contentId);
        dest.writeInt(this.categoryId);
        dest.writeString(this.playDate);
        dest.writeString(this.contentDownloadDate);
        dest.writeString(this.contentName);
        dest.writeString(this.parentsFilePath);
        dest.writeString(this.contentFilePath);
        dest.writeString(this.contentLastPlayTime);
        dest.writeInt(this.isFavoriteContent);
        dest.writeString(this.licenseInfo);
        dest.writeString(this.serial);
        dest.writeString(this.lmsRate);
        dest.writeInt(this.progressbarDrawableForLms);
        dest.writeInt(this.textRateDimColorForLms);
        dest.writeInt(this.textRateColorForLms);
        dest.writeByte(this.hasMore ? (byte) 1 : (byte) 0);
        dest.writeInt(this.depth);
        dest.writeInt(this.childCount);
    }

    protected ContentViewEntry(Parcel in) {
        this.storageType = in.readInt();
        this.mediaType = in.readString();
        this.directory = in.readString();
        this.data = in.readString();
        this.fileCapacity = in.readString();
        this.contentId = in.readInt();
        this.categoryId = in.readInt();
        this.playDate = in.readString();
        this.contentDownloadDate = in.readString();
        this.contentName = in.readString();
        this.parentsFilePath = in.readString();
        this.contentFilePath = in.readString();
        this.contentLastPlayTime = in.readString();
        this.isFavoriteContent = in.readInt();
        this.licenseInfo = in.readString();
        this.serial = in.readString();
        this.lmsRate = in.readString();
        this.progressbarDrawableForLms = in.readInt();
        this.textRateDimColorForLms = in.readInt();
        this.textRateColorForLms = in.readInt();
        this.hasMore = in.readByte() != 0;
        this.depth = in.readInt();
        this.childCount = in.readInt();
    }

    public static final Creator<ContentViewEntry> CREATOR = new Creator<ContentViewEntry>() {
        @Override
        public ContentViewEntry createFromParcel(Parcel source) {
            return new ContentViewEntry(source);
        }

        @Override
        public ContentViewEntry[] newArray(int size) {
            return new ContentViewEntry[size];
        }
    };

    @Override
    public String toString() {
        return "ContentViewEntry{" +
                "storageType=" + storageType +
                ", mediaType='" + mediaType + '\'' +
                ", directory='" + directory + '\'' +
                ", data='" + data + '\'' +
                ", fileCapacity='" + fileCapacity + '\'' +
                ", contentId=" + contentId +
                ", categoryId=" + categoryId +
                ", playDate='" + playDate + '\'' +
                ", contentDownloadDate='" + contentDownloadDate + '\'' +
                ", contentName='" + contentName + '\'' +
                ", parentsFilePath='" + parentsFilePath + '\'' +
                ", contentFilePath='" + contentFilePath + '\'' +
                ", contentLastPlayTime='" + contentLastPlayTime + '\'' +
                ", isFavoriteContent=" + isFavoriteContent +
                ", licenseInfo='" + licenseInfo + '\'' +
                ", serial='" + serial + '\'' +
                ", lmsRate='" + lmsRate + '\'' +
                ", progressbarDrawableForLms=" + progressbarDrawableForLms +
                ", textRateDimColorForLms=" + textRateDimColorForLms +
                ", textRateColorForLms=" + textRateColorForLms +
                ", hasMore=" + hasMore +
                ", depth=" + depth +
                ", childCount=" + childCount +
                '}';
    }

}