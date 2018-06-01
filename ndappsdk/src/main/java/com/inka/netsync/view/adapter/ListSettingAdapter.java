package com.inka.netsync.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.inka.netsync.R;
import com.inka.netsync.common.bus.ContentSettingItemClickListener;
import com.inka.netsync.logs.LogUtil;
import com.inka.netsync.view.adapter.holder.BaseViewHolder;
import com.inka.netsync.view.adapter.holder.ListSettingSwitchViewHolder;
import com.inka.netsync.view.adapter.holder.ListSettingViewHolder;
import com.inka.netsync.view.adapter.holder.ListSettingWithButtonViewHolder;
import com.inka.netsync.view.model.SettingMenuViewEntry;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ListSettingAdapter extends HeaderFooterRecyclerViewAdapter implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private final String TAG = "ListSettingAdapter";

    private Context context;

    private List<String> data;
    private List<SettingMenuViewEntry> settingMenuEntries;

    private ContentSettingItemClickListener mContentSettingItemClickListener = null;

    private boolean mEnableMarketUpdate = false;

    public ListSettingAdapter(Context context, List<SettingMenuViewEntry> items, ContentSettingItemClickListener contentSettingItemClickListener) {
        this.context = context;
        this.settingMenuEntries = items;
        this.mContentSettingItemClickListener = contentSettingItemClickListener;
        this.data = new ArrayList<>();
    }

    public void add(String s , int position) {
        position = position == -1 ? getItemCount()  : position;
        data.add(position,s);
        notifyItemInserted(position);
    }

    public void updateEnableMarketVersion (boolean enable) {
        this.mEnableMarketUpdate = enable;
        notifyDataSetChanged();
    }

    public void updateSettingMenus (long id, SettingMenuViewEntry settingMenuEntry) {
        for (int i=0; i<settingMenuEntries.size(); i++) {
            int entryId = settingMenuEntries.get(i).getId();
            if (id == entryId) {
                settingMenuEntries.set(i, settingMenuEntry);
            }
        }
        notifyDataSetChanged();
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
        return (null == settingMenuEntries) ? 0 : settingMenuEntries.size();
    }

    @Override
    protected int getContentItemViewType(int position) {
        super.getContentItemViewType(position);
        try {
            if (null != settingMenuEntries) {
                SettingMenuViewEntry item = settingMenuEntries.get(position);
                if (StringUtils.equals(item.getSettingType(), SettingMenuViewEntry.SettingType.SWITCH.getType())) {
                    return SettingMenuViewEntry.SettingType.SWITCH.ordinal();
                }
                else if (StringUtils.equals(item.getSettingType(), SettingMenuViewEntry.SettingType.SWITCHWITHDESCRIPTION.getType())) {
                    return SettingMenuViewEntry.SettingType.SWITCHWITHDESCRIPTION.ordinal();
                }
                else if (StringUtils.equals(item.getSettingType(), SettingMenuViewEntry.SettingType.WITHBUTTON.getType())) {
                    return SettingMenuViewEntry.SettingType.WITHBUTTON.ordinal();
                }
                else {
                    return SettingMenuViewEntry.SettingType.COMMON.ordinal();
                }
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
        BaseViewHolder viewHolder = null;
        if (contentViewType == SettingMenuViewEntry.SettingType.SWITCH.ordinal()) {
            viewHolder = new ListSettingSwitchViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_setting_switch_item, parent, false), contentViewType);
        }
        else if (contentViewType == SettingMenuViewEntry.SettingType.SWITCHWITHDESCRIPTION.ordinal()) {
            viewHolder = new ListSettingSwitchViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_setting_switch_with_description_item, parent, false), contentViewType);
        }
        else if (contentViewType == SettingMenuViewEntry.SettingType.WITHBUTTON.ordinal()) {
            viewHolder = new ListSettingWithButtonViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_setting_with_button_item, parent, false), contentViewType);
        }
        else {
            viewHolder = new ListSettingViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_setting_item, parent, false), contentViewType);
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
            SettingMenuViewEntry item = settingMenuEntries.get(position);
            String contentType = item.getSettingType();
            if (StringUtils.equals(SettingMenuViewEntry.SettingType.SWITCH.getType(), contentType)) {
                fillContentSettingInfoSwitch(item, (ListSettingSwitchViewHolder) contentViewHolder, position);
            }
            else if (StringUtils.equals(SettingMenuViewEntry.SettingType.SWITCHWITHDESCRIPTION.getType(), contentType)) {
                fillContentSettingInfoSwitch(item, (ListSettingSwitchViewHolder) contentViewHolder, position);
            }
            else if (StringUtils.equals(SettingMenuViewEntry.SettingType.WITHBUTTON.getType(), contentType)) {
                fillContentSettingInfoWithButton(item, (ListSettingWithButtonViewHolder) contentViewHolder, position);
            }
            else {
                fillContentSettingInfo(item, (ListSettingViewHolder) contentViewHolder, position);
            }
        } catch (Exception e) {
            LogUtil.INSTANCE.error(getClass().getSimpleName(), e);
        }
    }

    private void fillContentSettingInfo (final SettingMenuViewEntry settingMenuEntry, ListSettingViewHolder holder, int position) throws Exception {
        LinearLayout containerRoot = holder.getLayoutRoot();
        containerRoot.setOnClickListener(this);
        containerRoot.setTag(settingMenuEntry);
        containerRoot.setEnabled(true);

        String title = settingMenuEntry.getTitle();
        String value = settingMenuEntry.getValue();
        String description = settingMenuEntry.getDescription();
        boolean hasMore = settingMenuEntry.isHasMore();

        LogUtil.INSTANCE.info(TAG, "title : " + title + " , value : " + value + " , description : " + description + " , hasMore : " + hasMore);

        TextView textTitle = holder.getTextTitle();
        textTitle.setText(title);
        textTitle.setEnabled(true);

        // setting value Matching
        TextView txtItemValue = holder.getTxtItemValue();
        if (txtItemValue != null) {
            txtItemValue.setText(value);
            txtItemValue.setEnabled(true);
        }

        // setting arrow image Matching
        ImageView arrowView = holder.getImgArrow();
        if (arrowView != null) {
            if (hasMore) {
                arrowView.setVisibility(View.VISIBLE);
            } else {
                arrowView.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void fillContentSettingInfoSwitch (final SettingMenuViewEntry settingMenuEntry, ListSettingSwitchViewHolder holder, int position) throws Exception {
        final LinearLayout containerRoot = holder.getLayoutRoot();
        containerRoot.setOnClickListener(this);
        containerRoot.setTag(settingMenuEntry);
        containerRoot.setEnabled(true);

        String title = settingMenuEntry.getTitle();
        boolean enable = settingMenuEntry.isToggleOnOrFalse();

        TextView textTitle = holder.getTextTitle();
        textTitle.setText(title);
        textTitle.setEnabled(true);

        SwitchCompat switchCompat = holder.getSwitchCompat();
        switchCompat.setTag(settingMenuEntry.getId());
        if (enable) {
            switchCompat.setChecked(true);
        } else  {
            switchCompat.setChecked(false);
        }
        switchCompat.setOnCheckedChangeListener(this);
    }


    private void fillContentSettingInfoWithButton (final SettingMenuViewEntry settingMenuEntry, ListSettingWithButtonViewHolder holder, int position) throws Exception {
        final LinearLayout containerRoot = holder.getLayoutRoot();
        containerRoot.setOnClickListener(this);
        containerRoot.setTag(settingMenuEntry);
        containerRoot.setEnabled(true);

        String title = settingMenuEntry.getTitle();
        String value = settingMenuEntry.getValue();

        TextView textTitle = holder.getTextTitle();
        textTitle.setText(title);
        textTitle.setEnabled(true);

        TextView textVersion = holder.getTextValue();
        textVersion.setText(value);

        LinearLayout containerVersionUpdate = holder.getContainerVersionUpdate();

        TextView textVersionUpdate = holder.getTextVersionUpdate();
        settingMenuEntry.setEnableMarketUpdate(mEnableMarketUpdate);

        if (mEnableMarketUpdate) {
            containerVersionUpdate.setBackgroundResource(R.color.color_enable);
            textVersionUpdate.setText(context.getResources().getString(R.string.setting_result_enable_market_version_update));
        }
        else {
            containerVersionUpdate.setBackgroundResource(R.color.color_disable);
            textVersionUpdate.setText(context.getResources().getString(R.string.setting_result_disable_market_version_update));
        }
    }

    @Override
    public void onClick(View v) {
        try {
            if (mContentSettingItemClickListener == null) {
                return;
            }
            mContentSettingItemClickListener.onItemClick(v);
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        try {
            int tag = (int) buttonView.getTag();
            if (mContentSettingItemClickListener == null) {
                return;
            }
            mContentSettingItemClickListener.onItemSwitchClick(tag, isChecked);
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
    }

}
