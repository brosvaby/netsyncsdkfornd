package com.inka.netsync.sd.ui.fragment;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.inka.ncg.nduniversal.hidden.Certification;
import com.inka.ncg.nduniversal.hidden.CertificationHelper;
import com.inka.ncg2.Ncg2Exception;
import com.inka.netsync.BaseConfiguration;
import com.inka.netsync.R;
import com.inka.netsync.R2;
import com.inka.netsync.common.AppConstants;
import com.inka.netsync.common.ExplorerConstants;
import com.inka.netsync.common.bus.ContentItemClickListener;
import com.inka.netsync.common.bus.EventBus;
import com.inka.netsync.controler.ContentControler;
import com.inka.netsync.controler.SettingControler;
import com.inka.netsync.logs.LogUtil;
import com.inka.netsync.model.AddLicenseEntry;
import com.inka.netsync.model.ContentEntry;
import com.inka.netsync.ncg.model.PlayerEntry;
import com.inka.netsync.sd.ui.mvppresenter.SDExplorerSearchMvpPresenter;
import com.inka.netsync.sd.ui.mvpview.SDExplorerSearchMvpView;
import com.inka.netsync.ui.fragment.BaseFragment;
import com.inka.netsync.view.RecyclerHasEmptyView;
import com.inka.netsync.view.adapter.ListSearchExplorerAdapter;
import com.inka.netsync.view.meterial.DividerItemDecoration;
import com.inka.netsync.view.model.ContentViewEntry;
import com.inka.netsync.view.widget.EditTextWithClearButtonWidget;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SDSearchFragment extends BaseFragment implements SDExplorerSearchMvpView {

    private final String TAG = "SDSearchFragment";

    @Inject
    SDExplorerSearchMvpPresenter<SDExplorerSearchMvpView> mPresenter;

    private RecyclerHasEmptyView mRecyclerHasEmptyView;

    private RecyclerView mRecyclerView;

    @BindView(R2.id.btn_floating_action)
    FloatingActionButton mFloatingActionButton;

    private InputMethodManager mInputMethodManager;

    public ActionBar mActionBar;

    private EditTextWithClearButtonWidget mEditTextWithClearButtonWidget = null;

    protected ListSearchExplorerAdapter mListSearchExplorerAdapter = null;

    protected List<ContentViewEntry> mContentEntries = null;

    protected boolean isItemClick = false;

    public String mAlbumType = AppConstants.LIST_TYPE_FOLDER;

    protected Certification mCertification = null;

    protected String mStrLastSelectedContentPath = "";

    public static SDSearchFragment newInstance() {
        Bundle args = new Bundle();
        SDSearchFragment fragment = new SDSearchFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        mPlayType = AppConstants.TYPE_PLAY_DOWNLOAD;
        super.onCreate(savedInstanceState);
        EventBus.getDefault().registerEventUpdateContentListener(onEventUpdateContentListener);

        mContentEntries = new ArrayList<>();
        setHasOptionsMenu(true);
        mStrLastSelectedContentPath = ExplorerConstants.lastSelectedContentPath;
        mCertification = CertificationHelper.getDefault().initCertification(getActivity(), Build.VERSION.SDK_INT, BaseConfiguration.getDefault().getStrCardManufacturer());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sd_search, container, false);

        getActivityComponent().inject(this);
        setUnBinder(ButterKnife.bind(this, view));
        mPresenter.onAttach(this);

        setUp(view);

        return view;
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

        mRecyclerHasEmptyView = (RecyclerHasEmptyView) view.findViewById(R.id.recycler_has_empty_list_view);
        mRecyclerView = mRecyclerHasEmptyView.getRecyclerView();
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


        try {
            restoreActionBar();
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
    }


    @Override
    protected void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.btn_search_back);
        View customView = LayoutInflater.from(getActivity()).inflate(R.layout.view_actionbar_search, null);
        ImageView imgSearch = (ImageView) customView.findViewById(R.id.img_search);
        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    doSearch((null == mEditTextWithClearButtonWidget) ? StringUtils.EMPTY : mEditTextWithClearButtonWidget.getText());
                } catch (Exception e) {
                    LogUtil.INSTANCE.error(TAG, e);
                }
            }
        });

        mEditTextWithClearButtonWidget = (EditTextWithClearButtonWidget) customView.findViewById(R.id.edit_search_keyword);
        mEditTextWithClearButtonWidget.setImeOptionSearch();
        mEditTextWithClearButtonWidget.setOnDoneClickListener(new EditTextWithClearButtonWidget.OnDoneClickListener() {
            @Override
            public void onDoneClicked(String keywoard) {
                try {
                    doSearch(keywoard);
                } catch (Exception e) {
                    LogUtil.INSTANCE.error(TAG, e);
                }
            }
        });

        mInputMethodManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        mInputMethodManager.showSoftInputFromInputMethod((mEditTextWithClearButtonWidget.getEditText()).getWindowToken(), InputMethodManager.SHOW_FORCED);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        actionBar.setCustomView(customView);
        Toolbar parent = (Toolbar) customView.getParent();
        parent.setContentInsetsAbsolute(0, 0);

        ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        actionBar.setCustomView(customView, params);
    }


    @Override
    public boolean onBackPressed() {
        if (null != mFloatingActionButton) {
            mFloatingActionButton.hide();
        }
        return false;
    }

    public ContentItemClickListener mContentItemClickListener = new ContentItemClickListener() {
        @Override
        public void onItemCategoryClick(View view) {}

        @Override
        public void onItemClick(View view) {
            try {
                int position = (Integer) view.getTag();
                ContentViewEntry detailInfo = mContentEntries.get(position);
                ContentControler.getDefault().setPlaylistFromView(mContentEntries);
                ContentEntry contentEntry = new ContentEntry(detailInfo);
                String path = contentEntry.getContentFilePath();
                LogUtil.INSTANCE.info("birdgangclickevent", "onItemClick > path : " + path);
                boolean availableContent = checkAvailableContent(path);
                if (!availableContent) {
                    isItemClick = false;
                    return;
                }

                contentItemClick(contentEntry);
                LogUtil.INSTANCE.info("birdgangclickevent", "onItemClick > path : " + detailInfo.toString());
                mAlbumType = AppConstants.LIST_TYPE_FILE;
            } catch (Exception e) {
                LogUtil.INSTANCE.error(getClass().getSimpleName(), e);
            }
        }
    };


    public synchronized void contentItemClick (ContentEntry detailInfo) {
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


    protected void doSearch (String searchKeyword) throws Exception {
        ArrayList<ContentEntry> searchResults = ContentControler.getDefault().findListContentSearchKeyword(searchKeyword);
        LogUtil.INSTANCE.info("birdgangsearch" , "onDoneClicked() > searchResults.size() : " + searchResults.size());

        if (searchResults.size() > 0) {
            mRecyclerHasEmptyView.success();
        } else {
            mRecyclerHasEmptyView.empty();
            mRecyclerHasEmptyView.setImgEmpthVisisble(View.VISIBLE);
            mRecyclerHasEmptyView.setTextEmpth(getString(R.string.search_empty_message));
        }

        ArrayList<ContentViewEntry> contentViewEntries = new ArrayList<ContentViewEntry>();

        for (ContentEntry contentEntry : searchResults) {
            contentViewEntries.add(contentEntry.convertViewEntry());
        }

        mContentEntries.clear();
        mContentEntries.addAll(contentViewEntries);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        mListSearchExplorerAdapter = new ListSearchExplorerAdapter(getActivity(), mContentEntries, mContentItemClickListener);
        mRecyclerView.setAdapter(mListSearchExplorerAdapter);
        mRecyclerView.setHasFixedSize(true);

        mListSearchExplorerAdapter.notifyDataSetChanged();
        mInputMethodManager.hideSoftInputFromWindow(mEditTextWithClearButtonWidget.getWindowToken(), 0);
    }

    @Override
    public void onLoadOfflineAuthenticationDialog(int contentId, File file) {
    }

    @Override
    public void onLoadToastMessage(String message) {
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

    protected EventBus.onEventUpdateContentListener onEventUpdateContentListener = new EventBus.onEventUpdateContentListener() {
        @Override
        public void onRefleshContents() {
        }

        @Override
        public void onUpdateFavoriteExplorerContentList(String playType, int contentId) {
            LogUtil.INSTANCE.info("birdgangnotify" , "onEventUpdateContentListener > onUpdateFavoriteExplorerContentList > playType : " + playType + " , contentId : " + contentId);
        }

        @Override
        public void onUpdateLMSExplorerContentList(String playType) {
            LogUtil.INSTANCE.info("birdgangnotify" , "onEventUpdateContentListener > onUpdateLMSExplorerContentList > playType : " + playType);
            if (StringUtils.equals(AppConstants.TYPE_PLAY_SEARCH, playType)) {
                refleshContents();
            }
        }

        @Override
        public void onListOrderContents(String type) {
        }
    };


    @Override
    public void onDestroy() {
        EventBus.getDefault().removeEventUpdateContentListener(onEventUpdateContentListener);
        super.onDestroy();
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
    }

    @Override
    public void onSDcardMountedEvent(String externalPath) {
    }

    @Override
    public void onSDcardEjectedEvent() {
    }

}
