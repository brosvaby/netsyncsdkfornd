package com.inka.playermango;

import com.inka.netsync.model.DrawerMenuEntry;

/**
 * Created by birdgang on 2017. 4. 21..
 */
public class DrawerMenuEntryEx extends DrawerMenuEntry {

    public final static int STATE_DISABLE = 0;
    public final static int STATE_ENABLE = 1;

    public String mTabTag = "";
    public String mTitle = "";
    public int mResDrawable = -1;

    public boolean mHasSelected = false;

    public int mStateMenu = STATE_ENABLE;

    private int drawerMenuType;

    public DrawerMenuEntryEx(int type, String tabTag, String title, int resDrawable, int state) {
        super(type, tabTag, title, resDrawable, state);

        drawerMenuType = type;
        mTabTag = tabTag;
        mTitle = title;
        mResDrawable = resDrawable;
        mStateMenu = state;
    }

    public int getDrawerMenuType() {
        return drawerMenuType;
    }

    public void setDrawerMenuType(int drawerMenuType) {
        this.drawerMenuType = drawerMenuType;
    }

    public int getStateMenu() {
        return mStateMenu;
    }

    public void setStateMenu(int stateMenu) {
        this.mStateMenu = stateMenu;
    }

    public boolean isHasSelected() {
        return mHasSelected;
    }

    public void setHasSelected(boolean hasSelected) {
        this.mHasSelected = hasSelected;
    }

}