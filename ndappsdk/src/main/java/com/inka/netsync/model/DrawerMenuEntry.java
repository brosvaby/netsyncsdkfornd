package com.inka.netsync.model;

import com.inka.netsync.view.model.DrawerMenuViewEntry;

public class DrawerMenuEntry extends DrawerMenuViewEntry {

    /**
     *
     * @param type 섹션 or 메뉴
     * @param tabTag 태킹
     * @param title 메뉴 이름
     * @param resDrawable 메뉴 아이콘
     * @param state enable or disable
     */
    public DrawerMenuEntry(int type, String tabTag, String title, int resDrawable, int state) {
        super(type, tabTag, title, resDrawable, state);
    }

}
