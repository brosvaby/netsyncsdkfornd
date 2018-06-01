package com.inka.netsync.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.inka.netsync.R;
import com.inka.netsync.logs.LogUtil;
import com.inka.netsync.ui.mvppresenter.DefaultMvpPresenter;
import com.inka.netsync.ui.mvpview.DefaultMvpView;

import javax.inject.Inject;

import butterknife.ButterKnife;

public class DefaultFragment extends BaseFragment implements DefaultMvpView {

    private final String TAG = "DefaultFragment";

    protected MenuItem mMenuItemReflesh;

    @Inject
    DefaultMvpPresenter<DefaultMvpView> mPresenter;

    public static DefaultFragment newInstance() {
        Bundle args = new Bundle();
        DefaultFragment fragment = new DefaultFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sd_default, container, false);

        getActivityComponent().inject(this);
        setUnBinder(ButterKnife.bind(this, view));
        mPresenter.onAttach(this);

        setUp(view);

        return view;
    }

    @Override
    public void setUp(View view) {
        try {
            restoreActionBar();
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
    }

    @Override
    public void refleshContents() {
    }

    @SuppressLint({"RestrictedApi", "NewApi"})
    @Override
    protected void restoreActionBar() {
        try {
            ActionBar actionBar = getActionBar();
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setDefaultDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayUseLogoEnabled(false);
            actionBar.setHomeButtonEnabled(false);
            
            View customView = LayoutInflater.from(getActivity()).inflate(R.layout.view_actionbar_logo, null);
            actionBar.setCustomView(customView);
            Toolbar parent = (Toolbar) customView.getParent();
            parent.setPadding(0,0,0,0);
            parent.setContentInsetsAbsolute(0,0);

            ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
            actionBar.setCustomView(customView, params);
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
    }


    @Override
    public boolean onBackPressed() {
        return true;
    }

    @Override
    public void onScanStarted() {
    }

    @Override
    public void onScaning() {
    }

    @Override
    public void onScanCompleated() {
    }

    @Override
    public void onSDcardMountedEvent(String externalPath) {
    }

    @Override
    public void onSDcardEjectedEvent() {
    }

}
