package com.inka.netsync.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.inka.netsync.R;
import com.inka.netsync.common.bus.ContentFavoriteClickListener;
import com.inka.netsync.common.bus.ContentItemClickListener;
import com.inka.netsync.common.bus.ContentItemLongClickListener;
import com.inka.netsync.common.utils.FileUtil;
import com.inka.netsync.common.utils.StringUtil;
import com.inka.netsync.logs.LogUtil;
import com.inka.netsync.view.CheckableLinearLayout;
import com.inka.netsync.view.adapter.holder.BaseViewHolder;
import com.inka.netsync.view.adapter.holder.ListExplorerFileNoLMSViewHolder;
import com.inka.netsync.view.adapter.holder.ListExplorerFileViewHolder;
import com.inka.netsync.view.adapter.holder.ListExplorerGroupViewHolder;
import com.inka.netsync.view.model.ContentViewEntry;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.List;


public class ListExplorerAdapter extends HeaderFooterRecyclerViewAdapter implements View.OnClickListener, View.OnLongClickListener {

    private final String TAG = "ListExplorerAdapter";

    private Context context;

    private List<ContentViewEntry> contentViewEntries;

    private ContentItemClickListener contentItemClickListener = null;
    private ContentItemLongClickListener contentItemLongClickListener = null;
    private ContentFavoriteClickListener contentFavoriteClickListener = null;

    public ListExplorerAdapter(Context context, List<ContentViewEntry> items, ContentItemClickListener contentItemClickListener, ContentItemLongClickListener longClickListener, ContentFavoriteClickListener clickListener) {
        this.context = context;
        this.contentViewEntries = items;
        this.contentItemClickListener = contentItemClickListener;
        this.contentItemLongClickListener = longClickListener;
        this.contentFavoriteClickListener = clickListener;
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
        return (null == contentViewEntries) ? 0 : contentViewEntries.size();
    }

    public void setData (List<ContentViewEntry> contentEntries) {
        this.contentViewEntries = contentEntries;
        notifyDataSetChanged();
    }

    public void setDataForLMS (List<ContentViewEntry> contentEntries) {
        this.contentViewEntries = contentEntries;
        notifyDataSetChanged();
    }
    
    public ContentViewEntry getContentEntryByPosition (int position) {
        if (getContentItemCount() < 1) {
            return null;
        }
        return contentViewEntries.get(position);
    }

    public boolean updateContentView (ContentViewEntry contentViewEntry) {
        boolean result = false;
        if (getContentItemCount() < 1) {
            return result;
        }

        try {
            for (ContentViewEntry entry : contentViewEntries) {
                if (contentViewEntry.getContentId() == entry.getContentId()) {
                    entry.copyContentViewEntry(contentViewEntry);
                    result = true;
                }
            }
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }

        return result;
    }


