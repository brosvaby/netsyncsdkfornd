package com.inka.netsync.view.adapter.holder;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.inka.netsync.R2;
import com.inka.netsync.view.widget.CircleAnimatedCheckBox;

import butterknife.BindView;

public class ListSingleCheckViewHolder extends BaseViewHolder {

    private final int viewType;

    @BindView(R2.id.layout_root)
    RelativeLayout layoutRoot;

    @BindView(R2.id.check_single_select_title)
    TextView tvItemTitle;

    @BindView(R2.id.check_single_select_value)
    CircleAnimatedCheckBox checkItemValue;

    public ListSingleCheckViewHolder(View itemView, int viewType) {
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

    public CircleAnimatedCheckBox getCheckItemValue() {
        return checkItemValue;
    }

}
