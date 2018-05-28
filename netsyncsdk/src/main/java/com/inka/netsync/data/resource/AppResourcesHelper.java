package com.inka.netsync.data.resource;

import android.content.Context;
import android.content.res.Resources;

import com.inka.netsync.di.ApplicationContext;
import com.inka.netsync.di.ResourceInfo;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by birdgang on 2018. 1. 24..
 */

@Singleton
public class AppResourcesHelper implements ResourcesHelper {

    private final Context mContext;
    private final Resources mResources;

    @Inject
    public AppResourcesHelper(@ApplicationContext Context context, @ResourceInfo String resourceName) {
        mContext = context;
        mResources = context.getResources();
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    @Override
    public String getStringResource(int resource) {
        return mResources.getString(resource);
    }

    @Override
    public String[] getStringArrayResource(int resource) {
        return mResources.getStringArray(resource);
    }

}