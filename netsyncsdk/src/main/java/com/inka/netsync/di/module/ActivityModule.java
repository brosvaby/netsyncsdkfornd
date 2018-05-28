package com.inka.netsync.di.module;

import android.app.Activity;
import android.content.Context;

import com.inka.netsync.di.ActivityContext;
import com.inka.netsync.di.PerActivity;
import com.inka.netsync.sd.ui.mvppresenter.SDCertificationMvpPresenter;
import com.inka.netsync.sd.ui.mvppresenter.SDExplorerDrawerMvpPresenter;
import com.inka.netsync.sd.ui.mvppresenter.SDExplorerFavoriteMvpPresenter;
import com.inka.netsync.sd.ui.mvppresenter.SDExplorerMvpPresenter;
import com.inka.netsync.sd.ui.mvppresenter.SDExplorerRecentlyMvpPresenter;
import com.inka.netsync.sd.ui.mvppresenter.SDExplorerSearchMvpPresenter;
import com.inka.netsync.sd.ui.mvppresenter.SDExplorerStackMvpPresenter;
import com.inka.netsync.sd.ui.mvppresenter.SDInfoMvpPresenter;
import com.inka.netsync.sd.ui.mvppresenter.SDSettingMvpPresenter;
import com.inka.netsync.sd.ui.mvpview.SDCertificationMvpView;
import com.inka.netsync.sd.ui.mvpview.SDExplorerDrawerMvpView;
import com.inka.netsync.sd.ui.mvpview.SDExplorerFavoriteMvpView;
import com.inka.netsync.sd.ui.mvpview.SDExplorerMvpView;
import com.inka.netsync.sd.ui.mvpview.SDExplorerRecentlyMvpView;
import com.inka.netsync.sd.ui.mvpview.SDExplorerSearchMvpView;
import com.inka.netsync.sd.ui.mvpview.SDExplorerStackMvpView;
import com.inka.netsync.sd.ui.mvpview.SDInfoMvpView;
import com.inka.netsync.sd.ui.mvpview.SDSettingMvpView;
import com.inka.netsync.sd.ui.presenter.SDCertificationPresenter;
import com.inka.netsync.sd.ui.presenter.SDExplorerDrawerPresenter;
import com.inka.netsync.sd.ui.presenter.SDExplorerFavoritePresenter;
import com.inka.netsync.sd.ui.presenter.SDExplorerPresenter;
import com.inka.netsync.sd.ui.presenter.SDExplorerRecentlyPresenter;
import com.inka.netsync.sd.ui.presenter.SDExplorerSearchPresenter;
import com.inka.netsync.sd.ui.presenter.SDExplorerStackPresenter;
import com.inka.netsync.sd.ui.presenter.SDInfoPresenter;
import com.inka.netsync.sd.ui.presenter.SDSettingPresenter;
import com.inka.netsync.ui.mvppresenter.BasePlayMvpPresenter;
import com.inka.netsync.ui.mvppresenter.BaseWebViewMvpPresenter;
import com.inka.netsync.ui.mvppresenter.DefaultMvpPresenter;
import com.inka.netsync.ui.mvppresenter.DrawerMvpPresenter;
import com.inka.netsync.ui.mvppresenter.ExplorerFavoriteMvpPresenter;
import com.inka.netsync.ui.mvppresenter.ExplorerMvpPresenter;
import com.inka.netsync.ui.mvppresenter.ExplorerRecentlyMvpPresenter;
import com.inka.netsync.ui.mvppresenter.ExplorerSearchMvpPresenter;
import com.inka.netsync.ui.mvppresenter.ExplorerStackMvpPresenter;
import com.inka.netsync.ui.mvppresenter.GuideMvpPresenter;
import com.inka.netsync.ui.mvppresenter.InfoMvpPresenter;
import com.inka.netsync.ui.mvppresenter.NavigationDrawerMvpPresenter;
import com.inka.netsync.ui.mvppresenter.PlayerMvpPresenter;
import com.inka.netsync.ui.mvppresenter.SearchContainerMvpPresenter;
import com.inka.netsync.ui.mvppresenter.SettingMvpPresenter;
import com.inka.netsync.ui.mvppresenter.SplashMvpPresenter;
import com.inka.netsync.ui.mvpview.BasePlayMvpView;
import com.inka.netsync.ui.mvpview.BaseWebViewMvpView;
import com.inka.netsync.ui.mvpview.DefaultMvpView;
import com.inka.netsync.ui.mvpview.DrawerMvpView;
import com.inka.netsync.ui.mvpview.ExplorerFavoriteMvpView;
import com.inka.netsync.ui.mvpview.ExplorerMvpView;
import com.inka.netsync.ui.mvpview.ExplorerRecentlyMvpView;
import com.inka.netsync.ui.mvpview.ExplorerSearchMvpView;
import com.inka.netsync.ui.mvpview.ExplorerStackMvpView;
import com.inka.netsync.ui.mvpview.GuideMvpView;
import com.inka.netsync.ui.mvpview.InfoMvpView;
import com.inka.netsync.ui.mvpview.NavigationDrawerMvpView;
import com.inka.netsync.ui.mvpview.PlayerMvpView;
import com.inka.netsync.ui.mvpview.SearchContainerMvpView;
import com.inka.netsync.ui.mvpview.SettingMvpView;
import com.inka.netsync.ui.mvpview.SplashMvpView;
import com.inka.netsync.ui.presenter.BasePlayPresenter;
import com.inka.netsync.ui.presenter.BaseWebViewPresenter;
import com.inka.netsync.ui.presenter.DefaultPresenter;
import com.inka.netsync.ui.presenter.DrawerPresenter;
import com.inka.netsync.ui.presenter.ExplorerFavoritePresenter;
import com.inka.netsync.ui.presenter.ExplorerPresenter;
import com.inka.netsync.ui.presenter.ExplorerRecentlyPresenter;
import com.inka.netsync.ui.presenter.ExplorerSearchPresenter;
import com.inka.netsync.ui.presenter.ExplorerStackPresenter;
import com.inka.netsync.ui.presenter.GuidePresenter;
import com.inka.netsync.ui.presenter.InfoPresenter;
import com.inka.netsync.ui.presenter.NavigationDrawerPresenter;
import com.inka.netsync.ui.presenter.PlayerPresenter;
import com.inka.netsync.ui.presenter.SearchContainerPresenter;
import com.inka.netsync.ui.presenter.SettingPresenter;
import com.inka.netsync.ui.presenter.SplashPresenter;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;


