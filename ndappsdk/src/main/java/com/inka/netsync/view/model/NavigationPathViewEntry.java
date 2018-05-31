package com.inka.netsync.view.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by birdgang on 2018. 1. 23..
 */

public class NavigationPathViewEntry implements BaseViewEntry, Parcelable {

    private int step = 0;
    private int depth = 0;
    private String name = "";
    private String fullPath = "";
    private String path = "";

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullPath() {
        return fullPath;
    }

    public void setFullPath(String fullPath) {
        this.fullPath = fullPath;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }


    public NavigationPathViewEntry() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.step);
        dest.writeInt(this.depth);
        dest.writeString(this.name);
        dest.writeString(this.fullPath);
        dest.writeString(this.path);
    }

    protected NavigationPathViewEntry(Parcel in) {
        this.step = in.readInt();
        this.depth = in.readInt();
        this.name = in.readString();
        this.fullPath = in.readString();
        this.path = in.readString();
    }

    public static final Creator<NavigationPathViewEntry> CREATOR = new Creator<NavigationPathViewEntry>() {
        @Override
        public NavigationPathViewEntry createFromParcel(Parcel source) {
            return new NavigationPathViewEntry(source);
        }

        @Override
        public NavigationPathViewEntry[] newArray(int size) {
            return new NavigationPathViewEntry[size];
        }
    };

    @Override
    public String toString() {
        return "NavigationPathViewEntry{" +
                "step=" + step +
                ", depth=" + depth +
                ", name='" + name + '\'' +
                ", fullPath='" + fullPath + '\'' +
                ", path='" + path + '\'' +
                '}';
    }

}