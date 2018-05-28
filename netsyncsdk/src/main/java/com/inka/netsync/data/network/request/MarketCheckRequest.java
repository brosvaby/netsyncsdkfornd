package com.inka.netsync.data.network.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by birdgang on 2018. 4. 12..
 */
public class MarketCheckRequest {

    private MarketCheckRequest() {
    }

    public static class ServerMarketCheckRequest {

        @Expose
        @SerializedName("marketCheck")
        private RequestMarketCheck requestMarketCheck;

        public ServerMarketCheckRequest(RequestMarketCheck requestMarketCheck) {
            this.requestMarketCheck = requestMarketCheck;
        }

        public RequestMarketCheck getRequestMarketCheck() {
            return requestMarketCheck;
        }

        public void setRequestMarketCheck(RequestMarketCheck requestMarketCheck) {
            this.requestMarketCheck = requestMarketCheck;
        }

        @Override
        public String toString() {
            return "ServerMarketCheckRequest{" +
                    "requestMarketCheck=" + requestMarketCheck +
                    '}';
        }
    }


    public static class RequestMarketCheck {
        public String packageName = "";
        public String currentAppVersion = "";
        public String marketVersion = "";
        public boolean enableUpdate = false;
    }

}
