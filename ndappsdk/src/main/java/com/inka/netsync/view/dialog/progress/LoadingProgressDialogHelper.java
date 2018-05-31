package com.inka.netsync.view.dialog.progress;

import com.inka.netsync.BaseApplication;
import com.inka.netsync.R;
import com.inka.netsync.logs.LogUtil;

/**
 * Created by birdgang on 2018. 4. 26..
 */
public class LoadingProgressDialogHelper {

    private static volatile LoadingProgressDialogHelper defaultInstance;

    private LoadingProgressDialog mLoadingProgressDialog;

    protected boolean hasProgress = false;

    public static LoadingProgressDialogHelper getDefault() {
        if (defaultInstance == null) {
            synchronized (LoadingProgressDialogHelper.class) {
                if (defaultInstance == null) {
                    defaultInstance = new LoadingProgressDialogHelper();
                }
            }
        }
        return defaultInstance;
    }

    private LoadingProgressDialogHelper () {
    }

    public void cancel() {
        if (hasProgress) {
            if (null != mLoadingProgressDialog && mLoadingProgressDialog.isShowing()) {
                mLoadingProgressDialog.cancel();
                mLoadingProgressDialog.dismiss();
            }
        }
        hasProgress = false;
    }

    public void show() {
        mLoadingProgressDialog = new LoadingProgressDialog(BaseApplication.getContext(), R.style.TransparentDialog);
        mLoadingProgressDialog.setCanceledOnTouchOutside(false);
        mLoadingProgressDialog.show();
    }

    public void hide() {
        try {
            if (hasProgress) {
                if (null != mLoadingProgressDialog) {
                    mLoadingProgressDialog.dismiss();
                }
            }
        } catch (Exception e) {
            LogUtil.INSTANCE.error(getClass().getSimpleName(), e);
        }
    }

}
