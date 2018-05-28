package com.inka.netsync.view.dialog;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.Transformation;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.inka.netsync.R;
import com.inka.netsync.common.bus.WeakHandler;
import com.inka.netsync.common.utils.AssetsUtil;
import com.inka.netsync.logs.LogUtil;
import com.inka.netsync.view.dialog.anim.OptAnimationLoader;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by birdgang on 2018. 3. 29..
 */

public class MediaLoadingAlertDialog extends AlertDialog {

    private Context context;

    protected static final int ACTIVITY_SHOW_INFOLAYOUT = 2;
    protected static final int ACTIVITY_HIDE_INFOLAYOUT = 3;
    protected static final int ACTIVITY_SHOW_PROGRESSBAR = 4;
    protected static final int ACTIVITY_HIDE_PROGRESSBAR = 5;
    protected static final int ACTIVITY_SHOW_TEXTINFO = 6;
    protected static final int ACTIVITY_UPDATE_PROGRESS = 7;
    protected static final int ACTIVITY_UPDATE_PROGRESS_SYNC = 8;

    private final String TAG = "MediaLoadingAlertDialog";

    private View mDialogView;

    private ProgressBar mInfoProgress;

    private TextView mTitleText;
    private TextView mInfoText;

    private AnimationSet mModalInAnim;
    private AnimationSet mModalOutAnim;
    private Animation mOverlayOutAnim;
    private int mAlertType;
    private boolean mCloseFromCancel;

    private static Drawable mDrawerColProgress;
    private static Drawable mDrawerColProgressSync;

    public static final int NORMAL_TYPE = 0;
    public static final int ERROR_TYPE = 1;
    public static final int SUCCESS_TYPE = 2;
    public static final int WARNING_TYPE = 3;
    public static final int CUSTOM_IMAGE_TYPE = 4;
    public static final int PROGRESS_TYPE = 5;
    public static final int CONFIRM_TYPE = 6;

    private Handler mProgressHandler = new MediaLoadingDialogHandler(this);

    public static interface OnSweetClickListener {
        public void onClick(CustomAlertDialog sweetAlertDialog);
    }

    public MediaLoadingAlertDialog(Context context) {
        this(context, NORMAL_TYPE);
    }

