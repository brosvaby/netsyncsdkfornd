package com.inka.netsync;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.widget.Toast;

import com.inka.ncg2.Ncg2Exception;
import com.inka.netsync.common.AppConstants;
import com.inka.netsync.common.utils.AndroidUtil;
import com.inka.netsync.data.cache.pref.PreferencesCacheHelper;
import com.inka.netsync.di.component.ApplicationComponent;
import com.inka.netsync.di.component.DaggerApplicationComponent;
import com.inka.netsync.di.module.ApplicationModule;
import com.inka.netsync.logs.LogUtil;
import com.inka.netsync.ncg.NetSyncSdkHelper;
import com.inka.netsync.observer.DataChangedObserver;

import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class BaseApplication extends MultiDexApplication {

    private final String TAG = "BaseApplication";

    ApplicationComponent mApplicationComponent;

    private static Context context = null;
    private static BaseApplication application = null;

    public static String mEnterpriseCode = "";
    public static String mEnterpriseName = "";

    private static boolean initialized = false;

    public static String mAppState = AppConstants.APPLICATION_STATE_READY;

    private ThreadPoolExecutor mThreadPool = new ThreadPoolExecutor(0, 2, 2, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());

    public static void runBackground(Runnable runnable) {
        application.mThreadPool.execute(runnable);
    }

    public static boolean removeTask(Runnable runnable) {
        return application.mThreadPool.remove(runnable);
    }

    public static BaseApplication get(Context context) {
        return (BaseApplication) context.getApplicationContext();
    }

    public static Context getContext () {
        return BaseApplication.context;
    }

    public static final Uri BOOKMARKS_URI = Uri.parse("content://browser/bookmarks");
    public DataChangedObserver dataChangedObserver = new DataChangedObserver(new Handler());

    // 해당 데이터베이스의 데이터값이 변경시에 onChange() method가 호출됩니다.
    private ContentObserver bookmarksObserver = new ContentObserver(new Handler()) {
        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
        }
    };

    public ContentResolver contentResolver;


    @Override
    public void onCreate() {
        super.onCreate();

        BaseApplication.context = getApplicationContext();
        BaseApplication.application = this;

        mApplicationComponent = DaggerApplicationComponent.builder().applicationModule(new ApplicationModule(this)).build();
        mApplicationComponent.inject(this);

        try {
            initData();
            initRegisterContentObserver();
            provideInit();
            this.contentResolver = context.getContentResolver();
            contentResolver.registerContentObserver(BOOKMARKS_URI, true, bookmarksObserver );
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
    }


    public void initRegisterContentObserver() {
        this.getApplicationContext().getContentResolver().registerContentObserver(MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        , false, dataChangedObserver);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public void initData () {
        NetSyncSdkHelper.getDefault().initContext(getAppContext());
        PreferencesCacheHelper.initContext(getAppContext());

        mEnterpriseCode = BaseConfigurationPerSite.getInstance().getEnterpriseCode();
        mEnterpriseName = BaseConfigurationPerSite.getInstance().getEnterpriseName();

        BaseConfiguration.getDefault().setApplicationContentId(provideApplicationContentId());
        BaseConfiguration.getDefault().setAppDialogBtnColor(provideDialogBtnColor());
        BaseConfiguration.getDefault().setStrCardManufacturer(provideCardManufacturer());

        BaseConfigurationPerSite.getInstance().setExternalSdPath(provideExternalSdPath());
        BaseConfigurationPerSite.getInstance().setHomeUrl(provideHomeWebViewUrl());
        BaseConfigurationPerSite.getInstance().setSubHomeUrl(provideSubHomeWebViewUrl());

        BaseConfigurationPerSite.getInstance().setAcquisitionUrl(provideAcquisitionUrl());
        BaseConfigurationPerSite.getInstance().setSerialAuthenticationUrl(provideSerialAuthenticationUrl());

        BaseConfigurationPerSite.getInstance().setPrivacyPolicyUrl(providePrivacyPolicyUrl());
        BaseConfigurationPerSite.getInstance().setHelpUrl(provideHelpUrl());
    }


    public static boolean initNcgSdk(Context context) {
        try {
            boolean init = NetSyncSdkHelper.getDefault().isInitialized();
            if (init) {
                initialized = true;
                return initialized;
            }

            String offLineCount = BaseConfigurationPerSite.getInstance().getOffLineCount();
            String deviceId = getCachedDeviceId();
            NetSyncSdkHelper.getDefault().initNcgSdk(context, deviceId, offLineCount);
            NetSyncSdkHelper.getDefault().setCardManufacturer(BaseConfiguration.getDefault().getStrCardManufacturer());
            initialized = true;
        }
        catch (Ncg2Exception ne) {
            initialized = false;
            Toast.makeText(context, ne.getMessage(), Toast.LENGTH_LONG).show();
        }
        catch (Exception e) {
            initialized = false;
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return initialized;
    }

    public ApplicationComponent getComponent() {
        return mApplicationComponent;
    }

    public void setComponent(ApplicationComponent applicationComponent) {
        mApplicationComponent = applicationComponent;
    }

    public void setAppState(String appState) {
        mAppState = appState;
    }

    public static Context getAppContext() {
        return application;
    }

    public static String getCachedDeviceId () {
        String deviceId = "";
        try {
            deviceId = PreferencesCacheHelper.getPreferenceValue(PreferencesCacheHelper.DEVICE_ID, "");
            if (StringUtils.isBlank(deviceId)) {
                deviceId = AndroidUtil.getDeviceID(context);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return deviceId;
    }


    public static String getCachedDeviceIdForPallyCond () {
        String deviceId = "";
        try {
            deviceId = PreferencesCacheHelper.getPreferenceValue(PreferencesCacheHelper.DEVICE_ID_FOR_PALLYCOND, "");
            if (StringUtils.isBlank(deviceId)) {
                deviceId = AndroidUtil.getDeviceIDForPallyCondSD(context);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return deviceId;
    }
    

    @Override
    public void onTerminate() {
        if (contentResolver != null && bookmarksObserver != null) {
            contentResolver.unregisterContentObserver(bookmarksObserver);
        }
        super.onTerminate();
    }


    public static List<String> provideEnableDeviceModels() {
        return new ArrayList<>();
    }

    protected void provideInit () {
    }

    protected String provideHomeWebViewUrl () {
        return "";
    }

    protected String provideSubHomeWebViewUrl () {
        return "";
    }

    protected String providePrivacyPolicyUrl () {
        return getString(R.string.privacy_policy_url);
    }

    protected String provideHelpUrl () {
        return getString(R.string.help_url);
    }

    protected String provideApplicationContentId () {
        return "";
    }

    protected String provideExternalSdPath () {
        return "";
    }

    protected String provideCardManufacturer () {
        return "";
    }

    protected String provideAcquisitionUrl () {
        return getString(R.string.acquisition_url);
    }

    protected String provideSerialAuthenticationUrl () {
        return getString(R.string.serial_authentication_url);
    }

    protected int provideDialogBtnColor () {
        return R.color.provider_default_color_dialog_btn_confirm;
    }

}