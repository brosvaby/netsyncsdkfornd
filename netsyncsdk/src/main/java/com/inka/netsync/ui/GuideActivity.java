package com.inka.netsync.ui;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.inka.netsync.R;
import com.inka.netsync.R2;
import com.inka.netsync.common.bus.ClickListener;
import com.inka.netsync.common.utils.ThemeUtil;
import com.inka.netsync.data.cache.pref.PreferencesCacheHelper;
import com.inka.netsync.logs.LogUtil;
import com.inka.netsync.ui.mvppresenter.GuideMvpPresenter;
import com.inka.netsync.ui.mvpview.GuideMvpView;
import com.inka.netsync.view.adapter.PagerGuideAdapter;
import com.inka.netsync.view.model.GuideViewEntry;
import com.inka.netsync.view.pager.ScrollerViewPager;
import com.inka.netsync.view.pager.indicator.SpringIndicator;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;


public class GuideActivity extends BaseActivity implements GuideMvpView {

    private final String TAG = "GuideActivity";

    private static final String PAGER_POSITION = "PAGER_POSITION";
    private static final String SYSTEM_OVERLAY_NUM_INSTANCE = "SYSTEM_OVERLAY_NUM_INSTANCE";

    @Inject
    GuideMvpPresenter<GuideMvpView> mPresenter;

    @BindView(R2.id.scroller_view_pager)
    protected ScrollerViewPager scrollerViewPager;

    @BindView(R2.id.indicator)
    protected SpringIndicator indicator;

    private Class<?> mClassMainActivity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_guide_container);

        getActivityComponent().inject(this);
        setUnBinder(ButterKnife.bind(this));
        mPresenter.onAttach(GuideActivity.this);

        setUp();
    }


    @Override
    protected void setUp() {
        scrollerViewPager.setAdapter(new PagerGuideAdapter(getSupportFragmentManager(), GuideViewEntry.GuidesType.INTRO.getType(), getEntries(), new ClickListener() {
            @Override
            public void onItemClick(View view) {
                try {
                    int position = (int) view.getTag();
                    LogUtil.INSTANCE.info(TAG, "PagerGuideAdapter > onItemClick > position : " + position);
                    if (position >= 1) {
                        PreferencesCacheHelper.setPreferenceValueForBol(PreferencesCacheHelper.HAS_RUN_INTRO_GUIDE, true);
                        startMainActivity();
                    } else {
                        scrollerViewPager.setCurrentItem(1);
                    }
                } catch (Exception e) {
                    LogUtil.INSTANCE.error(TAG, e);
                }
            }
        }));
        indicator.setViewPager(scrollerViewPager);
        scrollerViewPager.setOffscreenPageLimit(getEntries().size());
//        int color = permissions().get(0).getLayoutColor();
        int color = 0;
        if (color == 0) {
            color = ThemeUtil.getPrimaryColor(this);
        }
        scrollerViewPager.setBackgroundColor(color);
        onStatusBarColorChange(color);
        scrollerViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                int color = 0;
                if (color == 0) {
                    color = ThemeUtil.getPrimaryColor(GuideActivity.this);
                }
                animateColorChange(scrollerViewPager, color);
            }
        });
    }


    public void onStatusBarColorChange(@ColorInt int color) {
        if (color == 0) return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            float cl = 0.9f;
            float[] hsv = new float[3];
            Color.colorToHSV(color, hsv);
            hsv[2] *= cl;
            int primaryDark = Color.HSVToColor(hsv);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(primaryDark);
        }
    }


    private void animateColorChange(final View view, final int color) {
        ValueAnimator animator = new ValueAnimator();
        animator.setIntValues(((ColorDrawable) view.getBackground()).getColor(), color);
        animator.setEvaluator(new ArgbEvaluator());
        animator.setDuration(600);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                view.setBackgroundColor((Integer) animation.getAnimatedValue());
                onStatusBarColorChange((Integer) animation.getAnimatedValue());
            }
        });
        animator.start();
    }


    public List<GuideViewEntry> getEntries () {
        List<GuideViewEntry> guideEntries = new ArrayList<GuideViewEntry>();

        GuideViewEntry guideEntry = new GuideViewEntry();
        guideEntry.setGuideType(GuideViewEntry.GuidesType.INTRO.getType());
        guideEntry.setPosition(0);
        guideEntry.setImageResource(R.drawable.img_onboarding_first);
        guideEntry.setTitle(getString(R.string.guide_text_title_ndplayer));
        guideEntry.setDescription(getString(R.string.guide_text_description_ndplayer));
        guideEntry.setBtnText("next");

        guideEntries.add(guideEntry);

        GuideViewEntry guideEntry2 = new GuideViewEntry();
        guideEntry2.setGuideType(GuideViewEntry.GuidesType.INTRO.getType());
        guideEntry2.setPosition(1);
        guideEntry2.setImageResource(R.drawable.img_onboarding_second);
        guideEntry2.setTitle(getString(R.string.guide_text_description_ndplayer));
        guideEntry2.setDescription(getString(R.string.guide_text_title_ndplayer));
        guideEntry2.setBtnText("start");

        guideEntries.add(guideEntry2);


        return guideEntries;
    }

    protected void startMainActivity(Class<?> cls) throws Exception {
        try {
            Intent intent = new Intent(this, cls);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        } finally {
            finish();
        }
    }

    public void startMainActivity() {
        mPresenter.requestSetHasRunIntroGuide(true);
    }

    @Override
    public void callActivity() {
        setNextContentView();
    }


    protected void setNextContentView () {}

    protected void setNextContentView (Class<?> activity) throws Exception {
        this.mClassMainActivity = activity;
        startMainActivity(mClassMainActivity);
    }

}
