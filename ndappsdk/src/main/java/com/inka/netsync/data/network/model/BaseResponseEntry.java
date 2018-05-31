package com.inka.netsync.data.network.model;

/**
 * Created by birdgang on 2018. 4. 24..
 */
public interface BaseResponseEntry {
    public final String RESPONSE_SUCCESS = "0";
    public final String RESPONSE_FAIL = "2";

    public boolean success = false;
    public String message = "";

}
