package com.inka.netsync.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.inka.netsync.BaseApplication;
import com.inka.netsync.BaseConfigurationPerSite;
import com.inka.netsync.R;
import com.inka.netsync.R2;
import com.inka.netsync.admin.Extractor;
import com.inka.netsync.admin.ModuleConfig;
import com.inka.netsync.common.bus.ContentSettingItemClickListener;
import com.inka.netsync.common.bus.EventBus;
import com.inka.netsync.controler.SettingControler;
import com.inka.netsync.data.cache.db.MetaData;
import com.inka.netsync.data.cache.pref.PreferencesCacheHelper;
import com.inka.netsync.data.network.model.MarketVersionCheckEntry;
import com.inka.netsync.logs.LogUtil;
import com.inka.netsync.ncg.NetSyncSdkHelper;
import com.inka.netsync.ui.DrawerActivity;
import com.inka.netsync.ui.mvppresenter.SettingMvpPresenter;
import com.inka.netsync.ui.mvpview.SettingMvpView;
import com.inka.netsync.view.adapter.ListSettingAdapter;
import com.inka.netsync.view.adapter.SectionedRecyclerViewAdapter;
import com.inka.netsync.view.dialog.SingleChoiceWithDescriptionAlertDialog;
import com.inka.netsync.view.meterial.DividerItemDecoration;
import com.inka.netsync.view.model.SettingMenuViewEntry;
import com.inka.netsync.view.model.SingleCheckViewEntry;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingFragment extends BaseFragment implements SettingMvpView {

    private static final String TAG = "SettingFragment";

    @Inject
    SettingMvpPresenter<SettingMvpView> mPresenter;

    @BindView(R2.id.recycler_list)
    protected RecyclerView mRecyclerView;

    protected ListSettingAdapter mListSettingAdapter = null;

    public List<SettingMenuViewEntry> mSettingMenuEntries = new ArrayList<>();

    public List<SectionedRecyclerViewAdapter.Section> mSections = new ArrayList<>();

    protected boolean mEnableAppUpdate = false;

    public static SettingFragment newInstance(String tagKey, String tag, String nameKey, String name) {
        Bundle args = new Bundle();
        args.putString(tagKey, tag);
        args.putString(nameKey, name);
        SettingFragment fragment = new SettingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSettingMenuEntries = new ArrayList<>();
        mSettingMenuEntries.addAll(provideSettingMenus());
        if (ModuleConfig.ENABLE_EXTRACT_DATABASE) {
            mSettingMenuEntries.add(new SettingMenuViewEntry(
                    SettingMenuViewEntry.ID_BASIC_FNC_EXTRACT_DATABASE,
                    SettingMenuViewEntry.SettingType.COMMON.getType(),
                    "데이타 베이스 축출",
                    StringUtils.EMPTY,
                    StringUtils.EMPTY,
                    true, true));
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sd_setting, container, false);

        getActivityComponent().inject(this);
        setUnBinder(ButterKnife.bind(this, view));
        mPresenter.onAttach(this);

        setUp(view);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().registerDialogEventListener(mDialogEventListener);
        EventBus.getDefault().registerEventDeviceStateListener(onEventDeviceStateListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().removeDialogEventListener(mDialogEventListener);
        EventBus.getDefault().removeEventDeviceStateListener(onEventDeviceStateListener);
    }


    @Override
    public void setUp(View view) {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        mListSettingAdapter = new ListSettingAdapter(getActivity(), mSettingMenuEntries, mContentItemClickListener);
        mRecyclerView.setHasFixedSize(true);

        mSections.addAll(provideSettingSectionMenus());

        //Add your adapter to the sectionAdapter
        SectionedRecyclerViewAdapter.Section[] dummy = new SectionedRecyclerViewAdapter.Section[mSections.size()];
        SectionedRecyclerViewAdapter mSectionedAdapter = new SectionedRecyclerViewAdapter(getActivity(), R.layout.row_setting_section, R.id.section_text, mListSettingAdapter);
        mSectionedAdapter.setSections(mSections.toArray(dummy));

        //Apply this adapter to the RecyclerView
        mRecyclerView.setAdapter(mSectionedAdapter);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        FragmentActivity activity = getActivity();
        if (activity instanceof DrawerActivity) {
            // 슬라이드 메뉴가 열려 있다면
            if (((DrawerActivity)activity).isDrawerOpen() == true) {
                return;
            }
        }
        restoreActionBar();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        FragmentActivity activity = getActivity();
        if (activity instanceof DrawerActivity) {
            // 슬라이드 메뉴가 열려 있다면
            if(((DrawerActivity)activity).isDrawerOpen() == true) {
                return;
            }
        }
    }

    @Override
    protected void restoreActionBar() {
        super.restoreActionBar();
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public void refleshContents() {
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

    public ContentSettingItemClickListener mContentItemClickListener = new ContentSettingItemClickListener() {
        @Override
        public void onItemSwitchClick(int id, boolean enable) {
            try {
                LogUtil.INSTANCE.info(TAG, "onItemSwitchClick > id : " + id + " , enable : " + enable);
                itemSwitch(id, enable);
            } catch (Exception e) {
                LogUtil.INSTANCE.error(getClass().getSimpleName(), e);
            }
        }

        @Override
        public void onItemClick(View view) {
            try {
                SettingMenuViewEntry settingMenuEntry = (SettingMenuViewEntry) view.getTag();
                int id = settingMenuEntry.getId();
                LogUtil.INSTANCE.info(TAG, "onItemClick > id : " + id);
                itemClick(settingMenuEntry);
            } catch (Exception e) {
                LogUtil.INSTANCE.error(getClass().getSimpleName(), e);
            }
        }
    };

    protected void deleteAllLicenseAndFile() {
        NetSyncSdkHelper.getDefault().removeLicenseAllCID();   // License 전체 삭제(2중장치)
    }

    public EventBus.onDialogEventListener mDialogEventListener = new EventBus.onDialogEventListener() {
        @Override
        public void onDialogPositiveClick(int dialogId, int index, String commonName) {
        }
    };


    public void itemSwitch (int id, boolean enable) {
        if (id == SettingMenuViewEntry.ID_GESTURE_ALLOW) {
            SettingControler.getDefault().setEnableGestureOnPlayer(enable);
        }
        else if (id == SettingMenuViewEntry.ID_RESTRICTED_INTERNET) {
            SettingControler.getDefault().setEnableRestrictedInternet(enable);
            //Toast.makeText(getActivity(), "Currently not supported.", Toast.LENGTH_SHORT).show();
        }
    }


    public void itemClick(SettingMenuViewEntry settingMenuEntry) {
        if (null == settingMenuEntry) {
            return;
        }

        long commonId = settingMenuEntry.getId();

        if (commonId == SettingMenuViewEntry.ID_PLAYER) {
            mPresenter.changePlayerType();
        }
        if (commonId == SettingMenuViewEntry.ID_SWXPLAY_ALLOW) {
            mPresenter.changeDecoderType();
        }
        else if (commonId == SettingMenuViewEntry.ID_BASIC_INFO_APP_VERSION) {
            if (settingMenuEntry.isEnableMarketUpdate()) {
                Uri uri = Uri.parse("market://details?id=" + getAppPackage());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                getActivity().startActivity(intent);
            }
        }
        else if(commonId == SettingMenuViewEntry.ID_BASIC_FNC_PRIVACY) {
            String privacyPolicyUrl = BaseConfigurationPerSite.getInstance().getPrivacyPolicyUrl();
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(privacyPolicyUrl));
            startActivity(intent);
        }
        else if (commonId == SettingMenuViewEntry.ID_BASIC_FNC_EXTRACT_DATABASE) {
            String databaseLocation = BaseApplication.getContext().getDatabasePath(MetaData.DATABASE_NAME).getPath();
            Extractor.getDefault().extractDatabaseFile(getActivity(), databaseLocation, getAppName());
        }
    }


    public String getAppName() {
        return provideApplicationName();
    }

    public String getAppVersion() {
        try {
            String version = mPresenter.getAppVersion();
            return version;
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
            return "N/A";
        }
    }

    public String getAppPackage() {
        try {
            String appPackageName = mPresenter.getAppPackage();
            return appPackageName;
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
            return "N/A";
        }
    }


    public String getCompanyName() {
        return provideApplicationCompany();
    }

    public String getPlayerName() {
        return providePlayerName();
    }

    public String getContact() {
        return provideContact();
    }

    public void updateListData () {
        try {
            LogUtil.INSTANCE.info("birdggangsetting" , "updateListData > settingMenuEntries size : " + mSettingMenuEntries.size());
            mListSettingAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
    }


    public void setContentPath(int settingDir) {
    }

    public String getPlayerType () {
        int value = PreferencesCacheHelper.getPreferenceValueForInteger(PreferencesCacheHelper.SETTING_PLAYER, PreferencesCacheHelper.VISUALON_PLAYER);
        LogUtil.INSTANCE.info("birdgangbasesettingdialog" , "getPlayerType > value : " + value);

        String playerType = "";
        switch (value) {
            case PreferencesCacheHelper.BASE_PLAYER:
                playerType = this.getString(R.string.setting_player_base);
                break;
            case PreferencesCacheHelper.VISUALON_PLAYER:
                playerType = this.getString(R.string.setting_player_double_speed);
                break;
            default:
                playerType = this.getString(R.string.setting_player_base);
                break;
        }
        return playerType;
    }

    public String getDecoderType() {
        int value = PreferencesCacheHelper.getPreferenceValueForInteger(PreferencesCacheHelper.SETTING_DECODER, PreferencesCacheHelper.DECODER_HARDWARE);
        String decoderType = "";
        switch (value) {
            case PreferencesCacheHelper.DECODER_SOFTWARE:
                decoderType = this.getString(R.string.setting_decoder_software);
                break;
            case PreferencesCacheHelper.DECODER_HARDWARE:
                decoderType = this.getString(R.string.setting_decoder_hardware);
                break;
            default:
                decoderType = this.getString(R.string.setting_none);
                break;
        }
        return decoderType;
    }

    public boolean getEnableGusture () {
        boolean enableGusture = SettingControler.getDefault().getEnableGestureOnPlayer();
        return enableGusture;
    }

    public boolean getEnableRestrictedInternet () {
        boolean enableGusture = SettingControler.getDefault().getEnableRestrictedInternet();
        return enableGusture;
    }

    public boolean getEnableMarkUpdate () {
        return mEnableAppUpdate;
    }

    /***
     *
     */
    protected EventBus.onEventDeviceStateListener onEventDeviceStateListener = new EventBus.onEventDeviceStateListener() {
        @Override
        public void onNetworkChangedEvent() {}

        @Override
        public void onSDcardMountedEvent(String externalSDPath) {
            LogUtil.INSTANCE.info("birdgangbroadcast", "onSDcardMountedEvent > externalSDPath : " + externalSDPath);
        }

        @Override
        public void onSDcardEjectedEvent() {
            LogUtil.INSTANCE.info("birdgangbroadcast", "SettingFragment > onSDcardEjectedEvent > ");
        }
    };


    /**
     * 마켓 버전 확인
     * @param requestMarketVersionCheck
     */
    protected void checkMarketVersionRx (MarketVersionCheckEntry requestMarketVersionCheck) {
        mPresenter.checkMarketVersion(requestMarketVersionCheck);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onBackPressed() {
        return true;
    }

    @Override
    public void onLoadChangePlayerDialog(List<SingleCheckViewEntry> singleSelectViewEntries) {
        try {
            new SingleChoiceWithDescriptionAlertDialog(getActivity())
                    .setDataSource(singleSelectViewEntries)
                    .setCanceled(true)
                    .setTitleText(getResources().getString(R.string.setting_info_player))
                    .setDescriptionText(getResources().getString(R.string.setting_description_native_player))
                    .setConfirmText(getString(R.string.dialog_ok))
                    .setConfirmClickListener(new SingleChoiceWithDescriptionAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClickListContent(SingleChoiceWithDescriptionAlertDialog sDialog) {
                            try {
                                int selectedId = sDialog.getSelectedId();
                                LogUtil.INSTANCE.info("birdgangbasesettingdialog" , "onClick > selectedId : " + selectedId);

                                PreferencesCacheHelper.setPreferenceValueForInteger(PreferencesCacheHelper.SETTING_PLAYER, selectedId);
                                for (SettingMenuViewEntry entry : mSettingMenuEntries) {
                                    if (entry.getId() == SettingMenuViewEntry.ID_PLAYER) {
                                        if (sDialog.getSelectedId() == 0) {
                                            entry.setValue(getResources().getString(R.string.setting_player_base));
                                        } else {
                                            entry.setValue(getResources().getString(R.string.setting_player_double_speed));
                                        }
                                        mListSettingAdapter.updateSettingMenus(SettingMenuViewEntry.ID_PLAYER, entry);
                                    }
                                }
                            } catch (Exception e) {
                                LogUtil.INSTANCE.error(TAG, e);
                            } finally {
                                sDialog.cancel();
                            }
                        }

                        @Override
                        public void onClick(SingleChoiceWithDescriptionAlertDialog sDialog) {}
                    })
                    .show();

        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
    }

    @Override
    public void onLoadChangeDecoderDialog(List<SingleCheckViewEntry> singleCheckViewEntries) {
        try {
            new SingleChoiceWithDescriptionAlertDialog(getActivity())
                    .setDataSource(singleCheckViewEntries)
                    .setCanceled(true)
                    .setTitleText(getResources().getString(R.string.setting_info_decoder))
                    .setDescriptionText(getResources().getString(R.string.setting_description_decoder))
                    .setConfirmText(getString(R.string.dialog_ok))
                    .setConfirmClickListener(new SingleChoiceWithDescriptionAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClickListContent(SingleChoiceWithDescriptionAlertDialog sDialog) {
                            try {
                                int selectedId = sDialog.getSelectedId();
                                LogUtil.INSTANCE.info("birdgangbasesettingdialog" , "onClick > selectedId : " + selectedId);
                                PreferencesCacheHelper.setPreferenceValueForInteger(PreferencesCacheHelper.SETTING_DECODER, selectedId);

                                for (SettingMenuViewEntry entry : mSettingMenuEntries) {
                                    if (entry.getId() == SettingMenuViewEntry.ID_SWXPLAY_ALLOW) {
                                        if (sDialog.getSelectedId() == 0) {
                                            entry.setValue(getResources().getString(R.string.setting_decoder_hardware));
                                        } else {
                                            entry.setValue(getResources().getString(R.string.setting_decoder_software));
                                        }
                                        mListSettingAdapter.updateSettingMenus(SettingMenuViewEntry.ID_SWXPLAY_ALLOW, entry);
                                    }
                                }
                            } catch (Exception e) {
                                LogUtil.INSTANCE.error(TAG, e);
                            } finally {
                                sDialog.cancel();
                            }
                        }

                        @Override
                        public void onClick(SingleChoiceWithDescriptionAlertDialog sDialog) {}
                    })
                    .show();
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
    }


    /***
     *
     * @return
     */
    public List<SectionedRecyclerViewAdapter.Section> provideSettingSectionMenus () {
        return new ArrayList<>();
    }


    /**
     *
     * @return
     */
    protected List<SettingMenuViewEntry> provideSettingMenus () {
        return new ArrayList<>();
    }


    /**
     *
     * @return
     */
    protected String provideApplicationName () {
        return StringUtils.EMPTY;
    }

    /**
     *
     * @return
     */
    protected String provideApplicationCompany () {
        return StringUtils.EMPTY;
    }

    /**
     *
     * @return
     */
    protected String providePlayerName () {
        return StringUtils.EMPTY;
    }

    /**
     *
     * @return
     */
    protected String provideContact () {
        return StringUtils.EMPTY;
    }

}
