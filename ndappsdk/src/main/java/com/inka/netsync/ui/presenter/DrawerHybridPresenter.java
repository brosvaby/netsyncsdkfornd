package com.inka.netsync.ui.presenter;

import com.inka.netsync.data.DataManager;
import com.inka.netsync.ui.mvppresenter.DrawerHybridMvpPresenter;
import com.inka.netsync.ui.mvpview.DrawerHybridMvpView;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by birdgang on 2017. 4. 18..
 */
public class DrawerHybridPresenter<V extends DrawerHybridMvpView> extends BasePresenter<V> implements DrawerHybridMvpPresenter<V> {

    @Inject
    public DrawerHybridPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public void onViewInitialized() {
    }

}