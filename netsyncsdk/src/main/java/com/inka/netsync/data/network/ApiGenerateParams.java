package com.inka.netsync.data.network;

import com.inka.netsync.data.network.model.SerialAuthEntry;
import com.inka.netsync.logs.LogUtil;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public final class ApiGenerateParams {

    // serial auth
    public static final String KEY_PARAMS_DATA = "data";
    public static final String KEY_PARAMS_DEVICE_ID = "device_id";
    public static final String KEY_PARAMS_DEVICE_MODEL = "device_model";
    public static final String KEY_PARAMS_CID = "cid";
    public static final String KEY_PARAMS_APP_VERSION = "app_version";
    public static final String KEY_PARAMS_SERIAL = "serial";

    private static final String MODE = "mode";
    private static final String USER_ID = "user_id";
    private static final String SITE_ID = "site_id";
    private static final String INPUT = "input";
    private static final String CID = "cid";
    private static final String APP_VERSION = "app_version";
    private static final String DEVICE_ID = "device_id";
    private static final String DEVICE_MODEL = "device_model";
    private static final String INVALIDE_TELEPHONEY_ID = "invalid_telephony_id";
    private static final String LICENSE_URL = "license_url";
    private static final String SERIAL = "serial";
    private static final String PASSWORD = "password";
    private static final String PACKAGE_NAME = "package_name";
    private static final String DEVICE_TYPE = "device_type";
    private static final String DEVICE_TOKEN = "device_token";

    private static final String CATEGORY_NAME = "category_name";
    private static final String CONTENT_NAME = "content_name";
    private static final String FILE_NAME = "file_name";

    private static final String DATE = "date";
    private static final String DOWNLOAD_STATUS = "download_status";
    private static final String INFO_ORDER_ID = "info_orderID";
    private static final String INFO_ONE = "info_one";
    private static final String INFO_TWO = "info_two";
    private static final String INFO_THREE = "info_three";
    private static final String INFO_FOUR = "info_four";
    private static final String LAST_PLAY_TIME = "last_play_time";
    private static final String LMS_PERCENT = "lms_percent";
    private static final String LMS_SECTIONS = "lms_sections";
    private static final String LMS_RAW_SECTIONS = "lms_raw_sections";
    private static final String REFUND_INFO = "refund_info";
    private static final String DELETED_CATEGORIES_COUNT = "deleted_categories_count";


    public static String generateSerialAuthenticationMode(SerialAuthEntry requestSerialAuthEntry) throws Exception {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(SERIAL, requestSerialAuthEntry.getSerialNumber());
        map.put(CID, requestSerialAuthEntry.getRequestContentId());
        map.put(DEVICE_ID, requestSerialAuthEntry.getDeviceID());
        map.put(DEVICE_MODEL, requestSerialAuthEntry.getDeviceModel());
        map.put(APP_VERSION, requestSerialAuthEntry.getAppVersion());
        String result = generateParamMapToString(map);
        LogUtil.INSTANCE.info("AppApiHelper", "map : " + map.toString());
        return result;
    }

    public static String generateParamMapToString(HashMap<String, String> map) throws JSONException {
        return new JSONObject(map).toString(2);
    }

    public static final String getUserId(String userId) {
        if (StringUtils.isBlank(userId)) {
            userId = "testId";
        }
        return userId;
    }

    public static final String getAppVersion(String appVersion) {
        if (StringUtils.isBlank(appVersion)) {
            appVersion = "0.0.0";
        }
        return appVersion;
    }


    public static final String getGcmToken(String gcmToken) {
        if (StringUtils.isBlank(gcmToken)) {
            gcmToken = "testGcmToken";
        }
        return gcmToken;
    }

    public static final String getDeviceId(String deviceId) {
        if (StringUtils.isBlank(deviceId)) {
            deviceId = "testDeviceId";
        }
        return deviceId;
    }

    public static final String getAccessToken(String accessToken) {
        if (StringUtils.isBlank(accessToken)) {
            accessToken = "testAccessToken";
        }
        return accessToken;
    }


    public static final String getAccountType(String accountType) {
        if (StringUtils.isBlank(accountType)) {
            accountType = "testAccountType";
        }
        return accountType;
    }


}
