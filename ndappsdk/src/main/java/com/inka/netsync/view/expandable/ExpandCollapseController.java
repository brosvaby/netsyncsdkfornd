package com.inka.netsync.view.expandable;

import com.inka.netsync.logs.LogUtil;
import com.inka.netsync.view.model.ExpandableGroupEntry;

/**
 * Created by birdgang on 2018. 2. 22..
 */

public class ExpandCollapseController {

    private ExpandCollapseListener listener;
    private ExpandableList expandableList;

    public ExpandCollapseController(ExpandableList expandableList, ExpandCollapseListener listener) {
        this.expandableList = expandableList;
        this.listener = listener;
    }

    private void collapseGroup(ExpandableListPosition listPosition) {
        expandableList.expandedGroupIndexes[listPosition.groupPos] = false;
        if (listener != null) {
            listener.onGroupCollapsed(expandableList.getFlattenedGroupIndex(listPosition) + 1,
                    expandableList.groups.get(listPosition.groupPos).getItemCount());
        }
    }

    private void expandGroup(ExpandableListPosition listPosition) {
        expandableList.expandedGroupIndexes[listPosition.groupPos] = true;
        if (listener != null) {
            listener.onGroupExpanded(expandableList.getFlattenedGroupIndex(listPosition) + 1,
                    expandableList.groups.get(listPosition.groupPos).getItemCount());
        }
    }

    public boolean isGroupExpanded(ExpandableGroupEntry group) {
        int groupIndex = expandableList.groups.indexOf(group);
        return expandableList.expandedGroupIndexes[groupIndex];
    }

    public boolean isGroupExpanded(int flatPos) {
        ExpandableListPosition listPosition = expandableList.getUnflattenedPosition(flatPos);
        return expandableList.expandedGroupIndexes[listPosition.groupPos];
    }

    public boolean toggleGroup(int flatPos) {
        ExpandableListPosition listPos = expandableList.getUnflattenedPosition(flatPos);
        boolean expanded = expandableList.expandedGroupIndexes[listPos.groupPos];
        LogUtil.INSTANCE.info("birdganggrouplist" , "toggleGroup > flatPos : " + flatPos);
        if (expanded) {
            collapseGroup(listPos);
        } else {
            expandGroup(listPos);
        }
        return expanded;
    }

    public boolean toggleGroup(ExpandableGroupEntry group) {
        ExpandableListPosition listPos = expandableList.getUnflattenedPosition(expandableList.getFlattenedGroupIndex(group));
        boolean expanded = expandableList.expandedGroupIndexes[listPos.groupPos];
        if (expanded) {
            collapseGroup(listPos);
        } else {
            expandGroup(listPos);
        }
        return expanded;
    }

}
