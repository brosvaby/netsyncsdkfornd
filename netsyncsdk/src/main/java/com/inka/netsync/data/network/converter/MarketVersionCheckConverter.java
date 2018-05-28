package com.inka.netsync.data.network.converter;

import com.inka.netsync.common.utils.StringUtil;
import com.inka.netsync.data.network.model.MarketVersionCheckEntry;
import com.inka.netsync.data.network.response.MarketVersionCheckResponse;
import com.inka.netsync.logs.LogUtil;

/**
 * Created by birdgang on 2018. 5. 11..
 */

public class MarketVersionCheckConverter {

    private final String TAG  = "MarketVersionCheckConverter";

    public MarketVersionCheckResponse converter(MarketVersionCheckEntry marketVersionCheckEntry, String resource) throws Exception {

        MarketVersionCheckResponse responseMarketVersionCheck = new MarketVersionCheckResponse();

        String result = "";
        int start = 0;
        int end = 0;

        try {
            start = resource.indexOf("softwareVersion") + "softwareVersion".length() + 2;
            end = resource.indexOf("</div>", start);
            result = resource.substring(start, end);
            result = result.replace(" ", "");
        } catch (Exception e) {
            result = "";
        }

        String currentAppVersion = marketVersionCheckEntry.getCurrentAppVersion();

        int marketAppVersion = StringUtil.convertIntFromTokenizeredString(result);
        int installedAppVersion = StringUtil.convertIntFromTokenizeredString(currentAppVersion);
        LogUtil.INSTANCE.info(TAG, "after > marketAppVersion : " + marketAppVersion + " , installedAppVersion : " + installedAppVersion);

        responseMarketVersionCheck.setCurrentAppVersion(marketVersionCheckEntry.getCurrentAppVersion());
        responseMarketVersionCheck.setPackageName(marketVersionCheckEntry.getPackageName());
        responseMarketVersionCheck.setMarketVersion(result);
        if (marketAppVersion > installedAppVersion) {
            responseMarketVersionCheck.setEnableUpdate(true);
        } else {
            responseMarketVersionCheck.setEnableUpdate(false);
        }

        return responseMarketVersionCheck;
    }

}
