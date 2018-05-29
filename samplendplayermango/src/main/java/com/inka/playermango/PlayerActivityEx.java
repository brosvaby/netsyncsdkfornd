package com.inka.playermango;

import com.inka.netsync.ncg.NetSyncSdkHelper;
import com.inka.netsync.ui.PlayerActivity;

public class PlayerActivityEx extends PlayerActivity {
	
	@Override
	protected int provideCompanyLogo() {
		return R.drawable.img_companylogo;
	}

	@Override
	protected void onDestroy() {
		NetSyncSdkHelper.getDefault().removeLicenseAllCID();
		super.onDestroy();
	}

}