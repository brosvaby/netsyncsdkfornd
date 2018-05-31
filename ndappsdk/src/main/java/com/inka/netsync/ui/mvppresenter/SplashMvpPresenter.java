package com.inka.netsync.ui.mvppresenter;

import com.inka.netsync.ui.mvpview.SplashMvpView;

/**
 * Created by birdgang on 2017. 4. 17..
 */
public interface SplashMvpPresenter<V extends SplashMvpView> extends MvpPresenter<V> {
    void onViewInitialized();
}