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

    public void setData(String title, String[] subItems) {
        mTitle.setText(title);
        if (subItems != null) {
            mItem.beginSubItemCreation();
            for (int i = 0; i < subItems.length; i++) {
                View subItemView = null;
                try {
                    subItemView = mItem.createSubItem(i);
                    TextView text = (TextView) subItemView.findViewById(R.id.sub_title);
                    text.setText(subItems[i]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
//            mItem.endSubItemCreation();
        }
        //Test with hexa color
        mItem.setIndicatorColorRes(R.color.colorAccent);
        mItem.setIndicatorIcon(ContextCompat.getDrawable(mContext, R.drawable.ic_ghost));
    }
}
