package com.inka.playermango;

import com.inka.netsync.sd.ui.SDSearchActivity;
import com.inka.netsync.ui.fragment.BaseFragment;

public class SearchActivityEx extends SDSearchActivity {

    /**
     * 검색 목록 화면을 지정합니다.
     * @return
     */
    @Override
    protected BaseFragment provideSearchFragment() {
        return SearchFragmentEx.newInstance();
    }

}
