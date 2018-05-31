package com.inka.netsync.media.bus;

import com.inka.netsync.logs.LogUtil;
import com.inka.netsync.media.model.ScanEntry;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by birdgang on 2017. 4. 22..
 */

public class ScanEventBus {

    private final String TAG = "ScanEventBus";

    static volatile ScanEventBus defaultInstance;

    private ArrayList<ScanResult> onScanResult = null;

    public static ScanEventBus getDefault() {
        if (defaultInstance == null) {
            synchronized (ScanEventBus.class) {
                if (defaultInstance == null) {
                    defaultInstance = new ScanEventBus();
                }
            }
        }
        return defaultInstance;
    }

    private ScanEventBus() {
        init();
    }


    /***
     * scan process
     */
    public interface ScanResult {
        public void onMediaLoadCache();
        public void onMediaScanStart();
        public void onUpdateMediaPreSync(LinkedList<File> mediaToScan, LinkedList<String> dirsToIgnore);
        public void onUpdateMedia(int totalSize, ScanEntry scanEntry);
        public void onUpdateMediaPostSync(boolean isStopping);
        public void onMediaScanCompleted(List<ScanEntry> scanEntries);
        public void onMediaScanStop();
    }


    /**
     * ui process
     */
    public interface ScanResultOnUi {
        public void onMediaScanStartInUi();
        public void onUpdateMediaInUi(int totalSize, ScanEntry scanEntry);
        public void onMediaScanCompletedInUi(List<ScanEntry> scanEntries);
        public void onMediaLoadCompletedInUi();
        public void onMediaScanStopInUi();
    }

    public void init() {
        onScanResult = new ArrayList<>();
    }

    public void setScanResult (ScanResult scanResult) {
        onScanResult.clear();
        onScanResult.add(scanResult);
    }

    public void registerScanResult (ScanResult scanResult) {
        if (!onScanResult.contains(scanResult)) {
            onScanResult.add(scanResult);
        }
    }

    public void removeScanResult (ScanResult scanResult) {
        onScanResult.remove(scanResult);
    }


    public void notifyMediaLoadCache () {
        try {
            for (ScanResult listener : onScanResult) {
                listener.onMediaLoadCache();
            }
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
    }


    public void notifyMediaStart () {
        try {
            for (ScanResult listener : onScanResult) {
                listener.onMediaScanStart();
            }
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
    }

    public void notifyMediaUpdatePreSync (LinkedList<File> mediaToScan, LinkedList<String> dirsToIgnore) {
        try {
            for (ScanResult listener : onScanResult) {
                listener.onUpdateMediaPreSync(mediaToScan, dirsToIgnore);
            }
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
    }


    public void notifyMediaUpdate (int totalSize , ScanEntry scanEntry) {
        try {
            for (ScanResult listener : onScanResult) {
                listener.onUpdateMedia(totalSize, scanEntry);
            }
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
    }


    public void notifyMediaUpdatePostSync (boolean isStopping) {
        try {
            for (ScanResult listener : onScanResult) {
                listener.onUpdateMediaPostSync(isStopping);
            }
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
    }


    public void notifyMediaScanComplited (List<ScanEntry> scanEntries) {
        try {
            for (ScanResult listener : onScanResult) {
                listener.onMediaScanCompleted(scanEntries);
            }
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
    }

    public void notifyMediaScanStop () {
        try {
            for (ScanResult listener : onScanResult) {
                listener.onMediaScanStop();
            }
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
    }

}
