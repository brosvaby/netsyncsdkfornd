package com.inka.netsync.view;

import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.inka.netsync.R;
import com.inka.netsync.logs.LogUtil;

public class ProgressManagerClient {

    private final String TAG = ProgressManagerClient.class.toString();

    private static ProgressManagerClient mInstance;
    private static boolean mIsShowing = false;
    private FragmentActivity mActivity = null;
    private final Handler mHandler = new Handler(Looper.getMainLooper());

    public static ProgressManagerClient getInstance() {
        if( mInstance == null ) {
            mInstance = new ProgressManagerClient();
        }
        return mInstance;
    }

    private final Runnable mPendingProgress = new Runnable() {
        @Override
        public void run() {
            try {
                FragmentManager fragmentManager = mActivity.getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                Fragment previous = fragmentManager.findFragmentByTag(mActivity.getString(R.string.tag_progress_fragment));
                if (previous != null) {
                    transaction.remove(previous);
                    transaction.commitAllowingStateLoss();
                }
            } catch (Exception e) {
                LogUtil.INSTANCE.error(TAG, e);
                mHandler.postDelayed(mPendingProgress, 5000);
            }
        }
    };

    public ProgressManagerClient() {
    }

    public void showProgress(FragmentActivity activity, String showMessage) {
        LogUtil.INSTANCE.info(TAG, "showProgress > showMessage : " + showMessage + " , activity : " + activity.getLocalClassName() + " , mIsShowing : " + mIsShowing);
        if (mIsShowing) {
            return;
        }

        mActivity = activity;
        mIsShowing = true;

        try {
            FragmentManager fragmentManager = mActivity.getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            ProgressFragment pfragment = ProgressFragment.newInstance(showMessage);
            transaction.add(R.id.progresscontent, pfragment, mActivity.getString(R.string.tag_progress_fragment));
            transaction.commitAllowingStateLoss();
        } catch (Exception e) {
            mIsShowing = false;
        }
    }

    public void stopProgress(FragmentActivity activity) {
        LogUtil.INSTANCE.info(TAG, "stopProgress");
        mActivity = activity;
        mIsShowing = false;

        mHandler.postDelayed(mPendingProgress, 0);
    }

    public boolean isShowProgress() {
        boolean result = false;
        try {
            FragmentManager fragmentManager = mActivity.getSupportFragmentManager();
            Fragment previous = fragmentManager.findFragmentByTag(mActivity.getString(R.string.tag_progress_fragment));
            if (previous != null) {
                result = true;
            } else {
                result = false;
            }
        } catch (Exception e) {
            result = false;
        }

        LogUtil.INSTANCE.info(TAG, "isShowProgress > result : " + result);

        return result;
    }

    public void setText(String message) {
        try {
            FragmentManager fragmentManager = mActivity.getSupportFragmentManager();
            Fragment previous = fragmentManager.findFragmentByTag(mActivity.getString(R.string.tag_progress_fragment));

            if (previous != null) {
                ((ProgressFragment)previous).setText(message);
            }
        } catch(Exception e) {
        }
    }

    public void setSize(float size) {
        try {
            FragmentManager 	fragmentManager = mActivity.getSupportFragmentManager();
            Fragment 			previous 		= fragmentManager.findFragmentByTag(mActivity.getString(R.string.tag_progress_fragment));

            if (previous != null) {
                ((ProgressFragment)previous).setSize(size);
            }
        } catch(Exception e) {
        }
    }

    public void setBackgroundColor(int color) {
        try {
            FragmentManager 	fragmentManager = mActivity.getSupportFragmentManager();
            Fragment 			previous 		= fragmentManager.findFragmentByTag(mActivity.getString(R.string.tag_progress_fragment));

            if (previous != null) {
                ((ProgressFragment)previous).setBackgroundColor(color);
            }
        } catch(Exception e) {
        }
    }

    public void setTextColor(int color) {
        try {
            FragmentManager 	fragmentManager = mActivity.getSupportFragmentManager();
            Fragment 			previous 		= fragmentManager.findFragmentByTag(mActivity.getString(R.string.tag_progress_fragment));

            if (previous != null) {
                ((ProgressFragment)previous).setTextColor(color);
            }
        } catch(Exception e) {
        }
    }
}
