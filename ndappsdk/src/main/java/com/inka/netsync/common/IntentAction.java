package com.inka.netsync.common;

public class IntentAction {

    public static final String INTENT_ACTION_SDCARD_MEDIA = buildPkgString("sdcard.media");
    public static final String INTENT_ACTION_OPEN_PLAYER = buildPkgString("action.openplayer");

    public static String buildPkgString(String string) {
        return AppConstants.APPLICATION_ID + "." + string;
    }
}
