package com.inka.netsync.sd.ui.presenter;

import com.inka.netsync.data.DataManager;
import com.inka.netsync.sd.ui.mvppresenter.SDInfoMvpPresenter;
import com.inka.netsync.sd.ui.mvpview.SDInfoMvpView;
import com.inka.netsync.ui.presenter.BasePresenter;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by birdgang on 2017. 4. 23..
 */
public class SDInfoPresenter<V extends SDInfoMvpView> extends BasePresenter<V> implements SDInfoMvpPresenter<V> {

    @Inject
    public SDInfoPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public void onViewInitialized() {
    }

}