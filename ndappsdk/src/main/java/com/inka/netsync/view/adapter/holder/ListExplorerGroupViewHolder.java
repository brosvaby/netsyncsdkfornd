package com.inka.netsync.view.adapter.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.inka.netsync.R2;
import com.inka.netsync.view.CheckableLinearLayout;

import butterknife.BindView;

public class ListExplorerGroupViewHolder extends BaseViewHolder {

    private final int viewType;

    @BindView(R2.id.layout_root)
    CheckableLinearLayout layoutRoot;

    @BindView(R2.id.iv_fileitem_image)
    ImageView ivFileItemImage;

    @BindView(R2.id.tv_fileitem_register_ymdt)
    TextView tvItemRegisterYmdt;

    @BindView(R2.id.tv_fileitem_name)
    TextView tvItemName;

    public ListExplorerGroupViewHolder(View itemView, int viewType) {
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

    public TextView getTvItemRegisterYmdt() {
        return tvItemRegisterYmdt;
    }

    public TextView getTvItemName() {
        return tvItemName;
    }

}
