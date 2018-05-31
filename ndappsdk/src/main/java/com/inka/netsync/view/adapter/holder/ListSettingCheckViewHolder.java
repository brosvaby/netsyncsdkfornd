package com.inka.netsync.view.adapter.holder;

import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.inka.netsync.R2;

import butterknife.BindView;

public class ListSettingCheckViewHolder extends BaseViewHolder {

    private final int viewType;

    @BindView(R2.id.layout_root)
    RelativeLayout layoutRoot;

    @BindView(R2.id.check_setting_select_title)
    TextView tvItemTitle;

    @BindView(R2.id.check_setting_select_value)
    CheckBox checkItemValue;


    public ListSettingCheckViewHolder(View itemView, int viewType) {
        super(itemView);
        this.viewType = viewType;
    }

    public int getViewType() {
        return viewType;
    }

    public RelativeLayout getLayoutRoot() {
        return layoutRoot;
    }

    public TextView getTvItemTitle() {
        return tvItemTitle;
    }

    public CheckBox getCheckItemValue() {
        return checkItemValue;
    }

}
