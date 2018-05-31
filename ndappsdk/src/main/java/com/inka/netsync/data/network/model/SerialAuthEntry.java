package com.inka.netsync.data.network.model;

import android.os.Parcel;
import android.os.Parcelable;

public class SerialAuthEntry implements Parcelable {

    private String requestUrl = "";
    private String serialNumber = "";
    private String contentId = "";
    private String requestContentId = "";
    private String filePath = "";
    private String deviceID = "";
    private String deviceModel = "";
    private String appVersion = "";
    private String serialAuthenticationUrl = "";

    private String key = "";
    private String iv = "";
    private String enterpriseCode = "";

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getRequestContentId() {
        return requestContentId;
    }

    public void setRequestContentId(String requestContentId) {
        this.requestContentId = requestContentId;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getSerialAuthenticationUrl() {
        return serialAuthenticationUrl;
    }

    public void setSerialAuthenticationUrl(String serialAuthenticationUrl) {
        this.serialAuthenticationUrl = serialAuthenticationUrl;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getIv() {
        return iv;
    }

    public void setIv(String iv) {
        this.iv = iv;
    }

    public String getEnterpriseCode() {
        return enterpriseCode;
    }

    public void setEnterpriseCode(String enterpriseCode) {
        this.enterpriseCode = enterpriseCode;
    }


    public SerialAuthEntry() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.requestUrl);
        dest.writeString(this.serialNumber);
        dest.writeString(this.contentId);
        dest.writeString(this.requestContentId);
        dest.writeString(this.filePath);
        dest.writeString(this.deviceID);
        dest.writeString(this.deviceModel);
        dest.writeString(this.appVersion);
        dest.writeString(this.serialAuthenticationUrl);
        dest.writeString(this.key);
        dest.writeString(this.iv);
        dest.writeString(this.enterpriseCode);
    }

    protected SerialAuthEntry(Parcel in) {
        this.requestUrl = in.readString();
        this.serialNumber = in.readString();
        this.contentId = in.readString();
        this.requestContentId = in.readString();
        this.filePath = in.readString();
        this.deviceID = in.readString();
        this.deviceModel = in.readString();
        this.appVersion = in.readString();
        this.serialAuthenticationUrl = in.readString();
        this.key = in.readString();
        this.iv = in.readString();
        this.enterpriseCode = in.readString();
    }

    public static final Creator<SerialAuthEntry> CREATOR = new Creator<SerialAuthEntry>() {
        @Override
        public SerialAuthEntry createFromParcel(Parcel source) {
            return new SerialAuthEntry(source);
        }

        @Override
        public SerialAuthEntry[] newArray(int size) {
            return new SerialAuthEntry[size];
        }
    };

    @Override
    public String toString() {
        return "SerialAuthEntry{" +
                "requestUrl='" + requestUrl + '\'' +
                ", serialNumber='" + serialNumber + '\'' +
                ", contentId='" + contentId + '\'' +
                ", requestContentId='" + requestContentId + '\'' +
                ", filePath='" + filePath + '\'' +
                ", deviceID='" + deviceID + '\'' +
                ", deviceModel='" + deviceModel + '\'' +
                ", appVersion='" + appVersion + '\'' +
                ", serialAuthenticationUrl='" + serialAuthenticationUrl + '\'' +
                ", key='" + key + '\'' +
                ", iv='" + iv + '\'' +
                ", enterpriseCode='" + enterpriseCode + '\'' +
                '}';
    }

}
