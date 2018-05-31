package com.inka.netsync.data.cache.db.model;

import com.inka.netsync.data.CacheConfiguration;
import com.inka.netsync.data.cache.ContentCategoryCache;
import com.inka.netsync.logs.LogUtil;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by birdgang on 2018. 1. 15..
 */

public class ListContentCategoryCacheEntry implements BaseCacheEntry {

    private final String TAG = "ListContentCategoryEntry";

    private ContentCategoryCache contentCategoryCache = null;

    public ListContentCategoryCacheEntry() {
        contentCategoryCache = new ContentCategoryCache(CacheConfiguration.CAHCE_CATEGORY_SIZE);
        contentCategoryCache.clear();
    }

    public void clear () {
        try {
            List<String> keys = new ArrayList<String>(contentCategoryCache.keys());

            for (String key : keys) {
                ContentCategoryCacheEntry contentCategoryEntry = findMediaCategoryByDirectory(key);
                if (null != contentCategoryEntry) {
                    contentCategoryEntry.clear();
                }
            }

            if (null != contentCategoryCache) {
                contentCategoryCache.clear();
            }
        } catch (Exception e) {
            LogUtil.INSTANCE.error(getClass().getSimpleName(), e);
        }
    }


    public void generateContent (ContentCacheEntry originalMedia) throws Exception {
        String directory = originalMedia.getDirectory();
        int type = originalMedia.getStorageType();
        ContentCategoryCacheEntry contentCategoryCacheEntry = generateMediaCategory(directory);
        contentCategoryCacheEntry.setDirectory(directory);
        LogUtil.INSTANCE.info("birdgangcontentstoragetype" , "generateContent > type : " + type + " , directory : " + directory);
        contentCategoryCacheEntry.setStorageType(type);
        contentCategoryCacheEntry.updateContentEntry(directory, originalMedia);
        put(directory, contentCategoryCacheEntry);
    }

    public ArrayList<ContentCategoryCacheEntry> convertToList () {
        ArrayList<ContentCategoryCacheEntry> contentCategoryCacheEntries = contentCategoryCache.convertToList();
        LogUtil.INSTANCE.info("birdgangcache" , "contentCategoryCacheEntries size : " + contentCategoryCacheEntries.size());
        return contentCategoryCacheEntries;
    }

    public ContentCategoryCacheEntry getContentCategoryCacheEntry (String directory) {
        return contentCategoryCache.get(directory);
    }


    public ContentCategoryCacheEntry generateMediaCategory (String directory) throws Exception {
        if (StringUtils.isBlank(directory)) {
            return null;
        }

        ContentCategoryCacheEntry contentCategoryEntry = contentCategoryCache.get(directory);
        if (null == contentCategoryEntry) {
            contentCategoryEntry = new ContentCategoryCacheEntry();
            contentCategoryEntry.setDirectory(directory);
        }
        return contentCategoryEntry;
    }


    public ContentCategoryCacheEntry findMediaCategoryByDirectory (String directory) throws Exception {
        if (StringUtils.isBlank(directory)) {
            return null;
        }
        ContentCategoryCacheEntry contentCategoryEntry = contentCategoryCache.get(directory);
        return contentCategoryEntry;
    }

    /***
     * 카테고리의 컨텐츠를 업데이트 함.
     * @param contentEntry
     * @throws Exception
     */
    public void updateContentCategoryByContent (ContentCacheEntry contentEntry) throws Exception {
        String directory = contentEntry.getDirectory();
        ContentCategoryCacheEntry contentCategoryEntry = findMediaCategoryByDirectory(directory);
        if (null != contentCategoryEntry) {
            contentCategoryEntry.updateContentEntry(directory, contentEntry);
            updateCategoryByContent(contentCategoryEntry);
        }
    }

    public void updateCategoryByContent (ContentCategoryCacheEntry contentCategoryEntry) {
        if (null == contentCategoryEntry) {
            return;
        }

        put(contentCategoryEntry.getDirectory(), contentCategoryEntry);
    }

    protected void put(String directory, ContentCategoryCacheEntry contentCategoryEntry) {
        try {
            contentCategoryCache.put(directory, contentCategoryEntry);
        } catch (Exception e) {
            LogUtil.INSTANCE.error(getClass().getSimpleName(), e);
        }
    }

    public boolean removeContent (ContentCacheEntry originalMedia) throws Exception {
        String directory = originalMedia.getDirectory();
        String path = originalMedia.getContentFilePath();
        ContentCategoryCacheEntry contentCategoryEntry = findMediaCategoryByDirectory(directory);
        boolean isRemoved = contentCategoryEntry.removeContentByPlayUrl(directory, path);
        put(directory, contentCategoryEntry);
        return isRemoved;
    }


}
