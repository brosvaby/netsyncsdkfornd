package com.inka.netsync.ui.mvpview;

import com.inka.netsync.data.network.model.ResponseSerialAuthEntry;
import com.inka.netsync.ncg.model.LicenseEntry;
import com.inka.netsync.ncg.model.PlayerEntry;

import java.io.File;

/**
 * Created by birdgang on 2017. 4. 18..
 */
public interface ExplorerMvpView extends MvpView {
    void onLoadInputSerialDialog(final File file);
    void onLoadToastMessage (String message);
    void onLoadMessageDialog (String message);
//    void onRequesetSerialAuth (SerialAuthEntry requestSerialAuthEntry);
    void onResponseSerialAuth (ResponseSerialAuthEntry responseSerialAuthEntry);
    void clientAcquireLicense (String filePath, String serialNumber);
    void onResultClientAcquireLicense (LicenseEntry licenseEntry);


    /**
     * 플레이어 준비가 완료된후 뷰 에 알림.
     * @param playerEntry
     */
    void onLoadPlaybackActivity (PlayerEntry playerEntry);
}