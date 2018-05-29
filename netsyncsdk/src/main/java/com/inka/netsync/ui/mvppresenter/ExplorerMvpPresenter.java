package com.inka.netsync.ui.mvppresenter;

import android.content.Context;

import com.inka.ncg2.Ncg2Exception;
import com.inka.netsync.model.ContentEntry;
import com.inka.netsync.ncg.model.LicenseEntry;
import com.inka.netsync.ncg.model.PlayerEntry;
import com.inka.netsync.ui.mvpview.ExplorerMvpView;
import com.inka.netsync.view.model.ContentViewEntry;

import java.util.List;

/**
 * Created by birdgang on 2017. 4. 18..
 */

public interface ExplorerMvpPresenter<V extends ExplorerMvpView> extends MvpPresenter<V> {
    void onViewInitialized ();
    void sortListContent (List<ContentViewEntry> contentViewEntries);

    void checkLicenseValid (Context context, ContentEntry contentEntry) throws Ncg2Exception;
    void checkSerialNumberValid (Context context, String filePath, String serialNumber) throws Exception;
    void requesetClientAcquireLicense (LicenseEntry licenseEntry) throws Ncg2Exception;

    void requestApiSerialAuthRx (String filePath, String serialNumber) throws Exception;      // RxAndroidNetworking


    /***
     *
     * @param context
     * @param playerEntry
     */
    void requestPrepareLicenseAndExecutePlayer(Context context, PlayerEntry playerEntry);

}