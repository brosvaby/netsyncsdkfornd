package com.inka.netsync.command;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public class CommandHandler {

	private static Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.obj instanceof Command) {
                ((Command) msg.obj).execute();
            }
        }
    };

    private static Handler handlerOnMainLooper = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            if (msg.obj instanceof Command) {
                ((Command) msg.obj).execute();
            }
        }
    };
    
    public void send(Command command) {
        Message message = handler.obtainMessage();
        message.obj = command;
        handler.sendMessage(message);
    }

    public void sendForMainLooper (Command command) {
        Message message = handlerOnMainLooper.obtainMessage();
        message.obj = command;
        handlerOnMainLooper.sendMessage(message);
    }
    
    public void sendForDelay (Command command){
    	Message message = handler.obtainMessage();
        message.obj = command;
        handler.sendMessageDelayed(message, 500);
    }
    
    public void increase (int num) {
    	Message message = handler.obtainMessage();
        message.what = num;
        handler.sendMessage(message);
    }
}
