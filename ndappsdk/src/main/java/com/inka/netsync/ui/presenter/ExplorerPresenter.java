package com.inka.netsync.ui.presenter;

import android.content.Context;

import com.androidnetworking.error.ANError;
import com.inka.ncg.nduniversal.NcgValidationCheck;
import com.inka.ncg2.Ncg2Exception;
import com.inka.ncg2.StringEncrypter;
import com.inka.netsync.BaseApplication;
import com.inka.netsync.BaseConfigurationPerSite;
import com.inka.netsync.R;
import com.inka.netsync.common.utils.AndroidUtil;
import com.inka.netsync.common.utils.FileUtil;
import com.inka.netsync.common.utils.NetworkUtils;
import com.inka.netsync.data.DataManager;
import com.inka.netsync.data.cache.pref.PreferencesCacheHelper;
import com.inka.netsync.data.network.converter.SerialAuthenticationConverter;
import com.inka.netsync.data.network.model.ResponseSerialAuthEntry;
import com.inka.netsync.data.network.request.SerialAuthRequest;
import com.inka.netsync.logs.LogUtil;
import com.inka.netsync.model.ContentEntry;
import com.inka.netsync.ncg.NetSyncSdkHelper;
import com.inka.netsync.ncg.model.LicenseEntry;
import com.inka.netsync.ncg.model.PlayerEntry;
import com.inka.netsync.ui.mvppresenter.ExplorerMvpPresenter;
import com.inka.netsync.ui.mvpview.ExplorerMvpView;
import com.inka.netsync.view.model.ContentViewEntry;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by birdgang on 2017. 4. 18..
 */
public class ExplorerPresenter<V extends ExplorerMvpView> extends BasePresenter<V> implements ExplorerMvpPresenter<V> {

    private final String TAG = "ExplorerPresenter";

    StringEncrypter mEncrypter = null;

    @Inject
    public ExplorerPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public void onViewInitialized() {}

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


    @Override
    public void checkLicenseValid(Context context, ContentEntry contentEntry) throws Ncg2Exception {
        String path = contentEntry.getContentFilePath();

        boolean isNcgContent = NetSyncSdkHelper.getDefault().isNcgContent(path);
        LogUtil.INSTANCE.info("AppApiHelper", "mContentItemClickListener > isNcgContent : " + isNcgContent);

        if (isNcgContent) {
            int checkLicenseValid = NetSyncSdkHelper.getDefault().checkLicenseValid(path);
            LogUtil.INSTANCE.info("AppApiHelper", "mContentItemClickListener > checkLicenseValid : " + checkLicenseValid);
            if (NcgValidationCheck.NotExistLicense == checkLicenseValid) {
                getMvpView().onLoadInputSerialDialog(new File(path));
            }
            else if (NcgValidationCheck.ScreenRecorderDetected == checkLicenseValid) {
                String packageName = NetSyncSdkHelper.getDefault().getScreenRecorderDetectedPackageName(path);
                getMvpView().onLoadToastMessage(context.getString(R.string.license_screen_recorder_detected, packageName));
            }
            else {
                String contentIdInHeaderInformation = NetSyncSdkHelper.getDefault().getContentId(path);
                if (StringUtils.isBlank(contentIdInHeaderInformation)) {
                    getMvpView().onLoadToastMessage(context.getString(R.string.business_logic_invalid_ncg_file));
                    return;
                }
                getMvpView().clientAcquireLicense(path, "NOUSERID");
            }
        }
    }


    @Override
    public void checkSerialNumberValid(Context context, String filePath, String serialNumber) throws Exception {
        if (StringUtils.isBlank(serialNumber)) {
            getMvpView().onLoadMessageDialog(context.getString(R.string.message_for_dialog_download_no_serial));
        }
        else {
            requestApiSerialAuthRx(filePath, serialNumber);
        }
    }


