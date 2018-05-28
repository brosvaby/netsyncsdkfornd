package com.inka.netsync.ui.mvppresenter;

import com.inka.netsync.ui.mvpview.SearchContainerMvpView;

public interface SearchContainerMvpPresenter<V extends SearchContainerMvpView> extends MvpPresenter<V> {
    void onViewInitialized();
}