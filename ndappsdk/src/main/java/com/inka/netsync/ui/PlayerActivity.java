package com.inka.netsync.ui;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.PersistableBundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.VelocityTracker;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.inka.ncg.nduniversal.ModuleConfig;
import com.inka.ncg.nduniversal.exception.Ncg2CoreException;
import com.inka.ncg2.Ncg2Agent;
import com.inka.ncg2.Ncg2Exception;
import com.inka.ncg2.Ncg2LocalWebServer;
import com.inka.ncg2.Ncg2SdkFactory;
import com.inka.ncg2.playerlib.Ncg2Player;
import com.inka.ncg2.playerlib.Ncg2PlayerLibFactory;
import com.inka.netsync.BaseApplication;
import com.inka.netsync.BaseConfiguration;
import com.inka.netsync.R;
import com.inka.netsync.R2;
import com.inka.netsync.common.AppConstants;
import com.inka.netsync.common.IntentParams;
import com.inka.netsync.common.bus.ClickListener;
import com.inka.netsync.common.bus.EventBus;
import com.inka.netsync.common.utils.AndroidUtil;
import com.inka.netsync.common.utils.AnimUtils;
import com.inka.netsync.common.utils.DateTimeUtil;
import com.inka.netsync.common.utils.FileUtil;
import com.inka.netsync.common.utils.StringUtil;
import com.inka.netsync.common.utils.ThemeUtil;
import com.inka.netsync.controler.BookmarkControler;
import com.inka.netsync.controler.ContentControler;
import com.inka.netsync.controler.FavoriteControler;
import com.inka.netsync.controler.RecentlyPlayedListControler;
import com.inka.netsync.controler.SettingControler;
import com.inka.netsync.data.cache.pref.PreferencesCacheHelper;
import com.inka.netsync.lms.LmsControler;
import com.inka.netsync.lms.model.LmsEntry;
import com.inka.netsync.logs.FileLogUtil;
import com.inka.netsync.logs.LogUtil;
import com.inka.netsync.model.BookmarkEntry;
import com.inka.netsync.model.ContentEntry;
import com.inka.netsync.model.FavoriteEntry;
import com.inka.netsync.model.RecentlyEntry;
import com.inka.netsync.ncg.NetSyncSdkHelper;
import com.inka.netsync.receiver.EarPhoneChangeReceiver;
import com.inka.netsync.ui.mvppresenter.PlayerMvpPresenter;
import com.inka.netsync.ui.mvpview.PlayerMvpView;
import com.inka.netsync.view.ProgressManagerClient;
import com.inka.netsync.view.adapter.PagerGuideAdapter;
import com.inka.netsync.view.dialog.AddBookMarkAlertDialog;
import com.inka.netsync.view.dialog.CustomAlertDialog;
import com.inka.netsync.view.dialog.ListBookMarkAlertDialog;
import com.inka.netsync.view.dialog.PlayerSettingDialog;
import com.inka.netsync.view.model.BookMarkViewEntry;
import com.inka.netsync.view.model.GuideViewEntry;
import com.inka.netsync.view.pager.ScrollerViewPager;
import com.inka.netsync.view.pager.indicator.SpringIndicator;
import com.inka.netsync.view.widget.CheckableImageView;
import com.inka.netsync.view.widget.MarqueeTextView;
import com.inka.netsync.view.widget.MultiCheckableImageView;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PlayerActivity extends BaseActivity implements PlayerMvpView {

    private final String TAG = "PlayerActivity";

    protected static final int UI_HIDE_MAX_COUNT = 20;
    protected static final int EARPHONECHANGE_HANDLER_REMOTE_CLICKED = 0;
    protected int SKIP_TIME = 0;        // 업체별 스킵 구간 설정

    @Inject
    PlayerMvpPresenter<PlayerMvpView> mPresenter;

    private String PLAY_TYPE = AppConstants.TYPE_PLAY_DOWNLOAD;
    private String SWXPLAY_TYPE = AppConstants.TYPE_SWXPLAY_NO_ALLOW;
    private String PLAY_STATE = AppConstants.STATE_PLAYT_ONSTOP;

    
    // View 설정정보
    protected int mRepeatMode = -1;
    protected int mRepeatAreaState = -1;

    private int mUIHideCountingDown = UI_HIDE_MAX_COUNT;
    protected int mSkipTime = 10000;
    protected int mScreenRate = 0;
    protected int mRepeatAPosition = -1;
    protected int mRepeatBPosition = -1;

    private Ncg2Player.DisplayMode mCurrentDisplayMode = Ncg2Player.DisplayMode.FullScreenWithKeepRatio;


    /**
     * intent params
     */
    private String mUserId;
    private int mContentId;
    private String mNcgFilePath = "";
    private String mPlayerType = AppConstants.TYPE_PLAYER_VISUALONPLAYER;
    private String mNcgFileName = "";
    private long mNcgFileSize = 0;
    private int mPlayListSize = -1;
    private boolean mIsAllowCellularNetwork = false;

    /**
     * property
     */
    private Ncg2Player mNcg2Player = null;
    private GestureDetector mGestureDetector;
    private VelocityTracker mVelocityTracker;

    private PowerManager.WakeLock mWakeLock = null;

    private UnlockReceiver mUnlockReceiver = null;
    private NetworkChangeReceiver mNetworkReceiver = null;
    private TelephonyManager mTelephonyManager = null;

    //Touch Events
    private static final int TOUCH_NONE = 0;
    private static final int TOUCH_VOLUME = 1;
    private static final int TOUCH_BRIGHTNESS = 2;
    private static final int TOUCH_SEEK = 3;
    private int mTouchAction = TOUCH_NONE;
    private int mCurrentPlayIndex = -1;
    private int mSurfaceYDisplayRange;
    private float mInitTouchY, mTouchY = -1f, mTouchX = -1f;
    private int mDecoderType = 0;


    //Volume
    private AudioManager mAudioManager;
    private int mAudioMax;
    private float mVol;

    // Brightness
    private boolean mIsFirstBrightnessGesture = true;
    private float mRestoreAutoBrightness = -1f;


    // 초기화 상태 정보
    private boolean mbAvailVideoRate = false;
    private boolean mIsSeekBarTrackingNow = false;
    private boolean mEnableTouchScreenLock = false;   // 스크린락 상태인지 확인
    private boolean mOverlayShowing = false;        // 현재 오버레이가 보여지고 있는 상태인지 확인
    private boolean mShownGuide = false;
    private boolean mRestrictInternet = false;


    /***
     *  model resource
     */
    private ContentEntry mContentEntry;
    private BookmarkEntry mBookmarkEntry;

    private ArrayList<ContentEntry> mContentEntries = null;
    private ArrayList<ContentEntry> mPlayedListList = null;
    private ArrayList<ContentEntry> mFavoriteList = null;

    private Timer mTimeCheckTimer = null;
    private TimerTask mTimeCheckTimerTask = null;

    private ComponentName mRemoteComponent = null;

    /***
     * view
     */
    @BindView(R2.id.player_footer_container)
    RelativeLayout mFooterViewGroup;

    @BindView(R2.id.player_controler_container)
    RelativeLayout mControlerViewGroup;

    @BindView(R2.id.container_pos_on_player)
    RelativeLayout mControlerViewPosOnPlayer;

    @BindView(R2.id.container_pos_and_seekbar_on_player)
    RelativeLayout mControlerViewPosAndSeekBarOnPlayer;

    @BindView(R2.id.container_touch_lockscreen_hor)
    RelativeLayout mControlerViewTouchLockscreenHor;

    @BindView(R2.id.container_controler_on_player)
    RelativeLayout mControlerViewControlerOnPlayer;

    @BindView(R2.id.player_container_actionbar_and_setting)
    RelativeLayout mHeaderAndSettingViewGroup;

    @BindView(R2.id.player_container_footer_and_controler)
    LinearLayout mFooterAndControlerViewGroup;

    @BindView(R2.id.player_guide_container)
    FrameLayout mGuideViewGroup;

    @BindView(R2.id.container_speed_on_player)
    LinearLayout mSpeedViewGroup;

    @BindView(R2.id.container_extra_option_on_player)
    LinearLayout mExtraViewGroup;

    @BindView(R2.id.container_surfaceview)
    FrameLayout mFrameLayoutPlayerContainer;

    @BindView(R2.id.btn_playnpause)
    ToggleButton mPlayAndPause;

    @BindView(R2.id.img_company_logo)
    ImageView mIvCompanyLogo;

    @BindView(R2.id.repeat_point_a)
    ImageView mIvRepeatA;

    @BindView(R2.id.repeat_point_b)
    ImageView mIvRepeatB;

    @BindView(R2.id.btn_favorite)
    ToggleButton mTbIsFavorite;

    @BindView(R2.id.seek_bar_on_player)
    SeekBar mSeekBar;

    @BindView(R2.id.text_player_title)
    MarqueeTextView mMarqueeTextView;

    @BindView(R2.id.tx_play_speed)
    TextView mTxtPlaySpeed;

    @BindView(R2.id.img_touch_lockscreen)
    CheckableImageView mImgTouchLockScreen;

    @BindView(R2.id.img_touch_lockscreen_hor)
    CheckableImageView mImgTouchLockScreenHor;

    @BindView(R2.id.img_repeat_mode)
    MultiCheckableImageView mImgRepeatMode;

    @BindView(R2.id.img_repeat_area)
    MultiCheckableImageView mImgRepeatArea;

    @BindView(R2.id.img_repeat_mode_hor)
    MultiCheckableImageView mImgRepeatModeHor;

    @BindView(R2.id.img_repeat_area_hor)
    MultiCheckableImageView mImgRepeatAreaHor;

    @BindView(R2.id.player_overlay_info)
    TextView mTextOverlayInfo;

    @BindView(R2.id.posTextView)
    TextView mTextPositionView;

    @BindView(R2.id.durTextView)
    TextView mTextDurationView;

    @BindView(R2.id.decoderTextView)
    TextView mTextDecoderView;

    @BindView(R2.id.img_audioplayer_background)
    ImageView mImageAudioPlayerBackground;

    @BindView(R2.id.img_setting)
    ImageView mImgSetting;

    @BindView(R2.id.scroller_view_pager)
    protected ScrollerViewPager mScrollerViewPager;

    @BindView(R2.id.indicator)
    protected SpringIndicator indicator;

    @BindView(R2.id.btn_file_advance)
    Button mBtnFileAdvance;

    @BindView(R2.id.btn_file_retour)
    Button mBtnFileRetour;


    protected RelativeLayout.LayoutParams mParamRepeatA = null;
    protected RelativeLayout.LayoutParams mParamRepeatB = null;

    // Play 정보
    private DataSourceSetupTask mDataSourceSetupTask = null;
    private static boolean mIsSurfaceCreated;
    private static boolean mIsForeground;
    private int mCurrentPosition = 0;
    private int mPlayDuration = 0;
    private int mLastPlayTime = -1;
    private int mPlaySpeed = 0;
    private int mExpectedCurrentEndTime = 0;

    private boolean mIsSkipErrorHandling = false;
    private boolean mIsPauseded = false;
    private boolean mIsSuspended = false;
    private boolean mIsPrepared = false;
    private boolean mIsRealPlaying = true;
    private boolean mIsSurfaceSenario = false;
    private boolean mIsDownloadComplete = false;
    private boolean mIsReadyUnlock = true;
    private boolean mEnableGesture = false;
    private boolean mEnableRestrictedInternet = false;

    private PlayerNavigatorFactory mPlayerNavigatorFactory = new PlayerNavigatorFactory();
    private PlayerNavigator mPlayerNavigator;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_player_container);

        getActivityComponent().inject(this);
        setUnBinder(ButterKnife.bind(this));
        mPresenter.onAttach(this);

        ProgressManagerClient.getInstance().showProgress(this, getString(R.string.dialog_progress_initializing_player));

        mAudioManager = (AudioManager) getApplicationContext().getSystemService(AUDIO_SERVICE);
        mAudioMax = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

        mEnableGesture = SettingControler.getDefault().getEnableGestureOnPlayer();
        mEnableRestrictedInternet = SettingControler.getDefault().getEnableRestrictedInternet();

        try {
            loadContent();
            setUp();
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
            finish();
        }
    }


    @Override
    protected void setUp() {
        initPlayList();
        initNcgPlayer();
        initRotation();
        loadCacheData ();
        initUI();
        updateInfo();
        setupWakeLock();
        setupPhoneListener();
        registerUnlockReceiver();
        registerNetworkChangeReceiver();
        setupAirplaneMode();
    }


    public void loadContent() {
        try {
            Intent intent = getIntent();

            // 플레이어 타입
            mPlayerType = intent.getStringExtra(IntentParams.MEDIA_PLAYER_TYPE);

            // S/W 배속
            SWXPLAY_TYPE = intent.getStringExtra(IntentParams.MEDIA_SWX_PLAY);

            // 플레이 타입
            PLAY_TYPE = intent.getStringExtra(IntentParams.MEDIA_PLAY_TYPE);

            // 유저 아이디
            mUserId = intent.getStringExtra(IntentParams.USER_ID);

            // 컨텐츠 아이디
            mContentId = intent.getIntExtra(IntentParams.MEDIA_CONTENT_ID, -1);

            // 파일 경로
            mNcgFilePath = intent.getStringExtra(IntentParams.MEDIA_ITEM_LOCATION);

            // 플레이어 가이드 유무
            mShownGuide = intent.getBooleanExtra(IntentParams.MEDIA_SHOWN_PLAYER_GUIDE, false);

            if (mNcgFilePath == null || mNcgFilePath.length() == 0) {
                ContentEntry contentEntry = ContentControler.getDefault().findContentById(mContentId);
                mNcgFilePath = contentEntry.getContentFilePath();
                if (StringUtils.isBlank(mNcgFilePath)) {
                    throw new RuntimeException("'path' param must be provided.");
                }
            } else {
                ContentEntry contentEntry = ContentControler.getDefault().findContentByFilePath(mNcgFilePath);
                mContentId = contentEntry.getContentId();
                if (mContentId <= -1) {
                    throw new RuntimeException("'contentId' param must be provided.");
                }
            }

            LogUtil.INSTANCE.info("birdgangplaypathparams", "mNcgFilePath : " + mNcgFilePath + " , mContentId : " + mContentId + " , mUserId : " + mUserId);

            // 파일 사이즈
            mNcgFileSize = intent.getLongExtra("fileSize", 0L);

            LogUtil.INSTANCE.info(TAG, "mNcgFilePath : " + mNcgFilePath + " , mUserId : " + mUserId);

            mContentEntry = ContentControler.getDefault().findContentById(mContentId);
            mBookmarkEntry = intent.getExtras().getParcelable(IntentParams.MEDIA_BOOKMARK);

            LogUtil.INSTANCE.info("birdgangplaypathparams", "mContentEntry : " + mContentEntry.toString());

            // 강의 이름
            mNcgFileName = mContentEntry.getContentName();

            LogUtil.INSTANCE.info("birdgangplaypathparams", "mPlayerType : " + mPlayerType + " , SWXPLAY_TYPE : " + SWXPLAY_TYPE + " , PLAY_TYPE : " + PLAY_TYPE
                    + " , mUserId: " + mUserId + " , mNcgFilePath : " + mNcgFilePath + " , mNcgFileName : " + mNcgFileName
                    + " , mContentId : " + mContentId);

        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }

    }


    /**************
     * activity lifecycle
     **************/
    @Override
    protected void onResume() {
        super.onResume();
        registerEarPhoneChangeReceiver();
        setVisibleOverlay(View.VISIBLE);

        if (StringUtils.equals(PLAY_STATE, AppConstants.STATE_PLAYT_ONPAUSE)) {
            PLAY_STATE = AppConstants.STATE_PLAYT_ONRESUME;
        }

        mIsForeground = true;
    }


    @Override
    protected void onStart() {
        super.onStart();
        BaseApplication application = (BaseApplication) getApplication();
        application.setAppState(AppConstants.APPLICATION_STATE_PLAY);

        try {
            boolean enableBrightness = true;
            if (enableBrightness) {
                float brightness = PreferencesCacheHelper.getPreferenceValueForFloat(PreferencesCacheHelper.SCREEN_BRIGHTNESS, -1);
                if (brightness != -1f) {
                    setWindowBrightness(brightness);
                }
            }
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }

    }


    @Override
    protected void onPause() {
        super.onPause();
        if (mAudioManager != null) {
            mAudioManager.abandonAudioFocus(mAudioFocusChangeListener);
            mAudioManager.unregisterMediaButtonEventReceiver(mRemoteComponent);
        }

        if (mIsPrepared == false) {
            // 재생 하자마자 BG 로 넘기는 경우
            closePlayer();
        } else {
            mPlayerNavigator.doPause();
        }

    }


    @Override
    protected void onDestroy() {
        try {
            mAudioManager = null;
            restoreBrightness();
            if (ProgressManagerClient.getInstance().isShowProgress()) {
                ProgressManagerClient.getInstance().stopProgress(this);
            }

            if (null != mPlayerNavigator) {
                mPlayerNavigator.doDestroy();
            }
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        } finally {
            super.onDestroy();
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void finish() {
        super.finish();
    }

    public void executeDataSourceSetupTask() {
        if (mDataSourceSetupTask != null && mDataSourceSetupTask.getStatus() == DataSourceSetupTask.Status.RUNNING) {
            return;
        }

        mDataSourceSetupTask = new DataSourceSetupTask();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mDataSourceSetupTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            mDataSourceSetupTask.execute();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        try {
            if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
                fillContentVerticalLayout();
            } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                fillContentHorizontalalLayout();
            }
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }

        mIvRepeatA.setVisibility(View.INVISIBLE);
        mIvRepeatB.setVisibility(View.INVISIBLE);

        setVisibleOverlay(View.VISIBLE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mIsForeground) {
                    convertRepeatA();
                    convertRepeatB();
                }
            }
        }, 1500);
    }


    private void fillContentVerticalLayout() throws Exception {
        int checkedState = mImgRepeatModeHor.isCheckedState();
        mImgRepeatMode.setCheckedStateRepeatMode(checkedState);
        int checkedStateArea = mImgRepeatAreaHor.isCheckedState();
        mImgRepeatArea.setCheckedStateArea(checkedStateArea);
        boolean checkedStateLock = mImgTouchLockScreenHor.isChecked();
        mImgTouchLockScreen.setChecked(checkedStateLock);

        //LogUtil.INSTANCE.info("birdgangplayerconfiguration",  "createVerticalLayout > checkedState : " + checkedState + " , checkedStateArea : " + checkedStateArea + " , checkedStateLock : " + checkedStateLock);

        mImgRepeatMode.setVisibility(View.VISIBLE);
        mImgRepeatArea.setVisibility(View.VISIBLE);
        mImgTouchLockScreen.setVisibility(View.VISIBLE);
        mImgRepeatModeHor.setVisibility(View.GONE);
        mImgRepeatAreaHor.setVisibility(View.GONE);
        mImgTouchLockScreenHor.setVisibility(View.GONE);
        mControlerViewTouchLockscreenHor.setVisibility(View.GONE);
    }

    private void fillContentHorizontalalLayout() throws Exception {
        int checkedState = mImgRepeatMode.isCheckedState();
        mImgRepeatModeHor.setCheckedStateRepeatMode(checkedState);
        int checkedStateArea = mImgRepeatArea.isCheckedState();
        mImgRepeatAreaHor.setCheckedStateArea(checkedStateArea);
        boolean checkedStateLock = mImgTouchLockScreen.isChecked();
        mImgTouchLockScreenHor.setChecked(checkedStateLock);

        //LogUtil.INSTANCE.info("birdgangplayerconfiguration",  "createHorizontalalLayout > checkedState : " + checkedState + " , checkedStateArea : " + checkedStateArea + " , checkedStateLock : " + checkedStateLock);

        mImgRepeatMode.setVisibility(View.GONE);
        mImgRepeatArea.setVisibility(View.GONE);
        mImgTouchLockScreen.setVisibility(View.GONE);
        mImgRepeatModeHor.setVisibility(View.VISIBLE);
        mImgRepeatAreaHor.setVisibility(View.VISIBLE);
        mImgTouchLockScreenHor.setVisibility(View.VISIBLE);
        mControlerViewTouchLockscreenHor.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        if (mEnableTouchScreenLock) {
            showInfo(R.string.player_locked, 1000);
            return;
        }

        if (mIsPrepared == true) {
            closePlayer();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x_changed, y_changed = 0.0f;
        float coef = 0.0f;
        float xgesturesize = 0.0f;
        float delta_y = 0.0f;

        DisplayMetrics screen = new DisplayMetrics();

        try {
            if (null != mGestureDetector) {
                boolean gesture = mGestureDetector.onTouchEvent(event);
            } else {
                mGestureDetector = new GestureDetector(this, new VideoOnGestureListener());
                mGestureDetector.setOnDoubleTapListener(mDoubleTapListener);        // register double tap listener
            }

            if (mVelocityTracker == null) {
                mVelocityTracker = VelocityTracker.obtain();
            }

            mVelocityTracker.addMovement(event);

            getWindowManager().getDefaultDisplay().getMetrics(screen);

            if (mSurfaceYDisplayRange == 0) {
                mSurfaceYDisplayRange = Math.min(screen.widthPixels, screen.heightPixels);
            }

            if (mTouchX != -1f && mTouchY != -1f) {
                y_changed = event.getRawY() - mTouchY;
                x_changed = event.getRawX() - mTouchX;
            } else {
                x_changed = 0f;
                y_changed = 0f;
            }

            // coef is the gradient's move to determine a neutral zone
            coef = Math.abs(y_changed / x_changed);
            xgesturesize = ((x_changed / screen.xdpi) * 2.54f);
            delta_y = Math.max(1f, (Math.abs(mInitTouchY - event.getRawY()) / screen.xdpi + 0.5f) * 2f);
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }


        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                try {
                    mTouchY = mInitTouchY = event.getRawY();
                    mVol = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                    mTouchAction = TOUCH_NONE;
                    // Seek
                    mTouchX = event.getRawX();
                    // Mouse events for the core
                } catch (Exception e) {
                    LogUtil.INSTANCE.error(TAG, e);
                }
                break;

            case MotionEvent.ACTION_MOVE:
                try {
                    mVelocityTracker.computeCurrentVelocity(1);
                    float velocity = mVelocityTracker.getXVelocity();
                    if (mTouchAction != TOUCH_SEEK && coef > 2) {
                        if (Math.abs(y_changed / mSurfaceYDisplayRange) < 0.05) {
                            return false;
                        }
                        mTouchY = event.getRawY();
                        mTouchX = event.getRawX();
                        // Volume (Up or Down - Right side)
                        if (mEnableGesture && !mEnableTouchScreenLock && (int) mTouchX > (3 * screen.widthPixels / 5)) {
                            doVolumeTouch(y_changed);
                            setVisibleOverlay(View.GONE);
                        }
                        // Brightness (Up or Down - Left side)
                        else if (mEnableGesture && !mEnableTouchScreenLock && (int) mTouchX < (2 * screen.widthPixels / 5)) {
                            doBrightnessTouch(y_changed);
                            setVisibleOverlay(View.GONE);
                        }
                    }

                    if (mEnableGesture && !mEnableTouchScreenLock) {
                        if (Math.abs(velocity) < 5) {
                            doSeekTouch(Math.round(delta_y), xgesturesize, false);
                        } else {
                            doSeekTouchSkip(Math.round(coef), xgesturesize, false);
                        }
                    }
                    //LogUtil.INSTANCE.info("birdgangtouchscreen", "onTouchEvent > coef : " + coef + " , mTouchY : " + mTouchY + " , mTouchX : " + mTouchX);
                } catch (Exception e) {
                    LogUtil.INSTANCE.error(TAG, e);
                }
                break;

            case MotionEvent.ACTION_UP:
                try {
                    if (mTouchAction == TOUCH_SEEK) {
                        float velocity2 = mVelocityTracker.getXVelocity();
                        if (Math.abs(velocity2) < 5) {
                            doSeekTouch(Math.round(delta_y), xgesturesize, true);
                        } else {
                            doSeekTouchSkip(Math.round(delta_y), xgesturesize, true);
                        }
                    }
                } catch (Exception e) {
                    LogUtil.INSTANCE.error(TAG, e);
                }

                mTouchX = -1f;
                mTouchY = -1f;
                break;
        }

        return mTouchAction != TOUCH_NONE;
    }


    private boolean doVolumeTouch(float y_changed) {
        boolean result = false;
        if (mTouchAction != TOUCH_NONE && mTouchAction != TOUCH_VOLUME) {
            return false;
        }
        float delta = -((y_changed / mSurfaceYDisplayRange) * mAudioMax);
        mVol += delta;
        int vol = (int) Math.min(Math.max(mVol, 0), mAudioMax);
        if (delta != 0f) {
            setAudioVolume(vol);
            result = true;
        }
        return result;
    }

    protected void doVolumeKey(int y_changed) {
        mVol = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        int vol = (int) Math.min(Math.max(mVol + y_changed, 0), mAudioMax);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, vol, 0);
        mTouchAction = TOUCH_VOLUME;
        showInfoWithDisplay(Integer.toString(vol), 1000, R.drawable.img_audio_line);
    }

    private void setAudioVolume(int vol) {
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, vol, 0);
        int newVol = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        if (vol != newVol) {
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, vol, AudioManager.FLAG_SHOW_UI);
        }
        mTouchAction = TOUCH_VOLUME;
        showInfoWithDisplay(Integer.toString(vol), 1000, R.drawable.img_audio_line);
    }


    private void initBrightnessTouch() {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        float brightnesstemp = lp.screenBrightness != -1f ? lp.screenBrightness : 0.6f;
        try {
            int screenBrightnessMode = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE);
            if (screenBrightnessMode == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC) {
                Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
                mRestoreAutoBrightness = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS) / 255.0f;
            } else if (brightnesstemp == 0.6f) {
                brightnesstemp = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS) / 255.0f;
            }
            mIsFirstBrightnessGesture = false;
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        lp.screenBrightness = brightnesstemp;
        getWindow().setAttributes(lp);
    }


    private boolean doBrightnessTouch(float y_changed) {
        boolean result = false;
        if (mTouchAction != TOUCH_NONE && mTouchAction != TOUCH_BRIGHTNESS) {
            return result;
        }

        if (mIsFirstBrightnessGesture) {
            initBrightnessTouch();
        } else {
            mTouchAction = TOUCH_BRIGHTNESS;

            try {
                // Set delta : 2f is arbitrary for now, it possibly will change in the future
                float delta = -y_changed / mSurfaceYDisplayRange;
                if (delta > 0.0001f || delta < -0.0001f) {
                    changeBrightness(delta);
                    result = true;
                }
            } catch (Exception e) {
                LogUtil.INSTANCE.error(TAG, e);
            }
        }
        return result;
    }

    private void changeBrightness(float delta) throws Exception {
        LogUtil.INSTANCE.info("birdganglifecycl" , "PlayerActivity > changeBrightness");
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        float brightness = Math.min(Math.max(lp.screenBrightness + delta, 0.01f), 1f);
        setWindowBrightness(brightness);
        brightness = Math.round(brightness * 100);
        showInfoWithDisplay("" + Math.round(lp.screenBrightness * 15), 1000, R.drawable.img_bright_line);
    }

    private void setWindowBrightness(float brightness) throws Exception {
        LogUtil.INSTANCE.info("birdganglifecycl" , "PlayerActivity > setWindowBrightness");

        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.screenBrightness = brightness;
        getWindow().setAttributes(lp);
    }


    @TargetApi(Build.VERSION_CODES.FROYO)
    private void restoreBrightness() {
        if (mRestoreAutoBrightness != -1f) {
            int brightness = (int) (mRestoreAutoBrightness * 255f);
            Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, brightness);
            Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
        }
        boolean enableBrightness = true;
        if (enableBrightness) {
            float brightness = getWindow().getAttributes().screenBrightness;
            if (brightness != -1f) {
                PreferencesCacheHelper.setPreferenceValueForFloat(PreferencesCacheHelper.SCREEN_BRIGHTNESS, brightness);
            }
        }
    }


    protected long getDuration () {
        long length = mNcg2Player.getDuration();
        return length;
    }

    protected long getTime () {
        long time = mNcg2Player.getCurrentPosition();
        return time;
    }


    private void doSeekTouch(int coef, float gesturesize, boolean seek) {
        if (coef == 0) {
            coef = 1;
        }

        try {
            if (Math.abs(gesturesize) < 1) {
                return;
            }

            if (mTouchAction != TOUCH_NONE && mTouchAction != TOUCH_SEEK) {
                return;
            }
            mTouchAction = TOUCH_SEEK;

            long length = mNcg2Player.getDuration();
            long time = mNcg2Player.getCurrentPosition();

            // Size of the jump, 10 minutes max (600000), with a bi-cubic progression, for a 8cm gesture
            int jump = (int) ((Math.signum(gesturesize) * ((600000 * Math.pow((gesturesize / 8), 4)) + 3000)) / coef);

            // Adjust the jump
            if ((jump > 0) && ((time + jump) > length)) {
                jump = (int) (length - time);
            }
            if ((jump < 0) && ((time + jump) < 0)) {
                jump = (int) -time;
            }

            //Jump !
            LogUtil.INSTANCE.info("birdgangseek", "doSeekTouch > seek : " + seek + " , jump : " + jump + " , length : " + length);
            if (seek && length > 0) {
                seek(time + jump, length);
            }
            if (length > 0) {
                showInfo(String.format("%s%s (%s)%s", jump >= 0 ? "+" : "", StringUtil.millisToString(jump), StringUtil.millisToString(time + jump), coef > 1 ? String.format(" x%.1g", 1.0 / coef) : ""), 1000);
            }
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
    }

    private void doSeekTouchSkip(int coef, float gesturesize, boolean seek) {
        LogUtil.INSTANCE.info("birdgangseek", "doSeekTouchSkip > coef : " + coef + " , seek : " + seek + " , mTouchAction : " + mTouchAction);

        if (coef == 0) {
            coef = 1;
        }

        try {
            if (Math.abs(gesturesize) < 1) {
                return;
            }

            if (mTouchAction != TOUCH_NONE && mTouchAction != TOUCH_SEEK) {
                return;
            }
            mTouchAction = TOUCH_SEEK;

            long length = mNcg2Player.getDuration();
            long time = mNcg2Player.getCurrentPosition();

            // Size of the jump, 10 minutes max (600000), with a bi-cubic progression, for a 8cm gesture
            float signum = Math.signum(gesturesize);

            int videoSeekSkipTime = 10;

            int skipTime = 1000 * videoSeekSkipTime;

            int jump = 1;

            if (signum > 0) {
                jump = skipTime;
            } else {
                jump = -skipTime;
            }

            // Adjust the jump
            if ((jump > 0) && ((time + jump) > length)) {
                jump = (int) (length - time);
            }

            if ((jump < 0) && ((time + jump) < 0)) {
                jump = (int) -time;
            }

            //Jump !
            if (seek && length > 0) {
                seek(time + jump, length);
            }
            if (length > 0) {
                showInfo(String.format("%s%s (%s)%s", jump >= 0 ? "+" : "", StringUtil.millisToString(jump), StringUtil.millisToString(time + jump), coef > 1 ? String.format(" x%.1g", 1.0 / coef) : ""), 1000);
            }
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
    }

    private void seek(long position) {
        seek(position, mNcg2Player.getDuration());
    }

    private void seek(long position, float length) {
        seekTo((int) position);
    }


    private void showInfoWithDisplay(String text, int duration, int image) {
        LogUtil.INSTANCE.info("birdgangoverlay", "showInfoWithDisplay > text : " + text + " , duration : " + duration + ", image : " + image);
        mTextOverlayInfo.setCompoundDrawablesWithIntrinsicBounds(image, 0, 0, 0);
        mTextOverlayInfo.setVisibility(View.VISIBLE);
        mTextOverlayInfo.setText(text);
        mOverlayHandler.removeMessages(IntentParams.FADE_OUT_INFO);
        mOverlayHandler.sendEmptyMessageDelayed(IntentParams.FADE_OUT_INFO, duration);
    }


    private void showInfo(String text, int duration) {
        LogUtil.INSTANCE.info("birdgangoverlay", "showInfo > text : " + text + " , duration : " + duration);
        mTextOverlayInfo.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        mTextOverlayInfo.setVisibility(View.VISIBLE);
        mTextOverlayInfo.setText(text);
        mOverlayHandler.removeMessages(IntentParams.FADE_OUT_INFO);
        mOverlayHandler.sendEmptyMessageDelayed(IntentParams.FADE_OUT_INFO, duration);
    }


    private void showInfo(int textid, int duration) {
        LogUtil.INSTANCE.info("birdgangoverlay", "showInfo > textid : " + textid + " , duration : " + duration);
        mTextOverlayInfo.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        mTextOverlayInfo.setVisibility(View.VISIBLE);
        mTextOverlayInfo.setText(textid);
        mOverlayHandler.removeMessages(IntentParams.FADE_OUT_INFO);
        mOverlayHandler.sendEmptyMessageDelayed(IntentParams.FADE_OUT_INFO, duration);
    }


    protected class DataSourceSetupTask extends AsyncTask<Void, Void, Void> {

        String errorMsg = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgressManagerClient.getInstance().showProgress(PlayerActivity.this, getString(R.string.dialog_progress_initializing_player));
        }

        @Override
        protected Void doInBackground(Void... params) {
            synchronized (this) {
                try {
                    LogUtil.INSTANCE.info("birdganglifecycl", "DataSourceSetupTask > mIsSurfaceSenario  : " + mIsSurfaceSenario + " , mIsSurfaceCreated : " + mIsSurfaceCreated + " , mNcgFilePath : " + mNcgFilePath);

                    if (mIsSurfaceSenario == true && mIsSurfaceCreated == false) {
                        errorMsg = getString(R.string.player_error_surface_calling);
                        return null;
                    }

                    File temp = new File(mNcgFilePath);
                    if (null != temp && temp.isFile()) {
                        LogUtil.INSTANCE.info(TAG, "DataSourceSetupTask > null != temp && temp.isFile()");
                    }

                    mNcg2Player.setDataSource(mNcgFilePath);
                    setPreparedData();
                    mNcg2Player.prepareAsync();
                    LogUtil.INSTANCE.info("birdganglifecycl", "DataSourceSetupTask > doInBackground > did mNcg2Player.prepareAsync() > path : " + mNcgFilePath);
                } catch (Exception e) {
                    LogUtil.INSTANCE.error(TAG, e);
                    errorMsg = e.getMessage();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            LogUtil.INSTANCE.info(TAG, "DataSourceSetupTask > onPostExecute > errorMsg : " + errorMsg);
            if (StringUtils.isNotBlank(errorMsg)) {
                Toast.makeText(PlayerActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                closePlayer();
            }
            super.onPostExecute(result);
        }
    }


    public void registerEarPhoneChangeReceiver() {
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        int result = mAudioManager.requestAudioFocus(mAudioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            mRemoteComponent = new ComponentName(this, EarPhoneChangeReceiver.class);
            EarPhoneChangeReceiver.setHandler(playerHandler, PlayerActivity.this);
            mAudioManager.registerMediaButtonEventReceiver(mRemoteComponent);
        }
    }


    Handler playerHandler = new Handler() {
        public void handleMessage(Message msg) {
            LogUtil.INSTANCE.info("birdganglifecycl", "playerHandler > msg : " + msg.what);

            switch (msg.what) {
                case AppConstants.MESSAGE_EARPHONECHANGE_HANDLER_REMOTE_CLICKED:
                    if (hasWindowFocus() == true) {
                        if (isPlaying() == true) {
                            pausePlayback();
                        } else {
                            resumePlayback();
                        }

                    } else {
                        if (mNcgFilePath.contains(".mp3")) {
                            if (isPlaying() == true) {
                                pausePlayback();
                            } else {
                                resumePlayback();
                            }
                        }
                    }
                    break;
            }
        }

        ;
    };


    /**
     * Handle resize of the surface and the overlay
     */
    private final Handler mOverlayHandler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            LogUtil.INSTANCE.info("birdganglifecycl", "mOverlayHandler > msg : " + msg.what);

            switch (msg.what) {
                case IntentParams.FADE_OUT:
                    setVisibleOverlay(View.GONE);
                    break;
                case IntentParams.FADE_OUT_INFO:
                    fadeOutInfo();
                    break;
            }
            return true;
        }
    });


    private void fadeOutInfo() {
        try {
            if (mTextOverlayInfo.getVisibility() == View.VISIBLE) {
                mTextOverlayInfo.startAnimation(AnimationUtils.loadAnimation(PlayerActivity.this, android.R.anim.fade_out));
            }

            mTextOverlayInfo.setVisibility(View.INVISIBLE);
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
    }

    private void startPlayback() {
        loadContentMedia();
    }


    private void loadContentMedia() {
        Intent intent = getIntent();
        String action = intent.getAction();
        Bundle extras = getIntent().getExtras();
        mNcgFilePath = extras.getString(IntentParams.MEDIA_ITEM_LOCATION);
    }


    /************************************
     *  init
     *************************************/
    public List<GuideViewEntry> getPlayerGuideEntries () {
        List<GuideViewEntry> guideEntries = new ArrayList<GuideViewEntry>();

        GuideViewEntry guideEntry = new GuideViewEntry();
        guideEntry.setGuideType(GuideViewEntry.GuidesType.PLAYER.getType());
        guideEntry.setPosition(0);
        guideEntry.setImageResource(R.drawable.img_onboarding_first);
        guideEntry.setTitle(getString(R.string.guide_text_title_ndplayer));
        guideEntry.setDescription(getString(R.string.guide_text_description_ndplayer));
        guideEntry.setBtnText("next");

        guideEntries.add(guideEntry);

        GuideViewEntry guideEntry2 = new GuideViewEntry();
        guideEntry2.setGuideType(GuideViewEntry.GuidesType.PLAYER.getType());
        guideEntry2.setPosition(1);
        guideEntry2.setImageResource(R.drawable.img_onboarding_second);
        guideEntry2.setTitle(getString(R.string.guide_text_description_ndplayer));
        guideEntry2.setDescription(getString(R.string.guide_text_title_ndplayer));
        guideEntry2.setBtnText("start");

        guideEntries.add(guideEntry2);

        return guideEntries;
    }


    private void checkShownGuide () {
        mShownGuide = SettingControler.getDefault().getShownPlayerGuide();
        LogUtil.INSTANCE.info("birdgangguides", "initGuide > mShownGuide : " + mShownGuide);
        if (mShownGuide && !ModuleConfig.ENABLE_FORCE_INTRO_GUIDE) {
            mGuideViewGroup.setVisibility(View.GONE);
        }
        else {
            pausePlayback();
            mGuideViewGroup.setVisibility(View.VISIBLE);
        }
    }


    private void initPlayList() {
        try {
            LogUtil.INSTANCE.info("birdganglifecycl" , "PlayerActivity > initPlayList > PLAY_TYPE : " + PLAY_TYPE);
            switch (PLAY_TYPE) {
                case AppConstants.TYPE_PLAY_DOWNLOAD:
                    try {
                        int downloadContentId = mContentEntry.getContentId();
                        String directory = mContentEntry.getDirectory();
//                        ContentControler.getDefault().findListContentByCategoryId(directory);
                        mContentEntries = (ArrayList<ContentEntry>) ContentControler.getDefault().getPlaylist();

                        for (int i = 0; mContentEntries.size() > i; i++) {
                            ContentEntry contentEntry = mContentEntries.get(i);
                            if (StringUtils.equals(mNcgFilePath, contentEntry.getContentFilePath())) {
                                mCurrentPlayIndex = i;
                            }
                        }

                        mPlayListSize = mContentEntries.size();
                        LogUtil.INSTANCE.info("birdgangplaylist", "initPlayList > mPlayListSize : " + mPlayListSize + " , mCurrentPlayIndex : " + mCurrentPlayIndex + " , downloadContentId : " + downloadContentId + " , mNcgFilePath : " + mNcgFilePath);

                        if (null != mContentEntries && mContentEntries.size() > mCurrentPlayIndex && mCurrentPlayIndex >= 0) {
                            ContentEntry contentEntry = mContentEntries.get(mCurrentPlayIndex);
                            mContentEntry = ContentControler.getDefault().findContentById(contentEntry.getContentId());
//                            mContentEntry = ContentControler.getDefault().findContentByName(contentEntry.getContentName());
                            LogUtil.INSTANCE.info("birdgangplaylist", "mContentEntry  : " + mContentEntry.toString());
                        }
                    } catch (Exception e) {
                        LogUtil.INSTANCE.error(TAG, e);
                    }
                    LogUtil.INSTANCE.info("birdgangplaylist", "initPlayList >  mContentEntry filepath : " + mContentEntry.getContentFilePath());
                    break;

                case AppConstants.TYPE_PLAY_FAVORITE:
                    try {
                        mFavoriteList = FavoriteControler.getDefault().convertContentFromListFavoritedContent();
                        // 존재 하지 않는 파일 삭제 로직
                        for (int i = mFavoriteList.size() - 1; i >= 0; i--) {
                            if (!FileUtil.existsFile(mFavoriteList.get(i).getContentFilePath())) {
                                mFavoriteList.remove(i);
                            }
                        }

                        for (int i = 0; mFavoriteList.size() > i; i++) {
                            if (mContentEntry.getContentId() == mFavoriteList.get(i).getContentId()) {
                                mCurrentPlayIndex = i;
                            }
                        }
                        mPlayListSize = mFavoriteList.size();
                        mContentEntry = mFavoriteList.get(mCurrentPlayIndex);
                    } catch (Exception e) {
                        LogUtil.INSTANCE.error(TAG, e);
                    }
                    break;

                case AppConstants.TYPE_PLAY_PLAYEDLIST:
                    try {
                        mPlayedListList = RecentlyPlayedListControler.getDefault().convertContentFromPlayedListContent();
                        // 존재 하지 않는 파일 삭제 로직
                        for (int i = mPlayedListList.size() - 1; i >= 0; i--) {
                            ContentEntry playedListEntry = mPlayedListList.get(i);
                            String path = playedListEntry.getContentFilePath();

                            if (!FileUtil.existsFile(path)) {
                                mPlayedListList.remove(i);
                            }
                        }

                        for (int i = 0; mPlayedListList.size() > i; i++) {
                            ContentEntry playedListEntry = mPlayedListList.get(i);
                            int contentId = playedListEntry.getContentId();
                            if (mContentEntry.getContentId() == contentId) {
                                mCurrentPlayIndex = i;
                            }
                        }
                        mPlayListSize = mPlayedListList.size();
                        mContentEntry = mPlayedListList.get(mCurrentPlayIndex);
                    } catch (Exception e) {
                        LogUtil.INSTANCE.error(TAG, e);
                    }
                    break;
            }
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }

    }


    private boolean initAdvanceData() throws Exception {
        ContentEntry contentEntry = null;
        // 데이터 초기화
        if (StringUtils.equals(AppConstants.TYPE_PLAY_DOWNLOAD, PLAY_TYPE)) {
            contentEntry = mContentEntries.get(mCurrentPlayIndex);
            for (ContentEntry entry : mContentEntries) {
                LogUtil.INSTANCE.info("birdgangplaylist", "entry : " + entry.toString());
            }
        } else if (StringUtils.equals(AppConstants.TYPE_PLAY_PLAYEDLIST, PLAY_TYPE)) {
            contentEntry = mPlayedListList.get(mCurrentPlayIndex);
            for (ContentEntry entry : mPlayedListList) {
                LogUtil.INSTANCE.info("birdgangplaylist", "entry : " + entry.toString());
            }
        } else if (StringUtils.equals(AppConstants.TYPE_PLAY_FAVORITE, PLAY_TYPE)) {
            contentEntry = mFavoriteList.get(mCurrentPlayIndex);
            for (ContentEntry entry : mFavoriteList) {
                LogUtil.INSTANCE.info("birdgangplaylist", "entry : " + entry.toString());
            }
        }

        mContentEntry = ContentControler.getDefault().findContentByName(contentEntry.getContentName());
        int contentId = mContentEntry.getContentId();
        String filePath = mContentEntry.getContentFilePath();

        LogUtil.INSTANCE.info("birdganglifecycl", "contentId : " + contentId + " , filePath : " + filePath + " , mCurrentPlayIndex : " + mCurrentPlayIndex);

        try {
            if (!ModuleConfig.ENABLE_NO_LICENSE) {
                // 라이센스 Info 체크
                boolean isValidLicense = NetSyncSdkHelper.getDefault().isValidLicense(filePath);
                LogUtil.INSTANCE.info("birdganglifecycl", "initAdvanceData > isValidLicense : " + isValidLicense);

                if (!isValidLicense) {
                    throw new Ncg2CoreException(getString(R.string.exception_license_to_play_next_file));
                }
            }

            mNcgFilePath = mContentEntry.getContentFilePath();
            mNcgFileName = mContentEntry.getContentName();
            mPlaySpeed = 0;
            return true;
        } catch (Exception e) {
            throw e;
        }
    }

    private boolean initRetourData() throws Exception {
        LogUtil.INSTANCE.info(TAG, "initRetourData");

        // 데이터 초기화
        if (StringUtils.equals(AppConstants.TYPE_PLAY_DOWNLOAD, PLAY_TYPE)) {
            mContentEntry = mContentEntries.get(mCurrentPlayIndex);
        } else if (StringUtils.equals(AppConstants.TYPE_PLAY_PLAYEDLIST, PLAY_TYPE)) {
            mContentEntry = mPlayedListList.get(mCurrentPlayIndex);
        } else if (StringUtils.equals(AppConstants.TYPE_PLAY_FAVORITE, PLAY_TYPE)) {
            mContentEntry = mFavoriteList.get(mCurrentPlayIndex);
        }

        try {
            if (!ModuleConfig.ENABLE_NO_LICENSE) {
                // 라이센스 Info 체크
                boolean isValidLicense = NetSyncSdkHelper.getDefault().isValidLicense(mContentEntry.getContentFilePath());
                if (!isValidLicense) {
                    throw new Ncg2CoreException(getString(R.string.exception_license_to_play_previous_file));
                }
            }

            mNcgFilePath = mContentEntry.getContentFilePath();
            mNcgFileName = mContentEntry.getContentName();
            mPlaySpeed = 0;
            return true;
        } catch (Exception e) {
            throw e;
        }
    }


    /***
     *
     */
    private void initNcgPlayer() {
        LogUtil.INSTANCE.info("birdganglifecycl" , "PlayerActivity > initNcgPlayer > mUserId : " + mUserId + " , mPlayerType : " + mPlayerType + " , SWXPLAY_TYPE : " + SWXPLAY_TYPE);
        try {
            if (null == mFrameLayoutPlayerContainer) {
                LogUtil.INSTANCE.info(TAG, "initNcgPlayer > null == mFrameLayoutPlayerContainer");
                mFrameLayoutPlayerContainer = (FrameLayout)findViewById(R.id.container_surfaceview);
            }

            Ncg2PlayerLibFactory playerFactory = new Ncg2PlayerLibFactory(PlayerActivity.this, mFrameLayoutPlayerContainer, "F8BVL01Q0VK15567635E454D");
            switch (mPlayerType) {
                case AppConstants.TYPE_PLAYER_NATIVE:
                    mNcg2Player = playerFactory.createMediaPlayer();
                    mbAvailVideoRate = false;
                    mIsSurfaceSenario = true;
                    break;
                case AppConstants.TYPE_PLAYER_VISUALONPLAYER:
                    mNcg2Player = playerFactory.createVisualOnPlayer();
                    mbAvailVideoRate = true;
                    mIsSurfaceSenario = true;
                    break;
            }

            if (ModuleConfig.ENABLE_FORCE_USE_BASE_PLAYER) {
                mNcg2Player = playerFactory.createMediaPlayer();
                mbAvailVideoRate = false;
                mIsSurfaceSenario = true;
            }

            registerNcg2LocalWebServer();

            if (null == mNcg2Player) {
                throw new Ncg2Exception();
            }

            mNcg2Player.setSecure(!ModuleConfig.ENABLE_NO_SECURITY_MODE); // ModuleConfig.AVAIL_CAPTURE_VIDEO
            mNcg2Player.setOnBufferingUpdateListener(mBufferingUpdateListener);
            mNcg2Player.setOnCompletionListener(mCompletionListener);
            mNcg2Player.setOnErrorListener(mErrorListener);
            mNcg2Player.setOnPreparedListener(mPreparedListener);
            mNcg2Player.setOnSeekCompleteListener(mSeekCompleteListener);
            mNcg2Player.setOnStatusChangeCallbackListener(mStatusChangeListener);

            // AudioPlayer와 VideoPlayer를 설정하는 부분
            if (mNcgFilePath.contains(".mp3")) {
                mNcg2Player.mIsAudioPlayer = true;
                mPlayerNavigator = mPlayerNavigatorFactory.createAudioNavigator();

            } else {
                mNcg2Player.mIsAudioPlayer = false;
                mPlayerNavigator = mPlayerNavigatorFactory.createVideoNavigator();
            }

            if (StringUtils.equals(mPlayerType, AppConstants.TYPE_PLAYER_VISUALONPLAYER)) {
                if (StringUtils.equals(SWXPLAY_TYPE, AppConstants.TYPE_SWXPLAY_NO_ALLOW)) {
                    mNcg2Player.setSwCodecMode(false);
                } else if (StringUtils.equals(SWXPLAY_TYPE, AppConstants.TYPE_SWXPLAY_ALLOW)) {
                    mNcg2Player.setSwCodecMode(true);
                }
            }
        } catch (Exception e) {
            FileLogUtil.INSTANCE.write("initNcgPlayer >> " + e.getMessage());
            LogUtil.INSTANCE.error(TAG, e);
            Toast.makeText(PlayerActivity.this, "initNcgPlayer" + "\n" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        try {
            mNcg2Player.init(mSurfaceHolderCallback);
        } catch (Ncg2Exception e) {
            Toast.makeText(PlayerActivity.this, "initNcgPlayer" + "\n" + e.getMessage(), Toast.LENGTH_SHORT).show();
            finish();
        } catch (Exception e) {
            Toast.makeText(PlayerActivity.this, "initNcgPlayer" + "\n" + e.getMessage(), Toast.LENGTH_SHORT).show();
            finish();
        }

    }


    public void registerNcg2LocalWebServer () {
        try {
            Ncg2Agent ncgAgent = Ncg2SdkFactory.getNcgAgentInstance();
            ncgAgent.getLocalWebServer().setWebServerListener(new LocalWebServerListener());
            ncgAgent.getLocalWebServer().setEnableUserAgentChecking(false);
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
    }


    /***
     *
     */
    private class LocalWebServerListener implements Ncg2LocalWebServer.WebServerListener {
        @Override
        public void onNotification(final int notifyCode, final String notifyMsg) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    LogUtil.INSTANCE.debug("birdganglifecycl", "WebServerListener-onNotification-run notifyCode : " + notifyCode + ", notifyMsg : " + notifyMsg);
                    if (notifyCode == Ncg2LocalWebServer.WebServerListener.LWS_ERROR_TRIAL_TIME_OUT) {
                        if (mIsSkipErrorHandling) {
                            return;
                        }
                        mIsSkipErrorHandling = true;

                        if (isFinishing()) {
                            return;
                        }
                        else {
                            try {
                                if (mNcg2Player.isPlaying()) {
                                    mNcg2Player.stop();
                                }
                            } catch (Ncg2Exception e) {
                                LogUtil.INSTANCE.error(TAG, e);
                            } catch (Exception e) {
                                LogUtil.INSTANCE.error(TAG, e);
                            }
                        }
                    } else if (notifyCode == Ncg2LocalWebServer.WebServerListener.LWS_NOTIFY_SCREEN_RECORDER_DETECTED) {
                        Toast.makeText(PlayerActivity.this, getString(R.string.webserver_notification_screen_recorder_detected), Toast.LENGTH_LONG).show();
                        finish();

                    } else if (notifyCode == Ncg2LocalWebServer.WebServerListener.LWS_NOTIFY_HDMI_DETECTED){
                        Toast.makeText(PlayerActivity.this, getString(R.string.webserver_notification_hdmi_detected), Toast.LENGTH_LONG).show();
                        finish();
                    }
                }
            });
        }

        @Override
        public void onError(final int errorCode, final String errorMessage) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    LogUtil.INSTANCE.info("birdganglifecycl", "WebServerListener-onError-run" + "\n" + " - " + "errorCode :" + errorCode + "\n" + " - " + "errorMessage :" + errorMessage);
                    try {
                        // Crash Report 전송
                        Exception ex = new Exception(String.format("WebServerListener.onError() : class name:%s "
                                        + "errorCode:%s "
                                        + "errorMessage:%s",
                                getClass().toString(),
                                errorCode,
                                errorMessage));

                        if (mIsSkipErrorHandling) {
                            return;
                        }

                        mIsSkipErrorHandling = true;

                        if (isFinishing()) {
                            return;
                        } else {
                            try {
                                if (mNcg2Player.isPlaying()) {
                                    mNcg2Player.stop();
                                }
                            } catch (Exception e) {
                                LogUtil.INSTANCE.error(TAG, e.getMessage());
                            }
                        }

                        String msg = String.format("WebServerListener-onError-run" + "\n" + "errorCode :" + errorCode + "\n" + "errorMessage:" + errorMessage);

                        new CustomAlertDialog(PlayerActivity.this)
                                .setCancelAbleDialog(true)
                                .setConfirmText(getString(R.string.dialog_ok))
                                .setTitleText("WebServer Error")
                                .setConfirmBtnColoer(BaseConfiguration.getDefault().getAppDialogBtnColor())
                                .setContentText(msg)
                                .setConfirmClickListener(new CustomAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(CustomAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismiss();
                                        finish();
                                    }
                                })
                                .show();
                    } catch (Exception e) {
                        FileLogUtil.INSTANCE.write("onError >> " + e.getMessage());
                    }

                }
            });
        }

        @Override
        public PlayerState onCheckPlayerStatus(String uri) {
            LogUtil.INSTANCE.info("birdganglifecycl", " WebServerListener-onCheckPlayerStatus " + " - " + " uri : " + uri);

            if (!isFinishing()) {
                try {
                    mNcg2Player.isPlaying();
                    mNcg2Player.getCurrentPosition();
                } catch (IllegalStateException e) {
                    LogUtil.INSTANCE.info(TAG, " WebServerListener-onCheckPlayerStatus" + " - " + "error : " + e.getMessage());
                    return PlayerState.Fail;
                } catch (Exception e) {
                    LogUtil.INSTANCE.info(TAG, " WebServerListener-onCheckPlayerStatus" + " - " + "error : " + e.getMessage());
                    return PlayerState.Fail;
                }
            }
            return PlayerState.ReadyToPlay;
        }
    }


    /**
     *
     */
    private void initRotation() {
        LogUtil.INSTANCE.info("birdgangscreenrate" , "PlayerActivity > initRotation");

        WindowManager windowManager = getWindowManager();
        int orientation = windowManager.getDefaultDisplay().getRotation();
        try {
            if (orientation == 0 || orientation == 2) {
                fillContentVerticalLayout();
            } else if (orientation == 1 || orientation == 3) {
                fillContentHorizontalalLayout();
            }
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
    }


    protected GestureDetector.OnDoubleTapListener mDoubleTapListener = new GestureDetector.OnDoubleTapListener() {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            if (mEnableTouchScreenLock) {
                showInfo(R.string.player_locked, 1000);
                return true;
            }

            if (e.getAction() == MotionEvent.ACTION_DOWN) {
                if (mHeaderAndSettingViewGroup.getVisibility() == View.GONE) {
                    setVisibleOverlay(View.VISIBLE);
                } else {
                    setVisibleOverlay(View.GONE);
                }
            }
            return true;
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            if (mEnableGesture && !mEnableTouchScreenLock) {
                //onClickPlaynPause();
            }
            return true;
        }
    };


    private void loadCacheData () {
        mScreenRate = PreferencesCacheHelper.getPreferenceValueForInteger(PreferencesCacheHelper.SCREEN_RATE, 0);
        mSkipTime = PreferencesCacheHelper.getPreferenceValueForInteger(PreferencesCacheHelper.SKIP_TIME, 10);

        mDecoderType = SettingControler.getDefault().getDecoderType();
        mShownGuide = SettingControler.getDefault().getShownPlayerGuide();
        mRestrictInternet = SettingControler.getDefault().getEnableRestrictedInternet();
    }


    /***
     *
     */
    private void initUI() {
        try {
            if (null == mContentEntry) {
                return;
            }

            mSeekBar.setOnSeekBarChangeListener(mOnSeekBarChangeListener);

            if (mTbIsFavorite != null) {
                //0 : 즐겨찾기 아님 | 1: 즐겨찾기 맞음
                String contentName = mContentEntry.getContentName();
                boolean hasFavorite = FavoriteControler.getDefault().hasFavoroteByContentName(contentName);
                if (hasFavorite || mContentEntry.getIsFavoriteContent() > 0) {
                    mTbIsFavorite.setChecked(true);
                } else {
                    mTbIsFavorite.setChecked(false);
                }
            }

            mIvCompanyLogo.setBackgroundResource(provideCompanyLogo());
            mIvRepeatA.setVisibility(View.INVISIBLE);
            mIvRepeatB.setVisibility(View.INVISIBLE);

            // Audio와 Video의 BackgroundImage를 설정하는 부분
            if (mNcg2Player.mIsAudioPlayer) {
                mImageAudioPlayerBackground.setVisibility(View.VISIBLE);
            } else {
                mImageAudioPlayerBackground.setVisibility(View.GONE);
            }

            // value 설정
            mTxtPlaySpeed.setText(String.format("%.1fx", (100 + mPlaySpeed) / 100.0));
            mMarqueeTextView.setText(mNcgFileName);

            setUIEnable(false);
            setVisibleOverlay(View.VISIBLE);
            //setScreenLockRotation();

            if (mEnableTouchScreenLock) {
                showInfo(R.string.player_locked, 1000);
                mImgTouchLockScreen.setChecked(true);
                mImgTouchLockScreenHor.setChecked(true);
            } else {
                mImgTouchLockScreen.setChecked(false);
                mImgTouchLockScreenHor.setChecked(false);
            }

            // Audio와 Video의 BackgroundImage를 설정하는 부분
            if (mNcg2Player.mIsAudioPlayer) {
                mImageAudioPlayerBackground.setVisibility(View.VISIBLE);
            } else {
                mImageAudioPlayerBackground.setVisibility(View.GONE);
            }

            if (PreferencesCacheHelper.DECODER_SOFTWARE == mDecoderType) {
                mTextDecoderView.setText("S / W");
            } else {
                mTextDecoderView.setText("H / W");
            }

            mScrollerViewPager.setAdapter(new PagerGuideAdapter(getSupportFragmentManager(), GuideViewEntry.GuidesType.PLAYER.getType(), getPlayerGuideEntries(), new ClickListener() {
                @Override
                public void onItemClick(View view) {
                    try {
                        int position = (int) view.getTag();
                        LogUtil.INSTANCE.info(TAG, "PagerGuideAdapter > onItemClick > position : " + position);
                        if (position >= 1) {
                            PreferencesCacheHelper.setPreferenceValueForBol(PreferencesCacheHelper.GUIDE_PLAYER_INIT, true);
                            mGuideViewGroup.setVisibility(View.GONE);
                            resumePlayback();
                        } else {
                            mScrollerViewPager.setCurrentItem(1);
                        }
                    } catch (Exception e) {
                        LogUtil.INSTANCE.error(TAG, e);
                    }
                }
            }));
            indicator.setViewPager(mScrollerViewPager);
            mScrollerViewPager.setOffscreenPageLimit(2);
            int color = 0;
            if (color == 0) {
                color = ThemeUtil.getPrimaryColor(this);
            }
            mScrollerViewPager.setBackgroundColor(color);
            mScrollerViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                @Override
                public void onPageSelected(int position) {
                }
            });
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
    }


    private void setUIEnable(boolean enabled) {
        mPlayAndPause.setEnabled(enabled);
        mIvCompanyLogo.setEnabled(enabled);
        mIvRepeatA.setEnabled(enabled);
        mIvRepeatB.setEnabled(enabled);
        mTbIsFavorite.setEnabled(enabled);
        mSeekBar.setEnabled(enabled);
        mTxtPlaySpeed.setEnabled(enabled);
        mImgTouchLockScreen.setEnabled(enabled);
        mImgTouchLockScreenHor.setEnabled(enabled);
        mImgRepeatMode.setEnabled(enabled);
        mImgRepeatArea.setEnabled(enabled);
        mImgRepeatModeHor.setEnabled(enabled);
        mImgRepeatAreaHor.setEnabled(enabled);
        mTextOverlayInfo.setEnabled(enabled);
        mTextPositionView.setEnabled(enabled);
        mTextDurationView.setEnabled(enabled);
        mImageAudioPlayerBackground.setEnabled(enabled);
        mImgSetting.setEnabled(enabled);
    }

    /**
     *  현재 컨텐츠 객체를 변경 및 업데이트
     */
    private void updateInfo() {
        LogUtil.INSTANCE.info("birdganglifycycl", "updateInfo");

        try {
            if (null == mContentEntry) {
                return;
            }

            mContentId = mContentEntry.getContentId();

            Date date = new Date();
            ContentEntry contentEntry = ContentControler.getDefault().findContentById(mContentId);
            contentEntry.setPlayDateToDate(date);
            mContentEntry = contentEntry;

            SimpleDateFormat detailedDate = DateTimeUtil.getSimpleDateFormat();
            long isAdded = RecentlyPlayedListControler.getDefault().addRecentlyPlayed(new RecentlyEntry(contentEntry));
            boolean isUpdated = ContentControler.getDefault().updateContentPlayDate(mContentId, detailedDate.format(date));
            LogUtil.INSTANCE.info(TAG, "updateInfo > isUpdated : " + isUpdated + ", isAdded : " + isAdded + " , mContentId : " + mContentId);
            if (isUpdated) {
                EventBus.getDefault().notifyEventListOrderContents(AppConstants.LIST_ORDER_DEFAULT);
            }
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
    }


    /*********************
     * click listener
     *********************/

    @OnClick(R2.id.img_setting)
    public void onClickSetting () {
        try {
            pausePlayback();

            mSkipTime = PreferencesCacheHelper.getPreferenceValueForInteger(PreferencesCacheHelper.SKIP_TIME, 10);
            mScreenRate = PreferencesCacheHelper.getPreferenceValueForInteger(PreferencesCacheHelper.SCREEN_RATE, 0);

            new PlayerSettingDialog(this)
                    .setConfirmText(getString(R.string.dialog_ok))
                    .setSkipTime(mSkipTime)
                    .setScreenRate(mScreenRate)
                    .setConfirmClickListener(new PlayerSettingDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(PlayerSettingDialog sDialog) {
                            try {
                                mSkipTime = sDialog.getSkipTime();
                                mScreenRate = sDialog.getScreenRate();

                                PreferencesCacheHelper.setPreferenceValueForInteger(PreferencesCacheHelper.SKIP_TIME, mSkipTime);
                                PreferencesCacheHelper.setPreferenceValueForInteger(PreferencesCacheHelper.SCREEN_RATE, mScreenRate);

                                //setScreenLockRotation();
                                onChangeScreenMode();
                                sDialog.cancel();
                            } catch (Exception e) {
                                e.getMessage();
                            } finally {
                                resumePlayback();
                            }
                        }
                    })
                    .show();
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        } finally {
            resetUiHideCounting();
        }
    }


    @OnClick(R2.id.btn_close)
    public void onClickClose() {
        closePlayer();
    }

    @OnClick(R2.id.btn_play_speed_up)
    public void onClickSpeedUP() {
        try {
            if (mPlaySpeed < 100) {
                mPlaySpeed += 10;
                setPlaybackSpeed();
            }
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        } finally {
            resetUiHideCounting();
        }
    }

    @OnClick(R2.id.btn_play_speed_down)
    public void onClickSpeedDown() {
        try {
            if (mPlaySpeed > -50) {
                mPlaySpeed -= 10;
                setPlaybackSpeed();
            }
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        } finally {
            resetUiHideCounting();
        }
    }

    @OnClick({R2.id.img_touch_lockscreen, R2.id.img_touch_lockscreen_hor})
    public void onClickTouchLock () {
        LogUtil.INSTANCE.info("birdgangtouchscreen" , "onClickTouchLock > mEnableTouchScreenLock : " + mEnableTouchScreenLock);

        try {
            if (mEnableTouchScreenLock) {
                unlockScreen();
            } else {
                lockScreen();
            }
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
    }


    /**
     * Lock screen rotation
     */
    private void lockScreen() throws Exception {
        mEnableTouchScreenLock = true;
        setScreenLockRotation();

        showInfo(R.string.player_locked, 1000);

        mImgTouchLockScreen.setChecked(true);
        mImgTouchLockScreenHor.setChecked(true);

        setVisibleOverlay(View.GONE);
    }

    /**
     * Remove screen lock
     */
    private void unlockScreen() throws Exception {
        mEnableTouchScreenLock = false;
        setScreenLockRotation();

        showInfo(R.string.player_unlocked, 1000);

        mImgTouchLockScreen.setChecked(false);
        mImgTouchLockScreenHor.setChecked(false);

        setVisibleOverlay(View.VISIBLE);
    }


    /***
     *
     */
    public void setScreenLockRotation() {
        LogUtil.INSTANCE.info("birdganglock" , "PlayerActivity > onChangeLockScreen > mEnableTouchScreenLock : " + mEnableTouchScreenLock);
        try {
            if (mEnableTouchScreenLock) {
                int screenOrientation = getScreenOrientation();
                LogUtil.INSTANCE.info("birdganglock" , "PlayerActivity > onChangeLockScreen > screenOrientation : " + screenOrientation);
                setRequestedOrientation(screenOrientation);
            } else {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
            }
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        } finally {
            resetUiHideCounting();
        }
    }


    public void onChangeScreenMode() {
        LogUtil.INSTANCE.info("birdgangscreenrate" , "onChangeScreenMode > mCurrentDisplayMode : " + mCurrentDisplayMode);

        try {
            if (mScreenRate == 1) {
                mCurrentDisplayMode = Ncg2Player.DisplayMode.FitToScreen;
            } else {
                mCurrentDisplayMode = Ncg2Player.DisplayMode.FullScreenWithKeepRatio;
            }

            switch (mCurrentDisplayMode) {
                case FitToScreen:
                    mCurrentDisplayMode = Ncg2Player.DisplayMode.FitToScreen;
                    break;
                case FullScreenWithKeepRatio:
                    mCurrentDisplayMode = Ncg2Player.DisplayMode.FullScreenWithKeepRatio;
                    break;
                case OriginalContentSize:
                    mCurrentDisplayMode = Ncg2Player.DisplayMode.OriginalContentSize;
                    break;
                default:
                    mCurrentDisplayMode = Ncg2Player.DisplayMode.OriginalContentSize;
                    break;
            }

            mNcg2Player.setDisplayMode(mCurrentDisplayMode);
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        } finally {
            resetUiHideCounting();
        }
    }


    @OnClick(R2.id.btn_bookmark_check)
    public void onClickCheckBookmark() {
        try {
            String contentName = mContentEntry.getContentName();
            LogUtil.INSTANCE.info("birdgangbookmark", "contentName : " + contentName);

            ArrayList<BookmarkEntry> bookmarkEntries = BookmarkControler.getDefault().loadBookmarkListInContentByContentName(contentName);
            if (null == bookmarkEntries) {
                LogUtil.INSTANCE.info("birdgangbookmark", "null == bookmarkEntries");
            } else {
                LogUtil.INSTANCE.info("birdgangbookmark", "bookmarkEntries size : " + bookmarkEntries.size());
            }

            int size = bookmarkEntries.size();
            if (size >= 20) {
                Toast.makeText(PlayerActivity.this, getString(R.string.message_for_toast_bookmark_limit_count), Toast.LENGTH_SHORT).show();
                return;
            }

            // 현재 위치 가져오기
            int currentPosition = mNcg2Player.getCurrentPosition();
            if (currentPosition > mNcg2Player.getDuration()) {
                currentPosition = mNcg2Player.getDuration();
            }

            for (BookmarkEntry bookmarkEntry : bookmarkEntries) {
                LogUtil.INSTANCE.info("birdgangbookmark", "bookmarkEntry.toString() : " + bookmarkEntry.toString());

                int bookmarkTime = Integer.valueOf(bookmarkEntry.getBookmarkLocation());
                if (currentPosition > bookmarkTime - 10000 && currentPosition < bookmarkTime + 10000) {
                    // 간격이 짧을 경우
                    Toast.makeText(PlayerActivity.this, getString(R.string.message_for_toast_bookmark_limit_time), Toast.LENGTH_SHORT).show();
                    return;
                }
            }


            LogUtil.INSTANCE.info("birdgangbookmark", "currentPosition : " + currentPosition);

            // 정상 간격 일 경우
            if (currentPosition > 0) {
                pausePlayback();
                new AddBookMarkAlertDialog(this)
                        .setCancelText(getString(R.string.dialog_cancel))
                        .setConfirmText(getString(R.string.dialog_ok))
                        .setContentText("시간 " + DateTimeUtil.changeTime(Integer.valueOf(mNcg2Player.getCurrentPosition())))
                        .setCancelClickListener(new AddBookMarkAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(AddBookMarkAlertDialog sDialog) {
                                try {
                                    sDialog.cancel();
                                    resumePlayback();
                                } catch (Exception e) {
                                    e.getMessage();
                                }
                            }
                        })
                        .setConfirmClickListener(new AddBookMarkAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(AddBookMarkAlertDialog sDialog) {
                                try {
                                    String bookmarkLocation = "";
                                    String bookmarkMemo = StringUtils.replace(sDialog.getEditTextContents(), "," , " ");
                                    if (StringUtils.isBlank(bookmarkMemo)) {
                                        bookmarkMemo = getString(R.string.player_function_no_memo);
                                    }

                                    // 시간
                                    bookmarkLocation = String.format("%08d", mNcg2Player.getCurrentPosition());

                                    LogUtil.INSTANCE.info("birdgangbookmark", "bookmarkMemo : " + bookmarkMemo + " , bookmarkLocation : " + bookmarkLocation);

                                    try {
                                        SimpleDateFormat detailedDate = DateTimeUtil.getSimpleDateFormat();

                                        BookmarkEntry bookmarkEntry = new BookmarkEntry();
                                        bookmarkEntry.setBookmarkDate(detailedDate.format(new Date()));
                                        bookmarkEntry.setBookmarkContentPath(mContentEntry.getContentFilePath());
                                        bookmarkEntry.setBookmarkContentName(mContentEntry.getContentName());
                                        bookmarkEntry.setBookmarkMemo(bookmarkMemo);
                                        bookmarkEntry.setBookmarkLocation(bookmarkLocation);
                                        bookmarkEntry.setContentId(mContentEntry.getContentId());

                                        int result = (int) BookmarkControler.getDefault().addBookmark(bookmarkEntry);
                                        if (result > -1) {
                                            int contentId = mContentEntry.getContentId();
                                            ContentEntry contentEntry = ContentControler.getDefault().findContentById(contentId);
                                            if (null != contentEntry) {
                                                String contentName = contentEntry.getContentName();
                                                Toast.makeText(PlayerActivity.this, String.format(getString(R.string.message_for_toast_done_add_bookmark), contentName), Toast.LENGTH_SHORT).show();
                                            }
                                        }


                                    } catch (Exception e) {
                                        LogUtil.INSTANCE.error(TAG, e);
                                    }
                                    sDialog.cancel();
                                    resumePlayback();
                                } catch (Exception e) {
                                    e.getMessage();
                                }
                            }
                        })
                        .show();
            }
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        } finally {
            resetUiHideCounting();
        }
    }

    @OnClick(R2.id.btn_bookmark_view)
    public void onClickViewBookmark() {
        // 구간 반복중 사용 중지
        if (mRepeatBPosition != -1) {
            Toast.makeText(PlayerActivity.this, getString(R.string.message_for_toast_not_add_bookmark), Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            String contentName = mContentEntry.getContentName();
//            ArrayList<BookmarkEntry> bookmarkEntries = BookmarkControler.getDefault().loadBookmarkListInContent(mContentEntry.getContentId());
            ArrayList<BookmarkEntry> bookmarkEntries = BookmarkControler.getDefault().loadBookmarkListInContentByPath(mContentEntry.getContentFilePath());

            LogUtil.INSTANCE.info("birdgangbookmark" , "onClickViewBookmark > contentName : " + contentName);

            // 지정된 북마크 없음
            if (bookmarkEntries.size() == 0) {
                Toast.makeText(PlayerActivity.this, getString(R.string.message_for_toast_not_exist_bookmark), Toast.LENGTH_SHORT).show();
                return;
            }

            LogUtil.INSTANCE.info("birdgangbookmark" , "onClickViewBookmark > bookmarkEntries.size() : " + bookmarkEntries.size());

            pausePlayback();
            ArrayList<BookMarkViewEntry> bookmarkViewEntries = new ArrayList<>();
            for (BookmarkEntry entry : bookmarkEntries) {
                bookmarkViewEntries.add(entry.convertViewEntry());
            }

            new ListBookMarkAlertDialog(this)
                    .setDataSource(bookmarkViewEntries)
                    .setConfirmText(getString(R.string.dialog_ok))
                    .setConfirmClickListener(new ListBookMarkAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClickListContent(ListBookMarkAlertDialog sDialog) {
                            try {
                                int bookmarkLocation = sDialog.getmBookmarkPosition();
                                LogUtil.INSTANCE.info(TAG, "onBookmarkItemClick" + " - " + "bookmarkLocation : " + bookmarkLocation);

                                int timePos = bookmarkLocation;
                                if (timePos > mNcg2Player.getDuration()) {
                                    timePos = mNcg2Player.getDuration();
                                }
                                seekTo(timePos);
                                resumePlayback();
                            } catch (Exception e) {
                                LogUtil.INSTANCE.error(TAG, e);
                            } finally {
                                sDialog.cancel();
                            }
                        }

                        @Override
                        public void onClick(ListBookMarkAlertDialog sDialog) {
                            try {
                                List<BookMarkViewEntry>  selectedForDeleteEntries = sDialog.getSelectedForDeleteEntries();
                                if (null != selectedForDeleteEntries) {
                                    for (BookMarkViewEntry entry : selectedForDeleteEntries) {
                                        long result = BookmarkControler.getDefault().deleteBookmarkById(entry.getBookmarkId());
                                        LogUtil.INSTANCE.info(TAG, "result : " + result + " , entry : " + entry.toString());
                                        if (result > 0) {
                                            Toast.makeText(PlayerActivity.this, getResources().getString(R.string.message_for_toast_success_for_deleted_item_on_bookmark), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                                sDialog.cancel();
                                resumePlayback();
                            } catch (Exception e) {
                                e.getMessage();
                            }
                        }
                    })
                    .setDismissListener(new ListBookMarkAlertDialog.OnDialogDismissListener() {
                        @Override
                        public void onDissmiss() {
                            resumePlayback();
                        }
                    })
                    .show();
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        } finally {
            resetUiHideCounting();
        }

    }

    @OnClick(R2.id.btn_favorite)
    public void onClickFavorite() {
        try {
            // 0 : 즐겨찾기 아님 | 1: 즐겨찾기 맞음
            if (mTbIsFavorite.isChecked() == false) {
                mContentEntry.setIsFavoriteContent(0);
                long result = FavoriteControler.getDefault().deleteFavoriteByContentName(new FavoriteEntry(mContentEntry));
                LogUtil.INSTANCE.info("birdgangmedia" , "deleteFavoriteByContentName > result : " + result + " , mContentEntry : " + mContentEntry.toString());
                Toast.makeText(PlayerActivity.this, getString(R.string.message_for_toast_favorite_content_no_use), Toast.LENGTH_SHORT).show();
            } else {
                mContentEntry.setIsFavoriteContent(1);
                long result = FavoriteControler.getDefault().addFavorite(new FavoriteEntry(mContentEntry));
                LogUtil.INSTANCE.info("birdgangmedia" , "addFavorite > result : " + result + " , mContentEntry : " + mContentEntry.toString());
                Toast.makeText(PlayerActivity.this, getString(R.string.message_for_toast_favorite_content_use), Toast.LENGTH_SHORT).show();
            }

            ContentControler.getDefault().updateContent(mContentEntry);

            updateFavoriteExplorerContentList(mContentEntry);
            resetUiHideCounting();
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        } finally {
            resetUiHideCounting();
        }

    }

    @OnClick(R2.id.btn_advance)
    public void onClickAdvance() {
        try {
            int timePos = mNcg2Player.getCurrentPosition() + (mSkipTime * 1000);
            LogUtil.INSTANCE.info(TAG, "btn_advance > mSkipTime : " + (mSkipTime * 1000) + " , mNcg2Player.getCurrentPosition() : " + mNcg2Player.getCurrentPosition());
            // B 구간 초과시
            if (checkRepeatB(timePos) == true) {
                return;
            }
            if (timePos > mNcg2Player.getDuration()) {
                timePos = mNcg2Player.getDuration();
            }
            seekTo(timePos);
            resetUiHideCounting();
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        } finally {
            resetUiHideCounting();
        }
    }


    @OnClick(R2.id.btn_retour)
    public void onClickRetour() {
        try {
            int timePos = mNcg2Player.getCurrentPosition() - (mSkipTime * 1000);
            LogUtil.INSTANCE.info(TAG, "retour > mSkipTime : " + (mSkipTime * 1000) + " , mNcg2Player.getCurrentPosition() : " + mNcg2Player.getCurrentPosition());
            // A 구간 초과시
            if (checkRepeatA(timePos) == true) {
                timePos = mRepeatAPosition;
            }

            if (timePos < 0) {
                timePos = 0;
            }
            seekTo(timePos);
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        } finally {
            resetUiHideCounting();
        }
    }


    @OnClick(R2.id.btn_playnpause)
    public void onClickPlaynPause() {
        try {
            if (mNcg2Player != null) {
                if (mNcg2Player.isPlaying()) {
                    pausePlayback();
                } else {
                    resumePlayback();
                }
            }
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        } finally {
            resetUiHideCounting();
        }
    }


    @OnClick(R2.id.btn_file_advance)
    public void onClickFileAdvance() {
        // PD 재생시 사용중지
        if (StringUtils.equals(AppConstants.TYPE_PLAY_BOOKMARK, PLAY_TYPE)) {
            Toast.makeText(PlayerActivity.this, getString(R.string.message_for_toast_play_not_bookmark), Toast.LENGTH_SHORT).show();
            return;
        } else if (StringUtils.equals(AppConstants.TYPE_PLAY_SEARCH, PLAY_TYPE)) {
            Toast.makeText(PlayerActivity.this, getString(R.string.message_for_toast_play_not_search), Toast.LENGTH_SHORT).show();
            return;
        }

        enableMediaNevigation(false);

        if (mCurrentPlayIndex + 1 >= mPlayListSize) {
            mCurrentPlayIndex = 0;
        } else {
            ++mCurrentPlayIndex;
        }

        mIsPrepared = false;
        mIsPauseded = false;
        mIsSuspended = false;
        mDataSourceSetupTask = null;

        PLAY_STATE = AppConstants.STATE_PLAYT_ONSTOP;

        try {
            updateLastUpdatePlayTime();
            // 구간반복 해제
            releaseRepeatAB();
            // 멈춤
            releasePlayback();

            // 플레이어 초기화
            boolean initAdvanceData = initAdvanceData();
            LogUtil.INSTANCE.info(TAG, "onClickFileAdvance > initAdvanceData : " + initAdvanceData + " , mCurrentPlayIndex : " + mCurrentPlayIndex + ", mIsSurfaceSenario : " + mIsSurfaceSenario);

            if (initAdvanceData) {
                initNcgPlayer();
                initUI();
                updateInfo();

                // onResume 코드
                if (mIsSurfaceSenario == false) {
                    executeDataSourceSetupTask();
                }
            }
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
            closePlayer();
        } finally {
            resetUiHideCounting();
        }
    }

    // 이전 파일 재생
    @OnClick(R2.id.btn_file_retour)
    public void onClickFileRetour() {
        // PD 재생시 사용중지
        if (StringUtils.equals(AppConstants.TYPE_PLAY_BOOKMARK, PLAY_TYPE)) {
            Toast.makeText(PlayerActivity.this, getString(R.string.message_for_toast_play_not_bookmark), Toast.LENGTH_SHORT).show();
            return;
        } else if (StringUtils.equals(AppConstants.TYPE_PLAY_SEARCH, PLAY_TYPE)) {
            Toast.makeText(PlayerActivity.this, getString(R.string.message_for_toast_play_not_search), Toast.LENGTH_SHORT).show();
            return;
        }

        enableMediaNevigation(false);

        if (mCurrentPlayIndex - 1 < 0) {
            mCurrentPlayIndex = mPlayListSize - 1;
        } else {
            --mCurrentPlayIndex;
        }

        mIsPrepared = false;
        mIsPauseded = false;
        mIsSuspended = false;
        mDataSourceSetupTask = null;
        PLAY_STATE = AppConstants.STATE_PLAYT_ONSTOP;

        try {
            updateLastUpdatePlayTime();
            // 구간반복 해제
            releaseRepeatAB();
            // 멈춤
            releasePlayback();

            boolean initRetourData = initRetourData();
            LogUtil.INSTANCE.info(TAG, "onClickFileRetour > initRetourData : " + initRetourData + " , mCurrentPlayIndex : " + mCurrentPlayIndex + " , mIsSurfaceSenario : " + mIsSurfaceSenario);

            // 플레이어 초기화
            if (initRetourData) {
                initNcgPlayer();
                initUI();
                updateInfo();

                // onResume 코드
                if (mIsSurfaceSenario == false) {
                    executeDataSourceSetupTask();
                }
            }
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
            closePlayer();
        } finally {
            resetUiHideCounting();
        }
    }


    @OnClick({R2.id.img_repeat_mode})
    public void onClickRepeatMode() {
        try {
            boolean isAudio = StringUtils.contains(mNcgFilePath, ".mp3");
            boolean needToRepeatAll = needToRepeatAllForPlayType();
            mRepeatMode = mImgRepeatMode.changeRepeatMode(needToRepeatAll, isAudio);
            LogUtil.INSTANCE.info("birdgangtouchscreen" , "onClickRepeatMode > mRepeatMode : " + mRepeatMode);
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        } finally {
            resetUiHideCounting();
        }
    }

    @OnClick({R2.id.img_repeat_mode_hor})
    public void onClickRepeatModHor() {
        try {
            boolean isAudio = StringUtils.contains(mNcgFilePath, ".mp3");
            boolean needToRepeatAll = needToRepeatAllForPlayType();
            mRepeatMode = mImgRepeatModeHor.changeRepeatMode(needToRepeatAll, isAudio);
            LogUtil.INSTANCE.info("birdgangtouchscreen" , "onClickRepeatMode > mRepeatMode : " + mRepeatMode);
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        } finally {
            resetUiHideCounting();
        }
    }


    @OnClick({R2.id.img_repeat_area})
    public void onClickRepeatArea() {
        fillContentRepeatArea(mImgRepeatArea);
    }


    @OnClick({R2.id.img_repeat_area_hor})
    public void onClickRepeatAreaHor() {
        fillContentRepeatArea(mImgRepeatAreaHor);
    }


    private void fillContentRepeatArea (MultiCheckableImageView imgRepeat) {
        try {
            mRepeatAreaState = imgRepeat.isCheckedState();

            LogUtil.INSTANCE.info("birdgangtouchscreen" , "onClickRepeatMode > mRepeatAreaState : " + mRepeatAreaState);

            /*
            구간 반복 모드 재생 시작 시점 구간 지정
             */
            if (MultiCheckableImageView.REPEAT_OFF == mRepeatAreaState) {
                if (mRepeatAPosition == -1) {
                    imgRepeat.changeRepeatArea();

                    mRepeatAPosition = mSeekBar.getProgress();
                    mParamRepeatA = (RelativeLayout.LayoutParams) mIvRepeatA.getLayoutParams();
                    int margin = (int) AndroidUtil.convertDpToPixel(15, PlayerActivity.this);
                    mParamRepeatA.leftMargin = mSeekBar.getLeft() + margin - mIvRepeatA.getWidth() / 2 + (int) ((mSeekBar.getWidth() - margin * 2) * ((float) mRepeatAPosition / mNcg2Player.getDuration()));
                    mIvRepeatA.setLayoutParams(mParamRepeatA);
                    mIvRepeatA.setVisibility(View.VISIBLE);
                }
            }
            /**
             * 구간 반복 모드 재생 종료 시점 구간 지정
             */
            else if (MultiCheckableImageView.REPEAT_A == mRepeatAreaState) {
                if (mRepeatBPosition == -1) {
                    if (mSeekBar.getProgress() <= mRepeatAPosition) {
                        Toast.makeText(PlayerActivity.this, getString(R.string.player_function_bposition_ahead_of_aposition), Toast.LENGTH_SHORT).show();
                        return;
                    } else if (mSeekBar.getProgress() < (mRepeatAPosition + 10000)) {
                        Toast.makeText(PlayerActivity.this, getString(R.string.player_function_section_shorter_than_tensecond), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    imgRepeat.changeRepeatArea();

                    mRepeatBPosition = mSeekBar.getProgress();
                    mParamRepeatB = (RelativeLayout.LayoutParams) mIvRepeatB.getLayoutParams();
                    int margin = (int) AndroidUtil.convertDpToPixel(15, PlayerActivity.this);
                    mParamRepeatB.leftMargin = mSeekBar.getLeft() + margin - mIvRepeatB.getWidth() / 2 + (int) ((mSeekBar.getWidth() - margin * 2) * ((float) mRepeatBPosition / mNcg2Player.getDuration()));
                    mIvRepeatB.setLayoutParams(mParamRepeatB);
                    mSeekBar.setProgress(mRepeatAPosition);
                    mIvRepeatB.setVisibility(View.VISIBLE);
                }
            }
            /**
             * 구간 반복 모드 종료
             */
            else {
                imgRepeat.changeRepeatArea();
                mIvRepeatA.setVisibility(View.INVISIBLE);
                mIvRepeatB.setVisibility(View.INVISIBLE);
                mRepeatAPosition = -1;
                mRepeatBPosition = -1;
            }
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        } finally {
            resetUiHideCounting();
        }
    }


    /******
     *
     */
    /*************************
     * ******Screen Orientation
     *************************/
    @SuppressWarnings("deprecation")
    private int getScreenRotation() {
        LogUtil.INSTANCE.info("birdganglock" , "PlayerActivity > getScreenRotation");

        WindowManager wm = (WindowManager) BaseApplication.getAppContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO /* Android 2.2 has getRotation */) {
            try {
                Method m = display.getClass().getDeclaredMethod("getRotation");
                return (Integer) m.invoke(display);
            } catch (Exception e) {
                return Surface.ROTATION_0;
            }
        } else {
            return display.getOrientation();
        }
    }


    private int getVideoScreenOrientation(int width, int height) {
        int result = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

        try {
            LogUtil.INSTANCE.info("birdgangorientation", "onCreate > width :" + width + " , height : " + height);
            boolean landscape = width > height;
            if (landscape) {
                result = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE;
            } else {
                result = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
            }
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }

        return result;
    }


    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    private int getScreenOrientation() {
        LogUtil.INSTANCE.info("birdganglock" , "PlayerActivity > getScreenOrientation");

        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int rot = getScreenRotation();

        @SuppressWarnings("deprecation")
        boolean defaultWide = display.getWidth() > display.getHeight();
        if (rot == Surface.ROTATION_90 || rot == Surface.ROTATION_270) {
            defaultWide = !defaultWide;
        }

        LogUtil.INSTANCE.info("birdganglock" , "PlayerActivity > getScreenOrientation > defaultWide : " + defaultWide + " , rot : " + rot);

        if (defaultWide) {
            switch (rot) {
                case Surface.ROTATION_0:
                    return ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                case Surface.ROTATION_90:
                    return ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                case Surface.ROTATION_180:
                    return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO ? ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE : ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                case Surface.ROTATION_270:
                    return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO ? ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT : ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                default:
                    return 0;
            }
        } else {
            switch (rot) {
                case Surface.ROTATION_0:
                    return ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                case Surface.ROTATION_90:
                    return ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                case Surface.ROTATION_180:
                    return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO ? ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT : ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                case Surface.ROTATION_270:
                    return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO ? ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE : ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                default:
                    return 0;
            }
        }
    }


    private void enableMediaNevigation (boolean enable) {
        try {
            LogUtil.INSTANCE.info("birdgangloadmedia" , "enableMediaNevigation");
            mBtnFileAdvance.setEnabled(enable);
            mBtnFileRetour.setEnabled(enable);
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
    }

    private void setVisibleOverlay(int visible) {
        int orientation = getScreenOrientation();
        LogUtil.INSTANCE.info("birdganglock", "setVisibleOverlay > visible : " + visible + " , orientation : " + orientation);

        setVisibleOverlay(visible, mHeaderAndSettingViewGroup);
        setVisibleOverlay(visible, mExtraViewGroup);
        setVisibleOverlay(visible, mControlerViewControlerOnPlayer);
        setVisibleOverlay(visible, mControlerViewPosOnPlayer);
        setVisibleOverlay(visible, mControlerViewPosAndSeekBarOnPlayer);


        if (StringUtils.equals(AppConstants.TYPE_PLAYER_VISUALONPLAYER, mPlayerType)) {
            setVisibleOverlay(visible, mSpeedViewGroup);
        } else {
            setVisibleOverlay(View.GONE, mSpeedViewGroup);
        }

        if (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT == orientation) {
            if (mEnableTouchScreenLock) {
                setVisibleOverlay(View.VISIBLE, mFooterViewGroup);
                setVisibleOverlay(View.VISIBLE, mImgTouchLockScreen);
            } else {
                setVisibleOverlay(visible, mFooterViewGroup);
                setVisibleOverlay(visible, mImgTouchLockScreen);
            }

            int resultVisible = needToOverlayForPlayType() ?  visible : View.GONE;

            setVisibleOverlay(resultVisible, mImgRepeatMode);
            setVisibleOverlay(visible, mImgRepeatArea);
            setVisibleOverlay(View.GONE, mImgRepeatModeHor);
            setVisibleOverlay(View.GONE, mImgRepeatAreaHor);
        }
        else if (ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE == orientation) {
            if (mEnableTouchScreenLock) {
                setVisibleOverlay(View.VISIBLE, mFooterViewGroup);
                setVisibleOverlay(View.VISIBLE, mImgTouchLockScreenHor);
                setVisibleOverlay(View.VISIBLE, mControlerViewTouchLockscreenHor);
            } else {
                setVisibleOverlay(visible, mFooterViewGroup);
                setVisibleOverlay(visible, mImgTouchLockScreenHor);
                setVisibleOverlay(visible, mControlerViewTouchLockscreenHor);
            }

            int resultVisible = needToOverlayForPlayType() ?  visible : View.GONE;

            setVisibleOverlay(resultVisible, mImgRepeatModeHor);
            setVisibleOverlay(visible, mImgRepeatAreaHor);
            setVisibleOverlay(View.GONE, mImgRepeatMode);
            setVisibleOverlay(View.GONE, mImgRepeatArea);
        }

        setForceVisibleOverlayForPlayType();
    }

    /**
     * 파일 탐색기 기능이 필요없는 경우 (검색 & 즐겨찾기등
     */
    private void setForceVisibleOverlayForPlayType () {
        int resultVisible = needToOverlayForPlayType() ?  View.VISIBLE : View.GONE;
        setVisibleOverlay(resultVisible, mBtnFileRetour);
        setVisibleOverlay(resultVisible, mBtnFileAdvance);
    }


    private boolean needToOverlayForPlayType () {
//        switch (PLAY_TYPE) {
//            case AppConstants.TYPE_PLAY_FAVORITE :
//            case AppConstants.TYPE_PLAY_SEARCH :
//                return false;
//            default:
//                return true;
//        }
        return true;

    }


    private boolean needToRepeatAllForPlayType () {
        switch (PLAY_TYPE) {
            case AppConstants.TYPE_PLAY_FAVORITE :
            case AppConstants.TYPE_PLAY_SEARCH :
                return false;
            default:
                return true;
        }
    }


    private void setVisibleOverlay (int visible, View view) {
        if (View.VISIBLE == visible) {
            AnimUtils.fadeIn(view);
        } else {
            AnimUtils.fadeOut(view);
        }
    }


    /***********************
     * player controler
     ***********************/
    public void resetUiHideCounting() {
        mUIHideCountingDown = UI_HIDE_MAX_COUNT;
    }

    public void countingDownHideCount() {
        if (mEnableTouchScreenLock) {
            setVisibleOverlay(View.GONE);
            return;
        }

        int visible = mHeaderAndSettingViewGroup.getVisibility();

        if (View.VISIBLE == visible) {
            mUIHideCountingDown--;
            if (mUIHideCountingDown <= 0) {
                mUIHideCountingDown = UI_HIDE_MAX_COUNT;
                if (View.GONE != visible) {
                    setVisibleOverlay(View.GONE);
                }
            }
        }
    }

    private boolean isDownloadAndPlay() {
        if (mNcgFileSize != 0) {
            if (mIsDownloadComplete == false) {
                return true;
            } else {
                return false;
            }
        }

        return false;
    }

    public boolean isPlaying() {
        return (null == mNcg2Player || !mNcg2Player.isPlaying()) ? false : true;
    }

    private void stopPosTimer() {
        if (mTimeCheckTimer != null) {
            mTimeCheckTimer.cancel();
        }
        mTimeCheckTimer = null;
        mTimeCheckTimerTask = null;
    }

    /**
     * @brief timer to update playing time
     * @return void
     */
    private void startPosTimer() {
        stopPosTimer();

        if (mTimeCheckTimer == null) {
            mTimeCheckTimer = new Timer();
        }

        if (mTimeCheckTimerTask == null) {
            mTimeCheckTimerTask = new TimerTask() {
                @Override
                public void run() {
                    updatePlayingTime(mNcg2Player.getCurrentPosition());
                }
            };
        }

        mTimeCheckTimer.schedule(mTimeCheckTimerTask, 10, 500);
    }


    private void setPreparedData() {
        // onPause 에서 재생한 경우
        if (StringUtils.equals(AppConstants.STATE_PLAYT_ONPAUSE, PLAY_STATE)) {
            return;

            // onResume 에서 재생한 경우
        } else if (StringUtils.equals(AppConstants.STATE_PLAYT_ONRESUME, PLAY_STATE)) {

            // 최초 실행시 (move/next/complete)
        } else if (StringUtils.equals(AppConstants.STATE_PLAYT_ONSTOP, PLAY_STATE)) {

            // 북마크 로 재생한 경우
            if (StringUtils.equals(AppConstants.TYPE_PLAY_BOOKMARK, PLAY_TYPE)) {
                // 재생 시점 설정
                mCurrentPosition = Integer.valueOf(mBookmarkEntry.getBookmarkLocation());

                // 로컬에서 재생한 경우
            } else if (StringUtils.equals(AppConstants.TYPE_PLAY_DOWNLOAD, PLAY_TYPE)
                    || StringUtils.equals(AppConstants.TYPE_PLAY_PLAYEDLIST, PLAY_TYPE)
                    || StringUtils.equals(AppConstants.TYPE_PLAY_FAVORITE, PLAY_TYPE)
                    || StringUtils.equals(AppConstants.TYPE_PLAY_SEARCH, PLAY_TYPE)) {
                // 재생 시점 설정

                String lastPlayTime = mContentEntry.getContentLastPlayTime();

                if (StringUtils.isBlank(lastPlayTime)) {
                    mCurrentPosition = 0;
                } else {
                    mCurrentPosition = Integer.valueOf(lastPlayTime);
                }

                LogUtil.INSTANCE.info(TAG, "setPreparedData > lastPlayTime : " + lastPlayTime);
            }
            // 잘못된 시작 방법
        } else {
            closePlayer();
        }

        LogUtil.INSTANCE.info(TAG, "setPreparedData > mCurrentPosition : " + mCurrentPosition + " , PLAY_STATE : " + PLAY_STATE);

    }


    protected void closePlayer() {
        LogUtil.INSTANCE.info("birdganglifecycl", "closePlayer");

        try {
            // 결과 코드설정
            setActivityResultCode();
            updateLastUpdatePlayTime();
            // Player 의 reset() 호출.
            resetPlayback();
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        } finally {
            finish();
        }
    }

    private void resetPlayback() {
        LogUtil.INSTANCE.info("birdganglifecycl" , "PlayerActivity > resetPlayback");
        // 플레이어 재설정
        stopPosTimer();
        try {
            if (mNcg2Player.isPlaying()) {
                mCurrentPosition = mNcg2Player.getCurrentPosition();
                try {
                    mNcg2Player.stop();
                } catch (Ncg2Exception e) {
                    LogUtil.INSTANCE.error(TAG, e);
                }
            }
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }

        try {
            mNcg2Player.reset();
            LogUtil.INSTANCE.info("birdganglifecycl" , "PlayerActivity > mPlayer.reset()");
        } catch (Ncg2Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
    }

    // 멈춤 (move/previous 발생시 사용)
    private void releasePlayback() {
        LogUtil.INSTANCE.info("birdganglifecycl" , "PlayerActivity > releasePlayback");

        // 플레이어 종료
        stopPosTimer();
        try {
            resetPlayback();
            mNcg2Player.release();
            mPlayAndPause.setChecked(true);
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
    }


    /**
     * @return
     */
    private int getSeekableEndTime() {
        return mExpectedCurrentEndTime;
    }

    /****
     *
     * @param timePos
     */
    private void seekTo(int timePos) {
        LogUtil.INSTANCE.info("birdgangseek", "seekTo" + " - " + "timePos : " + timePos);

        if (mIsPrepared == false) {
            LogUtil.INSTANCE.info(TAG, "You need to complete the prepre task before this call.");
            return;
        }

        try {
            if (isDownloadAndPlay() && getSeekableEndTime() < timePos) {
                timePos = getSeekableEndTime();
            }
            mNcg2Player.seek(timePos);
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
    }

    private void registerUnlockReceiver() {
        LogUtil.INSTANCE.info("birdganglifecycl", "registerUnlockReceiver");

        IntentFilter unlockReceiverfilter = new IntentFilter();
        unlockReceiverfilter.addAction(Intent.ACTION_USER_PRESENT);
        unlockReceiverfilter.addAction(Intent.ACTION_SCREEN_ON);
        unlockReceiverfilter.addAction(Intent.ACTION_SCREEN_OFF);
        mUnlockReceiver = new UnlockReceiver();
        registerReceiver(mUnlockReceiver, unlockReceiverfilter);
    }

    /**
     * @brief register NetworkChangeReceiver
     * @return void
     */
    public boolean registerNetworkChangeReceiver() {
        IntentFilter networkChangeReceiverFilter = new IntentFilter();
        networkChangeReceiverFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        networkChangeReceiverFilter.addAction(getPackageName() + ".push_player");
        mNetworkReceiver = new NetworkChangeReceiver();
        registerReceiver(mNetworkReceiver, networkChangeReceiverFilter);
        return false;
    }


    private void setActivityResultCode() {
        setResult(0);
    }

    // 완전 종료 (onDestroy 발생시 사용)
    private void onDestroyPlayback() {
        LogUtil.INSTANCE.info("birdganglifecycl" , "PlayerActivity > onDestroyPlayback");

        // 플레이어 종료
        stopPosTimer();
        try {
            resetPlayback();
            mNcg2Player.release();
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
    }


    /*************************
     *
     *
     *************************/
    public class PlayerNavigatorFactory {
        public PlayerNavigator createAudioNavigator() {
            PlayerNavigator navigator = new AudioPlayerNavigator();
            return navigator;
        }

        public PlayerNavigator createVideoNavigator() {
            PlayerNavigator navigator = new VideoPlayerNavigator();
            return navigator;
        }
    }


    public abstract class PlayerNavigator {
        abstract void doPause();
        abstract void doDestroy();
        abstract void doSurfaceCreated();
    }


    /*******
     * @class mOnSeekBarChangeListener
     * @brief register OnSeekBarChangeListener to SeekBar
     * <br>called when there is any change on SeekBar.
     */
    SeekBar.OnSeekBarChangeListener mOnSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            boolean isDownloaded = isDownloadAndPlay();
            if (isDownloaded) {
                if (fromUser && progress > getSeekableEndTime()) {
                    seekBar.setProgress(getSeekableEndTime());
                }
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            stopPosTimer();
            mIsSeekBarTrackingNow = true;
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            try {
                mIsSeekBarTrackingNow = false;
                startPosTimer();
                int nSeekPos = seekBar.getProgress();

                // A 구간 초과시
                if (checkRepeatA(nSeekPos)) {
                    nSeekPos = mRepeatAPosition;
                }

                // B 구간 초과시
                if (checkRepeatB(nSeekPos)) {
                    return;
                }

                seekTo(nSeekPos);
                resetUiHideCounting();
            } catch (Exception e) {
                LogUtil.INSTANCE.error(TAG, e);
            }
        }
    };


    /***
     *
     */
    private SurfaceHolder.Callback mSurfaceHolderCallback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            mIsSurfaceCreated = false;
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            LogUtil.INSTANCE.info("birdgangscreenrate", " surfaceCreated ");
            mPlayerNavigator.doSurfaceCreated();

            if (ModuleConfig.ENABLE_LOG) {
                if (null == holder) {
                    LogUtil.INSTANCE.info("birdgangscreenrate", " surfaceCreated > null == holder ");
                } else {
                    int width = holder.getSurfaceFrame().width();
                    int height = holder.getSurfaceFrame().height();
                    LogUtil.INSTANCE.info("birdgangscreenrate", " surfaceCreated > null != holder > width : " + width + " , height : " + height);
                }
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            mIsSurfaceCreated = true;
        }
    };


    /***
     *
     */
    private Ncg2Player.OnPreparedListener mPreparedListener = new Ncg2Player.OnPreparedListener() {
        @Override
        public void onPrepared(Ncg2Player ncgPlayer) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    LogUtil.INSTANCE.info("birdganglifecycl", "mPreparedListener > mIsForeground : " + mIsForeground);
                    // BackGround 시 종료
                    if (!mIsForeground) {
                        return;
                    }

                    try {
                        mPlayDuration = mNcg2Player.getDuration();
                        int contentId = mContentEntry.getContentId();
                        String lastPlayTime = mContentEntry.getContentLastPlayTime();

                        LogUtil.INSTANCE.info("birdganglifecycl", " mPreparedListener  > mPlayDuration: " + mPlayDuration + " , contentId : " + contentId + " , lastPlayTime : " + lastPlayTime);

                        if (StringUtils.isBlank(lastPlayTime)) {
                            ContentEntry contentEntry = ContentControler.getDefault().findContentById(contentId);
                            if (null != contentEntry) {
                                lastPlayTime = contentEntry.getContentLastPlayTime();
                            }
                        }

                        if (StringUtils.isNotBlank(lastPlayTime) && mRepeatMode == MultiCheckableImageView.REPEAT_OFF) {
                            mCurrentPosition = Integer.parseInt(lastPlayTime);
                        } else {
                            mCurrentPosition = 0;
                        }

                        // craete LMS (공통사항)
                        int duration = mPlayDuration / 1000;
                        if (mPlayDuration % 1000 != 0) {
                            duration = duration + 1;
                        }

                        LogUtil.INSTANCE.info("birdganglifecycl", "mPreparedListener > PlayStatus : " + PLAY_STATE + " ,contentId : " + contentId + " , RepeatMode : " + mRepeatMode + " , lastPlayTime : " + lastPlayTime
                                + " , mCurrentPosition : " + mCurrentPosition + " , mPlayDuration : " + mPlayDuration + " , mSkipTime : " + mSkipTime + " , duration : " + duration + " , mPlayerType : " + mPlayerType);

                        // onPause 에서 재생한 경우
                        if (StringUtils.equals(PLAY_STATE, AppConstants.STATE_PLAYT_ONPAUSE)) {
                            return;
                            // onResume 에서 재생한 경우
                        } else if (StringUtils.equals(PLAY_STATE, AppConstants.STATE_PLAYT_ONRESUME)) {
                            // 최초 실행시 (move/next/complete)
                        } else if (StringUtils.equals(PLAY_STATE, AppConstants.STATE_PLAYT_ONSTOP)) {
                            if (mRepeatMode != MultiCheckableImageView.REPEAT_ONE) {
                                LmsEntry lmsEntry = LmsControler.getDefault().findLmsCacheEntryByPath(mNcgFilePath);
                                String section = lmsEntry.getSection();
                                String rawSection = lmsEntry.getRawSection();
                                String rate = lmsEntry.getRate();
                                LogUtil.INSTANCE.info("birdgangloadlms", "section : " + section + " , rawSection : " + rawSection + " , duration : " + duration + " , rate : " + rate);
                                LmsControler.getDefault().init(section, rawSection, duration, mNcgFilePath);
                            }
                            // 잘못된 시작 방법
                        } else {
                            closePlayer();
                        }

                        mSeekBar.setMax(mPlayDuration);
                        mSeekBar.setProgress(mCurrentPosition);
                        mSeekBar.setSecondaryProgress(0);

                        try {
                            mNcg2Player.setDisplayMode(mCurrentDisplayMode);
                            mNcg2Player.seek(mCurrentPosition);
                            mPlaySpeed = 0;

                            setPlaybackSpeed();
                            resumePlayback();
                            checkShownGuide();

                            LogUtil.INSTANCE.info("birdganglifecycl", "mPreparedListener > done > mCurrentDisplayMode : " + mCurrentDisplayMode.name());
                        } catch (Exception e) {
                            Toast.makeText(PlayerActivity.this, "Ncg2Player.OnPreparedListener-onPrepared" + "\n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        LogUtil.INSTANCE.error(TAG, e);
                    }
                }
            });

            startPosTimer();
            setUIEnable(true);
            enableMediaNevigation(true);
            mIsPrepared = true;
            PLAY_STATE = AppConstants.STATE_PLAYT_ONRESUME;

            ProgressManagerClient.getInstance().stopProgress(PlayerActivity.this);
        }
    };


    /***
     *
     */
    private Ncg2Player.OnSeekCompleteListener mSeekCompleteListener = new Ncg2Player.OnSeekCompleteListener() {
        @Override
        public void onSeekComplete(Ncg2Player ncgPlayer) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    startPosTimer();
                }
            });
        }
    };


    /***
     *
     */
    private Ncg2Player.OnStatusChangeListener mStatusChangeListener = new Ncg2Player.OnStatusChangeListener() {
        @Override
        public void OnStatusChange(Ncg2Player ncgPlayer) {
            LogUtil.INSTANCE.info("birdganglifecycl" , "PlayerActivity > Ncg2Player.OnStatusChangeListener-OnStatusChange-run ");

            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    stopPosTimer();
                    try {
                        mNcg2Player.reset();
                        mNcg2Player.release();
                    } catch (Ncg2Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        initNcgPlayer();
                        if (StringUtils.equals(mPlayerType, AppConstants.TYPE_PLAYER_VISUALONPLAYER)) {
                            if (StringUtils.equals(SWXPLAY_TYPE, AppConstants.TYPE_SWXPLAY_NO_ALLOW)) {
                                mNcg2Player.setSwCodecMode(false);
                            } else if (StringUtils.equals(SWXPLAY_TYPE, AppConstants.TYPE_SWXPLAY_ALLOW)) {
                                mNcg2Player.setSwCodecMode(true);
                            }
                        }
                        mDataSourceSetupTask = new DataSourceSetupTask();
                        mDataSourceSetupTask.execute((Void[])null);
                    } catch (Exception e) {
                        LogUtil.INSTANCE.error(TAG, e);
                    }
                }
            });
        }
    };


    /***
     *
     */
    private Ncg2Player.OnCompletionListener mCompletionListener = new Ncg2Player.OnCompletionListener() {
        @Override
        public void onCompletion(Ncg2Player ncgPlayer) {
            LogUtil.INSTANCE.info("birdganglifecycl" , "PlayerActivity > Ncg2Player.OnCompletionListener mCompletionListener ");

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (mRepeatMode == MultiCheckableImageView.REPEAT_ONE) {
                            mDataSourceSetupTask = null;
                            mIsPrepared = false;
                            mPlaySpeed = 0;
                            PLAY_STATE = AppConstants.STATE_PLAYT_ONSTOP;
                            mLastPlayTime = 0;

                            releasePlayback();
                            initNcgPlayer();

                            mCurrentPosition = 0;

                            if (!mIsSurfaceSenario) {
                                mDataSourceSetupTask = new DataSourceSetupTask();
                                mDataSourceSetupTask.execute((Void[]) null);
                            }
                        } else if (mRepeatMode == MultiCheckableImageView.REPEAT_ALL) {
                            mLastPlayTime = 0;
                            onClickFileAdvance();

                            // 파일 종료
                        } else {
                            mLastPlayTime = 0;
                            closePlayer();
                        }
                    } catch (Exception e) {
                        LogUtil.INSTANCE.error(TAG, e.getMessage());
                    }
                }
            });
        }
    };


    /***
     *
     */
    private Ncg2Player.OnErrorListener mErrorListener = new Ncg2Player.OnErrorListener() {

        @Override
        public boolean onError(Ncg2Player ncgPlayer, int what, int extra) {
            final int errorCode = what;
            final int extraCode = extra;

            LogUtil.INSTANCE.info(TAG, "Ncg2Player.OnErrorListener" + " - " + "errorCode :" + errorCode + "(" + Integer.toHexString(errorCode) + ")");
            LogUtil.INSTANCE.info(TAG, "Ncg2Player.OnErrorListener" + " - " + "extraCode :" + extraCode + "(" + Integer.toHexString(extraCode) + ")");

            if (PlayerActivity.this.isFinishing() || mIsSkipErrorHandling) {
                return true;
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // 메시지 message
                    Toast.makeText(PlayerActivity.this, "Ncg2Player.OnErrorListener-onError-run"
                            + "\n" + "errorCode :" + errorCode + "(" + Integer.toHexString(errorCode) + ")"
                            + "\n" + "extraCode :" + extraCode + "(" + Integer.toHexString(extraCode) + ")", Toast.LENGTH_LONG).show();

                    // Crash Report 전송
                    Exception ex = new Exception(String.format("Ncg2Player.OnErrorListener-onError-run : class name : %s "
                                    + "errorCode : %s "
                                    + "extraCode : %s "
                                    + "LastErrorMsg : %s "
                                    + "\n" + "OS Version : %s"
                                    + "\n" + "Device Model : %s"
                                    + "\n" + "PlayerType : %s"
                                    + "\n" + "SwXPlay : %s"
                                    + "\n" + "PlayType : %s"
                                    + "\n" + "UserID : %s"
                                    + "\n" + "NcgFilePath : %s"
                                    + "\n" + "NcgFileName : %s"
                                    + "\n" + "LmsPercent : %s"
                                    + "\n" + "IsAllowCellularNetwork : %s",
                            getClass().toString(),
                            errorCode + "(" + Integer.toHexString(errorCode) + ")",
                            extraCode + "(" + Integer.toHexString(extraCode) + ")",
                            mNcg2Player.getLastErrorMessage(),
                            Build.VERSION.RELEASE,
                            AndroidUtil.getDeviceModel(),
                            mPlayerType,
                            SWXPLAY_TYPE,
                            PLAY_TYPE,
                            mUserId,
                            mNcgFilePath,
                            mNcgFileName,
                            0,
                            mIsAllowCellularNetwork));

                    try {
                        if (mNcg2Player.isPlaying()) {
                            mNcg2Player.stop();
                        } else {
                            mNcg2Player.reset();
                            mIsPrepared = false;
                        }
                    } catch (Exception e) {
                        LogUtil.INSTANCE.error(TAG, e.getMessage());
                    }

                    finish();

                    ProgressManagerClient.getInstance().stopProgress(PlayerActivity.this);
                }
            });
            return false;
        }
    };


    /***
     *
     */
    private Ncg2Player.OnBufferingUpdateListener mBufferingUpdateListener = new Ncg2Player.OnBufferingUpdateListener() {
        @Override
        public void onBufferingUpdate(Ncg2Player ncgPlayer, int percent) {
            LogUtil.INSTANCE.debug(TAG, "BufferingUpdateListener > onBufferingUpdate > percent " + percent);
        }
    };


    public AudioManager.OnAudioFocusChangeListener mAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            switch (focusChange) {
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                    // pause playback
                case AudioManager.AUDIOFOCUS_GAIN:
                    // resume playback
                case AudioManager.AUDIOFOCUS_LOSS:
                    // stop playback
            }
        }
    };


    /***
     *
     */
    private class VideoOnGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDoubleTap(MotionEvent me) {
            return false;
        }
    }


    public class VideoPlayerNavigator extends PlayerNavigator {
        @Override
        void doPause() {
            LogUtil.INSTANCE.info("birdganglifecycl", "VideoPlayerNavigator > doPause > mIsSurfaceSenario : " + mIsSurfaceSenario);

            if (StringUtils.equals(PLAY_STATE, AppConstants.STATE_PLAYT_ONRESUME)) {
                PLAY_STATE = AppConstants.STATE_PLAYT_ONPAUSE;
            }

            mIsForeground = false;

            if (StringUtils.equals(mPlayerType, AppConstants.TYPE_PLAYER_VISUALONPLAYER)) {
                pausePlayback();
            } else if (StringUtils.equals(mPlayerType, AppConstants.TYPE_PLAYER_NATIVE)) {
                if (mNcg2Player.isPlaying()) {
                    mIsRealPlaying = true;
                } else {
                    mIsRealPlaying = false;
                }
                pausePlayback();
            }
        }

        @Override
        void doDestroy() {
            LogUtil.INSTANCE.info("birdganglifecycl", "VideoPlayerNavigator > doDestroy > mIsSurfaceSenario : " + mIsSurfaceSenario);

            try {
                releaseWakeLock();

                if (mUnlockReceiver != null) {
                    unregisterReceiver(mUnlockReceiver);
                    mUnlockReceiver = null;
                }

                if (mNetworkReceiver != null) {
                    unregisterReceiver(mNetworkReceiver);
                    mNetworkReceiver = null;
                }

                if (mAudioManager != null) {
                    mAudioManager.unregisterMediaButtonEventReceiver(mRemoteComponent);
                }

                onDestroyPlayback();
            } catch (Exception e) {
                LogUtil.INSTANCE.error(TAG, e);
            }

            // 앱 상태 변화 (READY)
            BaseApplication application = (BaseApplication) getApplication();
            application.setAppState(AppConstants.APPLICATION_STATE_READY);
        }

        @Override
        void doSurfaceCreated() {
            mIsSurfaceCreated = true;
            LogUtil.INSTANCE.info("birdganglifecycl", "VideoPlayerNavigator > doSurfaceCreated > mIsSurfaceSenario : " + mIsSurfaceSenario + " , mIsSurfaceCreated : " + mIsSurfaceCreated + " , mIsPrepared : " + mIsPrepared);

            if (mIsSurfaceSenario) {
                if (mDataSourceSetupTask == null) {
                    executeDataSourceSetupTask();
                } else {
                    boolean keyguardInputMode = AndroidUtil.inKeyguardRestrictedInputMode(PlayerActivity.this);
                    LogUtil.INSTANCE.info("birdganglifecycl", "VideoPlayerNavigator > doSurfaceCreated > keyguardInputMode : " + keyguardInputMode);

                    if (!keyguardInputMode) {
                        if (StringUtils.equals(mPlayerType, AppConstants.TYPE_PLAYER_VISUALONPLAYER)) {
                            resumePlayback();
                        } else if (StringUtils.equals(mPlayerType, AppConstants.TYPE_PLAYER_NATIVE)) {
                            if (mIsRealPlaying) {
                                resumePlayback();
                            }
                        }
                    } else {
                        LogUtil.INSTANCE.info(TAG, "screen - locked");
                    }
                }
            }
        }
    }


    public class AudioPlayerNavigator extends PlayerNavigator {
        @Override
        void doPause() {
            if (StringUtils.equals(PLAY_STATE, AppConstants.STATE_PLAYT_ONRESUME)) {
                PLAY_STATE = AppConstants.STATE_PLAYT_ONPAUSE;
            }

            mRepeatMode = MultiCheckableImageView.REPEAT_OFF;
        }

        @Override
        void doDestroy() {
            try {
                mDataSourceSetupTask = null;
                mIsPrepared = false;

                releaseWakeLock();

                if (mUnlockReceiver != null) {
                    unregisterReceiver(mUnlockReceiver);
                    mUnlockReceiver = null;
                }

                if (mNetworkReceiver != null) {
                    unregisterReceiver(mNetworkReceiver);
                    mNetworkReceiver = null;
                }

                onDestroyPlayback();
            } catch (Exception e) {
                LogUtil.INSTANCE.error(TAG, e);
            }

            // 앱 상태 변화 (READY)
            BaseApplication application = (BaseApplication) getApplication();
            application.setAppState(AppConstants.APPLICATION_STATE_READY);
        }

        @Override
        void doSurfaceCreated() {
            mIsSurfaceCreated = true;

            if (mIsSurfaceSenario) {
                if (mDataSourceSetupTask == null) {
                    executeDataSourceSetupTask();
                }
            }
        }
    }


    private void setupAirplaneMode () {
        //AndroidUtil.changeAirplaneMode(this, mRestrictInternet);
    }

    private void setupWakeLock() {
        LogUtil.INSTANCE.info("birdganglifecycl", "setupWakeLock");

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE | PowerManager.ACQUIRE_CAUSES_WAKEUP, "NCG2SamplePlayer");
        mWakeLock.acquire();
    }

    private void releaseWakeLock() {
        if (mWakeLock != null) {
            mWakeLock.release();
        }
    }


    private void setupPhoneListener() {
        LogUtil.INSTANCE.info("birdganglifecycl", "setupPhoneListener");

        if (mTelephonyManager == null) {
            mTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            mTelephonyManager.listen(mPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        }
    }


    private PhoneStateListener mPhoneStateListener = new PhoneStateListener() {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            LogUtil.INSTANCE.info(TAG, "mPhoneStateListener : state : " + state + " , incomingNumber : " + incomingNumber);
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    pausePlayback();
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    pausePlayback();
                    break;
            }
        }
    };


    private void setPlaybackSpeed() {
        LogUtil.INSTANCE.info(TAG, "setPlaybackSpeed  > mPlaySpeed : " + mPlaySpeed);
        mNcg2Player.setPlaybackSpeed(mPlaySpeed);

        if (mTxtPlaySpeed != null) {
            mTxtPlaySpeed.setText(String.format("%.1fx", (100 + mPlaySpeed) / 100.0));
        }
    }


    /**********
     * 플레이어 컨트롤.
     * @return
     */
    private boolean resumePlayback() {
        mPlayAndPause = (ToggleButton)findViewById(R.id.btn_playnpause);

        // 플레이어 재설정
        startPosTimer();

        LogUtil.INSTANCE.info("birdganglifecycl", "resumePlayback > PLAY_STATE : " + PLAY_STATE + " , mIsPauseded : " + mIsPauseded + " , mIsSurfaceCreated : " + mIsSurfaceCreated + " , mIsPrepared : " + mIsPrepared);

        try {
            if (StringUtils.equals(PLAY_STATE, AppConstants.STATE_PLAYT_ONRESUME)) {
                if (mIsPauseded) {
                    mNcg2Player.resume();
                    mIsPauseded = false;
                } else {
                    mNcg2Player.start();
                }
            } else {
                mNcg2Player.start();
            }

            if (isPlaying()) {
                mPlayAndPause.setChecked(false);
            } else {
                mPlayAndPause.setChecked(true);
            }
        } catch (Ncg2Exception e) {
            LogUtil.INSTANCE.error("birdganglifecycl", e);
            mPlayAndPause.setChecked(true);
            return false;

        } catch (Exception e) {
            LogUtil.INSTANCE.error("birdganglifecycl", e);
            mPlayAndPause.setChecked(true);
            return false;
        }
        return true;
    }


    // 일시정지 (Button 클릭시 사용)
    private boolean pausePlayback() {
        LogUtil.INSTANCE.info("birdganglifecycl", "pausePlayback > PLAY_STATE : " + PLAY_STATE + " , mIsPauseded : " + mIsPauseded);

        mPlayAndPause = (ToggleButton)findViewById(R.id.btn_playnpause);

        // 플레이어 재설정
        stopPosTimer();

        try {
            mCurrentPosition = mNcg2Player.getCurrentPosition();
            if (StringUtils.equals(PLAY_STATE, AppConstants.STATE_PLAYT_ONPAUSE)) {
                if (mIsPauseded == false) {
                    mNcg2Player.suspend();
                    mIsPauseded = true;
                }
            } else {
                mNcg2Player.pause();
            }

            if (isPlaying()) {
                mPlayAndPause.setChecked(false);
            } else {
                mPlayAndPause.setChecked(true);
            }

            return true;
        } catch (Exception e) {
            mPlayAndPause.setChecked(false);
            return false;
        }
    }


    private boolean suspendPlayback () {
        LogUtil.INSTANCE.info("birdganglifecycl", "suspendPlayback > PLAY_STATE : " + PLAY_STATE + " , mIsSuspended : " + mIsSuspended);
        // 플레이어 재설정
        stopPosTimer();
        try {
            mCurrentPosition = mNcg2Player.getCurrentPosition();
            if (mIsSuspended == false) {
                updateLastUpdatePlayTime();
                releasePlayback();
                mIsSuspended = true;
            }

            if (isPlaying()) {
                mPlayAndPause.setChecked(false);
            } else {
                mPlayAndPause.setChecked(true);
            }

            return true;
        } catch (Exception e) {
            mPlayAndPause.setChecked(false);
            return false;
        }
    }


    // exception 완료
    private void convertRepeatA() {
        try {
            // A point 설정 전
            if (mRepeatAPosition != -1) {
                mParamRepeatA = (RelativeLayout.LayoutParams) mIvRepeatA.getLayoutParams();
                int margin = (int) AndroidUtil.convertDpToPixel(15, PlayerActivity.this);
                mParamRepeatA.leftMargin = mSeekBar.getLeft() + margin - mIvRepeatA.getWidth() / 2 + (int) ((mSeekBar.getWidth() - margin * 2) * ((float) mRepeatAPosition / mNcg2Player.getDuration()));
                mIvRepeatA.setLayoutParams(mParamRepeatA);
                mIvRepeatA.setVisibility(View.VISIBLE);
            } else {
                mIvRepeatA.setVisibility(View.INVISIBLE);
            }
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
    }

    // exception 완료
    private void convertRepeatB() {
        try {
            // B point 설정 전
            if (mRepeatBPosition != -1) {
                mParamRepeatB = (RelativeLayout.LayoutParams) mIvRepeatB.getLayoutParams();
                int margin = (int) AndroidUtil.convertDpToPixel(15, PlayerActivity.this);
                mParamRepeatB.leftMargin = mSeekBar.getLeft() + margin - mIvRepeatB.getWidth() / 2 + (int) ((mSeekBar.getWidth() - margin * 2) * ((float) mRepeatBPosition / mNcg2Player.getDuration()));
                mIvRepeatB.setLayoutParams(mParamRepeatB);
                mIvRepeatB.setVisibility(View.VISIBLE);
            } else {
                mIvRepeatB.setVisibility(View.INVISIBLE);
            }
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
    }


    private void releaseRepeatAB() {
        // 구간 반복 B Point
        mRepeatAPosition = -1;
        mRepeatBPosition = -1;
    }


    // B 구간 초과시
    private boolean checkRepeatB(int timePos) {
        // 구간 반복 설정이 되어 있는 경우
        if (mRepeatBPosition != -1) {
            // 1초 후 B 범위 초과시
            if (timePos >= mRepeatBPosition) {
                return true;
            }
        }

        return false;
    }


    // A 구간 초과시
    private boolean checkRepeatA(int timePos) {
        // 구간 반복 설정이 되어 있는 경우
        if (mRepeatBPosition != -1) {
            // A 범위 초과시
            if (timePos < mRepeatAPosition) {
                return true;
            }
        }

        return false;
    }


    /****
     *
     */
    private void updatePlayingTime(final int millisecond) {
        final int currentMSec = millisecond;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // 마지막 플레이시간 갱신
                mLastPlayTime = currentMSec;

                if (!mIsPrepared) {
                    return;
                }

                // B 범위 초과 시
                try {
                    if (checkRepeatB(currentMSec + 1000)) {
                        mSeekBar.setProgress(mRepeatAPosition);
                        mNcg2Player.seek(mRepeatAPosition);
                        Toast.makeText(PlayerActivity.this, getString(R.string.player_function_start_section_repeat), Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (Exception e) {
                    LogUtil.INSTANCE.error(TAG, e);
                }

                // 시크바 업데이트
                try {
                    if (!mIsSeekBarTrackingNow) {
                        mSeekBar.setProgress(currentMSec);
                        if (mPlayDuration == 0) {
                            mPlayDuration = mNcg2Player.getDuration();
                            mSeekBar.setMax(mPlayDuration);
                        }
                    }

                } catch (Exception e) {
                    LogUtil.INSTANCE.error(TAG, e);
                }

                // 재생시간 업데이트
                try {
                    // 현재 재생 시간
                    if (mTextPositionView != null) {
                        int nTemp = mNcg2Player.getCurrentPosition() / 1000;

                        int nHour = nTemp / 3600;
                        nTemp -= nHour * 3600;
                        int nMin = nTemp / 60;
                        nTemp -= nMin * 60;
                        int nSec = nTemp;

                        String strTimeInfo = String.format("%02d:%02d:%02d", nHour, nMin, nSec);
                        mTextPositionView.setText(strTimeInfo);
                    }

                    // 총 재생 시간
                    if (mTextDurationView != null) {
                        int nTemp = mNcg2Player.getDuration() / 1000;
                        int nDurHour = nTemp / 3600;
                        nTemp -= nDurHour * 3600;
                        int nDurMin = nTemp / 60;
                        nTemp -= nDurMin * 60;
                        int nDurSec = nTemp;

                        String strTimeInfo = String.format("%02d:%02d:%02d", nDurHour, nDurMin, nDurSec);
                        mTextDurationView.setText(strTimeInfo);
                    }

                    countingDownHideCount();
                } catch (Exception e) {
                    LogUtil.INSTANCE.error(TAG, e);
                }

                // LMS 업데이트
                try {
                    if (mIsSeekBarTrackingNow == false) {
                        LmsControler.getDefault().setLMSSection(currentMSec/1000);
                    }
                } catch (Exception e) {
                    LogUtil.INSTANCE.error(TAG, e);
                }
            }
        });
    }


    /***
     * 탐색기 컨텐츠 상태를 업데이트
     * @param contentEntry
     */
    private void updateFavoriteExplorerContentList(ContentEntry contentEntry) {
        // 컨텐츠 목록
        if (null == contentEntry) {
            return;
        }

        int selectedContentId = contentEntry.getContentId();

        LogUtil.INSTANCE.info(TAG, "updatePlayList > PLAY_TYPE : " + PLAY_TYPE);

        if (StringUtils.equals(AppConstants.TYPE_PLAY_DOWNLOAD, PLAY_TYPE)) {
            for (int i = 0; mContentEntries.size() > i; i++) {
                ContentEntry entry = mContentEntries.get(i);
                if (selectedContentId == entry.getContentId()) {
                    mContentEntries.set(i, contentEntry);
                }
            }
        } else if (StringUtils.equals(AppConstants.TYPE_PLAY_PLAYEDLIST, PLAY_TYPE)) {
            for (int i = 0; mPlayedListList.size() > i; i++) {
                ContentEntry playedListEntry = mPlayedListList.get(i);
                int contentId = playedListEntry.getContentId();
                if (selectedContentId == contentId) {
                    ContentEntry playedlist = mPlayedListList.get(i);
                    mPlayedListList.set(i, playedlist);
                }
            }
        } else if (StringUtils.equals(AppConstants.TYPE_PLAY_FAVORITE, PLAY_TYPE)) {
            for (int i = 0; mFavoriteList.size() > i; i++) {
                if (selectedContentId == mFavoriteList.get(i).getContentId()) {
                    mFavoriteList.set(i, contentEntry);
                }
            }
        }

        EventBus.getDefault().notifyEventUpdateFavoriteExplorerContentList(PLAY_TYPE, selectedContentId);
    }


    private void updateLastUpdatePlayTime () {
        try {
            int duration = mNcg2Player.getDuration();

            if (mLastPlayTime >= 0) {
                if (mLastPlayTime >= duration) {
                    mLastPlayTime = 0;
                }
                boolean success = ContentControler.getDefault().updateContentLastPlayTime(mContentId, String.valueOf(mLastPlayTime));
                LogUtil.INSTANCE.info("birdganglifecycl", "updateLastUpdatePlayTime > success :  " + success + " , mContentId : " + mContentId + " , mLastPlayTime : " + mLastPlayTime + " , mNcgFilePath : " + mNcgFilePath);
            }

            updateLmsInfo();
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
    }

    private void updateLmsInfo () throws Exception {
        // LMS 업데이트
        String lastPlayTime = String.valueOf(mLastPlayTime);
        String rate = String.valueOf(LmsControler.getDefault().getLMSPercent());
        String section = LmsControler.getDefault().getProcessedSection();
        String rawSection = LmsControler.getDefault().getRawSection();
        LogUtil.INSTANCE.info("birdgangloadlms", "updateInfo > lastPlayTime : " + lastPlayTime + ", rate : " + rate + " , section : " + section + " , rawSection : " + rawSection);

        LmsEntry lmsEntry = new LmsEntry();
        lmsEntry.setContentId(mContentId);
        lmsEntry.setContentName(mContentEntry.getContentName());
        lmsEntry.setContentFilePath(mContentEntry.getContentFilePath());
        lmsEntry.setContentLastPlayTime(lastPlayTime);
        lmsEntry.setSection(section);
        lmsEntry.setRawSection(rawSection);
        lmsEntry.setRate(rate);

        long result = LmsControler.getDefault().updateLms(lmsEntry);
        LogUtil.INSTANCE.info("birdgangloadlms", "updateLmsInfo > result : " + result);
        EventBus.getDefault().notifyEventUpdateLMSExplorerContentList(PLAY_TYPE);

    }

    /**
     * @class UnlockReceiver
     * @brief ACTION_USER_PRESENT action should be handled in case of unlocking screen
     */
    private class UnlockReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (mIsSurfaceSenario) {
                    String action = intent.getAction();
                    if (action.equals(Intent.ACTION_SCREEN_OFF)) {          // 화면 off
                        LogUtil.INSTANCE.info("birdganglifecycl", " UnlockReceiver > " + intent.getAction());
                        mIsReadyUnlock = true;

                    } else if (action.equals(Intent.ACTION_USER_PRESENT) || action.equals(Intent.ACTION_SCREEN_ON)) {
                        LogUtil.INSTANCE.info("birdganglifecycl", " UnlockReceiver >" + intent.getAction() + " , mIsForeground : " + mIsForeground);
                        if (mIsForeground) {
                            if (mDataSourceSetupTask == null) {
                                executeDataSourceSetupTask();
                            } else {
                                boolean keyguardInputMode = AndroidUtil.inKeyguardRestrictedInputMode(PlayerActivity.this);
                                LogUtil.INSTANCE.info("birdganglifecycl", "UnlockReceiver > doSurfaceCreated > keyguardInputMode : " + keyguardInputMode);
                                if (!keyguardInputMode) {
                                    if (mIsReadyUnlock) {
                                        LogUtil.INSTANCE.info("birdganglifecycl", ":UnlockReceiver - ready");
                                        if (StringUtils.equals(mPlayerType, AppConstants.TYPE_PLAYER_VISUALONPLAYER)) {
                                            resumePlayback();
                                        } else if (StringUtils.equals(mPlayerType, AppConstants.TYPE_PLAYER_NATIVE)) {
                                            if (mIsRealPlaying) {
                                                resumePlayback();
                                            }
                                        }
                                        mIsReadyUnlock = false;
                                    } else {
                                        LogUtil.INSTANCE.info(TAG, ":UnlockReceiver - not ready");
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                LogUtil.INSTANCE.error(TAG, e);
            }
        }
    }


    private class NetworkChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                String action = intent.getAction();
                LogUtil.INSTANCE.info("birdganglifecycl", "NetworkChangeReceiver > onReceive > action : " + action);

                if (StringUtils.equals(action, ConnectivityManager.CONNECTIVITY_ACTION)) {
                    if (mIsSurfaceSenario) {
                        if (mIsForeground == true && mIsSurfaceCreated == true) {
                            if (mDataSourceSetupTask == null) {
                                executeDataSourceSetupTask();
                            }
                        }
                    }

                } else if (StringUtils.equals(action, getPackageName() + ".push_player")) {
                    Bundle bundle = intent.getBundleExtra("push");
                    if (bundle != null) {
                        pausePlayback();
                        new CustomAlertDialog(PlayerActivity.this)
                                .setConfirmText(getString(R.string.dialog_ok))
                                .setTitleText(bundle.getString("title"))
                                .setConfirmBtnColoer(BaseConfiguration.getDefault().getAppDialogBtnColor())
                                .setContentText(bundle.getString("desc"))
                                .show();
                    }
                }
            } catch (Exception e) {
                LogUtil.INSTANCE.error(TAG, e);
            }
        }
    }


    /**
     *
     * @return
     */
    protected int provideCompanyLogo() {
        return R.drawable.img_companylogo;
    }


}
