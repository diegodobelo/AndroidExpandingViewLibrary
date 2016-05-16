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
    private static final int DEFAULT_ANIM_DURATION = 300;

    private ViewGroup mItemLayout;
    private LayoutInflater mInflater;
    private RelativeLayout mBaseLayout;
    private LinearLayout mBaseListLayout;
    private LinearLayout mBaseSubListLayout;
    private ImageView mIndicatorImage;
    private View mIndicatorBackground;
    private ViewStub mSeparatorStub;
    private ViewGroup mIndicatorContainer;

    private int mSubItemRes;

    private int mItemHeight;
    private int mSubItemHeight;
    private int mSubItemWidth;
    private int mSubItemCount;
    private int mIndicatorSize;
    private int mAnimationDuration;
    private int mIndicatorMarginLeft;
    private int mIndicatorMarginRight;

    private boolean mShowIndicator;
    private boolean mShowAnimation;
    private boolean mSubItemsShown;

    private int mItemLayoutId;
    private int mSeparatorLayoutId;

    //TODO: make it a list
    private OnItemStateChanged mListener;
    private int mCurrentSubItemsHeight;

    public interface OnItemStateChanged {
        void itemCollapseStateChanged(boolean expanded);
    }

    public ExpandingItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ExpandingItem(Context context, AttributeSet attrs) {
        super(context, attrs);

        readAttributes(context, attrs);
        inflateLayouts(context);

        setupIndicator();

        addItem(mItemLayout);
        addView(mBaseLayout);
    }

    private void setupIndicator() {
        if (mIndicatorSize != 0) {
            setIndicatorBackgroundSize();
        }

        mIndicatorContainer.setVisibility(mShowIndicator && mIndicatorSize != 0 ? VISIBLE : GONE);
    }

    private void readAttributes(Context context, AttributeSet attrs) {
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.ExpandingItem, 0, 0);

        try {
            mItemLayoutId = array.getResourceId(R.styleable.ExpandingItem_item_layout, 0);
            mSeparatorLayoutId = array.getResourceId(R.styleable.ExpandingItem_separator_layout, 0);
            mSubItemRes = array.getResourceId(R.styleable.ExpandingItem_sub_item_layout, 0);
            mIndicatorSize = array.getDimensionPixelSize(R.styleable.ExpandingItem_indicator_size, 0);
            mIndicatorMarginLeft = array.getDimensionPixelSize(R.styleable.ExpandingItem_indicator_margin_left, 0);
            mIndicatorMarginRight = array.getDimensionPixelSize(R.styleable.ExpandingItem_indicator_margin_right, 0);
            mShowIndicator = array.getBoolean(R.styleable.ExpandingItem_show_indicator, true);
            mShowAnimation = array.getBoolean(R.styleable.ExpandingItem_show_animation, true);
            mAnimationDuration = array.getInt(R.styleable.ExpandingItem_animation_duration, DEFAULT_ANIM_DURATION);
        } finally {
            array.recycle();
        }
    }

    private void inflateLayouts(Context context) {
        mInflater = LayoutInflater.from(context);
        mBaseLayout = (RelativeLayout) mInflater.inflate(R.layout.expanding_item_base_layout,
                null, false);
        mBaseListLayout = (LinearLayout) mBaseLayout.findViewById(R.id.base_list_layout);
        mBaseSubListLayout = (LinearLayout) mBaseLayout.findViewById(R.id.base_sub_list_layout);
        mIndicatorImage = (ImageView) mBaseLayout.findViewById(R.id.indicator_image);
        mBaseLayout.findViewById(R.id.icon_container).bringToFront();
        mSeparatorStub = (ViewStub) mBaseLayout.findViewById(R.id.base_separator_stub);
        mIndicatorBackground = mBaseLayout.findViewById(R.id.icon_indicator_middle);
        mIndicatorContainer = (ViewGroup) mBaseLayout.findViewById(R.id.indicator_container);
        mIndicatorContainer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                expand();
            }
        });

        if (mItemLayoutId != 0) {
            mItemLayout = (ViewGroup) mInflater.inflate(mItemLayoutId, null, false);
        }
        if (mSeparatorLayoutId != 0) {
            mSeparatorStub.setLayoutResource(mSeparatorLayoutId);
            mSeparatorStub.inflate();
        }

        if (!mShowAnimation) {
            mAnimationDuration = 0;
        }
    }

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

    private void addItem(final ViewGroup item) {
        if (item != null) {
            mBaseListLayout.addView(item);
            item.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    expand();
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

    public void setStateChangedListener(OnItemStateChanged listener) {
        mListener = listener;
    }

    public boolean isExpanded() {
        return mSubItemsShown;
    }

    public int getSubItemsCount() {
        return mSubItemCount;
    }

    public void collapse() {
        mBaseSubListLayout.post(new Runnable() {
            @Override
            public void run() {
                CustomViewUtils.setViewHeight(mBaseSubListLayout, 0);
            }
        });
    }

    public void expand() {
        if (mSubItemCount == 0) {
            return;
        }
        toggleSubItems();
        expandSubItemsWithAnimation(0f);
        expandIconIndicator(0f);
        animateSubItemsIn();
    }

    public void setIndicatorColorRes(int colorRes) {
        setIndicatorColor(ContextCompat.getColor(getContext(), colorRes));
    }

    public void setIndicatorColor(int color) {
        ((GradientDrawable)findViewById(R.id.icon_indicator_top).getBackground().mutate()).setColor(color);
        ((GradientDrawable)findViewById(R.id.icon_indicator_bottom).getBackground().mutate()).setColor(color);
        findViewById(R.id.icon_indicator_middle).setBackgroundColor(color);
    }

    public void setIndicatorIconRes(int res) {
        setIndicatorIcon(ContextCompat.getDrawable(getContext(), res));
    }

    public void setIndicatorIcon(Drawable icon) {
        mIndicatorImage.setImageDrawable(icon);
    }

    @Nullable
    public View createSubItem(int index) {
        if (mSubItemRes == 0) {
            throw new RuntimeException("There is no layout to be inflated. " +
                    "Please set sub_item_layout value");
        }
        if (mBaseSubListLayout.getChildAt(index) != null) {
            return mBaseSubListLayout.getChildAt(index);
        }
        //TODO: verify if not null
        ViewGroup subItemLayout = (ViewGroup) mInflater.inflate(mSubItemRes, null, false);
        mBaseSubListLayout.addView(subItemLayout);
        mSubItemCount++;
        setSubItemDimensions(subItemLayout);
        return subItemLayout;
    }

    public void createSubItems(int count) {
        if (mSubItemRes == 0) {
            throw new RuntimeException("There is no layout to be inflated. " +
                    "Please set sub_item_layout value");
        }
        for (int i = 0; i < count; i++) {
            createSubItem(i);
        }
    }

    public View getSubItemView(int position) {
        if (mBaseSubListLayout.getChildAt(position) != null) {
            return mBaseSubListLayout.getChildAt(position);
        }
        throw new RuntimeException("There is no sub item for position " + position +
                ". There are only " + mBaseSubListLayout.getChildCount() + " in the list.");
    }

    public void removeSubItemAt(int position) {
        removeSubItem(mBaseSubListLayout.getChildAt(position));
    }

    public void removeSubItem(View view) {
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

    public void removeSubItem(View view, boolean animate) {
        if (animate) {
            removeSubItemWithAnimation(view);
        } else {
            removeSubItem(view);
        }
    }

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

    private void toggleSubItems() {
        mSubItemsShown = !mSubItemsShown;
        if (mListener != null) {
            mListener.itemCollapseStateChanged(mSubItemsShown);
        }
    }

    private void animateSubItemsIn() {
        for (int i = 0; i < mSubItemCount; i++) {
            animateSubViews((ViewGroup) mBaseSubListLayout.getChildAt(i), i);
            animateViewAlpha((ViewGroup) mBaseSubListLayout.getChildAt(i), i);
        }
    }

    private void animateSubViews(final ViewGroup viewGroup, int index) {
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

    private void animateViewAlpha(final ViewGroup viewGroup, int index) {
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

            animation.start();
        }
    }

    private void removeSubItemWithAnimation(final View subItem) {
        ValueAnimator alphaAnimation =
                ValueAnimator.ofFloat(1f, 0f);
        alphaAnimation.setDuration(mAnimationDuration/2);

        ValueAnimator heightAnimation =
                ValueAnimator.ofFloat(mSubItemHeight, 0f);
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

        heightAnimation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                removeSubItem(subItem);
            }
        });
    }

}
