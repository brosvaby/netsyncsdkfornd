package com.inka.netsync.ui.presenter;

import com.inka.netsync.data.DataManager;
import com.inka.netsync.ui.mvppresenter.ExplorerRecentlyMvpPresenter;
import com.inka.netsync.ui.mvpview.ExplorerRecentlyMvpView;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by birdgang on 2017. 4. 18..
 */

public class ExplorerRecentlyPresenter<V extends ExplorerRecentlyMvpView> extends ExplorerPresenter<V> implements ExplorerRecentlyMvpPresenter<V> {

    @Inject
    public ExplorerRecentlyPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

}