    @Override
    protected int getContentItemViewType(int position) {
        super.getContentItemViewType(position);
        try {
            if (null != contentViewEntries) {
                ContentViewEntry item = contentViewEntries.get(position);
                boolean hasMore = item.isHasMore();
                if (hasMore) {
                    return ContentViewEntry.ContentType.GROUP.ordinal();
                }
                else {
                    String mediaType = item.getMediaType();
                    LogUtil.INSTANCE.info(TAG, "getContentItemViewType > mediaType : " + mediaType);
                    if (StringUtils.equals(ContentViewEntry.ContentType.AUDIO.getType(), mediaType)) {
                        return ContentViewEntry.ContentType.AUDIO.ordinal();
                    } else if (StringUtils.equals(ContentViewEntry.ContentType.DOC.getType(), mediaType)) {
                        return ContentViewEntry.ContentType.DOC.ordinal();
                    } else {
                        return ContentViewEntry.ContentType.VIDEO.ordinal();
                    }
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
        BaseViewHolder viewHolder = null;
        if (contentViewType == ContentViewEntry.ContentType.GROUP.ordinal()) {
            viewHolder = new ListExplorerGroupViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_explorer_group, parent, false), contentViewType);
        }
        else if (contentViewType == ContentViewEntry.ContentType.AUDIO.ordinal()) {
            viewHolder = new ListExplorerFileViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_explorer_file, parent, false), contentViewType);
        }
        else if (contentViewType == ContentViewEntry.ContentType.DOC.ordinal()) {
            viewHolder = new ListExplorerFileNoLMSViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_explorer_file_no_lms, parent, false), contentViewType);
        }
        else {
            viewHolder = new ListExplorerFileViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_explorer_file, parent, false), contentViewType);
        }
        return viewHolder;
    }

    @Override
    protected void onBindHeaderItemViewHolder(RecyclerView.ViewHolder headerViewHolder, int position) {}

    @Override
    protected void onBindFooterItemViewHolder(RecyclerView.ViewHolder footerViewHolder, int position) {}

    @Override
    protected void onBindContentItemViewHolder(RecyclerView.ViewHolder contentViewHolder, int position) {
        try {
            ContentViewEntry item = contentViewEntries.get(position);
            boolean hasMore = item.isHasMore();
            if (hasMore) {
                fillContentDirectory(item, (ListExplorerGroupViewHolder) contentViewHolder, position);
            }
            else {
                String mediaType = item.getMediaType();
                LogUtil.INSTANCE.info(TAG, "getContentItemViewType > mediaType : " + mediaType);
                if (StringUtils.equals(ContentViewEntry.ContentType.AUDIO.getType(), mediaType)) {
                    fillContentFile(item, (ListExplorerFileViewHolder) contentViewHolder, position);
                }
                else if (StringUtils.equals(ContentViewEntry.ContentType.DOC.getType(), mediaType)) {
                    fillContentFileNoLMS(item, (ListExplorerFileNoLMSViewHolder) contentViewHolder, position);
                }
                else {
                    fillContentFile(item, (ListExplorerFileViewHolder) contentViewHolder, position);
                }
            }
        } catch (Exception e) {
            LogUtil.INSTANCE.error(getClass().getSimpleName(), e);
        }
    }


    private void fillContentDirectory (final ContentViewEntry contentEntry, ListExplorerGroupViewHolder holder, int position) {
        CheckableLinearLayout containerRoot = holder.getLayoutRoot();
        containerRoot.setOnClickListener(this);
        containerRoot.setOnLongClickListener(this);
        containerRoot.setTag(position);
        containerRoot.setEnabled(true);

        TextView textDirectoryTitle = holder.getTvItemName();
        textDirectoryTitle.setText(contentEntry.getContentName());
        textDirectoryTitle.setFocusable(true);
        textDirectoryTitle.setClickable(false);

        TextView textRegisterYmdt = holder.getTvItemRegisterYmdt();
        textRegisterYmdt.setText(contentEntry.getContentDownloadDate());
        textRegisterYmdt.setFocusable(true);
        textRegisterYmdt.setClickable(false);
    }


    private void fillContentFile (final ContentViewEntry contentViewEntry, ListExplorerFileViewHolder holder, int position) {
        CheckableLinearLayout containerRoot = holder.getContainerRoot();
        containerRoot.setOnClickListener(this);
        containerRoot.setOnLongClickListener(this);
        containerRoot.setTag(position);
        containerRoot.setEnabled(true);

        int isFavoriteContent = contentViewEntry.getIsFavoriteContent();
        LogUtil.INSTANCE.info(TAG, "fillContentFile > contentViewEntry : " + contentViewEntry.toString());

        final ToggleButton toggleButton = holder.getToggleFavorite();
        toggleButton.setTag(contentViewEntry);
        toggleButton.setFocusable(false);
        if (isFavoriteContent <= 0) {
            toggleButton.setChecked(false);
        } else {
            toggleButton.setChecked(true);
        }

        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null == contentFavoriteClickListener) {
                    return;
                }
                try {
                    int contentId = contentViewEntry.getContentId();
                    contentFavoriteClickListener.onItemClick(toggleButton.isChecked(), contentId);
                } catch (Exception e) {
                    LogUtil.INSTANCE.error(TAG, e);
                }
            }
        });

        String contentName = StringUtil.removeAllExtension(contentViewEntry.getContentName());
        LogUtil.INSTANCE.info(TAG, "fillContentFile > contentName : " + contentName);

        TextView textDirectoryTitle = holder.getTvItemName();
        textDirectoryTitle.setText(String.valueOf(contentName));
        textDirectoryTitle.setFocusable(true);
        textDirectoryTitle.setClickable(false);

        TextView textMediaData = holder.getTvItemData();
        String filePath = contentViewEntry.getContentFilePath();
        if (StringUtils.isNotBlank(filePath)) {
            File file = new File(filePath);
            String capacity = FileUtil.getFileSizeString(file.length());
            textMediaData.setText(capacity);
            textMediaData.setFocusable(true);
            textMediaData.setClickable(false);
        }

        TextView textMediaDuration = holder.getTvItemDataDuration();
        String data = contentViewEntry.getData();
        if (StringUtils.isNotBlank(data)) {
            textMediaDuration.setText(data);
            textMediaDuration.setFocusable(true);
            textMediaDuration.setClickable(false);
        }
        else {
            textMediaDuration.setText(StringUtils.EMPTY);
            textMediaDuration.setFocusable(false);
            textMediaDuration.setClickable(false);
        }

        ProgressBar progressLmsPercent = holder.getProgressBarLms();
        TextView txtlmsPercent = holder.getTvLmsPercent();

        int progressbarDrawable = contentViewEntry.getProgressbarDrawableForLms();
        if (progressbarDrawable > 0) {
            progressLmsPercent.setProgressDrawable(context.getResources().getDrawable(progressbarDrawable));
        }
        else{
            progressLmsPercent.setProgressDrawable(context.getResources().getDrawable(R.drawable.default_progress_lms));
        }

        int textRateDimColorForLms = contentViewEntry.getTextRateDimColorForLms();
        int textRateColorForLms = contentViewEntry.getTextRateColorForLms();

        LogUtil.INSTANCE.info("birdgangexplorerlmsinfo", " progressbarDrawable : " + progressbarDrawable + " , textRateDimColorForLms : " + textRateDimColorForLms + " , textRateColorForLms : " + textRateColorForLms);


        String rate = contentViewEntry.getLmsRate();
        if (StringUtils.isBlank(rate)) {
            progressLmsPercent.setProgress(0);
            txtlmsPercent.setText(0 + "%");
            txtlmsPercent.setTextColor(context.getResources().getColor(R.color.text_color_explorer_rate_number_none));
        }
        else {
            progressLmsPercent.setProgress(Integer.valueOf(rate));
            txtlmsPercent.setText(rate + "%");
            if (textRateColorForLms > 0) {
                txtlmsPercent.setTextColor(context.getResources().getColor(textRateColorForLms));
            } else {
                txtlmsPercent.setTextColor(context.getResources().getColor(R.color.text_color_explorer_rate_number));
            }
        }

        String contentFormatName = contentViewEntry.getContentName();
        LogUtil.INSTANCE.info(TAG, "contentFormatName : " + contentFormatName);

        RelativeLayout containerLms = holder.getContainerLms();

        ImageView imageFileItemImage = holder.getIvFileItemImage();
        imageFileItemImage.setScaleType(ImageView.ScaleType.FIT_XY);
        if (StringUtils.contains(contentFormatName, "mp4")) {
            holder.getContainerFavorite().setVisibility(View.VISIBLE);
            containerLms.setVisibility(View.VISIBLE);

            if (StringUtils.equals(rate, "100")) {
                imageFileItemImage.setImageResource(R.drawable.img_video_played);
            }
            else {
                imageFileItemImage.setImageResource(R.drawable.img_video);
            }
        }
        else if (StringUtils.contains(contentFormatName, "mp3")) {
            holder.getContainerFavorite().setVisibility(View.VISIBLE);
            containerLms.setVisibility(View.VISIBLE);

            if (StringUtils.equals(rate, "100")) {
                imageFileItemImage.setImageResource(R.drawable.img_mp3_played);
            }
            else {
                imageFileItemImage.setImageResource(R.drawable.img_mp3);
            }
        }

    }


    private void fillContentFileNoLMS (final ContentViewEntry contentViewEntry, ListExplorerFileNoLMSViewHolder holder, int position) {
        CheckableLinearLayout containerRoot = holder.getContainerRoot();
        containerRoot.setOnClickListener(this);
        containerRoot.setOnLongClickListener(this);
        containerRoot.setTag(position);
        containerRoot.setEnabled(true);

        LogUtil.INSTANCE.info(TAG, "fillContentFileNoLMS > contentViewEntry : " + contentViewEntry.toString());
        String contentName = StringUtil.removeExtensionForPdf(contentViewEntry.getContentName());
        LogUtil.INSTANCE.info(TAG, "fillContentFileNoLMS > contentName : " + contentName);

        TextView textDirectoryTitle = holder.getTvItemName();
        textDirectoryTitle.setText(contentName);
        textDirectoryTitle.setFocusable(true);
        textDirectoryTitle.setClickable(false);

        TextView textMediaData = holder.getTvItemData();
        String filePath = contentViewEntry.getContentFilePath();
        if (StringUtils.isNotBlank(filePath)) {
            File file = new File(filePath);
            String capacity = FileUtil.getFileSizeString(file.length());
            textMediaData.setText(capacity);
            textMediaData.setFocusable(true);
            textMediaData.setClickable(false);
        }

        TextView textMediaDuration = holder.getTvItemDataDuration();
        String data = contentViewEntry.getData();
        if (StringUtils.isNotBlank(data)) {
            textMediaDuration.setText(data);
            textMediaDuration.setFocusable(true);
            textMediaDuration.setClickable(false);
        }
        else {
            textMediaDuration.setText(StringUtils.EMPTY);
            textMediaDuration.setFocusable(false);
            textMediaDuration.setClickable(false);
        }

        ImageView imageFileItemImage = holder.getIvFileItemImage();
        imageFileItemImage.setScaleType(ImageView.ScaleType.FIT_XY);
        imageFileItemImage.setImageResource(R.drawable.img_doc);

    }




    public void updateContentFavorite (boolean needToUpdate, ContentViewEntry contentViewEntry) {
        try {
            if (needToUpdate) {
                boolean state = contentViewEntry.getIsFavoriteContent() == 1 ? true : false;
                if (!state) {
                    Toast.makeText(context, context.getString(R.string.message_for_toast_favorite_content_no_use), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, context.getString(R.string.message_for_toast_favorite_content_use), Toast.LENGTH_SHORT).show();
                }
            }

            updateContentView(contentViewEntry);
            notifyDataSetChanged();
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
    }


    @Override
    public void onClick(View view) {
        try {
            if (contentItemClickListener == null) {
                return;
            }

            int position = (Integer) view.getTag();
            ContentViewEntry detailInfo = getContentEntryByPosition(position);
            if (null == detailInfo) {
                return;
            }

            boolean hasMore = detailInfo.isHasMore();
            if (hasMore) {
                contentItemClickListener.onItemCategoryClick(view);
            } else {
                contentItemClickListener.onItemClick(view);
            }
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
    }

    @Override
    public boolean onLongClick(View view) {
        try {
            if (contentItemLongClickListener == null) {
                return false;
            }

            int position = (Integer) view.getTag();
            ContentViewEntry detailInfo = getContentEntryByPosition(position);
            if (null == detailInfo) {
                return false;
            }

            contentItemLongClickListener.onItemLongClick(view);
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
        return false;
    }
}
