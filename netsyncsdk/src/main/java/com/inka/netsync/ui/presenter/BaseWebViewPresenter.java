package com.inka.netsync.ui.presenter;

import com.inka.netsync.data.DataManager;
import com.inka.netsync.ui.mvppresenter.BaseWebViewMvpPresenter;
import com.inka.netsync.ui.mvpview.BaseWebViewMvpView;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by birdgang on 2017. 4. 23..
 */
public class BaseWebViewPresenter<V extends BaseWebViewMvpView> extends BasePresenter<V> implements BaseWebViewMvpPresenter<V> {

    @Inject
    public BaseWebViewPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public void onViewInitialized() {
    }
    
}