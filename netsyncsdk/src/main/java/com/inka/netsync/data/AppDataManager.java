package com.inka.netsync.data;

import android.content.Context;

import com.inka.netsync.data.network.ApiHelper;
import com.inka.netsync.data.network.request.MarketCheckRequest;
import com.inka.netsync.data.network.request.SerialAuthRequest;
import com.inka.netsync.data.network.response.SerialAuthResponse;
import com.inka.netsync.data.prefs.PreferencesHelper;
import com.inka.netsync.data.resource.ResourcesHelper;
import com.inka.netsync.di.ApplicationContext;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;

@Singleton
public class AppDataManager implements DataManager {

    private final PreferencesHelper mPreferencesHelper;
    private final ResourcesHelper mResourcesHelper;
    private final ApiHelper mApiHelper;

    @Inject
    public AppDataManager(@ApplicationContext Context context, PreferencesHelper preferencesHelper, ResourcesHelper resourcesHelper, ApiHelper apiHelper) {
        mPreferencesHelper = preferencesHelper;
        mResourcesHelper = resourcesHelper;
        mApiHelper = apiHelper;
    }

    @Override
    public Context getContext() {
        return mResourcesHelper.getContext();
    }

    @Override
    public String getStringResource(int resource) {
        return mResourcesHelper.getStringResource(resource);
    }

    @Override
    public String[] getStringArrayResource(int resource) {
        return mResourcesHelper.getStringArrayResource(resource);
    }

    @Override
    public Observable<SerialAuthResponse> getSerialAuthApiCall(SerialAuthRequest.ServerSerialAuthRequest request) throws Exception {
        return mApiHelper.getSerialAuthApiCall(request);
    }

    @Override
    public Observable<String> getSerialAuthStringApiCall(SerialAuthRequest.ServerSerialAuthRequest request) throws Exception {
        return mApiHelper.getSerialAuthStringApiCall(request);
    }

    @Override
    public Observable<String> getStringMarketVersionCheckApiCall(MarketCheckRequest.ServerMarketCheckRequest request) {
        return mApiHelper.getStringMarketVersionCheckApiCall(request);
    }

}
