package com.inka.netsync.ui.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.inka.ncg2.Ncg2Exception;
import com.inka.netsync.R;
import com.inka.netsync.common.AppConstants;
import com.inka.netsync.data.cache.pref.PreferencesCacheHelper;
import com.inka.netsync.di.component.ActivityComponent;
import com.inka.netsync.logs.LogUtil;
import com.inka.netsync.ncg.NetSyncSdkHelper;
import com.inka.netsync.ncg.model.PlayerEntry;
import com.inka.netsync.ui.BaseActivity;
import com.inka.netsync.ui.mvpview.MvpView;
import com.inka.netsync.view.dialog.progress.LoadingProgressDialogHelper;
import com.inka.netsync.view.model.ContentViewEntry;
import com.inka.netsync.view.web.callback.ViewClientCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Unbinder;

/**
 * Created by birdgang on 2017. 4. 14..
 */
public abstract class BaseFragment extends Fragment implements MvpView {

    protected final static int ACT_PLAYER = 1000;

    private final String TAG = "BaseFragment";

    private BaseActivity mActivity;
    private Unbinder mUnBinder;

    protected String mPlayType;

    protected ViewClientCallback mViewClientCallback = null;

    protected void requestSearchView () {}

    protected void onLoadPlaybackActivity(PlayerEntry playerEntry) {}

    protected Class<?> provideSearchView () {
        return null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BaseActivity) {
            BaseActivity activity = (BaseActivity) context;
            this.mActivity = activity;
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        try {
            if (getActivity() instanceof ViewClientCallback) {
                mViewClientCallback = (ViewClientCallback)getActivity();
            }
        } catch (Exception e) {
            LogUtil.INSTANCE.error("error", e);
        }
    }

    @Override
    public boolean isNetworkConnected() {
        if (mActivity != null) {
            return mActivity.isNetworkConnected();
        }
        return false;
    }

    @Override
    public void onDetach() {
        mActivity = null;
        super.onDetach();
    }

    @Override
    public void showProgress(Activity activity, int message) {
        LoadingProgressDialogHelper.getDefault().show();
    }

    @Override
    public void hideProgress(Activity activity) {
        LoadingProgressDialogHelper.getDefault().hide();
    }

    public void contentItemClick(final View view) {}

    public ActivityComponent getActivityComponent() {
        return mActivity.getActivityComponent();
    }

    public BaseActivity getBaseActivity() {
        return mActivity;
    }

    public void setUnBinder(Unbinder unBinder) {
        mUnBinder = unBinder;
    }

    @Override
    public void onDestroy() {
        if (mUnBinder != null) {
            mUnBinder.unbind();
        }
        cancelTask();
        super.onDestroy();
    }

    public interface Callback {
        void onFragmentAttached();
        void onFragmentDetached(String tag);
    }


    public boolean checkStoragePermission() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    /***
     *
     * @return
     */
    protected ActionBar getActionBar() {
        AppCompatActivity activity = (AppCompatActivity)getActivity();
        if (null != activity) {
            return activity.getSupportActionBar();
        }
        return null;
    }


