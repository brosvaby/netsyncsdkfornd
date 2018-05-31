package com.inka.netsync.ui.presenter;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.androidnetworking.error.ANError;
import com.inka.netsync.R;
import com.inka.netsync.data.DataManager;
import com.inka.netsync.data.cache.pref.PreferencesCacheHelper;
import com.inka.netsync.data.network.converter.MarketVersionCheckConverter;
import com.inka.netsync.data.network.model.MarketVersionCheckEntry;
import com.inka.netsync.data.network.request.MarketCheckRequest;
import com.inka.netsync.data.network.response.MarketVersionCheckResponse;
import com.inka.netsync.logs.LogUtil;
import com.inka.netsync.ui.mvppresenter.SettingMvpPresenter;
import com.inka.netsync.ui.mvpview.SettingMvpView;
import com.inka.netsync.view.model.SingleCheckViewEntry;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by birdgang on 2017. 4. 14..
 */

public class SettingPresenter<V extends SettingMvpView> extends BasePresenter<V> implements SettingMvpPresenter<V> {

    @Inject
    public SettingPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public void onViewInitialized() {
    }

    @Override
    public void changePlayerType() {
        String[] strings = getDataManager().getStringArrayResource(R.array.array_setting_check_player);
        int value = PreferencesCacheHelper.getPreferenceValueForInteger(PreferencesCacheHelper.SETTING_PLAYER, PreferencesCacheHelper.VISUALON_PLAYER);
        LogUtil.INSTANCE.info("birdgangbasesettingdialog" , "showChangePlayerDialog > value : " + value);
        List<SingleCheckViewEntry> singleSelectViewEntries = new ArrayList<>();
        singleSelectViewEntries.add(new SingleCheckViewEntry(PreferencesCacheHelper.BASE_PLAYER, strings[0], (value == PreferencesCacheHelper.BASE_PLAYER) ? true : false));
        singleSelectViewEntries.add(new SingleCheckViewEntry(PreferencesCacheHelper.VISUALON_PLAYER, strings[1], (value == PreferencesCacheHelper.VISUALON_PLAYER) ? true : false));
        getMvpView().onLoadChangePlayerDialog(singleSelectViewEntries);
    }

    @Override
    public void changeDecoderType() {
        String[] strings = getDataManager().getStringArrayResource(R.array.array_setting_check_decoder);
        int value = PreferencesCacheHelper.getPreferenceValueForInteger(PreferencesCacheHelper.SETTING_DECODER, PreferencesCacheHelper.DECODER_HARDWARE);
        LogUtil.INSTANCE.info("birdgangbasesettingdialog" , "showChangeDecoderDialog > value : " + value);
        List<SingleCheckViewEntry> singleSelectViewEntries = new ArrayList<>();
        singleSelectViewEntries.add(new SingleCheckViewEntry(PreferencesCacheHelper.DECODER_HARDWARE, strings[0], (value == PreferencesCacheHelper.DECODER_HARDWARE) ? true : false));
        singleSelectViewEntries.add(new SingleCheckViewEntry(PreferencesCacheHelper.DECODER_SOFTWARE, strings[1], (value == PreferencesCacheHelper.DECODER_SOFTWARE) ? true : false));
        getMvpView().onLoadChangeDecoderDialog(singleSelectViewEntries);
    }

    @Override
    public String checkPlayerType() {
        int value = PreferencesCacheHelper.getPreferenceValueForInteger(PreferencesCacheHelper.SETTING_PLAYER, PreferencesCacheHelper.VISUALON_PLAYER);
        LogUtil.INSTANCE.info("birdgangbasesettingdialog" , "getPlayerType > value : " + value);
        String playerType = "";
        switch (value) {
            case PreferencesCacheHelper.BASE_PLAYER:
                playerType = getDataManager().getStringResource(R.string.setting_player_base);
                break;
            case PreferencesCacheHelper.VISUALON_PLAYER:
                playerType = getDataManager().getStringResource(R.string.setting_player_double_speed);
                break;
            default:
                playerType = getDataManager().getStringResource(R.string.setting_player_base);
                break;
        }
        return playerType;
    }

    @Override
    public String checkDecoderType() {
        int value = PreferencesCacheHelper.getPreferenceValueForInteger(PreferencesCacheHelper.SETTING_DECODER, PreferencesCacheHelper.DECODER_HARDWARE);
        String decoderType = "";
        switch (value) {
            case PreferencesCacheHelper.DECODER_SOFTWARE:
                decoderType = getDataManager().getStringResource(R.string.setting_decoder_software);
                break;
            case PreferencesCacheHelper.DECODER_HARDWARE:
                decoderType = getDataManager().getStringResource(R.string.setting_decoder_hardware);
                break;
            default:
                decoderType = getDataManager().getStringResource(R.string.setting_none);
                break;
        }
        return decoderType;
    }


    @Override
    public String getAppVersion() throws Exception {
        Context context = getDataManager().getContext();
        PackageManager packageManager = context.getPackageManager();
        String packageName 	= context.getPackageName();
        PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);
        String appVersion = packageInfo.versionName;
        String version = appVersion;
        return version;
    }

    @Override
    public String getAppPackage() throws Exception {
        Context context = getDataManager().getContext();
        PackageManager packageManager = context.getPackageManager();
        String packageName 	= context.getPackageName();
        PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);
        String appPackageName = packageInfo.packageName;
        return appPackageName;
    }


    @Override
    public void checkMarketVersion(MarketVersionCheckEntry requestMarketVersionCheck) {
        MarketCheckRequest.RequestMarketCheck requestMarketCheck = new MarketCheckRequest.RequestMarketCheck();
        requestMarketCheck.packageName = requestMarketVersionCheck.getPackageName();

        CompositeDisposable compositeDisposable = getCompositeDisposable();
        Observable<String> marketVersionCheckResponseObservable = getDataManager().getStringMarketVersionCheckApiCall(new MarketCheckRequest.ServerMarketCheckRequest(requestMarketCheck));
        Disposable disposable = marketVersionCheckResponseObservable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new ResponseMarketVersionCheck(requestMarketVersionCheck), responseError);
        compositeDisposable.add(disposable);
    }


    private class ResponseMarketVersionCheck implements Consumer<String> {

        MarketVersionCheckEntry requestMarketVersionCheck;

        public ResponseMarketVersionCheck (MarketVersionCheckEntry requestMarketVersionCheck) {
            this.requestMarketVersionCheck = requestMarketVersionCheck;
        }

        @Override
        public void accept(String response) throws Exception {
            if (!isViewAttached()) {
                return;
            }

            LogUtil.INSTANCE.info("AppApiHelper" , "accept > response : " + response);

            if (StringUtils.isNotBlank(response)) {
                response = StringUtils.trim(response);
                Context context = getDataManager().getContext();
                MarketVersionCheckConverter marketVersionCheckConverter = new MarketVersionCheckConverter();
                MarketVersionCheckResponse marketVersionCheckResponse = marketVersionCheckConverter.converter(requestMarketVersionCheck, response);
                LogUtil.INSTANCE.info("AppApiHelper" , "marketVersionCheckResponse.toString() : " + marketVersionCheckResponse.toString());
            }
        }
    }


    private Consumer responseError = new Consumer<Throwable>() {
        @Override
        public void accept(Throwable throwable) throws Exception {
            if(!isViewAttached()) {
                return;
            }

            if (throwable instanceof ANError) {
                ANError anError = (ANError) throwable;
                handleApiError(anError);
            }
        }
    };

}
