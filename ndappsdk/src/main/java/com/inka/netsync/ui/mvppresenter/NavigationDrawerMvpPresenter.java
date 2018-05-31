package com.inka.netsync.ui.mvppresenter;

import com.inka.netsync.model.DrawerMenuEntry;
import com.inka.netsync.ui.mvpview.NavigationDrawerMvpView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by birdgang on 2017. 4. 21..
 */

public interface NavigationDrawerMvpPresenter<V extends NavigationDrawerMvpView> extends MvpPresenter<V> {
    void onViewInitialized();
    boolean isFirstRun ();
    void setFirstRun (boolean firstRun);
    List<DrawerMenuEntry> getListStorage ();
    List<DrawerMenuEntry> getListStorageForSD ();
    void setCurrentDrawerMenu (String selectedMenu, ArrayList<DrawerMenuEntry> drawerMenuEntries);
    void changeDrawerMenuState (String selectedMenu, ArrayList<DrawerMenuEntry> drawerMenuEntries);
}