package com.inka.playermango;

import com.inka.netsync.ui.SplashActivity;

public class SplashActivityEx extends SplashActivity {

	/***
	 * 인증이 끝난후에 돌아갈 페이지 Activity
	 * @return
	 */
	@Override
	protected Class<?> provideNextContentView (boolean hasPermission) {
		return (hasPermission) ? DrawerActivityEx.class : CertificationActivityEx.class;
	}

	/***
	 * 스플래쉬 로딩 딜레이 시간
	 * @return
	 */
	@Override
	protected int provideSplashDelayTime() {
		return R.integer.provider_default_splash_delay;
	}


	/**
	 * 스플래쉬 이미지
	 * @return
	 */
	@Override
	protected int provideSplashImageResource() {
		return R.drawable.c_splash_screen;
	}


	/***
	 * 스플래쉬 배경 컬러
	 * @return
	 */
	@Override
	protected int provideSplashBackgroundColorResource() {
		return R.color.provider_color_splash_background;
	}

}