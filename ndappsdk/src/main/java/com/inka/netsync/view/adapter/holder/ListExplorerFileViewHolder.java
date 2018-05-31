package com.inka.netsync.view.adapter.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.inka.netsync.R2;
import com.inka.netsync.view.CheckableLinearLayout;

import butterknife.BindView;

public class ListExplorerFileViewHolder extends BaseViewHolder {

    private final int viewType;

    @BindView(R2.id.layout_root)
    CheckableLinearLayout containerRoot;

    @BindView(R2.id.iv_fileitem_image)
    ImageView ivFileItemImage;

    @BindView(R2.id.tv_fileitem_name)
    TextView tvItemName;

    @BindView(R2.id.tv_fileitem_data)
    TextView tvItemData;

    @BindView(R2.id.tv_fileitem_data_duration)
    TextView tvItemDataDuration;

    @BindView(R2.id.favorite_area)
    LinearLayout containerFavorite;

    @BindView(R2.id.is_favorite)
    ToggleButton toggleButton;

    @BindView(R2.id.container_lms)
    RelativeLayout containerLms;

    @BindView(R2.id.lms_percent)
    TextView tvLmsPercent;

    @BindView(R2.id.lms_progess)
    ProgressBar progressBarLms;

    public ListExplorerFileViewHolder(View itemView, int viewType) {
        super(itemView);
        this.viewType = viewType;
    }

    public int getViewType() {
        return viewType;
    }

    public CheckableLinearLayout getContainerRoot() {
        return containerRoot;
    }

    public ImageView getIvFileItemImage() {
        return ivFileItemImage;
    }

    public TextView getTvItemName() {
        return tvItemName;
    }

    public TextView getTvItemData() {
        return tvItemData;
    }

    public TextView getTvItemDataDuration() {
        return tvItemDataDuration;
    }

    public LinearLayout getContainerFavorite() {
        return containerFavorite;
    }

    public ToggleButton getToggleFavorite() {
        return toggleButton;
    }

    public RelativeLayout getContainerLms() {
        return containerLms;
    }

    public TextView getTvLmsPercent() {
        return tvLmsPercent;
    }

    public ProgressBar getProgressBarLms() {
        return progressBarLms;
    }

}
