package com.inka.netsync.media.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by birdgang on 2017. 9. 29..
 */

public class ScanEntry implements Parcelable {

    protected String contentName = "";
    protected String contentFilePath = "";
    protected String directory = "";

    public String getContentName() {
        return contentName;
    }

    public void setContentName(String contentName) {
        this.contentName = contentName;
    }

    public String getContentFilePath() {
        return contentFilePath;
    }

    public void setContentFilePath(String contentFilePath) {
        this.contentFilePath = contentFilePath;
    }

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.contentName);
        dest.writeString(this.contentFilePath);
        dest.writeString(this.directory);
    }

    public ScanEntry() {
    }

    protected ScanEntry(Parcel in) {
        this.contentName = in.readString();
        this.contentFilePath = in.readString();
        this.directory = in.readString();
    }

    public static final Creator<ScanEntry> CREATOR = new Creator<ScanEntry>() {
        @Override
        public ScanEntry createFromParcel(Parcel source) {
            return new ScanEntry(source);
        }

        @Override
        public ScanEntry[] newArray(int size) {
            return new ScanEntry[size];
        }
    };

    @Override
    public String toString() {
        return "ScanEntry{" +
                "contentName='" + contentName + '\'' +
                ", contentFilePath='" + contentFilePath + '\'' +
                ", directory='" + directory + '\'' +
                '}';
    }

}
