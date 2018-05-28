package com.inka.netsync.model;

import com.inka.netsync.data.network.model.BaseResponseEntry;

import java.util.List;

/**
 * Created by birdgang on 2017. 6. 22..
 */

public class ListExplorerEntry implements BaseResponseEntry {

    private List<ExplorerStackEntry> explorerStackEntries;

    public List<ExplorerStackEntry> getExplorerStackEntries() {
        return explorerStackEntries;
    }

    public void setExplorerStackEntries(List<ExplorerStackEntry> explorerStackEntries) {
        this.explorerStackEntries = explorerStackEntries;
    }
}