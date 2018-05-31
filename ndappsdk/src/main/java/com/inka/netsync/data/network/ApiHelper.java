package com.inka.netsync.data.network;

import com.inka.netsync.data.network.request.MarketCheckRequest;
import com.inka.netsync.data.network.request.SerialAuthRequest;
import com.inka.netsync.data.network.response.SerialAuthResponse;

import io.reactivex.Observable;

public interface ApiHelper {
    // serial
    Observable<SerialAuthResponse> getSerialAuthApiCall(SerialAuthRequest.ServerSerialAuthRequest request) throws Exception;
    Observable<String> getSerialAuthStringApiCall(SerialAuthRequest.ServerSerialAuthRequest request) throws Exception;

    // market version check
    Observable<String> getStringMarketVersionCheckApiCall(MarketCheckRequest.ServerMarketCheckRequest request);
}