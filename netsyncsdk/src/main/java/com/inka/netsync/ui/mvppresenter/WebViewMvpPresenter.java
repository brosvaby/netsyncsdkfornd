package com.inka.netsync.ui.mvppresenter;

import com.inka.netsync.ui.mvpview.WebViewMvpView;

/**
 * Created by birdgang on 2017. 4. 22..
 */

public interface WebViewMvpPresenter<V extends WebViewMvpView> extends MvpPresenter<V> {
    void onViewInitialized();
}