    protected void restoreActionBar() {
        try {
            ActionBar actionBar = getActionBar();
            BaseActivity activity = (BaseActivity)getActivity();
            if (activity.getAppUiType() == AppConstants.APPUITYPE_MAIN) {
                actionBar.setTitle(getArguments().getString(this.getString(R.string.key_name_fragment)));
                actionBar.setSubtitle(null);
                actionBar.setDisplayHomeAsUpEnabled(false);
                actionBar.setHomeButtonEnabled(true);
                actionBar.setDisplayShowHomeEnabled(true);
            } else if (activity.getAppUiType() == AppConstants.APPUITYPE_DRAWER) {
                String title = getArguments().getString(this.getString(R.string.key_name_fragment));
                LogUtil.INSTANCE.info("birdgangactionbar" , "restoreActionBar > title : " + title);
                actionBar.setDisplayShowTitleEnabled(false);
                actionBar.setDisplayShowCustomEnabled(true);
                actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

                View customView = LayoutInflater.from(getActivity()).inflate(R.layout.view_actionbar_drawable_menu, null);
                actionBar.setCustomView(customView);
                Toolbar parent = (Toolbar) customView.getParent();
                parent.setPadding(0,0,0,0);
                parent.setContentInsetsAbsolute(0,0);

                TextView textViewTitle = (TextView) customView.findViewById(R.id.text_actionbar_title);
                textViewTitle.setText(title);

                ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
                actionBar.setCustomView(customView, params);

                actionBar.setSubtitle(null);
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setHomeButtonEnabled(false);
                actionBar.setDisplayShowHomeEnabled(false);
            }
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
    }

    private void cancelTask() {
    }

    protected void showResultDialog (String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }


    public void getSortListData(List<ContentViewEntry> contentViewEntries) {
        try {
            int conditionSort = PreferencesCacheHelper.getPreferenceValueForInteger(PreferencesCacheHelper.SORT_NAME, PreferencesCacheHelper.SORT_NAME_ASC);

            List<ContentViewEntry> sortDirectories = new ArrayList<>();
            List<ContentViewEntry> sortVideos = new ArrayList<>();
            List<ContentViewEntry> sortAudios = new ArrayList<>();
            List<ContentViewEntry> sortDocs = new ArrayList<>();

            for (ContentViewEntry entry : contentViewEntries) {
                if (entry.getMediaType() == ContentViewEntry.ContentType.GROUP.getType()) {
                    sortDirectories.add(entry);
                } else if (entry.getMediaType() == ContentViewEntry.ContentType.VIDEO.getType()) {
                    sortVideos.add(entry);
                } else if (entry.getMediaType() == ContentViewEntry.ContentType.AUDIO.getType()) {
                    sortAudios.add(entry);
                } else if (entry.getMediaType() == ContentViewEntry.ContentType.DOC.getType()) {
                    sortDocs.add(entry);
                }
            }

            LogUtil.INSTANCE.info("birdgangsort" , " sortDirectories size : " + sortDirectories.size() + " , sortVideos size : " + sortVideos.size()
                    + " , sortAudios size : " + sortAudios.size() + " , sortDocs size : " + sortDocs.size());


            if (conditionSort == PreferencesCacheHelper.SORT_NAME_ASC) {
                Collections.sort(sortDirectories, new ContentViewEntry.SortContentNameAscCompare());
                Collections.sort(sortVideos, new ContentViewEntry.SortContentNameAscCompare());
                Collections.sort(sortAudios, new ContentViewEntry.SortContentNameAscCompare());
                Collections.sort(sortDocs, new ContentViewEntry.SortContentNameAscCompare());
            }
            else if (conditionSort == PreferencesCacheHelper.SORT_NAME_DESC) {
                Collections.sort(sortDirectories, new ContentViewEntry.SortContentNameDescCompare());
                Collections.sort(sortVideos, new ContentViewEntry.SortContentNameDescCompare());
                Collections.sort(sortAudios, new ContentViewEntry.SortContentNameDescCompare());
                Collections.sort(sortDocs, new ContentViewEntry.SortContentNameDescCompare());
            }

            contentViewEntries.clear();
            contentViewEntries.addAll(sortDirectories);
            contentViewEntries.addAll(sortVideos);
            contentViewEntries.addAll(sortAudios);
            contentViewEntries.addAll(sortDocs);
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
    }


    protected boolean checkAvailableContent (String path) {
        boolean result = false;
        try {
            result = checkExistContentOnDisk(path);
            if (!result) {
                Toast.makeText(getActivity(), getString(R.string.message_for_toast_file_not_exist), Toast.LENGTH_SHORT).show();
                return result;
            }
            result = checkNcgContent(path);
            if (!result) {
                Toast.makeText(getActivity(), getString(R.string.message_for_toast_file_is_not_ncg), Toast.LENGTH_SHORT).show();
                return result;
            }
        } catch (Ncg2Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
        return result;
    }

    private boolean checkExistContentOnDisk (String path) {
        return new File (path).exists();
    }

    private boolean checkNcgContent (String path) throws Ncg2Exception {
        return NetSyncSdkHelper.getDefault().isNcgContent(path);
    }

    public abstract boolean onBackPressed();

    public abstract void setUp(View view);
    public abstract void refleshContents ();
    public abstract void onScanStarted();
    public abstract void onScaning();
    public abstract void onScanCompleated();
    public abstract void onSDcardMountedEvent(String externalPath);
    public abstract void onSDcardEjectedEvent();
}
