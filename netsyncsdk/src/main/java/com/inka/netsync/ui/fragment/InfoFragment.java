package com.inka.netsync.ui.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.inka.netsync.R;
import com.inka.netsync.R2;
import com.inka.netsync.common.bus.ClickListener;
import com.inka.netsync.logs.LogUtil;
import com.inka.netsync.ui.DrawerActivity;
import com.inka.netsync.ui.mvppresenter.InfoMvpPresenter;
import com.inka.netsync.ui.mvpview.InfoMvpView;
import com.inka.netsync.view.adapter.ListExpandableAdapter;
import com.inka.netsync.view.meterial.ObservableRecyclerView;
import com.inka.netsync.view.model.HelpInfoGroupViewEntry;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;


public class InfoFragment extends BaseFragment implements InfoMvpView {

    private static final String TAG = "InfoFragment";
    
    @Inject
    InfoMvpPresenter<InfoMvpView> mPresenter;

    @BindView(R2.id.recycler_list)
    ObservableRecyclerView mRecyclerView;

    protected MenuItem mMenuItemReflesh;
    protected MenuItem mMenuItemSearch;
    protected MenuItem mMenuItemOverflow;
    protected MenuItem mMenuItemSort;

    private ListExpandableAdapter mListExpandableAdapter;
    private List<HelpInfoGroupViewEntry> mHelpInfoGroupViewEntries;

    public static InfoFragment newInstance(String tagKey, String tag, String nameKey, String name) {
        Bundle args = new Bundle();
        args.putString(tagKey, tag);
        args.putString(nameKey, name);
        InfoFragment fragment = new InfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHelpInfoGroupViewEntries = new ArrayList<>();
        setHasOptionsMenu(true);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_infomation_view, container, false);

        getActivityComponent().inject(this);
        setUnBinder(ButterKnife.bind(this, view));
        mPresenter.onAttach(this);

        setUp(view);

        return view;
    }

    @Override
    public void setUp(View view) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);

        final int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            view.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white));
            ContextCompat.getColor(getActivity(), R.color.white);
        } else {
            view.setBackgroundColor(getActivity().getResources().getColor(R.color.white));
        }

        RecyclerView.ItemAnimator animator = mRecyclerView.getItemAnimator();
        if (animator instanceof DefaultItemAnimator) {
            ((DefaultItemAnimator) animator).setSupportsChangeAnimations(true);
        }

        mPresenter.requestListInfo();
    }


    /**
     * menu XML 을 inflate 하여 메뉴 항목을 생성한다.
     * menu event 관련 첫번째로 호출
     */
    @Override
    public void onCreateOptionsMenu(final Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        mMenuItemReflesh = menu.findItem(R.id.action_reflesh);
        mMenuItemSearch = menu.findItem(R.id.action_search);
        mMenuItemOverflow = menu.findItem(R.id.action_overflow);
        mMenuItemSort = menu.findItem(R.id.action_sort);

        if (((DrawerActivity)getActivity()).isDrawerOpen() == false) {
            inflater.inflate(R.menu.menu_mainscreen, menu);
            restoreActionBar();
        }
    }

    /**
     * inflate 된 menu 를 show or hide 및 icon 설정 등을 통해 세부 셋팅을 한다.
     * menu event 관련 두번째로 호출
     */
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        mMenuItemReflesh = menu.findItem(R.id.action_reflesh);
        mMenuItemSearch = menu.findItem(R.id.action_search);
        mMenuItemOverflow = menu.findItem(R.id.action_overflow);
        mMenuItemSort = menu.findItem(R.id.action_sort);

        FragmentActivity activity = getActivity();
        if (activity instanceof DrawerActivity) {
            if (((DrawerActivity)activity).isDrawerOpen()) {
                return;
            }
        }

        mMenuItemReflesh.setVisible(false);
        mMenuItemSort.setVisible(false);
        mMenuItemSearch.setVisible(false);
        mMenuItemOverflow.setVisible(false);
    }


    @Override
    public void refleshContents() {
    }

    @Override
    public void onResume() {
        super.onResume();
        setHasOptionsMenu(true);
    }


    public ClickListener mContentItemClickListener = new ClickListener() {
        @Override
        public void onItemClick(View view) {
            try {
            } catch (Exception e) {
                LogUtil.INSTANCE.error(getClass().getSimpleName(), e);
            }
        }
    };

    @Override
    public boolean isNetworkConnected() {
        return false;
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

    @Override
    public void onLoadListInfo(List<HelpInfoGroupViewEntry> responseList) {
        for (HelpInfoGroupViewEntry help : responseList) {
            mHelpInfoGroupViewEntries.add(help);
        }

        mListExpandableAdapter = new ListExpandableAdapter(mHelpInfoGroupViewEntries);
        mRecyclerView.setAdapter(mListExpandableAdapter);
        mListExpandableAdapter.notifyDataSetChanged();
    }

}
