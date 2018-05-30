package com.inka.netsync;

/**
 * 사이트당 설정 값을 표현하는 클래스.
 * 메타에 있을수도 있고 없을 수도 있어서 vender resource 에서 무조건 보유해야 하는 값들
 * TODO 설정값을 XML에서 읽어들이자.
 */
public class BaseConfigurationPerSite {

    private static BaseConfigurationPerSite mInstance;

    /**** 전용(old DB) 일 경우 **************
     * 단 전용 createDefaultenterprise 를 위해
     * APP 생성시 모두 이곳에 설정해놓아야 한다.
     ***************************************/

    /**** 범용 및 전용(new DB) 일 경우 ********
     * 밑의 생성시점에 각각 추가 되어야 한다.
     ****************************************/

    // Activity 생성시 (공통)
    private String 	mAcquisitionUrl = "";
    private String 	mHomeUrl = "";
    private String 	mSubHomeUrl = "";
    private String 	mLockScreenUrl = "";
    private String 	mSubLockScreenUrl = "";
    private String 	mBuyerCertificateUrl = "";
    private String 	mSerialAuthenticationUrl = "";

    // Activity 생성시 (전용)
    private String 	mLogInUrl = "";
    private String 	mLogInChk = "";
    private String 	mDeviceUnregist = "";
    private String 	mLmsUrl = "";
    private String 	mDownloadFinishUrl = "";
    private String 	mValidityUrl = "";
    private String 	mRefundUrl = "";
    private String 	mRefundHomePageUrl = "";
    private String  mPrivacyPolicy = "";
    private String 	mFileCertificateUrl = "";
    private String 	mPushRegistrationUrl = "";
    private String mHelpUrl = "";

    // 업체인증 후 (공통)
    private String mEnterpriseCode = "";
    private String 	mEnterpriseName = "";
    private String 	mKey 					= "";
    private String 	mIv 					= "";

    // 업체업데이트 후 (공통)
    private String 	mPlayType				= "";
    private String 	mLogType				= "";
    private String 	mOffLineCount			= "0";
    private String	mSenderId				= "";
    private String 	mCardManufacturer		= "";
    private String 	mExternalSdPath			= "";
    private String 	mFontType				= "";

    private String mStrDirRootPath = "";

    // mKey, mIv 사용 가능 여부
    private boolean mEnable	= true;

    private BaseConfigurationPerSite() {
        setEnable();
        setEnterpriseCode(BaseApplication.getContext().getString(R.string.enterprise_code));
        setEnterpriseName(BaseApplication.getContext().getString(R.string.enterprise_name));
        setKey("9mB5L2GTRRScbrTH99L1JRnJKTRCeCkB");
        setIv("0123456789abcdef");
        setPlayType(BaseApplication.getContext().getString(R.string.play_type));
        setLogType(BaseApplication.getContext().getString(R.string.log_type));
        setStrDirRootPath("ND");
    }

    public static BaseConfigurationPerSite getInstance() {
        if (mInstance == null) {
            mInstance = new BaseConfigurationPerSite();
        }
        return mInstance;
    }

    // only 전용
    public void setKey(String key) {
        mKey = key;
    }

    // only 전용
    public String getKey() {
        if (mEnable == false) {
            return "";
        }

        return mKey;
    }

    // only 전용
    public void setIv(String iv) {
        mIv = iv;
    }

    // only 전용
    public String getIv() {
        if(mEnable == false) {
            return "";
        }

        return mIv;
    }


    // only 전용
    public void setEnterpriseCode(String siteId) {
        mEnterpriseCode = siteId;
    }

    // only 전용
    public String getEnterpriseCode() {
        return mEnterpriseCode;
    }

    // only 전용
    public void setEnterpriseName(String enterpriseName) {
        mEnterpriseName = enterpriseName;
    }

    // only 전용
    public String getEnterpriseName() {
        return mEnterpriseName;
    }

    public void setPlayType(String playType) {
        mPlayType = playType;
    }

    public String getPlayType() {
        return mPlayType;
    }

    public void setLogType(String LogType) {
        mLogType = LogType;
    }

    public String getLogType() {
        return mLogType;
    }

    public String getStrDirRootPath() {
        return mStrDirRootPath;
    }

    public void setStrDirRootPath(String strDirRootPath) {
        this.mStrDirRootPath = strDirRootPath;
    }

    public void setOffLineCount(String offLineCount) {
        mOffLineCount = offLineCount;
    }

    public String getOffLineCount() {
        return mOffLineCount;
    }

