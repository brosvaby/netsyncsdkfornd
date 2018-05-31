package com.inka.netsync.view.expandable;

import com.inka.netsync.view.model.ExpandableGroupEntry;

public interface GroupExpandCollapseListener {
    void onGroupExpanded(ExpandableGroupEntry group);
    void onGroupCollapsed(ExpandableGroupEntry group);
}
