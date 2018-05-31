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
import android.widget.TextView;

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
public class SingleChoiceWithDescriptionAlertDialog extends AlertDialog implements View.OnClickListener {

    private final String TAG = "SingleChoiceWithDescriptionAlertDialog";

    private View mDialogView;
    private AnimationSet mModalInAnim;
    private AnimationSet mModalOutAnim;
    private Animation mOverlayOutAnim;

    private RadioButton mRadioButtonItem1;
    private RadioButton mRadioButtonItem2;

    private TextView mTitleTextView;
    private TextView mDescriptionTextView;

    private String mTitleText;
    private String mDescriptionText;
    private String mConfirmText;

    private int mSelectedId;
    private int mAlertType;

    private OnSweetClickListener mConfirmClickListener;

    private boolean mCloseFromCancel;

    public static final int NORMAL_TYPE = 0;

    private List<SingleCheckViewEntry> mSingleCheckViewEntries = new ArrayList<>();

    public static interface OnSweetClickListener {
        public void onClickListContent(SingleChoiceWithDescriptionAlertDialog sweetAlertDialog);
        public void onClick(SingleChoiceWithDescriptionAlertDialog sweetAlertDialog);
    }

    public SingleChoiceWithDescriptionAlertDialog(Context context) {
        this(context, -1, NORMAL_TYPE);
    }

    public SingleChoiceWithDescriptionAlertDialog(Context context, int layout, int alertType) {
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
                            SingleChoiceWithDescriptionAlertDialog.super.cancel();
                        } else {
                            SingleChoiceWithDescriptionAlertDialog.super.dismiss();
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
        setContentView(R.layout.dialog_alert_single_check_with_description);

        mDialogView = getWindow().getDecorView().findViewById(android.R.id.content);
        boolean isSelected = mSingleCheckViewEntries.get(0).isSelected();

        mTitleTextView = (TextView) findViewById(R.id.title_text);
        mDescriptionTextView = (TextView) findViewById(R.id.text_setting_check_description);

        mRadioButtonItem1 = (RadioButton) findViewById(R.id.radio_item1);
        mRadioButtonItem1.setTag(mSingleCheckViewEntries.get(0).getId());
        if (isSelected) {
            mRadioButtonItem1.setTextColor(Color.parseColor("#ff000000"));
        } else {
            mRadioButtonItem1.setTextColor(Color.parseColor("#33000000"));
        }
        mRadioButtonItem1.setText(mSingleCheckViewEntries.get(0).getTitle());
        mRadioButtonItem1.setSelected(mSingleCheckViewEntries.get(0).isSelected());
        mRadioButtonItem1.setChecked(mSingleCheckViewEntries.get(0).isSelected());
        mRadioButtonItem1.setOnClickListener(mOptionClickListener);

        isSelected = mSingleCheckViewEntries.get(1).isSelected();
        mRadioButtonItem2 = (RadioButton) findViewById(R.id.radio_item2);
        mRadioButtonItem2.setTag(mSingleCheckViewEntries.get(1).getId());
        mRadioButtonItem2.setText(mSingleCheckViewEntries.get(1).getTitle());
        if (isSelected) {
            mRadioButtonItem2.setTextColor(Color.parseColor("#ff000000"));
        } else {
            mRadioButtonItem2.setTextColor(Color.parseColor("#33000000"));
        }
        mRadioButtonItem2.setSelected(mSingleCheckViewEntries.get(1).isSelected());
        mRadioButtonItem2.setChecked(mSingleCheckViewEntries.get(1).isSelected());
        mRadioButtonItem2.setOnClickListener(mOptionClickListener);

        setTitleText(mTitleText);
        setDescriptionText(mDescriptionText);
        setConfirmText(mConfirmText);
        changeAlertType(mAlertType, true);
    }

    RadioButton.OnClickListener mOptionClickListener = new RadioButton.OnClickListener() {
        @Override
        public void onClick(View view) {
            try {
                int id = (int) view.getTag();
                mSelectedId = id;

                LogUtil.INSTANCE.info(TAG,  "id : " + id);

                if (mConfirmClickListener != null) {
                    mConfirmClickListener.onClickListContent(SingleChoiceWithDescriptionAlertDialog.this);
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


    public SingleChoiceWithDescriptionAlertDialog setTitleText (String text) {
        mTitleText = text;
        if (mTitleTextView != null && text != null) {
            mTitleTextView.setText(text);
        }

        return this;
    }

    public SingleChoiceWithDescriptionAlertDialog setDescriptionText (String text) {
        mDescriptionText = text;
        if (mDescriptionTextView != null && text != null) {
            mDescriptionTextView.setText(text);
        }
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

    public SingleChoiceWithDescriptionAlertDialog setConfirmText (String text) {
        mConfirmText = text;
//        if (mConfirmButton != null && mConfirmText != null) {
//            mConfirmButton.setText(mConfirmText);
//        }
        return this;
    }

    public SingleChoiceWithDescriptionAlertDialog setConfirmClickListener (OnSweetClickListener listener) {
        mConfirmClickListener = listener;
        return this;
    }

    public SingleChoiceWithDescriptionAlertDialog setDataSource (List<SingleCheckViewEntry> settingSelectViewEntries) {
        this.mSingleCheckViewEntries = settingSelectViewEntries;
        for (SingleCheckViewEntry viewEntry : mSingleCheckViewEntries) {
            if (viewEntry.isSelected()) {
                setSelectedId(viewEntry.getId());
            }
        }
        return this;
    }

    public SingleChoiceWithDescriptionAlertDialog setCanceled (boolean value) {
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
                    mConfirmClickListener.onClickListContent(SingleChoiceWithDescriptionAlertDialog.this);
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
                mConfirmClickListener.onClick(SingleChoiceWithDescriptionAlertDialog.this);
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
