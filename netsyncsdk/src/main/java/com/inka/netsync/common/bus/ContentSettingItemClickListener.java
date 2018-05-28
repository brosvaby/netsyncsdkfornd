package com.inka.netsync.common.bus;

import android.view.View;

public interface ContentSettingItemClickListener {
    public void onItemSwitchClick(int id, boolean enable);
    public void onItemClick(View view);
}