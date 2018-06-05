package com.inka.netsync.media;

import com.inka.ncg.nduniversal.ModuleConfig;
import com.inka.netsync.common.utils.StringUtil;
import com.inka.netsync.logs.LogUtil;
import com.inka.netsync.media.bus.ScanEventBus;
import com.inka.netsync.media.model.ScanEntry;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by birdgang on 2017. 4. 20..
 */
public class MediaScan {

    private final String TAG = "MediaScan";

    private static MediaScan mInstance;

    private final ArrayList<ScanEntry> mItemList;

    private final ReadWriteLock mItemListLock;
    private boolean isStopping = false;
    private boolean mRestart = false;
    protected Thread mLoadingThread;

    public synchronized static MediaScan getInstance() {
        if (mInstance == null) {
            mInstance = new MediaScan();
        }
        return mInstance;
    }

    private MediaScan() {
        mInstance = this;
        mItemList = new ArrayList<ScanEntry>();
        mItemListLock = new ReentrantReadWriteLock();
    }


    public void scanMediaItems(boolean restart) {
        if (restart && isWorking()) {
            /* do a clean restart if a scan is ongoing */
            mRestart = true;
            isStopping = true;
        } else {
            scanMediaItems();
        }
    }

    public void scanMediaItems() {
        LogUtil.INSTANCE.info("birdgangmediascan", "scanMediaItems!!!!!!!!!!!!!!!!!!!");
        if (mLoadingThread == null || mLoadingThread.getState() == Thread.State.TERMINATED) {
            isStopping = false;
            mLoadingThread = new Thread(new GetMediaItemsRunnable());
            mLoadingThread.start();

            LogUtil.INSTANCE.info("birdgangmediascan", "mLoadingThread.getState() : " + mLoadingThread.getState());
        }
        else {
            LogUtil.INSTANCE.info("birdgangmediascan", "mLoadingThread != null");
        }
    }

    public void loadMediaItems() {
        if (ModuleConfig.ENABLE_NO_SCAN_MEDIA) {
            return;
        }

        if (mLoadingThread == null || mLoadingThread.getState() == Thread.State.TERMINATED) {
            isStopping = false;
            mLoadingThread = new Thread(new LoadMediaItemsRunnable());
            mLoadingThread.start();
        }
    }


    public void stop() {
        isStopping = true;
    }

    public boolean isWorking() {
        if (mLoadingThread == null) {
            return false;
        }

        boolean threadAlive = mLoadingThread.isAlive();
        if (mLoadingThread != null && threadAlive && mLoadingThread.getState() != Thread.State.TERMINATED && mLoadingThread.getState() != Thread.State.NEW) {
            return true;
        }
        return false;
    }


