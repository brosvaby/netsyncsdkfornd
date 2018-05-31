package com.inka.netsync.data.network.converter;

import android.content.Context;

import com.inka.netsync.R;
import com.inka.netsync.data.network.ResponseCode;
import com.inka.netsync.data.network.model.ResponseSerialAuthEntry;
import com.inka.netsync.logs.LogUtil;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by birdgang on 2017. 6. 14..
 */

public class SerialAuthenticationConverter {

    private Context context;

    private final String RESPONSE = "response";
    private final String MESSAGE = "message";
    private final String CID = "cid";
    private final String SERIAL = "serial";
    private final String LOG_TYPE = "log_type";

    public ResponseSerialAuthEntry converter(Context context, String resource) throws Exception {
        this.context = context;

        ResponseSerialAuthEntry responseSerialAuthentication = new ResponseSerialAuthEntry();
        try {
            JSONObject jsonObj = new JSONObject(String.valueOf(resource));
            LogUtil.INSTANCE.info("birdgangconverter", "JSONObject : " + jsonObj.toString());

            String response = convertString(jsonObj, RESPONSE);
            String message = convertString(jsonObj, MESSAGE);
            String cid = convertString(jsonObj, CID);
            String serial = convertString(jsonObj, SERIAL);
            String logType = convertString(jsonObj, LOG_TYPE);

            responseSerialAuthentication.setResponse(response);
            responseSerialAuthentication.setMessage(convertResponseErrorCode(response, message));
            responseSerialAuthentication.setCid(cid);
            responseSerialAuthentication.setSerialNumber(serial);
            responseSerialAuthentication.setLogType(logType);
        } catch (Exception e) {
            LogUtil.INSTANCE.error(getClass().getSimpleName(), e);
        }
        return responseSerialAuthentication;
    }


    public String convertResponseErrorCode (String response, String message) {
        if (StringUtils.equals(ResponseCode.RESPONSE_CODE_SERIAL_INVALID_NUMBER_OF_DIGIT, response)) {
            message = context.getResources().getString(R.string.network_serial_auth_code_invalid_number);
        }
        else if (StringUtils.equals(ResponseCode.RESPONSE_CODE_SERIAL_INVALID_CONTENT_ID, response)) {
            message = context.getResources().getString(R.string.network_serial_auth_code_invalid_content_Id);
        }
        else if (StringUtils.equals(ResponseCode.RESPONSE_CODE_SERIAL_EXCEEDED_DEVICE_COUNT_LIMIT, response)) {
            message = context.getResources().getString(R.string.network_serial_auth_code_exceeded_device_count_limit);
        }
        else if (StringUtils.equals(ResponseCode.RESPONSE_CODE_SERIAL_INVALID_PLAYBACK_PERIOD, response)) {
            message = context.getResources().getString(R.string.network_serial_auth_code_invalid_playback_period);
        }
        else if (StringUtils.equals(ResponseCode.RESPONSE_CODE_SERIAL_INVALID_DRM_TYPE, response)) {
            message = context.getResources().getString(R.string.network_serial_auth_code_invalid_drm_type);
        }
        else if (StringUtils.equals(ResponseCode.RESPONSE_CODE_SERIAL_INVALID_DATE_FORMAT, response)) {
            message = context.getResources().getString(R.string.network_serial_auth_code_invalid_date_format);
        }
        else if (StringUtils.equals(ResponseCode.RESPONSE_CODE_SERIAL_NOT_CONNECTED_NETWORK, response)) {
            message = context.getResources().getString(R.string.network_connect_fail);
        }
        else {
            message = response;
        }
        return message;
    }

    public JSONObject convertJsonObject(JSONObject jsonObj, String key) throws Exception {
        JSONObject jsonObject = new JSONObject();

        try {
            if (jsonObj.has(key)) {
                jsonObject = jsonObj.getJSONObject(key);
            }
        } catch(JSONException e) {
            LogUtil.INSTANCE.error(getClass().getSimpleName(), e);
        }

        return jsonObject;
    }

    public String convertString(JSONObject jsonObj, String key) throws Exception {
        try {
            if (StringUtils.isNotBlank(key)) {
                if (!jsonObj.isNull(key)) {
                    String result = jsonObj.getString(key);
                    return result;
                }
            }
        } catch (JSONException e) {
            LogUtil.INSTANCE.error(getClass().getSimpleName(), e);
        }

        return StringUtils.EMPTY;
    }

}
