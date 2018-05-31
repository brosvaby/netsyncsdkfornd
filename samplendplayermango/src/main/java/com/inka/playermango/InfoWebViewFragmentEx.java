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
	 *
	 * @return
	 */
	@Override
	protected boolean provideWideViewPort() {
		return true;
	}
}
