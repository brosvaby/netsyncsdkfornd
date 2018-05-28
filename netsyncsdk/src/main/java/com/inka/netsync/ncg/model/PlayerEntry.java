package com.inka.netsync.ncg.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.inka.netsync.data.network.model.BaseResponseEntry;

/**
 * Created by birdgang on 2018. 5. 4..
 */
public class PlayerEntry implements BaseResponseEntry, Parcelable {

    private boolean isSuccess = false;

    private int contentId = -1;
    private String filePath;
    private String fileSize;

    private String swXPlay;
    private String playerType;
    private String playType;

    private String userId;
    private String infoOrderId;

    private boolean shownPlayerGuide;

    private String errorMessage;

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public int getContentId() {
        return contentId;
    }

    public void setContentId(int contentId) {
        this.contentId = contentId;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getSwXPlay() {
        return swXPlay;
    }

    public void setSwXPlay(String swXPlay) {
        this.swXPlay = swXPlay;
    }

    public String getPlayerType() {
        return playerType;
    }

    public void setPlayerType(String playerType) {
        this.playerType = playerType;
    }

    public String getPlayType() {
        return playType;
    }

    public void setPlayType(String playType) {
        this.playType = playType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getInfoOrderId() {
        return infoOrderId;
    }

    public void setInfoOrderId(String infoOrderId) {
        this.infoOrderId = infoOrderId;
    }

    public boolean isShownPlayerGuide() {
        return shownPlayerGuide;
    }

    public void setShownPlayerGuide(boolean shownPlayerGuide) {
        this.shownPlayerGuide = shownPlayerGuide;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }


    public PlayerEntry() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.isSuccess ? (byte) 1 : (byte) 0);
        dest.writeInt(this.contentId);
        dest.writeString(this.filePath);
        dest.writeString(this.fileSize);
        dest.writeString(this.swXPlay);
        dest.writeString(this.playerType);
        dest.writeString(this.playType);
        dest.writeString(this.userId);
        dest.writeString(this.infoOrderId);
        dest.writeByte(this.shownPlayerGuide ? (byte) 1 : (byte) 0);
        dest.writeString(this.errorMessage);
    }

    protected PlayerEntry(Parcel in) {
        this.isSuccess = in.readByte() != 0;
        this.contentId = in.readInt();
        this.filePath = in.readString();
        this.fileSize = in.readString();
        this.swXPlay = in.readString();
        this.playerType = in.readString();
        this.playType = in.readString();
        this.userId = in.readString();
        this.infoOrderId = in.readString();
        this.shownPlayerGuide = in.readByte() != 0;
        this.errorMessage = in.readString();
    }

    public static final Creator<PlayerEntry> CREATOR = new Creator<PlayerEntry>() {
        @Override
        public PlayerEntry createFromParcel(Parcel source) {
            return new PlayerEntry(source);
        }

        @Override
        public PlayerEntry[] newArray(int size) {
            return new PlayerEntry[size];
        }
    };

    @Override
    public String toString() {
        return "PlayerEntry{" +
                "isSuccess=" + isSuccess +
                ", contentId=" + contentId +
                ", filePath='" + filePath + '\'' +
                ", fileSize='" + fileSize + '\'' +
                ", swXPlay='" + swXPlay + '\'' +
                ", playerType='" + playerType + '\'' +
                ", playType='" + playType + '\'' +
                ", userId='" + userId + '\'' +
                ", infoOrderId='" + infoOrderId + '\'' +
                ", shownPlayerGuide=" + shownPlayerGuide +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }

}
