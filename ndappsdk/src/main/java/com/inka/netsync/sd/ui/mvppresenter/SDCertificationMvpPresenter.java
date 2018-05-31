package com.inka.netsync.sd.ui.mvppresenter;

import android.content.Context;

import com.inka.netsync.sd.ui.mvpview.SDCertificationMvpView;
import com.inka.netsync.ui.mvppresenter.MvpPresenter;

import java.util.List;

/**
 * Created by birdgang on 2017. 5. 29..
 */
public interface SDCertificationMvpPresenter<V extends SDCertificationMvpView> extends MvpPresenter<V> {
    void onViewInitialized();
    boolean checkEnableDeviceModel(Context context, List<String> enableDeviceModels);
}