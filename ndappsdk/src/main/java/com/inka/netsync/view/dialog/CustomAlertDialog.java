package com.inka.netsync.view.dialog;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.Transformation;
import android.widget.TextView;

import com.inka.netsync.R;
import com.inka.netsync.logs.LogUtil;
import com.inka.netsync.view.dialog.anim.OptAnimationLoader;
import com.inka.netsync.view.widget.FancyButton;

/**
 * Created by birdgang on 2018. 1. 11..
 */
public class CustomAlertDialog extends AlertDialog implements View.OnClickListener {
    private View mDialogView;
    private AnimationSet mModalInAnim;
    private AnimationSet mModalOutAnim;
    private Animation mOverlayOutAnim;
    private TextView mTitleTextView;
    private TextView mContentTextView;
    private String mTitleText;
    private String mContentText;
    private boolean mShowCancel;
    private boolean mShowContent;
    private String mCancelText;
    private String mConfirmText;
    private int mAlertType;

    private int mConfirmBtnColor = -1;
    private int mCancelBtnColor = -1;

    private FancyButton mConfirmButton;
    private FancyButton mCancelButton;
    private OnSweetClickListener mCancelClickListener;
    private OnSweetClickListener mConfirmClickListener;
    private boolean mCloseFromCancel;

    public static final int NORMAL_TYPE = 0;
    public static final int ERROR_TYPE = 1;
    public static final int SUCCESS_TYPE = 2;
    public static final int WARNING_TYPE = 3;
    public static final int CUSTOM_IMAGE_TYPE = 4;
    public static final int PROGRESS_TYPE = 5;
    public static final int CONFIRM_TYPE = 6;


    public static interface OnSweetClickListener {
        public void onClick(CustomAlertDialog sweetAlertDialog);
    }

    public CustomAlertDialog(Context context) {
        this(context, NORMAL_TYPE);
    }

    public CustomAlertDialog(Context context, int alertType) {
        super(context, R.style.alert_dialog);
        setCancelable(true);
        setCanceledOnTouchOutside(false);
        mAlertType = alertType;

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
                            CustomAlertDialog.super.cancel();
                        } else {
                            CustomAlertDialog.super.dismiss();
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
        setContentView(R.layout.dialog_alert);

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
        mContentTextView = (TextView)findViewById(R.id.content_text);
        mConfirmButton = (FancyButton)findViewById(R.id.confirm_button);
        mCancelButton = (FancyButton)findViewById(R.id.cancel_button);
        mConfirmButton.setOnClickListener(this);
        mCancelButton.setOnClickListener(this);

        setTitleText(mTitleText);
        setContentText(mContentText);
        setCancelText(mCancelText);
        setConfirmText(mConfirmText);
        setConfirmBtnColoer(mConfirmBtnColor);
        setCancelBtnColoer(mCancelBtnColor);
        changeAlertType(mAlertType, true);
        restore();

    }

