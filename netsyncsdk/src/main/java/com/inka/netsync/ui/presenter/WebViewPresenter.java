package com.inka.netsync.ui.presenter;


import com.inka.netsync.data.DataManager;
import com.inka.netsync.ui.mvppresenter.WebViewMvpPresenter;
import com.inka.netsync.ui.mvpview.WebViewMvpView;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by birdgang on 2017. 4. 22..
 */
public class WebViewPresenter<V extends WebViewMvpView> extends BasePresenter<V> implements WebViewMvpPresenter<V> {

    @Inject
    public WebViewPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public void onViewInitialized() {

    }

}