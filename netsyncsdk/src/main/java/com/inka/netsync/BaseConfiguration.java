package com.inka.netsync;

import com.inka.netsync.common.AppConstants;

public class BaseConfiguration {

    private static BaseConfiguration mInstance;

    private String APPLICATION_STRUCTURE = AppConstants.APPLICATION_STRUCTURE_APPLAYER;

    private String mAppStyle = AppConstants.APPLICATION_STYLE_PREMIUM;
    private String mRequestType = AppConstants.APPLICATION_REQUESTTYPE_DEFAULTMODULE;
    private String mServiceManagerServerSpec = AppConstants.APPLICATION_VERSION_OLDVERSION;

    private String APPLICATION_CONTENT_CID = "W7WMZUZ236AA2B76@pallycon.sd";
    private String APPLICATION_ORDER_ID = "SDCardOrderINKA";
    private int APPLICATION_DIALOG_BTN_COLOR = -1;

    private boolean						mIsNetsyncDisk				= false;
    private boolean						mIsUseKoranParam			= true;
    private boolean						mIsKeyValueParam			= false;
    private boolean						mIsCrashReport				= false;
    private boolean						mIsOfflineLms				= true;
    private boolean						mIsPushAlarm				= false;
    private boolean						mIsRemoveTempLicense		= true;
    private boolean						mIsPreBuilt					= false;
    private boolean						mIsMetaType					= true;

    // 메이져 버젼을 만들때 사용되는 값 으로 표기 방법은 3.xx.xxxxx
    private static final String 		VERSION_MAJOR				= "3";
    // 마이너 버젼을 만들때 사용되는 값 으로 표기 방법은 x.00.xxxxx
    private static final String 		VERSION_MINOR				= "27";
    // 디테일 버젼을 만들때 사용되는 값 으로 표기 방법은 x.xx.xxx00
    private static final String 		VERSION_DETAIL_PROPRIETARY	= "00";
    private static final String 		VERSION_DETAIL_COMMON		= "00";
    private static final String 		VERSION_DETAIL_INTEGRATION	= "00";

    public static String APPLICATION_ID = "com.inka.netsync";

    // **
//    public static final int CAHCE_CATEGORY_SIZE = 300;
//    public static final int CAHCE_MEDIA_SIZE = 500;
//    public static final int CAHCE_SETTING_SIZE = 10;

    public static String MEDIA_EXTERNAL_SCAN_ROOT = "";

    public static BaseConfiguration getInstance() {
        if (mInstance == null) {
            mInstance = new BaseConfiguration();
        }

        return mInstance;
    }

    // 앱 구조 (앱 플레이어 , 하이브리드 플레이어)
    public String getApplicationStructure() {
        return APPLICATION_STRUCTURE;
    }

    public void setApplicationStructure(String structure) {
        this.APPLICATION_STRUCTURE = structure;
    }

    // 앱 스타일 (
    public String getApplicationStyle() {
        return mAppStyle;
    }

    public void setApplicationStyle(String style) {
        mAppStyle = style;
    }

    // 리퀘스트 타입 (자바 기본 모듈, 아파치 모듈)
    public String getRequestType() {
        return mRequestType;
    }

    public void setRequestype(String type) {
        mRequestType = type;
    }

    // 버젼 (기본형, 고급형)
    public String getServiceManagerServerVersion() {
        return mServiceManagerServerSpec;
    }

    public void setServiceManagerServerVersion(String spec) {
        mServiceManagerServerSpec = spec;
    }

    public String getApplicationContentId () {
        return APPLICATION_CONTENT_CID;
    }

    public void setApplicationContentId (String contentId) {
        APPLICATION_CONTENT_CID = contentId;
    }

    public int getAppDialogBtnColor () {
        return APPLICATION_DIALOG_BTN_COLOR;
    }

    public void setAppDialogBtnColor (int color) {
        APPLICATION_DIALOG_BTN_COLOR = color;
    }


