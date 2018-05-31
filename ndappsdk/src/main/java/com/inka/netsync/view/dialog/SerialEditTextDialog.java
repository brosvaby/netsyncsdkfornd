package com.inka.netsync.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.Transformation;
import android.widget.TextView;

import com.inka.netsync.BaseConfiguration;
import com.inka.netsync.R;
import com.inka.netsync.admin.ModuleConfig;
import com.inka.netsync.logs.LogUtil;
import com.inka.netsync.view.dialog.anim.OptAnimationLoader;
import com.inka.netsync.view.widget.EditTextViewSerialNumberWidget;
import com.inka.netsync.view.widget.FancyButton;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by birdgang on 2017. 11. 24..
 */

public class SerialEditTextDialog extends Dialog implements View.OnClickListener {

    private View mDialogView;
    private AnimationSet mModalOutAnim;
    private Animation mShakeInAnim;
    private Animation mOverlayOutAnim;
    private TextView mTitleTextView;
    private EditTextViewSerialNumberWidget mEditTextViewSerialNumberWidget;
    private String mTitleText;
    private String mContentText;
    private boolean mShowCancel;
    private boolean mShowContent;
    private String mCancelText;
    private String mConfirmText;

    private int mAlertType;
    private int mResourceLayout = -1;

    private int mConfirmBtnColor = -1;
    private int mCancelBtnColor = -1;

    private FancyButton mConfirmButton;
    private FancyButton mCancelButton;
    private OnSweetClickListener mCancelClickListener;
    private OnSweetClickListener mConfirmClickListener;
    private boolean mCloseFromCancel;

    public static final int NORMAL_TYPE = 0;

    public static interface OnSweetClickListener {
        public void onClick(SerialEditTextDialog sweetAlertDialog);
    }

    public SerialEditTextDialog(Context context) {
        this(context, -1, NORMAL_TYPE);
    }

    public SerialEditTextDialog(Context context, int layout, int alertType) {
        super(context, R.style.alert_dialog);
        setCancelable(true);
        setCanceledOnTouchOutside(false);
        setResourceLayout(R.layout.dialog_alert_serial_auth);
        mAlertType = alertType;

        mShakeInAnim = OptAnimationLoader.loadAnimation(getContext(), R.anim.shake);
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
                            SerialEditTextDialog.super.cancel();
                        } else {
                            SerialEditTextDialog.super.dismiss();
                        }
                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
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
        setContentView(R.layout.dialog_alert_serial_auth);

        mDialogView = getWindow().getDecorView().findViewById(android.R.id.content);
        mTitleTextView = (TextView)findViewById(R.id.title_text);

        mEditTextViewSerialNumberWidget = (EditTextViewSerialNumberWidget) findViewById(R.id.container_edittext_serial_number_widget);
        mEditTextViewSerialNumberWidget.setPrivateImeOptions("defaultInputmode=english;");
        mEditTextViewSerialNumberWidget.setPrimaryColor(BaseConfiguration.getDefault().getAppDialogBtnColor());
        mEditTextViewSerialNumberWidget.setEditTextInputFinishListener(new EditTextViewSerialNumberWidget.EditTextInputFinishListener() {
            @Override
            public void onUpdate(String values) {
                int length = StringUtils.length(values);
                if (length >= 24 || ModuleConfig.ENABLE_NOT_NEED_TO_SIRIAL_NUMBER) {
                    mConfirmButton.setEnabled(true);
                }
                else {
                    mConfirmButton.setEnabled(false);
                }
            }

            @Override
            public void onCompleate(String values) {
                LogUtil.INSTANCE.info("birdgangserialedittext", "values : " + values);
                mConfirmButton.setEnabled(true);
            }
        });

        mConfirmButton = (FancyButton)findViewById(R.id.confirm_button);

        if (ModuleConfig.ENABLE_NOT_NEED_TO_SIRIAL_NUMBER) {
            mConfirmButton.setEnabled(true);
        }

