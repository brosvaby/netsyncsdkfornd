package com.inka.netsync.sd.ui.presenter;

import android.content.Context;

import com.inka.netsync.data.DataManager;
import com.inka.netsync.sd.ui.mvppresenter.SDCertificationMvpPresenter;
import com.inka.netsync.sd.ui.mvpview.SDCertificationMvpView;
import com.inka.netsync.ui.presenter.BasePresenter;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by birdgang on 2017. 5. 29..
 */

public class SDCertificationPresenter<V extends SDCertificationMvpView> extends BasePresenter<V> implements SDCertificationMvpPresenter<V> {

    @Inject
    public SDCertificationPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public void onViewInitialized() {
    }

    @Override
    public boolean checkEnableDeviceModel(Context context, List<String> enableDeviceModels) {

        boolean result = false;

//        if (!enableDeviceModels.contains(enableDeviceModels) && !ModuleConfig.NO_DEVICE_CHECK) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(context);
//            builder.setTitle(context.getString(R.string.dialog_title_device_authentication));
//            builder.setMessage(context.getString(R.string.log_result_fail_to_get_proprietary_info));
//            builder.setPositiveButton(context.getString(R.string.dialog_button_ok), new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.dismiss();
//                }
//            });
//
//            AlertDialog dialog = builder.create();
//            dialog.setCancelable(false);
//            dialog.setCanceledOnTouchOutside(false);
//            dialog.show();
//
//            result = true;
//        }

        return result;
    }

}
