package com.inka.netsync.collection;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by birdgang on 2018. 1. 24..
 */

public class UniqueList implements Parcelable {

    private List<String> items = null;

    public UniqueList() {
        items = new ArrayList<>();
    }

    public boolean add (String value) {
        return items.add(value);
    }

    public boolean set (List<String> values) {
        items.clear();
        return items.addAll(values);
    }

    public List<String> getItems () {
        return new ArrayList<>(new HashSet<String>(items));
    }

    @Override
    public String toString() {
        return "UniqueList{" +
                "items=" + items +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(this.items);
    }

    protected UniqueList(Parcel in) {
        this.items = in.createStringArrayList();
    }

    public static final Creator<UniqueList> CREATOR = new Creator<UniqueList>() {
        @Override
        public UniqueList createFromParcel(Parcel source) {
            return new UniqueList(source);
        }

        @Override
        public UniqueList[] newArray(int size) {
            return new UniqueList[size];
        }
    };
}
