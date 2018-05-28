package com.inka.netsync.data.prefs;

import android.content.Context;
import android.content.SharedPreferences;

import com.inka.netsync.data.cache.pref.PreferencesCacheHelper;
import com.inka.netsync.di.ApplicationContext;
import com.inka.netsync.di.PreferenceInfo;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by birdgang on 2017. 4. 10..
 */
@Singleton
public class AppPreferencesHelper implements PreferencesHelper {

    private static final String PREF_KEY_LOGINURL = "PREF_KEY_LOGINURL";

    private final SharedPreferences mPrefs;

    @Inject
    public AppPreferencesHelper(@ApplicationContext Context context, @PreferenceInfo String prefFileName) {
        mPrefs = context.getSharedPreferences(prefFileName, Context.MODE_PRIVATE);
        PreferencesCacheHelper.initContext(context);
    }

}
