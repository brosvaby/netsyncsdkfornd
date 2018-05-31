package com.inka.netsync.ui.presenter;

import com.inka.netsync.data.DataManager;
import com.inka.netsync.ui.mvppresenter.SearchContainerMvpPresenter;
import com.inka.netsync.ui.mvpview.SearchContainerMvpView;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public class SearchContainerPresenter<V extends SearchContainerMvpView> extends BasePresenter<V> implements SearchContainerMvpPresenter<V> {

    @Inject
    public SearchContainerPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public void onViewInitialized() {
    }

}