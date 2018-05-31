package com.inka.netsync.ui.presenter;

import com.inka.netsync.data.DataManager;
import com.inka.netsync.ui.mvppresenter.PlayerMvpPresenter;
import com.inka.netsync.ui.mvpview.PlayerMvpView;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by birdgang on 2017. 4. 14..
 */

public class PlayerPresenter<V extends PlayerMvpView> extends BasePresenter<V> implements PlayerMvpPresenter<V> {

    private final String TAG = "PlayerPresenter";

    @Inject
    public PlayerPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public void onViewInitialized() {
    }
    
}
