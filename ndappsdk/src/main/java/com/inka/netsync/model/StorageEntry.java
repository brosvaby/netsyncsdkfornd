package com.inka.netsync.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by birdgang on 2017. 7. 20..
 */
public class StorageEntry implements BaseEntry, Parcelable {

    public enum StorageType {
        INTERNAL("INTERNAL"),
        EXTERNAL("EXTERNAL"),
        OTG("OTG");

        private String type;

        StorageType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }
    }

    private int storageType;
    private String name;
    private int count;
    private long size;
    private String path;

    public StorageEntry (int type, String name, int count, long size, String path) {
        this.storageType = type;
        this.name = name;
        this.count = count;
        this.size = size;
        this.path = path;
    }

    public int getStorageType() {
        return storageType;
    }

    public void setStorageType(int storageType) {
        this.storageType = storageType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }


    @Override
    public String toString() {
        return "StorageEntry{" +
                "storageType=" + storageType +
                ", name='" + name + '\'' +
                ", count=" + count +
                ", size=" + size +
                ", path='" + path + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.storageType);
        dest.writeString(this.name);
        dest.writeInt(this.count);
        dest.writeLong(this.size);
        dest.writeString(this.path);
    }

    protected StorageEntry(Parcel in) {
        this.storageType = in.readInt();
        this.name = in.readString();
        this.count = in.readInt();
        this.size = in.readLong();
        this.path = in.readString();
    }

    public static final Creator<StorageEntry> CREATOR = new Creator<StorageEntry>() {
        @Override
        public StorageEntry createFromParcel(Parcel source) {
            return new StorageEntry(source);
        }

        @Override
        public StorageEntry[] newArray(int size) {
            return new StorageEntry[size];
        }
    };
}