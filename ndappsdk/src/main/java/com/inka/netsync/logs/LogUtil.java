package com.inka.netsync.logs;

import android.text.TextUtils;
import android.util.Log;

import com.inka.ncg.nduniversal.ModuleConfig;

import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.File;

/**
 * Created by birdgang on 2016. 3. 29..
 */
public enum LogUtil {
    INSTANCE;

    private static final String TAG = "LogUtil";

    private File mLogFile;

    public enum Level {
        INFO,
        DEBUG,
        WARNING,
        ERROR;
    }

    private LogUtil() {
        mLogFile = null;
    }

    public void info(String tag, String message) {
        print(Level.INFO, tag, message);
    }

    public void debug(String tag, String message) {
        print(Level.DEBUG, tag, message);
    }

    public void warning(String tag, String message) {
        print(Level.WARNING, tag, message);
    }

    public void error(String tag, Exception e) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("error message :: >> " + e.getMessage());
            sb.append("\n");

            String stackTrace = ExceptionUtils.getStackTrace(e);
            sb.append(stackTrace);

            error(tag, sb.toString());
        } catch (Exception error) {}
    }

    public void report (String tag, Exception e) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("error message :: >> " + e.getMessage());
            sb.append("\n");

            String stackTrace = ExceptionUtils.getStackTrace(e);
            sb.append(stackTrace);
        } catch (Exception error) {}
    }

    public void error(String tag, String message) {
        final String errorMessage = String.format("%s: %s", tag, message);
        print(Level.ERROR, tag, errorMessage);
    }

    private void print(Level level, String tag, String message) {
        printToDevice(level, tag, message);
    }

    public void printToDevice(Level level, String tag, String message) {
        if (TextUtils.isEmpty(message)) {
            return;
        }
        switch (level) {
            case INFO:
                if (ModuleConfig.ENABLE_LOG) {
                    Log.i(tag, message);
                }
                break;
            case DEBUG:
                if (ModuleConfig.ENABLE_LOG) {
                    Log.d(tag, message);
                }
                break;
            case WARNING:
                if (ModuleConfig.ENABLE_LOG) {
                    Log.w(tag, message);
                }
                break;
            case ERROR:
                Log.e(tag, message);
                break;
            default:
                break;
        }
    }

    @SuppressWarnings("unused")
    private String getString(Throwable throwable) {
        final StringBuilder builder = new StringBuilder();
        try {
            builder.append(throwable.toString());
            builder.append('\n');
            for (StackTraceElement element : throwable.getStackTrace()) {
                builder.append(element);
                builder.append('\n');
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return builder.toString();
    }


}
