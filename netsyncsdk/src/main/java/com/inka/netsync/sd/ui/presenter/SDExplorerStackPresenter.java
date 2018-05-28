package com.inka.netsync.sd.ui.presenter;

import com.inka.ncg2.StringEncrypter;
import com.inka.netsync.data.DataManager;
import com.inka.netsync.data.cache.db.model.ContentCacheEntry;
import com.inka.netsync.data.cache.pref.PreferencesCacheHelper;
import com.inka.netsync.lms.LmsControler;
import com.inka.netsync.lms.model.LmsEntry;
import com.inka.netsync.logs.LogUtil;
import com.inka.netsync.media.MediaStorage;
import com.inka.netsync.model.ContentEntry;
import com.inka.netsync.model.ExplorerStackEntry;
import com.inka.netsync.model.ExplorerStackEntryList;
import com.inka.netsync.model.ExplorerStackGenerate;
import com.inka.netsync.sd.ui.mvppresenter.SDExplorerStackMvpPresenter;
import com.inka.netsync.sd.ui.mvpview.SDExplorerStackMvpView;
import com.inka.netsync.view.model.ContentViewEntry;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by birdgang on 2018. 5. 8..
 */

public class SDExplorerStackPresenter<V extends SDExplorerStackMvpView> extends SDExplorerPresenter<V> implements SDExplorerStackMvpPresenter<V> {

    private final String TAG = "ExplorerPresenter";

    StringEncrypter mEncrypter = null;

    @Inject
    public SDExplorerStackPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public void onViewInitialized() {
    }

    @Override
    public void generateExplorerStack(final Map<String, List<ContentCacheEntry>> contentCacheMemorAwareHashMap, final Map<String, ContentCacheEntry> contentCacheDiskAwareMaps, final String rootType) {

        getExplorerStackObservable(contentCacheMemorAwareHashMap, contentCacheDiskAwareMaps, rootType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<ExplorerStackEntry>>() {
                    @Override
                    public void accept(List<ExplorerStackEntry> explorerStackEntries) throws Exception {
                        LogUtil.INSTANCE.info("birdgangobservable" , "accept > explorerStackEntries.size() : " + explorerStackEntries.size());
                        getMvpView().onLoadExplorerStackEntries(explorerStackEntries);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        LogUtil.INSTANCE.info("birdgangobservable" , "throwable.getMessage() : " + throwable.getMessage());
                    }
                });

    }

