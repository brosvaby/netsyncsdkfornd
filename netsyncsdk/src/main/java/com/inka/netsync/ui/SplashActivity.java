package com.inka.netsync.ui;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.inka.ncg.nduniversal.common.NcgResponseCode;
import com.inka.ncg.nduniversal.exception.Ncg2CoreException;
import com.inka.ncg.nduniversal.hidden.CertificationHelper;
import com.inka.ncg.nduniversal.model.ResponseNcgEntry;
import com.inka.netsync.BaseApplication;
import com.inka.netsync.R;
import com.inka.netsync.R2;
import com.inka.netsync.common.AppConstants;
import com.inka.netsync.common.utils.AndroidUtil;
import com.inka.netsync.data.cache.pref.PreferencesCacheHelper;
import com.inka.netsync.logs.LogUtil;
import com.inka.netsync.permission.PermissionHelper;
import com.inka.netsync.permission.callback.OnPermissionCallback;
import com.inka.netsync.sd.ui.SDCertificationActivity;
import com.inka.netsync.ui.mvppresenter.SplashMvpPresenter;
import com.inka.netsync.ui.mvpview.SplashMvpView;
import com.inka.netsync.view.dialog.CommonAlertDialog;
import com.inka.netsync.view.dialog.CustomAlertDialog;

import java.util.Arrays;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by birdgang on 2017. 4. 17..
 */
public class SplashActivity extends BaseActivity implements SplashMvpView, OnPermissionCallback {

    private final String TAG = "SplashActivity";

    @Inject
    SplashMvpPresenter<SplashMvpView> mPresenter;

    private final static String SINGLE_PERMISSION = Manifest.permission.GET_ACCOUNTS;

    static final int DIALOG_WARNNING = 0;

    @BindView(R2.id.container_splash_screen)
    RelativeLayout mSplashContainer;

    @BindView(R2.id.img_splash_screen)
    ImageView mSplashImageView = null;

    protected Class<?> mNextClz = SDCertificationActivity.class;

    private final static String[] MULTI_PERMISSIONS = new String[] {
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CHANGE_NETWORK_STATE
    };

    private PermissionHelper permissiontHelper;

    private boolean isSingle = false;

    private CommonAlertDialog mCommonAlertDialog;

    private String[] neededPermission;

