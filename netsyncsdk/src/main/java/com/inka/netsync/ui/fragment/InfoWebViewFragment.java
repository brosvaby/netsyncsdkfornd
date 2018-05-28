package com.inka.netsync.ui.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.inka.netsync.BaseConfigurationPerSite;
import com.inka.netsync.R;
import com.inka.netsync.R2;
import com.inka.netsync.logs.LogUtil;
import com.inka.netsync.ui.DrawerActivity;
import com.inka.netsync.ui.mvppresenter.BaseWebViewMvpPresenter;
import com.inka.netsync.ui.mvpview.BaseWebViewMvpView;
import com.inka.netsync.view.web.InfoOnLineWebViewClient;
import com.inka.netsync.view.web.WebViewJavaScriptInterface;
import com.inka.netsync.view.web.callback.InfoWebViewClientCallback;

import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by birdgang on 2017. 4. 18..
 */

public class InfoWebViewFragment extends BaseFragment implements BaseWebViewMvpView {

    @Inject
    BaseWebViewMvpPresenter<BaseWebViewMvpView> mPresenter;

    @BindView(R2.id.web_view)
    protected WebView mWebView = null;

    protected InfoOnLineWebViewClient mInfoOnLineWebViewClient = null;
    protected WebChromeClient mWebChromeClient = null;

    protected boolean mIsWideViewPort = false;

    protected MenuItem mMenuItemReflesh;
    protected MenuItem mMenuItemSearch;
    protected MenuItem mMenuItemOverflow;
    protected MenuItem mMenuItemSort;

    public static InfoWebViewFragment newInstance(String fragmentTag_key, String fragmentTag, String fragmentName_key, String fragmentName) {
        InfoWebViewFragment newFragment = new InfoWebViewFragment();
        Bundle args = new Bundle();
        args.putString(fragmentTag_key, fragmentTag);
        args.putString(fragmentName_key, fragmentName);
        newFragment.setArguments(args);
        return newFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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
        mInfoOnLineWebViewClient = new InfoOnLineWebViewClient(getActivity(), mWebView, mInfoWebViewClientCallback, mWebViewJavaScriptInterface);

        mInfoOnLineWebViewClient.initData(BaseConfigurationPerSite.getInstance().getHelpUrl(), BaseConfigurationPerSite.getInstance().getHelpUrl());
        mWebChromeClient = mInfoOnLineWebViewClient.createCustomChromeClient();

        mWebView.setWebViewClient(mInfoOnLineWebViewClient);
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


    /**
     * menu XML 을 inflate 하여 메뉴 항목을 생성한다.
     * menu event 관련 첫번째로 호출
     */
    @Override
    public void onCreateOptionsMenu(final Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        mMenuItemReflesh = menu.findItem(R.id.action_reflesh);
        mMenuItemSearch = menu.findItem(R.id.action_search);
        mMenuItemOverflow = menu.findItem(R.id.action_overflow);
        mMenuItemSort = menu.findItem(R.id.action_sort);

        if (((DrawerActivity)getActivity()).isDrawerOpen() == false) {
            inflater.inflate(R.menu.menu_mainscreen, menu);
            restoreActionBar();
        }
    }

    /**
     * inflate 된 menu 를 show or hide 및 icon 설정 등을 통해 세부 셋팅을 한다.
     * menu event 관련 두번째로 호출
     */
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        mMenuItemReflesh = menu.findItem(R.id.action_reflesh);
        mMenuItemSearch = menu.findItem(R.id.action_search);
        mMenuItemOverflow = menu.findItem(R.id.action_overflow);
        mMenuItemSort = menu.findItem(R.id.action_sort);

        FragmentActivity activity = getActivity();
        if (activity instanceof DrawerActivity) {
            if (((DrawerActivity)activity).isDrawerOpen()) {
                return;
            }
        }

        mMenuItemReflesh.setVisible(false);
        mMenuItemSort.setVisible(false);
        mMenuItemSearch.setVisible(false);
        mMenuItemOverflow.setVisible(false);
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
        }
    };

    // Interface
    private WebViewJavaScriptInterface mWebViewJavaScriptInterface = new WebViewJavaScriptInterface() {
        @Override
        public void onSetHtmlTitleMessage(String title) {
        }
    };

}