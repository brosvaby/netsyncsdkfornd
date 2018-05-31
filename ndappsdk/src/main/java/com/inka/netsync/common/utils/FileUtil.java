package com.inka.netsync.common.utils;

import com.inka.netsync.logs.LogUtil;

import java.io.File;

/**
 * Created by birdgang on 2017. 4. 27..
 */

public class FileUtil {

    private static final String TAG = "FileUtil";

    private static final long SIZE_KB = 1024;
    private static final long SIZE_MB = (1024 * SIZE_KB);
    private static final long SIZE_GB = (1024 * SIZE_MB);

    public static boolean canListFiles(File f) {
        try {
            if (f.canRead() && f.isDirectory())
                return true;
            else
                return false;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean existsFile(String filePath) {
        boolean ret = false;
        File file = null;
        try {
            file = new File (filePath);
            ret = file.exists();
        } catch (Exception e) {
            LogUtil.INSTANCE.error("error", e);
        } finally {
            file = null;
        }
        return ret;
    }

    /********************************************************************************
     * filePath 의 파일을 삭제 합니다.
     * @param filePath 삭제할 파일의 로컬 경로 ( full path )
     ********************************************************************************/
    public final static boolean deleteFile( String filePath ) {
        boolean ret = false;
        File file = null;
        try {
            file = new File (filePath);
            ret = file.delete();
        } catch (Exception e) {
            LogUtil.INSTANCE.error("error", e);
        } finally {
            file = null;
        }
        return ret;
    }

    public final static String getFileSizeString(long nSize) {
        String fileSize = "";

        try {
            if (nSize <= 0) {
                fileSize = String.format("0 MB");
            } else if(nSize > SIZE_GB) {
                fileSize = String.format("%.2f GB", (float) nSize / (float) SIZE_GB);
            } else if(nSize > SIZE_MB) {
                fileSize = String.format("%.2f MB", (float) nSize / (float) SIZE_MB);
            } else if(nSize > SIZE_KB) {
                fileSize = String.format("%.2f KB", (float) nSize / (float) SIZE_KB);
            } else {
                fileSize = String.format("%d byte", nSize);
            }
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
        return fileSize;
    }

}
