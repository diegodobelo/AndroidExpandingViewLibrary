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

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import utils.CustomViewUtils;

/**
 * Created by diego on 5/5/16.
 */
public class ExpandingItem extends RelativeLayout {
    /**
     * Constant defining default animation duration in milliseconds.
     */
    private static final int DEFAULT_ANIM_DURATION = 300;

    /**
     * Member variable to hold the Item Layout. Set by item_layout in ExpandingItem layout.
     */
    private ViewGroup mItemLayout;

    /**
     * The layout inflater.
     */
    private LayoutInflater mInflater;

    /**
     * Member variable to hold the base layout. Should not be changed.
     */
    private RelativeLayout mBaseLayout;

    /**
     * Member variable to hold item. Should not be changed.
     */
    private LinearLayout mBaseListLayout;

    /**
     * Member variable to hold sub items. Should not be changed.
     */
    private LinearLayout mBaseSubListLayout;

    /**
     * Member variable to hold the indicator icon.
     * Can be set by {@link #setIndicatorIconRes(int)}} or by {@link #setIndicatorIcon(Drawable)}.
     */
    private ImageView mIndicatorImage;

    /**
     * Member variable to hold the expandable part of indicator. Should not be changed.
     */
    private View mIndicatorBackground;

    /**
     * Stub to hold separator;
     */
    private ViewStub mSeparatorStub;

    /**
     * Member variable to hold the indicator container. Should not be changed.
     */
    private ViewGroup mIndicatorContainer;

    /**
     * Member variable to hold the measured item height.
     */
    private int mItemHeight;

    /**
     * Member variable to hold the measured sub item height.
     */
    private int mSubItemHeight;

    /**
     * Member variable to hold the measured sub item width.
     */
    private int mSubItemWidth;

    /**
     * Member variable to hold the measured total height of sub items.
     */
    private int mCurrentSubItemsHeight;

    /**
     * Member variable to hold the sub items count.
     */
    private int mSubItemCount;

    /**
     * Member variable to hold the indicator size. Set by indicator_size in ExpandingItem layout.
     */
    private int mIndicatorSize;

    /**
     * Member variable to hold the animation duration.
     * Set by animation_duration in ExpandingItem layout in milliseconds.
     * Default is 300ms.
     */
    private int mAnimationDuration;

    /**
     * Member variable to hold the indicator margin at left. Set by indicator_margin_left in ExpandingItem layout.
     */
    private int mIndicatorMarginLeft;

    /**
     * Member variable to hold the indicator margin at right. Set by indicator_margin_right in ExpandingItem layout.
     */
    private int mIndicatorMarginRight;

    /**
     * Member variable to hold the boolean value that defines if the indicator should be shown.
     * Set by show_indicator in ExpandingItem layout.
     */
    private boolean mShowIndicator;

    /**
     * Member variable to hold the boolean value that defines if the animation should be shown.
     * Set by show_animation in ExpandingItem layout. Default is true.
     */
    private boolean mShowAnimation;

    /**
     * Member variable to hold the boolean value that defines if the sub list will start collapsed or not.
     * Set by start_collapsed in ExpandingItem layout. Default is true.
     */
    private boolean mStartCollapsed;

    /**
     * Member variable to hold the state of sub items. true if shown. false otherwise.
     */
    private boolean mSubItemsShown;

    /**
     * Member variable to hold the layout resource of sub items. Set by sub_item_layout in ExpandingItem layout.
     */
    private int mSubItemLayoutId;

    /**
     * Member variable to hold the layout resource of items. Set by item_layout in ExpandingItem layout.
     */
    private int mItemLayoutId;

    /**
     * Member variable to hold the layout resource of separator. Set by separator_layout in ExpandingItem layout.
     */
    private int mSeparatorLayoutId;

    /**
     * Holds a reference to the parent. Used to calculate positioning.
     */
    private ExpandingList mParent;

