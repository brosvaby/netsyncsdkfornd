package com.inka.netsync.controler;

import android.os.Environment;
import android.support.v4.util.ArrayMap;

import com.inka.ncg2.Ncg2Exception;
import com.inka.netsync.BaseApplication;
import com.inka.netsync.BaseConfiguration;
import com.inka.netsync.admin.ModuleConfig;
import com.inka.netsync.common.utils.DateTimeUtil;
import com.inka.netsync.common.utils.StringUtil;
import com.inka.netsync.data.cache.db.dao.ContentDao;
import com.inka.netsync.data.cache.db.dao.FavoriteDao;
import com.inka.netsync.data.cache.db.model.ContentCacheEntry;
import com.inka.netsync.logs.LogUtil;
import com.inka.netsync.media.MediaScan;
import com.inka.netsync.media.MediaStorage;
import com.inka.netsync.media.bus.DevicesDiscoveryCb;
import com.inka.netsync.media.bus.ScanEventBus;
import com.inka.netsync.media.model.ScanEntry;
import com.inka.netsync.model.ContentCategoryEntry;
import com.inka.netsync.model.ContentEntry;
import com.inka.netsync.model.ListContentCategoryEntry;
import com.inka.netsync.model.StorageEntry;
import com.inka.netsync.ncg.Ncg2SdkHelper;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by birdgang on 2017. 12. 13..
 */

public class MediaControler {

    private final String TAG = "MediaControler";

    private final ReadWriteLock mItemListLock;
    private final ArrayList<ContentEntry> mItemList;
    private final ListContentCategoryEntry mListContentCategoryEntry;

    private volatile List<DevicesDiscoveryCb> mDvicesDiscoveryCbList = new ArrayList<>();

    static volatile MediaControler defaultInstance;

    public static MediaControler getDefault() {
        if (defaultInstance == null) {
            synchronized (MediaControler.class) {
                if (defaultInstance == null) {
                    defaultInstance = new MediaControler();
                }
            }
        }
        return defaultInstance;
    }

    private MediaControler() {
        mItemList = new ArrayList<ContentEntry>();
        mListContentCategoryEntry = new ListContentCategoryEntry();
        mItemListLock = new ReentrantReadWriteLock();
        init();
    }

    public void init() {
    }

    public void setMediaScanListener (ScanEventBus.ScanResultOnUi scanResultOnUi) {
        ScanEventBus.getDefault().setScanResult(new ScanResultImpl(scanResultOnUi));
    }

    public void loadData () {
        MediaScan.getInstance().loadMediaItems();
    }

    public void scanMediaItems(boolean restart) {
        MediaScan.getInstance().scanMediaItems(restart);
    }

    public void scanMediaItems() {
        MediaScan.getInstance().scanMediaItems();
    }

    public void stop() {
        MediaScan.getInstance().stop();
    }

    public boolean isWorking() {
        return MediaScan.getInstance().isWorking();
    }

    public void clear () {
        if (null != mItemList) {
            mItemList.clear();
        }
        if (null != mListContentCategoryEntry) {
            mListContentCategoryEntry.clear();
        }
    }


    public ArrayList<ContentEntry> getItemList() {
        return mItemList;
    }


    public void addDeviceDiscoveryCb(DevicesDiscoveryCb cb) {
        mDvicesDiscoveryCbList.add(cb);
    }

    public void setDeviceDiscoveryCb(DevicesDiscoveryCb cb) {
        mDvicesDiscoveryCbList.add(0, cb);
    }

    public void removeDeviceDiscoveryCb(DevicesDiscoveryCb cb) {
        mDvicesDiscoveryCbList.remove(cb);
    }

    public void onDiscoveryStarted(String entryPoint) {
        if (!mDvicesDiscoveryCbList.isEmpty())
            for (DevicesDiscoveryCb cb : mDvicesDiscoveryCbList)
                cb.onDiscoveryStarted(entryPoint);
        LogUtil.INSTANCE.debug(TAG, "onDiscoveryStarted: "+entryPoint);
    }

