package com.inka.netsync.view.dialog;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.Transformation;
import android.widget.RadioButton;

import com.inka.netsync.R;
import com.inka.netsync.common.bus.ContentItemClickListener;
import com.inka.netsync.logs.LogUtil;
import com.inka.netsync.view.dialog.anim.OptAnimationLoader;
import com.inka.netsync.view.model.SingleCheckViewEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by birdgang on 2018. 2. 19..
 */
public class SingleChoiceAlertDialog extends AlertDialog implements View.OnClickListener {

    private final String TAG = "SingleChoiceAlertDialog";

    private View mDialogView;
    private AnimationSet mModalInAnim;
    private AnimationSet mModalOutAnim;
    private Animation mOverlayOutAnim;

    private RadioButton mRadioButtonAsc;
    private RadioButton mRadioButtonDesc;

    private String mTitleText;
    private String mConfirmText;

    private int mSelectedId;
    private int mAlertType;

    private OnSweetClickListener mConfirmClickListener;

    private boolean mCloseFromCancel;

    public static final int NORMAL_TYPE = 0;

    private List<SingleCheckViewEntry> mSingleCheckViewEntries = new ArrayList<>();

    public static interface OnSweetClickListener {
        public void onClickListContent(SingleChoiceAlertDialog sweetAlertDialog);
        public void onClick(SingleChoiceAlertDialog sweetAlertDialog);
    }

    public SingleChoiceAlertDialog(Context context) {
        this(context, -1, NORMAL_TYPE);
    }

    public SingleChoiceAlertDialog(Context context, int layout, int alertType) {
        super(context, R.style.alert_dialog);
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
                            SingleChoiceAlertDialog.super.cancel();
                        } else {
                            SingleChoiceAlertDialog.super.dismiss();
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
        setContentView(R.layout.dialog_alert_single_check);

        mDialogView = getWindow().getDecorView().findViewById(android.R.id.content);
        boolean isSelected = mSingleCheckViewEntries.get(0).isSelected();

        mRadioButtonAsc = (RadioButton) findViewById(R.id.radio_sort_asc);
        mRadioButtonAsc.setTag(mSingleCheckViewEntries.get(0).getId());
        if (isSelected) {
            mRadioButtonAsc.setTextColor(Color.parseColor("#ff000000"));
        } else {
            mRadioButtonAsc.setTextColor(Color.parseColor("#33000000"));
        }
        mRadioButtonAsc.setText(mSingleCheckViewEntries.get(0).getTitle());
        mRadioButtonAsc.setSelected(mSingleCheckViewEntries.get(0).isSelected());
        mRadioButtonAsc.setChecked(mSingleCheckViewEntries.get(0).isSelected());
        mRadioButtonAsc.setOnClickListener(mOptionClickListener);

        isSelected = mSingleCheckViewEntries.get(1).isSelected();
        mRadioButtonDesc = (RadioButton) findViewById(R.id.radio_sort_desc);
        mRadioButtonDesc.setTag(mSingleCheckViewEntries.get(1).getId());
        mRadioButtonDesc.setText(mSingleCheckViewEntries.get(1).getTitle());
        if (isSelected) {
            mRadioButtonDesc.setTextColor(Color.parseColor("#ff000000"));
        } else {
            mRadioButtonDesc.setTextColor(Color.parseColor("#33000000"));
        }
        mRadioButtonDesc.setSelected(mSingleCheckViewEntries.get(1).isSelected());
        mRadioButtonDesc.setChecked(mSingleCheckViewEntries.get(1).isSelected());
        mRadioButtonDesc.setOnClickListener(mOptionClickListener);


        setTitleText(mTitleText);
        setConfirmText(mConfirmText);
        changeAlertType(mAlertType, true);
    }

