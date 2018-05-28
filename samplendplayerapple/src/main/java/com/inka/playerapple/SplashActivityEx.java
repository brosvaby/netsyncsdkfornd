package com.inka.playerapple;

import com.inka.ncg.nduniversal.exception.Ncg2CoreException;
import com.inka.netsync.logs.LogUtil;
import com.inka.netsync.ui.SplashActivity;

public class SplashActivityEx extends SplashActivity {

	@Override
	protected void setNextContentView() throws Ncg2CoreException {
		boolean hasRunIntroGuide = hasRunIntroGuide();
		LogUtil.INSTANCE.info("birdgangauth", "setNextContentView > hasRunIntroGuide : " + hasRunIntroGuide);
		mNextClz = provideNextContentView(hasRunIntroGuide);
	}

	/***
	 * 인증이 끝난후에 돌아갈 페이지 Activity
	 * @return
	 */
	@Override
	protected Class<?> provideNextContentView (boolean hasNext) {
		return (hasNext) ? DrawerActivityEx.class : GuideActivityEx.class;
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