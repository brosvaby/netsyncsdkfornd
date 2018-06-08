package com.inka.netsync.sd.ui.fragment;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.inka.netsync.BaseConfigurationPerSite;
import com.inka.netsync.R;
import com.inka.netsync.R2;
import com.inka.netsync.common.utils.NetworkUtils;
import com.inka.netsync.logs.LogUtil;
import com.inka.netsync.sd.ui.mvppresenter.SDInfoMvpPresenter;
import com.inka.netsync.sd.ui.mvpview.SDInfoMvpView;
import com.inka.netsync.ui.fragment.BaseFragment;
import com.inka.netsync.view.web.InfoOffWebViewClient;
import com.inka.netsync.view.web.WebViewJavaScriptInterface;
import com.inka.netsync.view.web.callback.InfoWebViewClientCallback;

import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by birdgang on 2017. 4. 18..
 */
public class SDInfoFragment extends BaseFragment implements SDInfoMvpView {

    @Inject
    SDInfoMvpPresenter<SDInfoMvpView> mPresenter;

    @BindView(R2.id.web_view)
    protected WebView mWebView = null;

    protected InfoOffWebViewClient mBaseWebViewClient = null;
    protected WebChromeClient mWebChromeClient = null;

    protected boolean mIsWideViewPort = false;

    protected String mSchemeDownload = "";
    protected String mSchemeDownloads = "";
    protected String mSchemePlay = "";
    protected String mSchemeRefund = "";

    protected boolean mMenuRefresh = true;


    public static SDInfoFragment newInstance(String fragmentTag_key, String fragmentTag, String fragmentName_key, String fragmentName) {
        SDInfoFragment newFragment = new SDInfoFragment();
        Bundle args = new Bundle();
        args.putString(fragmentTag_key, fragmentTag);
        args.putString(fragmentName_key, fragmentName);
        newFragment.setArguments(args);
        return newFragment;
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void onCreate(Bundle savedInstanceState, String shemeDownload, String shemeDownloads, String shemePlay, String shemeRefund) {
        super.onCreate(savedInstanceState);
        mSchemeDownload = shemeDownload;
        mSchemeDownloads = shemeDownloads;
        mSchemePlay = shemePlay;
        mSchemeRefund = shemeRefund;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_webview, container, false);
        view.setBackgroundColor(getActivity().getResources().getColor(R.color.color_webview_main));

        getActivityComponent().inject(this);
        setUnBinder(ButterKnife.bind(this, view));
        mPresenter.onAttach(this);

        setUp(view);
        return view;
    }

    @Override
    public boolean onBackPressed() {
        return true;
    }

    @Override
    public void setUp(View view) {
        mBaseWebViewClient = new InfoOffWebViewClient(getActivity(), mWebView, mInfoWebViewClientCallback, mWebViewJavaScriptInterface);

        mBaseWebViewClient.initData(BaseConfigurationPerSite.getInstance().getHelpUrl(), BaseConfigurationPerSite.getInstance().getHelpUrl());
        mWebChromeClient = mBaseWebViewClient.createCustomChromeClient();

        mWebView.setWebViewClient(mBaseWebViewClient);
        mWebView.setWebChromeClient(mWebChromeClient);
        mWebView.requestFocus(View.FOCUS_DOWN);

        WebSettings set = mWebView.getSettings();
        set.setJavaScriptEnabled(true);
        set.setSupportMultipleWindows(true);

        set.setDefaultTextEncodingName("UTF-8");
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            set.setTextZoom(100);
        }

        if (mIsWideViewPort == true) {
            set.setUseWideViewPort(true);
            set.setLoadWithOverviewMode(true);
        }

        loadHomeUrl();
    }


    @Override
    public void refleshContents() {
        LogUtil.INSTANCE.info("birdgangwebview" , "WebViewClientCallback > refleshContents()");
    }

    @Override
    public void onScanStarted() {
    }

    @Override
    public void onScaning() {
    }

    @Override
    public void onScanCompleated() {
    }

    @Override
    public void onSDcardMountedEvent(String externalPath) {
    }

    @Override
    public void onSDcardEjectedEvent() {
    }


    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        LogUtil.INSTANCE.info("birdgangwebview" , "onViewStateRestored");
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        LogUtil.INSTANCE.info("birdgangwebview" , "onSaveInstanceState");
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        LogUtil.INSTANCE.info("birdgangwebview" , "onConfigurationChanged");
        super.onConfigurationChanged(newConfig);
    }

    public void loadHomeUrl() {
        String homeUrl = BaseConfigurationPerSite.getInstance().getHelpUrl();
        LogUtil.INSTANCE.info("birdgangwebview" , "homeUrl : " + homeUrl);
        try {
            if (StringUtils.isNotBlank(homeUrl)) {
                mWebView.loadUrl(homeUrl);
                mInfoWebViewClientCallback.onUpdateScreen();
            }
        } catch (Exception e) {
            LogUtil.INSTANCE.error("error", e);
        }

    }


    protected InfoWebViewClientCallback mInfoWebViewClientCallback = new InfoWebViewClientCallback() {
        @Override
        public void onUpdateScreen() {
            LogUtil.INSTANCE.info("birdgangwebview" , "WebViewClientCallback > onUpdateScreen()");
            try {
                restoreActionBar();
            } catch (Exception e) {
                LogUtil.INSTANCE.error("birdgangerror", e);
            }
        }
    };


    // Interface
    private WebViewJavaScriptInterface mWebViewJavaScriptInterface = new WebViewJavaScriptInterface() {
        @Override
        public void onSetHtmlTitleMessage(String title) {
        }
    };

    @SuppressLint("NewApi")
    public void setVisibleRefrsh(boolean value) {
        mMenuRefresh = value;
        getActivity().invalidateOptionsMenu();
    }

    public void reload() {
        if (NetworkUtils.isNetworkConnected(getActivity())) {
            mWebView.reload();
            mInfoWebViewClientCallback.onUpdateScreen();
        }
    }

    public void setClearHistory() {
        if (null != mBaseWebViewClient) {
            mBaseWebViewClient.setClearHistory();
        }
    }

    public void loadUrl(String url) {
        LogUtil.INSTANCE.info("birdgangwebview" , "loadUrl > url : " + url);

        if (!NetworkUtils.isNetworkConnected(getActivity())) {
            mWebView.loadUrl(url);
        } else {
            mWebView.loadUrl(url);
        }
    }

}