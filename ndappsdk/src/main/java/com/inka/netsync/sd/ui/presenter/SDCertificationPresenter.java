package com.inka.netsync.sd.ui.presenter;

import android.content.Context;

import com.inka.netsync.data.DataManager;
import com.inka.netsync.sd.ui.mvppresenter.SDCertificationMvpPresenter;
import com.inka.netsync.sd.ui.mvpview.SDCertificationMvpView;
import com.inka.netsync.ui.presenter.BasePresenter;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by birdgang on 2017. 5. 29..
 */

public class SDCertificationPresenter<V extends SDCertificationMvpView> extends BasePresenter<V> implements SDCertificationMvpPresenter<V> {

    @Inject
    public SDCertificationPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public void onViewInitialized() {
    }

    @Override
    public boolean checkEnableDeviceModel(Context context, List<String> enableDeviceModels) {
        boolean result = false;
        return result;
    }

}