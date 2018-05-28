package com.inka.netsync.ui.mvpview;

import com.inka.netsync.model.ContentEntry;
import com.inka.netsync.model.ExplorerStackEntry;
import com.inka.netsync.view.model.ContentViewEntry;

import java.util.List;

/**
 * Created by birdgang on 2018. 4. 13..
 */
public interface ExplorerStackMvpView extends ExplorerMvpView {
    void onLoadExplorerStackEntries(List<ExplorerStackEntry> explorerStackEntries);
    void onUpdateContent (List<ContentEntry> contentEntries);
    void onUpdateContentForLMS (List<ContentViewEntry> contentViewEntries);
}