package com.inka.netsync.view.adapter.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.inka.netsync.R2;
import com.inka.netsync.view.CheckableLinearLayout;

import butterknife.BindView;

/**
 * Created by birdgang on 2017. 4. 24..
 */

public class ListFavoriteViewHolder extends BaseViewHolder {

    private final int viewType;

    @BindView(R2.id.layout_root)
    CheckableLinearLayout layoutRoot;

    @BindView(R2.id.iv_fileitem_image)
    ImageView ivFileItemImage;

    @BindView(R2.id.tv_fileitem_name)
    TextView tvItemName;

    @BindView(R2.id.tv_fileitem_data)
    TextView tvItemFilePath;

    @BindView(R2.id.is_favorite)
    ToggleButton toggleFavorite;

    public ListFavoriteViewHolder(View itemView, int viewType) {
        super(itemView);
        this.viewType = viewType;
    }

    public int getViewType() {
        return viewType;
    }

    public CheckableLinearLayout getLayoutRoot() {
        return layoutRoot;
    }

    public ImageView getIvFileItemImage() {
        return ivFileItemImage;
    }

    public TextView getTvItemName() {
        return tvItemName;
    }

    public TextView getTvItemFilePath() {
        return tvItemFilePath;
    }

    public ToggleButton getToggleFavorite() {
        return toggleFavorite;
    }
}
