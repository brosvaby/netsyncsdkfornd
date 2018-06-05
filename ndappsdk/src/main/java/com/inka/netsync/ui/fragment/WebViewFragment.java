package com.inka.netsync.ui.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebBackForwardList;
import android.webkit.WebChromeClient;
import android.webkit.WebHistoryItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;

import com.inka.netsync.BaseConfigurationPerSite;
import com.inka.netsync.R;
import com.inka.netsync.R2;
import com.inka.netsync.common.utils.NetworkUtils;
import com.inka.netsync.data.cache.pref.PreferencesCacheHelper;
import com.inka.netsync.logs.LogUtil;
import com.inka.netsync.ui.DrawerActivity;
import com.inka.netsync.ui.mvppresenter.BaseWebViewMvpPresenter;
import com.inka.netsync.ui.mvpview.BaseWebViewMvpView;
import com.inka.netsync.view.web.BaseWebViewClient;
import com.inka.netsync.view.web.OffLineWebViewClient;
import com.inka.netsync.view.web.OnLineWebViewClient;
import com.inka.netsync.view.web.WebViewJavaScriptInterface;
import com.inka.netsync.view.web.callback.WebViewClientCallback;

import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by birdgang on 2017. 4. 18..
 */
public class WebViewFragment extends BaseFragment implements BaseWebViewMvpView {

    @Inject
    BaseWebViewMvpPresenter<BaseWebViewMvpView> mPresenter;

    @BindView(R2.id.web_view)
    protected WebView mWebView = null;

    protected BaseWebViewClient mBaseWebViewClient = null;
    protected WebChromeClient mWebChromeClient = null;

    protected String mSchemeDownload = "";
    protected String mSchemeDownloads = "";
    protected String mSchemePlay = "";
    protected String mSchemeRefund = "";

    protected boolean mMenuRefresh = true;

    protected boolean mEnableOptionMenus = false;

    protected MenuItem mMenuItemReflesh;
    protected MenuItem mMenuItemSearch;
    protected MenuItem mMenuItemOverflow;
    protected MenuItem mMenuItemSort;

