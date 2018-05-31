package com.inka.netsync.ui.presenter;

import com.inka.netsync.data.DataManager;
import com.inka.netsync.ui.mvppresenter.GuidePagerMvpPresenter;
import com.inka.netsync.ui.mvpview.GuidePagerMvpView;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by birdgang on 2017. 5. 26..
 */

public class GuidePagerPresenter<V extends GuidePagerMvpView> extends BasePresenter<V> implements GuidePagerMvpPresenter<V> {

    @Inject
    public GuidePagerPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public void onViewInitialized() {
    }

}
