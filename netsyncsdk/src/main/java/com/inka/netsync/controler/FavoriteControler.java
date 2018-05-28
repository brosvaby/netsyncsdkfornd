package com.inka.netsync.controler;

import com.inka.netsync.data.cache.db.dao.ContentDao;
import com.inka.netsync.data.cache.db.dao.FavoriteDao;
import com.inka.netsync.data.cache.db.model.ContentCacheEntry;
import com.inka.netsync.data.cache.db.model.FavoriteCacheEntry;
import com.inka.netsync.logs.LogUtil;
import com.inka.netsync.model.ContentEntry;
import com.inka.netsync.model.FavoriteEntry;
import com.inka.netsync.view.model.FavoriteViewEntry;

import java.util.ArrayList;
import java.util.List;

public class FavoriteControler {

    private final String TAG = "FavoriteControler";

    private static volatile FavoriteControler defaultInstance;

    public static FavoriteControler getDefault() {
        if (defaultInstance == null) {
            synchronized (FavoriteControler.class) {
                if (defaultInstance == null) {
                    defaultInstance = new FavoriteControler();
                }
            }
        }
        return defaultInstance;
    }

    private FavoriteControler() {
    }

    public ArrayList<FavoriteEntry> loadFavoriteList() {
        ArrayList<FavoriteEntry> favoriteEntries = new ArrayList<>();
        try {
            ArrayList<FavoriteCacheEntry> favoriteCacheEntries = FavoriteDao.getDefault().getIsFavoriteContentArrayList();
            for (FavoriteCacheEntry entry : favoriteCacheEntries) {
                FavoriteEntry favoriteEntry = new FavoriteEntry(entry);
                favoriteEntries.add(favoriteEntry);
            }
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
        return favoriteEntries;
    }


    public ArrayList<FavoriteViewEntry> loadFavoriteViewList() {
        ArrayList<FavoriteViewEntry> favoriteViewEntries = new ArrayList<>();
        try {
            ArrayList<FavoriteCacheEntry> favoriteCacheEntries = FavoriteDao.getDefault().getIsFavoriteContentArrayList();
            for (FavoriteCacheEntry entry : favoriteCacheEntries) {
                FavoriteEntry favoriteEntry = new FavoriteEntry(entry);
                favoriteViewEntries.add(favoriteEntry.convertViewEntry());
            }
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
        return favoriteViewEntries;
    }



    public ArrayList<ContentEntry> convertContentFromListFavoritedContent() {
        ArrayList<ContentEntry> contentEntries = new ArrayList<>();
        try {
            List<FavoriteEntry> favoriteEntries = loadFavoriteList();
            if (null != favoriteEntries) {
                for (FavoriteEntry favoriteEntry : favoriteEntries) {
                    String name= favoriteEntry.getContentName();
                    ContentCacheEntry contentCacheEntry = ContentDao.getDefault().getContentByName(name);
                    ContentEntry contentEntry = new ContentEntry(contentCacheEntry);
                    contentEntries.add(contentEntry);
                }
            }
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
        return contentEntries;
    }


    public ContentEntry getFavoriteContentByPath(String path) {
        try {
            LogUtil.INSTANCE.info(TAG, "path : " + path);
            FavoriteCacheEntry favoriteCacheEntry = FavoriteDao.getDefault().getFavoriteByPath(path);
            FavoriteEntry favoriteEntry = new FavoriteEntry(favoriteCacheEntry);
            if (null != favoriteEntry) {
                LogUtil.INSTANCE.info(TAG, "favoriteEntry : " + favoriteEntry.toString());
            } else {
                LogUtil.INSTANCE.info(TAG, "null == favoriteEntry");
            }
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
        return null;
    }



    public boolean hasFavoriteContentWithPath(String path) {
        boolean result = false;
        try {
            LogUtil.INSTANCE.info(TAG, "path : " + path);
            FavoriteCacheEntry favoriteCacheEntry = FavoriteDao.getDefault().getFavoriteByPath(path);
            if (null != favoriteCacheEntry) {
                result = true;
            }
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
        return result;
    }

    public ContentEntry getFavoriteContentByContentId(int contentId) {
        try {
            LogUtil.INSTANCE.info(TAG, "getFavoriteContentByContentId > contentId : " + contentId);
//            FavoriteCacheEntry favoriteCacheEntry = FavoriteDao.getDefault().get(path);
//            FavoriteEntry favoriteEntry = new FavoriteEntry(favoriteCacheEntry);
//            if (null != favoriteEntry) {
//                LogUtil.INSTANCE.info(TAG, "favoriteEntry : " + favoriteEntry.toString());
//            } else {
//                LogUtil.INSTANCE.info(TAG, "null == favoriteEntry");
//            }
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
        return null;
    }

    public boolean hasFavoroteByContentName(String contentName) {
        boolean result = false;
        try {
            result = FavoriteDao.getDefault().checkFavoriteByContentName(contentName);
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
        return result;
    }


    public boolean hasFavoroteByContentId(int contentId) {
        boolean result = false;
        try {
            result = FavoriteDao.getDefault().checkFavoriteByContentId(contentId);
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
        return result;
    }


    public long addFavorite (FavoriteEntry favoriteEntry) {
        long count = 0;
        try {
            count = FavoriteDao.getDefault().insertFavorite(favoriteEntry.convertCacheEntry());
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
        return count;
    }


    public long deleteFavoriteById (FavoriteEntry favoriteEntry) {
        long count = 0;
        try {
            count = FavoriteDao.getDefault().deleteFavoriteById(favoriteEntry.getFavoriteId());
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
        return count;
    }


    public long deleteFavoriteByContentId (int contentId) {
        long count = 0;
        try {
            count = FavoriteDao.getDefault().deleteFavoriteByContentId(contentId);
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
        return count;
    }


    public long deleteFavoriteByContentName (FavoriteEntry favoriteEntry) {
        long count = 0;
        try {
            count = FavoriteDao.getDefault().deleteFavoriteByContentName(favoriteEntry.getContentName());
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
        return count;
    }

}