    /**
     *
     * @param licenseEntry
     * @throws Ncg2Exception
     */
    @Override
    public void requesetClientAcquireLicense(LicenseEntry licenseEntry) throws Ncg2Exception {
        String localFilePath = licenseEntry.getFilePath();

        if (!FileUtil.existsFile(localFilePath)) {
            getMvpView().onLoadToastMessage(getDataManager().getContext().getString(R.string.message_for_toast_file_not_exist));
            return;
        }

        boolean isNcgContent = NetSyncSdkHelper.getDefault().isNcgContent(localFilePath);
        if (!isNcgContent) {
            getMvpView().onLoadToastMessage(getDataManager().getContext().getString(R.string.message_for_toast_file_is_not_ncg));
            return;
        }

        getClientAcquireLicenseObservable(licenseEntry)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LicenseEntry>() {
                    @Override
                    public void accept(LicenseEntry licenseEntry) throws Exception {
                        LogUtil.INSTANCE.info("birdgangobservable" , "accept > licenseEntry.toString() : " + licenseEntry.toString());
                        getMvpView().onResultClientAcquireLicense(licenseEntry);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        LogUtil.INSTANCE.info("birdgangobservable" , "throwable.getMessage() : " + throwable.getMessage());
                    }
                });
    }


    @Override
    public void requestApiSerialAuthRx(String filePath, String serialNumber) throws Exception {
        SerialAuthRequest.RequestSerialAuth requestSerialAuth = new SerialAuthRequest.RequestSerialAuth();
        Context context = getDataManager().getContext();
        requestSerialAuth.key = BaseConfigurationPerSite.getInstance().getKey();
        requestSerialAuth.iv = BaseConfigurationPerSite.getInstance().getIv();
        requestSerialAuth.enterpriseCode = BaseConfigurationPerSite.getInstance().getEnterpriseCode();
        requestSerialAuth.deviceModel = AndroidUtil.getDeviceModel();
        requestSerialAuth.cid = StringUtils.substring(NetSyncSdkHelper.getDefault().getContentId(filePath), 4);
        requestSerialAuth.appVersion = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        requestSerialAuth.serial = serialNumber;
        requestSerialAuth.deviceId = BaseApplication.getCachedDeviceId();
        if (StringUtils.isNotBlank(requestSerialAuth.key) && StringUtils.isNotBlank(requestSerialAuth.iv)) {
            requestSerialAuth.encrypter = mEncrypter = new StringEncrypter(requestSerialAuth.key, requestSerialAuth.iv);
        }
        requestSerialAuth.encrypter = mEncrypter;

        CompositeDisposable compositeDisposable = getCompositeDisposable();
        Observable<String> serialAuthResponseObservable = getDataManager().getSerialAuthStringApiCall(new SerialAuthRequest.ServerSerialAuthRequest(requestSerialAuth));
        Disposable disposable = serialAuthResponseObservable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new ResponseSerialAuth(filePath), responseError);
        compositeDisposable.add(disposable);
    }


    @Override
    public void requestPrepareLicenseAndExecutePlayer(Context context, final PlayerEntry playerEntry) {
        if (null == playerEntry) {
            getMvpView().onLoadToastMessage(getDataManager().getContext().getString(R.string.message_for_toast_file_not_exist));
            return;
        }

        getPrepareLicenseAndExecuteObservable(context, playerEntry)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<PlayerEntry>() {
                    @Override
                    public void accept(final PlayerEntry entry) throws Exception {
                        LogUtil.INSTANCE.info("birdgangobservable" , "requestPrepareLicenseAndExecutePlayer > accept > entry.toString() : " + entry.toString());
                        getMvpView().onLoadPlaybackActivity(entry);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        LogUtil.INSTANCE.info("birdgangobservable" , "throwable.getMessage() : " + throwable.getMessage());
                    }
                });
    }


    class ResponseSerialAuth implements Consumer<String> {
        private String filepath;

        ResponseSerialAuth (String filepath) {
            this.filepath = filepath;
        }

        @Override
        public void accept(String responseEncrypter) throws Exception {
            if (!isViewAttached()) {
                return;
            }

            if (StringUtils.isNotBlank(responseEncrypter) && null != mEncrypter) {
                LogUtil.INSTANCE.info("AppApiHelper" , "responseSerialAuth > response : " + responseEncrypter);
                responseEncrypter = StringUtils.trim(responseEncrypter);
                String decryptedData = mEncrypter.decrypt(responseEncrypter);
                Context context = getDataManager().getContext();
                SerialAuthenticationConverter serialAuthenticationConverter = new SerialAuthenticationConverter();
                ResponseSerialAuthEntry responseSerialAuthEntry = serialAuthenticationConverter.converter(context, decryptedData);
                responseSerialAuthEntry.setOrginFilePat(filepath);
                LogUtil.INSTANCE.info("AppApiHelper" , "responseSerialAuth > responseSerialAuthEntry.toString() : " + responseSerialAuthEntry.toString());
                getMvpView().onResponseSerialAuth(responseSerialAuthEntry);
            }
        }
    }


    private Consumer responseError = new Consumer<Throwable>() {
        @Override
        public void accept(Throwable throwable) throws Exception {
            if (!isViewAttached()) {
                return;
            }

            LogUtil.INSTANCE.info("AppApiHelper" , "throwable : " + throwable.getMessage());

            if (throwable instanceof ANError) {
                ANError anError = (ANError) throwable;
                handleApiError(anError);
            }
        }
    };


    /**
     * 라이센스가 유효 한지 체크
     * @param licenseEntry
     * @return
     */
    private Observable<LicenseEntry> getClientAcquireLicenseObservable(final LicenseEntry licenseEntry) {
        final String localFilePath = licenseEntry.getFilePath();
        final String serialNumber = licenseEntry.getSerialNumber();
        final String orderId = licenseEntry.getOrderId();

        return Observable.fromCallable(new Callable<LicenseEntry>() {
            @Override
            public LicenseEntry call() throws Exception {
                int checkLicenseValid = NetSyncSdkHelper.getDefault().checkLicenseValid(localFilePath);
                LogUtil.INSTANCE.info("birdgangacquirelicense" , "localFilePath : " + localFilePath + " , serialNumber : " + serialNumber + " , orderId : " + orderId + " , checkLicenseValid : " + checkLicenseValid);
                if (NcgValidationCheck.ValidLicense != checkLicenseValid) {
                    NetSyncSdkHelper.getDefault().acquireLicenseByPath(localFilePath, serialNumber, orderId);
                    checkLicenseValid = NcgValidationCheck.ValidLicense;
                }

                if (NcgValidationCheck.ValidLicense == checkLicenseValid) {
                    licenseEntry.setMessage(LicenseEntry.RESPONSE_SUCCESS);
                    licenseEntry.setSerialNumber(serialNumber);
                    licenseEntry.setOrderId(orderId);
                    licenseEntry.setFilePath(localFilePath);
                }
                return licenseEntry;
            }
        });
    }


    private Observable<PlayerEntry> getPrepareLicenseAndExecuteObservable(final Context context, final PlayerEntry playerEntry) {
        final String filePath = playerEntry.getFilePath();
        final String userId = playerEntry.getUserId();
        final String infoOrderId = playerEntry.getInfoOrderId();

        return Observable.fromCallable(new Callable() {
            @Override
            public Object call() throws Exception {
                try {
                    boolean isNcgContent = NetSyncSdkHelper.getDefault().isNcgContent(filePath);
                    LogUtil.INSTANCE.info("birdgangplay",  "isNcgContent : " + isNcgContent + " , filePath : " + filePath);
                    if (isNcgContent) {
                        int licenseValid = NetSyncSdkHelper.getDefault().checkLicenseValid(filePath);
                        LogUtil.INSTANCE.info("birdgangplay",  "licenseValid : " + licenseValid);

                        if (licenseValid == NcgValidationCheck.ValidLicense) {
                            playerEntry.setSuccess(true);
                        }
                        else if (licenseValid == NcgValidationCheck.DeviceTimeModified || licenseValid == NcgValidationCheck.OfflineStatusTooLong) {
                            if (NetworkUtils.isNetworkConnected(context)) {
                                if(licenseValid == NcgValidationCheck.DeviceTimeModified) {
                                    playerEntry.setErrorMessage(context.getString(R.string.license_device_time_modified));
                                } else if(licenseValid == NcgValidationCheck.OfflineStatusTooLong) {
                                    playerEntry.setErrorMessage(context.getString(R.string.license_offline_status_too_long));
                                }
                            } else {
                                NetSyncSdkHelper.getDefault().updateSecureTime();
                            }
                            playerEntry.setSuccess(false);
                        }
                        else if (licenseValid == NcgValidationCheck.ScreenRecorderDetected) {
                            String packageName = NetSyncSdkHelper.getDefault().getLicenseValidationExtraData(filePath, "AppPackageName");
                            playerEntry.setErrorMessage(context.getString(R.string.license_screen_recorder_detected, packageName));
                            playerEntry.setSuccess(false);
                        }
                        else {
                            if (!NetworkUtils.isNetworkConnected(context)) {
                                if (licenseValid == NcgValidationCheck.ExpiredLicense || licenseValid == NcgValidationCheck.ExceededPlayCount) {
                                    playerEntry.setErrorMessage(context.getString(R.string.license_exception_no_authority));
                                } else {
                                    playerEntry.setErrorMessage(context.getString(R.string.license_exception_network_error));
                                }
                                playerEntry.setSuccess(false);
                                // online
                            } else {
                                // OneTime 라이센스 정책
                                boolean isTemporary = false;
                                LogUtil.INSTANCE.info("birdgangplay", " processNcgLicense > UserID : " + userId + " , orderID : " + infoOrderId + ", isTemporary : " + isTemporary);
                                NetSyncSdkHelper.getDefault().acquireLicenseByPath(filePath, userId, infoOrderId, isTemporary);
                                playerEntry.setSuccess(true);
                            }
                        }
                        playerEntry.setErrorMessage(NetSyncSdkHelper.getDefault().checkForPlaybackLicenseMessage(filePath, licenseValid));
                    }
                } catch (Exception e) {
                    LogUtil.INSTANCE.error("error", e);
                }
                return playerEntry;
            }
        });
    }


}
