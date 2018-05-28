package com.inka.netsync.common;

public class IntentAction {

    /**
     * loading intent
     */
    public static final String INTENT_ACTION_MEDIA_SCAN_START = buildPkgString("action.scanstart");
    public static final String INTENT_ACTION_MEDIA_SCAN_STOP = buildPkgString("action.scanstop");


    public static final String INTENT_ACTION_SDCARD_MEDIA = buildPkgString("sdcard.media");
    public static final String INTENT_ACTION_OPEN_PLAYER = buildPkgString("action.openplayer");

    public static String buildPkgString(String string) {
//        return BaseConfiguration.APPLICATION_ID + "." + string;
        return "";
    }
}
