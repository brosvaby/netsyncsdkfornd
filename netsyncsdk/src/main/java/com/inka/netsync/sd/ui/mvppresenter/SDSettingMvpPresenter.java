package com.inka.netsync.sd.ui.mvppresenter;

import com.inka.netsync.data.network.model.MarketVersionCheckEntry;
import com.inka.netsync.sd.ui.mvpview.SDSettingMvpView;
import com.inka.netsync.ui.mvppresenter.MvpPresenter;

/**
 * Created by birdgang on 2017. 4. 14..
 */

public interface SDSettingMvpPresenter<V extends SDSettingMvpView> extends MvpPresenter<V> {
    void onViewInitialized();
    void changePlayerType ();
    void changeDecoderType();
    String checkPlayerType ();
    String checkDecoderType ();

    String getAppVersion() throws Exception;
    String getAppPackage() throws Exception;

    void checkMarketVersion (MarketVersionCheckEntry requestMarketVersionCheck);
}