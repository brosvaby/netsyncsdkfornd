package com.inka.netsync.sd.ui.presenter;

import android.content.Context;
import android.os.Build;

import com.inka.ncg.nduniversal.ModuleConfig;
import com.inka.ncg.nduniversal.NcgValidationCheck;
import com.inka.ncg.nduniversal.common.NcgResponseCode;
import com.inka.ncg.nduniversal.hidden.Certification;
import com.inka.ncg.nduniversal.model.ResponseNcgEntry;
import com.inka.ncg2.Ncg2Exception;
import com.inka.netsync.R;
import com.inka.netsync.common.utils.FileUtil;
import com.inka.netsync.common.utils.NetworkUtils;
import com.inka.netsync.data.DataManager;
import com.inka.netsync.logs.LogUtil;
import com.inka.netsync.model.AddLicenseEntry;
import com.inka.netsync.model.ContentEntry;
import com.inka.netsync.ncg.NetSyncSdkHelper;
import com.inka.netsync.ncg.model.PlayerEntry;
import com.inka.netsync.sd.ui.mvppresenter.SDExplorerMvpPresenter;
import com.inka.netsync.sd.ui.mvpview.SDExplorerMvpView;
import com.inka.netsync.ui.presenter.BasePresenter;
import com.inka.netsync.view.model.ContentViewEntry;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by birdgang on 2017. 4. 18..
 */
public class SDExplorerPresenter<V extends SDExplorerMvpView> extends BasePresenter<V> implements SDExplorerMvpPresenter<V> {

    private final String TAG = "SDExplorerPresenter";

    @Inject
    public SDExplorerPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public void onViewInitialized() {
    }

    @Override
    public void checkLicenseValid(Context context, ContentEntry contentEntry) throws Ncg2Exception {
        int contentId = contentEntry.getContentId();
        String path = contentEntry.getContentFilePath();

        boolean isNcgContent = NetSyncSdkHelper.getDefault().isNcgContent(path);
        LogUtil.INSTANCE.info("birdgangacquirelicense", "mContentItemClickListener > isNcgContent : " + isNcgContent);

        if (isNcgContent) {
            int checkLicenseValid = NetSyncSdkHelper.getDefault().checkLicenseValid(path);
            LogUtil.INSTANCE.info("birdgangacquirelicense", "mContentItemClickListener > checkLicenseValid : " + checkLicenseValid);
            if (NcgValidationCheck.NotExistLicense == checkLicenseValid) {
                getMvpView().onResponseCheckLicenseValid(contentId, new File(path));
            }
            else if (NcgValidationCheck.ScreenRecorderDetected == checkLicenseValid) {
                String packageName = NetSyncSdkHelper.getDefault().getScreenRecorderDetectedPackageName(path);
                getMvpView().onLoadToastMessage(context.getString(R.string.license_screen_recorder_detected, packageName));
            }
            else {
                String contentIdInHeaderInformation = NetSyncSdkHelper.getDefault().getContentId(path);
                LogUtil.INSTANCE.info("birdgangacquirelicense", "mContentItemClickListener > contentIdInHeaderInformation : " + contentIdInHeaderInformation);
                if (StringUtils.isBlank(contentIdInHeaderInformation)) {
                    getMvpView().onLoadToastMessage(context.getString(R.string.business_logic_invalid_ncg_file));
                    return;
                }
                getMvpView().onRequestLicense(contentId, new File(path));
            }
        }
    }


    @Override
    public void checkSDLicense(Context context, int contentId, File file, Certification certification) {
        try {
            ResponseNcgEntry responseNcgEntry = certification.checkNewSdCard();
            int resultCode = responseNcgEntry.getResultCode();

            if (resultCode == NcgResponseCode.SUCCESS && Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
                getMvpView().onLoadOfflineAuthenticationDialog(contentId, file); // 인증 진행 확인
            } else {
                responseNcgEntry = certification.checkLicense();
                resultCode = responseNcgEntry.getResultCode();
                if (resultCode == NcgResponseCode.SUCCESS) {
                    getMvpView().onRequestLicense(contentId, file);
                } else {
                    getMvpView().onLoadToastMessage(context.getString(R.string.sd_business_logic_fail_to_device_certification)); // 인증 진행 확인
                }
            }
        }  catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
    }

