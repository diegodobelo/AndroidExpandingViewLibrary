package com.diegodobelo.expandinganimlib;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by diego on 5/5/16.
 */
public class ExpandingItemViewHolder extends RecyclerView.ViewHolder {
    private TextView mTitle;
    private ExpandingItem mItem;
    private Context mContext;

    public ExpandingItemViewHolder(View itemView) {
        super(itemView);
        mTitle = (TextView) itemView.findViewById(R.id.title);
        mItem = (ExpandingItem) itemView;
        mContext = itemView.getContext();
    }

    public void setData(ExpandingItemData data) {
        mTitle.setText((String)data.getItemData());
        if (data.getSubItemData() != null) {
            for (int i = 0; i < data.getSubItemData().size(); i++) {
                View subItemView = null;
                try {
                    subItemView = mItem.createSubItem(i);
                    TextView text = (TextView) subItemView.findViewById(R.id.sub_title);
                    text.setText((String) data.getSubItemData().get(i));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            mItem.collapseSubItems();
        }
        //Test with hexa color
        mItem.setIndicatorColorRes(data.getColorRes());
        //TODO: create method that receives res
        mItem.setIndicatorIcon(ContextCompat.getDrawable(mContext, data.getIconRes()));
    }
}
