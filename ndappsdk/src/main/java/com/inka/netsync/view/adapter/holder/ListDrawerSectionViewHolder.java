package com.inka.netsync.view.adapter.holder;

import android.view.View;
import android.widget.TextView;

import com.inka.netsync.R2;

import butterknife.BindView;

public class ListDrawerSectionViewHolder extends BaseViewHolder {

    private final int viewType;

    @BindView(R2.id.tv_drawermenu_section)
    TextView textDrawerMenuSection;

    public ListDrawerSectionViewHolder(View itemView, int viewType) {
        super(itemView);
        this.viewType = viewType;
    }

    public int getViewType() {
        return viewType;
    }

    public TextView getTextDrawerMenuSection() {
        return textDrawerMenuSection;
    }

}