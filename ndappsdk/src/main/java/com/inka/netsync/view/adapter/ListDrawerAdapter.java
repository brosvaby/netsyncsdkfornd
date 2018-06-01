package com.inka.netsync.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.inka.netsync.R;
import com.inka.netsync.common.bus.ContentItemClickListener;
import com.inka.netsync.common.bus.ContentItemLongClickListener;
import com.inka.netsync.logs.LogUtil;
import com.inka.netsync.view.adapter.holder.BaseViewHolder;
import com.inka.netsync.view.adapter.holder.ListDrawerItemViewHolder;
import com.inka.netsync.view.adapter.holder.ListDrawerSectionViewHolder;
import com.inka.netsync.view.model.DrawerMenuViewEntry;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class ListDrawerAdapter extends HeaderFooterRecyclerViewAdapter implements View.OnClickListener, View.OnLongClickListener {

    private final String TAG = "ListDrawerAdapter";

    private Context mContext;
    private List<? extends DrawerMenuViewEntry> mDrawerMenuItemEntries;

    private ContentItemClickListener mContentItemClickListener = null;
    private ContentItemLongClickListener mContentItemLongClickListener = null;

    public ListDrawerAdapter(Context context, List<? extends DrawerMenuViewEntry> items, ContentItemClickListener contentItemClickListener) {
        this.mContext = context;
        this.mDrawerMenuItemEntries = items;
        this.mContentItemClickListener = contentItemClickListener;
    }

    public void updateDrawer (String tag) {
        int count = getContentItemCount();
        try {
            for (int i=0; i<count; i++) {
                DrawerMenuViewEntry drawerMenuViewEntry = getItem(i);
                if (StringUtils.equals(drawerMenuViewEntry.mTabTag, tag)) {
                    drawerMenuViewEntry.setHasSelected(true);
                } else {
                    drawerMenuViewEntry.setHasSelected(false);
                }
            }
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
        notifyDataSetChanged();
    }

    @Override
    protected int getContentItemViewType(int position) {
        super.getContentItemViewType(position);
        try {
            if (null != mDrawerMenuItemEntries) {
                DrawerMenuViewEntry item = mDrawerMenuItemEntries.get(position);
                if (item.getDrawerMenuType() == DrawerMenuViewEntry.DrawerMenuType.SECTION.ordinal()) {
                    return DrawerMenuViewEntry.DrawerMenuType.SECTION.ordinal();
                } else {
                    return DrawerMenuViewEntry.DrawerMenuType.ITEM.ordinal();
                }
            }
        } catch (Exception e) {
        }

        return DrawerMenuViewEntry.DrawerMenuType.ITEM.ordinal();
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
        return (null == mDrawerMenuItemEntries) ? 0 : mDrawerMenuItemEntries.size();
    }

    public DrawerMenuViewEntry getItem (int pos) {
        return mDrawerMenuItemEntries.get(pos);
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
        if (contentViewType == DrawerMenuViewEntry.DrawerMenuType.SECTION.ordinal()) {
            viewHolder = new ListDrawerSectionViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_drawermenu_section, parent, false), contentViewType);
        }
        else {
            viewHolder = new ListDrawerItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_drawermenu_item, parent, false), contentViewType);
        }
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
            DrawerMenuViewEntry item = mDrawerMenuItemEntries.get(position);
            int viewType = getContentItemViewType(position);
            if (viewType == DrawerMenuViewEntry.DrawerMenuType.SECTION.ordinal()) {
                fillContentDrawerMenuSection(item, (ListDrawerSectionViewHolder) contentViewHolder, position);
            }
            else {
                fillContentDrawerMenuItem(item, (ListDrawerItemViewHolder) contentViewHolder, position);
            }
        } catch (Exception e) {
            LogUtil.INSTANCE.error(getClass().getSimpleName(), e);
        }
    }

    private void fillContentDrawerMenuSection (final DrawerMenuViewEntry drawerMenuEntry, ListDrawerSectionViewHolder holder, int position) throws Exception {
        TextView textDrawerMenuHeader = holder.getTextDrawerMenuSection();
        textDrawerMenuHeader.setText(drawerMenuEntry.mTitle);
        textDrawerMenuHeader.setFocusable(true);
        textDrawerMenuHeader.setClickable(false);
    }

    private void fillContentDrawerMenuItem (final DrawerMenuViewEntry drawerMenuEntry, ListDrawerItemViewHolder holder, int position) throws Exception {
        RelativeLayout containerRoot = holder.getLayoutRoot();
        containerRoot.setOnClickListener(this);
        containerRoot.setTag(drawerMenuEntry.mTabTag);
        containerRoot.setEnabled(true);

        TextView textDrawerMenuTitle = holder.getTextDrawerMenuTitle();
        textDrawerMenuTitle.setText(drawerMenuEntry.mTitle);
        textDrawerMenuTitle.setFocusable(true);
        textDrawerMenuTitle.setClickable(false);

        ImageView imageDrawerMenuIcon = holder.getImageDrawerMenuIcon();
        imageDrawerMenuIcon.setScaleType(ImageView.ScaleType.FIT_XY);
        imageDrawerMenuIcon.setImageResource(drawerMenuEntry.mResDrawable);

        ImageView imageDrawerMenuSelectedIcon = holder.getImageDrawerMenuSelectedIcon();
        imageDrawerMenuSelectedIcon.setScaleType(ImageView.ScaleType.FIT_XY);

        View view = holder.getvDisable();

        boolean selected = drawerMenuEntry.isHasSelected();
        if (selected) {
            imageDrawerMenuIcon.setVisibility(View.GONE);
            imageDrawerMenuSelectedIcon.setVisibility(View.VISIBLE);
            //textDrawerMenuTitle.setTextColor(ContextCompat.getColor(mContext, BaseConfiguration.getInstance().getAppDialogBtnColor()));
        } else {
            imageDrawerMenuIcon.setVisibility(View.VISIBLE);
            imageDrawerMenuSelectedIcon.setVisibility(View.GONE);
            //textDrawerMenuTitle.setTextColor(ContextCompat.getColor(mContext, R.color.font_black_1));
        }

        int state = drawerMenuEntry.getStateMenu();
        if (DrawerMenuViewEntry.STATE_DISABLE == state) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
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
            LogUtil.INSTANCE.error("ListItemViewHolder", e);
        }
    }

    @Override
    public boolean onLongClick(View v) {
        try {
            if (mContentItemLongClickListener == null) {
                return false;
            }

            mContentItemLongClickListener.onItemLongClick(v);
        } catch (Exception e) {
            LogUtil.INSTANCE.error("ListItemViewHolder", e);
        }
        return true;
    }

}
