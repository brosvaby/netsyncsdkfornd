package com.inka.netsync.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.inka.netsync.R;
import com.inka.netsync.view.adapter.holder.HelpInfoChildViewHolder;
import com.inka.netsync.view.adapter.holder.HelpInfoGroupViewHolder;
import com.inka.netsync.view.model.ExpandableGroupEntry;
import com.inka.netsync.view.model.HelpInfoChildViewEntry;
import com.inka.netsync.view.model.HelpInfoGroupViewEntry;

import java.util.List;

/**
 * Created by birdgang on 2018. 2. 22..
 */
public class ListExpandableAdapter extends ExpandableRecyclerViewAdapter<HelpInfoGroupViewHolder, HelpInfoChildViewHolder> {

    public ListExpandableAdapter(List<? extends ExpandableGroupEntry> groups) {
        super(groups);
    }

    @Override
    public HelpInfoGroupViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_list_item_info_group, parent, false);
        return new HelpInfoGroupViewHolder(view, -1);
    }

    @Override
    public HelpInfoChildViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_list_item_info_child, parent, false);
        return new HelpInfoChildViewHolder(view, -1);
    }

    @Override
    public void onBindChildViewHolder(HelpInfoChildViewHolder holder, int flatPosition, ExpandableGroupEntry group, int childIndex) {
        final HelpInfoChildViewEntry helpInfoChildViewEntry = ((HelpInfoGroupViewEntry) group).getItems().get(childIndex);
        holder.setNoticeContent(helpInfoChildViewEntry.getName());
    }

    @Override
    public void onBindGroupViewHolder(HelpInfoGroupViewHolder holder, int flatPosition, ExpandableGroupEntry group) {
        holder.setGenreTitle(group);
    }
}