    RadioButton.OnClickListener mOptionClickListener = new RadioButton.OnClickListener() {
        @Override
        public void onClick(View view) {
            LogUtil.INSTANCE.info(TAG , "Option 1 : " + mRadioButtonAsc.isChecked() + " , Option 2 : " + mRadioButtonDesc.isChecked());
            try {
                int id = (int) view.getTag();
                mSelectedId = id;

                LogUtil.INSTANCE.info(TAG,  "id : " + id);

                if (mConfirmClickListener != null) {
                    mConfirmClickListener.onClickListContent(SingleChoiceAlertDialog.this);
                } else {
                    dismissWithAnimation();
                }

            } catch (Exception e) {
                LogUtil.INSTANCE.error(getClass().getSimpleName(), e);
            }
        }
    };


    private void restore () {
//        mConfirmButton.setVisibility(View.VISIBLE);
//        mConfirmButton.setBackgroundResource(R.drawable.blue_button_background);
    }


    private void changeAlertType(int alertType, boolean fromCreate) {
        mAlertType = alertType;
        // call after created views
        if (mDialogView != null) {
            if (!fromCreate) {
                restore();
            }
        }
    }

    public SingleChoiceAlertDialog setTitleText (String text) {
        mTitleText = text;
        return this;
    }

    public int getSelectedId() {
        return mSelectedId;
    }

    public void setSelectedId(int selectedId) {
        this.mSelectedId = selectedId;
    }

    public String getConfirmText () {
        return mConfirmText;
    }

    public SingleChoiceAlertDialog setConfirmText (String text) {
        mConfirmText = text;
//        if (mConfirmButton != null && mConfirmText != null) {
//            mConfirmButton.setText(mConfirmText);
//        }
        return this;
    }

    public SingleChoiceAlertDialog setConfirmClickListener (OnSweetClickListener listener) {
        mConfirmClickListener = listener;
        return this;
    }

    public SingleChoiceAlertDialog setDataSource (List<SingleCheckViewEntry> settingSelectViewEntries) {
        this.mSingleCheckViewEntries = settingSelectViewEntries;
        for (SingleCheckViewEntry viewEntry : mSingleCheckViewEntries) {
            if (viewEntry.isSelected()) {
                setSelectedId(viewEntry.getId());
            }
        }
        return this;
    }

    public SingleChoiceAlertDialog setCanceled (boolean value) {
        setCanceledOnTouchOutside(value);
        return this;
    }

    protected void onStart() {
        mDialogView.startAnimation(mModalInAnim);
    }

    /**
     * The real Dialog.cancel() will be invoked async-ly after the animation finishes.
     */
    @Override
    public void cancel() {
        dismissWithAnimation(true);
    }

    /**
     * The real Dialog.dismiss() will be invoked async-ly after the animation finishes.
     */
    public void dismissWithAnimation() {
        dismissWithAnimation(false);
    }

    private void dismissWithAnimation(boolean fromCancel) {
        mCloseFromCancel = fromCancel;
//        mConfirmButton.startAnimation(mOverlayOutAnim);
        mDialogView.startAnimation(mModalOutAnim);
    }


    public ContentItemClickListener mContentItemClickListener = new ContentItemClickListener() {
        @Override
        public void onItemCategoryClick(View view) {
        }

        @Override
        public void onItemClick(View view) {
            try {
                int id = (int) view.getTag();
                mSelectedId = id;

                LogUtil.INSTANCE.info(TAG,  "id : " + id);

                if (mConfirmClickListener != null) {
                    mConfirmClickListener.onClickListContent(SingleChoiceAlertDialog.this);
                } else {
                    dismissWithAnimation();
                }

            } catch (Exception e) {
                LogUtil.INSTANCE.error(getClass().getSimpleName(), e);
            }
        }
    };


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.confirm_button) {
            if (mConfirmClickListener != null) {
                mConfirmClickListener.onClick(SingleChoiceAlertDialog.this);
            } else {
                dismissWithAnimation();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        dismissWithAnimation();
    }

}
