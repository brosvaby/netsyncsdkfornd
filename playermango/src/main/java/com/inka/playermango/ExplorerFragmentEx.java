package com.inka.playermango;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.inka.ncg.nduniversal.hidden.CertificationHelper;
import com.inka.netsync.BaseConfiguration;
import com.inka.netsync.common.IntentAction;
import com.inka.netsync.common.IntentParams;
import com.inka.netsync.common.utils.StringUtil;
import com.inka.netsync.logs.LogUtil;
import com.inka.netsync.media.MediaStorage;
import com.inka.netsync.ncg.model.PlayerEntry;
import com.inka.netsync.sd.ui.fragment.SDExplorerFragment;
import com.inka.netsync.view.dialog.CustomAlertDialog;

/**
 * 미디어 목록 화면 입니다.
 * Created by birdgang on 2017. 4. 24..
 */
public class ExplorerFragmentEx extends SDExplorerFragment {

    private final String TAG = "ExplorerFragmentEx";

    public static ExplorerFragmentEx newInstance(String fragmentTag_key, String fragmentTag, String fragmentName_key, String fragmentName, String storageType) {
        ExplorerFragmentEx newFragment = new ExplorerFragmentEx();
        Bundle args = new Bundle();
        args.putString(fragmentTag_key, fragmentTag);
        args.putString(fragmentName_key, fragmentName);
        args.putString(MediaStorage.TYPE_STORAGE, storageType);
        newFragment.setArguments(args);
        return newFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null && args.containsKey(MediaStorage.TYPE_STORAGE)) {
            mStrStorageType = args.getString(MediaStorage.TYPE_STORAGE);
        }
    }


    /**
     * 디렉토리 탐색기 컬러를 재정의 합니다.
     * @return
     */
    @Override
    protected int provideNavigationBackgroundColor () {
        return R.color.provider_color_actionbar_main_bg;
    }


    /**
     * 강의 진도율 프로그래스 바 컬러를 재정의 합니다.
     * @return
     */
    @Override
    protected int provideProgressBarForLmsDrawable () {
        return R.drawable.mango_progress_lms;
    }


    /**
     * 강의 진도율 활성화 컬러를 재정의 합니다.
     * @return
     */
    @Override
    protected int provideTextRateDimColorForLms () {
        return R.color.provider_color_text_rate_for_lms_dim;
    }


    /**
     * 강의 진도율 비활성화 컬러를 재정의 합니다.
     * @return
     */
    @Override
    protected int provideTextRateColorForLms () {
        return R.color.provider_color_text_rate_for_lms;
    }

    /**
     * 검색 Activity를 지정 합니다.
     * @return
     */
    @Override
    protected Class<?> provideSearchView() {
        return SearchActivityEx.class;
    }


    /**
     * 재생 시도 후처리 Callback method 입니다.
     * @param playerEntry
     */
    @Override
    public void onLoadPlaybackActivity(PlayerEntry playerEntry) {
        boolean isSuccess = playerEntry.isSuccess();
        if (!isSuccess) {
            String errorMessage = playerEntry.getErrorMessage();
            if (StringUtil.isNotBlank(errorMessage) && getActivity().isFinishing() == false) {
                new CustomAlertDialog(getActivity())
                        .setConfirmText(getString(R.string.dialog_ok))
                        .setTitleText(getActivity().getString(R.string.dialog_title_error))
                        .setContentText(errorMessage)
                        .setConfirmBtnColoer(BaseConfiguration.getDefault().getAppDialogBtnColor())
                        .setConfirmClickListener(new CustomAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(CustomAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                            }
                        })
                        .show();
            }
        }
        else {
            Toast.makeText(getActivity(), getString(R.string.player_function_start_play), Toast.LENGTH_SHORT).show();

            int contentId = playerEntry.getContentId();
            String filePath = playerEntry.getFilePath();
            String playerType = playerEntry.getPlayerType();
            String playType = playerEntry.getPlayType();
            String userId = playerEntry.getUserId();
            String swXPlay = playerEntry.getSwXPlay();
            boolean shownPlayerGuide = playerEntry.isShownPlayerGuide();

            try {
                Intent intent = new Intent(getActivity(), PlayerActivityEx.class);
                intent.setAction(IntentAction.INTENT_ACTION_SDCARD_MEDIA);
                intent.putExtra(IntentParams.MEDIA_ITEM_LOCATION, filePath);
                intent.putExtra(IntentParams.USER_ID, userId);
                intent.putExtra(IntentParams.MEDIA_CONTENT_ID, contentId);
                intent.putExtra(IntentParams.MEDIA_PLAYER_TYPE, playerType);
                intent.putExtra(IntentParams.MEDIA_SWX_PLAY, swXPlay);
                intent.putExtra(IntentParams.MEDIA_PLAY_TYPE, playType);
                intent.putExtra(IntentParams.MEDIA_SHOWN_PLAYER_GUIDE, shownPlayerGuide);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                getActivity().startActivityForResult(intent, ACT_PLAYER);
            } catch (Exception e) {
                LogUtil.INSTANCE.error(TAG, e);
            }
        }
    }

}