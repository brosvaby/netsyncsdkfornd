package com.inka.netsync.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.inka.netsync.R;
import com.inka.netsync.common.bus.ClickListener;
import com.inka.netsync.common.utils.AnimUtils;
import com.inka.netsync.logs.LogUtil;
import com.inka.netsync.view.adapter.holder.BaseViewHolder;
import com.inka.netsync.view.adapter.holder.ListNavigationPathViewHolder;
import com.inka.netsync.view.model.NavigationPathViewEntry;
import com.inka.netsync.view.model.SettingMenuViewEntry;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ListNavigationPathAdapter extends HeaderFooterRecyclerViewAdapter implements View.OnClickListener {

    private final String TAG = "ListNavigationPathAdapter";

    private Context context;

    private List<String> data;
    private List<NavigationPathViewEntry> navigationPathViewEntries;

    public final Map<String, Adapter> sections = new LinkedHashMap<String, Adapter>();

    private ClickListener mClickListener = null;

    public ListNavigationPathAdapter(Context context, List<NavigationPathViewEntry> items, ClickListener clickListener) {
        this.context = context;
        this.navigationPathViewEntries = items;
        this.mClickListener = clickListener;
        this.data = new ArrayList<>();
    }


    public void add(String s , int position) {
        position = position == -1 ? getItemCount()  : position;
        data.add(position,s);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        if (position < getItemCount()) {
            data.remove(position);
            notifyItemRemoved(position);
        }
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
        return (null == navigationPathViewEntries) ? 0 : navigationPathViewEntries.size();
    }

    @Override
    protected int getContentItemViewType(int position) {
        super.getContentItemViewType(position);
        try {
            if (null != navigationPathViewEntries) {
                NavigationPathViewEntry item = navigationPathViewEntries.get(position);
            }
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
        return SettingMenuViewEntry.SettingType.COMMON.ordinal();
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
        BaseViewHolder viewHolder = new ListNavigationPathViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_navigation_path_item, parent, false), contentViewType);
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
            NavigationPathViewEntry item = navigationPathViewEntries.get(position);
            fillContentNavigationInfo(item, (ListNavigationPathViewHolder) contentViewHolder, position);
        } catch (Exception e) {
            LogUtil.INSTANCE.error(getClass().getSimpleName(), e);
        }
    }


    private void fillContentNavigationInfo (final NavigationPathViewEntry navigationPathEntry, ListNavigationPathViewHolder holder, int position) throws Exception {
        LinearLayout containerRoot = holder.getLayoutRoot();
        containerRoot.setOnClickListener(this);
        containerRoot.setTag(position);
        containerRoot.setEnabled(true);

        TextView textDirectoryDivider = holder.getTextNavigationDivider();
        TextView textDirectoryTitle = holder.getTextNavigationPathName();
        textDirectoryTitle.setText(navigationPathEntry.getName());

        if (position < getContentItemCount() - 1) {
            textDirectoryTitle.setEnabled(false);
            textDirectoryTitle.setFocusable(false);
            AnimUtils.scaleIn(textDirectoryDivider);
            textDirectoryDivider.setVisibility(View.VISIBLE);
        } else {
            textDirectoryTitle.setEnabled(true);
            textDirectoryTitle.setFocusable(true);
            AnimUtils.scaleOut(textDirectoryDivider);
            textDirectoryDivider.setVisibility(View.GONE);
        }
    }


    @Override
    public void onClick(View view) {
        try {
            if (mClickListener == null) {
                return;
            }

            mClickListener.onItemClick(view);
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
    }


}
