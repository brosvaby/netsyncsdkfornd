package com.inka.netsync.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;

import com.inka.netsync.common.AppConstants;
import com.inka.netsync.logs.LogUtil;

public class EarPhoneChangeReceiver extends BroadcastReceiver {

    public static Handler mPlayerHandler = null;
    public static Context mContext = null;

    public static void setHandler(Handler handler, Context context) {
        mPlayerHandler = handler;
        mContext = context;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            String intentAction = intent.getAction();
            if (!Intent.ACTION_MEDIA_BUTTON.equals(intentAction)) {
                return;
            }
            KeyEvent event = intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
            if (event == null) {
                return;
            }
            int action = event.getAction();
            if (action == KeyEvent.ACTION_DOWN) {
                Message msg = new Message();
                msg.what = AppConstants.MESSAGE_EARPHONECHANGE_HANDLER_REMOTE_CLICKED;
                mPlayerHandler.sendMessage(msg);
            }
            abortBroadcast();
        } catch (Exception e) {
            LogUtil.INSTANCE.error("error", e);
        }
    }
}