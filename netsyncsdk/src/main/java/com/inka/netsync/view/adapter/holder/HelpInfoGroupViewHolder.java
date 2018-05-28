package com.inka.netsync.view.adapter.holder;

import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.inka.netsync.R;
import com.inka.netsync.R2;
import com.inka.netsync.logs.LogUtil;
import com.inka.netsync.view.model.ExpandableGroupEntry;
import com.inka.netsync.view.model.HelpInfoGroupViewEntry;

import butterknife.BindView;

/**
 * Created by birdgang on 2018. 2. 22..
 */
public class HelpInfoGroupViewHolder extends GroupViewHolder {

    private final int viewType;

    @BindView(R2.id.root)
    LinearLayout rootContainer;

    @BindView(R2.id.parent_layout)
    RelativeLayout ContainerNotice;

    @BindView(R2.id.notice_title)
    TextView noticeTitle;

    @BindView(R2.id.new_img)
    ImageView newImg;

    @BindView(R2.id.arrow_img)
    ImageView viewImage;

    public HelpInfoGroupViewHolder(View itemView, int viewType) {
        super(itemView);
        this.viewType = viewType;
    }

    public int getViewType() {
        return viewType;
    }

    public void setGenreTitle(ExpandableGroupEntry genre) {
        if (genre instanceof HelpInfoGroupViewEntry) {
            noticeTitle.setText(genre.getTitle());
            noticeTitle.setTextColor(Color.parseColor("#ff000000"));
        }
    }

    @Override
    public void expand() {
        animateExpand();
    }

    @Override
    public void collapse() {
        animateCollapse();
    }

    private void animateExpand() {
        viewImage.setImageResource(R.drawable.img_guide_arrow_opened);
        noticeTitle.setTypeface(null, Typeface.BOLD);
        noticeTitle.setTextColor(Color.parseColor("#ff000000"));
    }

    private void animateCollapse() {
        LogUtil.INSTANCE.info("birdganggrouplist" , "animateCollapse");
        viewImage.setImageResource(R.drawable.img_guide_arrow_closed);
        noticeTitle.setTypeface(null, Typeface.BOLD);
        noticeTitle.setTextColor(Color.parseColor("#88000000"));
    }


}
