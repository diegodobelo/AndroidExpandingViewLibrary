package utils;

import android.view.View;
import android.view.ViewGroup;

/**
 * View Utils class
 * Created by diego on 5/16/16.
 */
public class CustomViewUtils {
    /**
     * Set a View height in pixels.
     *
     * @param v      The view to set the height.
     * @param height The height in pixels.
     */
    public static void setViewHeight(View v, int height) {
        final ViewGroup.LayoutParams params = v.getLayoutParams();
        params.height = height;
        v.requestLayout();
    }

    /**
     * Set a View width in pixels.
     *
     * @param v     The view to set the width.
     * @param width The width in pixels.
     */
    public static void setViewWidth(View v, int width) {
        final ViewGroup.LayoutParams params = v.getLayoutParams();
        params.width = width;
        v.requestLayout();
    }

    /**
     * Set a View margin top.
     *
     * @param v         The View to set the margin top.
     * @param marginTop the margin top in pixles
     */
    public static void setViewMarginTop(View v, int marginTop) {
        setViewMargin(v, 0, marginTop, 0, 0);
    }

    /**
     * Set a View margin.
     *
     * @param v      The view to set margins.
     * @param left   The margin at left in pixels
     * @param top    The margin at top in pixels
     * @param right  The margin at right in pixels
     * @param bottom The margin at bottom in pixels
     */
    public static void setViewMargin(View v, int left, int top, int right, int bottom) {
        final ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
        params.setMargins(left, top, right, bottom);
        v.requestLayout();
    }
}