    /**
     * Member variable to hold the listener of item state change.
     */
    private OnItemStateChanged mListener;

    /**
     * Interface to notify item state changed.
     */
    public interface OnItemStateChanged {
        /**
         * Notify if item was expanded or collapsed.
         * @param expanded true if expanded. false otherwise.
         */
        void itemCollapseStateChanged(boolean expanded);
    }

    /**
     * Constructor.
     * @param context
     * @param attrs
     */
    public ExpandingItem(Context context, AttributeSet attrs) {
        super(context, attrs);

        readAttributes(context, attrs);
        setupStateVariables();
        inflateLayouts(context);

        setupIndicator();

        addItem(mItemLayout);
        addView(mBaseLayout);
    }

    /**
     * Setup the variables that defines item state.
     */
    private void setupStateVariables() {
        if (!mShowAnimation) {
            mAnimationDuration = 0;
        }
    }

    /**
     * Method to setup indicator, including size and visibility.
     */
    private void setupIndicator() {
        if (mIndicatorSize != 0) {
            setIndicatorBackgroundSize();
        }

        mIndicatorContainer.setVisibility(mShowIndicator && mIndicatorSize != 0 ? VISIBLE : GONE);
    }

    /**
     * Read all custom styleable attributes.
     * @param context The custom View Context.
     * @param attrs The atrributes to be read.
     */
    private void readAttributes(Context context, AttributeSet attrs) {
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.ExpandingItem, 0, 0);

