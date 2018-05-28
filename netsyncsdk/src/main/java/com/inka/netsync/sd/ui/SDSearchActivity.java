package com.inka.netsync.sd.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.inka.netsync.R;
import com.inka.netsync.R2;
import com.inka.netsync.sd.ui.fragment.SDSearchFragment;
import com.inka.netsync.ui.BaseActivity;
import com.inka.netsync.ui.mvppresenter.SearchContainerMvpPresenter;
import com.inka.netsync.ui.mvpview.SearchContainerMvpView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SDSearchActivity extends BaseActivity implements SearchContainerMvpView {

    private static final String TAG = "SDSearchActivity";

    @Inject
    SearchContainerMvpPresenter<SearchContainerMvpView> mPresenter;

    @BindView(R2.id.toolbar)
    Toolbar mToolbar;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_container);

        getActivityComponent().inject(this);
        setUnBinder(ButterKnife.bind(this));
        mPresenter.onAttach(SDSearchActivity.this);

        setUp();
    }


    @Override
    protected void setUp() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);

        SDSearchFragment searchFragment = (SDSearchFragment) provideSearchFragment();
        if (searchFragment == null) {
            return;
        }

        final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.fadein, R.anim.fadeout);
        String keyTagfragment = searchFragment.getArguments().getString(getString(R.string.key_tag_fragment));

        transaction.replace(R.id.content_frame, searchFragment, keyTagfragment);
        transaction.commitAllowingStateLoss();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
