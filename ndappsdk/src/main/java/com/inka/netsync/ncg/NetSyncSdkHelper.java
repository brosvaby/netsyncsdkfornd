package com.inka.netsync.ncg;

import android.content.Context;
import android.widget.Toast;

import com.inka.ncg.nduniversal.Ncg2SdkHelper;
import com.inka.ncg.nduniversal.NcgValidationCheck;
import com.inka.ncg2.Ncg2Agent;
import com.inka.ncg2.Ncg2Exception;
import com.inka.ncg2.Ncg2FatalException;
import com.inka.ncg2.Ncg2HttpException;
import com.inka.ncg2.Ncg2ServerResponseErrorException;
import com.inka.netsync.R;
import com.inka.netsync.common.utils.DateTimeUtil;
import com.inka.netsync.logs.LogUtil;

/**
 * Created by birdgang on 2018. 5. 4..
 */
public class NetSyncSdkHelper {

    private final String TAG = "NetSyncSdkHelper";

    private static Context context;

    static volatile NetSyncSdkHelper defaultInstance;

    protected Ncg2SdkHelper mNcg2SdkHelper = null;

    public static NetSyncSdkHelper getDefault() {
        if (defaultInstance == null) {
            synchronized (NetSyncSdkHelper.class) {
                if (defaultInstance == null) {
                    defaultInstance = new NetSyncSdkHelper();
                }
            }
        }
        return defaultInstance;
    }

    public void initContext (Context context) {
        this.context = context;
    }


    private NetSyncSdkHelper() {
        mNcg2SdkHelper = Ncg2SdkHelper.getDefault();
    }

    /**
     *
     * @return
     */
    public boolean isInitialized () {
        return mNcg2SdkHelper.isInitialized();
    }


