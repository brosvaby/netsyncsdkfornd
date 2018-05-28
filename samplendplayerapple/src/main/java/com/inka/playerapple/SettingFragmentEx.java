package com.inka.playerapple;

import android.os.Bundle;

import com.inka.netsync.common.utils.StringUtil;
import com.inka.netsync.data.network.model.MarketVersionCheckEntry;
import com.inka.netsync.logs.LogUtil;
import com.inka.netsync.ui.fragment.SettingFragment;
import com.inka.netsync.view.adapter.SectionedRecyclerViewAdapter;
import com.inka.netsync.view.model.SettingMenuViewEntry;

import java.util.ArrayList;
import java.util.List;

public class SettingFragmentEx extends SettingFragment {

	private final String TAG = SettingFragmentEx.class.toString();

	public static SettingFragmentEx newInstance(String fragmentTag_key, String fragmentTag, String fragmentName_key, String fragmentName) {
		SettingFragmentEx newFragment = new SettingFragmentEx();
		Bundle args = new Bundle();
		args.putString(fragmentTag_key, fragmentTag);
		args.putString(fragmentName_key, fragmentName);
		newFragment.setArguments(args);
		return newFragment;
	}

	@Override
	public void onStart() {
		super.onStart();
		checkMarketVersion();
	}

	@Override
	public void onSDcardMountedEvent(String externalSDPath) {
		try {
			Thread.sleep(2000);
			updateListData();
		} catch (InterruptedException e) {
			LogUtil.INSTANCE.error(TAG, e);
		}
	}

	public void checkMarketVersion() {
		MarketVersionCheckEntry requestMarketVersionCheck = new MarketVersionCheckEntry();
		requestMarketVersionCheck.setPackageName(getAppPackage());
		requestMarketVersionCheck.setCurrentAppVersion(getAppVersion());
		super.checkMarketVersionRx(requestMarketVersionCheck);
	}


	@Override
	public List<SectionedRecyclerViewAdapter.Section> provideSettingSectionMenus() {
		List<SectionedRecyclerViewAdapter.Section> sections = new ArrayList<>();
		sections.add(new SectionedRecyclerViewAdapter.Section(0, getString(R.string.setting_section_networkandplayer)));
		sections.add(new SectionedRecyclerViewAdapter.Section(3, getString(R.string.setting_section_basic_information)));
		sections.add(new SectionedRecyclerViewAdapter.Section(6, getString(R.string.setting_section_basic_function)));
		return sections;
	}


	@Override
	protected List<SettingMenuViewEntry> provideSettingMenus () {
		List<SettingMenuViewEntry> settingMenuEntries = new ArrayList<>();

		settingMenuEntries.add(new SettingMenuViewEntry(
				SettingMenuViewEntry.ID_PLAYER,
				SettingMenuViewEntry.SettingType.COMMON.getType(),
				getActivity().getString(R.string.setting_info_player),
				getPlayerType(),
				getActivity().getString(R.string.setting_description_native_player),
				true, true));

		settingMenuEntries.add(new SettingMenuViewEntry(
				SettingMenuViewEntry.ID_SWXPLAY_ALLOW,
				SettingMenuViewEntry.SettingType.COMMON.getType(),
				getActivity().getString(R.string.setting_info_swxplay),
				getDecoderType(),
				getActivity().getString(R.string.setting_description_decoder),
				true, true));

		settingMenuEntries.add(new SettingMenuViewEntry(
				SettingMenuViewEntry.ID_GESTURE_ALLOW,
				SettingMenuViewEntry.SettingType.SWITCH.getType(),
				getActivity().getString(R.string.setting_info_gesture_availble),
				StringUtil.EMPTY,
				StringUtil.EMPTY,
				getEnableGusture(), true));

		///////
		settingMenuEntries.add(new SettingMenuViewEntry(
				SettingMenuViewEntry.ID_BASIC_INFO_APP_NAME,
				SettingMenuViewEntry.SettingType.COMMON.getType(),
				getActivity().getString(R.string.setting_info_app_name),
				getAppName(),
				StringUtil.EMPTY,
				true, false));

		settingMenuEntries.add(new SettingMenuViewEntry(
				SettingMenuViewEntry.ID_BASIC_INFO_APP_VERSION,
				SettingMenuViewEntry.SettingType.WITHBUTTON.getType(),
				getActivity().getString(R.string.setting_info_app_version),
				getAppVersion(),
				StringUtil.EMPTY,
				true, getEnableMarkUpdate()));

		settingMenuEntries.add(new SettingMenuViewEntry(
				SettingMenuViewEntry.ID_BASIC_INFO_APP_COMPANY,
				SettingMenuViewEntry.SettingType.COMMON.getType(),
				getActivity().getString(R.string.setting_info_app_company),
				getCompanyName(),
				StringUtil.EMPTY,
				true, false));

		/////////
		settingMenuEntries.add(new SettingMenuViewEntry(
				SettingMenuViewEntry.ID_BASIC_INFO_CONTACT,
				SettingMenuViewEntry.SettingType.COMMON.getType(),
				getActivity().getString(R.string.setting_info_contact),
				getContact(),
				StringUtil.EMPTY,
				true, false));

		settingMenuEntries.add(new SettingMenuViewEntry(
				SettingMenuViewEntry.ID_BASIC_FNC_PRIVACY,
				SettingMenuViewEntry.SettingType.COMMON.getType(),
				getActivity().getString(R.string.setting_info_fnc_privacy),
				StringUtil.EMPTY,
				StringUtil.EMPTY,
				true, true));

		return settingMenuEntries;
	}


	@Override
	protected String provideApplicationName () {
		return getString(R.string.app_name);
	}

	@Override
	protected String provideApplicationCompany () {
		return getString(R.string.app_company);
	}

	@Override
	protected String providePlayerName () {
		return getString(R.string.player_name);
	}

	@Override
	protected String provideContact () {
		return getString(R.string.contact);
	}


}
