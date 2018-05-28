package com.inka.playermango;

import com.inka.netsync.logs.LogUtil;
import com.inka.netsync.ui.GuideActivity;

public class GuideActivityEx extends GuideActivity {

	@Override
	protected void setNextContentView() {
		try {
			setNextContentView(DrawerActivityEx.class);
		} catch (Exception e) {
			LogUtil.INSTANCE.error("error", e.getMessage());
		}
	}

}