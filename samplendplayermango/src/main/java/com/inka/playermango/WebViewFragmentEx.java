package com.inka.playermango;

import android.os.Bundle;

import com.inka.netsync.ui.fragment.WebViewFragment;

/**
 * WebView 화면 입니다.
 */
public class WebViewFragmentEx extends WebViewFragment {

	public static WebViewFragmentEx newInstance(String fragmentTag_key, String fragmentTag, String fragmentName_key, String fragmentName) {
		WebViewFragmentEx newFragment = new WebViewFragmentEx();
		Bundle args = new Bundle();
		args.putString(fragmentTag_key, fragmentTag);
		args.putString(fragmentName_key, fragmentName);
		newFragment.setArguments(args);
		return newFragment;
	}

}
