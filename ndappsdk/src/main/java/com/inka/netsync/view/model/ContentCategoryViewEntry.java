package com.inka.netsync.view.model;

import java.util.Comparator;

/**
 * Created by birdgang on 2018. 1. 18..
 */
public class ContentCategoryViewEntry extends ContentViewEntry {

    private final String TAG = "ContentCategoryViewEntry";

    public static final String TOKENIZER = "#%&@";


    private String id;
    private String directory;
    private int mediaCount;

    private int storageType;
    private String categoryType;

    public ContentCategoryViewEntry() {
    }

    public void clear () {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDirectory() {
        return directory;
    }

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


    public static class SortContentItemCountAscCompare implements Comparator<ContentCategoryViewEntry> {
        @Override
        public int compare(ContentCategoryViewEntry media1, ContentCategoryViewEntry media2) {
            int lhsContentCount = media1.getContentCount();
            int rhsContentCount = media2.getContentCount();
            return lhsContentCount - rhsContentCount;
        }
    }

    // 아이템 갯수 역순
    public static class SortContentItemCountDescCompare implements Comparator<ContentCategoryViewEntry> {
        @Override
        public int compare(ContentCategoryViewEntry media1, ContentCategoryViewEntry media2) {
            int lhsContentCount = media1.getContentCount();
            int rhsContentCount = media2.getContentCount();
            return rhsContentCount - lhsContentCount;
        }
    }

//    class SortContentCompare implements Comparator<ContentViewEntry> {
//        @Override
//        public int compare(ContentEntry media1, ContentEntry media2) {
//            return media1.getContentName().compareTo(media2.getContentName());
//        }
//    }


}
