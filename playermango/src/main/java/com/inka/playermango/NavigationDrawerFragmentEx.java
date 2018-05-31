package com.inka.playermango;

import com.inka.netsync.model.DrawerMenuEntry;
import com.inka.netsync.ui.fragment.NavigationDrawerFragment;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

/**
 * Created by birdgang on 2017. 4. 21..
 */
public class NavigationDrawerFragmentEx extends NavigationDrawerFragment {

    /***
     * Drawer 메뉴 목록 구성
     * @return
     */
    @Override
    protected ArrayList<DrawerMenuEntry> provideDrawerListMenus () {
        int value = DrawerMenuEntry.STATE_DISABLE;

        int selectedStorage = 1;
        if (selectedStorage <= -1) {
            value = DrawerMenuEntry.STATE_DISABLE;
        } else {
            value = DrawerMenuEntry.STATE_ENABLE;
        }

        ArrayList<DrawerMenuEntry> arrSection = new ArrayList<DrawerMenuEntry>();

        arrSection.add(new DrawerMenuEntry(DrawerMenuEntry.DrawerMenuType.ITEM.ordinal(), DrawerMenuEntry.TAG_WEBVIEW, getString(R.string.drawermenu_title_home), R.drawable.img_drawer_home, DrawerMenuEntry.STATE_ENABLE));

        arrSection.addAll(getStorageMenusForSD());

        arrSection.add(new DrawerMenuEntry(DrawerMenuEntry.DrawerMenuType.SECTION.ordinal(), StringUtils.EMPTY, StringUtils.EMPTY, -1, DrawerMenuEntry.STATE_DISABLE));
        arrSection.add(new DrawerMenuEntry(DrawerMenuEntry.DrawerMenuType.ITEM.ordinal(), DrawerMenuEntry.TAG_PLAYEDLIST, getString(R.string.drawermenu_title_playedlist), R.drawable.img_drawer_playedlist, value));
        arrSection.add(new DrawerMenuEntry(DrawerMenuEntry.DrawerMenuType.ITEM.ordinal(), DrawerMenuEntry.TAG_FAVORITE, getString(R.string.drawermenu_title_favorite), R.drawable.img_drawer_favorite, value));

        arrSection.add(new DrawerMenuEntry(DrawerMenuEntry.DrawerMenuType.SECTION.ordinal(), StringUtils.EMPTY, StringUtils.EMPTY, -1, DrawerMenuEntry.STATE_DISABLE));
        arrSection.add(new DrawerMenuEntry(DrawerMenuEntry.DrawerMenuType.ITEM.ordinal(), DrawerMenuEntry.TAG_INFORMATION_SETTING, getString(R.string.drawermenu_title_setting), R.drawable.img_drawer_setting, value));
        arrSection.add(new DrawerMenuEntry(DrawerMenuEntry.DrawerMenuType.ITEM.ordinal(), DrawerMenuEntry.TAG_INFORMATION_HELP, getString(R.string.drawermenu_title_help), R.drawable.img_drawer_help, value));

        return arrSection;
    }

}