package com.inka.netsync.ui.mvpview;

import android.app.Activity;

/**
 * Created by birdgang on 2017. 4. 14..
 */

public interface MvpView {
    void showProgress (Activity activity, int message);
    void hideProgress (Activity activity);
    boolean isNetworkConnected();
}