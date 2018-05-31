package com.inka.netsync.data.network.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ResponseSerialAuthEntry implements BaseResponseEntry, Parcelable {

    private String response = "";
    private String message = "";
    private String cid = "";
    private String serialNumber = "";
    private String logType = "";
    private String orginFilePat = "";

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getLogType() {
        return logType;
    }

    public void setLogType(String logType) {
        this.logType = logType;
    }

    public String getOrginFilePat() {
        return orginFilePat;
    }

    public void setOrginFilePat(String orginFilePat) {
        this.orginFilePat = orginFilePat;
    }

    public ResponseSerialAuthEntry() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.response);
        dest.writeString(this.message);
        dest.writeString(this.cid);
        dest.writeString(this.serialNumber);
        dest.writeString(this.logType);
        dest.writeString(this.orginFilePat);
    }

    protected ResponseSerialAuthEntry(Parcel in) {
        this.response = in.readString();
        this.message = in.readString();
        this.cid = in.readString();
        this.serialNumber = in.readString();
        this.logType = in.readString();
        this.orginFilePat = in.readString();
    }

    public static final Creator<ResponseSerialAuthEntry> CREATOR = new Creator<ResponseSerialAuthEntry>() {
        @Override
        public ResponseSerialAuthEntry createFromParcel(Parcel source) {
            return new ResponseSerialAuthEntry(source);
        }

        @Override
        public ResponseSerialAuthEntry[] newArray(int size) {
            return new ResponseSerialAuthEntry[size];
        }
    };

    @Override
    public String toString() {
        return "ResponseSerialAuthEntry{" +
                "response='" + response + '\'' +
                ", message='" + message + '\'' +
                ", cid='" + cid + '\'' +
                ", serialNumber='" + serialNumber + '\'' +
                ", logType='" + logType + '\'' +
                ", orginFilePat='" + orginFilePat + '\'' +
                '}';
    }

}
