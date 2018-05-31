package com.inka.netsync.media.bus;

/**
 * Created by birdgang on 2017. 12. 20..
 */

public interface DevicesDiscoveryCb {
    void onDiscoveryStarted(String entryPoint);
    void onDiscoveryProgress(String entryPoint);
    void onDiscoveryCompleted(String entryPoint);
    void onParsingStatsUpdated(int percent, String entryPoint);
    void onParsingStatsUpdatedSync(int percent, String entryPoint);
}
