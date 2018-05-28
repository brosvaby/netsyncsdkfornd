package com.inka.netsync.data.network;

import com.inka.netsync.data.network.model.ResponseSerialAuthEntry;
import com.inka.netsync.data.network.model.SerialAuthEntry;

public interface ApiServiceClient {
    public ResponseSerialAuthEntry requestSerialAuthentication(String url, SerialAuthEntry requestSerialAuthEntry) throws Exception;
}
