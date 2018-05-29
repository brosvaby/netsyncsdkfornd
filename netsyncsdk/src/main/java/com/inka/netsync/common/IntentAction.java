package com.inka.netsync.common;

import com.inka.netsync.BaseConfiguration;

public class IntentAction {

    public static final String INTENT_ACTION_SDCARD_MEDIA = buildPkgString("sdcard.media");
    public static final String INTENT_ACTION_OPEN_PLAYER = buildPkgString("action.openplayer");

    public static String buildPkgString(String string) {
        return BaseConfiguration.APPLICATION_ID + "." + string;
    }
}
