package com.inka.netsync.view.adapter.holder;

import android.view.View;
import android.widget.TextView;

import com.inka.netsync.R2;

import butterknife.BindView;

/**
 * Created by birdgang on 2018. 2. 22..
 */
public class HelpInfoChildViewHolder extends ChildViewHolder {

    private final int viewType;

    @BindView(R2.id.notice_content)
    TextView noticeContent;

    public HelpInfoChildViewHolder(View itemView, int viewType) {
        super(itemView);
        this.viewType = viewType;
    }

    public int getViewType() {
        return viewType;
    }

    public void setNoticeContent(String text) {
        this.noticeContent.setText(text);
    }

}
