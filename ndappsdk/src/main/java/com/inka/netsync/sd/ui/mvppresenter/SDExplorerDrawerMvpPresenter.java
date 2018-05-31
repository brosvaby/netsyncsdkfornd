package com.inka.netsync.sd.ui.mvppresenter;

import com.inka.netsync.sd.ui.mvpview.SDExplorerDrawerMvpView;
import com.inka.netsync.ui.mvppresenter.MvpPresenter;

/**
 * Created by birdgang on 2017. 4. 18..
 */
public interface SDExplorerDrawerMvpPresenter<V extends SDExplorerDrawerMvpView> extends MvpPresenter<V> {
    void onViewInitialized();
    void onUpdatePath();
}