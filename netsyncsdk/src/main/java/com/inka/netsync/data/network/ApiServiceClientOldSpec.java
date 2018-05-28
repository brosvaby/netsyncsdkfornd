package com.inka.netsync.data.network;

import android.content.Context;

import com.inka.ncg2.StringEncrypter;
import com.inka.netsync.R;
import com.inka.netsync.data.network.converter.SerialAuthenticationConverter;
import com.inka.netsync.data.network.model.ResponseSerialAuthEntry;
import com.inka.netsync.data.network.model.SerialAuthEntry;
import com.inka.netsync.logs.LogUtil;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by birdgang on 2017. 5. 2..
 */
public class ApiServiceClientOldSpec implements ApiServiceClient {

    private final String TAG = ApiServiceClientOldSpec.class.toString();

    private Context context;
    private String key;
    private String iv;
    private	String enterpriseCode;
    private StringEncrypter mEncrypter;

    private ApiGenerateParams apiGenerateParams = new ApiGenerateParams();

    public ApiServiceClientOldSpec(Context context, String key, String iv, String enterpriseCode) throws Exception {
        this.context = context;
        this.key = key;
        this.iv = iv;
        this.enterpriseCode = enterpriseCode;

        LogUtil.INSTANCE.info("AppApiHelper" , "key : " + key + " , iv : " + iv + " , enterpriseCode : " + enterpriseCode);

        if (StringUtils.isNotBlank(key) && StringUtils.isNotBlank(iv)) {
            mEncrypter = new StringEncrypter(this.key, this.iv);
        }
    }

    @Override
    public ResponseSerialAuthEntry requestSerialAuthentication(String url, SerialAuthEntry requestSerialAuthentication) throws Exception {
        if (StringUtils.isBlank(url)) {
            throw new Exception(context.getString(R.string.exception_network_fail_to_request_customurl));
        }

        try {
            LogUtil.INSTANCE.info("AppApiHelper", "requestSerialAuthentication : " + requestSerialAuthentication.toString());
            String encryptedParam = "data=" + mEncrypter.encrypt(ApiGenerateParams.generateSerialAuthenticationMode(requestSerialAuthentication));

            LogUtil.INSTANCE.info("AppApiHelper", "encryptedParam : " + encryptedParam);
            String responseData = ApiRequestConnection.getDefault().sendRequestByPostMethod(context, url, encryptedParam);
            LogUtil.INSTANCE.info("AppApiHelper", "responseData : " + responseData);

            responseData = StringUtils.trim(responseData);
            if (StringUtils.isBlank(responseData)) {
                return null;
            }

            String decryptedData = mEncrypter.decrypt(responseData);
            if (StringUtils.isBlank(decryptedData)) {
                return null;
            }

            SerialAuthenticationConverter serialAuthenticationConverter = new SerialAuthenticationConverter();
            ResponseSerialAuthEntry responseSerialAuthentication = serialAuthenticationConverter.converter(context, decryptedData);
            return responseSerialAuthentication;
        } catch (Exception e) {
            throw new Exception("구매자 인증 실패" + "\n" + e.toString(), e);
        }
    }
    
}
