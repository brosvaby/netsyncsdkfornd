package com.inka.playermango;

import android.content.DialogInterface;
import android.os.Build;
import android.support.v7.app.AlertDialog;

import com.inka.netsync.sd.ui.SDCertificationActivity;

import java.util.List;

/**
 * SD 카드를 통한 인증화면 입니다.
 */
public class CertificationActivityEx extends SDCertificationActivity {

    /**
     * 인증 성공시 다음에 보여질 화면을 지정합니다.
     * @return
     */
    @Override
    protected Class<?> provideNextContentView () {
        return DrawerActivityEx.class;
    }


    /**
     * 디바이스 제한에 실패 했을 경우에 대한 Callback 메소드 입니다.
     * @return
     */
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