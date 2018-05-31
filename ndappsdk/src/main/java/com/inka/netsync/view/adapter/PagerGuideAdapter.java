package com.inka.netsync.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.inka.netsync.common.bus.ClickListener;
import com.inka.netsync.logs.LogUtil;
import com.inka.netsync.view.adapter.fragment.GuideIntroFragment;
import com.inka.netsync.view.adapter.fragment.GuidePlayerFragment;
import com.inka.netsync.view.model.GuideViewEntry;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;


public class PagerGuideAdapter extends FragmentStatePagerAdapter {

    private List<GuideViewEntry> guideEntries;
    private List<Fragment> fragments;
    private ClickListener conClickListener;

    public PagerGuideAdapter(FragmentManager fm, String guideType, List<GuideViewEntry> guideEntries, ClickListener conClickListener) {
        super(fm);
        this.guideEntries = guideEntries;
        this.fragments = new ArrayList<>();
        this.conClickListener = conClickListener;

        LogUtil.INSTANCE.info("birdgangguides" , "PagerGuideAdapter > guideType : " + guideType);

        if (StringUtils.equals(guideType, GuideViewEntry.GuidesType.INTRO.getType())) {
            createIntroFragments();
        }
        else if (StringUtils.equals(guideType, GuideViewEntry.GuidesType.PLAYER.getType())) {
            createPlayerGuideFragments();
        }

    }


    private void createIntroFragments() {
        for (int i = 0; i < guideEntries.size(); i++) {
            Fragment fragment = GuideIntroFragment.newInstance(guideEntries.get(i), conClickListener);
            if (fragment != null) {
                fragments.add(fragment);
            }
        }
    }


    private synchronized void createPlayerGuideFragments() {
        for (int i = 0; i < guideEntries.size(); i++) {
            Fragment fragment = GuidePlayerFragment.newInstance(guideEntries.get(i), conClickListener);
            if (fragment != null) {
                fragments.add(fragment);
            }
        }
    }


    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

}