    protected final Runnable mPendingLauncherRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                startMainActivity(mNextClz);
            } catch (Ncg2CoreException e) {
            }
        }
    };

    protected void startMainActivity(Class<?> cls) throws Ncg2CoreException {
        try {
            Intent intent = new Intent(this, cls);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        } finally {
            finish();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        permissiontHelper = PermissionHelper.getInstance(this);
        mCommonAlertDialog = new CommonAlertDialog(SplashActivity.this);

        try {
            // 젤리빈 이상
            if (Build.VERSION.SDK_INT >= 17) {
                if (AndroidUtil.isAdminUser(this) == false) {
                    Toast.makeText(this, getString(R.string.message_for_toast_status_guest_user), Toast.LENGTH_LONG).show();
                    LogUtil.INSTANCE.info(TAG, "onCreate : " +  getString(R.string.message_for_toast_status_guest_user));
                    finish();
                    return;
                }
            }
        } catch (Exception e) {
            LogUtil.INSTANCE.info(TAG, "e.getMessage() : " +  e.getMessage());
        }

        // 중복실행 방지
        BaseApplication application = (BaseApplication) getApplication();

        LogUtil.INSTANCE.info("birdganglifecycl" , "SplashActivity > onCreate > application.mAppState : " + application.mAppState);

        if (application.mAppState == AppConstants.APPLICATION_STATE_DOWNLOAD) {
            Toast.makeText(this, getString(R.string.message_for_toast_status_downloading), Toast.LENGTH_LONG).show();
            LogUtil.INSTANCE.info(TAG, " onCreate : " + getString(R.string.message_for_toast_status_downloading));

            finish();
            return;
        } else if (application.mAppState == AppConstants.APPLICATION_STATE_PLAY) {
            Toast.makeText(this, getString(R.string.message_for_toast_status_playing), Toast.LENGTH_LONG).show();
            LogUtil.INSTANCE.info(TAG, " onCreate : " + getString(R.string.message_for_toast_status_playing));
            finish();
            return;
        }

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splashscreen_container);

        getActivityComponent().inject(this);
        setUnBinder(ButterKnife.bind(this));
        mPresenter.onAttach(SplashActivity.this);

        setUp();
    }


    @Override
    protected void setUp() {
        permissiontHelper.setForceAccepting(false).request(isSingle ? SINGLE_PERMISSION : MULTI_PERMISSIONS);
        int backgroundColorResource = provideSplashBackgroundColorResource();
        if (backgroundColorResource > 0) {
            mSplashContainer.setBackgroundColor(ContextCompat.getColor(this, backgroundColorResource));
        }

        int imageResource = provideSplashImageResource();
        if (-1 != imageResource) {
            mSplashImageView.setImageResource(imageResource);
            mSplashImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        LogUtil.INSTANCE.info("birdgangpermission", "onActivityResult > requestCode " + requestCode + " , resultCode : " + resultCode);
        permissiontHelper.onActivityForResult(requestCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        LogUtil.INSTANCE.info("birdgangpermission", "onRequestPermissionsResult > requestCode " + requestCode + " , permissions : " + permissions.toString());
        permissiontHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onPermissionGranted(@NonNull String[] permissionName) {
        LogUtil.INSTANCE.info("birdgangpermission", "onPermissionGranted > Permission(s) " + Arrays.toString(permissionName) + " Granted > isSingle : " + isSingle);
        // 업체별 전용 경고
        boolean initNcg2Sdk = BaseApplication.initNcgSdk(SplashActivity.this);
        LogUtil.INSTANCE.info("birdgangpermission", "onPermissionGranted > initNcg2Sdk : " + initNcg2Sdk);

        try {
            if (initNcg2Sdk) {
                setNextContentView();
                checkNotifyConnection();
            }
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
    }

    @Override
    public void onPermissionDeclined(@NonNull String[] permissionName) {
        LogUtil.INSTANCE.info("birdgangpermission", "onPermissionDeclined > Permission(s) " + Arrays.toString(permissionName) + " Declined");
        PreferencesCacheHelper.setPreferenceValueForBol(PreferencesCacheHelper.AUTO_RESCAN, true);
        finish();
    }

    @Override
    public void onPermissionPreGranted(@NonNull String permissionsName) {
        LogUtil.INSTANCE.info("birdgangpermission", "onPermissionPreGranted > Permission( " + permissionsName + " ) preGranted");
        boolean initNcg2Sdk = BaseApplication.initNcgSdk(SplashActivity.this);
        try {
            if (initNcg2Sdk) {
                setNextContentView();
                checkNotifyConnection();
            }
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
    }

    @Override
    public void onPermissionNeedExplanation(@NonNull final String permissionName) {
        LogUtil.INSTANCE.info("birdgangpermission", "onPermissionNeedExplanation > Permission( " + permissionName + " ) needs Explanation");
        if (!isSingle) {
            neededPermission = PermissionHelper.declinedPermissions(this, MULTI_PERMISSIONS);
            StringBuilder builder = new StringBuilder(neededPermission.length);
            if (neededPermission.length > 0) {
                for (String permission : neededPermission) {
                    builder.append(permission).append("\n");
                }
            }

            AlertDialog alert = mCommonAlertDialog.getAlertDialog(neededPermission, builder.toString(), new CommonAlertDialog.OnAlertClickListener() {
                @Override
                public void onClick(CommonAlertDialog alertDialog) {
                    permissiontHelper.requestAfterExplanation(neededPermission);
                }
            });

            if (!alert.isShowing()) {
                alert.show();
            }
        } else {
            AlertDialog alert = mCommonAlertDialog.getAlertDialog(permissionName, new CommonAlertDialog.OnAlertClickListener() {
                @Override
                public void onClick(CommonAlertDialog alertDialog) {
                    permissiontHelper.requestAfterExplanation(permissionName);
                }
            });
            alert.show();
        }
    }

    @Override
    public void onPermissionReallyDeclined(@NonNull String permissionName) {
        LogUtil.INSTANCE.info("birdgangpermission", "onPermissionReallyDeclined > Permission " + permissionName + " can only be granted from settingsScreen");
        boolean initNcg2Sdk = BaseApplication.initNcgSdk(SplashActivity.this);
        try {
            if (initNcg2Sdk) {
                setNextContentView();
                checkNotifyConnection();
            }
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
    }

    @Override
    public void onNoPermissionNeeded() {
        LogUtil.INSTANCE.info("birdgangpermission", "onNoPermissionNeeded > Permission(s) not needed");
        boolean initNcg2Sdk = BaseApplication.initNcgSdk(SplashActivity.this);

        try {
            if (initNcg2Sdk) {
                setNextContentView();
                checkNotifyConnection();
            }
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
    }

    protected void checkNotifyConnection() {
        int delay = Integer.valueOf(getResources().getInteger(provideSplashDelayTime()));
        mHandler.postDelayed(mPendingLauncherRunnable, delay);
    }


    @Override
    protected Dialog onCreateDialog(int id, Bundle args) {
        LogUtil.INSTANCE.info("birdgangpermission", "onCreateDialog > id : " + id);

        switch (id) {
            case DIALOG_WARNNING:
                return new CustomAlertDialog(this, CustomAlertDialog.CONFIRM_TYPE)
                        .setTitleText(getString(R.string.dialog_title_warning))
                        .setContentText(getString(R.string.bundle_key_dialog_message))
                        .setConfirmText(getString(R.string.dialog_ok))
                        .showCancelButton(false)
                        .setDialogTypeLarge()
                        .setConfirmClickListener(new CustomAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(CustomAlertDialog sDialog) {
                                try {
                                    sDialog.cancel();
                                } catch (Exception e) {
                                    e.getMessage();
                                }
                            }
                        });
            default:
                return null;
        }
    }

    @Override
    protected void onDestroy() {
        mPresenter.onDetach();
        super.onDestroy();
    }

    protected boolean hasRunIntroGuide () {
        return PreferencesCacheHelper.getPreferenceValueForBol(PreferencesCacheHelper.HAS_RUN_INTRO_GUIDE, false);
    }

    protected void setNextContentView () throws Ncg2CoreException {
        CertificationHelper.getDefault().initCertification(this, Build.VERSION.SDK_INT);
        ResponseNcgEntry responseNcgEntry = CertificationHelper.getDefault().checkAvailCard();

        int resultCode = responseNcgEntry.getResultCode();
        if (NcgResponseCode.FAIL == resultCode) {
            mNextClz = provideNextContentView(false);
        }
        else {
            boolean isHasNotuse = responseNcgEntry.isHasNotuse();
            boolean isHasPallyconsd = responseNcgEntry.isHasPallyconsd();

            LogUtil.INSTANCE.info("birdgangauth", "setNextContentView > isHasNotuse : " + isHasNotuse + " , isHasPallyconsd : " + isHasPallyconsd);
            if (isHasNotuse || !isHasPallyconsd) {
                mNextClz = provideNextContentView(false);
            }
            else {
                mNextClz = provideNextContentView(true);
            }
        }
    }

    protected Class<?> provideNextContentView (boolean hasNext) {
        return null;
    }

    protected int provideSplashDelayTime() {
        return R.integer.provider_default_splash_delay;
    }

    protected int provideSplashImageResource() {
        return R.drawable.c_splash_screen;
    }

    protected int provideSplashBackgroundColorResource() {
        return R.color.provider_default_color_splash_background;
    }

}
