package com.inka.netsync.controler;

import com.inka.netsync.common.AppConstants;
import com.inka.netsync.data.cache.pref.PreferencesCacheHelper;
import com.inka.netsync.logs.LogUtil;

public class SettingControler {

    private final String TAG = "SettingControler";

    private static volatile SettingControler defaultInstance;

    public static SettingControler getDefault() {
        if (defaultInstance == null) {
            synchronized (SettingControler.class) {
                if (defaultInstance == null) {
                    defaultInstance = new SettingControler();
                }
            }
        }
        return defaultInstance;
    }

    private SettingControler() {
    }

    public String getPlayerType () {
        String playerType = null;
        try {
            int value = PreferencesCacheHelper.getPreferenceValueForInteger(PreferencesCacheHelper.SETTING_PLAYER, PreferencesCacheHelper.VISUALON_PLAYER);
            LogUtil.INSTANCE.info("birdgangbasesettingdialog" , "getPlayerType > value : " + value);
            if (value == PreferencesCacheHelper.BASE_PLAYER) {
                playerType = AppConstants.TYPE_PLAYER_NATIVE;
            } else if (value == PreferencesCacheHelper.VISUALON_PLAYER) {
                playerType = AppConstants.TYPE_PLAYER_VISUALONPLAYER;
            }
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
        return playerType;
    }

    public String getSwXPlay () {
        String swXPlay = null;
        try {
            int value = getDecoderType();
            switch (value) {
                case PreferencesCacheHelper.DECODER_SOFTWARE:
                    swXPlay = AppConstants.TYPE_SWXPLAY_ALLOW;
                    break;
                case PreferencesCacheHelper.DECODER_HARDWARE:
                    swXPlay = AppConstants.TYPE_SWXPLAY_NO_ALLOW;
                    break;
                default:
                    swXPlay = AppConstants.TYPE_SWXPLAY_NO_ALLOW;
                    break;
            }
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
        return swXPlay;
    }

    public int getDecoderType () {
        return PreferencesCacheHelper.getPreferenceValueForInteger(PreferencesCacheHelper.SETTING_DECODER, PreferencesCacheHelper.DECODER_HARDWARE);
    }

    public boolean getEnableGestureOnPlayer () {
        boolean enableGestureOnPlayer = PreferencesCacheHelper.getPreferenceValueForBol(PreferencesCacheHelper.GESTURE, true);
        return enableGestureOnPlayer;
    }

    public void setEnableGestureOnPlayer (boolean enable) {
        PreferencesCacheHelper.setPreferenceValueForBol(PreferencesCacheHelper.GESTURE, enable);
    }

    public boolean getEnableRestrictedInternet () {
        boolean enableGestureOnPlayer = PreferencesCacheHelper.getPreferenceValueForBol(PreferencesCacheHelper.SETTING_RESTRICTED_INTERNET, false);
        return enableGestureOnPlayer;
    }

    public void setEnableRestrictedInternet (boolean enable) {
        PreferencesCacheHelper.setPreferenceValueForBol(PreferencesCacheHelper.SETTING_RESTRICTED_INTERNET, enable);
    }

    public boolean getShownPlayerGuide () {
        boolean shownGuide = PreferencesCacheHelper.getPreferenceValueForBol(PreferencesCacheHelper.GUIDE_PLAYER_INIT, false);
        return shownGuide;
    }

}
