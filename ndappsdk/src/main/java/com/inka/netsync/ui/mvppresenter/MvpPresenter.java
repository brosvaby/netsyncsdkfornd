package com.inka.netsync.ui.mvppresenter;

import com.androidnetworking.error.ANError;
import com.inka.netsync.ui.mvpview.MvpView;

/**
 * Created by birdgang on 2017. 4. 14..
 */
public interface MvpPresenter<V extends MvpView> {
    void onAttach(V mvpView);
    void onDetach();
    void handleApiError(ANError error);
}