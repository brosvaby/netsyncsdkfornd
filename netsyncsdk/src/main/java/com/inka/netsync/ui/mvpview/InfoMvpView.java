package com.inka.netsync.ui.mvpview;

import com.inka.netsync.view.model.HelpInfoGroupViewEntry;

import java.util.List;

public interface InfoMvpView extends MvpView {
    public void onLoadListInfo(List<HelpInfoGroupViewEntry> responseList);
}