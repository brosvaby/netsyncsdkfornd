package com.inka.netsync.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.inka.netsync.R;
import com.inka.netsync.common.bus.ContentItemClickListener;
import com.inka.netsync.common.bus.ContentItemLongClickListener;
import com.inka.netsync.logs.LogUtil;
import com.inka.netsync.view.adapter.ListBookMarkAdapter;
import com.inka.netsync.view.dialog.anim.OptAnimationLoader;
import com.inka.netsync.view.meterial.DividerItemDecoration;
import com.inka.netsync.view.model.BookMarkViewEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by birdgang on 2017. 11. 24..
 */

public class ListBookMarkAlertDialog extends Dialog implements View.OnClickListener {

    private final String TAG = "ListBookMarkAlertDialog";

    private View mDialogView;
    private AnimationSet mModalInAnim;
    private AnimationSet mModalOutAnim;
    private Animation mOverlayOutAnim;
    private TextView mTitleTextView;
    private TextView mEditTextView;
    private RecyclerView mRecyclerView = null;

    private String mTitleText;
    private String mConfirmText;

    private int mBookmarkPosition;
    private int mAlertType;

    private Button mConfirmButton;
    private Button mCancelButton;

    private OnSweetClickListener mConfirmClickListener;
    private OnDialogDismissListener mDialogDismissListener;

    private boolean mCloseFromCancel;

    public static final int NORMAL_TYPE = 0;

    private List<BookMarkViewEntry> mBookmarkEntries = new ArrayList<>();

    private ListBookMarkAdapter mListBookmarkAdapter = null;

    public static interface OnSweetClickListener {
        public void onClickListContent(ListBookMarkAlertDialog sweetAlertDialog);
        public void onClick(ListBookMarkAlertDialog sweetAlertDialog);
    }

    public interface OnDialogDismissListener {
        void onDissmiss();
    }

    public ListBookMarkAlertDialog(Context context) {
        this(context, -1, NORMAL_TYPE);
    }

    public ListBookMarkAlertDialog(Context context, int layout, int alertType) {
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
                            ListBookMarkAlertDialog.super.cancel();
                        } else {
                            ListBookMarkAlertDialog.super.dismiss();
                        }
                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

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
        setContentView(R.layout.dialog_list_bookmark_in_player);

        mDialogView = getWindow().getDecorView().findViewById(android.R.id.content);
        mTitleTextView = (TextView)findViewById(R.id.title_text);
        mEditTextView = (TextView) findViewById(R.id.title_edit_text);
        mEditTextView.setOnClickListener(mEditBookmarkListener);

        mConfirmButton = (Button)findViewById(R.id.confirm_button);
        mConfirmButton.setOnClickListener(this);

        mCancelButton = (Button) findViewById(R.id.cancel_button);
        mCancelButton.setOnClickListener(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_list);

