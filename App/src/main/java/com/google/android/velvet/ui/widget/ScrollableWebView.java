package com.google.android.velvet.ui.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.google.android.shared.ui.CoScrollContainer.LayoutParams;
import com.google.android.shared.ui.CoScrollContainer.LayoutParams.ScrollableChild;
import com.google.common.base.Preconditions;

public class ScrollableWebView
        extends TextScalingWebview
        implements CoScrollContainer.LayoutParams.ScrollableChild {
    private CoScrollContainer.LayoutParams mCoScrollLayoutParams;
    private boolean mDestroyed;
    private boolean mRestoreWebViewFocusOnWindowFocusToFixRefresh;
    private boolean mWebKitHasBrokenVisibleRect;

    public ScrollableWebView(Context paramContext) {
        this(paramContext, null, 0);
    }

    public ScrollableWebView(Context paramContext, AttributeSet paramAttributeSet) {
        this(paramContext, paramAttributeSet, 0);
    }

    public ScrollableWebView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
        super(paramContext, paramAttributeSet, paramInt);
        setOverScrollMode(2);
    }

    private void maybeKickWebKitToFixBrokenVisibleRect() {
        if (this.mWebKitHasBrokenVisibleRect) {
            onScrollChanged(getScrollX(), getScrollY(), getScrollX(), getScrollY());
        }
    }

    public void computeScroll() {
    }

    public void destroy() {
        if (getParent() == null) {
        }
        for (boolean bool = true; ; bool = false) {
            Preconditions.checkState(bool, "We MUST have been detached from the UI at this point.");
            this.mDestroyed = true;
            super.destroy();
            return;
        }
    }

    public void getDrawingRect(Rect paramRect) {
        super.getDrawingRect(paramRect);
        paramRect.offset(0, Math.round(getTranslationY()));
        this.mCoScrollLayoutParams.cropDrawingRectByPadding(paramRect);
    }

    @ViewDebug.ExportedProperty(category = "velvet")
    public int getScrollingContentHeight() {
        return (int) Math.floor(getContentHeight() * getScale());
    }

    public boolean onGenericMotionEvent(MotionEvent paramMotionEvent) {
        if (((0x2002 & paramMotionEvent.getSource()) != 0) && ((0x8 & paramMotionEvent.getActionMasked()) != 0)) {
            return false;
        }
        return super.onGenericMotionEvent(paramMotionEvent);
    }

    public void onScrollChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        super.onScrollChanged(paramInt1, paramInt2, paramInt3, paramInt4);
        this.mWebKitHasBrokenVisibleRect = false;
    }

    public boolean onTouchEvent(MotionEvent paramMotionEvent) {
        maybeKickWebKitToFixBrokenVisibleRect();
        if (super.onTouchEvent(paramMotionEvent)) {
            getParent().requestDisallowInterceptTouchEvent(true);
            return true;
        }
        return false;
    }

    public void onWindowFocusChanged(boolean paramBoolean) {
        boolean bool1 = isFocused();
        super.onWindowFocusChanged(paramBoolean);
        if ((this.mRestoreWebViewFocusOnWindowFocusToFixRefresh) && (paramBoolean)) {
            requestFocus();
        }
        if ((!paramBoolean) && (bool1)) {
        }
        for (boolean bool2 = true; ; bool2 = false) {
            this.mRestoreWebViewFocusOnWindowFocusToFixRefresh = bool2;
            return;
        }
    }

    protected boolean overScrollBy(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, boolean paramBoolean) {
        if (this.mCoScrollLayoutParams != null) {
        }
        for (int i = this.mCoScrollLayoutParams.consumeVerticalScroll(paramInt2); ; i = 0) {
            return super.overScrollBy(paramInt1, i, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramInt8, paramBoolean);
        }
    }

    public void scrollTo(int paramInt1, int paramInt2) {
        super.scrollTo(paramInt1, this.mCoScrollLayoutParams.adjustScrollToY(paramInt2));
    }

    public void setFocusable(boolean paramBoolean) {
        if (!this.mDestroyed) {
            super.setFocusable(paramBoolean);
        }
    }

    public void setFocusableInTouchMode(boolean paramBoolean) {
        if (!this.mDestroyed) {
            super.setFocusableInTouchMode(paramBoolean);
        }
    }

    public void setLayoutParams(ViewGroup.LayoutParams paramLayoutParams) {
        if ((paramLayoutParams instanceof CoScrollContainer.LayoutParams)) {
            this.mCoScrollLayoutParams = ((CoScrollContainer.LayoutParams) paramLayoutParams);
        }
        super.setLayoutParams(paramLayoutParams);
    }

    public void setScrollYFromContainer(int paramInt) {
        super.scrollTo(getScrollX(), paramInt);
    }

    public void setTranslationY(float paramFloat) {
        super.setTranslationY(paramFloat);
        this.mWebKitHasBrokenVisibleRect = true;
    }

    public String toString() {
        return super.toString() + "{translation=" + getTranslationY() + ";scroll=" + getScrollY() + "}[" + getTag() + "]";
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.velvet.ui.widget.ScrollableWebView

 * JD-Core Version:    0.7.0.1

 */