package com.inka.playerapple;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.inka.netsync.ui.SearchActivity;
import com.inka.netsync.ui.fragment.BaseFragment;

public class SearchActivityEx extends SearchActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected BaseFragment provideSearchFragment() {
        return SearchFragmentEx.newInstance();
    }

}