package com.inka.netsync.model;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by birdgang on 2018. 3. 16..
 */

public class ListDrawerMenuEntry {

    private ArrayList<DrawerMenuEntry> items = null;

    public ListDrawerMenuEntry() {
        items = new ArrayList<>();
    }

    public boolean add (DrawerMenuEntry value) {
        return items.add(value);
    }

    public boolean hasDrawerMenuName (DrawerMenuEntry value) {
        boolean result = false;
        for (DrawerMenuEntry entry : items) {
            if (StringUtils.equals(entry.mTabTag, value.mTabTag)) {
                result = true;
            }
        }
        return result;
    }

    public boolean set (List<DrawerMenuEntry> values) {
        items.clear();
        return items.addAll(values);
    }

    public List<DrawerMenuEntry> getItems () {
        return items;
    }

    @Override
    public String toString() {
        return "ListDrawerMenuEntry{" +
                "items=" + items +
                '}';
    }

}
