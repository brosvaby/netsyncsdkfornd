package com.inka.netsync.ui.mvppresenter;

import com.inka.netsync.ui.mvpview.DefaultMvpView;

public interface DefaultMvpPresenter<V extends DefaultMvpView> extends MvpPresenter<V> {
    void onViewInitialized();
}