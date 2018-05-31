package com.inka.netsync.ui.mvpview;

import com.inka.netsync.view.model.SingleCheckViewEntry;

import java.util.List;

/**
 * Created by birdgang on 2017. 4. 14..
 */
public interface SettingMvpView extends MvpView {
    void onLoadChangePlayerDialog (List<SingleCheckViewEntry> singleCheckViewEntries);
    void onLoadChangeDecoderDialog (List<SingleCheckViewEntry> singleCheckViewEntries);
}