@Module
public class ActivityModule {
    private Activity mActivity;

    public ActivityModule(Activity activity) {
        this.mActivity = activity;
    }

    @Provides
    @ActivityContext
    Context provideContext() {
        return mActivity;
    }

    @Provides
    Activity provideActivity() {
        return mActivity;
    }

    @Provides
    CompositeDisposable provideCompositeDisposable() {
        return new CompositeDisposable();
    }

    @Provides
    @PerActivity
    SplashMvpPresenter<SplashMvpView> provideSplashPresenter(SplashPresenter<SplashMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    GuideMvpPresenter<GuideMvpView> provideGuidePresenter(GuidePresenter<GuideMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    SDCertificationMvpPresenter<SDCertificationMvpView> provideSDCertificationPresenter(SDCertificationPresenter<SDCertificationMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    DrawerMvpPresenter<DrawerMvpView> provideDrawerPresenter(DrawerPresenter<DrawerMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    SDExplorerDrawerMvpPresenter<SDExplorerDrawerMvpView> provideSDExplorerDrawerPresenter(SDExplorerDrawerPresenter<SDExplorerDrawerMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    PlayerMvpPresenter<PlayerMvpView> providePlayerPresenter(PlayerPresenter<PlayerMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    SearchContainerMvpPresenter<SearchContainerMvpView> provideSearchContainerPresenter(SearchContainerPresenter<SearchContainerMvpView> presenter) {
        return presenter;
    }

    @Provides
    DefaultMvpPresenter<DefaultMvpView> provideDefaultPresenter (DefaultPresenter<DefaultMvpView> presenter) {
        return presenter;
    }

    @Provides
    NavigationDrawerMvpPresenter<NavigationDrawerMvpView> provideNavigationDrawerPresenter (NavigationDrawerPresenter<NavigationDrawerMvpView> presenter) {
        return presenter;
    }

    @Provides
    BaseWebViewMvpPresenter<BaseWebViewMvpView> provideBaseWebViewPresenter (BaseWebViewPresenter<BaseWebViewMvpView> presenter) {
        return presenter;
    }

    @Provides
    SettingMvpPresenter<SettingMvpView> provideSettingPresenter (SettingPresenter<SettingMvpView> presenter) {
        return presenter;
    }

    @Provides
    SDSettingMvpPresenter<SDSettingMvpView> provideSDSettingPresenter (SDSettingPresenter<SDSettingMvpView> presenter) {
        return presenter;
    }

    @Provides
    ExplorerMvpPresenter<ExplorerMvpView> provideExplorerPresenter (ExplorerPresenter<ExplorerMvpView> presenter) {
        return presenter;
    }

    @Provides
    SDExplorerMvpPresenter<SDExplorerMvpView> provideSDExplorerPresenter (SDExplorerPresenter<SDExplorerMvpView> presenter) {
        return presenter;
    }

    @Provides
    ExplorerStackMvpPresenter<ExplorerStackMvpView> provideExplorerStackPresenter (ExplorerStackPresenter<ExplorerStackMvpView> presenter) {
        return presenter;
    }

    @Provides
    SDExplorerStackMvpPresenter<SDExplorerStackMvpView> provideSDExplorerStackPresenter (SDExplorerStackPresenter<SDExplorerStackMvpView> presenter) {
        return presenter;
    }

    @Provides
    ExplorerFavoriteMvpPresenter<ExplorerFavoriteMvpView> provideExplorerFavoritePresenter (ExplorerFavoritePresenter<ExplorerFavoriteMvpView> presenter) {
        return presenter;
    }

    @Provides
    SDExplorerFavoriteMvpPresenter<SDExplorerFavoriteMvpView> provideSDExplorerFavoritePresenter (SDExplorerFavoritePresenter<SDExplorerFavoriteMvpView> presenter) {
        return presenter;
    }

    @Provides
    ExplorerRecentlyMvpPresenter<ExplorerRecentlyMvpView> provideExplorerRecentlyPresenter (ExplorerRecentlyPresenter<ExplorerRecentlyMvpView> presenter) {
        return presenter;
    }

    @Provides
    SDExplorerRecentlyMvpPresenter<SDExplorerRecentlyMvpView> provideSDExplorerRecentlyPresenter (SDExplorerRecentlyPresenter<SDExplorerRecentlyMvpView> presenter) {
        return presenter;
    }

    @Provides
    ExplorerSearchMvpPresenter<ExplorerSearchMvpView> provideExplorerSearchPresenter(ExplorerSearchPresenter<ExplorerSearchMvpView> presenter) {
        return presenter;
    }

    @Provides
    SDExplorerSearchMvpPresenter<SDExplorerSearchMvpView> provideSDExplorerSearchPresenter(SDExplorerSearchPresenter<SDExplorerSearchMvpView> presenter) {
        return presenter;
    }

    @Provides
    InfoMvpPresenter<InfoMvpView> provideInfoPresenter (InfoPresenter<InfoMvpView> presenter) {
        return presenter;
    }

    @Provides
    SDInfoMvpPresenter<SDInfoMvpView> provideSDInfoPresenter (SDInfoPresenter<SDInfoMvpView> presenter) {
        return presenter;
    }

    @Provides
    BasePlayMvpPresenter<BasePlayMvpView> provideBasePlayPresenter (BasePlayPresenter<BasePlayMvpView> presenter) {
        return presenter;
    }


}