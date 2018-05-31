package com.inka.netsync.data.cache.db.model;

import com.inka.netsync.common.utils.MediaUtil;
import com.inka.netsync.data.CacheConfiguration;
import com.inka.netsync.data.cache.ContentCache;
import com.inka.netsync.logs.LogUtil;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

/**
 * Created by birdgang on 2018. 1. 12..
 */

public class ContentCategoryCacheEntry extends ContentCacheEntry {

    private final String TAG = "ContentCategoryEntry";

    public static final String TOKENIZER = "#%&@";

    private ContentCache contentCache = null;

    private String id;
    private String directory;
    private int mediaCount;

    private int storageType;
    private String categoryType;

    public ContentCategoryCacheEntry() {
        setMediaType(ContentType.GROUP.getType());
        contentCache = new ContentCache(CacheConfiguration.CAHCE_MEDIA_SIZE);
        contentCache.clear();
    }

    public void clear () {
        if (null != contentCache) {
            contentCache.clear();
        }
    }

    public ArrayList<ContentCacheEntry> convertToList () {
        ArrayList<ContentCacheEntry> contentCacheEntries = contentCache.convertToList();
        return contentCacheEntries;
    }

    public ContentCacheEntry findContentByPath (String directory, String path) {
        String key = MediaUtil.generateTokenKey(directory, path, TOKENIZER);
        return contentCache.get(key);
    }


    public void updateContentEntry (String directory, ContentCacheEntry contentEntry) {
        put(directory, contentEntry);
    }

    private void put(String directory, ContentCacheEntry contentEntry) {
        try {
            String location = contentEntry.getContentFilePath();
            String key = MediaUtil.generateTokenKey(directory, location, TOKENIZER);
            LogUtil.INSTANCE.info("birdgangcache", "put > location : " + location + " , key : " + key + " , directory : " + directory);

            if (StringUtils.isBlank(location) || StringUtils.isBlank(key)) {
                return;
            }

            contentCache.put(key, contentEntry);
        } catch (Exception e) {
            LogUtil.INSTANCE.error(getClass().getSimpleName(), e);
        }
    }


    public boolean removeContentByPlayUrl (String directory, String playUrl) {
        boolean result = false;
        try {
            String key = MediaUtil.generateTokenKey(directory, playUrl, TOKENIZER);
            result = contentCache.remove(key);
        } catch (Exception e){
            LogUtil.INSTANCE.error(getClass().getSimpleName(), e);
        }
        return result;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getDirectory() {
        return directory;
    }

    @Override
    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public int getContentCount() {
        return mediaCount;
    }

    public void setContentCount(int mediaCount) {
        this.mediaCount = mediaCount;
    }

    public String getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(String categoryType) {
        this.categoryType = categoryType;
    }

    public int getStorageType() {
        return storageType;
    }

    public void setStorageType(int storageType) {
        this.storageType = storageType;
    }


}