package com.inka.netsync.ui.presenter;

import android.util.Log;

import com.inka.netsync.controler.ContentControler;
import com.inka.netsync.data.DataManager;
import com.inka.netsync.model.ContentEntry;
import com.inka.netsync.ui.mvppresenter.DrawerMvpPresenter;
import com.inka.netsync.ui.mvpview.DrawerMvpView;

import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by birdgang on 2017. 4. 18..
 */

public class DrawerPresenter<V extends DrawerMvpView> extends BasePresenter<V> implements DrawerMvpPresenter<V> {

    private final String TAG = "DrawerPresenter";

    @Inject
    public DrawerPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public void onViewInitialized() {
    }

    @Override
    public void onUpdatePath() {
//        getListContentObservable()
//                // Run on a background thread
//                .subscribeOn(Schedulers.io())
//                // Be notified on the main thread
//                .observeOn(AndroidSchedulers.mainThread())
//                .map(new Function<List<ContentEntry>>() {
//                    @Override
//                    public ContentEntry apply(ContentEntry apiUsers) throws Exception {
//                        return Utils.convertApiUserListToUserList(apiUsers);
//                    }
//                })
//                .subscribe(getObserver());
    }

//    private Observable<List<ContentEntry>> getListContentObservable() {
//        return Observable.create(new ObservableOnSubscribe<List<ContentEntry>>() {
//            @Override
//            public void subscribe(ObservableEmitter<List<ContentEntry>> e) throws Exception {
//                if (!e.isDisposed()) {
//                    e.onNext((List<ContentEntry>) e);
//                    e.onComplete();
//                }
//            }
//        });
//    }


//    private Observable<Integer> updatePathObservable () {
//        return Observable.create(new Observable.OnSubscribe<Progress>() {
//            @Override
//            public void call(Subscriber<? super Progress> subscriber) {
//                Request request = new Request.Builder()
//                        .url(tilesUrl)
//                        .build();
//
//                try {
//                    // see http://stackoverflow.com/a/34345052/1343969
//                    Call runningRequest =  mClient.newCall(request);
//                    Response response = runningRequest.execute();
//                    long contentLength = response.body().contentLength();
//                    File destinationFile = TileCacheHelper.getZipFile(mContext, tilesUrl);
//                    Timber.d("Saving tiles zip to %s", destinationFile);
//                    BufferedSink sink = Okio.buffer(Okio.sink(destinationFile));
//
//                    long totalRead = 0;
//                    long lastRead;
//                    while ((lastRead = response.body().source().read(sink.buffer(), BUFFER_SIZE)) != -1) {
//                        if(subscriber.isUnsubscribed()){
//                            runningRequest.cancel();
//                            response.body().close();
//                            sink.close();
//                            Timber.d("Download cancelled");
//                            return;
//                        }
//                        sink.emitCompleteSegments();
//                        totalRead += lastRead;
//                        subscriber.onNext(new Progress(totalRead, contentLength, false));
//                    }
//                    sink.writeAll(response.body().source());
//                    sink.close();
//
//                    subscriber.onNext(new Progress(totalRead, contentLength, true));
//                    subscriber.onCompleted();
//                } catch (IOException e) {
//                    subscriber.onError(e);
//                }
//            }
//        }).sample(100, TimeUnit.MILLISECONDS);
//    }


    private Observable<List<ContentEntry>> getListContentObservable() {
        return Observable.fromCallable(new Callable() {
            @Override
            public Object call() throws Exception {
                List<ContentEntry> contentEntries = null;
                try {
                    contentEntries = ContentControler.getDefault().loadContentList();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return contentEntries;
            }
        });
    }



    private Observer<List<ContentEntry>> getObserver() {
        return new Observer<List<ContentEntry>>() {

            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, " onSubscribe : " + d.isDisposed());
            }

            @Override
            public void onNext(List<ContentEntry> contentEntries) {
                Log.d(TAG, " onNext : " + contentEntries.size());
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, " onError : " + e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.d(TAG, " onComplete");
            }
        };
    }


}