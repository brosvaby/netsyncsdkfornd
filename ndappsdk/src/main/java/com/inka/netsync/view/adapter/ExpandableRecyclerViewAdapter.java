package com.inka.netsync.view.adapter;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.inka.netsync.logs.LogUtil;
import com.inka.netsync.view.adapter.holder.ChildViewHolder;
import com.inka.netsync.view.adapter.holder.GroupViewHolder;
import com.inka.netsync.view.expandable.ExpandCollapseController;
import com.inka.netsync.view.expandable.ExpandCollapseListener;
import com.inka.netsync.view.expandable.ExpandableList;
import com.inka.netsync.view.expandable.ExpandableListPosition;
import com.inka.netsync.view.expandable.GroupExpandCollapseListener;
import com.inka.netsync.view.expandable.OnGroupClickListener;
import com.inka.netsync.view.model.ExpandableGroupEntry;

import java.util.List;

/**
 * Created by birdgang on 2018. 2. 22..
 */

public abstract class ExpandableRecyclerViewAdapter<GVH extends GroupViewHolder, CVH extends ChildViewHolder>
    extends RecyclerView.Adapter implements ExpandCollapseListener, OnGroupClickListener {

    private static final String EXPAND_STATE_MAP = "expandable_recyclerview_adapter_expand_state_map";

    protected ExpandableList expandableList;
    private ExpandCollapseController expandCollapseController;

    private OnGroupClickListener groupClickListener;
    private GroupExpandCollapseListener expandCollapseListener;

    public ExpandableRecyclerViewAdapter(List<? extends ExpandableGroupEntry> groups) {
        this.expandableList = new ExpandableList(groups);
        this.expandCollapseController = new ExpandCollapseController(expandableList, this);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case ExpandableListPosition.GROUP:
                GVH gvh = onCreateGroupViewHolder(parent, viewType);
                gvh.setOnGroupClickListener(this);
                return gvh;
            case ExpandableListPosition.CHILD:
                CVH cvh = onCreateChildViewHolder(parent, viewType);
                return cvh;
            default:
                throw new IllegalArgumentException("viewType is not valid");
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ExpandableListPosition listPos = expandableList.getUnflattenedPosition(position);
        ExpandableGroupEntry group = expandableList.getExpandableGroup(listPos);

        LogUtil.INSTANCE.info("birdganggrouplist" , "onBindViewHolder > listPos.type : " + listPos.type);
        switch (listPos.type) {
            case ExpandableListPosition.GROUP:
                onBindGroupViewHolder((GVH) holder, position, group);
                if (isGroupExpanded(group)) {
                    ((GVH) holder).expand();
                } else {
                    ((GVH) holder).collapse();
                }
                break;
            case ExpandableListPosition.CHILD:
                onBindChildViewHolder((CVH) holder, position, group, listPos.childPos);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return expandableList.getVisibleItemCount();
    }

    @Override
    public int getItemViewType(int position) {
        return expandableList.getUnflattenedPosition(position).type;
    }

    @Override
    public void onGroupExpanded(int positionStart, int itemCount) {
        int headerPosition = positionStart - 1;
        notifyItemChanged(headerPosition);

        if (itemCount > 0) {
            notifyItemRangeInserted(positionStart, itemCount);
            if (expandCollapseListener != null) {
                int groupIndex = expandableList.getUnflattenedPosition(positionStart).groupPos;
                expandCollapseListener.onGroupExpanded(getGroups().get(groupIndex));
            }
        }
    }

    @Override
    public void onGroupCollapsed(int positionStart, int itemCount) {
        int headerPosition = positionStart - 1;
        notifyItemChanged(headerPosition);

        if (itemCount > 0) {
            notifyItemRangeRemoved(positionStart, itemCount);
            if (expandCollapseListener != null) {
                int groupIndex = expandableList.getUnflattenedPosition(positionStart - 1).groupPos;
                expandCollapseListener.onGroupCollapsed(getGroups().get(groupIndex));
            }
        }
    }

    @Override
    public boolean onGroupClick(int flatPos) {
        LogUtil.INSTANCE.info("birdganggrouplist",  "onGroupClick > flatPos : " + flatPos);
        if (groupClickListener != null) {
            groupClickListener.onGroupClick(flatPos);
        }
        return expandCollapseController.toggleGroup(flatPos);
    }

    public boolean toggleGroup(int flatPos) {
        return expandCollapseController.toggleGroup(flatPos);
    }

    public boolean toggleGroup(ExpandableGroupEntry group) {
        return expandCollapseController.toggleGroup(group);
    }

    public boolean isGroupExpanded(int flatPos) {
        return expandCollapseController.isGroupExpanded(flatPos);
    }

    public boolean isGroupExpanded(ExpandableGroupEntry group) {
        return expandCollapseController.isGroupExpanded(group);
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBooleanArray(EXPAND_STATE_MAP, expandableList.expandedGroupIndexes);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState == null || !savedInstanceState.containsKey(EXPAND_STATE_MAP)) {
            return;
        }
        expandableList.expandedGroupIndexes = savedInstanceState.getBooleanArray(EXPAND_STATE_MAP);
        notifyDataSetChanged();
    }

    public void setOnGroupClickListener(OnGroupClickListener listener) {
        groupClickListener = listener;
    }

    public void setOnGroupExpandCollapseListener(GroupExpandCollapseListener listener) {
        expandCollapseListener = listener;
    }

    public List<? extends ExpandableGroupEntry> getGroups() {
        return expandableList.groups;
    }

    public abstract GVH onCreateGroupViewHolder(ViewGroup parent, int viewType);

    public abstract CVH onCreateChildViewHolder(ViewGroup parent, int viewType);

    public abstract void onBindChildViewHolder(CVH holder, int flatPosition, ExpandableGroupEntry group, int childIndex);

    public abstract void onBindGroupViewHolder(GVH holder, int flatPosition, ExpandableGroupEntry group);
}
