package com.inka.netsync.model;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

import com.inka.netsync.common.utils.DateTimeUtil;
import com.inka.netsync.data.cache.db.MetaData;
import com.inka.netsync.data.cache.db.model.ContentCacheEntry;
import com.inka.netsync.lms.model.LmsEntry;
import com.inka.netsync.logs.LogUtil;
import com.inka.netsync.view.model.ContentViewEntry;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

public class ContentEntry implements BaseEntry, Parcelable {

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

	public void setMediaTypeByFilePath (String contentFormatName) {
		if (StringUtils.contains(contentFormatName, "mp4")) {
			setMediaType(ContentEntry.ContentType.VIDEO.getType());
		} else if (StringUtils.contains(contentFormatName, "mp3")) {
			setMediaType(ContentEntry.ContentType.AUDIO.getType());
		} else if (StringUtils.contains(contentFormatName, "pdf") || StringUtils.contains(contentFormatName, "PDF")) {
			setMediaType(ContentEntry.ContentType.DOC.getType());
		} else {
			setMediaType(ContentEntry.ContentType.GROUP.getType());
		}
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
	private String lmsRate = "";

	private boolean hasMore = false;
	private int depth = 0;
	private int childCount = 0;


	private ContentCacheEntry contentCacheEntry;
	private LmsEntry lmsEntry;

	public ContentEntry () {
		contentCacheEntry = new ContentCacheEntry();
		lmsEntry = new LmsEntry();
	}


	public ContentEntry (ContentCacheEntry contentCacheEntry) throws Exception {
		this();

		setContentId(contentCacheEntry.getContentId());
		setDirectory(contentCacheEntry.getDirectory());
		setCategoryId(contentCacheEntry.getCategoryId());
		setPlayDate(contentCacheEntry.getPlayDate());
		setContentDownloadDate(contentCacheEntry.getContentDownloadDate());
		setContentName(contentCacheEntry.getContentName());
		setContentFilePath(contentCacheEntry.getContentFilePath());
		setMediaTypeByFilePath(contentCacheEntry.getContentName());
		setContentLastPlayTime(contentCacheEntry.getContentLastPlayTime());
		setIsFavoriteContent(contentCacheEntry.getIsFavoriteContent());
		setLicenseInfo(contentCacheEntry.getLicenseInfo());
		setSerial(contentCacheEntry.getSerial());
		setParentsFilePath(contentCacheEntry.getParentsFilePath());
		setHasMore(contentCacheEntry.isHasMore());
		setChildCount(contentCacheEntry.getChildCount());
	}

	public ContentEntry (ContentViewEntry contentViewEntry) {
		this();

		setContentId(contentViewEntry.getContentId());
		setDirectory(contentViewEntry.getDirectory());
		setCategoryId(contentViewEntry.getCategoryId());
		setPlayDate(contentViewEntry.getPlayDate());
		setContentDownloadDate(contentViewEntry.getContentDownloadDate());
		setContentName(contentViewEntry.getContentName());
		setContentFilePath(contentViewEntry.getContentFilePath());
		setMediaType(contentViewEntry.getMediaType());
		setContentLastPlayTime(contentViewEntry.getContentLastPlayTime());
		setIsFavoriteContent(contentViewEntry.getIsFavoriteContent());
		setLicenseInfo(contentViewEntry.getLicenseInfo());
		setSerial(contentViewEntry.getSerial());
		setParentsFilePath(contentViewEntry.getParentsFilePath());
		setHasMore(contentViewEntry.isHasMore());
		setChildCount(contentViewEntry.getChildCount());
	}


	public ContentCacheEntry convertCacheEntry () {
		ContentCacheEntry contentCacheEntry = new ContentCacheEntry();
		contentCacheEntry.setContentId(contentId);
		contentCacheEntry.setDirectory(directory);
		contentCacheEntry.setCategoryId(categoryId);
		contentCacheEntry.setPlayDate(playDate);
		contentCacheEntry.setContentDownloadDate(contentDownloadDate);
		contentCacheEntry.setContentName(contentName);
		contentCacheEntry.setContentFilePath(contentFilePath);
		contentCacheEntry.setMediaType(mediaType);
		contentCacheEntry.setContentLastPlayTime(contentLastPlayTime);
		contentCacheEntry.setIsFavoriteContent(isFavoriteContent);
		contentCacheEntry.setLicenseInfo(licenseInfo);
		contentCacheEntry.setSerial(serial);
		contentCacheEntry.setParentsFilePath(parentsFilePath);
		contentCacheEntry.setHasMore(hasMore);
		contentCacheEntry.setChildCount(childCount);
		return contentCacheEntry;
	}


	public ContentViewEntry convertViewEntry () {
		ContentViewEntry contentViewEntry = new ContentViewEntry();
		contentViewEntry.setContentId(contentId);
		contentViewEntry.setDirectory(directory);
		contentViewEntry.setCategoryId(categoryId);
		contentViewEntry.setPlayDate(playDate);
		contentViewEntry.setContentDownloadDate(contentDownloadDate);
		contentViewEntry.setContentName(contentName);
		contentViewEntry.setContentFilePath(contentFilePath);
		contentViewEntry.setMediaType(mediaType);
		contentViewEntry.setContentLastPlayTime(contentLastPlayTime);
		contentViewEntry.setIsFavoriteContent(isFavoriteContent);
		contentViewEntry.setLicenseInfo(licenseInfo);
		contentViewEntry.setSerial(serial);
		contentViewEntry.setParentsFilePath(parentsFilePath);
		contentViewEntry.setHasMore(hasMore);
		contentViewEntry.setChildCount(childCount);
		contentViewEntry.setLmsRate(lmsEntry.getRate());
		return contentViewEntry;
	}



	public ContentValues toContentValues() {
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


	private static void checkEmptyInt (ContentValues values, String key, int value) {
		if (value > 0) {
			values.put(key, value);
		}
	}

	private void initValues () {
		if (isFavoriteContent == -1) isFavoriteContent = 0;
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

	public String getContentImage() {
		return contentImage;
	}

	public void setContentImage(String contentImage) {
		this.contentImage = contentImage;
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

	public LmsEntry getLmsEntry() {
		return lmsEntry;
	}

	public void setLmsEntry(LmsEntry lmsEntry) {
		this.lmsEntry = lmsEntry;
	}

	// 이름순
	public static class SortContentNameAscCompare implements Comparator<ContentEntry> {
		@Override
		public int compare(ContentEntry media1, ContentEntry media2) {
			String lhsName = media1.getContentName();
			String rhsName = media2.getContentName();
			return lhsName.compareTo(rhsName);
		}
	}

	// 이름역순
	public static class SortContentNameDescCompare implements Comparator<ContentEntry> {
		@Override
		public int compare(ContentEntry media1, ContentEntry media2) {
			String lhsName = media1.getContentName();
			String rhsName = media2.getContentName();
			return rhsName.compareTo(lhsName);
		}
	}

	// 날짜순
	public static class SortContentDateAscCompare implements Comparator<ContentCacheEntry> {
		@Override
		public int compare(ContentCacheEntry media1, ContentCacheEntry media2) {
			Date lhsDate = media1.getPlayDateToDate();
			Date rhsDate = media2.getPlayDateToDate();

			return lhsDate.compareTo(rhsDate);
		}
	}

	// 날짜 역순
	public static class SortContentDateDescCompare implements Comparator<ContentEntry> {
		@Override
		public int compare(ContentEntry media1, ContentEntry media2) {
			Date lhsDate = media1.getPlayDateToDate();
			Date rhsDate = media2.getPlayDateToDate();
			return rhsDate.compareTo(lhsDate);
		}
	}

	// 파일크기순
	public static class SortContentFileSizeAscCompare implements Comparator<ContentEntry> {
		@Override
		public int compare(ContentEntry media1, ContentEntry media2) {
			File file = new File(media1.getContentFilePath());
			long size = file != null ? file.length() : 0;
			File file2 = new File(media2.getContentFilePath());
			long size2 = file2 != null ? file2.length() : 0;
			return (size > size2 ? 1 : (size == size2 ? 0 : -1));
		}
	}

	// 파일크기 역순
	public static class SortContentFileSizeDescCompare implements Comparator<ContentEntry> {
		@Override
		public int compare(ContentEntry media1, ContentEntry media2) {
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
		dest.writeString(this.contentImage);
		dest.writeString(this.lmsRate);
		dest.writeByte(this.hasMore ? (byte) 1 : (byte) 0);
		dest.writeInt(this.depth);
		dest.writeInt(this.childCount);
		dest.writeParcelable(this.contentCacheEntry, flags);
		dest.writeParcelable(this.lmsEntry, flags);
	}

	protected ContentEntry(Parcel in) {
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
		this.lmsRate = in.readString();
		this.hasMore = in.readByte() != 0;
		this.depth = in.readInt();
		this.childCount = in.readInt();
		this.contentCacheEntry = in.readParcelable(ContentCacheEntry.class.getClassLoader());
		this.lmsEntry = in.readParcelable(LmsEntry.class.getClassLoader());
	}

	public static final Creator<ContentEntry> CREATOR = new Creator<ContentEntry>() {
		@Override
		public ContentEntry createFromParcel(Parcel source) {
			return new ContentEntry(source);
		}

		@Override
		public ContentEntry[] newArray(int size) {
			return new ContentEntry[size];
		}
	};

	@Override
	public String toString() {
		return "ContentEntry{" +
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
				", lmsRate='" + lmsRate + '\'' +
				", hasMore=" + hasMore +
				", depth=" + depth +
				", childCount=" + childCount +
				", contentCacheEntry=" + contentCacheEntry +
				", lmsEntry=" + lmsEntry +
				'}';
	}

}