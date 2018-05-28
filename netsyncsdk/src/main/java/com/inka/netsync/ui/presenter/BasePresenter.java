package com.inka.netsync.ui.presenter;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

import com.androidnetworking.error.ANError;
import com.inka.netsync.data.DataManager;
import com.inka.netsync.logs.LogUtil;
import com.inka.netsync.ui.mvppresenter.MvpPresenter;
import com.inka.netsync.ui.mvpview.MvpView;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import okhttp3.Response;

/**
 * Created by birdgang on 2017. 4. 14..
 */

public class BasePresenter<V extends MvpView> implements MvpPresenter<V> {

    private static final String TAG = "BasePresenter";

    private final DataManager mDataManager;

    private final CompositeDisposable mCompositeDisposable;

    private V mMvpView;

    @Inject
    public BasePresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        this.mDataManager = dataManager;
        this.mCompositeDisposable = compositeDisposable;
    }

    @Override
    public void onAttach(V mvpView) {
        mMvpView = mvpView;
    }

    @Override
    public void onDetach() {
        mCompositeDisposable.dispose();
        mMvpView = null;
    }

    public boolean isViewAttached() {
        return mMvpView != null;
    }

    public V getMvpView() {
        return mMvpView;
    }

    public void checkViewAttached() {
        if (!isViewAttached()) throw new MvpViewNotAttachedException();
    }

    public DataManager getDataManager() {
        return mDataManager;
    }

    public CompositeDisposable getCompositeDisposable() {
        return mCompositeDisposable;
    }


    @Override
    public void handleApiError(ANError error) {
        if (null == error) {
            return;
        }

        try {
            LogUtil.INSTANCE.info("AppApiHelper", "error.getErrorBody() :" + error.getErrorBody());
//            mMvpView.onError(error.getErrorBody());

            Response response = error.getResponse();
            if (null != response) {
                LogUtil.INSTANCE.info("birdgangnetworkerror", "response.request().toString() :" + response.request().toString());
            }
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
    }

    public static class MvpViewNotAttachedException extends RuntimeException {
        public MvpViewNotAttachedException() {
            super("Please call Presenter.onAttach(MvpView) before" + " requesting data to the Presenter");
        }
    }

    public boolean checkExternalStoragePermission() {
        if (ActivityCompat.checkSelfPermission(getDataManager().getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }
}
