package com.inka.netsync.data.cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;

/**
 * Created by birdgang on 2017. 4. 19..
 */
public interface MemoryCacheAware<K, V> {
    boolean put(K key, V value);
    V get(K key);
    LinkedHashMap<String, V> getAll();
    ArrayList<V> convertToList();
    boolean remove(K key);
    Collection<K> keys();
    void clear();
}