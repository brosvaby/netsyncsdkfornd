package com.inka.netsync.ui.presenter;

import com.inka.netsync.R;
import com.inka.netsync.data.DataManager;
import com.inka.netsync.ui.mvppresenter.InfoMvpPresenter;
import com.inka.netsync.ui.mvpview.InfoMvpView;
import com.inka.netsync.view.model.HelpInfoChildViewEntry;
import com.inka.netsync.view.model.HelpInfoGroupViewEntry;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public class InfoPresenter<V extends InfoMvpView> extends BasePresenter<V> implements InfoMvpPresenter<V> {

    @Inject
    public InfoPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public void onViewInitialized() {
    }

    @Override
    public void requestListInfo() {
        if (!isViewAttached()) {
            return;
        }

        List<HelpInfoGroupViewEntry> infoViewEntries = Arrays.asList(
                makeQuestion1(),
                makeQuestion2(),
                makeQuestion3(),
                makeQuestion4(),
                makeQuestion5(),
                makeQuestion6(),
                makeQuestion7(),
                makeQuestion8()
        );

        if (null != infoViewEntries) {
            getMvpView().onLoadListInfo(infoViewEntries);
        }
    }


    public HelpInfoGroupViewEntry makeQuestion1() {
        return new HelpInfoGroupViewEntry(getDataManager().getStringResource(R.string.help_text_question_01), makeAnswer1(), 1);
    }

    public List<HelpInfoChildViewEntry> makeAnswer1() {
        HelpInfoChildViewEntry queen = new HelpInfoChildViewEntry(getDataManager().getStringResource(R.string.help_text_answer_01));
        return Arrays.asList(queen);
    }

    public HelpInfoGroupViewEntry makeQuestion2() {
        return new HelpInfoGroupViewEntry(getDataManager().getStringResource(R.string.help_text_question_02), makeAnswer2(), 2);
    }

    public List<HelpInfoChildViewEntry> makeAnswer2() {
        HelpInfoChildViewEntry milesDavis = new HelpInfoChildViewEntry(getDataManager().getStringResource(R.string.help_text_answer_02));
        return Arrays.asList(milesDavis);
    }

    public HelpInfoGroupViewEntry makeQuestion3() {
        return new HelpInfoGroupViewEntry(getDataManager().getStringResource(R.string.help_text_question_03), makeAnswer3(), 3);
    }

    public List<HelpInfoChildViewEntry> makeAnswer3() {
        HelpInfoChildViewEntry beethoven = new HelpInfoChildViewEntry(getDataManager().getStringResource(R.string.help_text_answer_03));
        return Arrays.asList(beethoven);
    }


    public HelpInfoGroupViewEntry makeQuestion4() {
        return new HelpInfoGroupViewEntry(getDataManager().getStringResource(R.string.help_text_question_04), makeAnswer4(), 4);
    }

    public List<HelpInfoChildViewEntry> makeAnswer4() {
        HelpInfoChildViewEntry hectorLavoe = new HelpInfoChildViewEntry(getDataManager().getStringResource(R.string.help_text_answer_04));
        return Arrays.asList(hectorLavoe);
    }

    public HelpInfoGroupViewEntry makeQuestion5() {
        return new HelpInfoGroupViewEntry(getDataManager().getStringResource(R.string.help_text_question_05), makeAnswer5(), 5);
    }

    public List<HelpInfoChildViewEntry> makeAnswer5() {
        HelpInfoChildViewEntry billMonroe = new HelpInfoChildViewEntry(getDataManager().getStringResource(R.string.help_text_answer_05));
        return Arrays.asList(billMonroe);
    }

    public HelpInfoGroupViewEntry makeQuestion6() {
        return new HelpInfoGroupViewEntry(getDataManager().getStringResource(R.string.help_text_question_06), makeAnswer6(), 5);
    }

    public List<HelpInfoChildViewEntry> makeAnswer6() {
        HelpInfoChildViewEntry billMonroe = new HelpInfoChildViewEntry(getDataManager().getStringResource(R.string.help_text_answer_06));
        return Arrays.asList(billMonroe);
    }

    public HelpInfoGroupViewEntry makeQuestion7() {
        return new HelpInfoGroupViewEntry(getDataManager().getStringResource(R.string.help_text_question_07), makeAnswer7(), 5);
    }

    public List<HelpInfoChildViewEntry> makeAnswer7() {
        HelpInfoChildViewEntry billMonroe = new HelpInfoChildViewEntry(getDataManager().getStringResource(R.string.help_text_answer_07));
        return Arrays.asList(billMonroe);
    }

    public HelpInfoGroupViewEntry makeQuestion8() {
        return new HelpInfoGroupViewEntry(getDataManager().getStringResource(R.string.help_text_question_08), makeAnswer8(), 5);
    }

    public List<HelpInfoChildViewEntry> makeAnswer8() {
        HelpInfoChildViewEntry billMonroe = new HelpInfoChildViewEntry(getDataManager().getStringResource(R.string.help_text_answer_08));
        return Arrays.asList(billMonroe);
    }

}
