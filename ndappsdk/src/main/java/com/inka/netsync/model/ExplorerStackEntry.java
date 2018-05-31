package com.inka.netsync.model;

import com.inka.netsync.data.cache.db.model.ContentCacheEntry;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Stack;

public class ExplorerStackEntry extends Stack<ContentCacheEntry> {

    @Override
    public ContentCacheEntry push(ContentCacheEntry item) {
        return super.push(item);
    }

    @Override
    public synchronized ContentCacheEntry pop() {
        return super.pop();
    }

    @Override
    public synchronized ContentCacheEntry get(int index) {
        ContentCacheEntry contentEntry = super.get(index);
        return contentEntry;
    }

    public synchronized ContentCacheEntry findContentByPath(String path) {
        for (int i=0; i< this.size(); i++) {
            ContentCacheEntry contentEntry = super.get(i);
            if (StringUtils.equals(path, contentEntry.getContentFilePath())) {
                return contentEntry;
            }
        }
        return null;
    }

    @Override
    public synchronized int size() {
        return super.size();
    }

    @Override
    public String toString() {
        return "ExplorerStackEntry{" +
                "capacityIncrement=" + capacityIncrement +
                ", elementCount=" + elementCount +
                ", elementData=" + Arrays.toString(elementData) +
                ", modCount=" + modCount +
                '}';
    }
}