    /**
     *
     * @param context
     * @param deviceId
     * @param offlineCount
     * @throws Ncg2FatalException
     */
    public void initNcgSdk(Context context, String deviceId, String offlineCount) throws Ncg2FatalException {
        try {
            Ncg2Agent.OfflineSupportPolicy policy = Ncg2Agent.OfflineSupportPolicy.OfflineSupport;
            policy.setCountOfExecutionLimit(Integer.parseInt(offlineCount));
            mNcg2SdkHelper.init(context, policy, deviceId);
            mNcg2SdkHelper.enableLog();
        } catch (Ncg2FatalException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
            throw e;

        } catch (Ncg2Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }


    /**
     *
     * @throws Ncg2Exception
     */
    public void updateSecureTime() throws Ncg2Exception {
        mNcg2SdkHelper.updateSecureTime();
    }


    /**
     *
     * @param filePath
     * @return
     * @throws Ncg2Exception
     */
    public boolean isNcgContent (String filePath) throws Ncg2Exception {
        return mNcg2SdkHelper.isNcgContent(filePath);
    }


    /**
     *
     * @param contentFilePath
     * @return
     */
    public String getLicenseInfo(String contentFilePath) {
        String endDate 	= "";

        try {
            // 라이센스 체크
            Ncg2Agent.LicenseValidation lv = mNcg2SdkHelper.getLicenseValidation(contentFilePath);
            if (lv == Ncg2Agent.LicenseValidation.ValidLicense) {
                endDate = getLicenseDate(mNcg2SdkHelper.getPlayEndDate(contentFilePath));
                if (endDate.isEmpty()) {
                    endDate = context.getString(R.string.license_unlimited_content);
                }
            } else if (lv == Ncg2Agent.LicenseValidation.NotExistLicense) {
                endDate = context.getString(R.string.license_not_exist_license);
            } else if (lv == Ncg2Agent.LicenseValidation.ExpiredLicense) {
                endDate = context.getString(R.string.license_expired_license);
            } else if (lv == Ncg2Agent.LicenseValidation.BeforeStartDate) {
                endDate = context.getString(R.string.license_beforestartDate);
            } else if (lv == Ncg2Agent.LicenseValidation.ExceededPlayCount) {
                endDate = context.getString(R.string.license_exceeded_play_count);
            } else if (lv == Ncg2Agent.LicenseValidation.ExternalDeviceDisallowed) {
                endDate = context.getString(R.string.license_external_device_disallowed);
            } else if (lv == Ncg2Agent.LicenseValidation.RootedDeviceDisallowed) {
                endDate = context.getString(R.string.license_rooted_device_disallowed);
            } else if (lv == Ncg2Agent.LicenseValidation.DeviceTimeModified) {
                endDate = context.getString(R.string.license_device_time_modified);
            } else if (lv == Ncg2Agent.LicenseValidation.OfflineNotSupported) {
                endDate = context.getString(R.string.license_offline_not_supported);
            } else if (lv == Ncg2Agent.LicenseValidation.OfflineStatusTooLong) {
                endDate = context.getString(R.string.license_offline_status_too_long);
            } else if(lv == Ncg2Agent.LicenseValidation.ScreenRecorderDetected) {
                String packageName = lv.getExtraData().get("AppPackageName");
                endDate = context.getString(R.string.license_screen_recorder_detected, packageName);
            } else {
                endDate = context.getString(R.string.license_invalid_license);
            }
        } catch (Exception e) {
            endDate = "N/A";
        }
        return endDate;
    }


    /**
     *
     * @param filePath
     * @param licenseValid
     * @return
     */
    public String checkForPlaybackLicenseMessage (String filePath, int licenseValid) {
        String message = "";

        try {
            switch (licenseValid) {
                case NcgValidationCheck.ValidLicense :
                    break;

                case NcgValidationCheck.NotExistLicense :
                    message = context.getString(R.string.license_not_exist_license);
                    break;

                case NcgValidationCheck.ExpiredLicense :
                    message = context.getString(R.string.license_expired_license);
                    break;

                case NcgValidationCheck.BeforeStartDate :
                    message = context.getString(R.string.license_beforestartDate);
                    break;

                case NcgValidationCheck.ExceededPlayCount :
                    message = context.getString(R.string.license_exceeded_play_count);
                    break;

                case NcgValidationCheck.ExternalDeviceDisallowed :
                    message = context.getString(R.string.license_external_device_disallowed);
                    break;

                case NcgValidationCheck.RootedDeviceDisallowed :
                    message = context.getString(R.string.license_rooted_device_disallowed);
                    break;

                case NcgValidationCheck.DeviceTimeModified :
                    message = context.getString(R.string.license_fail_to_update_online);
                    break;

                case NcgValidationCheck.OfflineNotSupported :
                    message = context.getString(R.string.license_offline_not_supported);
                    break;

                case NcgValidationCheck.OfflineStatusTooLong :
                    message = context.getString(R.string.license_fail_to_update_online);
                    break;

                case NcgValidationCheck.ScreenRecorderDetected :
                    String packageName = mNcg2SdkHelper.getLicenseExtraDataPackageName(filePath);
                    message = context.getString(R.string.license_screen_recorder_detected, packageName);
                    break;

                default:
                    message = context.getString(R.string.license_invalid_license);
                    break;
            }
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
        return message;
    }

    /**
     *
     * @param playEndDate
     * @return
     */
    private String getLicenseDate(String playEndDate) {
        return DateTimeUtil.gmtToLocalTime(playEndDate, DateTimeUtil.NCG_TIME_FORMAT, DateTimeUtil.SIMPLE_DATE_FORMAT);
    }


    /**
     *
     * @param contentFilePath
     * @return
     */
    public boolean isValidLicense(String contentFilePath) {
        try {
            boolean isNcgContent = isNcgContent(contentFilePath);
            if (isNcgContent) {
                // 라이센스 체크
                int checkLicenseValid = checkLicenseValid(contentFilePath);
                if (checkLicenseValid != NcgValidationCheck.ValidLicense) {
                    Toast.makeText(context, getLicenseInfo(contentFilePath), Toast.LENGTH_LONG).show();
                    return false;
                }
            }
        } catch (Ncg2HttpException e) {
            Toast.makeText(context, context.getString(R.string.license_check_exception_network_error), Toast.LENGTH_LONG).show();
            return false;

        } catch (Ncg2Exception e) {
            Toast.makeText(context, context.getString(R.string.license_check_exception_ncg_error), Toast.LENGTH_LONG).show();
            return false;

        } catch (Exception e) {
            Toast.makeText(context, context.getString(R.string.license_check_exception_other_error), Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }


    /**
     *
     * @param filePath
     * @return
     * @throws Ncg2Exception
     */
    public int checkLicenseValid (String filePath) throws Ncg2Exception {
        return mNcg2SdkHelper.checkLicenseValid(filePath);
    }


    /**
     *
     * @param contentId
     * @return
     * @throws Ncg2Exception
     */
    public int checkLicenseValidByCID (String contentId) throws Ncg2Exception {
        return mNcg2SdkHelper.checkLicenseValidByCID(contentId);
    }


    /**
     *
     * @param filePath
     * @return
     * @throws Ncg2Exception
     */
    public String getScreenRecorderDetectedPackageName (String filePath) throws Ncg2Exception {
        Ncg2Agent.LicenseValidation licenseValidation = mNcg2SdkHelper.getLicenseValidation(filePath);
        return licenseValidation.getExtraData().get("AppPackageName");
    }


    /**
     *
     * @param filePath
     * @param key
     * @return
     * @throws Ncg2Exception
     */
    public String getLicenseValidationExtraData (String filePath, String key) throws Ncg2Exception {
        Ncg2Agent.LicenseValidation licenseValidation = mNcg2SdkHelper.getLicenseValidation(filePath);
        return licenseValidation.getExtraData().get(key);
    }

    /**
     *
     * @param filePath
     * @return
     * @throws Ncg2Exception
     */
    public String getContentId (String filePath) throws Ncg2Exception {
        return mNcg2SdkHelper.getContentIdInHeaderInformation(filePath);
    }


    /***
     *  범용.
     * @param var1
     * @param var2
     * @param var3
     * @throws Ncg2ServerResponseErrorException
     * @throws Ncg2HttpException
     * @throws Ncg2Exception
     */
    public void acquireLicenseByPath(String var1, String var2, String var3) throws Ncg2Exception {
        mNcg2SdkHelper.acquireLicenseByPath(var1, var2, var3);
    }


    /***
     *
     * @param var1
     * @param var2
     * @param var3
     * @param var4
     * @throws Ncg2Exception
     */
    public void acquireLicenseByPath(String var1, String var2, String var3, boolean var4) throws Ncg2Exception {
        mNcg2SdkHelper.acquireLicenseByPath(var1, var2, var3, var4);
    }


    /**
     *
     */
    public void removeLicenseAllCID() {
        mNcg2SdkHelper.removeLicenseAllCID();
    }


    /**
     *
     * @param path
     * @throws Ncg2Exception
     */
    public void removeLicenseByPath (String path) throws Ncg2Exception {
        mNcg2SdkHelper.removeLicenseByPath(path);
    }


    /**
     *
     * @param acquisitionUrl
     * @param base64Str
     * @return
     * @throws Ncg2Exception
     * @throws Ncg2HttpException
     */
    public String sendCustomChannelRequest(String acquisitionUrl, String base64Str) throws Ncg2Exception, Ncg2HttpException {
        return mNcg2SdkHelper.sendCustomChannelRequest(acquisitionUrl, base64Str);
    }


    /**
     *
     * @return
     * @throws Ncg2Exception
     */
    public String readPallyconInternalInfoTypeA() throws Ncg2Exception {
        return mNcg2SdkHelper.readPallyconInternalInfoTypeA();
    }


    /**
     *
     * @return
     * @throws Ncg2Exception
     */
    public String readPallyconInfoTypeA() throws Ncg2Exception {
        return mNcg2SdkHelper.readPallyconInfoTypeA();
    }


    /**
     *
     * @param path
     * @return
     * @throws Ncg2Exception
     */
    public boolean isPallyconFile(String path) throws Ncg2Exception {
        return mNcg2SdkHelper.isPallyconFile(path);
    }


    /**
     * 전용..
     * @param var1
     * @param var2
     * @throws Ncg2Exception
     */
    public void addLicense(String var1, String var2, String var3, String var4, int var5, int var6, int var7, boolean externalDisplay) throws Ncg2Exception {
        mNcg2SdkHelper.addLicense(var1, var2, var3, var4, var5, var6, var7, externalDisplay);
    }


    /**
     *
     */
    public void release () {
        mNcg2SdkHelper.release();
    }

}
