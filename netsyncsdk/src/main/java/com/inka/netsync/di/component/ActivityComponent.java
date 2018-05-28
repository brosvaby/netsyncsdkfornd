package com.inka.netsync.di.component;

import com.inka.netsync.di.PerActivity;
import com.inka.netsync.di.module.ActivityModule;
import com.inka.netsync.sd.ui.SDCertificationActivity;
import com.inka.netsync.sd.ui.SDDrawerPlayerActivity;
import com.inka.netsync.sd.ui.SDSearchActivity;
import com.inka.netsync.sd.ui.fragment.SDExplorerFragment;
import com.inka.netsync.sd.ui.fragment.SDFavoriteFragment;
import com.inka.netsync.sd.ui.fragment.SDInfoFragment;
import com.inka.netsync.sd.ui.fragment.SDRecentlyFragment;
import com.inka.netsync.sd.ui.fragment.SDSearchFragment;
import com.inka.netsync.sd.ui.fragment.SDSettingFragment;
import com.inka.netsync.ui.DrawerActivity;
import com.inka.netsync.ui.DrawerActivityPlayer;
import com.inka.netsync.ui.GuideActivity;
import com.inka.netsync.ui.PlayerActivity;
import com.inka.netsync.ui.SearchActivity;
import com.inka.netsync.ui.SplashActivity;
import com.inka.netsync.ui.fragment.DefaultFragment;
import com.inka.netsync.ui.fragment.ExplorerFavoriteFragment;
import com.inka.netsync.ui.fragment.ExplorerFragment;
import com.inka.netsync.ui.fragment.ExplorerRecentlyFragment;
import com.inka.netsync.ui.fragment.ExplorerSearchFragment;
import com.inka.netsync.ui.fragment.InfoFragment;
import com.inka.netsync.ui.fragment.InfoWebViewFragment;
import com.inka.netsync.ui.fragment.NavigationDrawerFragment;
import com.inka.netsync.ui.fragment.SettingFragment;
import com.inka.netsync.ui.fragment.WebViewFragment;

import dagger.Component;

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    /**
     * 범용.
     * @param activity
     */
    void inject(SplashActivity activity);
    void inject(GuideActivity activity);
    void inject(DrawerActivity activity);
    void inject(DrawerActivityPlayer activity);
    void inject(PlayerActivity activity);
    void inject(SearchActivity activity);


    void inject(ExplorerFavoriteFragment fragment);
    void inject(ExplorerRecentlyFragment fragment);
    void inject(ExplorerFragment fragment);
    void inject(ExplorerSearchFragment fragment);
    void inject(DefaultFragment fragment);
    void inject(NavigationDrawerFragment fragment);


    /**
     * 전용.
     * @param activity
     */
    void inject(SDDrawerPlayerActivity activity);
    void inject(SDCertificationActivity activity);
    void inject(SDSearchActivity activity);
    void inject(SDFavoriteFragment fragment);
    void inject(SDRecentlyFragment fragment);
    void inject(SDExplorerFragment fragment);
    void inject(SDSearchFragment fragment);
    void inject(SDSettingFragment fragment);
    void inject(SDInfoFragment fragment);


    /**
     * 공통.
     * @param fragment
     */
    void inject(InfoFragment fragment);
    void inject(WebViewFragment fragment);
    void inject(SettingFragment fragment);
    void inject(InfoWebViewFragment fragment);

}