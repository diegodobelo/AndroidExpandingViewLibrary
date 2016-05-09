package com.diegodobelo.expandinganimlib;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by diego on 5/9/16.
 */
public class ExpandingItemData {
    private int mIconRes;
    private int mColorRes;
    private boolean mIsExpanded;
    private Object mItemData;
    private List<Object> mSubItemsData;
    private ExpandingItem mItem;

    public int getIconRes() {
        return mIconRes;
    }

    public void setIconRes(int iconRes) {
        mIconRes = iconRes;
    }

    public int getColorRes() {
        return mColorRes;
    }

    public void setColorRes(int colorRes) {
        mColorRes = colorRes;
    }

    public boolean isExpanded() {
        return mIsExpanded;
    }

    public void setExpanded(boolean isExpanded) {
        mIsExpanded = isExpanded;
    }

    public List<Object> getSubItemData() {
        //TODO: use template do define data type
        return mSubItemsData;
    }

    public void addSubItemData(Object data) {
        if (mSubItemsData == null) {
            mSubItemsData = new ArrayList<>();
        }
        mSubItemsData.add(data);
    }

    public Object getItemData() {
        //TODO: use template do define data type
        return mItemData;
    }

    public void setItemData(Object mItemData) {
        this.mItemData = mItemData;
    }

    public ExpandingItem getItem() {
        return mItem;
    }

    public void setItem(ExpandingItem item) {
        mItem = item;
    }
}
