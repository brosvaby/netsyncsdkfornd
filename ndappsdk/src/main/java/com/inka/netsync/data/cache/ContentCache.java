package com.inka.netsync.data.cache;

import android.annotation.SuppressLint;

import com.inka.netsync.data.cache.db.model.ContentCacheEntry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;

public class ContentCache implements MemoryCacheAware<String, ContentCacheEntry> {
    private final LinkedHashMap<String, ContentCacheEntry> map;

    private final int mMaxSize;
    private int mSize;

    public ContentCache(int maxSize) {
        if (maxSize <= 0) {
            throw new IllegalArgumentException("maxSize <= 0");
        }

        this.mMaxSize = maxSize;
        this.map = new LinkedHashMap<String, ContentCacheEntry>(0, 0.75f, true);
    }

    @Override
    public final ContentCacheEntry get(String key) {
        if (key == null) {
            throw new NullPointerException("key == null");
        }

        synchronized (this) {
            return map.get(key);
        }
    }

    @Override
    public final boolean put(String key, ContentCacheEntry value) {
        if (key == null || value == null) {
            throw new NullPointerException("key == null || value == null");
        }

        synchronized (this) {
            mSize++;
            ContentCacheEntry previous = map.put(key, value);

            if (previous != null) {
                mSize--;
            }
        }

        trimToSize(mMaxSize);
        return true;
    }

    private void trimToSize(int maxSize) {
        while (true) {
            String key;
            @SuppressWarnings("unused")
            ContentCacheEntry value;

            synchronized (this) {
                if (mSize < 0 || (map.isEmpty() && mSize != 0)) {
                    throw new IllegalStateException(getClass().getName() + ".sizeOf() is reporting inconsistent results!");
                }

                if (mSize <= maxSize || map.isEmpty()) {
                    break;
                }

                Map.Entry<String, ContentCacheEntry> toEvict = map.entrySet().iterator().next();

                if (toEvict == null) {
                    break;
                }

                key = toEvict.getKey();
                value = toEvict.getValue();

                map.remove(key);
                // size -= sizeOf(key, value);
                mSize--;
            }
        }
    }

    @Override
    public final boolean remove(String key) {
        if (key == null) {
            throw new NullPointerException("key == null");
        }

        synchronized (this) {
            ContentCacheEntry previous = map.remove(key);
            if (previous != null) {
                mSize--;
                return true;
            }
        }

        return false;
    }

    @Override
    public Collection<String> keys() {
        return new HashSet<String>(map.keySet());
    }

    @Override
    public void clear() {
        trimToSize(-1);
    }

    public int size (){
        return mSize;
    }

    @Override
    @SuppressLint("DefaultLocale")
    public synchronized final String toString() {
        return String.format("LruCache[maxSize=%d]", mMaxSize);
    }

    @Override
    public LinkedHashMap<String, ContentCacheEntry> getAll() {
        return null;
    }

    @Override
    public ArrayList<ContentCacheEntry> convertToList() {
        return new ArrayList<ContentCacheEntry>(map.values());
    }

    public LinkedHashMap<String, ContentCacheEntry> getMap () {
        return map;
    }

    public void setMap (Map<String, ContentCacheEntry> map) {
        clear();

        for (String key : map.keySet()) {
            ContentCacheEntry entry = map.get(key);
            put(key, entry);
        }
    }

}