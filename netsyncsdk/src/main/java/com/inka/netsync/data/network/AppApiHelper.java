package com.inka.netsync.data.network;

import com.inka.ncg2.StringEncrypter;
import com.inka.netsync.data.network.request.MarketCheckRequest;
import com.inka.netsync.data.network.request.SerialAuthRequest;
import com.inka.netsync.data.network.response.SerialAuthResponse;
import com.inka.netsync.logs.LogUtil;
import com.rx2androidnetworking.Rx2ANRequest;
import com.rx2androidnetworking.Rx2AndroidNetworking;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;

@Singleton
public class AppApiHelper implements ApiHelper {

    private StringEncrypter mEncrypter;

    @Inject
    public AppApiHelper() {
    }

    @Override
    public Observable<SerialAuthResponse> getSerialAuthApiCall(SerialAuthRequest.ServerSerialAuthRequest request) throws Exception {
        String key = request.getRequestSerialAuth().key;
        String iv = request.getRequestSerialAuth().iv;
        String enterpriseCode = request.getRequestSerialAuth().enterpriseCode;
        String deviceId = request.getRequestSerialAuth().deviceId;
        String deviceModel = request.getRequestSerialAuth().deviceModel;
        String cId = request.getRequestSerialAuth().cid;
        String appVersion = request.getRequestSerialAuth().appVersion;
        String serial = request.getRequestSerialAuth().serial;

        LogUtil.INSTANCE.info("AppApiHelper" , "getSerialAuthApiCall > " +
                "deviceId : " + deviceId + " , deviceModel : " + deviceModel + " , cId : " + cId + " , appVersion : " + appVersion + " , serial : " + serial);

        if (StringUtils.isNotBlank(key) && StringUtils.isNotBlank(iv)) {
            mEncrypter = new StringEncrypter(key, iv);
        }

        HashMap<String, String> map = new HashMap<String, String>();
        map.put(ApiGenerateParams.KEY_PARAMS_DEVICE_ID, deviceId);
        map.put(ApiGenerateParams.KEY_PARAMS_DEVICE_MODEL, deviceModel);
        map.put(ApiGenerateParams.KEY_PARAMS_CID, cId);
        map.put(ApiGenerateParams.KEY_PARAMS_APP_VERSION, appVersion);
        map.put(ApiGenerateParams.KEY_PARAMS_SERIAL, serial);

        String jsonParams = ApiGenerateParams.generateParamMapToString(map);
        String encryptedParam = "data=" + mEncrypter.encrypt(jsonParams);

        String url = ApiEndPoint.ENDPOINT_SERIAL_AUTH + "?" + encryptedParam;
        LogUtil.INSTANCE.info("AppApiHelper" , "getSerialAuthApiCall > url : " + url);

        Rx2ANRequest.GetRequestBuilder build = Rx2AndroidNetworking.get(url);

        build.getResponseOnlyFromNetwork().build().getStringObservable();
        return build.getResponseOnlyFromNetwork().build().getObjectObservable(SerialAuthResponse.class);
    }


    @Override
    public Observable<String> getSerialAuthStringApiCall(SerialAuthRequest.ServerSerialAuthRequest request) throws Exception {
        String key = request.getRequestSerialAuth().key;
        String iv = request.getRequestSerialAuth().iv;
        String enterpriseCode = request.getRequestSerialAuth().enterpriseCode;
        String deviceId = request.getRequestSerialAuth().deviceId;
        String deviceModel = request.getRequestSerialAuth().deviceModel;
        String cId = request.getRequestSerialAuth().cid;
        String appVersion = request.getRequestSerialAuth().appVersion;
        String serial = request.getRequestSerialAuth().serial;

        LogUtil.INSTANCE.info("AppApiHelper" , "getSerialAuthApiCall > " +
                "deviceId : " + deviceId + " , deviceModel : " + deviceModel + " , cId : " + cId + " , appVersion : " + appVersion + " , serial : " + serial);

        if (StringUtils.isNotBlank(key) && StringUtils.isNotBlank(iv)) {
            mEncrypter = new StringEncrypter(key, iv);
        }

        HashMap<String, String> map = new HashMap<String, String>();
        map.put(ApiGenerateParams.KEY_PARAMS_DEVICE_ID, deviceId);
        map.put(ApiGenerateParams.KEY_PARAMS_DEVICE_MODEL, deviceModel);
        map.put(ApiGenerateParams.KEY_PARAMS_CID, cId);
        map.put(ApiGenerateParams.KEY_PARAMS_APP_VERSION, appVersion);
        map.put(ApiGenerateParams.KEY_PARAMS_SERIAL, serial);

        String jsonParams = ApiGenerateParams.generateParamMapToString(map);
        LogUtil.INSTANCE.info("AppApiHelper" , "getSerialAuthApiCall > jsonParams :" + jsonParams);

        String encryptedParam = "data=" + mEncrypter.encrypt(jsonParams);

        LogUtil.INSTANCE.info("AppApiHelper" , "getSerialAuthApiCall > encryptedParam :" + encryptedParam);

        String url = ApiEndPoint.ENDPOINT_SERIAL_AUTH + "?" + encryptedParam;

        LogUtil.INSTANCE.info("AppApiHelper" , "getSerialAuthApiCall > url : " + url);

        Rx2ANRequest.GetRequestBuilder build = Rx2AndroidNetworking.get(url);
        return build.getResponseOnlyFromNetwork().build().getStringObservable();
    }

    @Override
    public Observable<String> getStringMarketVersionCheckApiCall(MarketCheckRequest.ServerMarketCheckRequest request) {
        String requestUrl = "https://play.google.com/store/apps/details?id=" + request.getRequestMarketCheck().packageName;
        LogUtil.INSTANCE.info("AppApiHelper" , "getStringMarketVersionCheckApiCall > requestUrl : " + requestUrl);

        Rx2ANRequest.GetRequestBuilder build = Rx2AndroidNetworking.get(requestUrl);
        return build.getResponseOnlyFromNetwork().build().getStringObservable();
    }


}