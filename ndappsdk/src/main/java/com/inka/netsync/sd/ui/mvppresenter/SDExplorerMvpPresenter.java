package com.inka.netsync.sd.ui.mvppresenter;

import android.content.Context;

import com.inka.ncg.nduniversal.hidden.Certification;
import com.inka.ncg2.Ncg2Exception;
import com.inka.netsync.model.ContentEntry;
import com.inka.netsync.ncg.model.PlayerEntry;
import com.inka.netsync.sd.ui.mvpview.SDExplorerMvpView;
import com.inka.netsync.ui.mvppresenter.MvpPresenter;
import com.inka.netsync.view.model.ContentViewEntry;

import java.io.File;
import java.util.List;

/**
 * Created by birdgang on 2017. 4. 18..
 */
public interface SDExplorerMvpPresenter<V extends SDExplorerMvpView> extends MvpPresenter<V> {
    void onViewInitialized ();
    void checkLicenseValid (Context context, ContentEntry contentEntry) throws Ncg2Exception;
    void checkSDLicense (Context context, int contentId, File file, Certification certification);
    void sortListContent (List<ContentViewEntry> contentViewEntries);


    void requestAddLicense (int contentId, File file) throws Ncg2Exception;

    /***
     *
     * @param context
     * @param playerEntry
     */
    void requestPrepareLicenseAndExecutePlayer(Context context, PlayerEntry playerEntry);


}