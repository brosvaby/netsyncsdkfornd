package com.inka.netsync.view.dialog;

import android.app.AlertDialog;
import android.content.Context;

import com.inka.netsync.R;

public class SimpleMessageDialog {
	private Context mContext = null;
	private String	mMessage = "";
	private String  mTitle = "";
	
	public SimpleMessageDialog(Context context, String title, String message) {
		mContext = context;
		mMessage = message;
		mTitle = title;
	}
	
	public void show() {
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle(mTitle);
		builder.setMessage(mMessage);
		builder.setPositiveButton(mContext.getString(R.string.dialog_ok), null);
		AlertDialog dialog = builder.create();
		dialog.setCancelable(false);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
	}
}
