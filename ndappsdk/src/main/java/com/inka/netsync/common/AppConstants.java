package com.inka.netsync.common;

/**
 * Created by birdgang on 2017. 4. 14..
 */

public final class AppConstants {

    public static String APPLICATION_ID = "com.inka.netsync";

    // player type
    public static final String TYPE_PLAYER_NATIVE = "PLAYERTYPE_NATIVE";
    public static final String TYPE_PLAYER_EXOPLAYER = "PLAYERTYPE_EXOPLAYER";
    public static final String TYPE_PLAYER_CODENPLAYER = "PLAYERTYPE_CODENPLAYER";
    public static final String TYPE_PLAYER_EROUMPLAYER = "PLAYERTYPE_EROUMPLAYER";
    public static final String TYPE_PLAYER_VISUALONPLAYER = "PLAYERTYPE_VISUALONPLAYER";

    // swxplay type
    public static final String TYPE_SWXPLAY_NO_ALLOW = "SWXPLAY_NO_ALLOW";
    public static final String TYPE_SWXPLAY_ALLOW = "SWXPLAY_ALLOW";

    // play type
    public static final String TYPE_PLAY_DOWNLOAD = "PLAYTYPE_DOWNLOAD";
    public static final String TYPE_PLAY_FAVORITE = "PLAYTYPE_FAVORITE";
    public static final String TYPE_PLAY_PLAYEDLIST = "PLAYTYPE_PLAYEDLIST";
    public static final String TYPE_PLAY_BOOKMARK = "PLAYTYPE_BOOKMARK";
    public static final String TYPE_PLAY_SEARCH = "PLAYTYPE_SEARCH";


    // play state
    public static final String STATE_PLAYT_ONSTOP = "PLAYTYPE_ONSTOP";
    public static final String STATE_PLAYT_ONRESUME = "PLAYTYPE_ONRESUME";
    public static final String STATE_PLAYT_ONPAUSE = "PLAYTYPE_ONPAUSE";


    // list order
    public static final String LIST_ORDER_DEFAULT = "DEFAULT";
    public static final String LIST_ORDER_NAME_ASC = "NAME_ASC";
    public static final String LIST_ORDER_NAME_DES = "NAME_DES";
    public static final String LIST_ORDER_DATE_ASC = "DATE_ASC";
    public static final String LIST_ORDER_DATE_DES = "DATE_DES";
    public static final String LIST_ORDER_DATA_ASC = "DATA_ASC";
    public static final String LIST_ORDER_DATA_DES = "DATA_DES";
    public static final String LIST_ORDER_PLAYDATE_ASC = "PLAYDATE_ASC";
    public static final String LIST_ORDER_PLAYDATE_DES = "PLAYDATE_DES";



    // list type
    public static final String LIST_TYPE_FOLDER = "LIST_TYPE_FOLDER";
    public static final String LIST_TYPE_FILE = "LIST_TYPE_FILE";
    public static final String LIST_TYPE_SEARCH = "LIST_TYPE_SEARCH";


    public static final String APPUITYPE_MAIN = "APPUITYPE_MAIN";
    public static final String APPUITYPE_DRAWER = "APPUITYPE_DRAWER";


    /************************************************
     * 스마트넷싱크의 현재 상태를 표시한다.
     *************************************************/
    public static final String APPLICATION_STATE_READY = "STATE_READY";
    public static final String APPLICATION_STATE_DOWNLOAD = "STATE_DOWNLOAD";
    public static final String APPLICATION_STATE_PLAY = "STATE_PLAY";


    public static final int MESSAGE_EARPHONECHANGE_HANDLER_REMOTE_CLICKED = 0;


    public static final String DB_NAME = "smartnetsync_db.db";
    public static final String PREF_NAME = "smartnetsync_pref";

    public final static int ACT_PLAYER = 1000;
    public final static int ACT_CAPTURE_SERIAL = 1001;

}
