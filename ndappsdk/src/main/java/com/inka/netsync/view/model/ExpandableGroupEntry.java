package com.inka.netsync.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by birdgang on 2018. 2. 22..
 */

public class ExpandableGroupEntry<T extends Parcelable> implements Parcelable {

    private String title;
    private List<T> items;

    public ExpandableGroupEntry(String title, List<T> items) {
        this.title = title;
        this.items = items;
    }

    public String getTitle() {
        return title;
    }

    public List<T> getItems() {
        return items;
    }

    public int getItemCount() {
        return items == null ? 0 : items.size();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeTypedList(this.items);
    }

    protected ExpandableGroupEntry(Parcel in) {
        title = in.readString();
        byte hasItems = in.readByte();
        int size = in.readInt();
        if (hasItems == 0x01) {
            items = new ArrayList<T>(size);
            Class<?> type = (Class<?>) in.readSerializable();
            in.readList(items, type.getClassLoader());
        } else {
            items = null;
        }
    }

    public static final Creator<ExpandableGroupEntry> CREATOR = new Creator<ExpandableGroupEntry>() {
        @Override
        public ExpandableGroupEntry createFromParcel(Parcel source) {
            return new ExpandableGroupEntry(source);
        }

        @Override
        public ExpandableGroupEntry[] newArray(int size) {
            return new ExpandableGroupEntry[size];
        }
    };
}
