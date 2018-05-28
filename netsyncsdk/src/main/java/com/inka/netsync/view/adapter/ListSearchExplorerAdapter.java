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
import com.inka.netsync.view.adapter.holder.ListExplorerFileSearchViewHolder;
import com.inka.netsync.view.model.ContentViewEntry;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class ListSearchExplorerAdapter extends HeaderFooterRecyclerViewAdapter implements View.OnClickListener {

    private final String TAG = "ListSearchExplorerAdapter";

    private Context context;

    private List<ContentViewEntry> contentEntries;

    private ContentItemClickListener mContentItemClickListener = null;

    public ListSearchExplorerAdapter(Context context, List<ContentViewEntry> items, ContentItemClickListener contentItemClickListener) {
        this.context = context;
        this.contentEntries = items;
        this.mContentItemClickListener = contentItemClickListener;
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
        return (null == contentEntries) ? 0 : contentEntries.size();
    }

    public void setData (List<ContentViewEntry> contentEntries) {
        this.contentEntries = contentEntries;
        notifyDataSetChanged();
    }


    public ContentViewEntry getContentEntryByPosition (int position) {
        if (getContentItemCount() < 1) {
            return null;
        }
        return contentEntries.get(position);
    }


    @Override
    protected int getContentItemViewType(int position) {
        super.getContentItemViewType(position);
        try {
            if (null != contentEntries) {
                ContentViewEntry item = contentEntries.get(position);
                boolean hasMore = item.isHasMore();
                if (hasMore) {
                    return 1;
                }
                else {
                    return 0;
                }
            }
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
        return 1;
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
        LogUtil.INSTANCE.info("birdgangfileexplorer", "onCreateContentItemViewHolder > contentViewType : " + contentViewType);
        BaseViewHolder viewHolder = new ListExplorerFileSearchViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_sd_file_explorer_search, parent, false), contentViewType);
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
            ContentViewEntry item = contentEntries.get(position);
            fillContentFile(item, (ListExplorerFileSearchViewHolder) contentViewHolder, position);
        } catch (Exception e) {
            LogUtil.INSTANCE.error(getClass().getSimpleName(), e);
        }
    }

    private void fillContentFile (final ContentViewEntry contentViewEntry, ListExplorerFileSearchViewHolder holder, int position) {
        CheckableLinearLayout containerRoot = holder.getContainerRoot();
        containerRoot.setOnClickListener(this);
        containerRoot.setTag(position);
        containerRoot.setEnabled(true);

        String contentName = StringUtil.removeAllExtension(contentViewEntry.getContentName());
        String contentFormatName = StringUtil.removeExtension(contentViewEntry.getContentName());
        String rate = contentViewEntry.getLmsRate();
        TextView textDirectoryTitle = holder.getTvItemName();
        textDirectoryTitle.setText(contentName);
        textDirectoryTitle.setFocusable(true);
        textDirectoryTitle.setClickable(false);

        TextView textMediaData = holder.getTvItemData();
        String filePath = contentViewEntry.getContentFilePath();
        textMediaData.setText(StringUtil.extractContentDirctoryFullPath(contentViewEntry.getContentFilePath()));
        textMediaData.setFocusable(true);
        textMediaData.setClickable(false);

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
    public void onClick(View view) {
        try {
            if (mContentItemClickListener == null) {
                return;
            }

            int position = (Integer) view.getTag();
            ContentViewEntry detailInfo = getContentEntryByPosition(position);
            if (null == detailInfo) {
                return;
            }

            boolean hasMore = detailInfo.isHasMore();
            if (hasMore) {
                mContentItemClickListener.onItemCategoryClick(view);
            } else {
                mContentItemClickListener.onItemClick(view);
            }
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
    }

}
