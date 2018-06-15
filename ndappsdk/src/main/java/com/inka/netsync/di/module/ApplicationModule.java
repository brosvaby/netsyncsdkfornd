package com.inka.netsync.di.module;

import android.app.Application;
import android.content.Context;

import com.inka.netsync.common.AppConstants;
import com.inka.netsync.data.AppDataManager;
import com.inka.netsync.data.DataManager;
import com.inka.netsync.data.network.ApiHelper;
import com.inka.netsync.data.network.AppApiHelper;
import com.inka.netsync.data.prefs.AppPreferencesHelper;
import com.inka.netsync.data.prefs.PreferencesHelper;
import com.inka.netsync.data.resource.AppResourcesHelper;
import com.inka.netsync.data.resource.ResourcesHelper;
import com.inka.netsync.di.ApplicationContext;
import com.inka.netsync.di.DatabaseInfo;
import com.inka.netsync.di.PreferenceInfo;
import com.inka.netsync.di.ResourceInfo;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    private final Application mApplication;

    public ApplicationModule(Application application) {
        mApplication = application;
    }

    @Provides
    @ApplicationContext
    Context provideContext() {
        return mApplication;
    }

    @Provides
    Application provideApplication() {
        return mApplication;
    }

    @Provides
    @DatabaseInfo
    String provideDatabaseName() {
        return AppConstants.DB_NAME;
    }

    @Provides
    @PreferenceInfo
    String providePreferenceName() {
        return AppConstants.PREF_NAME;
    }

    @Provides
    @ResourceInfo
    String provideResourceName() {
        return AppConstants.PREF_NAME;
    }

    @Provides
    @Singleton
    DataManager provideDataManager(AppDataManager appDataManager) {
        return appDataManager;
    }

    @Provides
    @Singleton
    PreferencesHelper providePreferencesHelper(AppPreferencesHelper appPreferencesHelper) {
        return appPreferencesHelper;
    }

    @Provides
    @Singleton
    ResourcesHelper provideResourcesHelper (AppResourcesHelper appResourcesHelper) {
        return appResourcesHelper;
    }

    @Provides
    @Singleton
    ApiHelper provideApiHelper(AppApiHelper appApiHelper) {
        return appApiHelper;
    }

}