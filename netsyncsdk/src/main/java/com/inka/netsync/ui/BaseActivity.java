package com.inka.netsync.ui;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.view.WindowManager;

import com.inka.netsync.BaseApplication;
import com.inka.netsync.BaseConfiguration;
import com.inka.netsync.R;
import com.inka.netsync.common.bus.EventBus;
import com.inka.netsync.common.utils.NetworkUtils;
import com.inka.netsync.controler.MediaControler;
import com.inka.netsync.data.cache.pref.PreferencesCacheHelper;
import com.inka.netsync.di.component.ActivityComponent;
import com.inka.netsync.di.component.DaggerActivityComponent;
import com.inka.netsync.di.module.ActivityModule;
import com.inka.netsync.logs.LogUtil;
import com.inka.netsync.ncg.Ncg2SdkHelper;
import com.inka.netsync.ui.fragment.BaseFragment;
import com.inka.netsync.ui.fragment.ExplorerFragment;
import com.inka.netsync.ui.fragment.ExplorerSearchFragment;
import com.inka.netsync.ui.mvpview.MvpView;
import com.inka.netsync.view.dialog.CustomAlertDialog;
import com.inka.netsync.view.dialog.SimpleMessageDialog;
import com.inka.netsync.view.dialog.progress.LoadingProgressDialogHelper;
import com.inka.netsync.view.model.DrawerMenuViewEntry;

import org.apache.commons.lang3.StringUtils;

