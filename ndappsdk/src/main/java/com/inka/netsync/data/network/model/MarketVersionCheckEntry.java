package com.inka.netsync.data.network.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by birdgang on 2018. 4. 25..
 */

public class MarketVersionCheckEntry implements Parcelable {

    private String packageName = "";
    private String currentAppVersion = "";

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getCurrentAppVersion() {
        return currentAppVersion;
    }

    public void setCurrentAppVersion(String currentAppVersion) {
        this.currentAppVersion = currentAppVersion;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.packageName);
        dest.writeString(this.currentAppVersion);
    }

    public MarketVersionCheckEntry() {
    }

    protected MarketVersionCheckEntry(Parcel in) {
        this.packageName = in.readString();
        this.currentAppVersion = in.readString();
    }

    public static final Parcelable.Creator<MarketVersionCheckEntry> CREATOR = new Parcelable.Creator<MarketVersionCheckEntry>() {
        @Override
        public MarketVersionCheckEntry createFromParcel(Parcel source) {
            return new MarketVersionCheckEntry(source);
        }

        @Override
        public MarketVersionCheckEntry[] newArray(int size) {
            return new MarketVersionCheckEntry[size];
        }
    };


    @Override
    public String toString() {
        return "MarketVersionCheckEntry{" +
                "packageName='" + packageName + '\'' +
                ", currentAppVersion='" + currentAppVersion + '\'' +
                '}';
    }

}