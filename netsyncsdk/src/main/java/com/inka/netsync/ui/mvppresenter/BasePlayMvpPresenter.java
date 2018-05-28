package com.inka.netsync.ui.mvppresenter;

import com.inka.netsync.ui.mvpview.BasePlayMvpView;

/**
 * Created by birdgang on 2017. 4. 19..
 */
public interface BasePlayMvpPresenter<V extends BasePlayMvpView> extends MvpPresenter<V> {
    void onViewInitialized();
}