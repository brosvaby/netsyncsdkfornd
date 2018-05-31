package com.inka.netsync.model;

import com.inka.netsync.data.cache.db.model.ContentCacheEntry;
import com.inka.netsync.data.cache.db.model.ContentCategoryCacheEntry;
import com.inka.netsync.data.cache.db.model.ListContentCategoryCacheEntry;
import com.inka.netsync.logs.LogUtil;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ListContentCategoryEntry implements BaseEntry {

    private final String TAG = "ListContentCategoryEntry";

    private List<ContentCategoryEntry> contentCategoryEntries = null;

    private ListContentCategoryCacheEntry listContentCategoryCacheEntry;

    public ListContentCategoryEntry () {
        contentCategoryEntries = new ArrayList<>();
        listContentCategoryCacheEntry = new ListContentCategoryCacheEntry();
    }


    public void generateContent (ContentEntry originalMedia) throws Exception {
        ContentCacheEntry contentCacheEntry = new ContentCacheEntry();
        contentCacheEntry.setContentId(originalMedia.getContentId());
        contentCacheEntry.setDirectory(originalMedia.getDirectory());
        contentCacheEntry.setContentFilePath(originalMedia.getContentFilePath());
        contentCacheEntry.setContentName(originalMedia.getContentName());
        contentCacheEntry.setContentId(contentCacheEntry.getContentId());
        contentCacheEntry.setContentDownloadDate(contentCacheEntry.getContentDownloadDate());
        contentCacheEntry.setContentLastPlayTime(contentCacheEntry.getContentLastPlayTime());
        contentCacheEntry.setIsFavoriteContent(contentCacheEntry.getIsFavoriteContent());
        LogUtil.INSTANCE.info("birdganglistcontentcategory", "generateContent > contentCacheEntry.toString() : " + contentCacheEntry.toString());
        listContentCategoryCacheEntry.generateContent(contentCacheEntry);
    }

    public List<ContentCategoryEntry> getMediaCategoryEntries() {
        ArrayList<ContentCategoryEntry> contentCategoryEntries = null;
        try {
            contentCategoryEntries = convertToList();
            Collections.sort(contentCategoryEntries, new SortMediaCategoryCompare());
            contentCategoryEntries.addAll(contentCategoryEntries);
        } catch (Exception e) {
            LogUtil.INSTANCE.error(getClass().getSimpleName(), e);
        }
        return contentCategoryEntries;
    }


    public ContentCategoryEntry findMediaCategoryByDirectory (String directory) throws Exception {
        if (StringUtils.isBlank(directory)) {
            return null;
        }

        ContentCategoryCacheEntry contentCategoryCacheEntry = getContentCategoryCacheEntry(directory);
        ContentCategoryEntry contentCategoryEntry = new ContentCategoryEntry(contentCategoryCacheEntry);
        return contentCategoryEntry;
    }



    public void clear () {
        try {
            if (null != listContentCategoryCacheEntry) {
                listContentCategoryCacheEntry.clear();
            }
        } catch (Exception e) {
            LogUtil.INSTANCE.error(getClass().getSimpleName(), e);
        }
    }


    public ArrayList<ContentCategoryEntry> convertToList () {
        ArrayList<ContentCategoryEntry> contentCategoryEntries = new ArrayList<>();
        ArrayList<ContentCategoryCacheEntry> contentCategoryCacheEntries = listContentCategoryCacheEntry.convertToList();
        for (ContentCategoryCacheEntry entry : contentCategoryCacheEntries) {
            ContentCategoryEntry contentCategoryEntry = new ContentCategoryEntry(entry);
            contentCategoryEntries.add(contentCategoryEntry);
        }
        LogUtil.INSTANCE.info("birdgangcache" , "contentCategoryEntries size : " + contentCategoryEntries.size());
        return contentCategoryEntries;
    }

    protected ContentCategoryCacheEntry getContentCategoryCacheEntry (String directory) {
        return listContentCategoryCacheEntry.getContentCategoryCacheEntry(directory);
    }

    /***
     * 카테고리의 컨텐츠를 업데이트 함.
     * @param contentCacheEntry
     * @throws Exception
     */
    public void updateContentCategoryByContent (ContentCacheEntry contentCacheEntry) throws Exception {
        listContentCategoryCacheEntry.updateContentCategoryByContent(contentCacheEntry);
    }

    public boolean removeContent (ContentCacheEntry originalMedia) throws Exception {
        return listContentCategoryCacheEntry.removeContent(originalMedia);
    }


    public class SortMediaCategoryCompare implements Comparator<ContentCategoryEntry> {
        @Override
        public int compare(ContentCategoryEntry mediaCategory1, ContentCategoryEntry mediaCategory2) {
            return mediaCategory1.getDirectory().compareTo(mediaCategory2.getDirectory());
        }
    }

}
