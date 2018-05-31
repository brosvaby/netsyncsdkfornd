package com.inka.netsync.ui.presenter;

import com.inka.netsync.data.DataManager;
import com.inka.netsync.ui.mvppresenter.BasePlayMvpPresenter;
import com.inka.netsync.ui.mvpview.BasePlayMvpView;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by birdgang on 2017. 4. 19..
 */

public class BasePlayPresenter<V extends BasePlayMvpView> extends BasePresenter<V> implements BasePlayMvpPresenter<V> {

    @Inject
    public BasePlayPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public void onViewInitialized() {

    }
    
}