    public static WebViewFragment newInstance(String fragmentTag_key, String fragmentTag, String fragmentName_key, String fragmentName) {
        WebViewFragment newFragment = new WebViewFragment();
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

    public void onCreate(Bundle savedInstanceState, String shemeDownload, String shemeDownloads, String shemePlay, String shemeRefund) {
        super.onCreate(savedInstanceState);
        mSchemeDownload = shemeDownload;
        mSchemeDownloads = shemeDownloads;
        mSchemePlay = shemePlay;
        mSchemeRefund = shemeRefund;

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
        boolean isNetworkConncted = NetworkUtils.isNetworkConnected(getActivity());
        if (isNetworkConncted) {
            mBaseWebViewClient = new OnLineWebViewClient(getActivity(), mWebView, mWebViewClientCallback, mWebViewJavaScriptInterface);
        } else {
            mBaseWebViewClient = new OffLineWebViewClient(getActivity(), mWebView, mWebViewClientCallback, mWebViewJavaScriptInterface);
        }

        mBaseWebViewClient.initData(BaseConfigurationPerSite.getInstance().getHomeUrl(), BaseConfigurationPerSite.getInstance().getSubHomeUrl());
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

        set.setUseWideViewPort(true);
        set.setLoadWithOverviewMode(true);

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
            mEnableOptionMenus = true;
        } else {
            mEnableOptionMenus = false;
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

        mMenuItemSort.setVisible(false);
        mMenuItemOverflow.setVisible(false);
        mMenuItemSearch.setVisible(false);

        if (mEnableOptionMenus) {
            mMenuItemReflesh.setVisible(true);
        } else {
            mMenuItemReflesh.setVisible(false);
        }
    }


    /**
     * menu item 이 선택 되었을때 발생한다.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_search) {
            requestSearchView();
        }
        else if (itemId == R.id.action_reflesh) {
            loadHomeUrl();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void restoreActionBar() {
        super.restoreActionBar();
        try {
            ActionBar actionBar = getActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
        } catch (Exception e) {
            LogUtil.INSTANCE.error("birdgangerror", e);
        }
    }


    @Override
    public void refleshContents() {
        LogUtil.INSTANCE.info("birdgangwebview" , "WebViewClientCallback > refleshContents()");
    }

    @Override
    public void onScanStarted() {}

    @Override
    public void onScaning() {}

    @Override
    public void onScanCompleated() {}

    @Override
    public void onSDcardMountedEvent(String externalPath) {}

    @Override
    public void onSDcardEjectedEvent() {}

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
        LogUtil.INSTANCE.info("birdgangwebview" , "WebViewClientCallback > loadHomeUrl()");
        boolean isNetworkConnected = NetworkUtils.isNetworkConnected(getActivity());
        if (!isNetworkConnected) {
            String subHomeUrl = BaseConfigurationPerSite.getInstance().getSubHomeUrl();
            LogUtil.INSTANCE.info("birdgangwebview" , "subHomeUrl : " + subHomeUrl + " , isNetworkConnected : " + isNetworkConnected);
            if (StringUtils.isBlank(subHomeUrl)) {
                mWebViewClientCallback.onShowRefreshPage();
            } else {
                mWebView.loadUrl(subHomeUrl);
            }
        } else {
            String homeUrl = BaseConfigurationPerSite.getInstance().getHomeUrl();
            LogUtil.INSTANCE.info("birdgangwebview" , "homeUrl : " + homeUrl + " , isNetworkConnected : " + isNetworkConnected);
            // resource 에 homeurl 이 없는 경우 (하이브리드 테스트)
            try {
                if (StringUtils.isBlank(homeUrl)) {
                    LayoutInflater inflater = getActivity().getLayoutInflater();
                    final View viewInputHomeUrl = inflater.inflate(R.layout.dialog_input_homeurl, null);
                    final EditText editHomeUrl = (EditText)viewInputHomeUrl.findViewById(R.id.edit_homeurl);

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle(getActivity().getString(R.string.dialog_title_input_homeurl));
                    builder.setView(viewInputHomeUrl, 0, 40, 0, 0);
                    //Positive Button
                    builder.setPositiveButton(getActivity().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (editHomeUrl != null) {
                                String homeUrl = editHomeUrl.getText().toString().trim();
                                PreferencesCacheHelper.setPreferenceValue(getString(R.string.PREF_KEY_HOMEURL), homeUrl);

                                mWebView.loadUrl(homeUrl);
                                mWebViewClientCallback.onShowWebViewPage();
                            }
                        }
                    });
                    builder.setNegativeButton(getActivity().getString(R.string.dialog_cancel), null);
                    Dialog dialog = builder.create();

                    if (editHomeUrl != null) {
                        homeUrl = PreferencesCacheHelper.getPreferenceValue(getString(R.string.PREF_KEY_HOMEURL), StringUtils.EMPTY);
                        if (StringUtils.isBlank(homeUrl)) {
                            editHomeUrl.setText("http://");
                        } else {
                            editHomeUrl.setText(homeUrl);
                        }
                        editHomeUrl.setSelection(editHomeUrl.getText().length());
                    }

                    dialog.show();
                    // resource 에 homeurl 이 있는 경우 (하이브리드 테스트)
                } else {
                    LogUtil.INSTANCE.info("birdgangwebview" , "homeUrl : " + homeUrl);
                    mWebView.loadUrl(homeUrl);
                    mWebViewClientCallback.onShowWebViewPage();
                }
            } catch (Exception e) {
                LogUtil.INSTANCE.error("error", e);
            }
        }
    }


    protected WebViewClientCallback mWebViewClientCallback = new WebViewClientCallback() {
        @Override
        public void onUpdateScreen() {
            LogUtil.INSTANCE.info("birdgangwebview" , "WebViewClientCallback > onUpdateScreen()");
            try {
                restoreActionBar();
            } catch (Exception e) {
                LogUtil.INSTANCE.error("birdgangerror", e);
            }
        }

        //
        @Override
        public void onRequireDoMainWork() {
        }

        @Override
        public void onShowRefreshPage() {
            LogUtil.INSTANCE.info("birdgangwebview" , "WebViewClientCallback > onShowRefreshPage()");
            if (mWebView != null) {
                mWebView.setVisibility(View.GONE);
            }
        }

        @Override
        public void onShowWebViewPage() {
            LogUtil.INSTANCE.info("birdgangwebview" , "WebViewClientCallback > onShowWebViewPage()");
            if (mWebView != null) {
                mWebView.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onMoveMyFolder() {
            LogUtil.INSTANCE.info("birdgangwebview" , "WebViewClientCallback > onMoveMyFolder()");
            if (mViewClientCallback != null) {
                mViewClientCallback.onMoveMyFolder();
            }
        }

        @Override
        public void onMovePlayedList() {
            LogUtil.INSTANCE.info("birdgangwebview" , "WebViewClientCallback > onMovePlayedList()");
            if (mViewClientCallback != null) {
                mViewClientCallback.onMovePlayedList();
            }
        }

        @Override
        public void onMoveHome() {
            LogUtil.INSTANCE.info("birdgangwebview" , "WebViewClientCallback > onMoveHome()");
            if(mViewClientCallback != null) {
                mViewClientCallback.onMoveHome();
            }
        }

        public void onMoveFavorite() {
            LogUtil.INSTANCE.info("birdgangwebview" , "WebViewClientCallback > onMoveFavorite()");
            if (mViewClientCallback != null) {
                mViewClientCallback.onMoveFavorite();
            }
        }

        public void onMove3rdpartyapp() {
            LogUtil.INSTANCE.info("birdgangwebview" , "WebViewClientCallback > onMove3rdpartyapp()");
            if (mViewClientCallback != null) {
                mViewClientCallback.onMove3rdpartyapp();
            }
        }

        @Override
        public void onMoveSetting() {
            LogUtil.INSTANCE.info("birdgangwebview" , "WebViewClientCallback > onMoveSetting()");
            if (mViewClientCallback != null) {
                mViewClientCallback.onMoveSetting();
            }
        }

        @Override
        public void onMoveContent(int index) {
            LogUtil.INSTANCE.info("birdgangwebview" , "WebViewClientCallback > onMoveContent()");
            if (mViewClientCallback != null) {
                mViewClientCallback.onMoveContent(index);
            }
        };
    };


    // Interface
    private WebViewJavaScriptInterface mWebViewJavaScriptInterface = new WebViewJavaScriptInterface() {
        @Override
        public void onSetHtmlTitleMessage(String title) {
        }
    };


    public boolean canGoBack() {
        if (mWebView.canGoBack() == true) {
            return true;
        } else {
            return false;
        }
    }

    public void goBack() {
        WebBackForwardList list = mWebView.copyBackForwardList();
        if (list.getCurrentIndex() > 0) {
            WebHistoryItem Item =  list.getItemAtIndex(list.getCurrentIndex() - 1);
            String url = Item.getUrl();

            String prefix = "ndwebviewresource/mainscreen";
            // 내부 html
            if (url.contains("file:///android_asset") || url.contains(prefix)) {
                if (mWebView.canGoBack() == true) {
                    mWebView.goBack();
                }
            }
            else {
                if (NetworkUtils.isNetworkConnected(getActivity())) {
                    if (mWebView.canGoBack() == true) {
                        mWebView.goBack();
                    }
                }
            }
        }
    }

    @SuppressLint("NewApi")
    public void setVisibleRefrsh(boolean value) {
        mMenuRefresh = value;
        getActivity().invalidateOptionsMenu();
    }

    public void reload() {
        if (NetworkUtils.isNetworkConnected(getActivity())) {
            mWebView.reload();
            mWebViewClientCallback.onShowWebViewPage();
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