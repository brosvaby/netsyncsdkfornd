package com.inka.netsync.view.dialog;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.inka.netsync.R;
import com.inka.netsync.logs.LogUtil;
import com.inka.netsync.view.dialog.anim.OptAnimationLoader;

/**
 * Created by birdgang on 2018. 1. 31..
 */

public class PlayerSettingDialog extends AlertDialog implements View.OnClickListener {

    private final String TAG = "PlayerSettingDialog";

    private View mDialogView;
    private AnimationSet mModalInAnim;
    private AnimationSet mModalOutAnim;
    private Animation mOverlayOutAnim;
    private TextView mTitleTextView;

    private String mTitleText;
    private boolean mShowCancel;
    private String mConfirmText;
    private Button mConfirmButton;

    private TextView mTextSkipTime;
    private ImageView mImgMinus;
    private ImageView mImgPlus;

    private ToggleButton mToggleButtonFull;
    private ToggleButton mToggleButtonBase;

    private OnSweetClickListener mConfirmClickListener;
    private boolean mCloseFromCancel;

    private int mSkipTime = 0;
    private int mScreenRate = 0;

    public static interface OnSweetClickListener {
        public void onClick(PlayerSettingDialog sweetAlertDialog);
    }

    public PlayerSettingDialog(Context context) {
        super(context, R.style.alert_dialog);
        setCancelable(true);
        setCanceledOnTouchOutside(false);

        mModalInAnim = (AnimationSet) OptAnimationLoader.loadAnimation(getContext(), R.anim.modal_in);
        mModalOutAnim = (AnimationSet) OptAnimationLoader.loadAnimation(getContext(), R.anim.modal_out);
        mModalOutAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                mDialogView.setVisibility(View.GONE);
                mDialogView.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mCloseFromCancel) {
                            PlayerSettingDialog.super.cancel();
                        } else {
                            PlayerSettingDialog.super.dismiss();
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
    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_alert_player_setting);

        Rect displayRectangle = new Rect();
        Window window = getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.dialog_alert, null);
        layout.setMinimumWidth((int)(displayRectangle.width() * 0.9f));
        layout.setMinimumHeight((int)(displayRectangle.height() * 0.9f));

        setView(layout);

        mDialogView = getWindow().getDecorView().findViewById(android.R.id.content);
        mTitleTextView = (TextView)findViewById(R.id.title_text);

        mTextSkipTime = (TextView) findViewById(R.id.text_play_skip);

        mImgMinus = (ImageView) findViewById(R.id.btn_play_skip_minus);
        mImgMinus.setOnClickListener(mSkipTimeClickListener);
        mImgPlus = (ImageView) findViewById(R.id.btn_play_skip_plus);
        mImgPlus.setOnClickListener(mSkipTimeClickListener);

        mToggleButtonFull = (ToggleButton) findViewById(R.id.toggle_play_screen_rate_full);
        mToggleButtonFull.setOnClickListener(mScreenRateClickListener);
        mToggleButtonBase = (ToggleButton) findViewById(R.id.toggle_play_screen_rate_base);
        mToggleButtonBase.setOnClickListener(mScreenRateClickListener);

        mConfirmButton = (Button)findViewById(R.id.confirm_button);
        mConfirmButton.setOnClickListener(this);

        setTitleText(mTitleText);
        setConfirmText(mConfirmText);

        setSkipTime(mSkipTime);
        setScreenRate(mScreenRate);
    }


    public PlayerSettingDialog setTitleText (String text) {
        mTitleText = text;
        if (mTitleTextView != null && mTitleText != null) {
            mTitleTextView.setText(mTitleText);
        }
        return this;
    }


    public PlayerSettingDialog setSkipTime (int skipTime) {
        this.mSkipTime = skipTime;
        LogUtil.INSTANCE.info(TAG, "setSkipTime > skipTime : " + skipTime);
        if (null == mTextSkipTime) {
            return this;
        }

        mTextSkipTime.setText(String.valueOf(skipTime));
        return this;
    }

    public PlayerSettingDialog setScreenRate (int screenRate) {
        this.mScreenRate = screenRate;
        LogUtil.INSTANCE.info(TAG, "setScreenRate > screenRate : " + screenRate);

        if (null == mToggleButtonFull || null == mToggleButtonBase) {
            return this;
        }

        try {
            if (1 == screenRate) {
                mToggleButtonFull.setChecked(true);
                mToggleButtonBase.setChecked(false);
            }
            else {
                mToggleButtonFull.setChecked(false);
                mToggleButtonBase.setChecked(true);
            }
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
        return this;
    }

    public boolean isShowCancelButton () {
        return mShowCancel;
    }

    public String getConfirmText () {
        return mConfirmText;
    }

    public int getSkipTime () {
        return mSkipTime;
    }

    public int getScreenRate () {
        return mScreenRate;
    }

    public PlayerSettingDialog setConfirmText (String text) {
        mConfirmText = text;
        if (mConfirmButton != null && mConfirmText != null) {
            mConfirmButton.setText(mConfirmText);
        }
        return this;
    }

    public PlayerSettingDialog setConfirmClickListener (OnSweetClickListener listener) {
        mConfirmClickListener = listener;
        return this;
    }

    protected void onStart() {
        mDialogView.startAnimation(mModalInAnim);
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
        mConfirmButton.startAnimation(mOverlayOutAnim);
        mDialogView.startAnimation(mModalOutAnim);
    }


    private View.OnClickListener mSkipTimeClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                int id = v.getId();
                LogUtil.INSTANCE.info(TAG, "mSkipTimeClickListener > mSkipTime : " + mSkipTime);
                if (id == R.id.btn_play_skip_minus) {
                    if (10 < mSkipTime) {
                        mSkipTime -= 10;
                    }
                } else if (id == R.id.btn_play_skip_plus) {
                    if (60 > mSkipTime) {
                        mSkipTime += 10;
                    }
                }

                mTextSkipTime.setText(String.valueOf(mSkipTime));
            } catch (Exception e) {
                LogUtil.INSTANCE.error(TAG, e);
            }
        }
    };


    private View.OnClickListener mScreenRateClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                int id = v.getId();
                LogUtil.INSTANCE.info(TAG, "mScreenRateClickListener > mSkipTime : " + mSkipTime);
                if (id == R.id.toggle_play_screen_rate_full) {
                    setScreenRate(1);
                }
                else if (id == R.id.toggle_play_screen_rate_base) {
                    setScreenRate(0);
                }
            } catch (Exception e) {
                LogUtil.INSTANCE.error(TAG, e);
            }
        }
    };


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.confirm_button) {
            if (mConfirmClickListener != null) {
                mConfirmClickListener.onClick(PlayerSettingDialog.this);
            } else {
                dismissWithAnimation();
            }
        }
    }

}
