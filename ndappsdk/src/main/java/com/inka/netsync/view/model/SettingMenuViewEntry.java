package com.inka.netsync.view.model;


import android.os.Parcel;
import android.os.Parcelable;

public class SettingMenuViewEntry implements BaseViewEntry, Parcelable {

    public final static int ID_PLAYER = 0;              //플레이어 선택
    public final static int ID_SWXPLAY_ALLOW = 1;       // 재생 디코더
    public final static int ID_GESTURE_ALLOW = 2;       // 플레이어 제스쳐 사용유무
    public final static int ID_RESTRICTED_INTERNET = 3;       // 재생 방해 금지 모드
    public final static int ID_LOCKSCREEN_PLAYER = 4;       // 플레이어 락스크린
    public final static int ID_BASIC_INFO_APP_NAME = 5;        // 앱 이름
    public final static int ID_BASIC_INFO_APP_VERSION = 6;      // 앱 버전
    public final static int ID_BASIC_INFO_APP_COMPANY = 7;      // 제작사
    public final static int ID_BASIC_INFO_CONTACT = 8;         // 제휴문의
    public final static int ID_BASIC_FNC_PRIVACY = 9;          // 개인 정보 취급 방침
    public final static int ID_BASIC_FNC_EXTRACT_DATABASE = 10;     // 데이타 베이스 축출

    public enum SettingType {
        SWITCH("SWITCH"),
        WITHBUTTON("WITHBUTTON"),
        COMMON("COMMON"),
        SWITCHWITHDESCRIPTION("SWITCHWITHDESCRIPTION");

        private String type;

        SettingType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }
    }

    public String settingType;

    public String getSettingType() {
        return settingType;
    }

    public void setSettingType(String settingType) {
        this.settingType = settingType;
    }

    public int id = -1;
    public String title = "";
    public String value = "";
    public String description = "";
    public boolean toggleOnOrFalse = false;
    public boolean hasMore = false;
    public boolean enableMarketUpdate = false;


    public SettingMenuViewEntry(int id, String settingType, String title, String value, String description, boolean enable, boolean clickable) {
        this.id = id;
        this.settingType = settingType;
        this.title = title;
        this.value = value;
        this.description = description;
        this.toggleOnOrFalse = enable;
        this.hasMore = clickable;
    }


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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isToggleOnOrFalse() {
        return toggleOnOrFalse;
    }

    public void setToggleOnOrFalse(boolean toggleOnOrFalse) {
        this.toggleOnOrFalse = toggleOnOrFalse;
    }

    public boolean isHasMore() {
        return hasMore;
    }

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }

    public boolean isEnableMarketUpdate() {
        return enableMarketUpdate;
    }

    public void setEnableMarketUpdate(boolean enableMarketUpdate) {
        this.enableMarketUpdate = enableMarketUpdate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.settingType);
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.value);
        dest.writeString(this.description);
        dest.writeByte(this.toggleOnOrFalse ? (byte) 1 : (byte) 0);
        dest.writeByte(this.hasMore ? (byte) 1 : (byte) 0);
    }

    protected SettingMenuViewEntry(Parcel in) {
        this.settingType = in.readString();
        this.id = in.readInt();
        this.title = in.readString();
        this.value = in.readString();
        this.description = in.readString();
        this.toggleOnOrFalse = in.readByte() != 0;
        this.hasMore = in.readByte() != 0;
    }

    public static final Creator<SettingMenuViewEntry> CREATOR = new Creator<SettingMenuViewEntry>() {
        @Override
        public SettingMenuViewEntry createFromParcel(Parcel source) {
            return new SettingMenuViewEntry(source);
        }

        @Override
        public SettingMenuViewEntry[] newArray(int size) {
            return new SettingMenuViewEntry[size];
        }
    };

    @Override
    public String toString() {
        return "SettingMenuViewEntry{" +
                "settingType='" + settingType + '\'' +
                ", id=" + id +
                ", title='" + title + '\'' +
                ", value='" + value + '\'' +
                ", description='" + description + '\'' +
                ", toggleOnOrFalse=" + toggleOnOrFalse +
                ", hasMore=" + hasMore +
                '}';
    }

}
