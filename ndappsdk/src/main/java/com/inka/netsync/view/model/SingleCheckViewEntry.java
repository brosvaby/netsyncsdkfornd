package com.inka.netsync.view.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by birdgang on 2018. 2. 7..
 */

public class SingleCheckViewEntry implements BaseViewEntry, Parcelable {

    public int id = -1;
    public String title = "";
    public boolean selected = false;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
    
    public SingleCheckViewEntry(int id, String title, boolean selected) {
        this.id = id;
        this.title = title;
        this.selected = selected;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeByte(this.selected ? (byte) 1 : (byte) 0);
    }


    protected SingleCheckViewEntry(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.selected = in.readByte() != 0;
    }

    public static final Creator<SingleCheckViewEntry> CREATOR = new Creator<SingleCheckViewEntry>() {
        @Override
        public SingleCheckViewEntry createFromParcel(Parcel source) {
            return new SingleCheckViewEntry(source);
        }

        @Override
        public SingleCheckViewEntry[] newArray(int size) {
            return new SingleCheckViewEntry[size];
        }
    };


    @Override
    public String toString() {
        return "SingleCheckViewEntry{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", selected=" + selected +
                '}';
    }

}