    public void finish () {
        try {
            if (null != mLoadingThread) {
                mLoadingThread.join();
            }
        } catch (InterruptedException e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
    }

    /**
     *
     */
    private class LoadMediaItemsRunnable implements Runnable {

        public LoadMediaItemsRunnable() {
        }

        @Override
        public void run() {
            try {
                ScanEventBus.getDefault().notifyMediaLoadCache();
            } catch (Exception e) {
                LogUtil.INSTANCE.error("birdgangscanmedia", e);
            }
        }
    }
    

    private class GetMediaItemsRunnable implements Runnable {

        private final Stack<File> directories = new Stack<File>();
        private final HashSet<String> directoriesScanned = new HashSet<String>();

        public GetMediaItemsRunnable() {
        }

        @Override
        public void run() {
            ScanEventBus.getDefault().notifyMediaStart();

            List<File> mediaDirs = new ArrayList<>();
            LogUtil.INSTANCE.info("birdgangmediascan", "GetMediaItemsRunnable > mediaDirs : " + mediaDirs.size());

            // Use all available storage directories as our default
            String storageDirs[] = MediaStorage.INSTANCE.getMediaDirectories();
            LogUtil.INSTANCE.info("birdgangmediascan", "GetMediaItemsRunnable > storageDirs length : " + storageDirs.length);

            for (String dir: storageDirs) {
                LogUtil.INSTANCE.info("birdgangmediascan", "GetMediaItemsRunnable > storageDirs > dir : " + dir);
                File f = new File(dir);
                if (f.exists()) {
                    mediaDirs.add(f);
                }
            }

            directories.addAll(mediaDirs);

            // clear all old items
            mItemListLock.writeLock().lock();
            mItemList.clear();
            mItemListLock.writeLock().unlock();

            MediaItemFilter mediaFileFilter = new MediaItemFilter();

            LinkedList<File> mediaToScan = new LinkedList<File>();

            try {
                LinkedList<String> dirsToIgnore = new LinkedList<String>();
                // Count total files, and stack them
                while (!directories.isEmpty()) {
                    File dir = directories.pop();
                    String dirPath = dir.getAbsolutePath();
                    LogUtil.INSTANCE.info("birdgangmediascan", "GetMediaItemsRunnable > dir : " + dir + " , dirPath : " + dirPath);

                    // Skip some system folders
                    if (dirPath.startsWith("/proc/") || dirPath.startsWith("/sys/") || dirPath.startsWith("/dev/")) {
                        LogUtil.INSTANCE.debug("birdgangmediascan", "dirPath.startsWith(/proc/) || dirPath.startsWith(/sys/) || dirPath.startsWith(/dev/)");
                        continue;
                    }

                    // Do not scan again if same canonical path
                    try {
                        dirPath = dir.getCanonicalPath();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (directoriesScanned.contains(dirPath)) {
                        continue;
                    } else {
                        directoriesScanned.add(dirPath);
                    }

                    // Do no scan media in .nomedia folders
                    if (new File(dirPath + "/.nomedia").exists()) {
                        dirsToIgnore.add("file://"+dirPath);
                        continue;
                    }

                    // Filter the extensions and the folders
                    try {
                        String[] files = dir.list();
                        if (files != null) {
                            for (String fileName : files) {
                                File file = new File(dirPath, fileName);
                                if (mediaFileFilter.accept(file)) {
                                    if (file.isFile()) {
                                        mediaToScan.add(file);
                                    } else if (file.isDirectory()) {
                                        directories.push(file);
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        continue;
                    }

                    if (isStopping) {
                        ScanEventBus.getDefault().notifyMediaScanStop();
                        return;
                    }
                }

                ScanEventBus.getDefault().notifyMediaUpdatePreSync(mediaToScan, dirsToIgnore);

                LogUtil.INSTANCE.info("birdgangmediascan", "GetMediaItemsRunnable > mediaToScan size : " + mediaToScan.size());

                // Process the stacked items
                for (File file : mediaToScan) {
                    LogUtil.INSTANCE.info("birdgangmediascan" , " file.getParent() : " + file.getParent() + " , file.getCanonicalPath() : " + file.getCanonicalPath());

                    String filePath = StringUtils.trimToEmpty(file.getPath());
                    String fileName = file.getName();
                    
                    ScanEntry scanEntry = new ScanEntry();
                    scanEntry.setContentFilePath(filePath);
                    scanEntry.setContentName(fileName);
                    scanEntry.setDirectory(StringUtil.extractMediaDirctory(filePath));

                    mItemList.add(scanEntry);

                    LogUtil.INSTANCE.info("birdgangmediascan" , " isStopping : " + isStopping + ",  scanEntry.toString() : " + scanEntry.toString());

                    ScanEventBus.getDefault().notifyMediaUpdate(mediaToScan.size(), scanEntry);

                    MediaStorage.INSTANCE.addStorageForPath(file.getPath());

                    if (isStopping) {
                        LogUtil.INSTANCE.debug("birdgangscanmedia", "Stopping scan");
                        ScanEventBus.getDefault().notifyMediaScanStop();
                        return;
                    }
                }
            } catch (Exception e) {
                LogUtil.INSTANCE.error(TAG, e);
            } finally {
                try {
                    LogUtil.INSTANCE.info("birdgangmediascan" , " finally!!!!!! > mRestart : " + mRestart);

                    ScanEventBus.getDefault().notifyMediaUpdatePostSync(isStopping);

                    if (mRestart) {
                        LogUtil.INSTANCE.debug("birdgangscanmedia", "Restarting scan");
                        mRestart = false;
                        ScanEventBus.getDefault().notifyMediaStart();
                    }

                    List<ScanEntry> list = new ArrayList<>();
                    list.addAll(mItemList);

                    LogUtil.INSTANCE.info("birdgangmediascan" , " finally!!!!!! > list size : " + list.size());

                    ScanEventBus.getDefault().notifyMediaScanComplited(list);

                    LogUtil.INSTANCE.info("birdgangmediascan" , " end ");

                } catch (Exception e) {
                    LogUtil.INSTANCE.error(TAG, e);
                }
            }
        }
    }

}
