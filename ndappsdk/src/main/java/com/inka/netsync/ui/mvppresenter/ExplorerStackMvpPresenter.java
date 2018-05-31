package com.inka.netsync.ui.mvppresenter;

import com.inka.netsync.data.cache.db.model.ContentCacheEntry;
import com.inka.netsync.model.ExplorerStackEntryList;
import com.inka.netsync.ui.mvpview.ExplorerStackMvpView;
import com.inka.netsync.view.model.ContentViewEntry;

import java.util.List;
import java.util.Map;

/**
 * Created by birdgang on 2017. 4. 18..
 */
public interface ExplorerStackMvpPresenter<V extends ExplorerStackMvpView> extends ExplorerMvpPresenter<V> {
    void generateExplorerStack(Map<String, List<ContentCacheEntry>> contentCacheMemorAwareHashMap, Map<String, ContentCacheEntry> maps, String rootType);      // explorer stack 을 구성.
    void updateListContent(ExplorerStackEntryList explorerStackEntryList, String lastSelectedContentPath);
    void updateListContentForLMS(List<ContentViewEntry> contentEntries);
}