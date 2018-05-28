package com.inka.netsync.view.adapter.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.inka.netsync.R2;
import com.inka.netsync.view.CheckableLinearLayout;

import butterknife.BindView;

public class ListExplorerFileNoLMSViewHolder extends BaseViewHolder {

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

    public ListExplorerFileNoLMSViewHolder(View itemView, int viewType) {
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

}
