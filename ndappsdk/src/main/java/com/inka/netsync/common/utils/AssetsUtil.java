package com.inka.netsync.common.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;

import com.inka.netsync.logs.LogUtil;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by birdgang on 2017. 4. 26..
 */

public final class AssetsUtil {

    // asset 에서 내용 가져오기
    public final static String getTestDataFromAssets(Context context, String name) throws IOException {
        InputStream testDataInputStream = context.getAssets().open(name);

        BufferedReader in = new BufferedReader(new InputStreamReader(testDataInputStream));
        StringBuffer buffer = new StringBuffer();
        int c;
        while ((c = in.read()) != -1) {
            buffer.append((char) c);
        }
        in.close();
        String testData = buffer.toString();
        return testData;
    }


    public final static void copyFileFromAssets(Context context, String assetFile, String targetFile) {
        try {
            final int BUF_SIZE = 8 * 1024;
            // Dex 설정
            final File fileTarget = new File(targetFile);
            if (fileTarget.exists() != true) {
                // Buffered 를 이용한 파일 Input
                InputStream is = context.getAssets().open(assetFile);
                BufferedInputStream bis = new BufferedInputStream(is);
                // Buffered 를 이용한 파일 Output
                FileOutputStream fos = new FileOutputStream(fileTarget);
                BufferedOutputStream bos = new BufferedOutputStream(fos);
                byte[] buf = new byte[BUF_SIZE];
                int len = 0;
                while ((len = bis.read(buf, 0, BUF_SIZE)) > 0) {
                    bos.write(buf, 0, len);
                }

                bis.close();
                is.close();
                bos.close();
                fos.close();
            }
        } catch (Exception e) {
            LogUtil.INSTANCE.error("error", e);
        }
    }


    public static int getColor(Context context, int id) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= Build.VERSION_CODES.M) {
            return context.getColor(id);
        } else {
            return context.getResources().getColor(id);
        }
    }


    public static Drawable getDrawable(Context context, int id) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= Build.VERSION_CODES.LOLLIPOP) {
            return context.getDrawable(id);
        } else {
            return context.getResources().getDrawable(id);
        }
    }

}
