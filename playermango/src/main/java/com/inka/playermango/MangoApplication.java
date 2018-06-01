package com.inka.playermango;

import com.inka.netsync.BaseApplication;
import com.inka.playermango.db.DatabaseOpenHelperEx;

import java.util.ArrayList;
import java.util.List;

public class MangoApplication extends BaseApplication {

    /***
     * 앱 초기화 과정
     * 밧듯이 해야 합니다.
     */
    @Override
    public void provideInit() {
        new DatabaseOpenHelperEx(getContext()).init();
    }

    /**
     * 템플릿 Application 영역에 하기 메소드를 오버라이드 하여 제한될 디바이스 목록을 지정 하십시오.
     * 그러면 해당 명을 가진 디바이스 외에 다른 디바이스 에서는 실행이 제한 되어 집니다.
     * 해당 항목이 비어있을 경우에는 디바이스 제한을 하지 않습니다.
     **/
    public static List<String> provideEnableDeviceModels() {
        return new ArrayList<>();
    }

    /**
     * 템플릿 Application 영역에 하기 메소드를 오버라이드 하여 제한될 컨텐츠의 고유 번호를 지정 하십시오.
     * 해당 항목이 비어있을 경우에는 컨텐츠 목록이 보여지지 않거나 원할한 진행이 되지 않을 수 있기 때문에 반드시 입력을 해야 하는 값입니다.
     * 관련된 값은 INKA 에서 제공 받을 수 있습니다.
     **/
    @Override
    protected String provideApplicationContentId() {
        return getString(R.string.mango_content_id);
    }

    /**
     * 템플릿 Application 영역에 하기 메소드를 오버라이드 하여 제한될 SD Card type을 지정 하십시오.
     * 해당 항목이 비어있을 경우에는 인증이 실패하거나 원할한 진행이 되지 않을 수 있기 때문에 반드시 입력을 해야 하는 값입니다.
     * 관련된 값은 INKA 에서 제공 받을 수 있습니다.
     **/
    @Override
    protected String provideCardManufacturer () {
        return getString(R.string.mango_card_manufacturer);
    }

    /**
     * 템플릿 Application 영역에 하기 메소드를 오버라이드 하여 온라인 상태일 경우 홈 영역에 보여질 URL 을 지정 하십시오.
     * 해당 항목이 비어있을 경우에는 홍 영역을 제대로 표시 할 수 없습니다.
     **/
    @Override
    protected String provideHomeWebViewUrl() {
        return getString(R.string.mango_home_url);
    }

    /**
     * 템플릿 Application 영역에 하기 메소드를 오버라이드 하여 오프라인 상태일 경우 홈 영역에 보여질 Path 를 지정 하십시오.
     * 해당 항목이 비어있을 경우에는 홍 영역을 제대로 표시 할 수 없습니다.
     **/
    @Override
    protected String provideSubHomeWebViewUrl() {
        return getString(R.string.mango_subhome_url);
    }

    /**
     * 템플릿 Application 영역에 하기 메소드를 오버라이드 하여 개인정보 취급 방침에 대한 URL 을 지정 하십시오.
     * 해당 항목이 비어있을 경우에는 정상적으로 표시 되어 지지 않습니다.
     **/
    @Override
    protected String providePrivacyPolicyUrl() {
        return getString(R.string.mango_privacy_policy_url);
    }

    /**
     * 팝업 버튼 컬러를 지정 합니다.
     * @return
     */
    @Override
    protected int provideDialogBtnColor() {
        return R.color.provider_dialog_button_bg_color;
    }

}
