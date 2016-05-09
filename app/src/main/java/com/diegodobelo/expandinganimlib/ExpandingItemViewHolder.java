package com.diegodobelo.expandinganimlib;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
        mItem = (ExpandingItem) itemView;
        mTitle = (TextView) mItem.findViewById(R.id.title);
        mContext = itemView.getContext();
    }

    public void setData(final ExpandingItemData data) {
        mTitle.setText((String)data.getItemData());
        if (data.getItem() == null) {
            data.setItem(mItem);
        } else {
            mItem = data.getItem();
        }


        if (data.getSubItemData() != null) {
            for (int i = 0; i < data.getSubItemData().size(); i++) {
                View subItemView = mItem.createSubItem(i); //get already created item or create new one
                TextView text = (TextView) subItemView.findViewById(R.id.sub_title);
                text.setText((String) data.getSubItemData().get(i));
            }
        }
        if (data.isExpanded()) {
            mItem.expandSubItems();
        } else {
            mItem.collapseSubItems();
        }

//        if (!mItem.isExpanded()) {
//
//        }
        //Test with hexa color
        mItem.setIndicatorColorRes(data.getColorRes());
        //TODO: create method that receives res
        mItem.setIndicatorIcon(ContextCompat.getDrawable(mContext, data.getIconRes()));

        mItem.setStateChangedListener(new ExpandingItem.OnItemStateChanged() {
            @Override
            public void itemCollapseStateChanged(boolean expanded) {
                data.setExpanded(expanded);
            }
        });
    }
}
