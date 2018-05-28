package com.inka.netsync.sd.ui.mvppresenter;

import com.inka.netsync.sd.ui.mvpview.SDInfoMvpView;
import com.inka.netsync.ui.mvppresenter.MvpPresenter;

/**
 * Created by birdgang on 2017. 4. 23..
 */
public interface SDInfoMvpPresenter<V extends SDInfoMvpView> extends MvpPresenter<V> {
    void onViewInitialized();
}