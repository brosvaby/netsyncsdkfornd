package com.inka.netsync.controler;

import android.content.Context;
import android.support.v4.util.ArrayMap;

import com.inka.netsync.BaseApplication;
import com.inka.netsync.R;
import com.inka.netsync.common.AppConstants;
import com.inka.netsync.data.cache.db.MetaData;
import com.inka.netsync.data.cache.db.dao.ContentDao;
import com.inka.netsync.data.cache.db.model.ContentCacheEntry;
import com.inka.netsync.logs.LogUtil;
import com.inka.netsync.model.ContentCategoryEntry;
import com.inka.netsync.model.ContentEntry;
import com.inka.netsync.model.StorageEntry;
import com.inka.netsync.ncg.Ncg2SdkHelper;
import com.inka.netsync.view.model.ContentViewEntry;
import com.inka.netsync.view.model.RecentlyViewEntry;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by birdgang on 2017. 5. 2..
 */

public class ContentControler {

    private final String TAG = "ContentControler";

    private Context context;

    private static volatile ContentControler defaultInstance;

    private List<ContentEntry> playlist = null;

    public static ContentControler getDefault() {
        if (defaultInstance == null) {
            synchronized (ContentControler.class) {
                if (defaultInstance == null) {
                    defaultInstance = new ContentControler();
                }
            }
        }
        return defaultInstance;
    }

    private ContentControler() {
        this.context = BaseApplication.getContext();
        this.playlist = new ArrayList<>();
    }

    public void clear () {
        if (null != playlist) {
            playlist.clear();
        }
    }

    public synchronized List<ContentCacheEntry> loadContentListMemoryAware () throws Exception {
        List<ContentCacheEntry> contentCacheMemorAwareEntries = new ArrayList<>();
        List<ContentEntry> contentMemorAwareEntries = new ArrayList<>();
        contentMemorAwareEntries.addAll(MediaControler.getDefault().getItemList());
        if (null == contentMemorAwareEntries || contentMemorAwareEntries.size() <= 0) {
            return null;
        }

        for (ContentEntry contentMemorAwareEntry : contentMemorAwareEntries) {
            LogUtil.INSTANCE.info("birdgangloadmemoryaware" , "loadContentListMemoryAware > contentMemorAwareEntry : " + contentMemorAwareEntry.toString());
            contentCacheMemorAwareEntries.add(contentMemorAwareEntry.convertCacheEntry());
        }
        return contentCacheMemorAwareEntries;
    }

    ///////////////////////////////////////// Content Category

    public List<ContentCategoryEntry> loadContentCategory () {
        List<ContentCategoryEntry> contentCategoryEntries = MediaControler.getDefault().loadContentCategoryList();
        return contentCategoryEntries;
    }


    //////////////////////////////////////////   Content