    public MediaLoadingAlertDialog(Context context, int alertType) {
        super(context, R.style.alert_dialog);
        this.context = context;
        setCancelable(true);
        setCanceledOnTouchOutside(false);
        mAlertType = alertType;

        mModalInAnim = (AnimationSet) OptAnimationLoader.loadAnimation(getContext(), R.anim.modal_in);
        mModalOutAnim = (AnimationSet) OptAnimationLoader.loadAnimation(getContext(), R.anim.modal_out);
        mModalOutAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mDialogView.setVisibility(View.GONE);
                mDialogView.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mCloseFromCancel) {
                            MediaLoadingAlertDialog.super.cancel();
                        } else {
                            MediaLoadingAlertDialog.super.dismiss();
                        }
                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        // dialog overlay fade out
        mOverlayOutAnim = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                WindowManager.LayoutParams wlp = getWindow().getAttributes();
                wlp.alpha = 1 - interpolatedTime;
                getWindow().setAttributes(wlp);
            }
        };
        mOverlayOutAnim.setDuration(120);

        mDrawerColProgress = AssetsUtil.getDrawable(context, R.drawable.progress_media_scan);
        mDrawerColProgressSync = AssetsUtil.getDrawable(context, R.drawable.progress_media_scan_sync);
    }


    public void setProgressbarDrawerResource (int resource) {
        if (null == context) {
            return;
        }

        mDrawerColProgress = AssetsUtil.getDrawable(context, resource);
    }

    public void setProgressbarSyncDrawerResource (int resource) {
        if (null == context) {
            return;
        }

        mDrawerColProgressSync = AssetsUtil.getDrawable(context, resource);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_media_loading);

        Rect displayRectangle = new Rect();
        Window window = getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.dialog_media_loading, null);
        layout.setMinimumWidth((int)(displayRectangle.width() * 0.9f));
        layout.setMinimumHeight((int)(displayRectangle.height() * 0.9f));

        mInfoProgress = (ProgressBar) findViewById(R.id.info_progress);
        mTitleText = (TextView) findViewById(R.id.title_text);
        mInfoText = (TextView) findViewById(R.id.info_text);

        setView(layout);

        mDialogView = getWindow().getDecorView().findViewById(android.R.id.content);

        changeAlertType(mAlertType, true);
    }

    private void restore () {
    }

    private void playAnimation () {
    }

    private void changeAlertType(int alertType, boolean fromCreate) {
        mAlertType = alertType;
        if (mDialogView != null) {
            if (!fromCreate) {
                restore();
            }
            if (!fromCreate) {
                playAnimation();
            }
        }
    }

    public int getAlerType () {
        return mAlertType;
    }

    public void changeAlertType(int alertType) {
        changeAlertType(alertType, false);
    }

    public MediaLoadingAlertDialog setCancelAbleDialog (boolean enable) {
        setCancelable(enable);
        return this;
    }

    protected void onStart() {
        mDialogView.startAnimation(mModalInAnim);
        playAnimation();
    }

    @Override
    public void cancel() {
        dismissWithAnimation(true);
    }

    public void dismissWithAnimation() {
        dismissWithAnimation(false);
    }

    private void dismissWithAnimation(boolean fromCancel) {
        mCloseFromCancel = fromCancel;
        if (null != mDialogView) {
            mDialogView.startAnimation(mModalOutAnim);
        }
    }


    public MediaLoadingAlertDialog setDialogTypeLarge () {
        ViewGroup.LayoutParams params = getWindow().getAttributes();
//        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
//        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        params.width = 800;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes((WindowManager.LayoutParams) params);
        return this;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private class MediaLoadingDialogHandler extends WeakHandler<MediaLoadingAlertDialog> {

        public MediaLoadingDialogHandler(MediaLoadingAlertDialog owner) {
            super(owner);
        }

        @Override
        public void handleMessage(Message msg) {
            MediaLoadingAlertDialog ma = getOwner();
            if (ma == null) {
                return;
            }

            LogUtil.INSTANCE.info(TAG, "handleMessage > msg.what : " + msg.what);

            switch (msg.what) {
                case ACTIVITY_SHOW_INFOLAYOUT:
                    ma.onStart();
                    break;
                case ACTIVITY_HIDE_INFOLAYOUT:
                    removeMessages(ACTIVITY_SHOW_INFOLAYOUT);
                    ma.dismissWithAnimation();
                    break;
                case ACTIVITY_SHOW_PROGRESSBAR:
                    ma.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                    break;
                case ACTIVITY_HIDE_PROGRESSBAR:
                    ma.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                    break;
                case ACTIVITY_UPDATE_PROGRESS:
                    int max = msg.arg1;
                    int progress = msg.arg2;
                    String title = (String) msg.obj;
                    LogUtil.INSTANCE.info(TAG, "ACTIVITY_UPDATE_PROGRESS > max : " + max + " , progress : " + progress + " , title : " + title);
                    if (null != ma.mInfoProgress) {
                        ma.mInfoProgress.setMax(max);
                        ma.mInfoProgress.setProgress(progress);
                        ma.mInfoProgress.setProgressDrawable(mDrawerColProgress);
                    }
                    if (null != ma.mInfoText && StringUtils.isNotEmpty(title)) {
                        ma.mInfoText.setText(title);
                    }
                    if (null != ma.mTitleText) {
                        ma.mTitleText.setText(getContext().getResources().getString(R.string.dialog_progress_load_contents));
                    }
                    break;
                case ACTIVITY_UPDATE_PROGRESS_SYNC:
                    int max1 = msg.arg1;
                    int progress1 = msg.arg2;
                    String title1 = (String) msg.obj;
                    LogUtil.INSTANCE.info(TAG, "ACTIVITY_UPDATE_PROGRESS_SYNC > max1 : " + max1 + " , progress1 : " + progress1 + " , title1 : " + title1);
                    if (null != ma.mInfoProgress) {
                        ma.mInfoProgress.setMax(max1);
                        ma.mInfoProgress.setProgress(progress1);
                        ma.mInfoProgress.setProgressDrawable(mDrawerColProgressSync);
                    }
                    if (null != ma.mInfoText && StringUtils.isNotEmpty(title1)) {
                        ma.mInfoText.setText(title1);
                    }
                    if (null != ma.mTitleText) {
                        ma.mTitleText.setText(getContext().getResources().getString(R.string.dialog_progress_sync_contents));
                    }
                    break;
                case ACTIVITY_SHOW_TEXTINFO:
                    String info = (String) msg.obj;
                    ma.mInfoText.setText(info);
                    if (info == null) {
                        removeMessages(ACTIVITY_SHOW_INFOLAYOUT);
                    } else {
                        if (!hasMessages(ACTIVITY_SHOW_INFOLAYOUT)) {
                            sendEmptyMessageDelayed(ACTIVITY_SHOW_INFOLAYOUT, 300);
                        }
                    }
                    break;
            }
        }
    }


    public void onDiscoveryStarted(String entryPoint) {
        boolean isShowing = isShowing();

        LogUtil.INSTANCE.info(TAG, "onDiscoveryStarted > isShowing : " + isShowing + " , entryPoint : " + entryPoint);
        if (!isShowing) {
            show();
        }
    }

    public void onDiscoveryProgress(String entryPoint) {
        LogUtil.INSTANCE.info(TAG, "onDiscoveryProgress > entryPoint : " + entryPoint);
        mProgressHandler.obtainMessage(ACTIVITY_SHOW_TEXTINFO, entryPoint).sendToTarget();
    }

    public void onDiscoveryCompleted(String entryPoint) {
        LogUtil.INSTANCE.info(TAG, "onDiscoveryCompleted > entryPoint : " + entryPoint);
        mProgressHandler.obtainMessage(ACTIVITY_HIDE_INFOLAYOUT).sendToTarget();
    }

    public void onParsingStatsUpdated(final int percent, final String entryPoint, final boolean parsing) {
        LogUtil.INSTANCE.info(TAG, "onParsingStatsUpdated > percent : " + percent + " , entryPoint : " + entryPoint + " , parsing : " + parsing);
        if (parsing) {
            mProgressHandler.obtainMessage(ACTIVITY_UPDATE_PROGRESS, 100, percent, entryPoint).sendToTarget();
        }
    }

    public void onParsingStatsUpdatedSync(int percent, String entryPoint, final boolean parsing) {
        LogUtil.INSTANCE.info(TAG, "onParsingStatsUpdatedSync > percent : " + percent + " , entryPoint : " + entryPoint + " , parsing : " + parsing);
        if (parsing) {
            mProgressHandler.obtainMessage(ACTIVITY_UPDATE_PROGRESS_SYNC, 100, percent, entryPoint).sendToTarget();
        }
        else {
            mProgressHandler.obtainMessage(ACTIVITY_UPDATE_PROGRESS_SYNC, 100, 100, entryPoint).sendToTarget();
        }
    }



    public int provideProgressBarDrawerResource () {
        return R.drawable.progress_media_scan;
    }

}
