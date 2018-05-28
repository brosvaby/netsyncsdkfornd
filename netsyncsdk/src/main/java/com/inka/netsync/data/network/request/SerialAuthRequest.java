package com.inka.netsync.data.network.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.inka.ncg2.StringEncrypter;

/**
 * Created by birdgang on 2018. 4. 12..
 */
public class SerialAuthRequest {

    private SerialAuthRequest() {
    }

    public static class ServerSerialAuthRequest {

        @Expose
        @SerializedName("serialAuth")
        private RequestSerialAuth requestSerialAuth;

        public ServerSerialAuthRequest(RequestSerialAuth requestSerialAuth) {
            this.requestSerialAuth = requestSerialAuth;
        }

        public RequestSerialAuth getRequestSerialAuth() {
            return requestSerialAuth;
        }

        public void setRequestSerialAuth(RequestSerialAuth requestSerialAuth) {
            this.requestSerialAuth = requestSerialAuth;
        }

        @Override
        public String toString() {
            return "ServerSerialAuthRequest{" +
                    "requestSerialAuth=" + requestSerialAuth +
                    '}';
        }

    }


    public static class RequestSerialAuth {
        public String key;
        public String iv;
        public String enterpriseCode;
        public String deviceId;
        public String deviceModel;
        public String cid;
        public String appVersion;
        public String serial;
        public StringEncrypter encrypter;
    }

}
