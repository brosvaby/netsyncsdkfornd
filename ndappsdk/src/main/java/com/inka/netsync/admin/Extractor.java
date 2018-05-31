package com.inka.netsync.admin;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;

import com.inka.netsync.logs.LogUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * Created by birdgang on 2018. 4. 26..
 */

public class Extractor {

    private final String TAG = "Extract";

    private Context context;

    private final String EXTRACT_DIR = "extract_database";
    private final String DB = "database";
    private final String DIVIDER = "/";

    private String EXTERNAL_STORAGE_PATH = "";
    private String BASE_EXTERNAL_STORAGE_DIR = "";
    private String EXTERNAL_DB_PATH = "";

    static volatile Extractor defaultInstance;

    public static Extractor getDefault() {
        if (defaultInstance == null) {
            synchronized (Extractor.class) {
                if (defaultInstance == null) {
                    defaultInstance = new Extractor();
                }
            }
        }
        return defaultInstance;
    }

    private Extractor () {
        EXTERNAL_STORAGE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
        BASE_EXTERNAL_STORAGE_DIR = EXTERNAL_STORAGE_PATH + DIVIDER + EXTRACT_DIR + DIVIDER;
        EXTERNAL_DB_PATH = BASE_EXTERNAL_STORAGE_DIR + DB + DIVIDER;
    }

    /**
     *
     * @param context
     * @param databaseLocation
     * @param resultLocation
     */
    public void extractDatabaseFile (final Context context, final String databaseLocation, final String resultLocation) {
        this.context = context;

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (!TextUtils.isEmpty(databaseLocation)) {
                    File f = new File(databaseLocation);
                    boolean create = createDirectory(EXTERNAL_DB_PATH);
                    LogUtil.INSTANCE.info(TAG, "f.canRead() :" + f.canRead() + ", create :" + create);

                    if (create) {
                        fileCopy(databaseLocation, EXTERNAL_DB_PATH + resultLocation + "_" + EXTRACT_DIR + ".db");
                    }
                }
            }
        }).start();
    }

    /**
     *
     * @param path
     * @return
     */
    private boolean createDirectory (String path) {
        boolean root = makeDir(BASE_EXTERNAL_STORAGE_DIR);
        LogUtil.INSTANCE.info(TAG, "root :" + root + ", path :" + path);
        if (root) {
            boolean dir = makeDir(path);
            LogUtil.INSTANCE.info(TAG, "dir :" + dir + " , ");
            return dir;
        }
        return false;
    }


    /**
     *
     * @param path
     * @return
     */
    private boolean makeDir(String path) {
        try {
            File file = new File(path);
            if (!file.exists()) {
                boolean success = file.mkdir();
                return success;
            } else {
                return true;
            }
        } catch (Exception e) {
            LogUtil.INSTANCE.error(getClass().getSimpleName(), e);
            return false;
        }
    }


    /**
     * 파일을 복사하는 메소드
     * @param inFileName
     * @param outFileName
     */
    public void fileCopy(String inFileName, String outFileName) {
        LogUtil.INSTANCE.info(TAG, "inFileName :" + inFileName + ", outFileName :" + outFileName);
        try {
            if (new File(inFileName).exists()) {
                FileChannel src = new FileInputStream(inFileName).getChannel();
                FileChannel dst = new FileOutputStream(outFileName).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                final Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                final Uri contentUri = Uri.fromFile(new File(outFileName));
                scanIntent.setData(contentUri);
                context.sendBroadcast(scanIntent);
            } else {
                final Intent intent = new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory()));
                context.sendBroadcast(intent);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
