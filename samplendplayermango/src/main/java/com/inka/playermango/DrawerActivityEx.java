package com.inka.playermango;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;

import com.inka.netsync.logs.LogUtil;
import com.inka.netsync.model.DrawerMenuEntry;
import com.inka.netsync.sd.ui.SDDrawerPlayerActivity;
import com.inka.netsync.ui.fragment.BaseFragment;

import java.util.List;

public class DrawerActivityEx extends SDDrawerPlayerActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCurrentTabByTag(DrawerMenuEntry.TAG_WEBVIEW);
        onResultEnableDeviceModels();
    }

    @Override
    protected Fragment getFragment(String tabTag) {
        return super.getFragment(tabTag);
    }

    @Override
    protected BaseFragment provideWebViewFragment() {
        String keyTagFragment = this.getString(R.string.key_tag_fragment);
        String keyNameFragment = this.getString(R.string.key_name_fragment);
        return WebViewFragmentEx.newInstance(keyTagFragment, this.getString(R.string.tag_webview_fragment), keyNameFragment, this.getString(R.string.title_webview_fragment_tag));
    }

    @Override
    protected BaseFragment provideExplorerFragment(String storageType) {
        String keyTagFragment = this.getString(R.string.key_tag_fragment);
        String keyNameFragment = this.getString(R.string.key_name_fragment);
        return ExplorerFragmentEx.newInstance(keyTagFragment, this.getString(R.string.tag_storage_external_fragment), keyNameFragment, this.getString(R.string.title_explorer_external_fragment_tag), storageType);
    }

    @Override
    protected BaseFragment provideFavoriteFragment() {
        String keyTagFragment = this.getString(R.string.key_tag_fragment);
        String keyNameFragment = this.getString(R.string.key_name_fragment);
        return FavoriteFragmentEx.newInstance(keyTagFragment, this.getString(R.string.tag_menu_favorite_fragment), keyNameFragment, this.getString(R.string.title_myfavorite_fragment_tag));
    }

    @Override
    protected BaseFragment providePlayedListFragment() {
        String keyTagFragment = this.getString(R.string.key_tag_fragment);
        String keyNameFragment = this.getString(R.string.key_name_fragment);
        return RecentlyFragmentEx.newInstance(keyTagFragment, this.getString(R.string.tag_playedlist_fragment), keyNameFragment, this.getString(R.string.title_playedlist_fragment_tag));
    }

    @Override
    protected BaseFragment provideSettingFragment() {
        String keyTagFragment = this.getString(R.string.key_tag_fragment);
        String keyNameFragment = this.getString(R.string.key_name_fragment);
        return SettingFragmentEx.newInstance(keyTagFragment, this.getString(R.string.tag_setting_fragment), keyNameFragment, this.getString(R.string.title_setting_fragment_tag));
    }

    @Override
    protected BaseFragment provideInfoWebViewFragment() {
        String keyTagFragment = this.getString(R.string.key_tag_fragment);
        String keyNameFragment = this.getString(R.string.key_name_fragment);
        return InfoWebViewFragmentEx.newInstance(keyTagFragment, this.getString(R.string.tag_info_fragment), keyNameFragment, this.getString(R.string.title_info_fragment_tag));
    }

    @Override
    protected BaseFragment provideInfoFragment() {
        String keyTagFragment = this.getString(R.string.key_tag_fragment);
        String keyNameFragment = this.getString(R.string.key_name_fragment);
        return InfoFragmentEx.newInstance(keyTagFragment, this.getString(R.string.tag_info_fragment), keyNameFragment, this.getString(R.string.title_info_fragment_tag));
    }

    /**
     * 상태바 컬러
     * @return
     */
    @Override
    protected int provideStatusBarBackgroundColor() {
        return R.color.provider_color_statusbar_main_bg;
    }


    /**
     * 상태바 컬러
     * @return
     */
    @Override
    protected int provideActionBarColor() {
        return R.color.provider_color_actionbar_main_bg;
    }

    /**
     *
     * @return
     */
    @Override
    public int provideProgressBarDrawerResource () {
        return R.drawable.mango_progress_media_scan;
    }

    /**
     *
     * @return
     */
    @Override
    protected int provideProgressBarSyncDrawerResource () {
        return R.drawable.mango_progress_media_scan_sync;
    }


    /**
     *
     * @return
     */
    protected boolean onResultEnableDeviceModels () {
        boolean result = false;
        List<String> enableDeviceModels = MangoApplication.provideEnableDeviceModels();
        String currentDeviceModel = Build.MODEL;
        LogUtil.INSTANCE.info("birdganginit", "currentDeviceModel : " + currentDeviceModel);
        if (enableDeviceModels.size() > 0 && !enableDeviceModels.contains(currentDeviceModel)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.dialog_title_device_authentication));
            builder.setMessage(getString(R.string.log_result_fail_to_get_proprietary_info));
            builder.setPositiveButton(getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    finish();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            result = false;
        } else {
            result = true;
        }
        return result;
    }

}