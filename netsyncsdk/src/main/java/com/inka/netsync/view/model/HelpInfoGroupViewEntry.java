package com.inka.netsync.view.model;

import java.util.List;

/**
 * Created by birdgang on 2018. 2. 22..
 */

public class HelpInfoGroupViewEntry extends ExpandableGroupEntry<HelpInfoChildViewEntry> {

    private int iconResId;

    public HelpInfoGroupViewEntry(String title, List<HelpInfoChildViewEntry> items, int iconResId) {
        super(title, items);
        this.iconResId = iconResId;
    }

    public int getIconResId() {
        return iconResId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HelpInfoGroupViewEntry)) return false;

        HelpInfoGroupViewEntry helpInfoParentViewEntry = (HelpInfoGroupViewEntry) o;
        return getIconResId() == helpInfoParentViewEntry.getIconResId();

    }

    @Override
    public int hashCode() {
        return getIconResId();
    }

}