    @Override
    public void updateListContent(ExplorerStackEntryList explorerStackEntryList, String lastSelectedContentPath) {
        List<ContentEntry> contentEntries = new ArrayList<>();
        try {
            List<ContentCacheEntry> contentCacheEntries = explorerStackEntryList.extractListContent(lastSelectedContentPath);
            for (ContentCacheEntry contentCacheEntry : contentCacheEntries) {
                ContentEntry contentEntry = new ContentEntry(contentCacheEntry);
                contentEntries.add(contentEntry);
            }

            LogUtil.INSTANCE.info("birdgangexplorernavigator" , "contentEntries size : " + contentEntries.size());

            getMvpView().onUpdateContent(contentEntries);
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
    }


    @Override
    public void updateListContentForLMS(List<ContentViewEntry> contentEntries) {
        for (ContentViewEntry contentViewEntry : contentEntries) {
            String filePath = contentViewEntry.getContentFilePath();
            LmsEntry lmsEntry = LmsControler.getDefault().findLmsCacheEntryByPath(filePath);
            if (null != lmsEntry) {
                contentViewEntry.setLmsRate(lmsEntry.getRate());
            }
        }
        getMvpView().onUpdateContentForLMS(contentEntries);
    }


    @Override
    public void sortListContent(List<ContentViewEntry> contentViewEntries) {
        try {
            int conditionSort = PreferencesCacheHelper.getPreferenceValueForInteger(PreferencesCacheHelper.SORT_NAME, PreferencesCacheHelper.SORT_NAME_ASC);

            List<ContentViewEntry> sortDirectories = new ArrayList<>();
            List<ContentViewEntry> sortVideos = new ArrayList<>();
            List<ContentViewEntry> sortAudios = new ArrayList<>();
            List<ContentViewEntry> sortDocs = new ArrayList<>();

            for (ContentViewEntry entry : contentViewEntries) {
                if (entry.getMediaType() == ContentViewEntry.ContentType.GROUP.getType()) {
                    sortDirectories.add(entry);
                } else if (entry.getMediaType() == ContentViewEntry.ContentType.VIDEO.getType()) {
                    sortVideos.add(entry);
                } else if (entry.getMediaType() == ContentViewEntry.ContentType.AUDIO.getType()) {
                    sortAudios.add(entry);
                } else if (entry.getMediaType() == ContentViewEntry.ContentType.DOC.getType()) {
                    sortDocs.add(entry);
                }
            }

            LogUtil.INSTANCE.info("birdgangsort" , " sortDirectories size : " + sortDirectories.size() + " , sortVideos size : " + sortVideos.size()
                    + " , sortAudios size : " + sortAudios.size() + " , sortDocs size : " + sortDocs.size());


            if (conditionSort == PreferencesCacheHelper.SORT_NAME_ASC) {
                Collections.sort(sortDirectories, new ContentViewEntry.SortContentNameAscCompare());
                Collections.sort(sortVideos, new ContentViewEntry.SortContentNameAscCompare());
                Collections.sort(sortAudios, new ContentViewEntry.SortContentNameAscCompare());
                Collections.sort(sortDocs, new ContentViewEntry.SortContentNameAscCompare());
            }
            else if (conditionSort == PreferencesCacheHelper.SORT_NAME_DESC) {
                Collections.sort(sortDirectories, new ContentViewEntry.SortContentNameDescCompare());
                Collections.sort(sortVideos, new ContentViewEntry.SortContentNameDescCompare());
                Collections.sort(sortAudios, new ContentViewEntry.SortContentNameDescCompare());
                Collections.sort(sortDocs, new ContentViewEntry.SortContentNameDescCompare());
            }

            contentViewEntries.clear();
            contentViewEntries.addAll(sortDirectories);
            contentViewEntries.addAll(sortVideos);
            contentViewEntries.addAll(sortAudios);
            contentViewEntries.addAll(sortDocs);
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
    }


    private Observable<List<ExplorerStackEntry>> getExplorerStackObservable(final Map<String, List<ContentCacheEntry>> contentCacheMemorAwareHashMap, final Map<String, ContentCacheEntry> contentCacheDiskAwareMaps, final String rootType) {
        return Observable.fromCallable(new Callable() {
            @Override
            public Object call() throws Exception {
                List<ContentCacheEntry> contentCacheMemorAwareEntries = new ArrayList<>();
                List<ContentCacheEntry> resultEntries = new ArrayList<>();
                contentCacheMemorAwareEntries.addAll(contentCacheMemorAwareHashMap.get(rootType));

                List<String> innerStoragePaths = MediaStorage.INSTANCE.getInnerStoragePaths();
                List<String> externalStoragePaths = MediaStorage.INSTANCE.getExternalStoragePaths();
                List<String> usbStoragePaths = MediaStorage.INSTANCE.getUsbStoragePaths();

                LogUtil.INSTANCE.info(TAG, " innerStoragePaths : " + innerStoragePaths.toString());
                LogUtil.INSTANCE.info(TAG, " externalStoragePaths : " + externalStoragePaths.toString());
                LogUtil.INSTANCE.info(TAG, " usbStoragePaths : " + usbStoragePaths.toString());

                for (ContentCacheEntry contentEntryMemorAwares : contentCacheMemorAwareEntries) {
                    String contentFilePath = contentEntryMemorAwares.getContentFilePath();

                    if (StringUtils.equals(MediaStorage.ROOT_INTERNAL, rootType)) {
                        for (String root : innerStoragePaths) {
                            if (StringUtils.contains(contentFilePath, root)) {
                                resultEntries.add(contentEntryMemorAwares);
                                //LogUtil.INSTANCE.debug(TAG, "MediaStorage.INTERNAL > contentEntry.toString() : " + contentEntryMemorAwares.toString());
                            }
                        }
                    }
                    else if (StringUtils.equals(MediaStorage.ROOT_EXTERNAL, rootType)) {
                        for (String root : externalStoragePaths) {
                            if (StringUtils.contains(contentFilePath, root)) {
                                resultEntries.add(contentEntryMemorAwares);
                                //LogUtil.INSTANCE.debug(TAG, "MediaStorage.EXTERNAL > contentEntry.toString() : " + contentEntryMemorAwares.toString());
                            }
                        }
                    }
                    else if (StringUtils.equals(MediaStorage.ROOT_USB, rootType)) {
                        for (String root : usbStoragePaths) {
                            if (StringUtils.contains(contentFilePath, root)) {
                                resultEntries.add(contentEntryMemorAwares);
                                //LogUtil.INSTANCE.debug(TAG, "MediaStorage.USB > contentEntry.toString() : " + contentEntryMemorAwares.toString());
                            }
                        }
                    }
                }

                LogUtil.INSTANCE.debug(TAG, " contentCacheMemorAwareEntries : " + resultEntries.size());

                List<ExplorerStackEntry> explorerStackEntries = new ArrayList<>();
                ExplorerStackGenerate explorerStackGenerate = new ExplorerStackGenerate(contentCacheDiskAwareMaps);

                for (ContentCacheEntry contentEntryMemoryAwres : resultEntries) {
                    ExplorerStackEntry explorerStackEntry = explorerStackGenerate.generateStack(contentEntryMemoryAwres);
                    explorerStackEntries.add(explorerStackEntry);
                }
                return explorerStackEntries;
            }
        });
    }

}