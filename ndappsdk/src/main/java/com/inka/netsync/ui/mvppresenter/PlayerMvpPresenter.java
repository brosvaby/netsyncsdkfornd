package com.inka.netsync.ui.mvppresenter;

import com.inka.netsync.di.PerActivity;
import com.inka.netsync.ui.mvpview.PlayerMvpView;

/**
 * Created by birdgang on 2017. 4. 14..
 */

@PerActivity
public interface PlayerMvpPresenter<V extends PlayerMvpView> extends MvpPresenter<V> {
    void onViewInitialized();
}