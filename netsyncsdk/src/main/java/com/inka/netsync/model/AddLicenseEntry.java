package com.inka.netsync.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.inka.netsync.data.network.model.BaseResponseEntry;

/**
 * Created by birdgang on 2018. 5. 8..
 */
public class AddLicenseEntry implements BaseResponseEntry, Parcelable {

    private int responseCode;
    private String responseMessage;

    private String userId;
    private String userPassword;
    private int contentId;
    private String contentFile;
    private String contentName;
    private String orderId;

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public int getContentId() {
        return contentId;
    }

    public void setContentId(int contentId) {
        this.contentId = contentId;
    }

    public String getContentFile() {
        return contentFile;
    }

    public void setContentFile(String contentFile) {
        this.contentFile = contentFile;
    }

    public String getContentName() {
        return contentName;
    }

    public void setContentName(String contentName) {
        this.contentName = contentName;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public AddLicenseEntry() {
    }

    @Override
    public String toString() {
        return "ResponseLicenseContent{" +
                "responseCode=" + responseCode +
                ", responseMessage='" + responseMessage + '\'' +
                ", userId='" + userId + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", contentId=" + contentId +
                ", contentFile='" + contentFile + '\'' +
                ", contentName='" + contentName + '\'' +
                ", orderId='" + orderId + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.responseCode);
        dest.writeString(this.responseMessage);
        dest.writeString(this.userId);
        dest.writeString(this.userPassword);
        dest.writeInt(this.contentId);
        dest.writeString(this.contentFile);
        dest.writeString(this.contentName);
        dest.writeString(this.orderId);
    }

    protected AddLicenseEntry(Parcel in) {
        this.responseCode = in.readInt();
        this.responseMessage = in.readString();
        this.userId = in.readString();
        this.userPassword = in.readString();
        this.contentId = in.readInt();
        this.contentFile = in.readString();
        this.contentName = in.readString();
        this.orderId = in.readString();
    }

    public static final Parcelable.Creator<AddLicenseEntry> CREATOR = new Parcelable.Creator<AddLicenseEntry>() {
        @Override
        public AddLicenseEntry createFromParcel(Parcel source) {
            return new AddLicenseEntry(source);
        }

        @Override
        public AddLicenseEntry[] newArray(int size) {
            return new AddLicenseEntry[size];
        }
    };
}
