package com.inka.netsync.view.model;


import android.os.Parcel;
import android.os.Parcelable;

public class DrawerMenuViewEntry implements BaseViewEntry, Parcelable {

    public final static String TAG_DEFAULT = "TAG_DEFAULT";
    public final static String TAG_HOME = "TAG_HOME";
    public final static String TAG_WEBVIEW = "TAG_WEBVIEW";
    public final static String TAG_STORAGE = "TAG_STORAGE";
    public final static String TAG_INTERNAL_STORAGE = "TAG_INTERNAL_STORAGE";
    public final static String TAG_EXTERNAL_STORAGE = "TAG_EXTERNAL_STORAGE";
    public final static String TAG_OTG_STORAGE = "TAG_OTG_STORAGE";
    public final static String TAG_PLAYEDLIST = "TAG_FUNCTION_PLAYEDLIST";
    public final static String TAG_FAVORITE	= "TAG_FUNCTION_FAVORITE";
    public final static String TAG_INFORMATION_3RDPATY	= "TAG_INFORMATION_3RDPATY";
    public final static String TAG_INFORMATION_SETTING	= "TAG_INFORMATION_SETTING";
    public final static String TAG_INFORMATION_HELP	= "TAG_INFORMATION_HELP";

    public final static int STATE_DISABLE = 0;
    public final static int STATE_ENABLE = 1;

    public String 	mTabTag		= "";
    public String 	mTitle 		= "";
    public int		mResDrawable = -1;
    public int 		mResColor;
    public int		mResTextColor;

    public boolean mHasSelected = false;

    public int mStateMenu = STATE_ENABLE;


    private int drawerMenuType;

    public enum DrawerMenuType {
        SECTION("SECTION"),
        ITEM("ITEM");

        private String type;

        DrawerMenuType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }
    }

    public DrawerMenuViewEntry(int type, String tabTag, String title, int resDrawable, int state) {
        drawerMenuType = type;
        mTabTag = tabTag;
        mTitle = title;
        mResDrawable = resDrawable;
        mStateMenu = state;
    }

    public int getDrawerMenuType() {
        return drawerMenuType;
    }

    public void setDrawerMenuType(int drawerMenuType) {
        this.drawerMenuType = drawerMenuType;
    }

    public int getStateMenu() {
        return mStateMenu;
    }

    public void setStateMenu(int stateMenu) {
        this.mStateMenu = stateMenu;
    }

    public boolean isHasSelected() {
        return mHasSelected;
    }

    public void setHasSelected(boolean hasSelected) {
        this.mHasSelected = hasSelected;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mTabTag);
        dest.writeString(this.mTitle);
        dest.writeInt(this.mResDrawable);
        dest.writeInt(this.mResColor);
        dest.writeInt(this.mResTextColor);
        dest.writeByte(this.mHasSelected ? (byte) 1 : (byte) 0);
        dest.writeInt(this.mStateMenu);
        dest.writeInt(this.drawerMenuType);
    }

    protected DrawerMenuViewEntry(Parcel in) {
        this.mTabTag = in.readString();
        this.mTitle = in.readString();
        this.mResDrawable = in.readInt();
        this.mResColor = in.readInt();
        this.mResTextColor = in.readInt();
        this.mHasSelected = in.readByte() != 0;
        this.mStateMenu = in.readInt();
        this.drawerMenuType = in.readInt();
    }

    public static final Creator<DrawerMenuViewEntry> CREATOR = new Creator<DrawerMenuViewEntry>() {
        @Override
        public DrawerMenuViewEntry createFromParcel(Parcel source) {
            return new DrawerMenuViewEntry(source);
        }

        @Override
        public DrawerMenuViewEntry[] newArray(int size) {
            return new DrawerMenuViewEntry[size];
        }
    };

    @Override
    public String toString() {
        return "DrawerMenuViewEntry{" +
                "mTabTag='" + mTabTag + '\'' +
                ", mTitle='" + mTitle + '\'' +
                ", mResDrawable=" + mResDrawable +
                ", mResColor=" + mResColor +
                ", mResTextColor=" + mResTextColor +
                ", mHasSelected=" + mHasSelected +
                ", mStateMenu=" + mStateMenu +
                ", drawerMenuType=" + drawerMenuType +
                '}';
    }
}
