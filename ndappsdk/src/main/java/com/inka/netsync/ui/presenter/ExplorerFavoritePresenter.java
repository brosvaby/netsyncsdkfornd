package com.inka.netsync.ui.presenter;

import com.inka.ncg2.Ncg2Exception;
import com.inka.netsync.data.DataManager;
import com.inka.netsync.ncg.model.LicenseEntry;
import com.inka.netsync.ui.mvppresenter.ExplorerFavoriteMvpPresenter;
import com.inka.netsync.ui.mvpview.ExplorerFavoriteMvpView;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by birdgang on 2017. 4. 18..
 */

public class ExplorerFavoritePresenter<V extends ExplorerFavoriteMvpView> extends ExplorerPresenter<V> implements ExplorerFavoriteMvpPresenter<V> {

    @Inject
    public ExplorerFavoritePresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public void requesetClientAcquireLicense(LicenseEntry licenseEntry) throws Ncg2Exception {

    }
}