        try {
            mItemLayoutId = array.getResourceId(R.styleable.ExpandingItem_item_layout, 0);
            mSeparatorLayoutId = array.getResourceId(R.styleable.ExpandingItem_separator_layout, 0);
            mSubItemLayoutId = array.getResourceId(R.styleable.ExpandingItem_sub_item_layout, 0);
            mIndicatorSize = array.getDimensionPixelSize(R.styleable.ExpandingItem_indicator_size, 0);
            mIndicatorMarginLeft = array.getDimensionPixelSize(R.styleable.ExpandingItem_indicator_margin_left, 0);
            mIndicatorMarginRight = array.getDimensionPixelSize(R.styleable.ExpandingItem_indicator_margin_right, 0);
            mShowIndicator = array.getBoolean(R.styleable.ExpandingItem_show_indicator, true);
            mShowAnimation = array.getBoolean(R.styleable.ExpandingItem_show_animation, true);
            mStartCollapsed = array.getBoolean(R.styleable.ExpandingItem_start_collapsed, true);
            mAnimationDuration = array.getInt(R.styleable.ExpandingItem_animation_duration, DEFAULT_ANIM_DURATION);
        } finally {
            array.recycle();
        }
    }

    /**
     * Method to inflate all layouts.
     * @param context The custom View Context.
     */
    private void inflateLayouts(Context context) {
        mInflater = LayoutInflater.from(context);
        mBaseLayout = (RelativeLayout) mInflater.inflate(R.layout.expanding_item_base_layout,
                null, false);
        mBaseListLayout = (LinearLayout) mBaseLayout.findViewById(R.id.base_list_layout);
        mBaseSubListLayout = (LinearLayout) mBaseLayout.findViewById(R.id.base_sub_list_layout);
        mIndicatorImage = (ImageView) mBaseLayout.findViewById(R.id.indicator_image);
        mBaseLayout.findViewById(R.id.icon_indicator_top).bringToFront();
        mSeparatorStub = (ViewStub) mBaseLayout.findViewById(R.id.base_separator_stub);
        mIndicatorBackground = mBaseLayout.findViewById(R.id.icon_indicator_middle);
        mIndicatorContainer = (ViewGroup) mBaseLayout.findViewById(R.id.indicator_container);
        mIndicatorContainer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleExpanded();
            }
        });

        if (mItemLayoutId != 0) {
            mItemLayout = (ViewGroup) mInflater.inflate(mItemLayoutId, mBaseLayout, false);
        }
        if (mSeparatorLayoutId != 0) {
            mSeparatorStub.setLayoutResource(mSeparatorLayoutId);
            mSeparatorStub.inflate();
        }
    }

    /**
     * Set the indicator background width, height and margins
     */
    private void setIndicatorBackgroundSize() {
        CustomViewUtils.setViewHeight(mBaseLayout.findViewById(R.id.icon_indicator_top), mIndicatorSize);
        CustomViewUtils.setViewHeight(mBaseLayout.findViewById(R.id.icon_indicator_bottom), mIndicatorSize);
        CustomViewUtils.setViewHeight(mBaseLayout.findViewById(R.id.icon_indicator_middle), 0);

        CustomViewUtils.setViewWidth(mBaseLayout.findViewById(R.id.icon_indicator_top), mIndicatorSize);
        CustomViewUtils.setViewWidth(mBaseLayout.findViewById(R.id.icon_indicator_bottom), mIndicatorSize);
        CustomViewUtils.setViewWidth(mBaseLayout.findViewById(R.id.icon_indicator_middle), mIndicatorSize);

        mItemLayout.post(new Runnable() {
            @Override
            public void run() {
                CustomViewUtils.setViewMargin(mIndicatorContainer,
                        mIndicatorMarginLeft, mItemLayout.getMeasuredHeight()/2 - mIndicatorSize/2, mIndicatorMarginRight, 0);
            }
        });

        CustomViewUtils.setViewMarginTop(mBaseLayout.findViewById(R.id.icon_indicator_middle), (-1 * mIndicatorSize/2));
        CustomViewUtils.setViewMarginTop(mBaseLayout.findViewById(R.id.icon_indicator_bottom), (-1 * mIndicatorSize/2));

    }

    /**
     * Set a listener to listen item stage changed.
     * @param listener The listener of type {@link OnItemStateChanged}
     */
    public void setStateChangedListener(OnItemStateChanged listener) {
        mListener = listener;
    }

    /**
     * Tells if the item is expanded.
     * @return true if expanded. false otherwise.
     */
    public boolean isExpanded() {
        return mSubItemsShown;
    }

    /**
     * Returns the count of sub items.
     * @return The count of sub items.
     */
    public int getSubItemsCount() {
        return mSubItemCount;
    }

    /**
     * Collapses the sub items.
     */
    public void collapse() {
        mSubItemsShown = false;
        mBaseSubListLayout.post(new Runnable() {
            @Override
            public void run() {
                CustomViewUtils.setViewHeight(mBaseSubListLayout, 0);
            }
        });
    }

    /**
     * Expand or collapse the sub items.
     */
    public void toggleExpanded() {
        if (mSubItemCount == 0) {
            return;
        }

        if (!mSubItemsShown) {
            adjustItemPosIfHidden();
        }

        toggleSubItems();
        expandSubItemsWithAnimation(0f);
        expandIconIndicator(0f);
        animateSubItemsIn();
    }

    /**
     * Method to adjust Item position in parent if its sub items are outside screen.
     */
    private void adjustItemPosIfHidden() {
        int parentHeight = mParent.getMeasuredHeight();
        int[] parentPos = new int[2];
        mParent.getLocationOnScreen(parentPos);
        int parentY = parentPos[1];
        int[] itemPos = new int[2];
        mBaseLayout.getLocationOnScreen(itemPos);
        int itemY = itemPos[1];


        int endPosition = itemY + mItemHeight + (mSubItemHeight * mSubItemCount);
        int parentEnd = parentY + parentHeight;
        if (endPosition > parentEnd) {
            int delta = endPosition - parentEnd;
            int itemDeltaToTop = itemY - parentY;
            if (delta > itemDeltaToTop) {
                delta = itemDeltaToTop;
            }
            mParent.scrollUpByDelta(delta);
        }
    }

    /**
     * Set the indicator color by resource.
     * @param colorRes The color resource.
     */
    public void setIndicatorColorRes(int colorRes) {
        setIndicatorColor(ContextCompat.getColor(getContext(), colorRes));
    }

    /**
     * Set the indicator color by color value.
     * @param color The color value.
     */
    public void setIndicatorColor(int color) {
        ((GradientDrawable)findViewById(R.id.icon_indicator_top).getBackground().mutate()).setColor(color);
        ((GradientDrawable)findViewById(R.id.icon_indicator_bottom).getBackground().mutate()).setColor(color);
        findViewById(R.id.icon_indicator_middle).setBackgroundColor(color);
    }

    /**
     * Set the indicator icon by resource.
     * @param iconRes The icon resource.
     */
    public void setIndicatorIconRes(int iconRes) {
        setIndicatorIcon(ContextCompat.getDrawable(getContext(), iconRes));
    }

    /**
     * Set the indicator icon.
     * @param icon Drawable of the indicator icon.
     */
    public void setIndicatorIcon(Drawable icon) {
        mIndicatorImage.setImageDrawable(icon);
    }

    /**
     * Creates a sub item based on sub_item_layout Layout, set as ExpandingItem layout attribute.
     * @return The inflated sub item view.
     */
    @Nullable
    public View createSubItem() {
        return createSubItem(-1);
    }

    /**
     * Creates a sub item based on sub_item_layout Layout, set as ExpandingItem layout attribute.
     * @param position The position to add the new Item. Position should not be greater than the list size.
     *                 If position is -1, the item will be added in the end of the list.
     * @return The inflated sub item view.
     */
    @Nullable
    public View createSubItem(int position) {
        if (mSubItemLayoutId == 0) {
            throw new RuntimeException("There is no layout to be inflated. " +
                    "Please set sub_item_layout value");
        }

        if (position > mBaseSubListLayout.getChildCount()) {
            throw new IllegalArgumentException("Cannot add an item at position " + position +
                    ". List size is " + mBaseSubListLayout.getChildCount());
        }

        ViewGroup subItemLayout = (ViewGroup) mInflater.inflate(mSubItemLayoutId, mBaseSubListLayout, false);
        if (position == -1) {
            mBaseSubListLayout.addView(subItemLayout);
        } else {
            mBaseSubListLayout.addView(subItemLayout, position);
        }
        mSubItemCount++;
        setSubItemDimensions(subItemLayout);

        //Animate sub view in
        if (mSubItemsShown) {
            CustomViewUtils.setViewHeight(subItemLayout, 0);
            expandSubItemsWithAnimation(mSubItemHeight * (mSubItemCount));
            expandIconIndicator(mCurrentSubItemsHeight);
            animateSubItemAppearance(subItemLayout, true);
            adjustItemPosIfHidden();
        }

        return subItemLayout;
    }

    /**
     * Creates as many sub items as requested in {@param count}.
     * @param count The quantity of sub items.
     */
    public void createSubItems(int count) {
        if (mSubItemLayoutId == 0) {
            throw new RuntimeException("There is no layout to be inflated. " +
                    "Please set sub_item_layout value");
        }
        for (int i = 0; i < count; i++) {
            createSubItem();
        }
        if (mStartCollapsed) {
            collapse();
        } else {
            mSubItemsShown = true;
            mBaseSubListLayout.post(new Runnable() {
                @Override
                public void run() {
                    expandIconIndicator(0f);
                }
            });
        }
    }

    /**
     * Get a sub item at the given position.
     * @param position The sub item position. Should be > 0.
     * @return The sub item inflated view at the given position.
     */
    public View getSubItemView(int position) {
        if (mBaseSubListLayout.getChildAt(position) != null) {
            return mBaseSubListLayout.getChildAt(position);
        }
        throw new RuntimeException("There is no sub item for position " + position +
                ". There are only " + mBaseSubListLayout.getChildCount() + " in the list.");
    }

    /**
     * Remove sub item at the given position.
     * @param position The position of the item to be removed.
     */
    public void removeSubItemAt(int position) {
        removeSubItemFromList(mBaseSubListLayout.getChildAt(position));
    }

    /**
     * Remove the given view representing the sub item. Should be an existing sub item.
     * @param view The sub item to be removed.
     */
    public void removeSubItemFromList(View view) {
        if (view != null) {
            mBaseSubListLayout.removeView(view);
            mSubItemCount--;
            expandSubItemsWithAnimation(mSubItemHeight * (mSubItemCount + 1));
            if (mSubItemCount == 0) {
                mCurrentSubItemsHeight = 0;
                mSubItemsShown = false;
            }
            expandIconIndicator(mCurrentSubItemsHeight);
        }
    }

    /**
     * Remove the given view representing the sub item, with animation. Should be an existing sub item.
     * @param view The sub item to be removed.
     */
    public void removeSubItem(View view) {
        animateSubItemAppearance(view, false);
    }

    /**
     * Remove all sub items.
     */
    public void removeAllSubItems() {
        mBaseSubListLayout.removeAllViews();
    }

    /**
     * Set the parent in order to auto scroll.
     * @param parent The parent of type {@link ExpandingList}
     */
    protected void setParent(ExpandingList parent) {
        mParent = parent;
    }

    /**
     * Method to add the inflated item and set click listener.
     * Also measures the item height.
     * @param item The inflated item layout.
     */
    private void addItem(final ViewGroup item) {
        if (item != null) {
            mBaseListLayout.addView(item);
            item.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    toggleExpanded();
                }
            });
            item.post(new Runnable() {
                @Override
                public void run() {
                    mItemHeight = item.getMeasuredHeight();
                }
            });
        }
    }

    /**
     * Measure sub items dimension.
     * @param v The sub item to measure.
     */
    private void setSubItemDimensions(final ViewGroup v) {
        v.post(new Runnable() {
            @Override
            public void run() {
                if (mSubItemHeight <= 0) {
                    mSubItemHeight = v.getMeasuredHeight();
                    mSubItemWidth = v.getMeasuredWidth();
                }
            }
        });
    }

    /**
     * Toggle sub items collapsed/expanded
     */
    private void toggleSubItems() {
        mSubItemsShown = !mSubItemsShown;
        if (mListener != null) {
            mListener.itemCollapseStateChanged(mSubItemsShown);
        }
    }

    /**
     * Show sub items animation.
     */
    private void animateSubItemsIn() {
        for (int i = 0; i < mSubItemCount; i++) {
            animateSubViews((ViewGroup) mBaseSubListLayout.getChildAt(i), i);
            animateViewAlpha((ViewGroup) mBaseSubListLayout.getChildAt(i), i);
        }
    }

    /**
     * Show sub items translation animation.
     * @param viewGroup The sub item to animate
     * @param index The sub item index. Needed to calculate delays.
     */
    private void animateSubViews(final ViewGroup viewGroup, int index) {
        if (viewGroup == null) {
            return;
        }
        viewGroup.setLayerType(ViewGroup.LAYER_TYPE_HARDWARE, null);
        ValueAnimator animation = mSubItemsShown ?
                ValueAnimator.ofFloat(0f, 1f) :
                ValueAnimator.ofFloat(1f, 0f);
        animation.setDuration(mAnimationDuration);
        int delay = index * mAnimationDuration / mSubItemCount;
        int invertedDelay = (mSubItemCount - index) * mAnimationDuration / mSubItemCount;
        animation.setStartDelay(mSubItemsShown ? delay/2 : invertedDelay/2);

        animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float val = (float) animation.getAnimatedValue();
                viewGroup.setX((mSubItemWidth/2 * val) - mSubItemWidth/2);
            }
        });

        animation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                viewGroup.setLayerType(ViewGroup.LAYER_TYPE_NONE, null);
            }
        });

        animation.start();
    }

    /**
     * Show sub items alpha animation.
     * @param viewGroup The sub item to animate
     * @param index The sub item index. Needed to calculate delays.
     */
    private void animateViewAlpha(final ViewGroup viewGroup, int index) {
        if (viewGroup == null) {
            return;
        }
        ValueAnimator animation = mSubItemsShown ?
                ValueAnimator.ofFloat(0f, 1f) :
                ValueAnimator.ofFloat(1f, 0f);
        animation.setDuration(mSubItemsShown ? mAnimationDuration * 2 : mAnimationDuration);
        int delay = index * mAnimationDuration / mSubItemCount;
        animation.setStartDelay(mSubItemsShown ? delay/2 : 0);

        animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float val = (float) animation.getAnimatedValue();
                viewGroup.setAlpha(val);
            }
        });

        animation.start();
    }

    /**
     * Show indicator animation.
     * @param startingPos The position from where the animation should start. Useful when removing sub items.
     */
    private void expandIconIndicator(float startingPos) {
        if (mIndicatorBackground != null) {
            final int totalHeight = (mSubItemHeight * mSubItemCount) - mIndicatorSize/2 + mItemHeight/2;
            mCurrentSubItemsHeight = totalHeight;
            ValueAnimator animation = mSubItemsShown ?
                    ValueAnimator.ofFloat(startingPos, totalHeight) :
                    ValueAnimator.ofFloat(totalHeight, startingPos);
            animation.setInterpolator(new AccelerateDecelerateInterpolator());
            animation.setDuration(mAnimationDuration);
            animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float val = (float) animation.getAnimatedValue();
                    CustomViewUtils.setViewHeight(mIndicatorBackground, (int) val);
                }
            });

            animation.start();
        }
    }

    /**
     * Expand the sub items container with animation
     * @param startingPos The position from where the animation should start. Useful when removing sub items.
     */
    private void expandSubItemsWithAnimation(float startingPos) {
        if (mBaseSubListLayout != null) {
            final int totalHeight = (mSubItemHeight * mSubItemCount);
            ValueAnimator animation = mSubItemsShown ?
                    ValueAnimator.ofFloat(startingPos, totalHeight) :
                    ValueAnimator.ofFloat(totalHeight, startingPos);
            animation.setInterpolator(new AccelerateDecelerateInterpolator());
            animation.setDuration(mAnimationDuration);

            animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float val = (float) animation.getAnimatedValue();
                    CustomViewUtils.setViewHeight(mBaseSubListLayout, (int) val);
                }
            });

            animation.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    if (mSubItemsShown) {
                        adjustItemPosIfHidden();
                    }
                }
            });

            animation.start();
        }
    }

    /**
     * Remove the given sub item after animation ends.
     * @param subItem The view representing the sub item to be removed.
     * @param isAdding true if adding a view. false otherwise.
     */
    private void animateSubItemAppearance(final View subItem, boolean isAdding) {
        ValueAnimator alphaAnimation = isAdding ?
                ValueAnimator.ofFloat(0f, 1f) : ValueAnimator.ofFloat(1f, 0f);
        alphaAnimation.setDuration(isAdding ? mAnimationDuration*2 : mAnimationDuration/2);

        ValueAnimator heightAnimation = isAdding ?
                ValueAnimator.ofFloat(0f, mSubItemHeight) : ValueAnimator.ofFloat(mSubItemHeight, 0f);
        heightAnimation.setDuration(mAnimationDuration/2);
        heightAnimation.setStartDelay(mAnimationDuration/2);

        alphaAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float val = (float) animation.getAnimatedValue();
                subItem.setAlpha(val);
            }
        });

        heightAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float val = (float) animation.getAnimatedValue();
                CustomViewUtils.setViewHeight(subItem, (int) val);
            }
        });

        alphaAnimation.start();
        heightAnimation.start();

        if (!isAdding) {
            heightAnimation.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    removeSubItemFromList(subItem);
                }
            });
        }
    }

}