    @Override
    public void sortListContent(List<ContentViewEntry> contentViewEntries) {
    }

    @Override
    public void requestAddLicense(int contentId, File file) throws Ncg2Exception {
        if (!FileUtil.existsFile(file.getPath())) {
            getMvpView().onLoadToastMessage(getDataManager().getContext().getString(R.string.message_for_toast_file_not_exist));
            return;
        }

        getAddLicenseObservable(contentId, file)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<AddLicenseEntry>() {
                    @Override
                    public void accept(AddLicenseEntry entry) throws Exception {
                        LogUtil.INSTANCE.info("birdgangobservable" , "accept > entry.toString() : " + entry.toString());
                        getMvpView().onPrepareExecutePlay(entry);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        LogUtil.INSTANCE.info("birdgangobservable" , "throwable.getMessage() : " + throwable.getMessage());
                    }
                });

    }


    @Override
    public void requestPrepareLicenseAndExecutePlayer(Context context, PlayerEntry playerEntry) {
        if (null == playerEntry) {
            getMvpView().onLoadToastMessage(getDataManager().getContext().getString(R.string.message_for_toast_file_not_exist));
            return;
        }

        getPrepareLicenseAndExecuteObservable(context, playerEntry)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<PlayerEntry>() {
                    @Override
                    public void accept(PlayerEntry entry) throws Exception {
                        LogUtil.INSTANCE.info("birdgangobservable" , "accept > entry.toString() : " + entry.toString());
                        getMvpView().onLoadPlaybackActivity(entry);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        LogUtil.INSTANCE.info("birdgangobservable" , "throwable.getMessage() : " + throwable.getMessage());
                    }
                });
    }


    /***
     * 라이센스 발급을 위한 Observable
     * @param contentId
     * @param file
     * @return
     * @throws Ncg2Exception
     */
    private Observable<AddLicenseEntry> getAddLicenseObservable (final int contentId, final File file) throws Ncg2Exception {

        return Observable.fromCallable(new Callable() {
            @Override
            public Object call() throws Exception {
                final AddLicenseEntry addLicenseEntry = new AddLicenseEntry();

                try {
                    // 파일 존재 유무 체크
                    String localFilePath = file.getAbsolutePath();
                    if (!FileUtil.existsFile(localFilePath)) {
                        //Toast.makeText(context, context.getString(R.string.fragment_narmal_file_not_exist), Toast.LENGTH_SHORT).show();
                        return addLicenseEntry;
                    }

                    addLicenseEntry.setContentId(contentId);
                    addLicenseEntry.setContentFile(file.getAbsolutePath());
                    addLicenseEntry.setContentName(file.getName());

                    LogUtil.INSTANCE.info("birdgangchecklicense", "localFilePath : " + localFilePath);

                    boolean isNcgContent = NetSyncSdkHelper.getDefault().isNcgContent(localFilePath);

                    if (isNcgContent) {
                        String cId = NetSyncSdkHelper.getDefault().getContentId(localFilePath);
                        LogUtil.INSTANCE.info("birdgangchecklicense", "contentId : " + contentId);

                        //라이센스 체크
                        if (NcgValidationCheck.ValidLicense != NetSyncSdkHelper.getDefault().checkLicenseValidByCID(cId)) {
                            String seedString = NetSyncSdkHelper.getDefault().readPallyconInfoTypeA();
                            LogUtil.INSTANCE.info("birdgangchecklicense", "seedString : " + seedString);

                            if (StringUtils.isNotBlank(seedString)) {
                                NetSyncSdkHelper.getDefault().addLicense(cId, seedString, "", "", 0, -1, -1, ModuleConfig.ENABLE_NO_ANTI_MIRRORING_MODE);
                                addLicenseEntry.setResponseCode(NcgResponseCode.SUCCESS);
                            } else {
                                addLicenseEntry.setResponseCode(NcgResponseCode.FAIL);
                            }
                        }
                        else {
                            addLicenseEntry.setResponseCode(NcgResponseCode.SUCCESS);
                        }
                    }
                } catch (Exception e) {
                    addLicenseEntry.setResponseCode(NcgResponseCode.FAIL);
                    addLicenseEntry.setResponseMessage(e.getMessage());
                }

                return addLicenseEntry;
            }
        });

    }


    /**
     *
     * @param context
     * @param playerEntry
     * @return
     */
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
