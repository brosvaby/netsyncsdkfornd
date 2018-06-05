package com.inka.netsync.logs;

import android.os.Environment;
import android.util.Log;

import com.inka.ncg.nduniversal.ModuleConfig;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public enum FileLogUtil {
    INSTANCE;

    public final String SERVER_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private final String prefixFileInfo = "inka_nd_log_info.txt";

    private File mLogFileInfo;

    private SimpleDateFormat serverDateTimeFormat;

    public enum Level {
        INFO,
        DEBUG,
        WARNING,
        ERROR;
    }

    private FileLogUtil() {
        serverDateTimeFormat = new SimpleDateFormat(SERVER_DATE_TIME_FORMAT);
    }

    public void write (String message) {
        if (ModuleConfig.ENABLE_SAVE_LOG_FILE) {
            printToFile(Level.INFO, message);
        }
    }

    public void report (String message) {
        if (ModuleConfig.ENABLE_SAVE_LOG_FILE) {
            printToFile(Level.ERROR, message);
        }
    }

    private void printToFile(Level level, String message) {
        if (StringUtils.isBlank(message)) {
            return;
        }

        if (null == serverDateTimeFormat) {
            serverDateTimeFormat = new SimpleDateFormat(SERVER_DATE_TIME_FORMAT);
        }

        String dateString = serverDateTimeFormat.format(new Date());
        Log.i("birdgangfilelog" , "printToFile > dateString : " + dateString + " , message : " + message);

        switch (level) {
            case INFO:
                if (mLogFileInfo == null) {
                    mLogFileInfo = new File(Environment.getExternalStorageDirectory(), prefixFileInfo);
                }
                break;
            case DEBUG:
                break;
            case WARNING:
                break;
            case ERROR:
                break;
            default:
                break;
        }

        try {
            final BufferedWriter writer = new BufferedWriter(new FileWriter(mLogFileInfo, true));
            writer.write(String.format("[%s] %s %s", dateString, level.name(), message));
            writer.newLine();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