    // ND
    public boolean isNetsyncDisk() {
        return mIsNetsyncDisk;
    }

    public void setNetsyncDisk(boolean isNetsyncDisk) {
        mIsNetsyncDisk = isNetsyncDisk;
    }

    // Use Korean Param
    public boolean isUseKoreanParam() {
        return mIsUseKoranParam;
    }

    public void setUseKoreanParam(boolean isUseKoreanParam) {
        mIsUseKoranParam = isUseKoreanParam;
    }

    // is KeyValue Param
    public boolean isKeyValueParam() {
        return mIsKeyValueParam;
    }

    public void setKeyValueParam(boolean isKeyValueParam) {
        mIsKeyValueParam = isKeyValueParam;
    }

    // use Push Alarm
    public boolean isPushAlarm() {
        return mIsPushAlarm;
    }

    public void setPushAlarm(boolean isPushAlarm) {
        mIsPushAlarm = isPushAlarm;
    }

    // is PreBuilt
    public boolean isPreBuilt() {
        return mIsPreBuilt;
    }

    public void setPreBuilt(boolean isPreBuilt) {
        mIsPreBuilt = isPreBuilt;
    }

    // is MetaType
    public boolean isMetaType() {
        return mIsMetaType;
    }

    public void setMetaType(boolean isMetaType) {
        mIsMetaType = isMetaType;
    }

    // is Crash Report
    public boolean isCrashReport() {
        return mIsCrashReport;
    }

    public void setCrashReport(boolean isCrashReport) {
        mIsCrashReport = isCrashReport;
    }

    // is Offline Lms
    public boolean isOfflineLms() {
        return mIsOfflineLms;
    }

    public void setOfflineLms(boolean isOfflineLms) {
        mIsOfflineLms = isOfflineLms;
    }

    // Remove Temporary License
    public boolean isRemoveTempLicense() {
        return mIsRemoveTempLicense;
    }

    public void setRemoveTempLicense(boolean isRemoveTempLicense) {
        mIsRemoveTempLicense = isRemoveTempLicense;
    }

    // Solution Version
    public String getSolutionVersion() {
        String version = getMajorVersion() + "." + getMinorVersion() + "." + getTypeVersion() + getDetailVersion();
        return version;
    }

    // Major Version
    private String getMajorVersion() {
        return VERSION_MAJOR;
    }

    // Minor Version
    private String getMinorVersion() {
        return VERSION_MINOR;
    }

    // Detail Version
    private String getDetailVersion() {
        return VERSION_DETAIL_PROPRIETARY;
    }

    // Detail Version
    private String getTypeVersion() {
        if (APPLICATION_STRUCTURE == AppConstants.APPLICATION_STRUCTURE_APPLAYER) {
            // NetsyncDisk
            if (isNetsyncDisk() == true) {
                return "102";
            }

            // 고급
            if (mAppStyle == AppConstants.APPLICATION_STYLE_PREMIUM) {
                return "100";
                // 기본
            } else if (mAppStyle == AppConstants.APPLICATION_STYLE_BASIC) {
                return "101";
            }
            // 하이브리드 플레이어
        } else if (APPLICATION_STRUCTURE == AppConstants.APPLICATION_STRUCTURE_HYBRIDPLAYER) {
            // NetsyncDisk
            if (isNetsyncDisk() == true) {
                return "112";
            }

            // 고급
            if (mAppStyle == AppConstants.APPLICATION_STYLE_PREMIUM) {
                return "110";
                // 기본
            } else if (mAppStyle == AppConstants.APPLICATION_STYLE_BASIC) {
                return "111";
            }
        }

        return "xxx";
    }


    public static void setMediaExternalScanRoot (String externalScanRoot) {
        MEDIA_EXTERNAL_SCAN_ROOT = externalScanRoot;
    }

    public static String getMediaExternalScanRoot() {
        return MEDIA_EXTERNAL_SCAN_ROOT;
    }

}
