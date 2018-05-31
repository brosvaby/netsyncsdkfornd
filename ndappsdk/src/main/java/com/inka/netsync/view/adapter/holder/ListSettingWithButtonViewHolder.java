package com.inka.netsync.view.adapter.holder;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.inka.netsync.R2;

import butterknife.BindView;

public class ListSettingWithButtonViewHolder extends BaseViewHolder {

    private final int viewType;

    @BindView(R2.id.layout_root)
    LinearLayout layoutRoot;

    @BindView(R2.id.list_text_title)
    TextView textTitle;

    @BindView(R2.id.list_text_value)
    TextView textValue;

    @BindView(R2.id.container_version_update)
    LinearLayout containerVersionUpdate;

    @BindView(R2.id.text_version_update)
    TextView textVersionUpdate;

    public ListSettingWithButtonViewHolder(View itemView, int viewType) {
        super(itemView);
        this.viewType = viewType;
    }

    public int getViewType() {
        return viewType;
    }

    public LinearLayout getLayoutRoot() {
        return layoutRoot;
    }

    public TextView getTextTitle() {
        return textTitle;
    }

    public TextView getTextValue() {
        return textValue;
    }

    public LinearLayout getContainerVersionUpdate() {
        return containerVersionUpdate;
    }

    public TextView getTextVersionUpdate() {
        return textVersionUpdate;
    }

}
