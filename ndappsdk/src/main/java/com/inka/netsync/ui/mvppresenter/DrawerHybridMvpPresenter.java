package com.inka.netsync.ui.mvppresenter;

import com.inka.netsync.ui.mvpview.DrawerHybridMvpView;

/**
 * Created by birdgang on 2017. 4. 18..
 */
public interface DrawerHybridMvpPresenter<V extends DrawerHybridMvpView> extends MvpPresenter<V> {
    void onViewInitialized();
}