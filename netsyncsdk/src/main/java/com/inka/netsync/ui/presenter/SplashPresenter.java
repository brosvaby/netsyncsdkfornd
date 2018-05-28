package com.inka.netsync.ui.presenter;

import com.inka.netsync.data.DataManager;
import com.inka.netsync.ui.mvppresenter.SplashMvpPresenter;
import com.inka.netsync.ui.mvpview.SplashMvpView;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by birdgang on 2017. 4. 17..
 */
public class SplashPresenter<V extends SplashMvpView> extends BasePresenter<V> implements SplashMvpPresenter<V> {

    @Inject
    public SplashPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public void onViewInitialized() {
    }

}
