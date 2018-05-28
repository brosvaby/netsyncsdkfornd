package com.inka.netsync.data.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by birdgang on 2018. 4. 12..
 */

public class SerialAuth implements Parcelable {

    @Expose
    @SerializedName("response")
    private String response;

    @Expose
    @SerializedName("message")
    private String message;

    @Expose
    @SerializedName("cid")
    private String cid;

    @Expose
    @SerializedName("serialNumber")
    private String serialNumber;

    @Expose
    @SerializedName("logType")
    private String logType;


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
    }

    public SerialAuth() {
    }

    protected SerialAuth(Parcel in) {
        this.response = in.readString();
        this.message = in.readString();
        this.cid = in.readString();
        this.serialNumber = in.readString();
        this.logType = in.readString();
    }

    public static final Creator<SerialAuth> CREATOR = new Creator<SerialAuth>() {
        @Override
        public SerialAuth createFromParcel(Parcel source) {
            return new SerialAuth(source);
        }

        @Override
        public SerialAuth[] newArray(int size) {
            return new SerialAuth[size];
        }
    };
}