        mCancelButton = (FancyButton)findViewById(R.id.cancel_button);
        mConfirmButton.setOnClickListener(this);
        mCancelButton.setOnClickListener(this);

        setTitleText(mTitleText);
        setCancelText(mCancelText);
        setConfirmText(mConfirmText);
        setConfirmBtnColoer(mConfirmBtnColor);
        setCancelBtnColoer(mCancelBtnColor);
        changeAlertType(mAlertType, true);
        restore();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

//    private void restore () {
//        mConfirmButton.setVisibility(View.VISIBLE);
//        mConfirmButton.setBackgroundResource(R.drawable.btn_blue_background);
//    }

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

    private void changeAlertType(int alertType, boolean fromCreate) {
        mAlertType = alertType;
        if (mDialogView != null) {
            if (!fromCreate) {
                restore();
            }
        }
    }

    public SerialEditTextDialog setTitleText (String text) {
        mTitleText = text;
        if (mTitleTextView != null && mTitleText != null) {
            mTitleTextView.setText(mTitleText);
        }
        return this;
    }


    public String getContentText () {
        return mContentText;
    }

    public String getSerialToUpperNumber () {
        return mEditTextViewSerialNumberWidget.getEditMessageToUpperCaseAll();
    }

    public void setSerialNumber (String values) {
        mEditTextViewSerialNumberWidget.setEditMessage(values);
    }

    public boolean isShowCancelButton () {
        return mShowCancel;
    }

    public SerialEditTextDialog showCancelButton (boolean isShow) {
        mShowCancel = isShow;
        if (mCancelButton != null) {
            mCancelButton.setVisibility(mShowCancel ? View.VISIBLE : View.GONE);
        }
        return this;
    }

    public String getCancelText () {
        return mCancelText;
    }

    public SerialEditTextDialog setCancelText (String text) {
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

    public SerialEditTextDialog setConfirmText (String text) {
        mConfirmText = text;
        if (mConfirmButton != null && mConfirmText != null) {
            mConfirmButton.setText(mConfirmText);
        }
        return this;
    }

    public SerialEditTextDialog setConfirmBtnColoer (int color) {
        mConfirmBtnColor = color;
        if (mConfirmButton != null && color > 0) {
            mConfirmButton.setBackgroundColor(ContextCompat.getColor(getContext(), color));
            mConfirmButton.invalidate();
        }
        return this;
    }

    public SerialEditTextDialog setCancelBtnColoer (int color) {
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

    public SerialEditTextDialog setResourceLayout (int resourceLayout) {
        this.mResourceLayout = resourceLayout;
        return this;
    }

    public SerialEditTextDialog setCancelClickListener (SerialEditTextDialog.OnSweetClickListener listener) {
        mCancelClickListener = listener;
        return this;
    }

    public SerialEditTextDialog setConfirmClickListener (SerialEditTextDialog.OnSweetClickListener listener) {
        mConfirmClickListener = listener;
        return this;
    }

    protected void onStart() {
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

    public void wrongWithAnimation() {
//        mDialogView.startAnimation(mErrorXInAnim);
        mDialogView.startAnimation(mShakeInAnim);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.cancel_button) {
            if (mCancelClickListener != null) {
                mCancelClickListener.onClick(SerialEditTextDialog.this);
            } else {
                dismissWithAnimation();
            }
        } else if (v.getId() == R.id.confirm_button) {
            if (mConfirmClickListener != null) {
                mConfirmClickListener.onClick(SerialEditTextDialog.this);
            } else {
                dismissWithAnimation();
            }
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mCancelClickListener != null) {
            mCancelClickListener.onClick(SerialEditTextDialog.this);
        } else {
            dismissWithAnimation();
        }
    }


    public SerialEditTextDialog setDialogTypeLarge () {
        ViewGroup.LayoutParams params = getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes((WindowManager.LayoutParams) params);
        return this;
    }

}
