package com.inka.netsync.model;

import com.inka.netsync.data.cache.ContentCache;
import com.inka.netsync.data.cache.db.model.ContentCacheEntry;
import com.inka.netsync.logs.LogUtil;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by birdgang on 2017. 6. 21..
 */

public class ExplorerListEntry {

    private final String TAG = "ExplorerListEntry";

    private ContentCache contentCache = null;

    public ExplorerListEntry() {
        this.contentCache = new ContentCache(1000);
        this.contentCache.clear();
    }

    public void clear () {
        contentCache.clear();
    }

    public List<ContentCacheEntry> getContentList () {
        List<ContentCacheEntry> lists = new ArrayList<ContentCacheEntry>();
        try {
            List<ContentCacheEntry> contents = contentCache.convertToList();
//            Collections.sort(contents, new ContentCategoryEntry.SortContentCompare());
            lists.addAll(contents);
        } catch (Exception e) {
            LogUtil.INSTANCE.error(getClass().getSimpleName(), e);
        }
        return lists;
    }

    public void addContent (ContentCacheEntry contentEntry) {
        put(contentEntry);
    }

    private void put(ContentCacheEntry contentEntry) {
        try {
            String key = contentEntry.getContentName();
            String location = contentEntry.getContentFilePath();
            if (StringUtils.isBlank(location) || StringUtils.isBlank(key)) {
                return;
            }

            contentCache.put(key, contentEntry);
        } catch (Exception e) {
            LogUtil.INSTANCE.error(getClass().getSimpleName(), e);
        }
    }

    public ContentCacheEntry get(String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }

        return contentCache.get(key);
    }

}
