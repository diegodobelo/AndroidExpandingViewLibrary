/**
 Copyright (c) 2016, Diego Bezerra <diego.bezerra@gmail.com>
 Permission to use, copy, modify, and/or distribute this software for any purpose
 with or without fee is hereby granted, provided that the above copyright notice
 and this permission notice appear in all copies.
 THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH
 REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF MERCHANTABILITY AND
 FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT, INDIRECT,
 OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE,
 DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS
 ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 **/
package com.diegodobelo.expandingview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

/**
 * Created by diego on 5/9/16.
 */
public class ExpandingList extends ScrollView {
    private LinearLayout mContainer;
    //TODO: inherit from scrollView. Create linear layout to add items
    public ExpandingList(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContainer = new LinearLayout(context);
        mContainer.setOrientation(LinearLayout.VERTICAL);
        addView(mContainer);
    }

    public void addItem(ExpandingItem item) {
        mContainer.addView(item);
    }

    public ExpandingItem createNewItem(int layoutId) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        ViewGroup item = (ViewGroup) inflater.inflate(layoutId, this, false);
        if (item instanceof ExpandingItem) {
            ExpandingItem expandingItem = (ExpandingItem) item;
            addItem(expandingItem);
            return expandingItem;
        }
        throw new RuntimeException("The layout id not an instance of com.diegodobelo.expandinganimlib.ExpandingItem");
    }
}
