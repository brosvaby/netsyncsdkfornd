package com.inka.netsync.ui.presenter;

import com.inka.netsync.data.DataManager;
import com.inka.netsync.ui.mvppresenter.DefaultMvpPresenter;
import com.inka.netsync.ui.mvpview.DefaultMvpView;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public class DefaultPresenter<V extends DefaultMvpView> extends BasePresenter<V> implements DefaultMvpPresenter<V> {

    @Inject
    public DefaultPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public void onViewInitialized() {
    }

}
