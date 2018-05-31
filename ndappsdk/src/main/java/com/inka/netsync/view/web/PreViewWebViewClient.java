package com.inka.netsync.view.web;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.inka.netsync.common.utils.AndroidUtil;
import com.inka.netsync.view.web.callback.WebViewClientCallback;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by birdgang on 2018. 1. 11..
 */
public class PreViewWebViewClient extends BaseWebViewClient {

    private static final String TAG = "PreViewWebViewClient";

    protected Context mContext = null;

    public static final String INTENT_PROTOCOL_START = "intent:";
    public static final String INTENT_PROTOCOL_RTSP = "rtsp";
    public static final String INTENT_PROTOCOL_TEL = "tel";
    public static final String INTENT_PROTOCOL_FILE = "file";

    public static final String GOOGLE_PLAY_STORE_PREFIX = "market://details?id=";

    protected String mSchemeDownload = "";
    protected String mSchemeDownloads	= "";
    protected String mSchemePlay = "";
    protected String mSchemeRefund = "";

    private boolean mIsLoading = false;
    protected boolean mIsClicked = false;
    protected boolean mClearHistory = false;

    private WebView mWebView = null;
    private WebViewClientCallback mWebViewClientCallback = null;
    private WebViewJavaScriptInterface mWebViewJSInterfaceCallback = null;

    @SuppressLint("JavascriptInterface")
    public PreViewWebViewClient (Context context, WebView webView, WebViewClientCallback callback, WebViewJavaScriptInterface jsCallback) {
        super(context, webView, callback, jsCallback);

        mContext = context;
        mWebView = webView;
        mWebViewClientCallback = callback;
        mWebViewJSInterfaceCallback = jsCallback;
        mWebView.addJavascriptInterface(new BaseWebViewClient.InkaJavaScriptClass(), "INKA");
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
    }

    public void initData (String url, String subUrl) {
        mStrSubHomeUrl = subUrl;
    }

    public WebChromeClient createCustomChromeClient() {
        return new CustomWebChromeClient(mContext);
    }

    public class InkaJavaScriptClass {
        private Handler event_handler = new Handler();

        public void setHtmlTitleMessage(final String title) {
            event_handler.post(new Runnable() {
                public void run() {
                    // HTML의 Title 내용을 App 상에 표시한다.
                    mWebViewJSInterfaceCallback.onSetHtmlTitleMessage(title);
                }
            });
        }
    }

    @Override
    public boolean shouldOverrideUrlLoading(final WebView view, String url) {
        Log.d(TAG, "shouldOverrideUrlLoading ++ > url : " + url);
        mIsLoading = true;

        boolean result = super.shouldOverrideUrlLoading(view, url);
        if (result) {
            return result;
        }

        String prefix = "ndwebviewresource/mainscreen";

        // 내부 html
        if (url.contains(prefix)) {
            if (url.contains(".mp4")) {
                try {
                    Uri video = Uri.parse(url);
                    String fileName = video.getLastPathSegment();
                    String targetFile = "";
                    try {
                        targetFile = AndroidUtil.getInternalSDCardMemory(mContext) + "/" + fileName;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    Uri uri = Uri.parse("file://" + targetFile);
                    i.setDataAndType(uri, "video/mpeg4");
                    mContext.startActivity(i);
                    return true;
                } catch(ActivityNotFoundException e) {
                    e.printStackTrace();
                    return true;

                } catch(Exception e) {
                    e.printStackTrace();
                    return true;
                }
            }

            view.loadUrl(url);
            return true;
        }

        return super.shouldOverrideUrlLoading(view, url);
    }


    @Override
    public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
        int keyCode = event.getKeyCode();
        Log.i("birdgangwebview", "shouldOverrideKeyEvent > keyCode : " + keyCode);
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_LEFT:
                if (view.canGoBack()) {
                    view.goBack();
                    return true;
                }
                break;

            case KeyEvent.KEYCODE_DPAD_RIGHT:
                if (view.canGoForward()) {
                    view.goForward();
                    return true;
                }

                break;
        }
        return false;
    }


    public boolean isLoading() {
        return mIsLoading;
    }

    public void setClearHistory() {
        mClearHistory = true;
    }

    protected void loadLaunchBrowser(Context context, String tempUrl) {
        Log.i("birdgangwebview", "loadLaunchBrowser > tempUrl : " + tempUrl);

        Intent it = new Intent(Intent.ACTION_VIEW);
        it.setData(Uri.parse(tempUrl));
        context.startActivity(it);
    }


    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        Log.i("birdgangwebview", "onPageStarted > url : " + url);
        mWebViewClientCallback.onUpdateScreen();
        mIsLoading 	= true;
        super.onPageStarted(view, url, favicon);
    }


    @Override
    public void onPageFinished(WebView view, String url) {
        String subHomeUrl = mStrSubHomeUrl;

        if (StringUtils.equals(url, subHomeUrl)) {
            mWebView.clearHistory();
        }

        // menu 에서 직접 들어와 home 이 변동된 경우
        if (mClearHistory) {
            mWebView.clearHistory();
            mClearHistory = false;
        }

        mWebViewClientCallback.onUpdateScreen();
        mIsLoading 	= false;
        super.onPageFinished(view, url);
    }


    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        super.onReceivedError(view, errorCode, description, failingUrl);
        Log.i("birdgangwebview", "onReceivedError > failingUrl : " + failingUrl + " , errorCode : " + errorCode);
        if (failingUrl.equals(mStrSubHomeUrl) == true) {
            return;
        }

        failingUrl = mStrSubHomeUrl;
        if (failingUrl.equals("") == true) {
            mWebViewClientCallback.onShowRefreshPage();
        } else {
            view.loadUrl(failingUrl);
        }
    }

}
