package com.inka.netsync.data.network.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.inka.netsync.data.network.model.BaseResponseEntry;

/**
 * Created by birdgang on 2018. 4. 25..
 */

public class MarketVersionCheckResponse implements BaseResponseEntry, Parcelable {
    
    private String packageName = "";
    private String currentAppVersion = "";
    private String marketVersion = "";
    private boolean enableUpdate = false;

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

    public String getMarketVersion() {
        return marketVersion;
    }

    public void setMarketVersion(String marketVersion) {
        this.marketVersion = marketVersion;
    }

    public boolean isEnableUpdate() {
        return enableUpdate;
    }

    public void setEnableUpdate(boolean enableUpdate) {
        this.enableUpdate = enableUpdate;
    }

    @Override
    public String toString() {
        return "MarketVersionCheckResponse{" +
                "packageName='" + packageName + '\'' +
                ", currentAppVersion='" + currentAppVersion + '\'' +
                ", marketVersion='" + marketVersion + '\'' +
                ", enableUpdate=" + enableUpdate +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.packageName);
        dest.writeString(this.currentAppVersion);
        dest.writeString(this.marketVersion);
        dest.writeByte(this.enableUpdate ? (byte) 1 : (byte) 0);
    }

    public MarketVersionCheckResponse() {
    }

    protected MarketVersionCheckResponse(Parcel in) {
        this.packageName = in.readString();
        this.currentAppVersion = in.readString();
        this.marketVersion = in.readString();
        this.enableUpdate = in.readByte() != 0;
    }

    public static final Parcelable.Creator<MarketVersionCheckResponse> CREATOR = new Parcelable.Creator<MarketVersionCheckResponse>() {
        @Override
        public MarketVersionCheckResponse createFromParcel(Parcel source) {
            return new MarketVersionCheckResponse(source);
        }

        @Override
        public MarketVersionCheckResponse[] newArray(int size) {
            return new MarketVersionCheckResponse[size];
        }
    };

}
