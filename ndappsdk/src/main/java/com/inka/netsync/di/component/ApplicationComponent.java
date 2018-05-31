package com.inka.netsync.di.component;

import android.app.Application;
import android.content.Context;

import com.inka.netsync.BaseApplication;
import com.inka.netsync.data.DataManager;
import com.inka.netsync.di.ApplicationContext;
import com.inka.netsync.di.module.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by birdgang on 2017. 2. 26..
 */

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    void inject(BaseApplication app);

    @ApplicationContext
    Context context();

    Application application();
    DataManager getDataManager();
}
