package com.inka.netsync.ui.mvppresenter;

import com.inka.netsync.ui.mvpview.DrawerMvpView;

/**
 * Created by birdgang on 2017. 4. 18..
 */

public interface DrawerMvpPresenter<V extends DrawerMvpView> extends MvpPresenter<V> {
    void onViewInitialized();
    void onUpdatePath ();
}