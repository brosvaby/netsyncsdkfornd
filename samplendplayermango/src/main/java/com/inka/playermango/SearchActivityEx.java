package com.inka.playermango;

import com.inka.netsync.sd.ui.SDSearchActivity;
import com.inka.netsync.ui.fragment.BaseFragment;

public class SearchActivityEx extends SDSearchActivity {

    @Override
    protected BaseFragment provideSearchFragment() {
        return SearchFragmentEx.newInstance();
    }

}
