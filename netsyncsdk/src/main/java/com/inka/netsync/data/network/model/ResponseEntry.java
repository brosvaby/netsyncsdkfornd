package com.inka.netsync.data.network.model;

/**
 * Created by birdgang on 2017. 4. 26..
 */

public interface ResponseEntry extends BaseResponseEntry {
    boolean success = false;
    String message = "";
}