package com.inka.netsync.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.inka.netsync.R;
import com.inka.netsync.common.bus.ContentItemClickListener;
import com.inka.netsync.common.utils.StringUtil;
import com.inka.netsync.logs.LogUtil;
import com.inka.netsync.view.CheckableLinearLayout;
import com.inka.netsync.view.adapter.holder.BaseViewHolder;
import com.inka.netsync.view.adapter.holder.ListRecentlyPlayedViewHolder;
import com.inka.netsync.view.model.RecentlyViewEntry;

import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.List;

public class ListRecentlyPlayedAdapter extends HeaderFooterRecyclerViewAdapter implements View.OnClickListener {

    private final String TAG = "ListRecentlyPlayedAdapter";

    private Context context;

    private List<RecentlyViewEntry> playedListEntries;

    private ContentItemClickListener mContentItemClickListener = null;

    public ListRecentlyPlayedAdapter(Context context, List<RecentlyViewEntry> items, ContentItemClickListener contentItemClickListener) {
        this.context = context;
        this.playedListEntries = items;
        this.mContentItemClickListener = contentItemClickListener;
    }

    public void setDataForLMS (List<RecentlyViewEntry> items) {
        this.playedListEntries = items;
        notifyDataSetChanged();
    }

    @Override
    protected int getHeaderItemCount() {
        return 0;
    }

    @Override
    protected int getFooterItemCount() {
        return 0;
    }

    @Override
    protected int getContentItemCount() {
        return (null == playedListEntries) ? 0 : playedListEntries.size();
    }

    @Override
    protected RecyclerView.ViewHolder onCreateHeaderItemViewHolder(ViewGroup parent, int headerViewType) {
        return null;
    }

    @Override
    protected RecyclerView.ViewHolder onCreateFooterItemViewHolder(ViewGroup parent, int footerViewType) {
        return null;
    }

    @Override
    protected RecyclerView.ViewHolder onCreateContentItemViewHolder(ViewGroup parent, int contentViewType) {
        BaseViewHolder viewHolder = new ListRecentlyPlayedViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_sd_recently_played, parent, false), contentViewType);
        return viewHolder;
    }

    @Override
    protected void onBindHeaderItemViewHolder(RecyclerView.ViewHolder headerViewHolder, int position) {
    }

    @Override
    protected void onBindFooterItemViewHolder(RecyclerView.ViewHolder footerViewHolder, int position) {
    }

    @Override
    protected void onBindContentItemViewHolder(RecyclerView.ViewHolder contentViewHolder, int position) {
        try {
            RecentlyViewEntry item = playedListEntries.get(position);
            fillContentContentInfo(item, (ListRecentlyPlayedViewHolder) contentViewHolder, position);
        } catch (Exception e) {
            LogUtil.INSTANCE.error(getClass().getSimpleName(), e);
        }
    }

    private void fillContentContentInfo (final RecentlyViewEntry playedListEntry, ListRecentlyPlayedViewHolder holder, int position) throws Exception {
        CheckableLinearLayout containerRoot = holder.getContainerRoot();
        containerRoot.setOnClickListener(this);
        containerRoot.setTag(position);
        containerRoot.setEnabled(true);

        if (null == playedListEntry) {
            return;
        }

        String contentName = StringUtil.removeAllExtension(playedListEntry.getContentName());
        String contentFormatName = StringUtil.removeExtension(playedListEntry.getContentName());
        String rate = playedListEntry.getLmsRate();
        Date lastPlayDate = playedListEntry.getPlayDateToDate();

        TextView textPlayedContentName = holder.getTvItemName();
        textPlayedContentName.setText(contentName);
        textPlayedContentName.setFocusable(true);
        textPlayedContentName.setClickable(false);

        TextView textPlayedDate = holder.getTvItemData();
        textPlayedDate.setText(lastPlayDate.toLocaleString());
        textPlayedDate.setFocusable(true);
        textPlayedDate.setClickable(false);

        ImageView imageFileItemImage = holder.getIvFileItemImage();
        imageFileItemImage.setScaleType(ImageView.ScaleType.FIT_XY);
        if (StringUtils.contains(contentFormatName, "mp4")) {
            if (StringUtils.equals(rate, "100")) {
                imageFileItemImage.setImageResource(R.drawable.img_video_played);
            }
            else {
                imageFileItemImage.setImageResource(R.drawable.img_video);
            }
        } else if (StringUtils.contains(contentFormatName, "mp3")) {
            if (StringUtils.equals(rate, "100")) {
                imageFileItemImage.setImageResource(R.drawable.img_mp3_played);
            }
            else {
                imageFileItemImage.setImageResource(R.drawable.img_mp3);
            }
        } else if (StringUtils.contains(contentFormatName, "pdf") || StringUtils.contains(contentFormatName, "PDF")) {
            imageFileItemImage.setImageResource(R.drawable.img_doc);
        }
    }

    @Override
    public void onClick(View v) {
        try {
            if (mContentItemClickListener == null) {
                return;
            }
            mContentItemClickListener.onItemClick(v);
        } catch (Exception e) {
        }
    }
}
