package com.inka.netsync.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.inka.netsync.R;
import com.inka.netsync.common.bus.ContentFavoriteClickListener;
import com.inka.netsync.common.bus.ContentItemClickListener;
import com.inka.netsync.common.utils.StringUtil;
import com.inka.netsync.logs.LogUtil;
import com.inka.netsync.view.CheckableLinearLayout;
import com.inka.netsync.view.adapter.holder.BaseViewHolder;
import com.inka.netsync.view.adapter.holder.ListFavoriteViewHolder;
import com.inka.netsync.view.model.FavoriteViewEntry;

import org.apache.commons.lang3.StringUtils;

import java.util.List;


public class ListFavoriteAdapter extends HeaderFooterRecyclerViewAdapter implements View.OnClickListener {

    private final String TAG = "ListFavoriteAdapter";

    private Context context;

    private List<FavoriteViewEntry> favoriteEntries;

    private ContentFavoriteClickListener contentFavoriteClickListener = null;
    private ContentItemClickListener mContentItemClickListener = null;

    public ListFavoriteAdapter (Context context, List<FavoriteViewEntry> items, ContentItemClickListener contentItemClickListener, ContentFavoriteClickListener contentFavoriteClickListener) {
        this.context = context;
        this.favoriteEntries = items;
        this.mContentItemClickListener = contentItemClickListener;
        this.contentFavoriteClickListener = contentFavoriteClickListener;
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
        return (null == favoriteEntries) ? 0 : favoriteEntries.size();
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
        BaseViewHolder viewHolder = new ListFavoriteViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_sd_favorite, parent, false), contentViewType);
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
            FavoriteViewEntry item = favoriteEntries.get(position);
            fillContentMediaInfo(item, (ListFavoriteViewHolder) contentViewHolder, position);
        } catch (Exception e) {
            LogUtil.INSTANCE.error(getClass().getSimpleName(), e);
        }
    }


    private void fillContentMediaInfo (final FavoriteViewEntry favoriteEntry, ListFavoriteViewHolder holder, int position) throws Exception {
        CheckableLinearLayout containerRoot = holder.getLayoutRoot();
        containerRoot.setOnClickListener(this);
        containerRoot.setTag(position);
        containerRoot.setEnabled(true);

        String contentName = StringUtil.removeAllExtension(favoriteEntry.getContentName());
        String contentFormatName = StringUtil.removeExtension(favoriteEntry.getContentName());

        LogUtil.INSTANCE.info("birdgangmedia", " contentName : " + contentName);

        ImageView imageFileItemImage = holder.getIvFileItemImage();
        imageFileItemImage.setScaleType(ImageView.ScaleType.FIT_XY);
        if (StringUtils.contains(contentFormatName, "mp4")) {
            imageFileItemImage.setImageResource(R.drawable.img_video);
        } else if (StringUtils.contains(contentFormatName, "mp3")) {
            imageFileItemImage.setImageResource(R.drawable.img_mp3);
        }

        TextView textDirectoryTitle = holder.getTvItemName();
        textDirectoryTitle.setText(contentName);
        textDirectoryTitle.setFocusable(true);
        textDirectoryTitle.setClickable(false);

        TextView textMediaFilePath = holder.getTvItemFilePath();
        textMediaFilePath.setText(StringUtil.extractContentDirctoryFullPath(favoriteEntry.getContentPath()));
        textMediaFilePath.setFocusable(true);
        textMediaFilePath.setClickable(false);

        final ToggleButton toggleButton = holder.getToggleFavorite();
        toggleButton.setTag(favoriteEntry);
        toggleButton.setFocusable(false);
        toggleButton.setChecked(true);
        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null == contentFavoriteClickListener) {
                    return;
                }
                try {
                    int contentId = favoriteEntry.getContentId();
                    contentFavoriteClickListener.onItemClick(toggleButton.isChecked(), contentId);
                } catch (Exception e) {
                    LogUtil.INSTANCE.error(TAG, e);
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        try {
            if (mContentItemClickListener == null) {
                return;
            }
            LogUtil.INSTANCE.info("birdgangstorage", "v tag : " + v.getTag());
            mContentItemClickListener.onItemClick(v);
        } catch (Exception e) {
        }
    }

}
