package com.inka.netsync.ui.mvppresenter;

import com.inka.netsync.di.PerActivity;
import com.inka.netsync.ui.mvpview.GuidePagerMvpView;

@PerActivity
public interface GuidePagerMvpPresenter<V extends GuidePagerMvpView> extends MvpPresenter<V> {
    void onViewInitialized();
}