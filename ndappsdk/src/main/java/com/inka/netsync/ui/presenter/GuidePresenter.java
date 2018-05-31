package com.inka.netsync.ui.presenter;

import com.inka.netsync.R;
import com.inka.netsync.data.DataManager;
import com.inka.netsync.ui.mvppresenter.GuideMvpPresenter;
import com.inka.netsync.ui.mvpview.GuideMvpView;
import com.inka.netsync.view.model.GuideViewEntry;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public class GuidePresenter<V extends GuideMvpView> extends BasePresenter<V> implements GuideMvpPresenter<V> {

    @Inject
    public GuidePresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public void onViewInitialized() {
    }

    @Override
    public void requestSetHasRunIntroGuide(boolean enable) {
        getMvpView().callActivity();
    }

    @Override
    public List<GuideViewEntry> getGuideViewEntries() {
        List<GuideViewEntry> guideEntries = new ArrayList<GuideViewEntry>();

        GuideViewEntry guideEntry = new GuideViewEntry();
        guideEntry.setGuideType(GuideViewEntry.GuidesType.INTRO.getType());
        guideEntry.setPosition(0);
        guideEntry.setImageResource(R.drawable.img_onboarding_first);
        guideEntry.setTitle(getDataManager().getStringResource(R.string.guide_text_title_ndplayer));
        guideEntry.setDescription(getDataManager().getStringResource(R.string.guide_text_description_ndplayer));
        guideEntry.setBtnText("next");

        guideEntries.add(guideEntry);

        GuideViewEntry guideEntry2 = new GuideViewEntry();
        guideEntry2.setGuideType(GuideViewEntry.GuidesType.INTRO.getType());
        guideEntry2.setPosition(1);
        guideEntry2.setImageResource(R.drawable.img_onboarding_second);
        guideEntry2.setTitle(getDataManager().getStringResource(R.string.guide_text_description_ndplayer));
        guideEntry2.setDescription(getDataManager().getStringResource(R.string.guide_text_title_ndplayer));
        guideEntry2.setBtnText("start");

        guideEntries.add(guideEntry2);

        return guideEntries;
    }


}