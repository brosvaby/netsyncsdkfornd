package com.inka.netsync.view.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

/**
 * Created by birdgang on 2018. 1. 12..
 */

public class CommonAlertDialog {

    private Context context;

    private AlertDialog builder;

    public CommonAlertDialog (Context context) {
        this.context = context;
    }

    public interface OnAlertClickListener {
        public void onClick(CommonAlertDialog alertDialog);
    }

    public AlertDialog getAlertDialog(final String[] permissions, final String permissionName, final OnAlertClickListener alertClickListener) {
        if (builder == null) {
            builder = new AlertDialog.Builder(context).setTitle("Permission Needs Explanation").create();
        }
        builder.setButton(DialogInterface.BUTTON_POSITIVE, "Request", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertClickListener.onClick(CommonAlertDialog.this);
            }
        });
        builder.setMessage("Permissions need explanation (" + permissionName + ")");
        return builder;
    }

    public AlertDialog getAlertDialog(final String permission, final OnAlertClickListener alertClickListener) {
        if (builder == null) {
            builder = new AlertDialog.Builder(context).setTitle("Permission Needs Explanation").create();
        }
        builder.setButton(DialogInterface.BUTTON_POSITIVE, "Request", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertClickListener.onClick(CommonAlertDialog.this);
            }
        });
        builder.setMessage("Permission need explanation (" + permission + ")");
        return builder;
    }



}
