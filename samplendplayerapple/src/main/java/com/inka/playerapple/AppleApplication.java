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