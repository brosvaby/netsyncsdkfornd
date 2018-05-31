package com.inka.netsync.view.adapter.holder;

import android.view.View;
import android.view.View.OnClickListener;

import com.inka.netsync.view.expandable.OnGroupClickListener;

public abstract class GroupViewHolder extends BaseViewHolder implements OnClickListener {

    private OnGroupClickListener listener;

    public GroupViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onGroupClick(getAdapterPosition());
        }
    }

    public void setOnGroupClickListener(OnGroupClickListener listener) {
        this.listener = listener;
    }

    public void expand() {}

    public void collapse() {}

}
