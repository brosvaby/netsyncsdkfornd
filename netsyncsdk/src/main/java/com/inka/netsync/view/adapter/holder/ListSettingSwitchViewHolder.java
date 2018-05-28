package com.inka.netsync.view.adapter.holder;

import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.inka.netsync.R2;

import butterknife.BindView;

public class ListSettingSwitchViewHolder extends BaseViewHolder {

    private final int viewType;

    @BindView(R2.id.layout_root)
    LinearLayout layoutRoot;

    @BindView(R2.id.list_text_title)
    TextView textTitle;

    @BindView(R2.id.list_switch_allow)
    SwitchCompat switchCompat;


    public ListSettingSwitchViewHolder(View itemView, int viewType) {
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

    public SwitchCompat getSwitchCompat() {
        return switchCompat;
    }


}
