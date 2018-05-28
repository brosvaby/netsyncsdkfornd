package com.inka.netsync.sd.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.provider.DocumentFile;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.inka.ncg.nduniversal.common.NcgResponseCode;
import com.inka.ncg.nduniversal.exception.Ncg2CoreException;
import com.inka.ncg.nduniversal.hidden.Certification;
import com.inka.ncg.nduniversal.hidden.CertificationHelper;
import com.inka.ncg.nduniversal.model.ResponseNcgEntry;
import com.inka.netsync.R;
import com.inka.netsync.R2;
import com.inka.netsync.command.Command;
import com.inka.netsync.command.CommandHandler;
import com.inka.netsync.common.ActivityCalls;
import com.inka.netsync.common.IntentParams;
import com.inka.netsync.common.utils.AssetsUtil;
import com.inka.netsync.logs.LogUtil;
import com.inka.netsync.sd.ui.mvppresenter.SDCertificationMvpPresenter;
import com.inka.netsync.sd.ui.mvpview.SDCertificationMvpView;
import com.inka.netsync.ui.BaseActivity;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class SDCertificationActivity extends BaseActivity implements SDCertificationMvpView {

    private final String TAG = "SDCertificationActivity";

    private Certification mCertification;

    @Inject
    SDCertificationMvpPresenter<SDCertificationMvpView> mPresenter;

    private class PendingLauncherHandler implements Command {
        @Override
        public void execute() {
            try {
                startMainActivity(provideNextContentView());
            } catch (Ncg2CoreException e) {
            }
        }
    }

    protected void startMainActivity(Class<?> cls) throws Ncg2CoreException {
        Intent intent = new Intent(this, cls);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

        finish();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permissionhelper);

        getActivityComponent().inject(this);
        setUnBinder(ButterKnife.bind(this));
        mPresenter.onAttach(SDCertificationActivity.this);

        setUp();
    }

    @Override
    protected void setUp() {
        mCertification = CertificationHelper.getDefault().initCertification(this, Build.VERSION.SDK_INT);
    }

    @OnClick({R2.id.request})
    public void onClickRequestCertification () {
        boolean result = onResultEnableDeviceModels();
        if (result) {
            doInitializeSDCard();
        }
    }

    public boolean checkEnableDeviceModel (List<String> enableDeviceModels) {
        return mPresenter.checkEnableDeviceModel(this, enableDeviceModels);
    }

    protected void makeOfflineDownloadDialog() {
        LayoutInflater inflater = getLayoutInflater();
        final View mViewPcDownload = inflater.inflate(R.layout.view_offline_authentication, null);
        TextView tvDescription = (TextView)mViewPcDownload.findViewById(R.id.license_authentication);

        try {
            String description = AssetsUtil.getTestDataFromAssets(this, "license_authentication.txt");
            tvDescription.setText(description);
        } catch (IOException e) {
            LogUtil.INSTANCE.error("error", e);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.dialog_title_sdtype_a_download));
        builder.setView(mViewPcDownload, 0, 40, 0, 0);
        builder.setPositiveButton(this.getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    LogUtil.INSTANCE.info("birdgangauth", "mTreeUri : " + mTreeUri);
                    ResponseNcgEntry responseNcgEntry = mCertification.initWriteSdCard(mTreeUri);
                    LogUtil.INSTANCE.info("birdgangauth", "initPallycondSD : " + responseNcgEntry.getResultCode());
                    int resultCode = responseNcgEntry.getResultCode();

                    if (resultCode == NcgResponseCode.FAIL) {
                        Toast.makeText(SDCertificationActivity.this, getString(R.string.sd_business_logic_fail_to_init_certification), Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        onLoadNextContentView();
                    }
                } catch (Ncg2CoreException e) {
                    Toast.makeText(SDCertificationActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });
        Dialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        LogUtil.INSTANCE.info("birdganginit", "onActivityResult > requestCode : " + requestCode + " , resultCode : " + resultCode + " , resultData : " + resultData);
        if (requestCode == IntentParams.REQUEST_CODE_GET_PERMISSION_WRITE_SDCARD) {
            // 폴더 선택
            if (resultCode == -1) {
                mTreeUri = resultData.getData();
                DocumentFile pickedDir = DocumentFile.fromTreeUri(this, mTreeUri);
                if (pickedDir != null) {
                    DocumentFile pallyconFile = pickedDir.findFile(".pallyconsd");
                    if (pallyconFile != null) {
                        // 인증 진행 확인
                        makeOfflineDownloadDialog();
                        return;
                    }
                }
            }
            Toast.makeText(this, getString(R.string.business_logic_sdcard_permission_fail), Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode, resultCode, resultData);
    }

    protected Class<?> provideNextContentView () {
        return null;
    }

    protected void onLoadNextContentView () {
        CommandHandler ch = new CommandHandler();
        ch.send(new PendingLauncherHandler());
    }


    /**
     *
     * @return
     */
    protected boolean onResultEnableDeviceModels () {
        return false;
    }

    /**
     *
     */
    protected void doInitializeSDCard () {
        LogUtil.INSTANCE.info("birdganginit", "doInitializeSDCard");
        ResponseNcgEntry responseNcgEntry = null;
        try {
            responseNcgEntry = mCertification.checkNewSdCard();
            int resultCode = responseNcgEntry.getResultCode();
            LogUtil.INSTANCE.info("birdganginit", "resultCode : " + resultCode + " , responseNcgEntry.isHasPallyconsd() : " + responseNcgEntry.isHasPallyconsd() + " , responseNcgEntry.isHasNotuse() : " + responseNcgEntry.isHasNotuse());

            if (responseNcgEntry.isHasNotuse()) {
                ActivityCalls.callOpenDocumentTree(this);
            } else {
                onLoadNextContentView();
            }
        } catch (Exception e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.dialog_title_error));
            builder.setMessage(e.getMessage());
            builder.setPositiveButton(getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    finish();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }
    }

}
