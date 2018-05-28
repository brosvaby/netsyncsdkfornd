package com.inka.netsync.view.adapter.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.inka.netsync.R;
import com.inka.netsync.common.bus.ClickListener;
import com.inka.netsync.logs.LogUtil;
import com.inka.netsync.view.model.GuideViewEntry;

import org.apache.commons.lang3.StringUtils;

public class GuideIntroFragment extends Fragment {

    private final static String GUIDE_INSTANCE = "GUIDE_INSTANCE";
    private GuideViewEntry guideEntry;

    private static ClickListener conClickListener;

    public static GuideIntroFragment newInstance(GuideViewEntry guideEntry, ClickListener listener) {
        conClickListener = listener;

        GuideIntroFragment fragment = new GuideIntroFragment();
        Bundle localBundle = new Bundle();
        localBundle.putParcelable(GUIDE_INSTANCE, guideEntry);
        fragment.setArguments(localBundle);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (guideEntry != null) {
            outState.putParcelable(GUIDE_INSTANCE, guideEntry);
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_guide_view, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState != null) {
            guideEntry = savedInstanceState.getParcelable(GUIDE_INSTANCE);
        } else {
            guideEntry = getArguments().getParcelable(GUIDE_INSTANCE);
        }
        if (guideEntry == null) {
            throw new NullPointerException("Guide Model some how went nuts and become null or was it?.");
        }

        initViews(view);
    }

    private void initViews(View view) {
        TextView titleView = (TextView) view.findViewById(R.id.title);

        TextView descriptionView = (TextView) view.findViewById(R.id.description);

        ImageView imageView = (ImageView) view.findViewById(R.id.img_guide);

        RelativeLayout containerNext = (RelativeLayout) view.findViewById(R.id.container_next);
        containerNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int position = guideEntry.getPosition();
                    v.setTag(position);

                    if (null != conClickListener) {
                        conClickListener.onItemClick(v);
                    }
                } catch (Exception e) {
                    LogUtil.INSTANCE.error("error", e);
                }
            }
        });

        TextView textNext = (TextView) containerNext.findViewById(R.id.btn_next);

        String guideType = guideEntry.getGuideType();

        if (StringUtils.equals(guideType, GuideViewEntry.GuidesType.INTRO.getType())) {
            if (guideEntry.getPosition() == 0) {
                titleView.setText(getString(R.string.guide_text_title_ndplayer));
                descriptionView.setText(getString(R.string.guide_text_description_ndplayer));
                imageView.setImageResource(R.drawable.img_onboarding_first);
                textNext.setText(getString(R.string.guide_text_next));
            } else {
                titleView.setText(getString(R.string.guide_text_title_ndplayer_2));
                descriptionView.setText(getString(R.string.guide_text_description_ndplayer_2));
                imageView.setImageResource(R.drawable.img_onboarding_second);
                textNext.setText(getString(R.string.guide_text_start));
            }
        }
        else if (StringUtils.equals(guideType, GuideViewEntry.GuidesType.PLAYER.getType())) {
            if (guideEntry.getPosition() == 0) {
                titleView.setText(getString(R.string.guide_text_title_ndplayer));
                descriptionView.setText(getString(R.string.guide_text_description_ndplayer));
                imageView.setImageResource(R.drawable.img_onboarding_first);
                textNext.setText(getString(R.string.guide_text_next));
            } else {
                titleView.setText(getString(R.string.guide_text_title_ndplayer_2));
                descriptionView.setText(getString(R.string.guide_text_description_ndplayer_2));
                imageView.setImageResource(R.drawable.img_onboarding_second);
                textNext.setText(getString(R.string.guide_text_start));
            }
        }

    }

    private void setTextSizes() {
    }

}