import butterknife.Unbinder;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public abstract class BaseActivity extends AppCompatActivity implements MvpView {

    private final String TAG = "BaseActivity";

    private ActivityComponent mActivityComponent;
    private Unbinder mUnBinder;

    protected Uri mTreeUri = null;

    protected final Handler mHandler = new Handler();

    protected String mAppUiType = null;

    protected NetworkChangeReceiver mNetworkReceiver = null;
    protected SDcardChangeReceiver mSDcardReceiver = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.INSTANCE.info("birdganglifecycl" , "BaseActivity > onCreate");
        mActivityComponent = DaggerActivityComponent.builder()
                .activityModule(new ActivityModule(this))
                .applicationComponent(((BaseApplication) getApplication()).getComponent())
                .build();

        // network receiver 등록
        registerNetworkChangeReceiver();
        registerSDcardChangeReceiver();
    }


    @SuppressLint("NewApi")
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void onReSetUiSet(Toolbar toolbar, int drawable, int statusBarColor) {
        LogUtil.INSTANCE.info("birdgangpalette" , "onReSetUiSet");
        try {
            Window window = getWindow();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(ContextCompat.getColor(this, statusBarColor));
            }
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            toolbar.setBackgroundDrawable(getResources().getDrawable(drawable));
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
    }

    @SuppressLint("NewApi")
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void onReSetUiSetColor(Toolbar toolbar, int actionBarColor, int statusBarColor) {
        try {
            Window window = getWindow();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(ContextCompat.getColor(this, statusBarColor));
            }
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            toolbar.setBackgroundColor(ContextCompat.getColor(this, actionBarColor));
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
    }

    @SuppressLint("NewApi")
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void onReSetUiSet(Toolbar toolbar, int color, Drawable background) {
        LogUtil.INSTANCE.info("birdgangpalette" , "onReSetUiSet");
        try {
            Window window = getWindow();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(ContextCompat.getColor(this, color));
            }
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            toolbar.setBackgroundDrawable(background);
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
    }


    public void closeActivity() {
        new CustomAlertDialog(this, CustomAlertDialog.WARNING_TYPE)
                .setTitleText(getString(R.string.dialog_title_close))
                .setContentText(getString(R.string.dialog_message_close_application))
                .setCancelText(getString(R.string.dialog_cancel))
                .setConfirmText(getString(R.string.dialog_ok))
                .setConfirmBtnColoer(BaseConfiguration.getInstance().getAppDialogBtnColor())
                .setCancelBtnColoer(BaseConfiguration.getInstance().getAppDialogBtnColor())
                .showCancelButton(true)
                .setDialogTypeLarge()
                .setCancelClickListener(new CustomAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(CustomAlertDialog sDialog) {
                        sDialog.cancel();
                    }
                })
                .setConfirmClickListener(new CustomAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(CustomAlertDialog sDialog) {
                        try {
                            sDialog.cancel();
                            finish();

                            Ncg2SdkHelper.getDefault().release();
                        } catch (Exception e) {
                            e.getMessage();
                        }
                    }
                })
                .show();

    }

    public ActivityComponent getActivityComponent() {
        return mActivityComponent;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public boolean isNetworkConnected() {
        return NetworkUtils.isNetworkConnected(this);
    }

    public void setUnBinder(Unbinder unBinder) {
        mUnBinder = unBinder;
    }

    public String getAppUiType() {
        return mAppUiType;
    }

    @Override
    protected void onDestroy() {
        if (mUnBinder != null) {
            mUnBinder.unbind();
        }
        super.onDestroy();
    }

    public class NetworkChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                String action = intent.getAction();
                if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION) == true) {
                    EventBus.getDefault().notifyEventNetworkChanged();
                } else if(action.equals(getPackageName() + ".push_main") == true) {
                    Bundle bundle = intent.getBundleExtra("push");
                    if (bundle != null) {
                        SimpleMessageDialog dialog = new SimpleMessageDialog(BaseActivity.this, bundle.getString("title"), bundle.getString("desc"));
                        dialog.show();
                    }
                }
            } catch (Exception e) {
                LogUtil.INSTANCE.error(TAG, e);
            }
        }
    }

    public class SDcardChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                String action = intent.getAction();
                String path = intent.getData().getPath();
                boolean isReadOnly = intent.getBooleanExtra("read-only", false);
                LogUtil.INSTANCE.info("birdgangreceiver", "SDcardStateReceiver > onReceive > action : " + action + " , path : " + path);

                if (action.equalsIgnoreCase(Intent.ACTION_MEDIA_REMOVED)
                        || action.equalsIgnoreCase(Intent.ACTION_MEDIA_UNMOUNTED)
                        || action.equalsIgnoreCase(Intent.ACTION_MEDIA_BAD_REMOVAL)
                        || action.equalsIgnoreCase(Intent.ACTION_MEDIA_EJECT)) {
                    LogUtil.INSTANCE.info("birdgangreceiver", "SDcardStateReceiver > onReceive > ACTION_MEDIA_REMOVED , ACTION_MEDIA_UNMOUNTED, ACTION_MEDIA_BAD_REMOVAL, ACTION_MEDIA_EJECT");
                    PreferencesCacheHelper.setPreferenceValueForBol(PreferencesCacheHelper.AUTO_RESCAN, true);
                    PreferencesCacheHelper.setPreferenceValueForBol(PreferencesCacheHelper.HAS_RUN_INIT_FIRST, false);
                    PreferencesCacheHelper.setPreferenceValue(PreferencesCacheHelper.SELECTED_DRAWER_MENU, DrawerMenuViewEntry.TAG_DEFAULT);

                    removeManualExternalSDPath(path, false);
                    EventBus.getDefault().notifyEventSDcardEjected();
                    MediaControler.getDefault().clear();

                    boolean isWorking = MediaControler.getDefault().isWorking();
                    if (!isWorking) {
                        MediaControler.getDefault().scanMediaItems();
                    }

                } else if (action.equalsIgnoreCase(Intent.ACTION_MEDIA_MOUNTED)) {
                    LogUtil.INSTANCE.info("birdgangreceiver", "SDcardStateReceiver > onReceive > ACTION_MEDIA_MOUNTED");
                    saveManualExternalSDPath(path, true);
                    EventBus.getDefault().notifyEventSDcardMountedEventListener(path);
                    boolean isWorking = MediaControler.getDefault().isWorking();
                    if (!isWorking) {
                        MediaControler.getDefault().scanMediaItems();
                    }
                }
            } catch (Exception e) {
                LogUtil.INSTANCE.error(TAG, e);
            }
        }
    }


    /**
     *
     * @param externalSDPath
     * @param isShow
     */
    protected void saveManualExternalSDPath(String externalSDPath, boolean isShow) {
        try {
            // Preference 에 저장
            PreferencesCacheHelper.setPreferenceValue(getString(R.string.PREF_KEY_MANUAL_EXTERNAL_SD), externalSDPath);
            if (isShow == true) {
//                Toast.makeText(BaseActivity.this, getString(R.string.setting_result_complete_sdcard_recognition), Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
    }


    /**
     *
     * @param externalSDPath
     * @param isShow
     */
    protected void removeManualExternalSDPath(String externalSDPath, boolean isShow) {
        try {
            // Preference 에 저장
            String tmpExternalSDPath = PreferencesCacheHelper.getPreferenceValue(getString(R.string.PREF_KEY_MANUAL_EXTERNAL_SD), "");
            // 같으면 삭제
            if (StringUtils.equals(tmpExternalSDPath, externalSDPath)) {
                PreferencesCacheHelper.setPreferenceValue(getString(R.string.PREF_KEY_MANUAL_EXTERNAL_SD), "");
            }
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
    }


    /**
     *
     * @return
     */
    public boolean registerNetworkChangeReceiver() {
        try {
            IntentFilter networkChangeReceiverFilter = new IntentFilter();
            networkChangeReceiverFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            networkChangeReceiverFilter.addAction(getPackageName() + ".push_main");
            mNetworkReceiver = new NetworkChangeReceiver();
            registerReceiver(mNetworkReceiver, networkChangeReceiverFilter);
            return true;

        } catch(Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
            return false;
        }
    }


    public boolean registerSDcardChangeReceiver() {
        try {
            IntentFilter SDcardChangeReceiverFilter = new IntentFilter();
            SDcardChangeReceiverFilter.addAction(Intent.ACTION_MEDIA_MOUNTED);
            SDcardChangeReceiverFilter.addAction(Intent.ACTION_MEDIA_EJECT);
            SDcardChangeReceiverFilter.addDataScheme("file");

            mSDcardReceiver = new SDcardChangeReceiver();
            registerReceiver(mSDcardReceiver, SDcardChangeReceiverFilter);
            return true;

        } catch(Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
            return false;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtil.INSTANCE.info("birdgangactivityresult", "BaseActivity > requestCode : " + requestCode + " , resultCode : " + resultCode);
    }

    /**
     * SD 시나리오에서 앱 실행시 모든 라이센스를 삭제하여 매번 content 재생시 라이센스 획득하도록 한다.
     */
    protected void setOneTimeLicense() {
//        Ncg2SdkHelper.getDefault().removeLicenseAllCID();
        LogUtil.INSTANCE.info("birdgangncg2sdk", " remove all license..");
    }

    @Override
    public void showProgress(Activity activity, int message) {
        LoadingProgressDialogHelper.getDefault().show();
    }

    @Override
    public void hideProgress(Activity activity) {
        LoadingProgressDialogHelper.getDefault().hide();
    }


    /**
     *
     * @return
     */
    protected BaseFragment provideStorageFragment () {
        return null;
    }

    /**
     *
     * @return
     */
    protected BaseFragment provideWebViewFragment () {
        return null;
    }

    /**
     *
     * @return
     */
    protected BaseFragment provideInfoWebViewFragment () {
        return null;
    }

    /**
     *
     * @return
     */
    protected BaseFragment provideDefaultFragment () {
        return null;
    }


    /**
     *
     * @return
     */
    protected BaseFragment provideExplorerFragment (String storageType) {
        return ExplorerFragment.newInstance(storageType);
    }

    /**
     *
     * @return
     */
    protected BaseFragment provideFavoriteFragment () {
        return null;
    }

    /**
     *
     * @return
     */
    protected BaseFragment providePlayedListFragment () {
        return null;
    }

    /**
     *
     * @return
     */
    protected BaseFragment provideSettingFragment () {
        return null;
    }

    /**
     *
     * @return
     */
    protected BaseFragment provideSearchFragment () {
        return ExplorerSearchFragment.newInstance();
    }


    protected BaseFragment provideInfoFragment () {
        return null;
    }


    protected abstract void setUp();

}