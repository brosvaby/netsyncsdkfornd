package com.inka.netsync.view.adapter.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.inka.netsync.R2;

import butterknife.BindView;

public class ListSettingViewHolder extends BaseViewHolder {

    private final int viewType;

    @BindView(R2.id.layout_root)
    LinearLayout layoutRoot;

    @BindView(R2.id.list_text_title)
    TextView textTitle;

    @BindView(R2.id.list_text_value)
    TextView txtItemValue;

    @BindView(R2.id.list_img_arrow)
    ImageView imgArrow;

    public ListSettingViewHolder(View itemView, int viewType) {
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

    public TextView getTxtItemValue() {
        return txtItemValue;
    }

    public ImageView getImgArrow() {
        return imgArrow;
    }

}
