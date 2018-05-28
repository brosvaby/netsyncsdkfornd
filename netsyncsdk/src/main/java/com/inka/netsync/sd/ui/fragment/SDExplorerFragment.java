package com.inka.netsync.sd.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.inka.ncg.nduniversal.hidden.Certification;
import com.inka.ncg2.Ncg2Exception;
import com.inka.netsync.BaseConfiguration;
import com.inka.netsync.R;
import com.inka.netsync.R2;
import com.inka.netsync.command.Command;
import com.inka.netsync.command.CommandHandler;
import com.inka.netsync.common.ActivityCalls;
import com.inka.netsync.common.AppConstants;
import com.inka.netsync.common.ExplorerConstants;
import com.inka.netsync.common.bus.ClickListener;
import com.inka.netsync.common.bus.ContentFavoriteClickListener;
import com.inka.netsync.common.bus.ContentItemClickListener;
import com.inka.netsync.common.bus.ContentItemLongClickListener;
import com.inka.netsync.common.bus.EventBus;
import com.inka.netsync.common.utils.StringUtil;
import com.inka.netsync.controler.ContentControler;
import com.inka.netsync.controler.FavoriteControler;
import com.inka.netsync.controler.SettingControler;
import com.inka.netsync.data.cache.db.model.ContentCacheEntry;
import com.inka.netsync.data.cache.pref.PreferencesCacheHelper;
import com.inka.netsync.logs.LogUtil;
import com.inka.netsync.media.MediaScan;
import com.inka.netsync.media.MediaStorage;
import com.inka.netsync.model.AddLicenseEntry;
import com.inka.netsync.model.ContentEntry;
import com.inka.netsync.model.ExplorerStackEntry;
import com.inka.netsync.model.ExplorerStackEntryList;
import com.inka.netsync.model.FavoriteEntry;
import com.inka.netsync.ncg.model.PlayerEntry;
import com.inka.netsync.sd.ui.mvppresenter.SDExplorerStackMvpPresenter;
import com.inka.netsync.sd.ui.mvpview.SDExplorerStackMvpView;
import com.inka.netsync.ui.DrawerActivity;
import com.inka.netsync.ui.fragment.BaseFragment;
import com.inka.netsync.view.RecyclerHasEmptyView;
import com.inka.netsync.view.adapter.ListExplorerAdapter;
import com.inka.netsync.view.adapter.ListNavigationPathAdapter;
import com.inka.netsync.view.dialog.CustomAlertDialog;
import com.inka.netsync.view.dialog.SingleChoiceAlertDialog;
import com.inka.netsync.view.meterial.DividerItemDecoration;
import com.inka.netsync.view.model.ContentViewEntry;
import com.inka.netsync.view.model.NavigationPathViewEntry;
import com.inka.netsync.view.model.SingleCheckViewEntry;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SDExplorerFragment extends BaseFragment implements SDExplorerStackMvpView {

    private final String TAG = "SDExplorerFragment";

    @Inject
    SDExplorerStackMvpPresenter<SDExplorerStackMvpView> mPresenter;

    @BindView(R2.id.btn_up)
    Button mBtnUp;

    @BindView(R2.id.container_navigater_content)
    LinearLayout mContainerNavigaterContent;

    @BindView(R2.id.recycler_horizontal_list)
    RecyclerView mRecyclerHorizontalView;

    @BindView(R2.id.btn_floating_action)
    FloatingActionButton mFloatingActionButton;

    @BindView(R2.id.recycler_has_empty_list_view)
    RecyclerHasEmptyView mRecyclerHasEmptyView;

    private RecyclerView mRecyclerView;

    protected LinearLayoutManager mRecyclerLayoutManager;

    protected MenuItem mMenuItemReflesh;
    protected MenuItem mMenuItemSearch;
    protected MenuItem mMenuItemOverflow;
    protected MenuItem mMenuItemSort;

    protected ListExplorerAdapter mListExplorerEternalAdapter = null;
    protected ListNavigationPathAdapter mListNavigationPathAdapter = null;

    protected List<ContentViewEntry> mContentEntries = null;
    protected List<NavigationPathViewEntry> mNavigationPathEntries;

    protected ExplorerStackEntryList mExplorerStackEntryList;

    protected boolean mEnableOptionMenus = false;

    public String mAlbumType = AppConstants.LIST_TYPE_FOLDER;

    protected Certification mCertification = null;

    protected String mStrStorageType = "";
    protected String mStrLastSelectedContentPath = "";


    public static SDExplorerFragment newInstance(String tagKey, String tag, String nameKey, String name) {
        Bundle args = new Bundle();
        args.putString(tagKey, tag);
        args.putString(nameKey, name);
        SDExplorerFragment fragment = new SDExplorerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        mPlayType = AppConstants.TYPE_PLAY_DOWNLOAD;
        super.onCreate(savedInstanceState);
        EventBus.getDefault().registerEventUpdateContentListener(onEventUpdateContentListener);

        mExplorerStackEntryList = new ExplorerStackEntryList();
        mExplorerStackEntryList.initDepth();

        mNavigationPathEntries = new ArrayList<>();
        mContentEntries = new ArrayList<>();

        setHasOptionsMenu(true);

        mStrLastSelectedContentPath = ExplorerConstants.lastSelectedContentPath;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sd_explorer, container, false);

        getActivityComponent().inject(this);
        setUnBinder(ButterKnife.bind(this, view));
        mPresenter.onAttach(this);

        setUp(view);

        return view;
    }


    @SuppressLint("NewApi")
    @Override
    public void setUp(View view) {
        Bundle args = getArguments();
        if (args != null && args.containsKey(MediaStorage.TYPE_STORAGE)) {
            mStrStorageType = args.getString(MediaStorage.TYPE_STORAGE);
        }

        final int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            view.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white));
            ContextCompat.getColor(getActivity(), R.color.white);
        } else {
            view.setBackgroundColor(getActivity().getResources().getColor(R.color.white));
        }

        mContainerNavigaterContent.setBackgroundColor(ContextCompat.getColor(getActivity(), provideNavigationBackgroundColor()));

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerHorizontalView.setLayoutManager(layoutManager);
        mListNavigationPathAdapter = new ListNavigationPathAdapter(getActivity(), mNavigationPathEntries, mNavigationPathItemClickListener);
        mRecyclerHorizontalView.setAdapter(mListNavigationPathAdapter);

        mRecyclerLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView = mRecyclerHasEmptyView.getRecyclerView();
        mRecyclerView.setLayoutManager(mRecyclerLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        mListExplorerEternalAdapter = new ListExplorerAdapter(getActivity(), mContentEntries, mContentItemClickListener, mContentItemLongClickListener, mContentFavoriteClickListener);
        mRecyclerView.setAdapter(mListExplorerEternalAdapter);
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

        int count = mListExplorerEternalAdapter.getItemCount();
        if (count > 0) {
            mRecyclerHasEmptyView.success();
        } else {
            mRecyclerHasEmptyView.empty();
            mRecyclerHasEmptyView.setImgEmpthVisisble(View.GONE);
            mRecyclerHasEmptyView.setTextEmpth(getString(R.string.text_empty_message));
        }

        try {
            restoreActionBar();
            requestGenerateExplorerStack();
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
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
            mEnableOptionMenus = true;
        } else {
            mEnableOptionMenus = false;
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

        if (mEnableOptionMenus) {
            mMenuItemReflesh.setVisible(true);
            mMenuItemSearch.setVisible(true);
            mMenuItemSort.setVisible(true);
            mMenuItemOverflow.setVisible(true);
        } else {
            mMenuItemReflesh.setVisible(false);
            mMenuItemSearch.setVisible(false);
            mMenuItemSort.setVisible(false);
            mMenuItemOverflow.setVisible(false);
        }
    }


    /**
     * 정렬 팝업
     */
    public void showChangeSortingDialog() {
        try {
            // origin source
            String[] strings = getResources().getStringArray(R.array.menu_sort_folder);

            int value = PreferencesCacheHelper.getPreferenceValueForInteger(PreferencesCacheHelper.SORT_NAME, PreferencesCacheHelper.SORT_NAME_ASC);
            LogUtil.INSTANCE.info("birdgangbasesettingdialog" , "showChangePlayerDialog > value : " + value);

            List<SingleCheckViewEntry> singleSelectViewEntries = new ArrayList<>();
            singleSelectViewEntries.add(new SingleCheckViewEntry(PreferencesCacheHelper.SORT_NAME_ASC, strings[0], (value == PreferencesCacheHelper.SORT_NAME_ASC) ? true : false));
            singleSelectViewEntries.add(new SingleCheckViewEntry(PreferencesCacheHelper.SORT_NAME_DESC, strings[1], (value == PreferencesCacheHelper.SORT_NAME_DESC) ? true : false));

            new SingleChoiceAlertDialog(getActivity())
                    .setDataSource(singleSelectViewEntries)
                    .setCanceled(true)
                    .setTitleText(getResources().getString(R.string.sort))
                    .setConfirmText(getString(R.string.dialog_ok))
                    .setConfirmClickListener(new SingleChoiceAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClickListContent(SingleChoiceAlertDialog sDialog) {
                            try {
                                int selectedId = sDialog.getSelectedId();
                                LogUtil.INSTANCE.info("birdgangbasesettingdialog" , "onClick > selectedId : " + selectedId);
                                PreferencesCacheHelper.setPreferenceValueForInteger(PreferencesCacheHelper.SORT_NAME, selectedId);
                                sortListData();
                            } catch (Exception e) {
                                LogUtil.INSTANCE.error(TAG, e);
                            } finally {
                                sDialog.cancel();
                            }
                        }

                        @Override
                        public void onClick(SingleChoiceAlertDialog sDialog) {
                            try {
                                int selectedId = sDialog.getSelectedId();
                                LogUtil.INSTANCE.info("birdgangbasesettingdialog" , "onClick > selectedId : " + selectedId);
                                PreferencesCacheHelper.setPreferenceValueForInteger(PreferencesCacheHelper.SORT_NAME, selectedId);
                                sortListData();
                            } catch (Exception e) {
                                LogUtil.INSTANCE.error(TAG, e);
                            } finally {
                                sDialog.cancel();
                            }
                        }
                    })
                    .show();
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
    }


    /**
     * menu item 이 선택 되었을때 발생한다.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_sort) {
            showChangeSortingDialog();
        }
        else if (itemId == R.id.action_search) {
//            requestSearchView();
            Intent intent = new Intent(getActivity(), provideSearchView());
            getActivity().startActivity(intent);
        }
        else if (itemId == R.id.action_reflesh) {
            MediaScan.getInstance().scanMediaItems();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void restoreActionBar() {
        super.restoreActionBar();
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onBackPressed() {
        if (null == mExplorerStackEntryList) {
            return true;
        }

        if (null != mFloatingActionButton) {
            mFloatingActionButton.hide();
        }

        try {
            int currentDepth = mExplorerStackEntryList.currentDepth();
            LogUtil.INSTANCE.info("birdgangnavigation", "ExternalSDExplorerFragment > onBackPressed > currentDepth : " + currentDepth);

            if (currentDepth <= 1) {
                return true;
            }
            CommandHandler commandHandler = new CommandHandler();
            commandHandler.send(new ExplorerNavigator(ExplorerNavigator.PREVIOUS));
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
        return false;
    }

    @OnClick(R2.id.btn_up)
    public void onClickUp () {
        boolean isRoot = onBackPressed();
        if (isRoot) {
            Toast.makeText(getActivity(), getActivity().getString(R.string.message_for_toast_view_navigation_root), Toast.LENGTH_LONG).show();
        }
    }

    @OnClick(R2.id.btn_home)
    public void onClickHome () {
        CommandHandler commandHandler = new CommandHandler();
        commandHandler.send(new ExplorerNavigator(ExplorerNavigator.HOME));
    }


    public ClickListener mNavigationPathItemClickListener = new ClickListener () {
        @Override
        public void onItemClick(View view) {
            try {
                int position = (Integer) view.getTag();
                NavigationPathViewEntry navigationPathViewEntry = mNavigationPathEntries.get(position);
                CommandHandler commandHandler = new CommandHandler();
                commandHandler.send(new ExplorerNavigator(ExplorerNavigator.DIRECT, navigationPathViewEntry.getDepth() + 1, navigationPathViewEntry.getPath()));
            } catch (Exception e) {
                LogUtil.INSTANCE.error(getClass().getSimpleName(), e);
            }
        }
    };



    public ContentFavoriteClickListener mContentFavoriteClickListener = new ContentFavoriteClickListener() {
        @Override
        public void onItemClick(boolean state, int contentId) {
            try {
                LogUtil.INSTANCE.debug("birdgangclicklistener", "mItemClickListener > contentId : " + contentId);
                ContentEntry existEntry = ContentControler.getDefault().findContentById(contentId);
                String existPlayDate = existEntry.getPlayDate();

                LogUtil.INSTANCE.info("birdgangclicklistener", "existEntry : " + existEntry.toString());
                if (StringUtils.isBlank(existPlayDate)) {
                    Toast.makeText(getActivity(), getActivity().getString(R.string.business_logic_use_favorite_after_certification), Toast.LENGTH_LONG).show();
                    mListExplorerEternalAdapter.updateContentFavorite(false, existEntry.convertViewEntry());
                    return;
                }

                if (!state) {
                    existEntry.setIsFavoriteContent(0);
                    long result = FavoriteControler.getDefault().deleteFavoriteByContentName(new FavoriteEntry(existEntry));
                    LogUtil.INSTANCE.info("birdgangmedia" , "deleteFavoriteByContentId > result : " + result);
                    Toast.makeText(getActivity(), getString(R.string.message_for_toast_favorite_content_no_use), Toast.LENGTH_SHORT).show();
                } else {
                    existEntry.setIsFavoriteContent(1);
                    long result = FavoriteControler.getDefault().addFavorite(new FavoriteEntry(existEntry));
                    LogUtil.INSTANCE.info("birdgangmedia" , "addFavorite > result : " + result);
                    Toast.makeText(getActivity(), getString(R.string.message_for_toast_favorite_content_use), Toast.LENGTH_SHORT).show();
                }

                long updatedContent = ContentControler.getDefault().updateContent(existEntry);
                LogUtil.INSTANCE.info("birdgangclicklistener" , "changeFavorite > updatedContent : " + updatedContent + " , existEntry.getIsFavoriteContent() : " + existEntry.getIsFavoriteContent());
                if (updatedContent > 0) {
                    mExplorerStackEntryList.updateContentForStackEntry(existEntry.convertCacheEntry());
                    mListExplorerEternalAdapter.updateContentFavorite(true, existEntry.convertViewEntry());
                }
            } catch (Exception e) {
                LogUtil.INSTANCE.error(getClass().getSimpleName(), e);
            }
        }
    };


    public ContentItemLongClickListener mContentItemLongClickListener = new ContentItemLongClickListener() {
        @Override
        public void onItemLongClick(View view) {
            try {
                int position = (Integer) view.getTag();
                ContentViewEntry previousContentEntry = mContentEntries.get(position);
                if (null == previousContentEntry) {
                    return;
                }
                new CustomAlertDialog(getActivity())
                        .setConfirmText(getString(R.string.dialog_ok))
                        .setTitleText(getString(R.string.dialog_title_path))
                        .setConfirmBtnColoer(BaseConfiguration.getInstance().getAppDialogBtnColor())
                        .setContentText(previousContentEntry.getParentsFilePath())
                        .showCancelButton(false)
                        .show();
            } catch (Exception e) {
                LogUtil.INSTANCE.error(getClass().getSimpleName(), e);
            }
        }
    };


    public ContentItemClickListener mContentItemClickListener = new ContentItemClickListener() {
        @Override
        public void onItemCategoryClick(View view) {
            try {
                int position = (Integer) view.getTag();
                ContentViewEntry previousContentEntry = mContentEntries.get(position);
                mStrLastSelectedContentPath = previousContentEntry.getContentFilePath();
                String parentsFilePath = previousContentEntry.getParentsFilePath();
                LogUtil.INSTANCE.debug("birdgangexplorernavigator", "onItemCategoryClick > mStrLastSelectedContentPath : " + mStrLastSelectedContentPath);
                LogUtil.INSTANCE.debug("birdgangexplorernavigator", "onItemCategoryClick > parentsFilePath : " + parentsFilePath);

                CommandHandler commandHandler = new CommandHandler();
                commandHandler.send(new ExplorerNavigator(ExplorerNavigator.NEXT));
                mAlbumType = AppConstants.LIST_TYPE_FOLDER;
            } catch (Exception e) {
                LogUtil.INSTANCE.error(getClass().getSimpleName(), e);
            }
        }

        @Override
        public void onItemClick(View view) {
            try {
                int position = (Integer) view.getTag();
                ContentViewEntry detailInfo = mContentEntries.get(position);
                ContentControler.getDefault().setPlaylistFromView(mContentEntries);
                ContentEntry contentEntry = new ContentEntry(detailInfo);
                String mediaType = contentEntry.getMediaType();
                LogUtil.INSTANCE.info("birdgangclickevent", "onItemClick > mediaType : " + mediaType);

                if (StringUtils.contains(ContentEntry.ContentType.DOC.getType(), mediaType)) {
                    ActivityCalls.callOpenChooser(getActivity(), contentEntry.getContentFilePath());
                    return;
                }

                String path = contentEntry.getContentFilePath();
                LogUtil.INSTANCE.info("birdgangclickevent", "onItemClick > path : " + path);
                boolean availableContent = checkAvailableContent(path);
                if (!availableContent) {
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
            LogUtil.INSTANCE.error(getClass().getSimpleName(), e);
        }
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
        Toast.makeText(getActivity(), "Must be override 'onLoadPlaybackActivity' method. for playback", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoadExplorerStackEntries(List<ExplorerStackEntry> explorerStackEntries) {
        mExplorerStackEntryList.setExplorerStackEntries(explorerStackEntries);
        mPresenter.updateListContent(mExplorerStackEntryList, mStrLastSelectedContentPath);
    }


    @Override
    public void onUpdateContent(List<ContentEntry> contentEntries) {
        try {
            setVisibleContentInfo();
            restoreActionBar();

            mContentEntries.clear();

            for (ContentEntry contentEntry : contentEntries) {
                ContentViewEntry contentViewEntry = contentEntry.convertViewEntry();
                contentViewEntry.setProgressbarDrawableForLms(provideProgressBarForLmsDrawable());
                contentViewEntry.setTextRateColorForLms(provideTextRateColorForLms());
                contentViewEntry.setTextRateDimColorForLms(provideTextRateDimColorForLms());
                mContentEntries.add(contentViewEntry);
            }

            fillContentSortList();
            setVisibleOptionMenuOnView();
            requestUpdateContentForLMSList();
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
    }


    @Override
    public void onUpdateContentForLMS(List<ContentViewEntry> contentViewEntries) {
        mListExplorerEternalAdapter.setDataForLMS(contentViewEntries);
        mListExplorerEternalAdapter.notifyDataSetChanged();
    }


    public class ExplorerNavigator implements Command {
        public static final int HOME = 0;
        public static final int NEXT = 1;
        public static final int PREVIOUS = 2;
        public static final int DIRECT = 3;

        public ExplorerNavigator (int type, int position, String lastSelectedContentPath) {
            if (type == DIRECT) {
                mExplorerStackEntryList.decreageDepthByPosition(position);
                mStrLastSelectedContentPath = lastSelectedContentPath;

                int currentDepth = mExplorerStackEntryList.currentDepth();
                LogUtil.INSTANCE.info("birdgangexplorernavigator" , "currentDepth : " + currentDepth + " , mStrLastSelectedContentPath : " + mStrLastSelectedContentPath);
            }
        }

        public ExplorerNavigator (int type) {
            if (type == HOME) {
                mExplorerStackEntryList.decreageDepthByPosition(1);
                mStrLastSelectedContentPath = StringUtils.EMPTY;
            }
            else if (type == PREVIOUS) {
                mExplorerStackEntryList.decreageDepth();
                mStrLastSelectedContentPath = StringUtil.extractContentDirctoryFullPath(mStrLastSelectedContentPath);
            }
            else if (type == NEXT) {
                int size = mContentEntries.size();
                LogUtil.INSTANCE.info("birdgangexplorernavigator" , "ExplorerNavigator > size : " + size);
                mExplorerStackEntryList.increageDepth(size);
            }

            int currentDepth = mExplorerStackEntryList.currentDepth();
            LogUtil.INSTANCE.info("birdgangexplorernavigator" , "currentDepth : " + currentDepth + " , mStrLastSelectedContentPath : " + mStrLastSelectedContentPath);
        }

        @Override
        public void execute() {
            mPresenter.updateListContent(mExplorerStackEntryList, mStrLastSelectedContentPath);
        }
    }


    public class ContentInfoHandler implements Command {
        @Override
        public void execute() {
            LogUtil.INSTANCE.info(TAG , "ContentInfoHandler > execute > mContentEntries.size() : " + mContentEntries.size());
            if (mContentEntries.size() <= 0) {
                return;
            }

            ContentViewEntry contentEntry = mContentEntries.get(0);
            String mediaDirctoryFillPath = StringUtil.extractMediaDirctoryFullPath(contentEntry.getContentFilePath());

            mNavigationPathEntries.clear();

            StringTokenizer st = new StringTokenizer(mediaDirctoryFillPath, "/" );
            String navigationPath = "";
            int depth = 0;

            while (st.hasMoreTokens()) {
                String token = st.nextToken();
                navigationPath += "/" + token;
                NavigationPathViewEntry navigationPathEntry = new NavigationPathViewEntry();
                navigationPathEntry.setDepth(++depth);
                navigationPathEntry.setStep(mExplorerStackEntryList.currentDepth());
                navigationPathEntry.setName(token);
                navigationPathEntry.setPath(navigationPath);
                navigationPathEntry.setFullPath(mediaDirctoryFillPath);
                mNavigationPathEntries.add(navigationPathEntry);
            }

            mRecyclerHorizontalView.scrollToPosition(mListNavigationPathAdapter.getItemCount() - 1);
            mListNavigationPathAdapter.notifyDataSetChanged();
        }
    }


    protected synchronized void requestGenerateExplorerStack () {
        Map<String, List<ContentCacheEntry>> contentCacheMemoryAwareEntries = new HashMap<>();
        ArrayMap<String, ContentCacheEntry> contentCacheDiskAwareMaps = new ArrayMap<String, ContentCacheEntry>();
        try {
            List<ContentCacheEntry> loadEntries = ContentControler.getDefault().loadContentListMemoryAware();
            if (null != loadEntries && loadEntries.size() > 0) {
                contentCacheMemoryAwareEntries.put(mStrStorageType, loadEntries);
                LogUtil.INSTANCE.info(TAG , "requestGenerateExplorerStack > loadEntries.size() : " + loadEntries.size());
            }
            contentCacheDiskAwareMaps = ContentControler.getDefault().loadContentMapsByFilePath();
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }

        mPresenter.generateExplorerStack(contentCacheMemoryAwareEntries, contentCacheDiskAwareMaps, mStrStorageType);
    }


    protected void requestUpdateContentForLMSList() {
        mPresenter.updateListContentForLMS(mContentEntries);
    }


    public void fillContentSortList() {
        try {
            mPresenter.sortListContent(mContentEntries);
            mListExplorerEternalAdapter.setData(mContentEntries);

            int count = mListExplorerEternalAdapter.getItemCount();
            if (count > 0) {
                mRecyclerHasEmptyView.success();
            } else {
                mRecyclerHasEmptyView.empty();
                mRecyclerHasEmptyView.setImgEmpthVisisble(View.GONE);
                mRecyclerHasEmptyView.setTextEmpth(getString(R.string.text_empty_message));
            }
            mListExplorerEternalAdapter.notifyDataSetChanged();

        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
    }

    public void sortListData() {
        try {
            getSortListData(mContentEntries);
            mListExplorerEternalAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
    }

    public void fillContentListData () {
        try {
            int count = mListExplorerEternalAdapter.getItemCount();
            if (count > 0) {
                mRecyclerHasEmptyView.success();
            } else {
                mRecyclerHasEmptyView.empty();
                mRecyclerHasEmptyView.setImgEmpthVisisble(View.GONE);
                mRecyclerHasEmptyView.setTextEmpth(getString(R.string.text_empty_message));
            }
            mListExplorerEternalAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
    }

    protected void setVisibleContentInfo() {
        CommandHandler handler = new CommandHandler();
        handler.send(new ContentInfoHandler());
    }


    public void setVisibleOptionMenuOnView () {
        FragmentActivity activity = getActivity();
        if (activity instanceof DrawerActivity) {
            if (((DrawerActivity)activity).isDrawerOpen()) {
                return;
            }
        }
    }


    protected EventBus.onEventUpdateContentListener onEventUpdateContentListener = new EventBus.onEventUpdateContentListener() {

        @Override
        public void onRefleshContents() {}

        @Override
        public void onUpdateFavoriteExplorerContentList(String playType, int contentId) {
            LogUtil.INSTANCE.info("birdgangnotify" , "onEventUpdateContentListener > onUpdateFavoriteExplorerContentList > playType : " + playType + " , contentId : " + contentId);
            if (StringUtils.equals(AppConstants.TYPE_PLAY_DOWNLOAD, playType)) {
                try {
                    ContentEntry existEntry = ContentControler.getDefault().findContentById(contentId);
                    mExplorerStackEntryList.updateContentForStackEntry(existEntry.convertCacheEntry());
                    mListExplorerEternalAdapter.updateContentFavorite(true, existEntry.convertViewEntry());
                } catch (Exception e) {
                    LogUtil.INSTANCE.error(TAG, e);
                }
            }
        }

        @Override
        public void onUpdateLMSExplorerContentList(String playType) {
            LogUtil.INSTANCE.info("birdgangnotify" , "onEventUpdateContentListener > onUpdateLMSExplorerContentList > playType : " + playType);
            if (StringUtils.equals(AppConstants.TYPE_PLAY_DOWNLOAD, playType)) {
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
        requestUpdateContentForLMSList();
    }

    @Override
    public void onScanStarted() {}

    @Override
    public void onScaning() {}

    @Override
    public void onScanCompleated() {
        requestGenerateExplorerStack();
    }

    @Override
    public void onSDcardMountedEvent(String externalPath) {
    }

    @Override
    public void onSDcardEjectedEvent() {
    }

    protected int provideNavigationBackgroundColor () {
        return R.color.provider_default_color_actionbar_main_bg;
    }

    protected int provideSubStringCountFileName() {
        return 4;
    }

    protected int provideProgressBarForLmsDrawable () {
        return R.drawable.default_progress_lms;
    }

    protected int provideTextRateDimColorForLms () {
        return R.color.provider_default_color_text_rate_for_lms_dim;
    }

    protected int provideTextRateColorForLms () {
        return R.color.provider_default_color_text_rate_for_lms;
    }

}
