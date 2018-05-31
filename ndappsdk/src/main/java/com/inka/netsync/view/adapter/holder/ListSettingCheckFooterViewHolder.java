package com.inka.netsync.view.adapter.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.inka.netsync.R2;

import butterknife.BindView;

public class ListSettingCheckFooterViewHolder extends BaseViewHolder {

    private final int viewType;

    @BindView(R2.id.img_setting_check_player_footer_arrow)
    ImageView imgSettingCheck;

    @BindView(R2.id.text_setting_check_player_footer_description)
    TextView textSettingCheck;

    public ListSettingCheckFooterViewHolder(View itemView, int viewType) {
        super(itemView);
        this.viewType = viewType;
    }

    public int getViewType() {
        return viewType;
    }

    public TextView getTextSettingCheck() {
        return textSettingCheck;
    }

    public void setTextSettingCheck(TextView textSettingCheck) {
        this.textSettingCheck = textSettingCheck;
    }

    public ImageView getImgSettingCheck() {
        return imgSettingCheck;
    }

    public void setImgSettingCheck(ImageView imgSettingCheck) {
        this.imgSettingCheck = imgSettingCheck;
    }

}