    public void onDiscoveryProgress(String entryPoint) {
        if (!mDvicesDiscoveryCbList.isEmpty())
            for (DevicesDiscoveryCb cb : mDvicesDiscoveryCbList)
                cb.onDiscoveryProgress(entryPoint);
        LogUtil.INSTANCE.debug(TAG, "onDiscoveryProgress: "+entryPoint);
    }

    public void onDiscoveryCompleted(String entryPoint) {
        if (!mDvicesDiscoveryCbList.isEmpty())
            for (DevicesDiscoveryCb cb : mDvicesDiscoveryCbList)
                cb.onDiscoveryCompleted(entryPoint);
        LogUtil.INSTANCE.debug(TAG, "onDiscoveryCompleted: "+entryPoint);
    }

    public void onParsingStatsUpdated(int percent, String entryPoint) {
        if (!mDvicesDiscoveryCbList.isEmpty()) {
            for (DevicesDiscoveryCb cb : mDvicesDiscoveryCbList) {
                cb.onParsingStatsUpdated(percent, entryPoint);
            }
        }
    }

    public void onParsingStatsUpdatedSync(int percent, String entryPoint) {
        if (!mDvicesDiscoveryCbList.isEmpty()) {
            for (DevicesDiscoveryCb cb : mDvicesDiscoveryCbList) {
                cb.onParsingStatsUpdatedSync(percent, entryPoint);
            }
        }
    }

