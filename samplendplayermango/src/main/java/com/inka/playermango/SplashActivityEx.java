package com.inka.playermango;

import com.inka.netsync.ui.SplashActivity;

/**
 * 어플리케이션 구동 시 초기 화면 입니다.
 */
public class SplashActivityEx extends SplashActivity {

	/***
	 * 인증 성공시 다음에 보여질 화면을 지정합니다.
	 * @return
	 */
	@Override
	protected Class<?> provideNextContentView (boolean hasPermission) {
		return (hasPermission) ? DrawerActivityEx.class : CertificationActivityEx.class;
	}

	/***
	 * 화면 유지 시간을 지정 합니다.
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