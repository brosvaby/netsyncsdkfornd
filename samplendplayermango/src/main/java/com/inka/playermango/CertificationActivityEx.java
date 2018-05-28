package com.inka.playermango;

import android.content.DialogInterface;
import android.os.Build;
import android.support.v7.app.AlertDialog;

import com.inka.netsync.logs.LogUtil;
import com.inka.netsync.sd.ui.SDCertificationActivity;

import java.util.List;

public class CertificationActivityEx extends SDCertificationActivity {

    @Override
    protected Class<?> provideNextContentView () {
        return DrawerActivityEx.class;
    }

    @Override
    protected boolean onResultEnableDeviceModels() {
        boolean result = false;
        List<String> enableDeviceModels = MangoApplication.provideEnableDeviceModels();
        String currentDeviceModel = Build.MODEL;
        if (enableDeviceModels.size() > 0 && !enableDeviceModels.contains(currentDeviceModel)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.dialog_title_device_authentication));
            builder.setMessage(getString(R.string.log_result_fail_to_get_proprietary_info));
            builder.setPositiveButton(getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    finish();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            result = false;
        } else {
            result = true;
        }
        return result;
    }

}