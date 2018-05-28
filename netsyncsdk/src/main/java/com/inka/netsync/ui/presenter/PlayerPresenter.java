package com.inka.netsync.ui.presenter;

import com.inka.netsync.data.DataManager;
import com.inka.netsync.ui.mvppresenter.PlayerMvpPresenter;
import com.inka.netsync.ui.mvpview.PlayerMvpView;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by birdgang on 2017. 4. 14..
 */

public class PlayerPresenter<V extends PlayerMvpView> extends BasePresenter<V> implements PlayerMvpPresenter<V> {

    private final String TAG = "PlayerPresenter";

    @Inject
    public PlayerPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public void onViewInitialized() {
    }

//    @Override
//    public void daoUpdateLastPlayTime(int contentId, String path, int duration, int lastPlayTime) throws Exception {
//        LogUtil.INSTANCE.info("birdgangobservable" , "daoUpdateLastPlayTime > contentId : " + contentId + " , duration : " + duration + " , lastPlayTime : " + lastPlayTime + " , path : " + path);
//
//        if (lastPlayTime < 0) {
//            return;
//        }
//
//        if (lastPlayTime >= duration) {
//            lastPlayTime = 0;
//        }
//
//        final int finalLastPlayTime = lastPlayTime;
//        getDaoUpdateLastPlayTimeObservable(contentId, path, duration, lastPlayTime)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<Boolean>() {
//                    @Override
//                    public void accept(Boolean result) throws Exception {
//                        LogUtil.INSTANCE.info("birdgangobservable" , "getDaoUpdateLastPlayTimeObservable > accept > result : " + result);
//                        getMvpView().onResultDaoForUpdateLastPlayTime(finalLastPlayTime, result);
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) throws Exception {
//                        LogUtil.INSTANCE.info("birdgangobservable" , "getDaoUpdateLastPlayTimeObservable > throwable.getMessage() : " + throwable.getMessage());
//                    }
//                });
//    }
//
//
//    @Override
//    public void daoUpdateLMSInfo(ContentEntry contentEntry) throws Exception {
//        LogUtil.INSTANCE.info("birdgangobservable" , "daoUpdateLMSInfo > contentEntry : " + contentEntry.toString());
//
//        getDaoUpdateLMSInfoObservable(contentEntry)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<Boolean>() {
//                    @Override
//                    public void accept(Boolean result) throws Exception {
//                        LogUtil.INSTANCE.info("birdgangobservable" , "getDaoUpdateLMSInfoObservable > accept > result : " + result);
//                        getMvpView().onResultDaoUpdateLMSInfo(result);
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) throws Exception {
//                        LogUtil.INSTANCE.info("birdgangobservable" , "getDaoUpdateLMSInfoObservable > throwable.getMessage() : " + throwable.getMessage());
//                    }
//                });
//
//    }
//
//
//    /**
//     *
//     * @param contentId
//     * @param path
//     * @param duration
//     * @param lastPlayTime
//     * @return
//     */
//    private Observable<Boolean> getDaoUpdateLastPlayTimeObservable(final int contentId, String path, final int duration, final int lastPlayTime) {
//        return Observable.fromCallable(new Callable<Boolean>() {
//            @Override
//            public Boolean call() throws Exception {
//                boolean success = ContentControler.getDefault().updateContentLastPlayTime(contentId, String.valueOf(lastPlayTime));
//                return success;
//            }
//        });
//    }
//
//
//    /***
//     *
//     * @param contentEntry
//     * @return
//     */
//    private Observable<Boolean> getDaoUpdateLMSInfoObservable(final ContentEntry contentEntry) {
//        return Observable.fromCallable(new Callable<Boolean>() {
//            @Override
//            public Boolean call() throws Exception {
//                int contentId = contentEntry.getContentId();
//                String name = contentEntry.getContentName();
//                String filePath = contentEntry.getContentFilePath();
//                String lastPlayTime = contentEntry.getContentLastPlayTime();
//
//                String rate = String.valueOf(LmsControler.getDefault().getLMSPercent());
//                String section = LmsControler.getDefault().getProcessedSection();
//                String rawSection = LmsControler.getDefault().getRawSection();
//                LogUtil.INSTANCE.info("birdgangloadlms", "updateInfo > lastPlayTime : " + lastPlayTime + ", rate : " + rate + " , section : " + section + " , rawSection : " + rawSection);
//
//                LmsEntry lmsEntry = new LmsEntry();
//                lmsEntry.setContentId(contentId);
//                lmsEntry.setContentName(name);
//                lmsEntry.setContentFilePath(filePath);
//                lmsEntry.setContentLastPlayTime(lastPlayTime);
//                lmsEntry.setSection(section);
//                lmsEntry.setRawSection(rawSection);
//                lmsEntry.setRate(rate);
//
//                long result = LmsControler.getDefault().updateLms(lmsEntry);
//
//                return (result > 0) ? true : false;
//            }
//        });
//    }

}