    public void setAcquisitionUrl(String acquisitionUrl) {
        mAcquisitionUrl = acquisitionUrl;
    }

    public String getAcquisitionUrl() {
        return mAcquisitionUrl;
    }

    public void setHomeUrl(String homeUrl) {
        mHomeUrl = homeUrl;
    }

    public String getHomeUrl() {
        return mHomeUrl;
    }

    public void setSubHomeUrl(String subHomeUrl) {
        mSubHomeUrl = subHomeUrl;
    }

    public String getSubHomeUrl() {
        return mSubHomeUrl;
    }

    public void setLockScreenUrl(String lockScreenUrl) {
        mLockScreenUrl = lockScreenUrl;
    }

    public String getLockScreenUrl() {
        return mLockScreenUrl;
    }

    public void setSubLockScreenUrl(String subLockScreenUrl) {
        mSubLockScreenUrl = subLockScreenUrl;
    }

    public String getSubLockScreenUrl() {
        return mSubLockScreenUrl;
    }

    public void setBuyerCertificateUrl(String buyerCertificateUrl) {
        mBuyerCertificateUrl = buyerCertificateUrl;
    }

    public String getBuyerCertificateUrl() {
        return mBuyerCertificateUrl;
    }

    public void setSerialAuthenticationUrl(String serialAuthenticationUrl) {
        mSerialAuthenticationUrl = serialAuthenticationUrl;
    }

    public String getSerialAuthenticationUrl() {
        return mSerialAuthenticationUrl;
    }

    public void setLogInChk(String logInChk) {
        mLogInChk = logInChk;
    }

    public String getLogInChk() {
        return mLogInChk;
    }

    public void setDeviceUnregist(String deviceUnregist) {
        mDeviceUnregist = deviceUnregist;
    }

    public String getDeviceUnregist() {
        return mDeviceUnregist;
    }

    public void setLmsUrl(String lmsUrl) {
        mLmsUrl = lmsUrl;
    }

    public String getLmsUrl() {
        return mLmsUrl;
    }

    public void setDownloadFinishUrl(String downloadFinishUrl) {
        mDownloadFinishUrl = downloadFinishUrl;
    }

    public String getDownloadFinishUrl() {
        return mDownloadFinishUrl;
    }

    public void setValidityUrl(String validityUrl) {
        mValidityUrl = validityUrl;
    }

    public String getValidityUrl() {
        return mValidityUrl;
    }

    public void setRefundUrl(String refundUrl) {
        mRefundUrl = refundUrl;
    }

    public String getRefundUrl() {
        return mRefundUrl;
    }

    public void setRefundHomePageUrl(String refundHomePageUrl) {
        mRefundHomePageUrl = refundHomePageUrl;
    }

    public String getRefundHomePageUrl() {
        return mRefundHomePageUrl;
    }

    public void setPrivacyPolicyUrl(String privacyPolicyUrl) {
        mPrivacyPolicy = privacyPolicyUrl;
    }

    public String getPrivacyPolicyUrl() {
        return mPrivacyPolicy;
    }

    public String getHelpUrl() {
        return mHelpUrl;
    }

    public void setHelpUrl(String helpUrl) {
        this.mHelpUrl = helpUrl;
    }

    public void setFileCertificateUrl(String fileCertificateUrl) {
        mFileCertificateUrl = fileCertificateUrl;
    }

    public String getFileCertificateUrl() {
        return mFileCertificateUrl;
    }

    public void setPushRegistrationUrl(String pushRegistrationUrl) {
        mPushRegistrationUrl = pushRegistrationUrl;
    }

    public String getPushRegistrationUrl() {
        return mPushRegistrationUrl;
    }

    public void setSenderId(String senderId) {
        mSenderId = senderId;
    }

    public String getSenderId() {
        return mSenderId;
    }

    public void setCardManufacturer(String moID) {
        mCardManufacturer = moID;
    }

    public String getCardManufacturer() {
        return mCardManufacturer;
    }

    public void setExternalSdPath(String path) {
        mExternalSdPath = path;
    }

    public String getExternalSdPath() {
        return mExternalSdPath;
    }

    public void setFontType(String fontType) {
        mFontType = fontType;
    }

    public String getFontType() {
        return mFontType;
    }

    public void setEnable() {
        mEnable = true;
    }

    public void setDisEnable() {
        mEnable = false;

        mEnterpriseCode 	= "";
        mEnterpriseName 	= "";
        mKey 				= "";
        mIv 				= "";
    }

}
