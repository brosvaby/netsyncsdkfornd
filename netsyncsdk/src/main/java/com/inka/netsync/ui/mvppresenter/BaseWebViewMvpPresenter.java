package com.inka.netsync.ui.mvppresenter;

import com.inka.netsync.ui.mvpview.BaseWebViewMvpView;

/**
 * Created by birdgang on 2017. 4. 23..
 */

public interface BaseWebViewMvpPresenter<V extends BaseWebViewMvpView> extends MvpPresenter<V> {
    void onViewInitialized();
}