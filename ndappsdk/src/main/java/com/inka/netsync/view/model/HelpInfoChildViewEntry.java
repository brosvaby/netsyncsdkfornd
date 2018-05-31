package com.inka.netsync.view.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by birdgang on 2018. 2. 22..
 */

public class HelpInfoChildViewEntry implements Parcelable {

    private String name;

    public HelpInfoChildViewEntry(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
    }

    protected HelpInfoChildViewEntry(Parcel in) {
        this.name = in.readString();
    }

    public static final Creator<HelpInfoChildViewEntry> CREATOR = new Creator<HelpInfoChildViewEntry>() {
        @Override
        public HelpInfoChildViewEntry createFromParcel(Parcel source) {
            return new HelpInfoChildViewEntry(source);
        }

        @Override
        public HelpInfoChildViewEntry[] newArray(int size) {
            return new HelpInfoChildViewEntry[size];
        }
    };

}