    /****
     *
     * @return
     */
    public List<ContentCategoryEntry> loadContentCategoryList () {
        List<ContentCategoryEntry> mediaCategoryEntries = new ArrayList<ContentCategoryEntry>();
        try {
            mItemListLock.readLock().lock();

            List<ContentCategoryEntry> categoryEntries = mListContentCategoryEntry.getMediaCategoryEntries();
            LogUtil.INSTANCE.info("birdgangscanmedia", "getMediaCategoryList > categoryEntries size : " + categoryEntries.size());

            for (int i = 0; i < categoryEntries.size(); i++) {
                ContentCategoryEntry categoryEntry = categoryEntries.get(i);
                if (categoryEntry != null) {
                    if (categoryEntry.getContentEntries() == null || categoryEntry.getContentEntries().isEmpty()) {
                        continue;
                    }
                    int contentCount = categoryEntry.getContentEntries().size();
                    LogUtil.INSTANCE.info("birdgangscanmedia", "loadContentCategoryList > contentCount : " + contentCount + " , categoryEntry : " + categoryEntry.toString());

                    categoryEntry.setContentCount(contentCount);
                    mediaCategoryEntries.add(categoryEntry);
                }
            }

            mItemListLock.readLock().unlock();
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
        return mediaCategoryEntries;
    }


    /***
     *
     * @param storageType
     * @return
     */
    public List<ContentCategoryEntry> loadContentCategoryListAtStorage (int storageType) {
        List<ContentCategoryEntry> mediaCategoryEntries = new ArrayList<ContentCategoryEntry>();
        try {
            mItemListLock.readLock().lock();

            List<ContentCategoryEntry> categoryEntries = mListContentCategoryEntry.getMediaCategoryEntries();
            LogUtil.INSTANCE.info("birdgangscanmedia", "getMediaCategoryList > categoryEntries size : " + categoryEntries.size());

            for (int i = 0; i < categoryEntries.size(); i++) {
                ContentCategoryEntry categoryEntry = categoryEntries.get(i);
                if (categoryEntry != null) {
                    if (categoryEntry.getContentEntries() == null || categoryEntry.getContentEntries().isEmpty()) {
                        continue;
                    }
                    int contentCount = categoryEntry.getContentEntries().size();
                    categoryEntry.setContentCount(contentCount);
                    LogUtil.INSTANCE.info("birdgangscanmedia", "storageType : " + storageType + " , categoryEntry.getStorageType() : " + categoryEntry.getStorageType());
                    if (storageType == categoryEntry.getStorageType()) {
                        mediaCategoryEntries.add(categoryEntry);
                    }
                }
            }

            mItemListLock.readLock().unlock();
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
        return mediaCategoryEntries;
    }


    public void updateContentCategory (ContentEntry contentEntry) {
        try {
            mListContentCategoryEntry.updateContentCategoryByContent(contentEntry.convertCacheEntry());
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
    }


    /**
     * 디렉토리에 대한 영상 목록 가져오
     * @param directory
     * @return
     */
    public ContentCategoryEntry getContentCategoryByDirectory (String directory) {
        if (StringUtils.isBlank(directory)) {
            return null;
        }
        ContentCategoryEntry contentCategoryEntry = null;
        try {
            contentCategoryEntry = mListContentCategoryEntry.findMediaCategoryByDirectory(directory);
        } catch (Exception e){
            LogUtil.INSTANCE.error(getClass().getSimpleName(), e);
        }
        return contentCategoryEntry;
    }


    /***
     *
     */
    class ScanResultImpl implements ScanEventBus.ScanResult {

        private ScanEventBus.ScanResultOnUi scanResultOnUi;
        private HashSet<String> addedLocations;
        private ArrayMap<String, ContentEntry> existingContents;

        private int count = 0;
        private int totalCount = 0;

        public ScanResultImpl (ScanEventBus.ScanResultOnUi scanResultOnUi) {
            this.scanResultOnUi = scanResultOnUi;
            this.addedLocations = new HashSet<String>();
            this.existingContents = new ArrayMap<>();
            this.count = 0;
            this.totalCount = 0;
        }

        @Override
        public void onMediaLoadCache() {
            LogUtil.INSTANCE.info(TAG , "onMediaLoadCache");

            try {
                scanResultOnUi.onMediaScanStartInUi();
                mItemListLock.writeLock().lock();
                mItemList.clear();
                mListContentCategoryEntry.clear();
                addContentAll(ContentControler.getDefault().loadContentMaps().values());
                for (ContentEntry entry : mItemList) {
                    addContentCategory(entry);
                }
                mItemListLock.writeLock().unlock();

                LogUtil.INSTANCE.info(TAG , "onMediaLoadCache > mItemList size : " + mItemList.size());
            } catch (Exception e) {
                LogUtil.INSTANCE.error("birdgangscanmedia", e);
            } finally {
                scanResultOnUi.onMediaLoadCompletedInUi();
            }
        }

        @Override
        public void onMediaScanStart() {
            LogUtil.INSTANCE.info(TAG , "onScanStart");

            this.count = 0;

            BaseApplication.runBackground(new Runnable() {
                @Override
                public void run() {
                    try {
                        mItemListLock.writeLock().lock();
                        mItemList.clear();
                        mItemListLock.writeLock().unlock();
                        scanResultOnUi.onMediaScanStartInUi();
                        onDiscoveryStarted("0");
                    } catch (Exception e) {
                        LogUtil.INSTANCE.error(TAG, e);
                    }
                }
            });
        }

        @Override
        public void onUpdateMediaPreSync(final LinkedList<File> mediaToScan, final LinkedList<String> dirsToIgnore) {
            BaseApplication.runBackground(new Runnable() {
                @Override
                public void run() {
                    try {
                        existingContents = ContentControler.getDefault().loadContentMaps();
                        LogUtil.INSTANCE.info(TAG, "onUpdateMediaPreSync > existingContents : " + existingContents.size()
                                + " , dirsToIgnore : " + dirsToIgnore.size() + " , mediaToScan.size() : " + mediaToScan.size());

                        HashSet<String> mediasToRemove = new HashSet<String>();
                        String path;
                        outloop:
                        for (Map.Entry<String, ContentEntry> entry : existingContents.entrySet()) {
                            path = entry.getKey();
                            for (String dirPath : dirsToIgnore) {
                                if (path.startsWith(dirPath)) {
                                    mediasToRemove.add(entry.getValue().getContentFilePath());
                                    mItemListLock.writeLock().lock();
                                    mItemList.remove(existingContents.get(path));
                                    mItemListLock.writeLock().unlock();
                                    continue outloop;
                                }
                            }
                        }

                        LogUtil.INSTANCE.info(TAG, "onUpdateMediaPreSync > mediasToRemove.size() : " + mediasToRemove.size());

                        ContentDao.getDefault().bulkDeleteContentsByPath(mediasToRemove);

                        totalCount = mediaToScan.size();
                    } catch (Exception e) {
                        LogUtil.INSTANCE.error(TAG, e);
                    }
                }
            });
        }


        @Override
        public void onUpdateMedia(int totalSize, final ScanEntry scanEntry) {
            LogUtil.INSTANCE.info(TAG , "onUpdateMedia > totalSize : " + totalSize + " , scanEntry : " + scanEntry.toString());

            String filePath = scanEntry.getContentFilePath();
            String fileName = scanEntry.getContentName();
            String directory = scanEntry.getDirectory();

            count++;

            try {
                String applicationContentId = BaseConfiguration.getInstance().getApplicationContentId();
                String contentId = Ncg2SdkHelper.getDefault().getContentIdInHeaderInformation(filePath);

                LogUtil.INSTANCE.info(TAG , "onUpdateMedia > totalSize : " + totalSize + " , applicationContentId : " + applicationContentId + " , contentId : " + contentId + " , scanEntry : " + scanEntry.toString());

                if (!StringUtils.equals(applicationContentId, contentId) && !ModuleConfig.ENABLE_NO_LIMIT_BY_CID && !ModuleConfig.ENABLE_MODE_PREVIEW_APP) {
                    LogUtil.INSTANCE.info(TAG , "onUpdateMedia > return!! > contentId : " + contentId);
                }
                else {
                    String title = null;

                    if (existingContents.containsKey(filePath)) {
                        mItemListLock.writeLock().lock();
                        // get existing media item from database
                        ContentEntry existContent = existingContents.get(filePath);
                        if (null == existContent) {
                            return;
                        }
                        int existContentId = existContent.getContentId();
                        title = existContent.getContentName();
                        boolean added = addContent(existContent);
                        if (added) {
                            addContentCategory(existContent);
                        }
                        mItemListLock.writeLock().unlock();
                        addedLocations.add(filePath);
                        LogUtil.INSTANCE.info(TAG, "onUpdateMedia > added : " + added + " , existContentId : " + existContentId + " , title : " + title);

                    }
                    else {
                        mItemListLock.writeLock().lock();
                        // create new media item
                        if (StringUtils.isBlank(filePath)) {
                            mItemListLock.writeLock().unlock();
                            return;
                        }

                        title = fileName;

                        ContentEntry newContentEntry = new ContentEntry();
                        newContentEntry.setContentFilePath(scanEntry.getContentFilePath());
                        newContentEntry.setContentName(scanEntry.getContentName());
                        newContentEntry.setDirectory(scanEntry.getDirectory());

                        boolean hasFavorite = FavoriteControler.getDefault().hasFavoriteContentWithPath(scanEntry.getContentFilePath());
                        if (hasFavorite) {
                            newContentEntry.setIsFavoriteContent(1);
                        }

                        long updateContent = ContentDao.getDefault().replaceContent(newContentEntry.convertCacheEntry());
                        LogUtil.INSTANCE.info(TAG, "onUpdateMedia > updateContent : " + updateContent + " , newContentEntry.toString() : " + newContentEntry.toString());
                        newContentEntry.setContentId((int) updateContent);
                        boolean added = addContent(newContentEntry);
                        if (added) {
                            addContentCategory(newContentEntry);
                        }

                        final String finalTitle = title;
                        mItemListLock.writeLock().unlock();
                    }
                }
            } catch (Ncg2Exception e) {
                LogUtil.INSTANCE.error(TAG, e);
            } catch (Exception e) {
                LogUtil.INSTANCE.error(TAG, e);
            }

            try {
                int percent = (int) ((float)count / (float)totalSize * 100);
                LogUtil.INSTANCE.debug(TAG, "onParsingStatsUpdated > percent : " + percent + " , count : " + count + " , totalCount : " + totalCount);
                onParsingStatsUpdated(percent, fileName);
            } catch (Exception e) {
                LogUtil.INSTANCE.error(TAG, e);
            }
        }


        /***
         * 새롭게 디렉토리에서 발견된 미디어와 기존에 디스크 캐쉬된 미디어와 동기화.
         * @param isStopping
         */
        @Override
        public synchronized void onUpdateMediaPostSync(final boolean isStopping) {
            BaseApplication.runBackground(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (!isStopping && Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                            LogUtil.INSTANCE.info(TAG, "onUpdateMediaPostSync > addedLocations : " + addedLocations.size() + " , existingContents.size() : " + existingContents.values().size());

                            int updateCount = 0;
                            int updateTotalCount = addedLocations.size();

                            List<ContentEntry> updateContentEntries = new ArrayList<>();

                            for (String fileURI : addedLocations) {
                                ContentEntry contentEntry = getExistContent(fileURI);
                                if (null == contentEntry) {
                                    return;
                                }

                                contentEntry.setContentFilePath(fileURI);
                                contentEntry.setDirectory(StringUtil.extractMediaDirctory(fileURI));
                                updateContentEntries.add(contentEntry);
                                long updateContent = ContentDao.getDefault().updateContent(contentEntry.convertCacheEntry());
                                if (updateContent > 0) {
                                    existingContents.remove(fileURI);
                                }
                                updateCount++;

                                int percent = (int) ((float)updateCount / (float)updateTotalCount * 100);
                                onParsingStatsUpdatedSync(percent, contentEntry.getContentName());
                            }
                            LogUtil.INSTANCE.debug(TAG, "onUpdateMediaPostSync will remove soon > : " + existingContents.values().size());

                            ArrayMap<String, ContentCacheEntry> existingContentsCache = new ArrayMap<>();

                            for (Map.Entry<String, ContentEntry> entry : existingContents.entrySet()) {
                                existingContentsCache.put(entry.getKey(), entry.getValue().convertCacheEntry());
                                LogUtil.INSTANCE.debug(TAG, "onUpdateMediaPostSync > will remove soon > existingContents : " + entry.getValue().getContentFilePath());
                            }
                            ContentDao.getDefault().removeContentsWrappers(existingContentsCache.values());
                        }
                    } catch (Exception e) {
                        LogUtil.INSTANCE.error(TAG, e);
                    }
                }
            });
        }

        @Override
        public void onMediaScanCompleted(final List<ScanEntry> scanEntries) {
            BaseApplication.runBackground(new Runnable() {
                @Override
                public void run() {
                    try {
                        scanResultOnUi.onMediaScanCompletedInUi(scanEntries);
                        onDiscoveryCompleted("");
                        LogUtil.INSTANCE.info(TAG , "onMediaScanCompleted > scanEntries : " + scanEntries.size() + " , mItemList.size() : " + mItemList.size());
                    } catch (Exception e) {
                        LogUtil.INSTANCE.error(TAG, e);
                    }
                }
            });
        }

        @Override
        public void onMediaScanStop() {
            LogUtil.INSTANCE.info(TAG , "onMediaScanStopInUi");
            BaseApplication.runBackground(new Runnable() {
                @Override
                public void run() {
                    try {
                        scanResultOnUi.onMediaScanStopInUi();
                    } catch (Exception e) {
                        LogUtil.INSTANCE.error(TAG, e);
                    }
                }
            });
        }

        public ContentEntry getExistContent (String path) {
            ContentEntry existContent = null;
            if (null != existingContents && existingContents.size() > 0) {
                existContent = existingContents.get(path);
            }
            return existContent;
        }


        /**
         *
         * @param contentEntries
         * @throws Exception
         */
        public void addContentAll (Collection<ContentEntry> contentEntries) throws Exception {
            for (ContentEntry contentEntry : contentEntries) {
                addContent(contentEntry);
            }
        }

        /**
         *
         * @param contentEntry
         * @return
         * @throws Exception
         */
        public boolean addContent (ContentEntry contentEntry) throws Exception {
            try {
                String filePath = contentEntry.getContentFilePath();
                MediaStorage.INSTANCE.addStorageForPath(filePath);

                boolean addCondition = addConditionByCotentId(filePath);
                if (!addCondition) {
                    return false;
                }
            } catch (Exception e) {
                LogUtil.INSTANCE.error(TAG, e);
                return false;
            }


            try {
                String contentName = contentEntry.getContentName();
                boolean hasFavoriteTable = FavoriteDao.getDefault().hasTable();
                if (hasFavoriteTable) {
                    boolean hasFavorite = FavoriteControler.getDefault().hasFavoroteByContentName(contentName);
                    //LogUtil.INSTANCE.info(TAG, " hasFavorite : " + hasFavorite + " , contentName : " + contentName + " , hasFavoriteTable : " + hasFavoriteTable);
                    if (hasFavorite) {
                        contentEntry.setIsFavoriteContent(1);
                    } else {
                        contentEntry.setIsFavoriteContent(0);
                    }
                }

                String contentFormatName = contentEntry.getContentName();
                LogUtil.INSTANCE.info(TAG, "contentFormatName : " + contentFormatName);
                if (StringUtils.contains(contentFormatName, "mp4")) {
                    contentEntry.setMediaType(ContentEntry.ContentType.VIDEO.getType());
                } else if (StringUtils.contains(contentFormatName, "mp3")) {
                    contentEntry.setMediaType(ContentEntry.ContentType.AUDIO.getType());
                } else if (StringUtils.contains(contentFormatName, "pdf")) {
                    contentEntry.setMediaType(ContentEntry.ContentType.DOC.getType());
                } else {
                    contentEntry.setMediaType(ContentEntry.ContentType.GROUP.getType());
                }
            } catch (Exception e) {
                LogUtil.INSTANCE.error(TAG, e);
                return false;
            }

            try {
                String filePath = contentEntry.getContentFilePath();
                if (StringUtils.contains(filePath, "/storage/emulated/legacy") || StringUtils.contains(filePath, "/storage/emulated/0")) {
                    contentEntry.setStorageType(StorageEntry.StorageType.INTERNAL.ordinal());
                }
                else if (StringUtils.contains(filePath, "/storage/sdcard1")) {
                    contentEntry.setStorageType(StorageEntry.StorageType.EXTERNAL.ordinal());
                }
                else if (StringUtils.contains(filePath, "otg:/")) {
                    contentEntry.setStorageType(StorageEntry.StorageType.OTG.ordinal());
                }
                else {
                    contentEntry.setStorageType(StorageEntry.StorageType.EXTERNAL.ordinal());
                }
                //LogUtil.INSTANCE.info(TAG, " contentEntry.getStorageType() : " + contentEntry.getStorageType() + " , filePath : " + filePath);
            } catch (Exception e) {
                LogUtil.INSTANCE.error(TAG, e);
                return false;
            }

            try {
                SimpleDateFormat dateFormat = DateTimeUtil.getDateFormatDirectory();
                String dateString = dateFormat.format(new Date());
                LogUtil.INSTANCE.info(TAG, " dateString : " + dateString);
                contentEntry.setContentDownloadDate(dateString);
            } catch (Exception e) {
                LogUtil.INSTANCE.error(TAG, e);
                return false;
            }

            boolean hasContent = mItemList.contains(contentEntry);
            //LogUtil.INSTANCE.info(TAG, " hasContent : " + hasContent);
            if (!hasContent) {
                mItemList.add(contentEntry);
            } else {
                return false;
            }
            
            return true;
        }


        /**
         *
         * @param contentEntry
         * @return
         * @throws Exception
         */
        public long addContentCategory (ContentEntry contentEntry) throws Exception {
            String filePath = contentEntry.getContentFilePath();
            boolean addCondition = addConditionByCotentId(filePath);
            if (!addCondition) {
                return -1;
            }

            long result = contentEntry.getCategoryId();
            try {
                String directory = StringUtil.extractMediaDirctory(contentEntry.getContentFilePath());
                contentEntry.setDirectory(directory);
                mListContentCategoryEntry.generateContent(contentEntry);
            } catch (Exception e) {
                LogUtil.INSTANCE.error(TAG, e);
            }
            return result;
        }
    }


    public boolean addConditionByCotentId (String filePath) {
        try {
            String applicationContentId = BaseConfiguration.getInstance().getApplicationContentId();
            String contentId = Ncg2SdkHelper.getDefault().getContentIdInHeaderInformation(filePath);

            if (!StringUtils.equals(applicationContentId, contentId) && !ModuleConfig.ENABLE_NO_LIMIT_BY_CID && !ModuleConfig.ENABLE_MODE_PREVIEW_APP) {
                LogUtil.INSTANCE.info(TAG, " is not equal contentId : " + contentId + " , filePath : " + filePath);
                return false;
            }
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
        return true;
    }

}