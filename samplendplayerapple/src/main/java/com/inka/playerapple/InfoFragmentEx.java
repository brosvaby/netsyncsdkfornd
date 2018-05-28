package com.inka.playerapple;

import android.os.Bundle;

import com.inka.netsync.ui.fragment.InfoFragment;

/**
 * Created by birdgang on 2018. 2. 9..
 */
public class InfoFragmentEx extends InfoFragment {

    public static InfoFragmentEx newInstance(String fragmentTag_key, String fragmentTag, String fragmentName_key, String fragmentName) {
        InfoFragmentEx newFragment = new InfoFragmentEx();
        Bundle args = new Bundle();
        args.putString(fragmentTag_key, fragmentTag);
        args.putString(fragmentName_key, fragmentName);
        newFragment.setArguments(args);
        return newFragment;
    }

}
