package com.inka.netsync.ui.mvppresenter;

import com.inka.netsync.di.PerActivity;
import com.inka.netsync.ui.mvpview.InfoMvpView;

@PerActivity
public interface InfoMvpPresenter<V extends InfoMvpView> extends MvpPresenter<V> {
    void onViewInitialized();
    void requestListInfo();
}