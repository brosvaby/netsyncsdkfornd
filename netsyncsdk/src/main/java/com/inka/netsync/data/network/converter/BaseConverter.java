package com.inka.netsync.data.network.converter;

import com.inka.netsync.data.network.model.ResponseEntry;
import com.inka.netsync.logs.LogUtil;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by birdgang on 2017. 4. 26..
 */
public abstract class BaseConverter {

    protected final String TAG = "BaseConverter";

    protected String RESPONSE = "response";
    protected String MESSAGE = "message";

    public abstract ResponseEntry converter(ResponseEntry responseData) throws Exception;
    public abstract ResponseEntry converter(String responseData) throws Exception;
    public abstract ResponseEntry converter(int type, String responseData) throws Exception;

    public ResponseEntry converterResult(ResponseEntry responseBase) throws Exception {
        return responseBase;
    }

    public String convertString(JSONObject jsonObj, String key) throws Exception {
        try {
            if (StringUtils.isNotBlank(key)) {
                if (!jsonObj.isNull(key)) {
                    return jsonObj.getString(key);
                }
            }

        } catch (JSONException e) {
            LogUtil.INSTANCE.error(getClass().getSimpleName(), e);
        }

        return StringUtils.EMPTY;
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

    public JSONArray convertJsonArray(JSONObject jsonObj, String key) {
        JSONArray array = null;
        try {
            if (jsonObj.has(key)) {
                array = jsonObj.getJSONArray(key);
            }
        } catch(JSONException e) {
            LogUtil.INSTANCE.error(getClass().getSimpleName(), e);
        }

        return array;
    }


    protected JSONArray getJsonArray(JSONObject json, String name) {
        JSONArray arr = null;
        try {
            arr = json.getJSONArray(name);
        } catch (JSONException e) {
            return arr;
        }

        return arr;
    }

}
