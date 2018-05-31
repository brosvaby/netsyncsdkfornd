package com.inka.netsync.ui.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.inka.netsync.R;
import com.inka.netsync.R2;
import com.inka.netsync.common.bus.ContentItemClickListener;
import com.inka.netsync.logs.LogUtil;
import com.inka.netsync.model.DrawerMenuEntry;
import com.inka.netsync.ui.mvppresenter.NavigationDrawerMvpPresenter;
import com.inka.netsync.ui.mvpview.NavigationDrawerMvpView;
import com.inka.netsync.view.adapter.ListDrawerAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by birdgang on 2017. 4. 20..
 */
public class NavigationDrawerFragment extends BaseFragment implements NavigationDrawerMvpView {

    private final String TAG = "NavigationDrawerFragment";

    @Inject
    NavigationDrawerMvpPresenter<NavigationDrawerMvpView> mPresenter;

    @BindView(R2.id.recycler_list)
    RecyclerView mRecyclerView;

    private ListDrawerAdapter mListDrawerAdapter = null;
    private ArrayList<DrawerMenuEntry> mDrawerMenuEntries = null;

    private int mSelectedPosition = 1;

    public static NavigationDrawerFragment newInstance() {
        Bundle args = new Bundle();
        NavigationDrawerFragment fragment = new NavigationDrawerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDrawerMenuEntries = new ArrayList<DrawerMenuEntry>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_drawer, container, false);

        getActivityComponent().inject(this);
        setUnBinder(ButterKnife.bind(this, view));
        mPresenter.onAttach(this);

        setUp(view);

        return view;
    }

    @Override
    public boolean onBackPressed() {
        return true;
    }

    @Override
    public void setUp(View view) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            view.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white));
            ContextCompat.getColor(getActivity(), R.color.white);
        } else {
            view.setBackgroundColor(getActivity().getResources().getColor(R.color.white));
        }

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mDrawerMenuEntries = provideDrawerListMenus();
        LogUtil.INSTANCE.info(TAG, "mDrawerMenuEntries size : " + mDrawerMenuEntries.size());

        mListDrawerAdapter = new ListDrawerAdapter(getActivity(), mDrawerMenuEntries, mContentItemClickListener);
        mRecyclerView.setAdapter(mListDrawerAdapter);
        mRecyclerView.setHasFixedSize(true);

    }

    @Override
    public void refleshContents() {
    }

    public List<DrawerMenuEntry> getStorageMenus() {
        return mPresenter.getListStorage();
    }

    public List<DrawerMenuEntry> getStorageMenusForSD () {
        return mPresenter.getListStorageForSD();
    }

    @Override
    public void onScanStarted() {
    }

    @Override
    public void onScaning() {
    }

    @Override
    public void onScanCompleated() {
        LogUtil.INSTANCE.info("birdgangcreatestorage", "NavigationDrawerFragment > onScanCompleated");
        if (null != mDrawerMenuEntries) {
            mDrawerMenuEntries.clear();
            mDrawerMenuEntries.addAll(provideDrawerListMenus());
        }

        if (null != mListDrawerAdapter) {
            mListDrawerAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onSDcardMountedEvent(String externalPath) {
        LogUtil.INSTANCE.info("birdgangbroadcast", "SDRecentlyFragment > onSDcardMountedEvent > externalPath : "  + externalPath);
    }

    @Override
    public void onSDcardEjectedEvent() {
    }

    public String getCurrentTab() {
        DrawerMenuEntry drawerMenuEntry = mDrawerMenuEntries.get(mSelectedPosition);
        return drawerMenuEntry.mTabTag;
    }

    public void setCurrentTab(String drawerTag) {
        mPresenter.setCurrentDrawerMenu(drawerTag, mDrawerMenuEntries);
    }

    public void updateDrawerMenu (String tag) {
        mListDrawerAdapter.updateDrawer(tag);
    }

    public ContentItemClickListener mContentItemClickListener = new ContentItemClickListener() {
        @Override
        public void onItemCategoryClick(View view) {}

        @Override
        public void onItemClick(View view) {
            try {
                String selectedMenuTag = (String) view.getTag();
                LogUtil.INSTANCE.info(TAG, "ContentItemClickListener > tag : " + selectedMenuTag);
                mPresenter.changeDrawerMenuState(selectedMenuTag, mDrawerMenuEntries);
                mListDrawerAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                LogUtil.INSTANCE.error(getClass().getSimpleName(), e);
            }
        }
    };


    protected boolean isFirstRun () {
        return mPresenter.isFirstRun();
    }

    @Override
    public void lastSelectedDrawerPosition(int position) {
        mSelectedPosition = position;
        mListDrawerAdapter.notifyDataSetChanged();
    }


    /**
     *
     * @return
     */
    protected ArrayList<DrawerMenuEntry> provideDrawerListMenus () {
        return new ArrayList<>();
    }

}