package com.inka.netsync.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.inka.netsync.R;
import com.inka.netsync.R2;
import com.inka.netsync.command.Command;
import com.inka.netsync.command.CommandHandler;
import com.inka.netsync.common.AppConstants;
import com.inka.netsync.common.bus.EventBus;
import com.inka.netsync.common.utils.NetworkUtils;
import com.inka.netsync.controler.MediaControler;
import com.inka.netsync.data.cache.pref.PreferencesCacheHelper;
import com.inka.netsync.logs.LogUtil;
import com.inka.netsync.media.MediaStorage;
import com.inka.netsync.media.bus.DevicesDiscoveryCb;
import com.inka.netsync.media.bus.ScanEventBus;
import com.inka.netsync.media.model.ScanEntry;
import com.inka.netsync.model.DrawerMenuEntry;
import com.inka.netsync.ui.fragment.BaseFragment;
import com.inka.netsync.ui.fragment.NavigationDrawerFragment;
import com.inka.netsync.ui.mvppresenter.DrawerMvpPresenter;
import com.inka.netsync.ui.mvpview.DrawerMvpView;
import com.inka.netsync.view.ProgressManagerClient;
import com.inka.netsync.view.dialog.CustomAlertDialog;
import com.inka.netsync.view.dialog.MediaLoadingAlertDialog;
import com.inka.netsync.view.model.DrawerMenuViewEntry;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by birdgang on 2018. 1. 22..
 */
public abstract class DrawerActivityPlayer extends DrawerActivity implements ScanEventBus.ScanResultOnUi, DevicesDiscoveryCb {

    private final String TAG = DrawerActivityPlayer.class.toString();

    @Inject
    DrawerMvpPresenter<DrawerMvpView> mPresenter;

    @BindView(R2.id.toolbar)
    Toolbar mToolbar;

    @BindView(R2.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @BindView(R2.id.fl_fragment_area)
    public FrameLayout mFrameLayoutContent	= null;

    public ActionBar mActionBar;

    protected BaseFragment mBaseFragment;
    protected NavigationDrawerFragment mNavigationDrawerFragment = null;

    protected ActionBarDrawerToggle mActionBarDrawerToggle = null;

    private MediaLoadingAlertDialog mMediaLoadingAlertDialog;

    private boolean mScanNeeded = false;
    private boolean mParsing = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MediaControler.getDefault().setMediaScanListener(this);
        setContentView(R.layout.activity_drawer_container);

        getActivityComponent().inject(this);
        setUnBinder(ButterKnife.bind(this));
        mPresenter.onAttach(DrawerActivityPlayer.this);

        mAppUiType = AppConstants.APPUITYPE_DRAWER;

        EventBus.getDefault().registerEventDeviceStateListener(onEventDeviceStateListener);

        setUp();
    }


    @Override
    protected void onResume() {
        super.onResume();
        MediaControler.getDefault().addDeviceDiscoveryCb(this);
    }


    @Override
    protected void onPause() {
        super.onPause();
        MediaControler.getDefault().removeDeviceDiscoveryCb(this);
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (null != mMediaLoadingAlertDialog && mMediaLoadingAlertDialog.isShowing()) {
            mMediaLoadingAlertDialog.onDiscoveryCompleted("100");
        }
    }

