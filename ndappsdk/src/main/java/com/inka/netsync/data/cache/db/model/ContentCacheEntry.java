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
 * Created by birdgang on 2018. 1. 12..
 */
public class ContentCacheEntry implements BaseCacheEntry, Parcelable {

    private static final String TAG = "ContentCacheEntry";

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

    //
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
    private String contentImage = "";

    private boolean hasMore = false;
    private int depth = 0;
    private int childCount = 0;


    public ContentCacheEntry () {}

    public ContentCacheEntry (Cursor cursor) {
        contentId = checkEmptyCursorForInt(cursor, MetaData.ContentColumns.CONTENT_ID);
        directory = checkEmptyCursorForString(cursor, MetaData.ContentColumns.CONTENT_DIRECTORY);
        categoryId = checkEmptyCursorForInt(cursor, MetaData.ContentColumns.CATEGORY_ID);
        playDate = checkEmptyCursorForString(cursor, MetaData.ContentColumns.PLAY_DATE);
        contentDownloadDate = checkEmptyCursorForString(cursor, MetaData.ContentColumns.CONTENT_DOWNLOAD_DATE);
        contentName = checkEmptyCursorForString(cursor, MetaData.ContentColumns.CONTENT_NAME);
        contentFilePath = checkEmptyCursorForString(cursor, MetaData.ContentColumns.CONTENT_FILE_PATH);
        contentLastPlayTime = checkEmptyCursorForString(cursor, MetaData.ContentColumns.CONTENT_LAST_PLAY_TIME);
        isFavoriteContent = checkEmptyCursorForInt(cursor, MetaData.ContentColumns.IS_FAVORITE_CONTENT);
        licenseInfo = checkEmptyCursorForString(cursor, MetaData.ContentColumns.LICENSE_INFO);
        serial = checkEmptyCursorForString(cursor, MetaData.ContentColumns.SERIAL);
    }


    public ContentValues toContentValues() {
        initValues();

        ContentValues values = new ContentValues();

        checkEmptyInt(values, MetaData.ContentColumns.CATEGORY_ID , categoryId);

        values.put(MetaData.ContentColumns.CONTENT_DIRECTORY , directory);
        values.put(MetaData.ContentColumns.PLAY_DATE , playDate);
        values.put(MetaData.ContentColumns.CONTENT_DOWNLOAD_DATE , contentDownloadDate);
        values.put(MetaData.ContentColumns.CONTENT_NAME, contentName);
        values.put(MetaData.ContentColumns.CONTENT_FILE_PATH , contentFilePath);
        values.put(MetaData.ContentColumns.CONTENT_LAST_PLAY_TIME , contentLastPlayTime);
        values.put(MetaData.ContentColumns.IS_FAVORITE_CONTENT , isFavoriteContent);
        values.put(MetaData.ContentColumns.LICENSE_INFO , licenseInfo);
        values.put(MetaData.ContentColumns.SERIAL , serial);
        return values;
    }


    public ContentCacheEntry toContentObject(ContentValues contentValues) {
        setCategoryId(contentValues.getAsInteger(MetaData.ContentColumns.CATEGORY_ID));
        setIsFavoriteContent(contentValues.getAsInteger(MetaData.ContentColumns.IS_FAVORITE_CONTENT));
        setDirectory(contentValues.getAsString(MetaData.ContentColumns.CONTENT_DIRECTORY));
        setPlayDate(contentValues.getAsString(MetaData.ContentColumns.PLAY_DATE));
        setContentDownloadDate(contentValues.getAsString(MetaData.ContentColumns.CONTENT_DOWNLOAD_DATE));
        setContentName(contentValues.getAsString(MetaData.ContentColumns.CONTENT_NAME));
        setContentFilePath(contentValues.getAsString(MetaData.ContentColumns.CONTENT_FILE_PATH));
        setContentLastPlayTime(contentValues.getAsString(MetaData.ContentColumns.CONTENT_LAST_PLAY_TIME));
        setLicenseInfo(contentValues.getAsString(MetaData.ContentColumns.LICENSE_INFO));
        setSerial(contentValues.getAsString(MetaData.ContentColumns.SERIAL));
        return this;
    }


    public ContentCacheEntry copyContent(ContentCacheEntry contentCacheEntry) {
        setContentId(contentCacheEntry.getContentId());
        setCategoryId(contentCacheEntry.getCategoryId());
        setIsFavoriteContent(contentCacheEntry.getIsFavoriteContent());
        setDirectory(contentCacheEntry.getDirectory());
        setPlayDate(contentCacheEntry.getPlayDate());
        setContentDownloadDate(contentCacheEntry.getContentDownloadDate());
        setContentName(contentCacheEntry.getContentName());
        setContentFilePath(contentCacheEntry.getContentFilePath());
        setContentLastPlayTime(contentCacheEntry.getContentLastPlayTime());
        setLicenseInfo(contentCacheEntry.getLicenseInfo());
        setSerial(contentCacheEntry.getSerial());
        return this;
    }

    private static void checkEmptyInt (ContentValues values, String key, int value) {
        if (value > 0) {
            values.put(key, value);
        }
    }

    private static int checkEmptyCursorForInt (Cursor cursor, String contentColumns) {
        int result = -1;
        try {
            result = cursor.getColumnIndexOrThrow(contentColumns);
            //LogUtil.INSTANCE.info("birdgangcontentchache" , "checkEmptyCursorForInt > contentColumns : " + contentColumns);
            if (result > -1) {
                return cursor.getInt(result);
            }
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }

        return result;
    }

    private static String checkEmptyCursorForString (Cursor cursor, String contentColumns) {
        try {
            int result = cursor.getColumnIndexOrThrow(contentColumns);
            //LogUtil.INSTANCE.info("birdgangcontentchache" , "checkEmptyCursorForString > contentColumns : " + contentColumns);
            if (result > -1) {
                return cursor.getString(result);
            }
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }

        return StringUtils.EMPTY;
    }

    private void initValues () {
        if (isFavoriteContent == -1) isFavoriteContent = 0;
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

    public String getFileCapacity() {
        return fileCapacity;
    }

    public void setFileCapacity(String fileCapacity) {
        this.fileCapacity = fileCapacity;
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

    public String getContentImage() {
        return contentImage;
    }

    public void setContentImage(String contentImage) {
        this.contentImage = contentImage;
    }

    public int getStorageType() {
        return storageType;
    }

    public void setStorageType(int storageType) {
        this.storageType = storageType;
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
        dest.writeString(this.contentImage);
        dest.writeByte(this.hasMore ? (byte) 1 : (byte) 0);
        dest.writeInt(this.depth);
        dest.writeInt(this.childCount);
    }

    protected ContentCacheEntry(Parcel in) {
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
        this.contentImage = in.readString();
        this.hasMore = in.readByte() != 0;
        this.depth = in.readInt();
        this.childCount = in.readInt();
    }

    public static final Creator<ContentCacheEntry> CREATOR = new Creator<ContentCacheEntry>() {
        @Override
        public ContentCacheEntry createFromParcel(Parcel source) {
            return new ContentCacheEntry(source);
        }

        @Override
        public ContentCacheEntry[] newArray(int size) {
            return new ContentCacheEntry[size];
        }
    };


    @Override
    public String toString() {
        return "ContentCacheEntry{" +
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
                ", contentImage='" + contentImage + '\'' +
                ", hasMore=" + hasMore +
                ", depth=" + depth +
                ", childCount=" + childCount +
                '}';
    }
}
