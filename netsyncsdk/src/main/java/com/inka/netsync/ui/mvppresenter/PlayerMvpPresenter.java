package com.inka.netsync.ui.mvppresenter;

import com.inka.netsync.di.PerActivity;
import com.inka.netsync.ui.mvpview.PlayerMvpView;

/**
 * Created by birdgang on 2017. 4. 14..
 */

@PerActivity
public interface PlayerMvpPresenter<V extends PlayerMvpView> extends MvpPresenter<V> {
    void onViewInitialized();

//    /**
//     * 마지막 지산을 데이타 베이스에 저장한다.
//     * @param contentId
//     * @param path
//     * @param duration
//     * @param lastPlayTime
//     * @throws Exception
//     */
//    void daoUpdateLastPlayTime (int contentId, String path, int duration, int lastPlayTime) throws Exception;
//
//    /**
//     * LMS 정보를 데이타 베이스에 저장한다.
//     * @param contentEntry
//     * @throws Exception
//     */
//    void daoUpdateLMSInfo (ContentEntry contentEntry) throws Exception;
}