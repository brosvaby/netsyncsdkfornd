package com.inka.netsync.common.bus;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

public class EventBus {

    static volatile EventBus defaultInstance;

    private ArrayList<onEventSelectedMenuListener> onEventSelectedMenuListener = null;
    private ArrayList<onEventDialogBookmarkListener> onEventDialogBookmarkListener = null;
    private ArrayList<onEventUpdateContentListener> onEventUpdateContentListener = null;
    private ArrayList<onDialogEventListener> onDialogEventListener = null;
    private ArrayList<onEventDeviceStateListener> onEventDeviceStateListener = null;
    private ArrayList<onEventSDCardScanListener> onEventSDCardScanListener = null;

    public static EventBus getDefault() {
        if (defaultInstance == null) {
            synchronized (EventBus.class) {
                if (defaultInstance == null) {
                    defaultInstance = new EventBus();
                }
            }
        }
        return defaultInstance;
    }

    private EventBus() {
        init();
    }

    public interface onEventUpdateContentListener {
        public void onRefleshContents();
        public void onUpdateFavoriteExplorerContentList(String playType, int contentId);
        public void onUpdateLMSExplorerContentList(String playType);
        public void onListOrderContents(String type);
    }

    public interface onEventDeviceStateListener {
        public void onNetworkChangedEvent();
        public void onSDcardMountedEvent(String externalSDPath);
        public void onSDcardEjectedEvent();
    }

    public interface onEventSDCardScanListener {
        public void onScanStarted();
        public void onScaning();
        public void onScanCompleated();
    }

    public interface onEventDialogBookmarkListener {
        public void onBookmarkItemClick(int bookmarkLocation);
        public void onFragmentFinished();
    }

    public interface onDialogEventListener {
        public void onDialogPositiveClick(int dialogId, int index, String commonName);
    }

    public interface onEventSelectedMenuListener {
        public void onSelectedDrawerMenu(String tag);
    }


    public void init() {
        onEventSelectedMenuListener = new ArrayList<>();
        onEventDialogBookmarkListener = new ArrayList<>();
        onEventUpdateContentListener = new ArrayList<>();
        onDialogEventListener = new ArrayList<>();
        onEventDeviceStateListener = new ArrayList<>();
        onEventSDCardScanListener = new ArrayList<>();
    }

    public void registerDialogBookmarkListener(onEventDialogBookmarkListener listener) {
        if (!onEventDialogBookmarkListener.contains(listener)) {
            onEventDialogBookmarkListener.add(listener);
        }
    }

    public void removeDialogBookmarkListener(onEventDialogBookmarkListener listener) {
        onEventDialogBookmarkListener.remove(listener);
    }


    public void registerEventUpdateContentListener(onEventUpdateContentListener listener) {
        if (!onEventUpdateContentListener.contains(listener)) {
            onEventUpdateContentListener.add(listener);
        }
    }

    public void removeEventUpdateContentListener(onEventUpdateContentListener listener) {
        onEventUpdateContentListener.remove(listener);
    }


    public void registerDialogEventListener(onDialogEventListener listener) {
        if (!onDialogEventListener.contains(listener)) {
            onDialogEventListener.add(listener);
        }
    }

    public void removeDialogEventListener(onDialogEventListener listener) {
        onDialogEventListener.remove(listener);
    }

    public void registerEventDeviceStateListener(onEventDeviceStateListener listener) {
        if (!onEventDeviceStateListener.contains(listener)) {
            onEventDeviceStateListener.add(listener);
        }
    }

    public void removeEventDeviceStateListener(onEventDeviceStateListener listener) {
        onEventDeviceStateListener.remove(listener);
    }

    public void registerEventSDCardScanListener(onEventSDCardScanListener listener) {
        if (!onEventSDCardScanListener.contains(listener)) {
            onEventSDCardScanListener.add(listener);
        }
    }

    public void removeEventSDCardScanListener(onEventSDCardScanListener listener) {
        onEventSDCardScanListener.remove(listener);
    }

    public void registerSelectedMenuListener(onEventSelectedMenuListener listener) {
        if (!onEventSelectedMenuListener.contains(listener)) {
            onEventSelectedMenuListener.add(listener);
        }
    }

    public void removeSelectedMenuListener(onEventSelectedMenuListener listener) {
        onEventSelectedMenuListener.remove(listener);
    }



    /***
     *
     */
    public void notifyEventSelectedMenu (String tag) {
        if (StringUtils.isBlank(tag)) {
            return;
        }

        for (onEventSelectedMenuListener listener : onEventSelectedMenuListener) {
            listener.onSelectedDrawerMenu(tag);
        }
    }


    /***
     *
     */
    public void notifyEventBookmarkItemClick (int bookmarkLocation) {
        if (-1 > bookmarkLocation) {
            return;
        }

        for (onEventDialogBookmarkListener listener : onEventDialogBookmarkListener) {
            listener.onBookmarkItemClick(bookmarkLocation);
        }
    }

    public void notifyEventDialogBookmarkFinished () {
        for (onEventDialogBookmarkListener listener : onEventDialogBookmarkListener) {
            listener.onFragmentFinished();
        }
    }


    /***
     *
     */
    public void notifyEventUpdateFavoriteExplorerContentList (String playType, int contentId) {
        for (onEventUpdateContentListener listener : onEventUpdateContentListener) {
            listener.onUpdateFavoriteExplorerContentList(playType, contentId);
        }
    }


    public void notifyEventUpdateLMSExplorerContentList (String playType) {
        for (onEventUpdateContentListener listener : onEventUpdateContentListener) {
            listener.onUpdateLMSExplorerContentList(playType);
        }
    }


    public void notifyEventListOrderContents (String type) {
        for (onEventUpdateContentListener listener : onEventUpdateContentListener) {
            listener.onListOrderContents(type);
        }
    }

    public void notifyEventRefleshContents () {
        for (onEventUpdateContentListener listener : onEventUpdateContentListener) {
            listener.onRefleshContents();
        }
    }

    /***
     *
     */
    public void notifyEventDialogPositiveClicked (int dialogId, int index, String commonName) {
        for (onDialogEventListener listener : onDialogEventListener) {
            listener.onDialogPositiveClick(dialogId, index, commonName);
        }
    }

    /***
     *
     */
    public void notifyEventNetworkChanged () {
        for (onEventDeviceStateListener listener : onEventDeviceStateListener) {
            listener.onNetworkChangedEvent();
        }
    }

    public void notifyEventSDcardMountedEventListener (String externalSDPath) {
        for (onEventDeviceStateListener listener : onEventDeviceStateListener) {
            listener.onSDcardMountedEvent(externalSDPath);
        }
    }

    public void notifyEventSDcardEjected () {
        for (onEventDeviceStateListener listener : onEventDeviceStateListener) {
            listener.onSDcardEjectedEvent();
        }
    }

}