    private void restore () {
        LogUtil.INSTANCE.info("birdgangcustomalertdialog" , "restore > mConfirmBtnColor : " + mConfirmBtnColor + " , mCancelBtnColor : " + mCancelBtnColor);
        mConfirmButton.setVisibility(View.VISIBLE);
        if (mConfirmBtnColor > 0) {
            mConfirmButton.setBackgroundColor(ContextCompat.getColor(getContext(), mConfirmBtnColor));
        }
        else {
            mConfirmButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.provider_default_color_dialog_btn_confirm));
        }

        if (mCancelBtnColor > 0) {
            mCancelButton.setDisableBorderColor(ContextCompat.getColor(getContext(), mConfirmBtnColor));
            mCancelButton.setBorderColor(ContextCompat.getColor(getContext(), mConfirmBtnColor));
            mCancelButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
            mCancelButton.setTextColor(ContextCompat.getColor(getContext(), mConfirmBtnColor));
        }
        else {
            mCancelButton.setBorderColor(ContextCompat.getColor(getContext(), R.color.provider_default_color_dialog_btn_confirm));
            mCancelButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.provider_default_color_dialog_btn_confirm));
            mCancelButton.setTextColor(ContextCompat.getColor(getContext(), R.color.provider_default_color_dialog_btn_confirm));
        }
    }

    private void playAnimation () {
//        if (mAlertType == ERROR_TYPE) {
//            mErrorFrame.startAnimation(mErrorInAnim);
//            mErrorX.startAnimation(mErrorXInAnim);
//        } else if (mAlertType == SUCCESS_TYPE) {
//            mSuccessTick.startTickAnim();
//            mSuccessRightMask.startAnimation(mSuccessBowAnim);
//        }
    }

    private void changeAlertType(int alertType, boolean fromCreate) {
        mAlertType = alertType;
        // call after created views
        if (mDialogView != null) {
            if (!fromCreate) {
                // restore all of views state before switching alert type
                restore();
            }
            switch (mAlertType) {
                case CONFIRM_TYPE :
                    mCancelButton.setVisibility(View.GONE);
                    break;
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


    public String getTitleText () {
        return mTitleText;
    }

    public CustomAlertDialog setTitleText (String text) {
        mTitleText = text;
        if (mTitleTextView != null && mTitleText != null) {
            mTitleTextView.setText(mTitleText);
        }
        return this;
    }

    public String getContentText () {
        return mContentText;
    }

    public CustomAlertDialog setContentText (String text) {
        mContentText = text;
        if (mContentTextView != null && mContentText != null) {
            showContentText(true);
            mContentTextView.setText(mContentText);
        }
        return this;
    }

    public boolean isShowCancelButton () {
        return mShowCancel;
    }

    public CustomAlertDialog showCancelButton (boolean isShow) {
        mShowCancel = isShow;
        if (mCancelButton != null) {
            mCancelButton.setVisibility(mShowCancel ? View.VISIBLE : View.GONE);
        }
        return this;
    }

    public boolean isShowContentText () {
        return mShowContent;
    }

    public CustomAlertDialog showContentText (boolean isShow) {
        mShowContent = isShow;
        if (mContentTextView != null) {
            mContentTextView.setVisibility(mShowContent ? View.VISIBLE : View.GONE);
        }
        return this;
    }

    public String getCancelText () {
        return mCancelText;
    }

    public CustomAlertDialog setCancelAbleDialog (boolean enable) {
        setCancelable(enable);
        return this;
    }

    public CustomAlertDialog setCancelText (String text) {
        mCancelText = text;
        if (mCancelButton != null && mCancelText != null) {
            showCancelButton(true);
            mCancelButton.setText(mCancelText);
        }
        return this;
    }

    public String getConfirmText () {
        return mConfirmText;
    }

    public CustomAlertDialog setConfirmText (String text) {
        mConfirmText = text;
        if (mConfirmButton != null && mConfirmText != null) {
            mConfirmButton.setText(mConfirmText);
        }
        return this;
    }


    public CustomAlertDialog setConfirmBtnColoer (int color) {
        mConfirmBtnColor = color;
        if (mConfirmButton != null && color > 0) {
            mConfirmButton.setBackgroundColor(ContextCompat.getColor(getContext(), color));
            mConfirmButton.invalidate();
        }
        return this;
    }

    public CustomAlertDialog setCancelBtnColoer (int color) {
        mCancelBtnColor = color;
        if (mCancelButton != null && color > 0) {
            mCancelButton.setDisableBorderColor(ContextCompat.getColor(getContext(), color));
            mCancelButton.setBorderColor(ContextCompat.getColor(getContext(), color));
            mCancelButton.setTextColor(ContextCompat.getColor(getContext(), color));
            mCancelButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
            mCancelButton.invalidate();
        }
        return this;
    }

    public CustomAlertDialog setCancelClickListener (CustomAlertDialog.OnSweetClickListener listener) {
        mCancelClickListener = listener;
        return this;
    }

    public CustomAlertDialog setConfirmClickListener (CustomAlertDialog.OnSweetClickListener listener) {
        mConfirmClickListener = listener;
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
        mConfirmButton.startAnimation(mOverlayOutAnim);
        mDialogView.startAnimation(mModalOutAnim);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.cancel_button) {
            if (mCancelClickListener != null) {
                mCancelClickListener.onClick(CustomAlertDialog.this);
            } else {
                dismissWithAnimation();
            }
        } else if (v.getId() == R.id.confirm_button) {
            if (mConfirmClickListener != null) {
                mConfirmClickListener.onClick(CustomAlertDialog.this);
            } else {
                dismissWithAnimation();
            }
        }
    }

    public CustomAlertDialog setDialogTypeLarge () {
        ViewGroup.LayoutParams params = getWindow().getAttributes();
        params.width = 800;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes((WindowManager.LayoutParams) params);
        return this;
    }

}