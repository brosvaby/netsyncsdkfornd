package com.inka.netsync.view.adapter.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.inka.netsync.R2;

import butterknife.BindView;

/**
 * Created by birdgang on 2017. 4. 21..
 */

public class ListDrawerItemViewHolder extends BaseViewHolder {

    private final int viewType;

    @BindView(R2.id.layout_root)
    RelativeLayout layoutRoot;

    @BindView(R2.id.iv_drawermenu_icon)
    ImageView imageDrawerMenuIcon;

    @BindView(R2.id.iv_drawermenu_selected_icon)
    ImageView imageDrawerMenuSelectedIcon;

    @BindView(R2.id.tv_drawermenu_title)
    TextView textDrawerMenuTitle;

    @BindView(R2.id.v_disable)
    View vDisable;

    public ListDrawerItemViewHolder(View itemView, int viewType) {
        super(itemView);
        this.viewType = viewType;
    }

    public int getViewType() {
        return viewType;
    }

    public RelativeLayout getLayoutRoot() {
        return layoutRoot;
    }

    public ImageView getImageDrawerMenuIcon() {
        return imageDrawerMenuIcon;
    }

    public ImageView getImageDrawerMenuSelectedIcon() {
        return imageDrawerMenuSelectedIcon;
    }

    public TextView getTextDrawerMenuTitle() {
        return textDrawerMenuTitle;
    }

    public View getvDisable() {
        return vDisable;
    }
}