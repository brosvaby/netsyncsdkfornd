package com.inka.netsync.common;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.widget.Toast;

import com.inka.netsync.common.utils.AndroidUtil;
import com.inka.netsync.common.utils.MediaUtil;
import com.inka.netsync.common.utils.MimeTypeUtil;
import com.inka.netsync.logs.LogUtil;
import com.inka.netsync.provider.GenericFileProvider;

import java.io.File;

public class ActivityCalls {

    private static final String TAG = "ActivityCalls";

    public static void callOpenDocumentTree (Activity activity) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        intent.putExtra("android.content.extra.SHOW_ADVANCED", true);
        activity.startActivityForResult(intent, IntentParams.REQUEST_CODE_GET_PERMISSION_WRITE_SDCARD);
    }

    public static void callOpenChooser (Activity activity, String filepath) {
        boolean isNougatOrLater = AndroidUtil.isNougatOrLater();
        if (isNougatOrLater) {
            callOpenChooserForN(activity, filepath);
        } else {
            callOpenChooserM(activity, filepath);
        }
    }

    public static void callOpenChooserM (Activity activity, String filepath) {
        File file = new File(filepath);
        Intent target = new Intent(Intent.ACTION_VIEW);
        target.setDataAndType(Uri.fromFile(file),"application/pdf");
        target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

        Intent intent = Intent.createChooser(target, "Open File");
        try {
            activity.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(activity, "PDF  파일을 보기 위한 뷰어 앱이 없습니다." , Toast.LENGTH_SHORT).show();
        }
    }

    public static void callOpenChooserForN (Activity activity, String filepath) {
        File file = new File(filepath);
        if (!file.exists()) {
            Toast.makeText(activity, "해당 파일이 존재 하지 않습니다 : " + file.getPath() , Toast.LENGTH_SHORT).show();
            return;
        }

        LogUtil.INSTANCE.info(TAG, "callOpenChooser > filepath : " + filepath);

        try {
            Intent chooserIntent = new Intent();
            chooserIntent.setAction(Intent.ACTION_VIEW);
            String type = MimeTypeUtil.getMimeType(file.getPath(), file.isDirectory());
            LogUtil.INSTANCE.info(TAG, "callOpenChooser > FileProvider > type : " + type);

            Uri uri = MediaUtil.fileToContentUri(activity, file);
            if (uri == null) {
                uri = FileProvider.getUriForFile(activity, GenericFileProvider.PROVIDER_NAME , file);
                LogUtil.INSTANCE.info(TAG, "callOpenChooser > FileProvider > uri.toString() : " + uri.toString());
            } else {
                LogUtil.INSTANCE.info(TAG, "callOpenChooser > uri.toString() : " + uri.toString());
            }

            chooserIntent.setDataAndType(uri, type);
            Intent activityIntent = chooserIntent;
            applyNewDocFlag(activityIntent);

            activity.startActivity(activityIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(activity, "PDF  파일을 보기 위한 뷰어 앱이 없습니다." , Toast.LENGTH_SHORT).show();
        }
    }

    private static void applyNewDocFlag(Intent i) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
            i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME | Intent.FLAG_ACTIVITY_RETAIN_IN_RECENTS);
        }
    }

}
