package com.inka.netsync.view.adapter.fragment;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.inka.netsync.R;
import com.inka.netsync.common.bus.ClickListener;
import com.inka.netsync.logs.LogUtil;
import com.inka.netsync.view.model.GuideViewEntry;

import java.lang.reflect.Method;

public class GuidePlayerFragment extends Fragment {

    private final String TAG = "GuidePlayerFragment";

    private final static String GUIDE_INSTANCE = "GUIDE_INSTANCE";

    private GuideViewEntry guideEntry;

    private static ClickListener conClickListener;

    private RelativeLayout mContainerGuidePortrait;
    private RelativeLayout mContainerGuideLandscape;


    public static GuidePlayerFragment newInstance(GuideViewEntry guideEntry, ClickListener listener) {
        conClickListener = listener;

        GuidePlayerFragment fragment = new GuidePlayerFragment();
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            guideEntry = savedInstanceState.getParcelable(GUIDE_INSTANCE);
        } else {
            guideEntry = getArguments().getParcelable(GUIDE_INSTANCE);
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int position = guideEntry.getPosition();
        View view = null;
        if (0 == position) {
            view = inflater.inflate(R.layout.fragment_guide_player_view_first, container, false);
            mContainerGuidePortrait = (RelativeLayout) view.findViewById(R.id.container_guide_first_portrait);
            mContainerGuideLandscape = (RelativeLayout) view.findViewById(R.id.container_guide_first_landscape);
        } else {
            view = inflater.inflate(R.layout.fragment_guide_player_view_second, container, false);
            mContainerGuidePortrait = (RelativeLayout) view.findViewById(R.id.container_guide_second_portrait);
            mContainerGuideLandscape = (RelativeLayout) view.findViewById(R.id.container_guide_second_landscape);

            Button btnConfirm = (Button) view.findViewById(R.id.confirm_button);
            btnConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = guideEntry.getPosition();
                    v.setTag(position);
                    if (null != conClickListener) {
                        conClickListener.onItemClick(v);
                    }
                }
            });
        }
        LogUtil.INSTANCE.info("birdgangguides" , "GuidePlayerFragment > position : " + position);

        int orientation = getScreenOrientation();
        setVisibleGuide(orientation);

        return view;
    }


    private void setVisibleGuide (int orientation) {
        LogUtil.INSTANCE.info("birdgangguides" , "setVisibleGuide > orientation : " + orientation);
        if (orientation == Surface.ROTATION_0 || orientation == Surface.ROTATION_180) {
            mContainerGuidePortrait.setVisibility(View.GONE);
            mContainerGuideLandscape.setVisibility(View.VISIBLE);
        } else if (orientation == Surface.ROTATION_90 || orientation == Surface.ROTATION_270) {
            mContainerGuidePortrait.setVisibility(View.VISIBLE);
            mContainerGuideLandscape.setVisibility(View.GONE);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setVisibleGuide(newConfig.orientation);
    }



    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    private int getScreenOrientation() {
        LogUtil.INSTANCE.info("birdganglock" , "PlayerActivity > getScreenOrientation");

        WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int rot = getScreenRotation(getActivity());

        @SuppressWarnings("deprecation")
        boolean defaultWide = display.getWidth() > display.getHeight();
        if (rot == Surface.ROTATION_90 || rot == Surface.ROTATION_270) {
            defaultWide = !defaultWide;
        }

        LogUtil.INSTANCE.info("birdganglock" , "PlayerActivity > getScreenOrientation > defaultWide : " + defaultWide + " , rot : " + rot);

        if (defaultWide) {
            switch (rot) {
                case Surface.ROTATION_0:
                    return ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                case Surface.ROTATION_90:
                    return ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                case Surface.ROTATION_180:
                    return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO ? ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE : ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                case Surface.ROTATION_270:
                    return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO ? ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT : ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                default:
                    return 0;
            }
        } else {
            switch (rot) {
                case Surface.ROTATION_0:
                    return ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                case Surface.ROTATION_90:
                    return ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                case Surface.ROTATION_180:
                    return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO ? ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT : ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                case Surface.ROTATION_270:
                    return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO ? ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE : ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                default:
                    return 0;
            }
        }
    }


    private int getScreenRotation(Context context) {
        LogUtil.INSTANCE.info("birdganglock" , "PlayerActivity > getScreenRotation");

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO /* Android 2.2 has getRotation */) {
            try {
                Method m = display.getClass().getDeclaredMethod("getRotation");
                return (Integer) m.invoke(display);
            } catch (Exception e) {
                return Surface.ROTATION_0;
            }
        } else {
            return display.getOrientation();
        }
    }

}