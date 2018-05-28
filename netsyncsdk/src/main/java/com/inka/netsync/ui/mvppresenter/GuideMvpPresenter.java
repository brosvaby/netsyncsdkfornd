package com.inka.netsync.ui.mvppresenter;

import com.inka.netsync.ui.mvpview.GuideMvpView;
import com.inka.netsync.view.model.GuideViewEntry;

import java.util.List;

public interface GuideMvpPresenter<V extends GuideMvpView> extends MvpPresenter<V> {
    void onViewInitialized();
    void requestSetHasRunIntroGuide(boolean enable);
    List<GuideViewEntry> getGuideViewEntries ();
}