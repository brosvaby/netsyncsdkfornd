package com.inka.netsync.controler;

import com.inka.netsync.data.cache.db.dao.PlayedListDao;
import com.inka.netsync.data.cache.db.model.RecentlyCacheEntry;
import com.inka.netsync.logs.LogUtil;
import com.inka.netsync.model.ContentCategoryEntry;
import com.inka.netsync.model.ContentEntry;
import com.inka.netsync.model.RecentlyEntry;
import com.inka.netsync.view.model.ContentCategoryViewEntry;
import com.inka.netsync.view.model.RecentlyViewEntry;

import java.util.ArrayList;
import java.util.List;

public class RecentlyPlayedListControler {

    private final String TAG = "RecentlyPlayedListControler";

    private static volatile RecentlyPlayedListControler defaultInstance;

    public static RecentlyPlayedListControler getDefault() {
        if (defaultInstance == null) {
            synchronized (RecentlyPlayedListControler.class) {
                if (defaultInstance == null) {
                    defaultInstance = new RecentlyPlayedListControler();
                }
            }
        }
        return defaultInstance;
    }


    private RecentlyPlayedListControler() {
    }


    public ArrayList<ContentEntry> convertContentFromPlayedListContent() {
        ArrayList<ContentEntry> contentEntries = new ArrayList<>();
        try {
            ArrayList<RecentlyCacheEntry> recentlyCacheEntries = PlayedListDao.getDefault().getRecentlyPlayedList();
            for (RecentlyCacheEntry entry : recentlyCacheEntries) {
                contentEntries.add(ContentControler.getDefault().findContentByName(entry.getContentName()));
            }
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
        return contentEntries;
    }


    public ArrayList<RecentlyEntry> getRecentlyPlayedList() {
        ArrayList<RecentlyEntry> playedListEntries = new ArrayList<>();
        try {
            ArrayList<RecentlyCacheEntry> recentlyCacheEntries = PlayedListDao.getDefault().getRecentlyPlayedList();
            for (RecentlyCacheEntry entry : recentlyCacheEntries) {
                RecentlyEntry recentlyEntry = new RecentlyEntry(entry);
                playedListEntries.add(recentlyEntry);
            }
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
        return playedListEntries;
    }


    public ArrayList<RecentlyViewEntry> loadRecentlyViewEntries() {
        ArrayList<RecentlyViewEntry> playedListEntries = new ArrayList<>();
        try {
            ArrayList<RecentlyCacheEntry> recentlyCacheEntries = PlayedListDao.getDefault().getRecentlyPlayedList();
            for (RecentlyCacheEntry entry : recentlyCacheEntries) {
                RecentlyEntry recentlyEntry = new RecentlyEntry(entry);
                playedListEntries.add(recentlyEntry.convertViewEntry());
            }
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
        return playedListEntries;
    }


    public List<ContentCategoryViewEntry> loadContentCategoryViewEntries () {
        List<ContentCategoryViewEntry> contentCategoryViewEntries = new ArrayList<>();
        List<ContentCategoryEntry> contentCategoryEntries = MediaControler.getDefault().loadContentCategoryList();
        for (ContentCategoryEntry entry : contentCategoryEntries) {
            contentCategoryViewEntries.add(entry.convertContentCategoryViewEntry());
        }
        return contentCategoryViewEntries;
    }

    public long addRecentlyPlayed (RecentlyEntry recentlyEntry) {
        long count = 0;
        try {
            count = PlayedListDao.getDefault().insertRecentlyPlay(recentlyEntry.convertCacheEntry());
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
        return count;
    }


}
