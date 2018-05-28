package com.inka.netsync.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.inka.netsync.R;
import com.inka.netsync.logs.LogUtil;

/**
 * Created by birdgang on 2018. 2. 7..
 */

public class RecyclerHasEmptyView extends RelativeLayout {

    private Context mContext;

    private RecyclerView mRecyclerView;
    private LinearLayout mInputView;
    private LinearLayout mEmptyView;
    private LinearLayout mRetryView;
    private ProgressBar mProgressBar;

    private ImageView mImgEmpth;
    private TextView mTextEmpth;

    private OnRetryClick mOnRetryClick;

    public RecyclerHasEmptyView(Context context) {
        this(context, null);
    }

    public RecyclerHasEmptyView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecyclerHasEmptyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
    }


    private void init () {
        LogUtil.INSTANCE.info("RecyclerHasEmptyView", "RecyclerHasEmptyView > init");
        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.view_recycler_has_empty_view, null);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_list);
        mInputView = (LinearLayout) view.findViewById(R.id.layout_input);
        mEmptyView = (LinearLayout) view.findViewById(R.id.layout_empty);
        mRetryView = (LinearLayout) view.findViewById(R.id.layout_retry);
        mImgEmpth = (ImageView) view.findViewById(R.id.img_empth);
        mTextEmpth = (TextView) view.findViewById(R.id.text_empth);

        mRetryView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mRetryView.setVisibility(View.GONE);
                mOnRetryClick.onRetry();
            }
        });
        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar);

        addView(view);
        input();
    }

    public void input () {
        LogUtil.INSTANCE.info("RecyclerHasEmptyView", "RecyclerHasEmptyView > input");
        mInputView.setVisibility(View.VISIBLE);
        mRetryView.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);
    }

    public void loading() {
        LogUtil.INSTANCE.info("RecyclerHasEmptyView", "RecyclerHasEmptyView > loading");
        mInputView.setVisibility(View.GONE);
        mRetryView.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    public void empty() {
        LogUtil.INSTANCE.info("RecyclerHasEmptyView", "RecyclerHasEmptyView > empty");
        mInputView.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.VISIBLE);
        mRetryView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);
    }

    public void retry() {
        mInputView.setVisibility(View.GONE);
        mRetryView.setVisibility(View.VISIBLE);
        mEmptyView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);
    }

    public void success() {
        LogUtil.INSTANCE.info("RecyclerHasEmptyView", "RecyclerHasEmptyView > success");
        mInputView.setVisibility(View.GONE);
        mRetryView.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public void setImgEmpthVisisble(int visisble) {
        this.mImgEmpth.setVisibility(visisble);
    }

    public void setTextEmpth(String message) {
        this.mTextEmpth.setText(message);
    }

    public void setOnRetryClick(OnRetryClick onRetryClick) {
        mOnRetryClick = onRetryClick;
    }


    public void scrollToPosition (int position) {
        mRecyclerView.scrollToPosition(position);
    }

    public interface OnRetryClick {
        void onRetry();
    }

}