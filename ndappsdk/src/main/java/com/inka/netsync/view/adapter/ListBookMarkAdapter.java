package com.inka.netsync.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.inka.netsync.R;
import com.inka.netsync.common.bus.ContentItemClickListener;
import com.inka.netsync.common.bus.ContentItemLongClickListener;
import com.inka.netsync.common.utils.DateTimeUtil;
import com.inka.netsync.logs.LogUtil;
import com.inka.netsync.view.adapter.holder.BaseViewHolder;
import com.inka.netsync.view.adapter.holder.ListBookMarkViewHolder;
import com.inka.netsync.view.model.BookMarkViewEntry;

import java.util.ArrayList;
import java.util.List;

public class ListBookMarkAdapter extends HeaderFooterRecyclerViewAdapter implements View.OnClickListener , View.OnLongClickListener {

    private final String TAG = "ListBookMarkAdapter";

    private Context context;

    protected int MODE = 0;
    public final int COMMON = 0;
    public final int EDIT = 1;

    private List<BookMarkViewEntry> bookMarkViewEntries;
    private List<BookMarkViewEntry> selectedForDeleteEntries;

    private ContentItemClickListener mContentItemClickListener = null;
    private ContentItemLongClickListener mContentItemLongClickListener = null;

    public ListBookMarkAdapter(Context context, List<BookMarkViewEntry> items, ContentItemClickListener contentItemClickListener, ContentItemLongClickListener contentItemLongClickListener) {
        this.context = context;
        this.bookMarkViewEntries = items;
        this.mContentItemClickListener = contentItemClickListener;
        this.mContentItemLongClickListener = contentItemLongClickListener;

        this.selectedForDeleteEntries = new ArrayList<>();
    }

    public void setData (List<BookMarkViewEntry> bookmarkEntries) {
        this.bookMarkViewEntries = bookmarkEntries;
        notifyDataSetChanged();
    }

    public void changeEditMode () {
        MODE = EDIT;
        notifyDataSetChanged();
    }

    public void changeEditModeCancel () {
        MODE = COMMON;
        clearForDeleteEntries();
        notifyDataSetChanged();
    }

    public int currentEditMode () {
        return MODE;
    }

    public boolean addForDeleteEntry (BookMarkViewEntry entry) {
        boolean result = false;
        try {
            if (entry.isChecked()) {
                result = selectedForDeleteEntries.remove(entry);
            }
            else {
                result = selectedForDeleteEntries.add(entry);
            }
            notifyDataSetChanged();
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }

        return result;
    }

    public void clearForDeleteEntries () {
        selectedForDeleteEntries.clear();
        for (BookMarkViewEntry entry : bookMarkViewEntries) {
            entry.setChecked(false);
        }
    }

    public List<BookMarkViewEntry> getSelectedForDeleteEntries() {
        return selectedForDeleteEntries;
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
        return (null == bookMarkViewEntries) ? 0 : bookMarkViewEntries.size();
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
        BaseViewHolder viewHolder = new ListBookMarkViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_bookmark_item, parent, false), contentViewType);
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
            BookMarkViewEntry item = bookMarkViewEntries.get(position);
            fillBookmarkInfo(item, (ListBookMarkViewHolder) contentViewHolder, position);
        } catch (Exception e) {
            LogUtil.INSTANCE.error(getClass().getSimpleName(), e);
        }
    }


    private void fillBookmarkInfo (final BookMarkViewEntry bookMarkViewEntry, ListBookMarkViewHolder holder, final int position) throws Exception {
        final RelativeLayout containerRoot = holder.getLayoutRoot();
        containerRoot.setOnClickListener(this);
        containerRoot.setOnLongClickListener(this);
        containerRoot.setTag(bookMarkViewEntry);
        containerRoot.setEnabled(true);

        TextView txtBookmarkLocation = holder.getTvItemLocation();
        if (txtBookmarkLocation != null) {
            int location = Integer.valueOf(bookMarkViewEntry.getBookmarkLocation());
            txtBookmarkLocation.setText(DateTimeUtil.changeTime(location));
        }

        // bookmark_memo Matching
        TextView txtBookmarkMemo = holder.getTvItemMemo();
        if (txtBookmarkMemo != null) {
            txtBookmarkMemo.setText(bookMarkViewEntry.getBookmarkMemo());
        }

        // checkBox Delete
        RelativeLayout groupViewEditArea = holder.getContainerEditArea();

        CheckBox checkBox = holder.getCheckItemDelete();
        checkBox.setTag(bookMarkViewEntry);
        checkBox.setChecked(bookMarkViewEntry.isChecked());
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try {
                    BookMarkViewEntry entry = (BookMarkViewEntry) buttonView.getTag();
                    if (null == entry) {
                        return;
                    }

                    entry.setChecked(isChecked);

                    if (isChecked) {
                        selectedForDeleteEntries.add(entry);
                    }
                    else {
                        selectedForDeleteEntries.remove(entry);
                    }

                    for (BookMarkViewEntry selectedEntry : selectedForDeleteEntries) {
                        LogUtil.INSTANCE.info(TAG, "selectedEntry : " + selectedEntry.toString());
                    }

                    if (isChecked) {
                        containerRoot.setPressed(true);
                    } else {
                        containerRoot.setPressed(false);
                    }
                } catch (Exception e) {
                    LogUtil.INSTANCE.error(TAG, e);
                }
            }
        });

        boolean isChecked = checkBox.isChecked();
        if (isChecked) {
            containerRoot.setPressed(true);
        } else {
            containerRoot.setPressed(false);
        }
        if (MODE == COMMON) {
            groupViewEditArea.setVisibility(View.INVISIBLE);
        }
        else if (MODE == EDIT) {
            groupViewEditArea.setVisibility(View.VISIBLE);
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
