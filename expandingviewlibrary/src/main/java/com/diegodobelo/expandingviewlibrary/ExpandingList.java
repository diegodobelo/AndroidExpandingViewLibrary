package com.diegodobelo.expandingviewlibrary;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by diego on 5/9/16.
 */
public class ExpandingList extends LinearLayout {
    //TODO: inherit from scrollView. Create linear layout to add items
    public ExpandingList(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ExpandingList(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);
    }

    public ExpandingList(Context context) {
        super(context);
    }

    public void addItem(ExpandingItem item) {
        addView(item);
    }

    public ExpandingItem createNewItem(int layoutId) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        ViewGroup item = (ViewGroup) inflater.inflate(layoutId, null, false);
        if (item instanceof ExpandingItem) {
            ExpandingItem expandingItem = (ExpandingItem) item;
            addItem(expandingItem);
            return expandingItem;
        }
        throw new RuntimeException("The layout id not an instance of com.diegodobelo.expandinganimlib.ExpandingItem");
    }
}
