//package com.inka.netsync.async;
//
//import android.app.Activity;
//import android.os.AsyncTask;
//
//import com.inka.netsync.R;
//import com.inka.netsync.common.bus.BaseMessageListener;
//import com.inka.netsync.data.network.model.BaseResponseEntry;
//import com.inka.netsync.logs.LogUtil;
//import com.inka.netsync.view.dialog.progress.LoadingProgressDialog;
//
///**
// * Created by birdgang on 2018. 4. 25..
// */
//
//public abstract class BaseAsyncTask extends AsyncTask<String, Void, BaseResponseEntry> {
//
//    public final static int ACT_PLAYER = 1000;
//
//    protected Activity context = null;
//    protected BaseMessageListener baseMessageListener;
//
//    protected Class<?> mPlayerActivity = null;
//
//    protected boolean hasProgress = false;
//    protected boolean disable = false;
//
//    private LoadingProgressDialog mLoadingProgressDialog;
//
//    protected BaseAsyncTask(Activity context, BaseMessageListener baseMessageListener, boolean hasProgress) {
//        this.context = context;
//        this.baseMessageListener = baseMessageListener;
//        this.hasProgress = false;
//        mLoadingProgressDialog = new LoadingProgressDialog(context, R.style.TransparentDialog);
//        mLoadingProgressDialog.setCanceledOnTouchOutside(false);
//    }
//
//    @Override
//    protected void onCancelled() {
//        super.onCancelled();
//        if (hasProgress) {
//            if (null != mLoadingProgressDialog && mLoadingProgressDialog.isShowing()) {
//                mLoadingProgressDialog.dismiss();
//            }
//        }
//        hasProgress = false;
//    }
//
//    @Override
//    protected void onPreExecute() {
//        super.onPreExecute();
//        disable = true;
//        if (hasProgress) {
//            if (null != mLoadingProgressDialog) {
//                mLoadingProgressDialog.show();
//            }
//        }
//    }
//
//    @Override
//    protected void onPostExecute(BaseResponseEntry result) {
//        super.onPostExecute(result);
//        try {
//            if (hasProgress) {
//                if (null != mLoadingProgressDialog) {
//                    mLoadingProgressDialog.dismiss();
//                }
//            }
//            if (null != baseMessageListener && null != result) {
//                baseMessageListener.onResult(result);
//            }
//            disable = false;
//        } catch (Exception e) {
//            LogUtil.INSTANCE.error(getClass().getSimpleName(), e);
//        }
//    }
//
//}