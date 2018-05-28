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
import android.webkit.WebViewClient;

import com.inka.netsync.view.web.callback.WebViewClientCallback;
import com.inka.netsync.view.web.common.IntentAction;

import org.apache.commons.lang3.StringUtils;

import java.net.URISyntaxException;

public abstract class BaseWebViewClient extends WebViewClient {

    private static final String TAG = "BasicWebViewClient";

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

    protected String mStrHomeUrl;
    protected String mStrSubHomeUrl;

    private boolean mIsLoading = false;
    protected boolean mIsClicked = false;
    protected boolean mClearHistory = false;

    private WebView mWebView = null;
    private WebViewClientCallback mWebViewClientCallback = null;
    private WebViewJavaScriptInterface mWebViewJSInterfaceCallback = null;

    @SuppressLint("JavascriptInterface")
    public BaseWebViewClient (Context context, WebView webView, WebViewClientCallback callback, WebViewJavaScriptInterface jsCallback) {
        mContext = context;
        mWebView = webView;
        mWebViewClientCallback = callback;
        mWebViewJSInterfaceCallback = jsCallback;
        mWebView.addJavascriptInterface(new InkaJavaScriptClass(), "INKA");
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
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

        if (url.startsWith(INTENT_PROTOCOL_START)) {
            String packageName = "";
            try {
                Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                packageName = intent.getPackage();
                mContext.startActivity(intent);
                return true;

            } catch (URISyntaxException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return true;

            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
                mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(GOOGLE_PLAY_STORE_PREFIX + packageName)));
                return true;
            }
        }

        // RTSP를 처리해주는 부분
        if ( url.startsWith("rtsp")) {
            Intent it = new Intent(Intent.ACTION_VIEW);
            it.setData(Uri.parse(url));
            mContext.startActivity(it);
            return true;
        }

        // TEL를 처리해주는 부분
        if ( url.startsWith("tel")) {
            Intent it = new Intent(Intent.ACTION_VIEW);
            it.setData(Uri.parse(url));
            mContext.startActivity(it);
            return true;
        }

        // New Launch Browser
        if(url.toLowerCase().contains("%26pallyconlaunchbrowser") == true) {
            String tempUrl = url.replace("%26pallyconlaunchbrowser", "");
            loadLaunchBrowser(mContext, tempUrl);
            return true;
        }

        // New Launch Browser
        if(url.toLowerCase().contains("&pallyconlaunchbrowser") == true) {
            String tempUrl = url.replace("&pallyconlaunchbrowser", "");
            loadLaunchBrowser(mContext, tempUrl);
            return true;
        }

        // New Launch Browser
        if (StringUtils.contains(url, IntentAction.ACTION_MOVE_MY_FOLDER)) {
            mWebViewClientCallback.onMoveMyFolder();
            return true;
        }

        if (StringUtils.contains(url, IntentAction.ACTION_MOVE_CONTENT)) {
            Intent it1 = new Intent(Intent.ACTION_VIEW);
            it1.setData(Uri.parse(url));
            try {
                Intent intent1 = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                Uri uri = intent1.getData();
                String index = uri.getQueryParameter("index");
                mWebViewClientCallback.onMoveContent(Integer.valueOf(index));
            } catch (Exception e) {
                e.printStackTrace();
            }

            return true;
        }

        if (StringUtils.contains(url, IntentAction.ACTION_MOVE_PLAYED_LIST)) {
            mWebViewClientCallback.onMovePlayedList();
            return true;
        }

        if (StringUtils.contains(url, IntentAction.ACTION_MOVE_FAVORITE_LIST)) {
            mWebViewClientCallback.onMoveFavorite();
            return true;
        }

        if (StringUtils.contains(url, IntentAction.ACTION_MOVE_SETTING)) {
            mWebViewClientCallback.onMoveSetting();
            return true;
        }

        if (StringUtils.contains(url, IntentAction.ACTION_MOVE_3RDPARTY_APP)) {
            mWebViewClientCallback.onMove3rdpartyapp();
            return true;
        }

        boolean result = super.shouldOverrideUrlLoading(view, url);
        return result;
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
        super.onPageFinished(view, url);
    }


    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        super.onReceivedError(view, errorCode, description, failingUrl);
    }


    public abstract void initData (String url, String subUrl);

}