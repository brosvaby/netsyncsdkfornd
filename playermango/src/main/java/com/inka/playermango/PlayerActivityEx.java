package com.inka.playermango;

import com.inka.netsync.ncg.NetSyncSdkHelper;
import com.inka.netsync.ui.PlayerActivity;

public class PlayerActivityEx extends PlayerActivity {

	/**
	 * 플레이어 상단 로고를 지정 합니다.
	 * @return
	 */
	@Override
	protected int provideCompanyLogo() {
		return R.drawable.img_companylogo;
	}


	/**
	 * 플레이어 종료시 라이센스를 해제 합니다.
	 */
	@Override
	protected void onDestroy() {
		NetSyncSdkHelper.getDefault().removeLicenseAllCID();
		super.onDestroy();
	}

}