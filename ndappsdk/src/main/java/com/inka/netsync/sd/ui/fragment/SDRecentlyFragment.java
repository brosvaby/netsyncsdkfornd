package com.inka.netsync.sd.ui.fragment;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.inka.ncg.nduniversal.hidden.Certification;
import com.inka.ncg.nduniversal.hidden.CertificationHelper;
import com.inka.ncg2.Ncg2Exception;
import com.inka.netsync.BaseConfiguration;
import com.inka.netsync.R;
import com.inka.netsync.R2;
import com.inka.netsync.common.AppConstants;
import com.inka.netsync.common.bus.ContentItemClickListener;
import com.inka.netsync.common.bus.EventBus;
import com.inka.netsync.controler.ContentControler;
import com.inka.netsync.controler.RecentlyPlayedListControler;
import com.inka.netsync.controler.SettingControler;
import com.inka.netsync.logs.LogUtil;
import com.inka.netsync.model.AddLicenseEntry;
import com.inka.netsync.model.ContentEntry;
import com.inka.netsync.ncg.model.PlayerEntry;
import com.inka.netsync.sd.ui.mvppresenter.SDExplorerRecentlyMvpPresenter;
import com.inka.netsync.sd.ui.mvpview.SDExplorerRecentlyMvpView;
import com.inka.netsync.ui.DrawerActivity;
import com.inka.netsync.ui.fragment.BaseFragment;
import com.inka.netsync.view.ProgressManagerClient;
import com.inka.netsync.view.RecyclerHasEmptyView;
import com.inka.netsync.view.adapter.ListRecentlyPlayedAdapter;
import com.inka.netsync.view.meterial.DividerItemDecoration;
import com.inka.netsync.view.model.RecentlyViewEntry;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SDRecentlyFragment extends BaseFragment implements SDExplorerRecentlyMvpView {

    private final String TAG = "SDRecentlyFragment";

    @Inject
    SDExplorerRecentlyMvpPresenter<SDExplorerRecentlyMvpView> mPresenter;

    protected RecyclerHasEmptyView mRecyclerHasEmptyView;

    protected RecyclerView mRecyclerView;

    @BindView(R2.id.btn_floating_action)
    FloatingActionButton mFloatingActionButton;

    @BindView(R2.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    protected List<RecentlyViewEntry> mRecentlyViewEntries = null;
    protected ListRecentlyPlayedAdapter mListRecentlyPlayedAdapter = null;

    protected Certification mCertification = null;

    protected static String mSortType = AppConstants.LIST_ORDER_PLAYDATE_DES;

    protected boolean isItemClick = false;
    private boolean mDidAnimatid = true;

    private int mContentId;
    protected int mColorForReflesh;

    private File mFile = null;

    public static SDRecentlyFragment newInstance(String tagKey, String tag, String nameKey, String name) {
        Bundle args = new Bundle();
        args.putString(tagKey, tag);
        args.putString(nameKey, name);
        SDRecentlyFragment fragment = new SDRecentlyFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        mPlayType = AppConstants.TYPE_PLAY_PLAYEDLIST;
        super.onCreate(savedInstanceState);
        EventBus.getDefault().registerEventUpdateContentListener(onEventUpdateContentListener);
        mCertification = CertificationHelper.getDefault().initCertification(getActivity(), Build.VERSION.SDK_INT, BaseConfiguration.getDefault().getStrCardManufacturer());
        mRecentlyViewEntries = new ArrayList<RecentlyViewEntry>();
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_container_view, container, false);

        getActivityComponent().inject(this);
        setUnBinder(ButterKnife.bind(this, view));
        mPresenter.onAttach(this);

        setUp(view);

        return view;
    }


    @Override
    public void setUp(View view) {
        mRecentlyViewEntries.clear();

        List<RecentlyViewEntry> recentlyViewEntries = RecentlyPlayedListControler.getDefault().loadRecentlyViewEntries();
        mRecentlyViewEntries.addAll(recentlyViewEntries);
        LogUtil.INSTANCE.info(TAG, "setUp > mRecentlyViewEntries : " + mRecentlyViewEntries.size());

        final int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            view.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white));
            ContextCompat.getColor(getActivity(), R.color.white);
        } else {
            view.setBackgroundColor(getActivity().getResources().getColor(R.color.white));
        }

        mRecyclerHasEmptyView = (RecyclerHasEmptyView) view.findViewById(R.id.recycler_has_empty_list_view);
        mRecyclerView = mRecyclerHasEmptyView.getRecyclerView();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        mListRecentlyPlayedAdapter = new ListRecentlyPlayedAdapter(getActivity(), mRecentlyViewEntries, mContentItemClickListener);
        mRecyclerView.setAdapter(mListRecentlyPlayedAdapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx,int dy){
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    boolean isShown = mFloatingActionButton.isShown();
                    if (!isShown) {
                        mFloatingActionButton.show();
                    }
                }
                else if (dy < 0) {
                    boolean isShown = mFloatingActionButton.isShown();
                    if (isShown) {
                        mFloatingActionButton.hide();
                    }
                }
            }
        });

        mFloatingActionButton.hide();
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mRecyclerView.scrollToPosition(0);
                    mFloatingActionButton.hide();
                } catch (Exception e) {
                    LogUtil.INSTANCE.error(TAG, e);
                }
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (null == mRecentlyViewEntries) {
                    return;
                }

                try {
                    mRecentlyViewEntries.clear();
                    List<RecentlyViewEntry> recentlyViewEntries = RecentlyPlayedListControler.getDefault().loadRecentlyViewEntries();
                    mRecentlyViewEntries.addAll(recentlyViewEntries);
                    mListRecentlyPlayedAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    LogUtil.INSTANCE.error(TAG, e);
                } finally {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }
        });

        int count = mListRecentlyPlayedAdapter.getItemCount();
        if (count > 0) {
            mRecyclerHasEmptyView.success();
        } else {
            mRecyclerHasEmptyView.empty();
            mRecyclerHasEmptyView.setImgEmpthVisisble(View.GONE);
            mRecyclerHasEmptyView.setTextEmpth(getString(R.string.text_recently_played_empty_message));
        }
    }


    public ContentItemClickListener mContentItemClickListener = new ContentItemClickListener() {
        @Override
        public void onItemCategoryClick(View view) {
        }

        @Override
        public void onItemClick(View view) {
            try {
                contentItemClick(view);
            } catch (Exception e) {
                LogUtil.INSTANCE.error(getClass().getSimpleName(), e);
            }
        }
    };


    @Override
    public void contentItemClick(final View view) {
        try {
            int position = (int) view.getTag();

            RecentlyViewEntry recentlyViewEntry = mRecentlyViewEntries.get(position);
            if (null == recentlyViewEntry || 0 >= recentlyViewEntry.getContentId()) {
                isItemClick = false;
                return;
            }

            String path = recentlyViewEntry.getContentPath();
            LogUtil.INSTANCE.info("birdgangclickevent", "onItemClick > path : " + path);
            boolean availableContent = checkAvailableContent(path);
            if (!availableContent) {
                isItemClick = false;
                return;
            }

            ContentControler.getDefault().setRecentlyPlaylistFromView(mRecentlyViewEntries);
            ContentEntry detailInfo = ContentControler.getDefault().findContentByFilePath(recentlyViewEntry.getContentPath());
            contentItemClick(detailInfo);
        } catch (Exception e) {
            LogUtil.INSTANCE.error(getClass().getSimpleName(), e);
            isItemClick = false;
        }
    }


    public synchronized void contentItemClick (ContentEntry detailInfo) throws Exception {
        try {
            int contentId = detailInfo.getContentId();
            String mediaType = detailInfo.getMediaType();
            String path = detailInfo.getContentFilePath();
            LogUtil.INSTANCE.info(TAG, "mContentItemClickListener > contentId : " + contentId + " , mediaType : " + mediaType + " , path : " + path);

            mPresenter.checkLicenseValid(getActivity(), detailInfo);
        } catch (Exception e) {
            isItemClick = false;
            LogUtil.INSTANCE.error(getClass().getSimpleName(), e);
        }
    }


    @Override
    public void onLoadOfflineAuthenticationDialog(int contentId, File file) {
    }

    @Override
    public void onLoadToastMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestLicense(int contentId, File file) {
        try {
            mPresenter.requestAddLicense(contentId, file);
        } catch (Ncg2Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
    }

    @Override
    public void onPrepareExecutePlay(AddLicenseEntry addLicenseEntry) {
        try {
            ContentEntry contentEntry = ContentControler.getDefault().findContentByFilePath(addLicenseEntry.getContentFile());
            LogUtil.INSTANCE.info("birdgangacquirelicense", " contentEntry : " + contentEntry.toString());
            if (null == contentEntry || StringUtils.isBlank(contentEntry.getContentFilePath())) {
                return;
            }

            int contentId = contentEntry.getContentId();
            String filePath = contentEntry.getContentFilePath();

            boolean isUpdatedLicenseInfo = ContentControler.getDefault().updateContentLicenseInfo(contentId, filePath);
            if (!isUpdatedLicenseInfo) {
                LogUtil.INSTANCE.info("birdgangacquirelicense", " onPostExecute" + getActivity().getString(R.string.license_invalid_license_info));
            }

            final String playerType = SettingControler.getDefault().getPlayerType();
            final String swXPlay = SettingControler.getDefault().getSwXPlay();
            final boolean shownPlayerGuide = SettingControler.getDefault().getShownPlayerGuide();

            PlayerEntry playerEntry = new PlayerEntry();
            playerEntry.setFilePath(filePath);
            playerEntry.setPlayerType(playerType);
            playerEntry.setPlayType(AppConstants.TYPE_PLAY_DOWNLOAD);
            playerEntry.setSwXPlay(swXPlay);
            playerEntry.setShownPlayerGuide(shownPlayerGuide);

            mPresenter.requestPrepareLicenseAndExecutePlayer(getActivity(), playerEntry);
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);

        }
    }

    @Override
    public void onResponseCheckLicenseValid(int contentId, File file) {
        mPresenter.checkSDLicense(getActivity(), contentId, file, mCertification);
    }

    @Override
    public void onLoadPlaybackActivity(PlayerEntry playerEntry) {
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        FragmentActivity activity = getActivity();
        if (activity instanceof DrawerActivity) {
            // 슬라이드 메뉴가 열려 있다면
            if (((DrawerActivity)activity).isDrawerOpen() == true) {
                return;
            }
        }
        restoreActionBar();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().removeEventUpdateContentListener(onEventUpdateContentListener);
    }

    @Override
    public boolean onBackPressed() {
        if (null != mFloatingActionButton) {
            mFloatingActionButton.hide();
        }
        return true;
    }

    @Override
    public void refleshContents() {
    }

    @Override
    public void onScanStarted() {
    }

    @Override
    public void onScaning() {
    }

    @Override
    public void onScanCompleated() {
        try {
            ProgressManagerClient.getInstance().stopProgress(getActivity());
            Handler handler = new Handler();
            handler.postDelayed(mRunUpdataListData, 1000);
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
    }

    @Override
    public void onSDcardMountedEvent(String externalPath) {
        try {
            Handler handler = new Handler();
            handler.postDelayed(mRunUpdataMountedEvent, 2000);
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
    }
    
    @Override
    public void onSDcardEjectedEvent() {
        try {
            Handler handler = new Handler();
            handler.postDelayed(mRunUpdataEjectedEvent, 1000);
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
    }


    public void sortListData(String sortType) {
        if (sortType == AppConstants.LIST_ORDER_PLAYDATE_ASC) {
            Collections.sort(mRecentlyViewEntries, RecentlyViewEntry.PlayDateComparatorAsc);
        } else if(sortType == AppConstants.LIST_ORDER_PLAYDATE_DES) {
            Collections.sort(mRecentlyViewEntries, RecentlyViewEntry.PlayDateComparatorDes);
        }
    }

    private Runnable mRunUpdataListData = new Runnable() {
        @Override
        public void run() {
            try {
                refleshContents();
            } catch (Exception e) {
                LogUtil.INSTANCE.error(TAG, e);
            }
        }
    };


    private Runnable mRunUpdataEjectedEvent = new Runnable() {
        @Override
        public void run() {
            try {
//                ((DrawerActivity)getActivity()).goHome();
            } catch (Exception e) {
                LogUtil.INSTANCE.error(TAG, e);
            }
        }
    };


    private Runnable mRunUpdataMountedEvent = new Runnable() {
        @Override
        public void run() {
            try {
                refleshContents();
            } catch (Exception e) {
                LogUtil.INSTANCE.error(TAG, e);
            }
        }
    };


    protected EventBus.onEventUpdateContentListener onEventUpdateContentListener = new EventBus.onEventUpdateContentListener() {
        @Override
        public void onRefleshContents() {
        }

        @Override
        public void onUpdateFavoriteExplorerContentList(String playType, int contentId) {
            if (StringUtils.equals(playType, AppConstants.TYPE_PLAY_PLAYEDLIST)) {
                mRecentlyViewEntries.clear();
                List<RecentlyViewEntry> recentlyViewEntries = RecentlyPlayedListControler.getDefault().loadRecentlyViewEntries();
                mRecentlyViewEntries.addAll(recentlyViewEntries);
                sortListData(mSortType);
                mListRecentlyPlayedAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onUpdateLMSExplorerContentList(String playType) {
            LogUtil.INSTANCE.info("birdgangnotify" , "onEventUpdateContentListener > onUpdateLMSExplorerContentList > playType : " + playType);
        }

        @Override
        public void onListOrderContents(String type) {
            try {
                mRecentlyViewEntries.clear();
                List<RecentlyViewEntry> recentlyViewEntries = RecentlyPlayedListControler.getDefault().loadRecentlyViewEntries();
                mRecentlyViewEntries.addAll(recentlyViewEntries);
                sortListData(mSortType);
                mListRecentlyPlayedAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                LogUtil.INSTANCE.error(TAG, e);
            }
        }
    };

}
