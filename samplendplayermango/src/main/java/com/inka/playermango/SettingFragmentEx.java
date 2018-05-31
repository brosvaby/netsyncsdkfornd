package com.inka.playermango;

import android.os.Bundle;

import com.inka.netsync.sd.ui.fragment.SDSettingFragment;
import com.inka.netsync.view.adapter.SectionedRecyclerViewAdapter;
import com.inka.netsync.view.model.SettingMenuViewEntry;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 설정 화면 입니다.
 */
public class SettingFragmentEx extends SDSettingFragment {
	public static SettingFragmentEx newInstance(String fragmentTag_key,String fragmentTag, String fragmentName_key, String fragmentName) {
		SettingFragmentEx newFragment = new SettingFragmentEx();
		Bundle args = new Bundle();
		args.putString(fragmentTag_key, fragmentTag);
		args.putString(fragmentName_key, fragmentName);
		newFragment.setArguments(args);
		return newFragment;
	}

	/**
	 * 설정화면 섹션 목록을 지정합니다.
	 * @return
	 */
	@Override
	public List<SectionedRecyclerViewAdapter.Section> provideSettingSectionMenus() {
		List<SectionedRecyclerViewAdapter.Section> sections = new ArrayList<>();
		sections.add(new SectionedRecyclerViewAdapter.Section(0, getString(R.string.setting_section_networkandplayer)));
		sections.add(new SectionedRecyclerViewAdapter.Section(3, getString(R.string.setting_section_basic_information)));
		sections.add(new SectionedRecyclerViewAdapter.Section(6, getString(R.string.setting_section_basic_function)));
		return sections;
	}


	/**
	 * 설정화면 메뉴 목록을 지정합니다.
	 * @return
	 */
	@Override
	protected List<SettingMenuViewEntry> provideSettingMenus () {
		List<SettingMenuViewEntry> settingMenuEntries = new ArrayList<>();

		settingMenuEntries.add(new SettingMenuViewEntry (
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
				StringUtils.EMPTY,
				StringUtils.EMPTY,
				getEnableGusture(), true));

		///////
		settingMenuEntries.add(new SettingMenuViewEntry(
				SettingMenuViewEntry.ID_BASIC_INFO_APP_NAME,
				SettingMenuViewEntry.SettingType.COMMON.getType(),
				getActivity().getString(R.string.setting_info_app_name),
				getAppName(),
				StringUtils.EMPTY,
				true, false));

		settingMenuEntries.add(new SettingMenuViewEntry(
				SettingMenuViewEntry.ID_BASIC_INFO_APP_VERSION,
				SettingMenuViewEntry.SettingType.WITHBUTTON.getType(),
				getActivity().getString(R.string.setting_info_app_version),
				getAppVersion(),
				StringUtils.EMPTY,
				true, getEnableMarkUpdate()));

		settingMenuEntries.add(new SettingMenuViewEntry(
				SettingMenuViewEntry.ID_BASIC_INFO_APP_COMPANY,
				SettingMenuViewEntry.SettingType.COMMON.getType(),
				getActivity().getString(R.string.setting_info_app_company),
				getCompanyName(),
				StringUtils.EMPTY,
				true, false));

		/////////
		settingMenuEntries.add(new SettingMenuViewEntry(
				SettingMenuViewEntry.ID_BASIC_INFO_CONTACT,
				SettingMenuViewEntry.SettingType.COMMON.getType(),
				getActivity().getString(R.string.setting_info_contact),
				getContact(),
				StringUtils.EMPTY,
				true, false));

		settingMenuEntries.add(new SettingMenuViewEntry(
				SettingMenuViewEntry.ID_BASIC_FNC_PRIVACY,
				SettingMenuViewEntry.SettingType.COMMON.getType(),
				getActivity().getString(R.string.setting_info_fnc_privacy),
				StringUtils.EMPTY,
				StringUtils.EMPTY,
				true, true));

		return settingMenuEntries;
	}

	/**
	 * 어플리케이션 이름을 정의 합니다.
	 * @return
	 */
	@Override
	protected String provideApplicationName () {
		return getString(R.string.mango_app_name);
	}

	/**
	 * 제작사를 지정 합니다.
	 * @return
	 */
	@Override
	protected String provideApplicationCompany () {
		return getString(R.string.mango_app_company);
	}


	/**
	 * 제작사 이메일 지정 합니다.
	 * @return
	 */
	@Override
	protected String provideContact () {
		return getString(R.string.mango_contact);
	}

}
