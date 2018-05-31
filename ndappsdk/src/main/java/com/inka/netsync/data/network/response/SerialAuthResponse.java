package com.inka.netsync.data.network.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.inka.netsync.data.network.model.SerialAuth;

/**
 * Created by birdgang on 2018. 4. 12..
 */

public class SerialAuthResponse {
    @Expose
    @SerializedName("result")
    private SerialAuth result;

    public SerialAuth getResult() {
        return result;
    }

    public void setResult(SerialAuth result) {
        this.result = result;
    }
}