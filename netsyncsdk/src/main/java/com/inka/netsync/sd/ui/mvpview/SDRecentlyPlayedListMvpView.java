package com.inka.netsync.sd.ui.mvpview;

import com.inka.netsync.ui.mvpview.MvpView;

import java.io.File;

/**
 * Created by birdgang on 2017. 4. 18..
 */
public interface SDRecentlyPlayedListMvpView extends MvpView {
    void onLoadOfflineAuthenticationDialog (final int contentId, final File file);
    void onLoadToastMessage (String message);
    void onRequestLicense (int contentId, File file);
    void onResponseCheckLicenseValid (int contentId, File file);
}