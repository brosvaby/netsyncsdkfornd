package com.inka.netsync.view.adapter.holder;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.inka.netsync.R2;

import butterknife.BindView;

public class ListNavigationPathViewHolder extends BaseViewHolder {

    private final int viewType;

    @BindView(R2.id.layout_root)
    LinearLayout layoutRoot;

    @BindView(R2.id.tv_navigation_path_name)
    TextView textNavigationPathName;

    @BindView(R2.id.tv_navigation_divider)
    TextView textNavigationDivider;

    public ListNavigationPathViewHolder(View itemView, int viewType) {
        super(itemView);
        this.viewType = viewType;
    }

    public int getViewType() {
        return viewType;
    }

    public LinearLayout getLayoutRoot() {
        return layoutRoot;
    }

    public TextView getTextNavigationPathName() {
        return textNavigationPathName;
    }

    public TextView getTextNavigationDivider() {
        return textNavigationDivider;
    }

}