    public ArrayList<File> loadContentConvertToFile () {
        ArrayList<File> files = new ArrayList<>();
        try {
            ArrayList<ContentCacheEntry> contentCacheEntries = ContentDao.getDefault().loadContentList();
            for (ContentCacheEntry entry : contentCacheEntries) {
                files.add(new File(entry.getContentFilePath()));
            }
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
        return files;
    }

    public ArrayMap<String, ContentEntry> loadContentMaps() {
        ArrayMap<String, ContentEntry> contentMaps = new ArrayMap<>();
        try {
            ArrayMap<String, ContentCacheEntry> maps = ContentDao.getDefault().loadContentMaps();
            for (Map.Entry<String, ContentCacheEntry> entry : maps.entrySet()) {
                ContentEntry contentEntry = new ContentEntry(entry.getValue());
                contentMaps.put(entry.getKey(), contentEntry);
            }
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
        return contentMaps;
    }


    public ArrayMap<String, ContentEntry> loadContentMapsForSync() {
        ArrayMap<String, ContentEntry> contentMaps = new ArrayMap<>();
        try {
            ArrayMap<String, ContentCacheEntry> maps = ContentDao.getDefault().loadContentMaps();
            for (Map.Entry<String, ContentCacheEntry> entry : maps.entrySet()) {
                ContentEntry contentEntry = new ContentEntry(entry.getValue());
                contentMaps.put(entry.getKey(), contentEntry);
            }
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
        return contentMaps;
    }

    public ArrayMap<String, ContentCacheEntry> loadContentMapsByFilePath() {
        ArrayMap<String, ContentCacheEntry> contentMaps = new ArrayMap<>();
        try {
            ArrayMap<String, ContentCacheEntry> maps = ContentDao.getDefault().loadContentMapsKeyFilePath();
            for (Map.Entry<String, ContentCacheEntry> entry : maps.entrySet()) {
                contentMaps.put(entry.getKey(), entry.getValue());
            }
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
        return contentMaps;
    }


    public ArrayList<ContentEntry> loadContentList() {
        ArrayList<ContentEntry> contentEntries = new ArrayList<>();
        try {
            ArrayList<ContentCacheEntry> contentCacheEntries = ContentDao.getDefault().loadContentList();
            for (ContentCacheEntry entry : contentCacheEntries) {
                LogUtil.INSTANCE.info("birdgangControler" , "entry : " + entry.toString());
                ContentEntry contentEntry = new ContentEntry(entry);
                contentEntries.add(contentEntry);
            }
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
        return contentEntries;
    }


    public ArrayList<ContentCacheEntry> loadContentCacheList() {
        ArrayList<ContentCacheEntry> contentCacheEntries = new ArrayList<>();
        try {
            contentCacheEntries = ContentDao.getDefault().loadContentList();
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
        return contentCacheEntries;
    }

    public int getTotalCountOfContent () {
        int count = 0;
        try {
            count = MediaControler.getDefault().getItemList().size();
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
        return count;
    }


    public int getExternalTotalContentCount () {
        int count = 0;
        try {
            ArrayList<ContentEntry> contentEntries = MediaControler.getDefault().getItemList();
            for (ContentEntry contentEntry : contentEntries) {
                if (StringUtils.contains(contentEntry.getContentFilePath(), "/storage/emulated/legacy")
                        || StringUtils.contains(contentEntry.getContentFilePath(), "/storage/emulated/0")) {
                    count++;
                }
            }
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
        return count;
    }


    public int getSDCardTotalContentCount () {
        int count = 0;
        try {
            ArrayList<ContentEntry> contentEntries = MediaControler.getDefault().getItemList();
            for (ContentEntry contentEntry : contentEntries) {
                if (StringUtils.contains(contentEntry.getContentFilePath(), "/storage/emulated/legacy")
                        || StringUtils.contains(contentEntry.getContentFilePath(), "/storage/emulated/0")) {
                }
                else if (StringUtils.contains(contentEntry.getContentFilePath(), "/storage/sdcard1")) {
                    count++;
                }
                else {
                    count++;
                }
            }
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
        return count;
    }

    public ArrayList<ContentEntry> findListContentByCategoryId(int categotyId) {
        ArrayList<ContentEntry> contentEntries = null;
        try {
            ArrayList<ContentCacheEntry> contentCacheEntries = ContentDao.getDefault().getContentArrayListUsingCategoryId(categotyId);
            for (ContentCacheEntry entry : contentCacheEntries) {
                ContentEntry contentEntry = new ContentEntry(entry);
                contentEntries.add(contentEntry);
            }
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
        return contentEntries;
    }


    public ArrayList<ContentEntry> findListFavoritedContentByCategoryId(int categotyId) {
        ArrayList<ContentEntry> contentEntries = null;
        try {
            ArrayList<ContentCacheEntry> contentCacheEntries = ContentDao.getDefault().getIsFavoriteContentListUsingCategoryId(categotyId);
            for (ContentCacheEntry entry : contentCacheEntries) {
                ContentEntry contentEntry = new ContentEntry(entry);
                contentEntries.add(contentEntry);
            }
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
        return contentEntries;
    }


    /**
     * 검색 파일 리스트 가져오기
     * input 이 들어가 있는 모든 .sd.ncg 파일 리스트
     */
    public ArrayList<ContentEntry> findListContentSearchKeyword(String search) {
        ArrayList<ContentEntry> results = new ArrayList<>();

        if (StringUtils.isBlank(search)) {
            return results;
        }

        try {
            ArrayList<ContentEntry> contentEntries = loadContentList();
            LogUtil.INSTANCE.info("birdgangcontentsearch" , "findListContentSearchKeyword > contentEntries size : " + contentEntries.size());
            for (ContentEntry contentEntry : contentEntries) {
                LogUtil.INSTANCE.info("birdgangcontentsearch" , "findListContentSearchKeyword >  search keyword : " + search + " , name : " + contentEntry.getContentName() + " , path : " + contentEntry.getContentFilePath());
                if (StringUtils.endsWith(contentEntry.getContentFilePath(), ".sd.ncg")) {
                    String upperListFile = contentEntry.getContentName().toUpperCase();
                    String upperInput = search.toUpperCase();
                    if (upperListFile.contains(upperInput)) {
                        results.add(contentEntry);
                    }
                }
            }
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
        return results;
    }


    public ArrayList<ContentViewEntry> findListContentViewSearchKeyword (String search) {
        ArrayList<ContentViewEntry> results = new ArrayList<>();
        try {
            ArrayList<ContentEntry> contentEntries = loadContentList();
            for (ContentEntry contentEntry : contentEntries) {
                if (StringUtils.endsWith(contentEntry.getContentFilePath(), ".sd.ncg")) {
                    String upperListFile = contentEntry.getContentName().toUpperCase();
                    String upperInput = search.toUpperCase();
                    if (upperListFile.contains(upperInput)) {
                        results.add(contentEntry.convertViewEntry());
                    }
                }
            }
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
        return results;
    }


    public ArrayList<ContentEntry> findListFavoritedContent() {
        ArrayList<ContentEntry> contentEntries = new ArrayList<>();
        try {
            ArrayList<ContentCacheEntry> contentCacheEntries = ContentDao.getDefault().getIsFavoriteContentArrayList();
            for (ContentCacheEntry entry : contentCacheEntries) {
                ContentEntry contentEntry = new ContentEntry(entry);
                contentEntries.add(contentEntry);
            }
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
        return contentEntries;
    }


    public ContentEntry findContentById(int contentId) {
        ContentEntry contentEntry = null;
        try {
            ContentCacheEntry contentCacheEntry = ContentDao.getDefault().getContent(contentId);
            contentEntry = new ContentEntry(contentCacheEntry);
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
        return contentEntry;
    }

    public ContentEntry findContentByFilePath(String filePath) {
        ContentEntry contentEntry = null;
        try {
            ContentCacheEntry contentCacheEntry = ContentDao.getDefault().getContentByPath(filePath);
            contentEntry = new ContentEntry(contentCacheEntry);
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
        return contentEntry;
    }

    public ContentEntry findContentByName(String contentName) {
        ContentEntry contentEntry = null;
        try {
            ContentCacheEntry contentCacheEntry = ContentDao.getDefault().getContentByName(contentName);
            contentEntry = new ContentEntry(contentCacheEntry);
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
        return contentEntry;
    }

    public ArrayList<ContentEntry> findListContentByDirectoryOnCategory (String directory) {
        if (StringUtils.isBlank(directory)) {
            return null;
        }

        ArrayList<ContentEntry> contentEntries = new ArrayList<>();

        try {
            ContentCategoryEntry contentCategoryEntry = MediaControler.getDefault().getContentCategoryByDirectory(directory);
            if (null != contentCategoryEntry) {
                contentEntries.addAll(contentCategoryEntry.getContentEntries());
            }
        } catch (Exception e){
            LogUtil.INSTANCE.error(getClass().getSimpleName(), e);
        }
        return contentEntries;
    }


    public ArrayList<ContentViewEntry> findListContentViewByDirectoryOnCategory (String directory) {
        if (StringUtils.isBlank(directory)) {
            return null;
        }

        ArrayList<ContentViewEntry> contentEntries = new ArrayList<>();

        try {
            ContentCategoryEntry contentCategoryEntry = MediaControler.getDefault().getContentCategoryByDirectory(directory);
            if (null != contentCategoryEntry) {
                contentEntries.addAll(contentCategoryEntry.getContentViewEntries());
            }
        } catch (Exception e){
            LogUtil.INSTANCE.error(getClass().getSimpleName(), e);
        }
        return contentEntries;
    }


    public List<ContentEntry> findContentListRecentlyPlayedList() {
        List<ContentEntry> contentEntries = new ArrayList<>();
        try {
            ContentCategoryEntry contentCategoryEntry = null;
            if (null == contentCategoryEntry) {
                ArrayList<ContentCacheEntry> contentCacheEntries = ContentDao.getDefault().getRecentlyPlayedContentList();
                for (ContentCacheEntry entry : contentCacheEntries) {
                    ContentEntry contentEntry  = new ContentEntry(entry);
                    contentEntries.add(contentEntry);
                }
            } else {
                contentEntries = contentCategoryEntry.getContentEntries();
            }

        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
        return contentEntries;
    }


    /****
     *
     */
    public int getStorageTypeFromContent (String contentPath) {
        int type = 0;
        if (StringUtils.contains(contentPath, "/storage/emulated/legacy") || StringUtils.contains(contentPath, "/storage/emulated/0")) {
            LogUtil.INSTANCE.info("birdgangstoragelist" , "emulated : " + contentPath);
            type = StorageEntry.StorageType.INTERNAL.ordinal();
        }
        else if (StringUtils.contains(contentPath, "/storage/sdcard1")) {
            LogUtil.INSTANCE.info("birdgangstoragelist" , "sdcard1 : " + contentPath);
            type = StorageEntry.StorageType.EXTERNAL.ordinal();
        }
        else if (StringUtils.contains(contentPath, "otg:/")) {
            LogUtil.INSTANCE.info("birdgangstoragelist" , "otg : " + contentPath);
            type = StorageEntry.StorageType.OTG.ordinal();
        }
        else {
            LogUtil.INSTANCE.info("birdgangstoragelist" , "other : " + contentPath);
            type = StorageEntry.StorageType.EXTERNAL.ordinal();
        }
        return type;
    }


    public List<ContentEntry> findListContentByStorageType(int type) {
        List<ContentEntry> contentEntries = new ArrayList<>();
        try {
            List<ContentEntry> entries = MediaControler.getDefault().getItemList();
            for (ContentEntry entry : entries) {
                int storageType = entry.getStorageType();
                if (type == storageType) {
                    contentEntries.add(entry);
                } else if (type == storageType) {
                    contentEntries.add(entry);
                }
            }
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
        return contentEntries;
    }

    /***
     *
     */
    public int builkInsertContent (List<ContentEntry> contentEntries) {
        int count = 0;
        try {
            ArrayList<ContentCacheEntry> contentCacheEntries = new ArrayList<>();
            for (ContentEntry entry : contentEntries) {
                contentCacheEntries.add(entry.convertCacheEntry());
            }
            count = ContentDao.getDefault().bulkInsertContents(contentCacheEntries);
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
        return count;
    }

    /***
     *
     * @param filepath
     * @return
     */
    public boolean updateContentLicenseInfo(ContentEntry contentEntry, String filepath) {
        try {
            // 라이센스 Info 업데이트
            int contentId = contentEntry.getContentId();
            String licenseInfo = Ncg2SdkHelper.getDefault().getLicenseInfo(filepath);
            LogUtil.INSTANCE.info(TAG, "processNcgLicenseInfo > licenseInfo > " + licenseInfo + " , filepath : " + filepath + " , contentId : " + contentId);
            contentEntry.setLicenseInfo(licenseInfo);

            long result = updateContent(contentId, MetaData.ContentColumns.LICENSE_INFO, licenseInfo);
            if (result < 0) {
                LogUtil.INSTANCE.info(TAG, "processNcgLicenseInfo > " + context.getString(R.string.license_fail_to_update_license_info));
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }



    public boolean updateContentLicenseInfo(int contentId, String filepath) {
        try {
            // 라이센스 Info 업데이트
            String licenseInfo = Ncg2SdkHelper.getDefault().getLicenseInfo(filepath);
            LogUtil.INSTANCE.info(TAG, "processNcgLicenseInfo > licenseInfo > " + licenseInfo + " , filepath : " + filepath + " , contentId : " + contentId);

            long result = updateContent(contentId, MetaData.ContentColumns.LICENSE_INFO, licenseInfo);
            LogUtil.INSTANCE.info(TAG, "processNcgLicenseInfo > result > " + result);

            if (result < 0) {
                LogUtil.INSTANCE.info(TAG, "processNcgLicenseInfo > " + context.getString(R.string.license_fail_to_update_license_info));
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean updateContentLastPlayTime (int contentId, String lastPlayTime) {
        try {
            long result = updateContent(contentId, MetaData.ContentColumns.CONTENT_LAST_PLAY_TIME, lastPlayTime);
            if (result < 0) {
                return false;
            }
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
        return true;
    }


    public boolean updateContentPlayDate (int contentId, String playdate) {
        try {
            long result = updateContent(contentId, MetaData.ContentColumns.PLAY_DATE, playdate);
            if (result < 0) {
                return false;
            }
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
        return true;
    }


    private long updateContent (int contentId, String column, Object object) {
        long result = -1;
        try {
            result = ContentDao.getDefault().updateContent(contentId, column, object);
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
        return result;
    }


    /***
     *
     * @param contentEntry
     * @return
     */
    public long updateContent (ContentEntry contentEntry) {
        long result = 1;
        if (null == contentEntry) {
            return result;
        }

        try {
            if (contentEntry.getContentId() <= 0) {
                ContentEntry entry = findContentById(contentEntry.getContentId());
                contentEntry.setContentId(entry.getContentId());
            }
            MediaControler.getDefault().updateContentCategory(contentEntry);
            ContentCacheEntry contentCacheEntry = contentEntry.convertCacheEntry();
            result = ContentDao.getDefault().updateContent(contentCacheEntry);
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
        return result;
    }


    public List<ContentEntry> getPlaylist() {
        return playlist;
    }

    public void setPlaylist(List<ContentEntry> playlist) {
        this.playlist = playlist;
    }

    /**
     * 디렉토리가 아닌 경우에만 플레이리스트 삽입.
     * @param playlist
     */
    public void setPlaylistFromView(List<ContentViewEntry> playlist) {
        List<ContentEntry> contentEntries = new ArrayList<>();
        for (ContentViewEntry entry : playlist) {
            String mediaType = entry.getMediaType();
            LogUtil.INSTANCE.info(TAG , "setPlaylistFromView > mediaType : " + mediaType + ", entry : " + entry.toString());

            if (StringUtils.equals(ContentViewEntry.ContentType.VIDEO.getType(), mediaType)
                    || StringUtils.equals(ContentViewEntry.ContentType.AUDIO.getType(), mediaType)) {
                ContentEntry contentEntry = new ContentEntry(entry);
                contentEntries.add(contentEntry);
            }
        }
        this.playlist = contentEntries;
    }


    public void setRecentlyPlaylistFromView(List<RecentlyViewEntry> playlist) {
        List<ContentEntry> contentEntries = new ArrayList<>();
        for (RecentlyViewEntry recentlyViewEntry : playlist) {
            String path = recentlyViewEntry.getContentPath();
            ContentEntry contentEntry = findContentByFilePath(path);
            contentEntries.add(contentEntry);
        }
        this.playlist = contentEntries;
    }


    public List<ContentEntry> doSortContentList (String sortType) {
        if (null == playlist) {
            return null;
        }

        try {
            if (sortType == AppConstants.LIST_ORDER_DEFAULT) {
                Collections.sort(playlist, new ContentEntry.SortContentNameAscCompare());
            }
            else if (sortType == AppConstants.LIST_ORDER_NAME_ASC) {
                Collections.sort(playlist, new ContentEntry.SortContentNameAscCompare());
            }
            else if(sortType == AppConstants.LIST_ORDER_NAME_DES) {
                Collections.sort(playlist, new ContentEntry.SortContentNameDescCompare());
            }
            else if (sortType == AppConstants.LIST_ORDER_DATA_ASC) {
                Collections.sort(playlist, new ContentEntry.SortContentFileSizeAscCompare());
            }
            else if(sortType == AppConstants.LIST_ORDER_DATA_DES) {
                Collections.sort(playlist, new ContentEntry.SortContentFileSizeDescCompare());
            }
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
        return playlist;
    }

}
