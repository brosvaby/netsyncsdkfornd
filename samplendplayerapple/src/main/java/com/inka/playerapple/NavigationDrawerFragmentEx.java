package com.inka.playerapple;

import com.inka.netsync.common.utils.StringUtil;
import com.inka.netsync.logs.LogUtil;
import com.inka.netsync.model.DrawerMenuEntry;
import com.inka.netsync.ui.fragment.NavigationDrawerFragment;

import java.util.ArrayList;

/**
 * Created by birdgang on 2017. 4. 21..
 */
public class NavigationDrawerFragmentEx extends NavigationDrawerFragment {

    /***
     * 사이드 메뉴 구성
     * @return
     */
    @Override
    protected ArrayList<DrawerMenuEntry> provideDrawerListMenus () {
        int value = DrawerMenuEntry.STATE_DISABLE;

        boolean selectedStorage = isFirstRun();
        LogUtil.INSTANCE.info("birdgangfirstrun", "selectedStorage : " + selectedStorage);
        if (selectedStorage) {
            value = DrawerMenuEntry.STATE_ENABLE;
        } else {
            value = DrawerMenuEntry.STATE_DISABLE;
        }

        ArrayList<DrawerMenuEntry> arrSection = new ArrayList<DrawerMenuEntry>();

        arrSection.addAll(getStorageMenus());

        arrSection.add(new DrawerMenuEntry(DrawerMenuEntry.DrawerMenuType.SECTION.ordinal(), StringUtil.EMPTY, getString(R.string.drawermenu_section_function), -1, DrawerMenuEntry.STATE_DISABLE));
        arrSection.add(new DrawerMenuEntry(DrawerMenuEntry.DrawerMenuType.ITEM.ordinal(), DrawerMenuEntry.TAG_PLAYEDLIST, getString(R.string.drawermenu_title_playedlist), R.drawable.img_drawer_playedlist, value));
        arrSection.add(new DrawerMenuEntry(DrawerMenuEntry.DrawerMenuType.ITEM.ordinal(), DrawerMenuEntry.TAG_FAVORITE, getString(R.string.drawermenu_title_favorite), R.drawable.img_drawer_favorite, value));

        arrSection.add(new DrawerMenuEntry(DrawerMenuEntry.DrawerMenuType.SECTION.ordinal(), StringUtil.EMPTY, getString(R.string.drawermenu_section_information), -1, DrawerMenuEntry.STATE_DISABLE));
        arrSection.add(new DrawerMenuEntry(DrawerMenuEntry.DrawerMenuType.ITEM.ordinal(), DrawerMenuEntry.TAG_INFORMATION_SETTING, getString(R.string.drawermenu_title_setting), R.drawable.img_drawer_setting, value));
        arrSection.add(new DrawerMenuEntry(DrawerMenuEntry.DrawerMenuType.ITEM.ordinal(), DrawerMenuEntry.TAG_INFORMATION_HELP, getString(R.string.drawermenu_title_guide), R.drawable.img_drawer_help, value));

        return arrSection;
    }


}