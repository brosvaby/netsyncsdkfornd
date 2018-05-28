package com.inka.netsync.data.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ApiErrorResponse {

    @Expose
    @SerializedName("result")
    private ApiError result;

    public ApiError getResult() {
        return result;
    }

    public void setResult(ApiError result) {
        this.result = result;
    }
}