package com.inka.netsync.view.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by birdgang on 2018. 1. 25..
 */
public class GuideViewEntry implements BaseViewEntry, Parcelable {

    public enum GuidesType {
        INTRO("INTRO"),
        PLAYER("PLAYER");

        private String type;

        GuidesType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }
    }

    private String guideType;
    private String title;
    private String description;
    private String btnText;

    private int guideId;
    private int position;
    private int imageResource;
    private int layoutColor;
    private int textColor;
    private int textSize;

    public GuideViewEntry () {
    }

    public int getGuideId() {
        return guideId;
    }

    public void setGuideId(int guideId) {
        this.guideId = guideId;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getGuideType() {
        return guideType;
    }

    public void setGuideType(String guideType) {
        this.guideType = guideType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBtnText() {
        return btnText;
    }

    public void setBtnText(String btnText) {
        this.btnText = btnText;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public int getLayoutColor() {
        return layoutColor;
    }

    public void setLayoutColor(int layoutColor) {
        this.layoutColor = layoutColor;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.guideType);
        dest.writeString(this.title);
        dest.writeString(this.description);
        dest.writeString(this.btnText);
        dest.writeInt(this.guideId);
        dest.writeInt(this.position);
        dest.writeInt(this.imageResource);
        dest.writeInt(this.layoutColor);
        dest.writeInt(this.textColor);
        dest.writeInt(this.textSize);
    }

    protected GuideViewEntry(Parcel in) {
        this.guideType = in.readString();
        this.title = in.readString();
        this.description = in.readString();
        this.btnText = in.readString();
        this.guideId = in.readInt();
        this.position = in.readInt();
        this.imageResource = in.readInt();
        this.layoutColor = in.readInt();
        this.textColor = in.readInt();
        this.textSize = in.readInt();
    }

    public static final Creator<GuideViewEntry> CREATOR = new Creator<GuideViewEntry>() {
        @Override
        public GuideViewEntry createFromParcel(Parcel source) {
            return new GuideViewEntry(source);
        }

        @Override
        public GuideViewEntry[] newArray(int size) {
            return new GuideViewEntry[size];
        }
    };

    @Override
    public String toString() {
        return "GuideViewEntry{" +
                "guideType='" + guideType + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", btnText='" + btnText + '\'' +
                ", guideId=" + guideId +
                ", position=" + position +
                ", imageResource=" + imageResource +
                ", layoutColor=" + layoutColor +
                ", textColor=" + textColor +
                ", textSize=" + textSize +
                '}';
    }

}
