package com.inka.netsync.data.network;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public final class ApiGenerateParams {

    // serial auth
    public static final String KEY_PARAMS_DEVICE_ID = "device_id";
    public static final String KEY_PARAMS_DEVICE_MODEL = "device_model";
    public static final String KEY_PARAMS_CID = "cid";
    public static final String KEY_PARAMS_APP_VERSION = "app_version";
    public static final String KEY_PARAMS_SERIAL = "serial";

//    private static final String CID = "cid";
//    private static final String APP_VERSION = "app_version";
//    private static final String DEVICE_ID = "device_id";
//    private static final String DEVICE_MODEL = "device_model";
//    private static final String SERIAL = "serial";

//    public static String generateSerialAuthenticationMode(SerialAuthEntry requestSerialAuthEntry) throws Exception {
//        HashMap<String, String> map = new HashMap<String, String>();
//        map.put(SERIAL, requestSerialAuthEntry.getSerialNumber());
//        map.put(CID, requestSerialAuthEntry.getRequestContentId());
//        map.put(DEVICE_ID, requestSerialAuthEntry.getDeviceID());
//        map.put(DEVICE_MODEL, requestSerialAuthEntry.getDeviceModel());
//        map.put(APP_VERSION, requestSerialAuthEntry.getAppVersion());
//        String result = generateParamMapToString(map);
//        return result;
//    }

    public static String generateParamMapToString(HashMap<String, String> map) throws JSONException {
        return new JSONObject(map).toString(2);
    }

    public static final String getUserId(String userId) {
        if (StringUtils.isBlank(userId)) {
            userId = "testId";
        }
        return userId;
    }

//    public static final String getAppVersion(String appVersion) {
//        if (StringUtils.isBlank(appVersion)) {
//            appVersion = "0.0.0";
//        }
//        return appVersion;
//    }
//
//
//    public static final String getGcmToken(String gcmToken) {
//        if (StringUtils.isBlank(gcmToken)) {
//            gcmToken = "testGcmToken";
//        }
//        return gcmToken;
//    }
//
//    public static final String getDeviceId(String deviceId) {
//        if (StringUtils.isBlank(deviceId)) {
//            deviceId = "testDeviceId";
//        }
//        return deviceId;
//    }
//
//    public static final String getAccessToken(String accessToken) {
//        if (StringUtils.isBlank(accessToken)) {
//            accessToken = "testAccessToken";
//        }
//        return accessToken;
//    }
//
//
//    public static final String getAccountType(String accountType) {
//        if (StringUtils.isBlank(accountType)) {
//            accountType = "testAccountType";
//        }
//        return accountType;
//    }


}
