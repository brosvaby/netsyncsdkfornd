package com.inka.netsync.ui.presenter;

import com.inka.netsync.data.DataManager;
import com.inka.netsync.ui.mvppresenter.ExplorerSearchMvpPresenter;
import com.inka.netsync.ui.mvpview.ExplorerSearchMvpView;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public class ExplorerSearchPresenter<V extends ExplorerSearchMvpView> extends ExplorerPresenter<V> implements ExplorerSearchMvpPresenter<V> {
    
    @Inject
    public ExplorerSearchPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

}