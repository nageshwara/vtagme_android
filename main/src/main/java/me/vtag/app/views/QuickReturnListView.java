package me.vtag.app.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;

import java.util.Arrays;

/**
 * Created by nageswara on 6/5/14.
 */
public class QuickReturnListView extends ListView {

    private int mItemCount;
    private int[] mItemOffsetY;
    private boolean scrollIsComputed = false;
    private int mHeight;

    public QuickReturnListView(Context context) {
        super(context);
    }

    public QuickReturnListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public int getListHeight() {
        return mHeight;
    }

    public void computeScrollY() {
        mHeight = 0;
        if (getAdapter() == null) {
            return;
        }
        int newCount = getAdapter().getCount();
        if (newCount != mItemCount) {
            mItemCount = newCount;
            if (mItemOffsetY != null) {
                mItemOffsetY = Arrays.copyOf(mItemOffsetY, mItemCount);
            } else {
                mItemOffsetY = new int[mItemCount];
            }
        }
        for (int i = 0; i < mItemCount; ++i) {
            View view = getAdapter().getView(i, null, this);
            view.measure(
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            mItemOffsetY[i] = mHeight;
            mHeight += view.getMeasuredHeight();
            //System.out.println(mHeight);
        }
        scrollIsComputed = true;
    }

    public boolean scrollYIsComputed() {
        return scrollIsComputed;
    }

    public int getComputedScrollY() {
        int pos, nScrollY, nItemY;
        View view = null;
        pos = getFirstVisiblePosition();
        view = getChildAt(0);
        if (view != null) {
            nItemY = view.getTop();
        } else {
            nItemY = 0;
        }
        nScrollY = mItemOffsetY[pos] - nItemY;
        return nScrollY;
    }
}