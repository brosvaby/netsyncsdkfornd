package com.inka.netsync.ncg;

import android.content.Context;
import android.widget.Toast;

import com.inka.ncg.nduniversal.NcgValidationCheck;
import com.inka.ncg2.Base64Encoder;
import com.inka.ncg2.Ncg2Agent;
import com.inka.ncg2.Ncg2Exception;
import com.inka.ncg2.Ncg2FatalException;
import com.inka.ncg2.Ncg2HttpException;
import com.inka.ncg2.Ncg2LocalWebServer;
import com.inka.ncg2.Ncg2ModifiedDBFileInoException;
import com.inka.ncg2.Ncg2ReadPhoneStateException;
import com.inka.ncg2.Ncg2SdkFactory;
import com.inka.ncg2.Ncg2ServerResponseErrorException;
import com.inka.netsync.R;
import com.inka.netsync.common.utils.DateTimeUtil;
import com.inka.netsync.logs.LogUtil;

/**
 * Created by birdgang on 2018. 5. 4..
 */
public class Ncg2SdkHelper {

    private final String TAG = "Ncg2SdkHelper";

    private static Context context;

    static volatile Ncg2SdkHelper defaultInstance;

    protected Ncg2Agent mNcg2Agent = null;

    public static Ncg2SdkHelper getDefault() {
        if (defaultInstance == null) {
            synchronized (Ncg2SdkHelper.class) {
                if (defaultInstance == null) {
                    defaultInstance = new Ncg2SdkHelper();
                }
            }
        }
        return defaultInstance;
    }

    public void initContext (Context context) {
        this.context = context;
    }


    private Ncg2SdkHelper() {
        mNcg2Agent = Ncg2SdkFactory.getNcgAgentInstance();
    }


    public boolean isInitialized () {
        return mNcg2Agent.isInitialized();
    }

