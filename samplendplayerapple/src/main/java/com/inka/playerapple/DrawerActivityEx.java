package com.inka.playerapple;

import com.inka.netsync.common.utils.StringUtil;
import com.inka.netsync.media.MediaStorage;
import com.inka.netsync.ui.DrawerActivityPlayer;
import com.inka.netsync.ui.fragment.BaseFragment;

public class DrawerActivityEx extends DrawerActivityPlayer {

    /**
     *
     * @return
     */
    @Override
    protected BaseFragment provideDefaultFragment() {
        return DefaultFragmentEx.newInstance();
    }

    /**
     *
     * @param storageType
     * @return
     */
    @Override
    protected BaseFragment provideExplorerFragment(String storageType) {
        String keyTagFragment = this.getString(R.string.key_tag_fragment);
        String keyNameFragment = this.getString(R.string.key_name_fragment);

        if (StringUtil.equals(MediaStorage.ROOT_USB, storageType)) {
            return ExplorerFragmentEx.newInstance(keyTagFragment, this.getString(R.string.tag_storage_other_fragment), keyNameFragment, this.getString(R.string.title_explorer_other_fragment_tag), storageType);
        }
        else if (StringUtil.equals(MediaStorage.ROOT_INTERNAL, storageType)) {
            return ExplorerFragmentEx.newInstance(keyTagFragment, this.getString(R.string.tag_storage_internal_fragment), keyNameFragment, this.getString(R.string.title_explorer_internal_fragment_tag), storageType);
        }
        else if (StringUtil.equals(MediaStorage.ROOT_EXTERNAL, storageType)) {
            return ExplorerFragmentEx.newInstance(keyTagFragment, this.getString(R.string.tag_storage_external_fragment), keyNameFragment, this.getString(R.string.title_explorer_external_fragment_tag), storageType);
        }
        else {
            return super.provideExplorerFragment(storageType);
        }
    }

    /**
     *
     * @return
     */
    @Override
    protected BaseFragment provideFavoriteFragment() {
        String keyTagFragment = this.getString(R.string.key_tag_fragment);
        String keyNameFragment = this.getString(R.string.key_name_fragment);
        return FavoriteFragmentEx.newInstance(keyTagFragment, this.getString(R.string.tag_menu_favorite_fragment), keyNameFragment, this.getString(R.string.title_myfavorite_fragment_tag));
    }

    /**
     *
     * @return
     */
    @Override
    protected BaseFragment providePlayedListFragment() {
        String keyTagFragment = this.getString(R.string.key_tag_fragment);
        String keyNameFragment = this.getString(R.string.key_name_fragment);
        return RecentlyPlayedListFragmentEx.newInstance(keyTagFragment, this.getString(R.string.tag_playedlist_fragment), keyNameFragment, this.getString(R.string.title_playedlist_fragment_tag));
    }

    /**
     *
     * @return
     */
    @Override
    protected BaseFragment provideSettingFragment() {
        String keyTagFragment = this.getString(R.string.key_tag_fragment);
        String keyNameFragment = this.getString(R.string.key_name_fragment);
        return SettingFragmentEx.newInstance(keyTagFragment, this.getString(R.string.tag_setting_fragment), keyNameFragment, this.getString(R.string.title_setting_fragment_tag));
    }

    /**
     *
     * @return
     */
    @Override
    protected BaseFragment provideInfoWebViewFragment() {
        String keyTagFragment = this.getString(R.string.key_tag_fragment);
        String keyNameFragment = this.getString(R.string.key_name_fragment);
        return InfoWebViewFragmentEx.newInstance(keyTagFragment, this.getString(R.string.tag_info_fragment), keyNameFragment, this.getString(R.string.title_info_fragment_tag));
    }

    /**
     *
     * @return
     */
    @Override
    protected BaseFragment provideInfoFragment() {
        String keyTagFragment = this.getString(R.string.key_tag_fragment);
        String keyNameFragment = this.getString(R.string.key_name_fragment);
        return InfoFragmentEx.newInstance(keyTagFragment, this.getString(R.string.tag_info_fragment), keyNameFragment, this.getString(R.string.title_info_fragment_tag));
    }

    /**
     * 스플래쉬 이미지
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
        return R.drawable.apple_progress_media_scan;
    }

    /**
     *
     * @return
     */
    @Override
    protected int provideProgressBarSyncDrawerResource () {
        return R.drawable.apple_progress_media_scan_sync;
    }

    /***
     * 스플래쉬 배경 컬러
     * @return
     */
    @Override
    protected int provideActionBarDrawable() {
        return R.drawable.apple_actionbar;
    }


}
