package com.diegodobelo.expandinganimlib;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by diego on 5/22/16.
 */
public class CustomTextView extends TextView {
    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomFont(attrs);
    }

    private void setCustomFont(AttributeSet attrs) {
        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CustomFont,
                0, 0);

        try {
            String family = a.getString(R.styleable.CustomFont_fontFamily);
            Typeface myTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/" + family);
            setTypeface(myTypeface);
        } finally {
            a.recycle();
        }
    }
}
