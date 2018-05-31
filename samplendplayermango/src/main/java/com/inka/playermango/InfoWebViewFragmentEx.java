package com.inka.playermango;

import android.os.Bundle;

import com.inka.netsync.ui.fragment.InfoWebViewFragment;

public class InfoWebViewFragmentEx extends InfoWebViewFragment {

	public static InfoWebViewFragmentEx newInstance(String fragmentTag_key, String fragmentTag, String fragmentName_key, String fragmentName ) {
		InfoWebViewFragmentEx newFragment = new InfoWebViewFragmentEx();
		Bundle args = new Bundle();
		args.putString(fragmentTag_key, fragmentTag);
		args.putString(fragmentName_key, fragmentName);
		newFragment.setArguments(args);
		return newFragment;
	}

	/**
	 * 웹뷰의 html 컨텐츠가 웹뷰보다 클 경우 스크린 크기에 맞게 조정되도록 한다.
	 * 웹뷰의 html의 viewport 메타 태그를 지원하게 한다.
	 * @return
	 */
	@Override
	protected boolean provideWideViewPort() {
		return true;
	}
}
