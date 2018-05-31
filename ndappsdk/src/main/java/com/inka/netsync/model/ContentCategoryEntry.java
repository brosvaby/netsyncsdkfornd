package com.inka.netsync.model;

import com.inka.netsync.data.cache.db.model.ContentCacheEntry;
import com.inka.netsync.data.cache.db.model.ContentCategoryCacheEntry;
import com.inka.netsync.logs.LogUtil;
import com.inka.netsync.view.model.ContentCategoryViewEntry;
import com.inka.netsync.view.model.ContentViewEntry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ContentCategoryEntry extends ContentEntry {

    private final String TAG = "ContentCategoryEntry";

    public static final String TOKENIZER = "#%&@";

    private String id;
    private String directory;
    private int mediaCount;

    private int storageType;
    private String categoryType;

    private ContentCategoryCacheEntry contentCategoryCacheEntry = new ContentCategoryCacheEntry();

    public ContentCategoryEntry () {
        setMediaType(ContentType.GROUP.getType());
        contentCategoryCacheEntry = new ContentCategoryCacheEntry();
    }

    public ContentCategoryEntry(ContentCategoryCacheEntry contentCategoryCacheEntry) {
        this();
        setContentId(contentCategoryCacheEntry.getContentId());
        setContentName(contentCategoryCacheEntry.getContentName());
        setContentFilePath(contentCategoryCacheEntry.getContentFilePath());
        setDirectory(contentCategoryCacheEntry.getDirectory());
        setIsFavoriteContent(contentCategoryCacheEntry.getIsFavoriteContent());

        try {
            List<ContentCacheEntry> contentCacheEntries = contentCategoryCacheEntry.convertToList();
            for (ContentCacheEntry entry : contentCacheEntries) {
                updateContentEntry(entry.getDirectory(), entry);
            }
        } catch (Exception e) {
            LogUtil.INSTANCE.error(getClass().getSimpleName(), e);
        }
    }


    public void clear () {
        if (null != contentCategoryCacheEntry) {
            contentCategoryCacheEntry.clear();
        }
    }

    public ArrayList<ContentEntry> convertToList () {
        ArrayList<ContentEntry> contentEntries = new ArrayList<>();
        try {
            ArrayList<ContentCacheEntry> contentCacheEntries = contentCategoryCacheEntry.convertToList();
            for (ContentCacheEntry entry : contentCacheEntries) {
                ContentEntry contentEntry = new ContentEntry(entry);
                contentEntries.add(contentEntry);
            }
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }

        return contentEntries;
    }

    public ContentEntry findContentByPath (String directory, String path) {
        ContentEntry contentEntry = null;
        try {
            contentEntry = new ContentEntry(contentCategoryCacheEntry.findContentByPath(directory, path));
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
        return contentEntry;
    }

    public void updateContentEntry (String directory, ContentCacheEntry contentEntry) {
        contentCategoryCacheEntry.updateContentEntry(directory, contentEntry);
    }

    public boolean removeContentByPlayUrl (String directory, String playUrl) {
        return contentCategoryCacheEntry.removeContentByPlayUrl(directory, playUrl);
    }


    public List<ContentEntry> getContentEntries() {
        List<ContentEntry> contentEntries = null;
        try {
            contentEntries = convertToList();
            Collections.sort(contentEntries, new ContentCategoryEntry.SortContentCompare());
        } catch (Exception e) {
            LogUtil.INSTANCE.error(getClass().getSimpleName(), e);
        }
        return contentEntries;
    }


    public List<ContentViewEntry> getContentViewEntries() {
        List<ContentViewEntry> contentViewEntries = new ArrayList<>();
        try {
            List<ContentEntry> contentEntries = convertToList();
            for (ContentEntry entry : contentEntries) {
                contentViewEntries.add(entry.convertViewEntry());
            }
            Collections.sort(contentEntries, new ContentCategoryEntry.SortContentCompare());
        } catch (Exception e) {
            LogUtil.INSTANCE.error(getClass().getSimpleName(), e);
        }
        return contentViewEntries;
    }


    public ContentCategoryViewEntry convertContentCategoryViewEntry () {
        ContentCategoryViewEntry contentCategoryViewEntry = new ContentCategoryViewEntry();
        contentCategoryViewEntry.setId(getId());
        contentCategoryViewEntry.setCategoryType(getCategoryType());
        contentCategoryViewEntry.setContentCount(getContentCount());
        contentCategoryViewEntry.setDirectory(getDirectory());
        contentCategoryViewEntry.setStorageType(getStorageType());
        return contentCategoryViewEntry;
    }


    public static class SortContentItemCountAscCompare implements Comparator<ContentCategoryEntry> {
        @Override
        public int compare(ContentCategoryEntry media1, ContentCategoryEntry media2) {
            int lhsContentCount = media1.getContentCount();
            int rhsContentCount = media2.getContentCount();
            return lhsContentCount - rhsContentCount;
        }
    }

    // 아이템 갯수 역순
    public static class SortContentItemCountDescCompare implements Comparator<ContentCategoryEntry> {
        @Override
        public int compare(ContentCategoryEntry media1, ContentCategoryEntry media2) {
            int lhsContentCount = media1.getContentCount();
            int rhsContentCount = media2.getContentCount();
            return rhsContentCount - lhsContentCount;
        }
    }

    class SortContentCompare implements Comparator<ContentEntry> {
        @Override
        public int compare(ContentEntry media1, ContentEntry media2) {
            return media1.getContentName().compareTo(media2.getContentName());
        }
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