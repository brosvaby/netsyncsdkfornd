package com.inka.netsync.data.cache.pref;

import android.content.Context;
import android.content.SharedPreferences;

import com.inka.netsync.data.BaseData;

public class PreferencesCacheHelper {

    // value
    public final static int PLAYER = -1;
    public final static int BASE_PLAYER = 0;
    public final static int VISUALON_PLAYER = 1;

    public final static int DECODER_HARDWARE = 0;
    public final static int DECODER_SOFTWARE = 1;


    public final static int SORT_NAME_ASC = 0;
    public final static int SORT_NAME_DESC = 1;


    // key
    public final static String SELECTED_DRAWER_MENU = "selected_drawer_menu";
    public final static String HAS_RUN_INTRO_GUIDE = "has_run_intro_guide";
    public final static String HAS_RUN_INIT_FIRST = "has_run_init_first";

    public final static String AUTO_RESCAN = "auto_rescan";
    public final static String SCREEN_ORIENTATION_VALUE = "screen_orientation_value";          // 비디오 화면 회전 방향 설정
    public final static String SCREEN_BRIGHTNESS = "screen_brightness";             // 화면 밝기 기본 값
    public final static String USED_BASE_VIDEO_PLAYER = "used_base_video_player";
    public final static String DEVICE_ID = "device_id";
    public final static String DEVICE_ID_FOR_PALLYCOND = "device_id_for_pallycond";

    public final static String SORT_NAME = "sort_name";


    public final static String GUIDE_PLAYER_INIT = "guide_player_init";
    public final static String SKIP_TIME = "skip_time";
    public final static String ROTATE_SCREEN_LOCK = "rotate_screen_lock";
    public final static String TOUCH_SCREEN_LOCK = "rotate_screen";
    public final static String SCREEN_RATE = "screen_rate";

    public final static String GESTURE_SOUND = "gesture_sound";
    public final static String GESTURE_BRIGHTNESS = "gesture_brightness";
    public final static String GESTURE_BRIGHTNESS_VLUES = "gesture_brightness_value";
    public final static String GESTURE = "gesture";


    // setting
    public final static String SETTING_PLAYER = "setting_player";
    public final static String SETTING_DECODER = "setting_decoder";
    public final static String SETTING_RESTRICTED_INTERNET = "setting_restricted_internet";


    // preview
    public final static String PREVIEW_BACKGROUND_COLOR = "preview_background_color";


    private static Context context;
    private static SharedPreferences preferences;
    private static SharedPreferences.Editor editor;


    public static void initContext (Context context) {
        preferences = context.getSharedPreferences(BaseData.PREF_NAME_SMARTNETSYNC_VALUE, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public static final boolean getPreferenceValueForBol(String key, boolean defValue) {
        return preferences.getBoolean(key, defValue);
    }

    public static final void setPreferenceValueForBol(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static final int getPreferenceValueForInteger(String key, int defValue) {
        return preferences.getInt(key, defValue);
    }

    public static final void setPreferenceValueForInteger(String key, int value) {
        editor.putInt(key, value);
        editor.commit();
    }

    public static final float getPreferenceValueForFloat(String key, float defValue) {
        return preferences.getFloat(key, defValue);
    }

    public static final void setPreferenceValueForFloat(String key, float value) {
        editor.putFloat(key, value);
        editor.commit();
    }

    public static final String getPreferenceValue(String key, String defVal) {
        return preferences.getString(key, defVal);
    }

    final static public void setPreferenceValue(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }


}
