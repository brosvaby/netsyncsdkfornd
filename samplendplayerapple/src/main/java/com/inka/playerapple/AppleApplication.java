package com.inka.playerapple;

import com.inka.netsync.BaseApplication;
import com.inka.playerapple.db.DatabaseOpenHelperEx;

public class AppleApplication extends BaseApplication {

    /***
     *
     */
    @Override
    public void provideInit() {
        new DatabaseOpenHelperEx(getContext()).init();
    }


    /**
     * 템플릿 Application 영역에 하기 메소드를 오버라이드 하여 제한될 컨텐츠의 고유 번호를 지정 하십시오.
     * 해당 항목이 비어있을 경우에는 컨텐츠 목록이 보여지지 않거나 원할한 진행이 되지 않을 수 있기 때문에 반듯이 입력을 해야 하는 값입니다.
     * 관련된 값은 INKA 에서 제공 받을 수 있습니다.
     **/
    @Override
    protected String provideApplicationContentId() {
        return "";
    }

    /**
     *
     * @return
     */
    @Override
    protected String providePrivacyPolicyUrl() {
        return getString(R.string.privacy_policy_url);
    }

    /**
     *
     * @return
     */
    @Override
    protected int provideDialogBtnColor() {
        return R.color.provider_dialog_button_bg_color;
    }


}