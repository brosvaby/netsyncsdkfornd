package com.inka.netsync.ncg.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.inka.netsync.data.network.model.BaseResponseEntry;

/**
 * Created by birdgang on 2018. 5. 4..
 */
public class LicenseEntry implements BaseResponseEntry, Parcelable {

    private String filePath = "";
    private String orderId = "";
    private String message = "";
    private String serialNumber = "";

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.filePath);
        dest.writeString(this.orderId);
        dest.writeString(this.message);
        dest.writeString(this.serialNumber);
    }

    public LicenseEntry() {
    }

    protected LicenseEntry(Parcel in) {
        this.filePath = in.readString();
        this.orderId = in.readString();
        this.message = in.readString();
        this.serialNumber = in.readString();
    }

    public static final Parcelable.Creator<LicenseEntry> CREATOR = new Parcelable.Creator<LicenseEntry>() {
        @Override
        public LicenseEntry createFromParcel(Parcel source) {
            return new LicenseEntry(source);
        }

        @Override
        public LicenseEntry[] newArray(int size) {
            return new LicenseEntry[size];
        }
    };

    @Override
    public String toString() {
        return "LicenseEntry{" +
                "filePath='" + filePath + '\'' +
                ", orderId='" + orderId + '\'' +
                ", message='" + message + '\'' +
                ", serialNumber='" + serialNumber + '\'' +
                '}';
    }
    
}