    @Override
    protected void setUp() {
        mMediaLoadingAlertDialog = new MediaLoadingAlertDialog(this, CustomAlertDialog.WARNING_TYPE);

        CommandHandler commandHandler = new CommandHandler();
        commandHandler.sendForDelay(new CommandScanMedia());

        mActionBar = initActionBar(mToolbar);

        onReSetUiSetColor(mToolbar, provideActionBarColor(), provideStatusBarBackgroundColor());

        mNavigationDrawerFragment = (NavigationDrawerFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);

        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (ProgressManagerClient.getInstance().isShowProgress()) {
                    return;
                }
                try {
                    supportInvalidateOptionsMenu();
                } catch (Exception e) {
                    LogUtil.INSTANCE.error(TAG, e);
                }
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (ProgressManagerClient.getInstance().isShowProgress()) {
                    return;
                }
                supportInvalidateOptionsMenu();
            }
        };

        try {
            setDrawerState();
            setDrawerLayout();
            setCurrentTabByTag(DrawerMenuViewEntry.TAG_DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // MainThread 에서 Network 허용
        StrictMode.ThreadPolicy pol = new StrictMode.ThreadPolicy.Builder().permitNetwork().build();
        StrictMode.setThreadPolicy(pol);

    }


    class CommandScanMedia implements Command {
        @Override
        public void execute() {
            boolean isEmpthContent = MediaControler.getDefault().getItemList().isEmpty();
            LogUtil.INSTANCE.info(TAG, "DrawerActivity > onCreate > isEmpthContent : " + isEmpthContent);
            if (isEmpthContent) {
                boolean autoScan = PreferencesCacheHelper.getPreferenceValueForBol(PreferencesCacheHelper.AUTO_RESCAN, true);
                LogUtil.INSTANCE.info(TAG, "DrawerActivity > onCreate > autoScan : " + autoScan);
                if (autoScan) {
                    MediaControler.getDefault().scanMediaItems();
                } else {
                    MediaControler.getDefault().loadData();
                }
            } else {
                onMediaScanCompletedInUi(null);
            }
        }
    }




    @Override
    public void onDiscoveryStarted(final String entryPoint) {
        LogUtil.INSTANCE.info(TAG, "onDiscoveryStarted");
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                mMediaLoadingAlertDialog = new MediaLoadingAlertDialog(DrawerActivityPlayer.this, CustomAlertDialog.WARNING_TYPE);
                mMediaLoadingAlertDialog.onDiscoveryStarted(entryPoint);
                mMediaLoadingAlertDialog.setProgressbarDrawerResource(provideProgressBarDrawerResource());
                mMediaLoadingAlertDialog.setProgressbarSyncDrawerResource(provideProgressBarSyncDrawerResource());
            }
        });
    }

    @Override
    public void onDiscoveryProgress(String entryPoint) {
        LogUtil.INSTANCE.info(TAG, "onDiscoveryProgress > entryPoint : " + entryPoint);
        if (null == mMediaLoadingAlertDialog) {
            mMediaLoadingAlertDialog = new MediaLoadingAlertDialog(this, CustomAlertDialog.WARNING_TYPE);
            mMediaLoadingAlertDialog.setProgressbarDrawerResource(provideProgressBarDrawerResource());
            mMediaLoadingAlertDialog.setProgressbarSyncDrawerResource(provideProgressBarSyncDrawerResource());
        }
        mMediaLoadingAlertDialog.onDiscoveryProgress(entryPoint);
    }

    @Override
    public void onDiscoveryCompleted(String entryPoint) {
        LogUtil.INSTANCE.info(TAG, "onDiscoveryCompleted > entryPoint : " + entryPoint);
        if (null == mMediaLoadingAlertDialog) {
            mMediaLoadingAlertDialog = new MediaLoadingAlertDialog(this, CustomAlertDialog.WARNING_TYPE);
            mMediaLoadingAlertDialog.setProgressbarDrawerResource(provideProgressBarDrawerResource());
            mMediaLoadingAlertDialog.setProgressbarSyncDrawerResource(provideProgressBarSyncDrawerResource());
        }
        mMediaLoadingAlertDialog.onDiscoveryCompleted(entryPoint);
        PreferencesCacheHelper.setPreferenceValueForBol(PreferencesCacheHelper.AUTO_RESCAN, false);
    }

    @Override
    public void onParsingStatsUpdated(final int percent, final String entryPoint) {
        LogUtil.INSTANCE.info(TAG, "onParsingStatsUpdated > percent : " + percent + " , entryPoint : " + entryPoint);
        mParsing = percent < 100;

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if (null == mMediaLoadingAlertDialog) {
                    mMediaLoadingAlertDialog = new MediaLoadingAlertDialog(DrawerActivityPlayer.this, CustomAlertDialog.WARNING_TYPE);
                    mMediaLoadingAlertDialog.setProgressbarDrawerResource(provideProgressBarDrawerResource());
                    mMediaLoadingAlertDialog.setProgressbarSyncDrawerResource(provideProgressBarSyncDrawerResource());
                }
                mMediaLoadingAlertDialog.onParsingStatsUpdated(percent, entryPoint, mParsing);
                if (!mParsing) {
                    PreferencesCacheHelper.setPreferenceValueForBol(PreferencesCacheHelper.AUTO_RESCAN, false);
                }
            }
        });
    }


    @Override
    public void onParsingStatsUpdatedSync(int percent, String entryPoint) {
        LogUtil.INSTANCE.info(TAG, "onParsingStatsUpdated > percent : " + percent + " , entryPoint : " + entryPoint);
        mParsing = percent < 100;
        if (null == mMediaLoadingAlertDialog) {
            mMediaLoadingAlertDialog = new MediaLoadingAlertDialog(this, CustomAlertDialog.WARNING_TYPE);
            mMediaLoadingAlertDialog.setProgressbarDrawerResource(provideProgressBarDrawerResource());
            mMediaLoadingAlertDialog.setProgressbarSyncDrawerResource(provideProgressBarSyncDrawerResource());
        }
        mMediaLoadingAlertDialog.onParsingStatsUpdatedSync(percent, entryPoint, mParsing);
        if (!mParsing) {
            PreferencesCacheHelper.setPreferenceValueForBol(PreferencesCacheHelper.AUTO_RESCAN, false);
        }
    }


    @Override
    public void onMediaScanStartInUi() {
        LogUtil.INSTANCE.info(TAG, "onMediaScanStart");

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if (null != mBaseFragment) {
                    mBaseFragment.onScanStarted();
                }
            }
        });
    }

    @Override
    public void onUpdateMediaInUi(int totalSize, ScanEntry scanEntry) {
        LogUtil.INSTANCE.info(TAG, "onUpdateMedia");

        if (null != mBaseFragment) {
            mBaseFragment.onScaning();
        }
    }

    @Override
    public void onMediaScanCompletedInUi(List<ScanEntry> scanEntries) {
        LogUtil.INSTANCE.info(TAG, "onMediaScanCompleted");

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if (null != mBaseFragment) {
                    mBaseFragment.onScanCompleated();
                }
                if (null != mNavigationDrawerFragment) {
                    mNavigationDrawerFragment.onScanCompleated();
                }

                commandMediaScanCompleted();
            }
        });
    }


    @Override
    public void onMediaLoadCompletedInUi() {
        LogUtil.INSTANCE.info(TAG, "onMediaLoadCompleted");

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if (null != mBaseFragment) {
                    mBaseFragment.onScanCompleated();
                }
                if (null != mNavigationDrawerFragment) {
                    mNavigationDrawerFragment.onScanCompleated();
                }
                commandMediaScanCompleted();
            }
        });
    }

    @Override
    public void onMediaScanStopInUi() {
        LogUtil.INSTANCE.info(TAG, "onMediaScanStop");

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if (null != mBaseFragment) {
                    mBaseFragment.onScanCompleated();
                }

                commandMediaScanCompleted();
            }
        });
    }


    private void commandMediaScanCompleted () {
        ProgressManagerClient.getInstance().stopProgress(DrawerActivityPlayer.this);
        mPresenter.onUpdatePath();
    }


    public void setDrawerState() {
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        mActionBarDrawerToggle.onDrawerStateChanged(DrawerLayout.LOCK_MODE_UNLOCKED);
        mActionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        mActionBarDrawerToggle.syncState();
    }


    protected ActionBar initActionBar(Toolbar toolbar) {
        if (null == toolbar) {
            return null;
        }

        ActionBar actionBar = null;

        try {
            setSupportActionBar(toolbar);
            actionBar = getSupportActionBar();
            actionBar.setDisplayShowTitleEnabled(true);
        } catch (Exception e) {
            LogUtil.INSTANCE.error("errror", e);
        }
        return actionBar;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mActionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mActionBarDrawerToggle.syncState();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mActionBarDrawerToggle.onConfigurationChanged(newConfig);
    }



    private void setDrawerLayout () throws Exception {
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mActionBarDrawerToggle.syncState();
            }
        });
        mDrawerLayout.addDrawerListener(mActionBarDrawerToggle);
    }


    public boolean isDrawerOpen() {
        if (mDrawerLayout == null) {
            return false;
        } else {
            boolean isDrawerOpen = mDrawerLayout.isDrawerOpen(GravityCompat.START);
            LogUtil.INSTANCE.info("birdgangactionbar", "isDrawerOpen : " + isDrawerOpen);
            return isDrawerOpen;
        }
    }


    public void closeDrawer() {
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }


    protected EventBus.onEventDeviceStateListener onEventDeviceStateListener = new EventBus.onEventDeviceStateListener() {

        @Override
        public void onNetworkChangedEvent() {}

        @Override
        public void onSDcardMountedEvent(String externalSDPath) {
            try {
                if (null != mBaseFragment) {
                    mBaseFragment.onSDcardMountedEvent(externalSDPath);
                }
            } catch (Exception e) {
                LogUtil.INSTANCE.error(TAG, e);
            }
        }

        @Override
        public void onSDcardEjectedEvent() {
            try {
                if (null != mBaseFragment) {
                    mBaseFragment.onSDcardEjectedEvent();
                }

                onSelectedMenu(DrawerMenuEntry.TAG_DEFAULT);
            } catch (Exception e) {
                LogUtil.INSTANCE.error(TAG, e);
            }
        }
    };


    protected void onSelectedMenu(final String tag) {
        if (null == mDrawerLayout) {
            return;
        }

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                try {
                    closeDrawer();
                } catch (Exception e) {
                    LogUtil.INSTANCE.error(TAG , e);
                }
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    replaceFragment(tag);
                } catch (Exception e) {
                    LogUtil.INSTANCE.error(TAG , e);
                }
            }
        }, 500);
    }

    public Fragment getCurrentFragment(int resLayoutId) {
        Fragment currentFragment = null;
        try {
            currentFragment = getSupportFragmentManager().findFragmentById(resLayoutId);
        } catch (Exception e) {
            return null;
        }
        return currentFragment;
    }


    protected void replaceFragment(String tabTag) throws Exception {
        LogUtil.INSTANCE.info(TAG, "replaceFragment > tabTag : " + tabTag);

        mBaseFragment = (BaseFragment) getFragment(tabTag);
        if (mBaseFragment == null) {
            return;
        }

        final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.fadein, R.anim.fadeout);
        String keyTagfragment = mBaseFragment.getArguments().getString(getString(R.string.key_tag_fragment));

        transaction.replace(R.id.fl_fragment_area, mBaseFragment, keyTagfragment);

        // RealTabContent
        if (mFrameLayoutContent != null) {
            mFrameLayoutContent.setVisibility(View.VISIBLE);
        }

        transaction.commitAllowingStateLoss();


        if (null != mNavigationDrawerFragment) {
            mNavigationDrawerFragment.updateDrawerMenu(tabTag);
        }

        setLastSelectedDrawerMenu(tabTag);
    }


    protected Fragment getFragment(String tabTag) {
        LogUtil.INSTANCE.info(TAG, "getFragment > tabTag : " + tabTag);

        Fragment newFragment = null;

        if (tabTag.equals(DrawerMenuEntry.TAG_DEFAULT)) {
            newFragment = provideDefaultFragment();
        }
        else if (tabTag.equals(DrawerMenuEntry.TAG_INTERNAL_STORAGE)) {
            newFragment = provideExplorerFragment(MediaStorage.ROOT_INTERNAL);
        }
        else if (tabTag.equals(DrawerMenuEntry.TAG_EXTERNAL_STORAGE)) {
            newFragment = provideExplorerFragment(MediaStorage.ROOT_EXTERNAL);
        }
        else if (tabTag.equals(DrawerMenuEntry.TAG_OTG_STORAGE)) {
            newFragment = provideExplorerFragment(MediaStorage.ROOT_USB);
        }
        else if (tabTag.equals(DrawerMenuEntry.TAG_PLAYEDLIST)) {
            newFragment = providePlayedListFragment();
        }
        else if (tabTag.equals(DrawerMenuEntry.TAG_FAVORITE)) {
            newFragment = provideFavoriteFragment();
        }
        else if (tabTag.equals(DrawerMenuEntry.TAG_INFORMATION_SETTING)) {
            newFragment = provideSettingFragment();
        }
        else if (tabTag.equals(DrawerMenuEntry.TAG_INFORMATION_HELP)) {
            boolean networkstate = NetworkUtils.isNetworkConnected(this);
            if (networkstate) {
                newFragment = provideInfoWebViewFragment();
            } else {
                newFragment = provideInfoFragment();
            }
        }
        return newFragment;
    }

    @Override
    public void onBackPressed() {
        LogUtil.INSTANCE.info("birdganglifecycle", "onBackPressed");
        if (null != mDrawerLayout && mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            return;
        }

        if (null == mBaseFragment) {
            closeActivity();
        }
        else {
            boolean backPressed = mBaseFragment.onBackPressed();
            if (backPressed) {
                closeActivity();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        LogUtil.INSTANCE.info("birdgangintent" , "onActivityResult > requestCode : " + requestCode + " , resultCode : " + resultCode);
    }


    /**
     * 스플래쉬 이미지
     * @return
     */
    protected int provideStatusBarBackgroundColor() {
        return R.color.provider_default_color_statusbar_main_bg;
    }

    /***
     * 액션바 drawable
     * @return
     */
    protected int provideActionBarDrawable() {
        return R.drawable.default_actionbar_gradation;
    }


    /***
     * 액션바 컬러
     * @return
     */
    protected int provideActionBarColor() {
        return R.color.provider_default_color_actionbar_main_bg;
    }

    /**
     *
     * @return
     */
    protected int provideProgressBarSyncDrawerResource () {
        return R.drawable.progress_media_scan_sync;
    }

    /**
     *
     * @return
     */
    public int provideProgressBarDrawerResource () {
        return R.drawable.progress_media_scan;
    }


}