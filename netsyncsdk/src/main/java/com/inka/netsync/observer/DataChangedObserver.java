package com.inka.netsync.observer;

import android.database.ContentObserver;
import android.os.Handler;

import com.inka.netsync.BaseApplication;
import com.inka.netsync.data.cache.pref.PreferencesCacheHelper;
import com.inka.netsync.logs.LogUtil;

public class DataChangedObserver extends ContentObserver {

    private final String TAG = "DataChangedObserver";

    public DataChangedObserver(Handler handler) {
        super(handler);
        LogUtil.INSTANCE.info(TAG, "DataChangedObserver");
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        PreferencesCacheHelper.initContext(BaseApplication.getContext());
        PreferencesCacheHelper.setPreferenceValueForBol(PreferencesCacheHelper.AUTO_RESCAN, true);
    }

}