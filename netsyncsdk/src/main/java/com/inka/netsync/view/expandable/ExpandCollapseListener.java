package com.inka.netsync.view.expandable;

public interface ExpandCollapseListener {
    void onGroupExpanded(int positionStart, int itemCount);
    void onGroupCollapsed(int positionStart, int itemCount);
}
