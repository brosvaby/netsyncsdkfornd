package com.inka.netsync.data.network;

import android.content.Context;

import com.inka.netsync.R;
import com.inka.netsync.common.utils.NetworkUtils;
import com.inka.netsync.data.network.model.ResponseSerialAuthEntry;
import com.inka.netsync.data.network.model.SerialAuthEntry;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by birdgang on 2017. 5. 2..
 */

public class ApiServiceClientManager {

    private final String TAG = "ApiServiceClientManager";

    private Context context = null;

    private ApiServiceClient apiServiceClient = null;

    private String mServiceManagerUrl = "";

    public ApiServiceClientManager(Context context, SerialAuthEntry requestSerialAuthEntry) throws Exception {
        this.context = context;

        try {
            String key = requestSerialAuthEntry.getKey();
            String iv = requestSerialAuthEntry.getIv();
            String enterpriseCode = requestSerialAuthEntry.getEnterpriseCode();

            apiServiceClient = new ApiServiceClientOldSpec(context, key, iv, enterpriseCode);
//            GlobalConfiguration.getInstance().setServiceManagerServerVersion(AppConstants.APPLICATION_VERSION_OLDVERSION);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }


    // 시리얼 인증
    public ResponseSerialAuthEntry requestSerialAuthentication (SerialAuthEntry requestSerialAuthEntry) throws Exception {
        if (!NetworkUtils.isNetworkConnected(context)) {
            ResponseSerialAuthEntry responseSerialAuthEntry = new ResponseSerialAuthEntry();
            responseSerialAuthEntry.setResponse(ResponseCode.RESPONSE_CODE_SERIAL_NOT_CONNECTED_NETWORK);
            responseSerialAuthEntry.setMessage(context.getString(R.string.network_connect_fail));
            return responseSerialAuthEntry;
        }

        if (StringUtils.isBlank(mServiceManagerUrl)) {
            return apiServiceClient.requestSerialAuthentication(requestSerialAuthEntry.getRequestUrl(), requestSerialAuthEntry);
        }

        return apiServiceClient.requestSerialAuthentication(mServiceManagerUrl, requestSerialAuthEntry);
    }



}
