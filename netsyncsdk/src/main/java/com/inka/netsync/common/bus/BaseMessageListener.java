package com.inka.netsync.common.bus;

import com.inka.netsync.data.network.model.BaseResponseEntry;

public interface BaseMessageListener {
    public void onResult(BaseResponseEntry response);
}