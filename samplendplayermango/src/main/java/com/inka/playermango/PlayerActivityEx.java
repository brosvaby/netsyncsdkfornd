package com.inka.playermango;

import com.inka.netsync.ncg.Ncg2SdkHelper;
import com.inka.netsync.ui.PlayerActivity;

public class PlayerActivityEx extends PlayerActivity {
	
	@Override
	protected int provideCompanyLogo() {
		return R.drawable.img_companylogo;
	}

	@Override
	protected void onDestroy() {
		Ncg2SdkHelper.getDefault().removeLicenseAllCID();
		super.onDestroy();
	}

}