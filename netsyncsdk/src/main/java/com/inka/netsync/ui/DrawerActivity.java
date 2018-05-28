package com.inka.netsync.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import com.inka.netsync.BaseApplication;
import com.inka.netsync.R;
import com.inka.netsync.common.AppConstants;
import com.inka.netsync.common.bus.EventBus;
import com.inka.netsync.common.bus.WeakHandler;
import com.inka.netsync.controler.MediaControler;
import com.inka.netsync.data.cache.pref.PreferencesCacheHelper;
import com.inka.netsync.logs.LogUtil;
import com.inka.netsync.model.DrawerMenuEntry;
import com.inka.netsync.ui.fragment.BaseFragment;
import com.inka.netsync.ui.fragment.NavigationDrawerFragment;
import com.inka.netsync.ui.mvpview.DrawerMvpView;
import com.inka.netsync.view.dialog.SingleAlertDialog;
import com.inka.netsync.view.model.DrawerMenuViewEntry;
import com.inka.netsync.view.web.callback.ViewClientCallback;

public abstract class DrawerActivity extends BaseActivity implements DrawerMvpView, ViewClientCallback {

    private static final String TAG = "DrawerActivity";

    public ActionBar mActionBar;

    protected BaseFragment mBaseFragment;
    protected NavigationDrawerFragment mNavigationDrawerFragment = null;

    protected static Drawable mDrawerColProgress;
    protected static Drawable mDrawerColProgressSync;

    private boolean mScanNeeded = false;
    private boolean mParsing = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().registerEventDeviceStateListener(onEventDeviceStateListener);

        mAppUiType = AppConstants.APPUITYPE_DRAWER;

        boolean initNcgSdk = BaseApplication.initNcgSdk(DrawerActivity.this);
        if (!initNcgSdk) {
            finish();
        }
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
    protected void onResume() {
        super.onResume();
        LogUtil.INSTANCE.info("birdganglifecycler" , "onResume");
        if (mScanNeeded) {
            MediaControler.getDefault().scanMediaItems();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogUtil.INSTANCE.info("birdganglifecycler" , "DrawerActivity > onStart");
        EventBus.getDefault().registerSelectedMenuListener(mOnEventSelectedListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtil.INSTANCE.info("birdganglifecycler" , "DrawerActivity > onPause");
        mScanNeeded = MediaControler.getDefault().isWorking();
        MediaControler.getDefault().stop();
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtil.INSTANCE.info("birdganglifecycler" , "DrawerActivity > onStop");
        EventBus.getDefault().removeSelectedMenuListener(mOnEventSelectedListener);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtil.INSTANCE.info("birdganglifecycler" , "DrawerActivity > onDestroy");
        EventBus.getDefault().removeEventDeviceStateListener(onEventDeviceStateListener);
    }

    protected void setCurrentTabByTag(String tabTag) {
        if (null != mNavigationDrawerFragment) {
            mNavigationDrawerFragment.setCurrentTab(tabTag);
        }

        try {
            replaceFragment(tabTag);
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
    }


    public void goForceHomeMessageDialog () {
        SingleAlertDialog.getDefault().goForceHomeMessageDialog(this, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                            mBaseFragment = provideWebViewFragment();
                            String newKeyTagFragment = mBaseFragment.getArguments().getString(getString(R.string.key_tag_fragment));
                            final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.fl_fragment_area, mBaseFragment, newKeyTagFragment);
                            transaction.commitAllowingStateLoss();
                        } catch (Exception e) {
                            LogUtil.INSTANCE.error(TAG, e);
                        }
                    }
                });
            }
        });
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
            } catch (Exception e) {
                LogUtil.INSTANCE.error(TAG, e);
            }
        }
    };


    private EventBus.onEventSelectedMenuListener mOnEventSelectedListener = new EventBus.onEventSelectedMenuListener() {
        @Override
        public void onSelectedDrawerMenu(final String tag) {
            onSelectedMenu(tag);
        }
    };

    @Override
    public void onMoveMyFolder() {
        setCurrentTabByTag(DrawerMenuEntry.TAG_STORAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        try {
            switch (requestCode) {
                // Player 종료후 화면 업데이트
                case AppConstants.ACT_PLAYER:
                    if (resultCode == 0) {
                        // Download 재생
//                        updateAllFragment();
                    }
                    break;
            }
            super.onActivityResult(requestCode, resultCode, resultData);
        } catch (Exception e) {
            super.onActivityResult(requestCode, resultCode, resultData);
        }
    }

    @Override
    public void onMoveContent(int index) {
        setCurrentTabByTag(DrawerMenuEntry.TAG_INTERNAL_STORAGE);
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onMovePlayedList() {
        setCurrentTabByTag(DrawerMenuEntry.TAG_PLAYEDLIST);
    }

    @Override
    public void onMoveHome() {
        setCurrentTabByTag(DrawerMenuEntry.TAG_HOME);
    }

    @Override
    public void onMoveFavorite() {
        setCurrentTabByTag(DrawerMenuEntry.TAG_FAVORITE);
    }

    @Override
    public void onMoveSetting() {
        setCurrentTabByTag(DrawerMenuEntry.TAG_INFORMATION_SETTING);
    }

    @Override
    public void onMove3rdpartyapp() {
        setCurrentTabByTag(DrawerMenuEntry.TAG_INFORMATION_3RDPATY);
    }


    protected static class DrawerActivityHandler extends WeakHandler<DrawerActivity> {
        public DrawerActivityHandler(DrawerActivity owner) {
            super(owner);
        }
    }

    protected void setLastSelectedDrawerMenu (String selectedMenu) {
        PreferencesCacheHelper.setPreferenceValue(PreferencesCacheHelper.SELECTED_DRAWER_MENU, selectedMenu);
    }

    protected String getLastSelectedDrawerMenu () {
        return PreferencesCacheHelper.getPreferenceValue(PreferencesCacheHelper.SELECTED_DRAWER_MENU, DrawerMenuViewEntry.TAG_DEFAULT);
    }

    public abstract boolean isDrawerOpen ();

    protected abstract void replaceFragment(String tabTag) throws Exception;
    protected abstract void onSelectedMenu (String tag);
    protected abstract Fragment getFragment(String tabTag);
}