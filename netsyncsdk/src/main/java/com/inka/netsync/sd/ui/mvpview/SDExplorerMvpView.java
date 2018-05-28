package com.inka.netsync.sd.ui.mvpview;

import com.inka.netsync.model.AddLicenseEntry;
import com.inka.netsync.ncg.model.PlayerEntry;
import com.inka.netsync.ui.mvpview.MvpView;

import java.io.File;

/**
 * Created by birdgang on 2017. 4. 18..
 */
public interface SDExplorerMvpView extends MvpView {
    void onLoadOfflineAuthenticationDialog (final int contentId, final File file);
    void onLoadToastMessage (String message);
    void onRequestLicense (int contentId, File file);
    void onPrepareExecutePlay(AddLicenseEntry addLicenseEntry);
    void onResponseCheckLicenseValid (int contentId, File file);


    /**
     * 플레이어 준비가 완료된후 뷰 에 알림.
     * @param playerEntry
     */
    void onLoadPlaybackActivity (PlayerEntry playerEntry);
}