package com.inka.netsync.view.adapter.holder;

import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.inka.netsync.R2;

import butterknife.BindView;

public class ListBookMarkViewHolder extends BaseViewHolder {

    private final int viewType;

    @BindView(R2.id.layout_root)
    RelativeLayout layoutRoot;

    @BindView(R2.id.bookmark_location)
    TextView tvItemLocation;

    @BindView(R2.id.bookmark_memo)
    TextView tvItemMemo;

    @BindView(R2.id.edit_area)
    RelativeLayout containerEditArea;

    @BindView(R2.id.bookmark_item_delete)
    CheckBox checkItemDelete;

    public ListBookMarkViewHolder(View itemView, int viewType) {
        super(itemView);
        this.viewType = viewType;
    }

    public int getViewType() {
        return viewType;
    }

    public RelativeLayout getLayoutRoot() {
        return layoutRoot;
    }

    public TextView getTvItemLocation() {
        return tvItemLocation;
    }

    public TextView getTvItemMemo() {
        return tvItemMemo;
    }

    public RelativeLayout getContainerEditArea() {
        return containerEditArea;
    }

    public CheckBox getCheckItemDelete() {
        return checkItemDelete;
    }

}