    public void initNcgSdk(Context context, String deviceId, String offlineCount) throws Ncg2FatalException {
        try {
            Ncg2Agent.OfflineSupportPolicy policy = Ncg2Agent.OfflineSupportPolicy.OfflineSupport;
            policy.setCountOfExecutionLimit(Integer.parseInt(offlineCount));
            init(context, policy, deviceId);

            enableLog();
        } catch (Ncg2FatalException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
            throw e;

        } catch (Ncg2Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }


    public void init(Context context, Ncg2Agent.OfflineSupportPolicy policy, String deviceId) throws Ncg2ReadPhoneStateException, Ncg2ModifiedDBFileInoException, Ncg2Exception {
        mNcg2Agent.init(context, policy, deviceId);
    }

    public void updateSecureTime() throws Ncg2Exception {
        mNcg2Agent.updateSecureTime();
    }

    public boolean isNcgContent (String filePath) throws Ncg2Exception {
        return mNcg2Agent.isNcgContent(filePath);
    }

    private Ncg2Agent.LicenseValidation getLicenseValidation (String filePath) throws Ncg2Exception {
        return mNcg2Agent.checkLicenseValid(filePath);
    }

    private Ncg2Agent.LicenseValidation getLicenseValidationByCId (String contentId) throws Ncg2Exception {
        return mNcg2Agent.checkLicenseValidByCID(contentId);
    }

    private int checkLicenseValid (Ncg2Agent.LicenseValidation licenseValidation) {
        int result = -1;
        if (licenseValidation == Ncg2Agent.LicenseValidation.ValidLicense) {
            result = NcgValidationCheck.ValidLicense;
        } else if (licenseValidation == Ncg2Agent.LicenseValidation.NotExistLicense) {
            result = NcgValidationCheck.NotExistLicense;
        } else if (licenseValidation == Ncg2Agent.LicenseValidation.ExpiredLicense) {
            result = NcgValidationCheck.ExpiredLicense;
        } else if (licenseValidation == Ncg2Agent.LicenseValidation.BeforeStartDate) {
            result = NcgValidationCheck.BeforeStartDate;
        } else if (licenseValidation == Ncg2Agent.LicenseValidation.ExceededPlayCount) {
            result = NcgValidationCheck.ExceededPlayCount;
        } else if (licenseValidation == Ncg2Agent.LicenseValidation.ExternalDeviceDisallowed) {
            result = NcgValidationCheck.ExternalDeviceDisallowed;
        } else if (licenseValidation == Ncg2Agent.LicenseValidation.RootedDeviceDisallowed) {
            result = NcgValidationCheck.RootedDeviceDisallowed;
        } else if (licenseValidation == Ncg2Agent.LicenseValidation.DeviceTimeModified) {
            result = NcgValidationCheck.DeviceTimeModified;
        } else if (licenseValidation == Ncg2Agent.LicenseValidation.OfflineNotSupported) {
            result = NcgValidationCheck.OfflineNotSupported;
        } else if (licenseValidation == Ncg2Agent.LicenseValidation.OfflineStatusTooLong) {
            result = NcgValidationCheck.OfflineStatusTooLong;
        } else if (licenseValidation == Ncg2Agent.LicenseValidation.NotAuthorizedAppID) {
            result = NcgValidationCheck.NotAuthorizedAppID;
        } else if (licenseValidation == Ncg2Agent.LicenseValidation.ScreenRecorderDetected) {
            result = NcgValidationCheck.ScreenRecorderDetected;
        }

        return result;
    }

    public String getLicenseInfo(String contentFilePath) {
        String endDate 	= "";

        try {
            // 라이센스 체크
            Ncg2Agent.LicenseValidation lv = getLicenseValidation(contentFilePath);
            if (lv == Ncg2Agent.LicenseValidation.ValidLicense) {
                endDate = getLicenseDate(getPlayEndDate(contentFilePath));
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
                    String packageName = getLicenseExtraDataPackageName(filePath);
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


    private String getLicenseDate(String playEndDate) {
        return DateTimeUtil.gmtToLocalTime(playEndDate, DateTimeUtil.NCG_TIME_FORMAT, DateTimeUtil.SIMPLE_DATE_FORMAT);
    }


    /**
     *
     * @param contentFilePath
     * @return
     */
    public boolean isValidLicense(String contentFilePath) {
        // NCG 파일 체크
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
            return true;

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
    }

    public int checkLicenseValid (String filePath) throws Ncg2Exception {
        Ncg2Agent.LicenseValidation licenseValidation = getLicenseValidation(filePath);
        return checkLicenseValid(licenseValidation);
    }

    public String getLicenseExtraDataPackageName (String filePath) throws Ncg2Exception {
        Ncg2Agent.LicenseValidation licenseValidation = getLicenseValidation(filePath);
        String packageName = licenseValidation.getExtraData().get("AppPackageName");
        return packageName;
    }

    public int checkLicenseValidByCID (String contentId) throws Ncg2Exception {
        Ncg2Agent.LicenseValidation licenseValidation = getLicenseValidationByCId(contentId);
        return checkLicenseValid(licenseValidation);
    }


    public String getScreenRecorderDetectedPackageName (String filePath) throws Ncg2Exception {
        Ncg2Agent.LicenseValidation licenseValidation = getLicenseValidation(filePath);
        return licenseValidation.getExtraData().get("AppPackageName");
    }

    public String getLicenseValidationExtraData (String filePath, String key) throws Ncg2Exception {
        Ncg2Agent.LicenseValidation licenseValidation = getLicenseValidation(filePath);
        return licenseValidation.getExtraData().get(key);
    }

    public String getContentIdInHeaderInformation (String filePath) throws Ncg2Exception {
        Ncg2Agent.HeaderInformation information = mNcg2Agent.getHeaderInfo(filePath);
        return information.contentID;
    }

    public String getPlayEndDate (String contentFilePath) {
        String result = null;
        try {
            Ncg2Agent ncgAgent = Ncg2SdkFactory.getNcgAgentInstance();
            Ncg2Agent.LicenseInformation licInfos = ncgAgent.getLicenseInfo(contentFilePath);
            result = licInfos.playEndDate;
        } catch (Ncg2Exception e) {
            e.printStackTrace();
        }
        return result;
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
        mNcg2Agent.acquireLicenseByPath(var1, var2, var3);
    }

    public void acquireLicenseByPath(String var1, String var2, String var3, boolean var4) throws Ncg2Exception {
        mNcg2Agent.acquireLicenseByPath(var1, var2, var3, var4);
    }

    public void removeLicenseAllCID() {
        mNcg2Agent.removeLicenseAllCID();
    }

    public void removeLicenseByPath (String path) throws Ncg2Exception {
        mNcg2Agent.removeLicenseByPath(path);
    }

    public String encodeBase64 (byte[] utf8Value) {
        return Base64Encoder.encode(utf8Value);
    }

    public byte[] decodeBase64 (String utf8Value) throws Exception {
        return Base64Encoder.decode(utf8Value);
    }

    public String sendCustomChannelRequest(String acquisitionUrl, String base64Str) throws Ncg2Exception, Ncg2HttpException {
        return mNcg2Agent.sendCustomChannelRequest(acquisitionUrl, base64Str);
    }

    public String readPallyconInternalInfoTypeA() throws Ncg2Exception {
        return mNcg2Agent.readPallyconInternalInfoTypeA();
    }

    public String readPallyconInfoTypeA() throws Ncg2Exception {
        return mNcg2Agent.readPallyconInfoTypeA();
    }

    public boolean isPallyconFile(String path) throws Ncg2Exception {
        return mNcg2Agent.isPallyconFile(path);
    }


    /**
     * 전용..
     * @param var1
     * @param var2
     * @throws Ncg2Exception
     */
    public void addLicense(String var1, String var2, String var3, String var4, int var5, int var6, int var7, boolean externalDisplay) throws Ncg2Exception {
        mNcg2Agent.addLicense(var1, var2, var3, var4, var5, var6, var7, externalDisplay);
    }

    public Ncg2LocalWebServer getLocalWebServer () {
        return mNcg2Agent.getLocalWebServer();
    }


    public void enableLog() {
        mNcg2Agent.enableLog();
    }

    public void release () {
        mNcg2Agent.release();
    }

}
