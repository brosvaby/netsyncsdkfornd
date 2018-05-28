package com.inka.netsync.model;

import com.inka.netsync.common.ExplorerConstants;
import com.inka.netsync.data.cache.db.model.ContentCacheEntry;
import com.inka.netsync.logs.LogUtil;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by birdgang on 2017. 6. 21..
 */

public class ExplorerStackEntryList {

    private final String TAG = "ExplorerStackEntryList";

    private ExplorerListEntry explorerListEntry = null;
    private List<ExplorerStackEntry> explorerStackEntries = null;

    public ExplorerStackEntryList () {
        explorerListEntry = new ExplorerListEntry();
        explorerStackEntries = new ArrayList<>();
    }

    public void initDepth () {
        ExplorerConstants.currentDepth = 1;
    }

    public int increageDepth (int size) {
        ExplorerConstants.currentDepth = ExplorerConstants.currentDepth + 1;
        return ExplorerConstants.currentDepth;
    }

    public int decreageDepth () {
        ExplorerConstants.currentDepth = ExplorerConstants.currentDepth - 1;
        if (ExplorerConstants.currentDepth < 1) {
            ExplorerConstants.currentDepth = 1;
        }
        return ExplorerConstants.currentDepth;
    }

    public int decreageDepthByPosition (int position) {
        ExplorerConstants.currentDepth = position;
        return ExplorerConstants.currentDepth;
    }

    public int moveForceDepth (int depth) {
        ExplorerConstants.currentDepth = depth;
        return ExplorerConstants.currentDepth;
    }

    public int currentDepth () {
        return ExplorerConstants.currentDepth;
    }

    public void clear () {
        explorerListEntry.clear();
    }

    public int getSize () {
        return (explorerListEntry.getContentList() == null) ? 0 : explorerListEntry.getContentList().size();
    }

    public void addContent (ContentCacheEntry contentEntry) {
        explorerListEntry.addContent(contentEntry);
    }

    public List<ContentCacheEntry> loadContent () {
        return explorerListEntry.getContentList();
    }

    public List<ExplorerStackEntry> getExplorerStackEntries() {
        return explorerStackEntries;
    }

    public void setExplorerStackEntries(List<ExplorerStackEntry> explorerStackEntries) {
        this.explorerStackEntries = explorerStackEntries;
    }

    public int getChildCount (String selectedContentPath) {
        ArrayList<ContentCacheEntry> contentEntries = new ArrayList<>();

        for (ExplorerStackEntry stack : explorerStackEntries) {
            int stackSize = stack.size();
            if (stackSize > 0) {
                int depth = stackSize - currentDepth();
                if (depth >= 0 && depth < stackSize) {
                    ContentCacheEntry contentEntry = stack.get(depth);
                    LogUtil.INSTANCE.debug("birdgangexplorernavigator", " parentsFilePath : " + contentEntry.getParentsFilePath());
                    LogUtil.INSTANCE.debug("birdgangexplorernavigator", " selectedContentPath : " + selectedContentPath);
                    if (StringUtils.isBlank(selectedContentPath) || StringUtils.isBlank(contentEntry.getParentsFilePath())) {
                        ContentCacheEntry contentStackEntry = stack.get(depth);
                        contentEntries.add(contentStackEntry);
                    } else {
                        if (StringUtils.equals(selectedContentPath, contentEntry.getParentsFilePath())) {
                            ContentCacheEntry contentStackEntry = stack.get(depth);
                            contentEntries.add(contentStackEntry);
                        }
                    }
                }
            }
        }

        return contentEntries.size();
    }


    public synchronized List<ContentCacheEntry> extractListContent (String selectedContentPath) {
        ExplorerConstants.lastSelectedContentPath = selectedContentPath;

        List<ContentCacheEntry> resultContentList = new ArrayList<>();

        explorerListEntry.clear();

        if (null == explorerStackEntries || explorerStackEntries.size() <= 0) {
            return resultContentList;
        }

        LogUtil.INSTANCE.debug("birdgangexplorernavigator", " extractListContent : " + explorerStackEntries.size());

        try {
            for (ExplorerStackEntry stack : explorerStackEntries) {
                int stackSize = stack.size();
                if (stackSize > 0) {
                    int depth = stackSize - currentDepth();
                    if (depth >= 0 && depth < stackSize) {
                        ContentCacheEntry contentEntry = stack.get(depth);
//                        LogUtil.INSTANCE.debug("birdgangexplorernavigator", " parentsFilePath : " + contentEntry.getParentsFilePath());
//                        LogUtil.INSTANCE.debug("birdgangexplorernavigator", " selectedContentPath : " + selectedContentPath);
//                        LogUtil.INSTANCE.debug("birdgangexplorernavigator", " stackSize : " + stackSize + " , currentDepth() : " + currentDepth());
//                        LogUtil.INSTANCE.debug("birdgangexplorernavigator", " ---------------------------------------------------------- ");

                        if (StringUtils.isBlank(selectedContentPath) || StringUtils.isBlank(contentEntry.getParentsFilePath())) {
                            ContentCacheEntry contentStackEntry = stack.get(depth);
                            explorerListEntry.addContent(contentStackEntry);
                        } else {
                            if (StringUtils.equals(selectedContentPath, contentEntry.getParentsFilePath())) {
                                ContentCacheEntry contentStackEntry = stack.get(depth);
                                explorerListEntry.addContent(contentStackEntry);
                            }
                        }
                    }
                }
            }

            resultContentList = explorerListEntry.getContentList();
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }

        return resultContentList;
    }


    public void updateContent (ContentCacheEntry contentEntry) {
        explorerListEntry.addContent(contentEntry);
    }


    public void updateContentForStackEntry (ContentCacheEntry contentCacheEntry) throws Exception {
        for (ExplorerStackEntry stack : explorerStackEntries) {
            int stackSize = stack.size();
            if (stackSize > 0) {
                int depth = stackSize - currentDepth();
                if (depth >= 0 && depth < stackSize) {
                    ContentCacheEntry existEntry = stack.get(depth);
                    if (StringUtils.equals(existEntry.getContentFilePath(), contentCacheEntry.getContentFilePath())) {
                        existEntry.copyContent(contentCacheEntry);
                        LogUtil.INSTANCE.debug(TAG,  "hit Entry : " + contentCacheEntry.toString());
                    }
                }
            }
        }
    }


}
