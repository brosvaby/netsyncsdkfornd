package com.inka.netsync.data.cache.db.migration;

import com.inka.netsync.data.cache.db.model.ContentCacheEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by birdgang on 2018. 3. 13..
 */

public class MigrationContentManager {

    private static volatile MigrationContentManager defaultInstance;

    private List<ContentCacheEntry> existContentEntries = null;

    public static MigrationContentManager getDefault() {
        if (defaultInstance == null) {
            synchronized (MigrationContentManager.class) {
                if (defaultInstance == null) {
                    defaultInstance = new MigrationContentManager();
                }
            }
        }
        return defaultInstance;
    }

    private MigrationContentManager() {
        this.existContentEntries = new ArrayList<>();
    }

    public List<ContentCacheEntry> getExistContentEntries() {
        return existContentEntries;
    }

    public void setExistContentEntries(List<ContentCacheEntry> existContentEntries) {
        this.existContentEntries = existContentEntries;
    }
}
