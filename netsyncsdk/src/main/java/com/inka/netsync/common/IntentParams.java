package com.inka.netsync.common;

/**
 * Created by birdgang on 2017. 6. 13..
 */

public class IntentParams {

    public static final int REQUEST_CODE_GET_PERMISSION_WRITE_SDCARD = 42;

    public static final String ACTION_GET_PERMISSION_WRITE_SDCARD = "android.content.extra.SHOW_ADVANCED";

    public static String INTENT_PARAMS_KEY_PLAYER_TYPE = "playerType";
    public static String INTENT_PARAMS_KEY_SW_XPLAY = "swXPlay";
    public static String INTENT_PARAMS_KEY_PLAY_TYPE = "playType";
    public static String INTENT_PARAMS_KEY_USER_ID = "userID";
    public static String INTENT_PARAMS_KEY_CONTENT_PATH = "path";
    public static String INTENT_PARAMS_KEY_FILE_SIZE = "fileSize";
    public static String INTENT_PARAMS_KEY_IS_ALLOW_CELLULAR_NETWORK = "isAllowCellularNetwork";
    public static String INTENT_PARAMS_KEY_SCHEME_META_DATA = "schemeMetaData";
    public static String INTENT_PARAMS_KEY_CONTENT_ID = "content_id";
    public static String INTENT_PARAMS_KEY_BOOKMART_ID = "bookmark_id";
    public static String INTENT_PARAMS_KEY_CAPTURED_SERIAL_NUMBER = "captured_serial_number";


    public final static String MEDIA_ITEM_LOCATION = "item_location";
    public final static String MEDIA_SUBTITLES_LOCATION = "subtitles_location";
    public final static String MEDIA_ITEM_TITLE = "title";
    public final static String MEDIA_FROM_START = "from_start";
    public final static String MEDIA_OPENED_POSITION = "opened_position";
    public final static String MEDIA_DISABLE_HARDWARE = "disable_hardware";
    public final static String MEDIA_PLAY_TYPE = "playType";
    public final static String MEDIA_PLAYER_TYPE = "playerType";
    public final static String USER_ID = "user_id";
    public final static String MEDIA_ITEM_SIZE = "fileSize";
    public final static String MEDIA_IS_ALLOW_CELLULAR_NETWORK = "isAllowCellularNetwork";
    public final static String MEDIA_SCHEME_META_DATA = "schemeMetaData";
    public final static String MEDIA_CONTENT_ID = "content_id";
    public final static String MEDIA_CONTENT = "content";
    public final static String MEDIA_BOOKMARK = "bookmark";
    public final static String MEDIA_SWX_PLAY = "swXPlay";
    public final static String MEDIA_SHOWN_PLAYER_GUIDE = "shown_player_guide";


    // player overlay
    public static final int OVERLAY_TIMEOUT = 4000;
    public static final int OVERLAY_INFINITE = -1;
    public static final int FADE_OUT = 1;
    public static final int SHOW_PROGRESS = 2;
    public static final int FADE_OUT_INFO = 3;
    public static final int START_PLAYBACK = 4;
    public static final int AUDIO_SERVICE_CONNECTION_FAILED = 5;
    public static final int RESET_BACK_LOCK = 6;
    public static final int CHECK_VIDEO_TRACKS = 7;
    public static final int HW_ERROR = 1000;

}