        setRecyclerView(mRecyclerView);
        setTitleText(mTitleText);
        setConfirmText(mConfirmText);
        changeAlertType(mAlertType, true);
    }


    public List<BookMarkViewEntry> getSelectedForDeleteEntries () {
        return mListBookmarkAdapter.getSelectedForDeleteEntries();
    }


    private void restore () {
        mConfirmButton.setVisibility(View.VISIBLE);
        mConfirmButton.setBackgroundResource(R.drawable.btn_blue_background);
    }


    private View.OnClickListener mEditBookmarkListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (null == mListBookmarkAdapter) {
                return;
            }

            try {
                if (null != mListBookmarkAdapter) {
                    mListBookmarkAdapter.changeEditMode();
                }
                fillContentButton();
            } catch (Exception e) {
                LogUtil.INSTANCE.error(TAG, e);
            }
        }
    };


    private void fillContentButton () {
        if (null == mListBookmarkAdapter) {
            return;
        }

        int mode = mListBookmarkAdapter.currentEditMode();
        if (mListBookmarkAdapter.EDIT == mode) {
            mConfirmButton.setText(getContext().getResources().getString(R.string.dialog_remove_selected_bookmark));
            mCancelButton.setVisibility(View.VISIBLE);
            mEditTextView.setVisibility(View.GONE);
        }
        else {
            mConfirmButton.setText(getContext().getResources().getString(R.string.dialog_ok));
            mCancelButton.setVisibility(View.GONE);
            mEditTextView.setVisibility(View.VISIBLE);
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

    public ListBookMarkAlertDialog setRecyclerView (RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));
        mListBookmarkAdapter = new ListBookMarkAdapter(getContext(), mBookmarkEntries, mContentItemClickListener, mContentItemLongClickListener);
        recyclerView.setAdapter(mListBookmarkAdapter);
        recyclerView.setHasFixedSize(true);
        return this;
    }


    public int getmBookmarkPosition () {
        return mBookmarkPosition;
    }

    public ListBookMarkAlertDialog setTitleText (String text) {
        mTitleText = text;
        if (mTitleTextView != null && mTitleText != null) {
            mTitleTextView.setText(mTitleText);
        }
        return this;
    }

    public String getConfirmText () {
        return mConfirmText;
    }

    public ListBookMarkAlertDialog setConfirmText (String text) {
        mConfirmText = text;
        if (mConfirmButton != null && mConfirmText != null) {
            mConfirmButton.setText(mConfirmText);
        }
        return this;
    }

    public ListBookMarkAlertDialog setConfirmClickListener (ListBookMarkAlertDialog.OnSweetClickListener listener) {
        mConfirmClickListener = listener;
        return this;
    }

    public ListBookMarkAlertDialog setDismissListener (ListBookMarkAlertDialog.OnDialogDismissListener listener) {
        mDialogDismissListener = listener;
        return this;
    }

    public ListBookMarkAlertDialog setDataSource (List<BookMarkViewEntry> bookmarkEntries) {
        this.mBookmarkEntries = bookmarkEntries;
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
        if (null != mDialogDismissListener) {
            mDialogDismissListener.onDissmiss();
        }
        dismissWithAnimation(false);
    }

    private void dismissWithAnimation(boolean fromCancel) {
        mCloseFromCancel = fromCancel;
        mConfirmButton.startAnimation(mOverlayOutAnim);
        mDialogView.startAnimation(mModalOutAnim);
    }


    public ContentItemClickListener mContentItemClickListener = new ContentItemClickListener() {
        @Override
        public void onItemCategoryClick(View view) {
        }

        @Override
        public void onItemClick(View view) {
            try {
                BookMarkViewEntry selectedEntry = (BookMarkViewEntry) view.getTag();
                if (null == selectedEntry) {
                    return;
                }

                int mode = mListBookmarkAdapter.currentEditMode();
                if (mListBookmarkAdapter.EDIT == mode) {
                    selectedEntry.setChecked(!selectedEntry.isChecked());
                    mListBookmarkAdapter.addForDeleteEntry(selectedEntry);
                }
                else {
                    mBookmarkPosition = Integer.valueOf(selectedEntry.getBookmarkLocation());
                    if (mConfirmClickListener != null) {
                        mConfirmClickListener.onClickListContent(ListBookMarkAlertDialog.this);
                    } else {
                        dismissWithAnimation();
                    }
                }

                LogUtil.INSTANCE.info(TAG,  "mBookmarkPosition : " + mBookmarkPosition);
            } catch (Exception e) {
                LogUtil.INSTANCE.error(getClass().getSimpleName(), e);
            }
        }
    };


    public ContentItemLongClickListener mContentItemLongClickListener = new ContentItemLongClickListener() {
        @Override
        public void onItemLongClick(View view) {
            if (null == mListBookmarkAdapter) {
                return;
            }

            try {
                mListBookmarkAdapter.changeEditMode();

                fillContentButton();
            } catch (Exception e) {
                LogUtil.INSTANCE.error(TAG, e);
            }
        }
    };


    @Override
    public void onClick(View v) {
        int id = v.getId();
        try {
            if (id == R.id.confirm_button) {
                if (null == mListBookmarkAdapter) {
                    return;
                }

                int mode = mListBookmarkAdapter.currentEditMode();
                LogUtil.INSTANCE.info("birdgangbookmark" , "onClick > edit mode : " + mode);
                if (mListBookmarkAdapter.EDIT == mode) {
                    List<BookMarkViewEntry> entries = mListBookmarkAdapter.getSelectedForDeleteEntries();
                    LogUtil.INSTANCE.info("birdgangbookmark" , "onClick > entries size : " + entries.size());

                    if (null == entries || entries.size() <= 0) {
                        Toast.makeText(getContext(), getContext().getResources().getString(R.string.message_for_toast_select_item_for_delete), Toast.LENGTH_SHORT).show();
                    } else {
                        if (mConfirmClickListener != null) {
                            mConfirmClickListener.onClick(ListBookMarkAlertDialog.this);
                        } else {
                            dismissWithAnimation();
                        }
                    }
                }
                else {
                    dismissWithAnimation();
                }
            }
            else if (id == R.id.cancel_button) {
                mListBookmarkAdapter.changeEditModeCancel();
                fillContentButton();
            }
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        dismissWithAnimation();
    }

}
