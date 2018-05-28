package com.inka.netsync.view.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import com.inka.netsync.R;

public class SingleAlertDialog {

    private final String TAG = "SingleAlertDialog";

    static volatile SingleAlertDialog defaultInstance;

    private Dialog dialog;

    public static SingleAlertDialog getDefault() {
        if (defaultInstance == null) {
            synchronized (SingleAlertDialog.class) {
                if (defaultInstance == null) {
                    defaultInstance = new SingleAlertDialog();
                }
            }
        }
        return defaultInstance;
    }

    private SingleAlertDialog () {}

    public void goForceHomeMessageDialog (Context context, DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getResources().getString(R.string.dialog_title_sd_not_exist_sdcard));
        builder.setMessage(context.getResources().getString(R.string.dialog_description_sd_not_exist_sdcard));
        builder.setPositiveButton(context.getString(R.string.dialog_ok), onClickListener);

        if (null == dialog) {
            dialog = builder.create();
        }

        boolean isShowing = dialog.isShowing();
        Log.i(TAG, "goForceHomeMessageDialog > isShowing : " + isShowing);
        if (!isShowing) {
            dialog.show();
        }
    }


    public void showDialog(Context context, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(context.getString(R.string.dialog_ok), null);

        if (null == dialog) {
            dialog = builder.create();
            dialog.setCanceledOnTouchOutside(false);
        }

        boolean isShowing = dialog.isShowing();
        Log.i(TAG, "goForceHomeMessageDialog > isShowing : " + isShowing);
        if (!isShowing) {
            dialog.show();
        }
    }

}
