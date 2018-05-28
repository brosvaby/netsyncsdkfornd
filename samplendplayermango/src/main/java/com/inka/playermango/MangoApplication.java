package com.inka.playermango;

import com.inka.netsync.BaseApplication;
import com.inka.playermango.db.DatabaseOpenHelperEx;

import java.util.ArrayList;
import java.util.List;

public class MangoApplication extends BaseApplication {

    /***
     *
     */
    @Override
    public void provideInit() {
        new DatabaseOpenHelperEx(getContext()).init();
    }

    /***
     *
     * @return
     */
    public static List<String> provideEnableDeviceModels() {
//        return new ArrayList<String>(Arrays.asList("LG-V498S2", "LG-V498", "LG-V498S1", "LG-V400S1", "LG-V525S1", "LG-V525S3"));
        return new ArrayList<>();
    }

    /**
     *
     * @return
     */
    @Override
    protected String provideApplicationContentId() {
        return "W7WMZUZ236AA2B76@pallycon.sd";
    }

    /**
     *
     * @return
     */
    @Override
    protected String provideExternalSdPath() {
        return getString(R.string.external_path);
    }

    /**
     *
     * @return
     */
    @Override
    protected String provideCardManufacturer() {
        return "284245";
    }

    /**
     *
     * @return
     */
    @Override
    protected String provideHomeWebViewUrl() {
        return getString(R.string.mango_home_url);
    }

    /**
     *
     * @return
     */
    @Override
    protected String provideSubHomeWebViewUrl() {
        return getString(R.string.mango_subhome_url);
    }

    /**
     *
     * @return
     */
    @Override
    protected String providePrivacyPolicyUrl() {
        return getString(R.string.mango_privacy_policy_url);
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
