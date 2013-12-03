package com.google.android.velvet.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;

import com.google.android.shared.ui.CoScrollContainer;
import com.google.android.shared.ui.OnScrollViewHider;
import com.google.android.velvet.VelvetStrictMode;
import com.google.android.velvet.ui.util.LayoutDebugging;

public class VelvetTopLevelContainer
        extends FrameLayout {
    private int mBaseFooterPadding;
    private View mContextHeader;
    private int mContextHeaderMargin;
    private boolean mContextHeaderShown;
    private View mDogfoodIndicator;
    private View mFooter;
    private boolean mIncludeFooterPadding;
    private boolean mIncludeFooterSpace;
    private View.OnKeyListener mPreImeKeyListener;
    private CoScrollContainer mScrollView;
    private View mSearchPlate;

    public VelvetTopLevelContainer(Context paramContext) {
        super(paramContext);
    }

    public VelvetTopLevelContainer(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }

    public VelvetTopLevelContainer(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
        super(paramContext, paramAttributeSet, paramInt);
    }

    private void setMainContentPadding(int paramInt1, int paramInt2, int paramInt3) {
        MainContentView localMainContentView = (MainContentView) findViewById(paramInt1);
        if (localMainContentView != null) {
            localMainContentView.setHeaderAndFooterPadding(paramInt2, paramInt3);
        }
    }

    private void updateMainContentPadding() {
        if (this.mContextHeaderShown) {
        }
        for (int i = this.mContextHeader.getMeasuredHeight() + this.mContextHeaderMargin; ; i = this.mSearchPlate.getMeasuredHeight()) {
            boolean bool = this.mIncludeFooterPadding;
            int j = 0;
            if (bool) {
                j = 0 + this.mBaseFooterPadding;
            }
            if (this.mIncludeFooterSpace) {
                j += this.mFooter.getMeasuredHeight();
            }
            setMainContentPadding(2131296792, i, j);
            setMainContentPadding(2131296789, i, j);
            if (this.mScrollView != null) {
                this.mScrollView.setHeaderAndFooterPadding(i, j);
            }
            return;
        }
    }

    protected void dispatchDraw(Canvas paramCanvas) {
        VelvetStrictMode.onUiOperationStart("DRAW");
        super.dispatchDraw(paramCanvas);
        VelvetStrictMode.onUiOperationEnd("DRAW");
    }

    public boolean dispatchKeyEventPreIme(KeyEvent paramKeyEvent) {
        VelvetStrictMode.onPreImeKeyEvent(paramKeyEvent);
        if ((this.mPreImeKeyListener != null) && (this.mPreImeKeyListener.onKey(this, paramKeyEvent.getKeyCode(), paramKeyEvent))) {
            return true;
        }
        return super.dispatchKeyEventPreIme(paramKeyEvent);
    }

    protected void onFinishInflate() {
        super.onFinishInflate();
        this.mContextHeader = findViewById(2131296483);
        this.mSearchPlate = findViewById(2131297194);
        this.mFooter = findViewById(2131296640);
        this.mScrollView = ((CoScrollContainer) findViewById(2131296788));
        this.mBaseFooterPadding = getResources().getDimensionPixelSize(2131689595);
        this.mContextHeaderMargin = getResources().getDimensionPixelSize(2131689580);
        LayoutDebugging.maybeTraceLayoutTransitions(this);
    }

    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo paramAccessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(paramAccessibilityNodeInfo);
        paramAccessibilityNodeInfo.setClassName(VelvetTopLevelContainer.class.getCanonicalName());
    }

    protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        VelvetStrictMode.onUiOperationStart("LAYOUT");
        super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
        VelvetStrictMode.onUiOperationEnd("LAYOUT");
    }

    protected void onMeasure(int paramInt1, int paramInt2) {
        VelvetStrictMode.onUiOperationStart("MEASURE");
        measureChildWithMargins(this.mContextHeader, paramInt1, 0, paramInt2, 0);
        measureChildWithMargins(this.mSearchPlate, paramInt1, 0, paramInt2, 0);
        measureChildWithMargins(this.mFooter, paramInt1, 0, paramInt2, 0);
        updateMainContentPadding();
        super.onMeasure(paramInt1, paramInt2);
        VelvetStrictMode.onUiOperationEnd("MEASURE");
    }

    public void setContextHeaderShown(boolean paramBoolean) {
        this.mContextHeaderShown = paramBoolean;
        updateMainContentPadding();
    }

    public void setIncludeFooterPadding(boolean paramBoolean1, boolean paramBoolean2) {
        this.mIncludeFooterSpace = paramBoolean1;
        this.mIncludeFooterPadding = paramBoolean2;
        updateMainContentPadding();
    }

    public void setPreImeKeyListener(View.OnKeyListener paramOnKeyListener) {
        this.mPreImeKeyListener = paramOnKeyListener;
    }

    public void showDogfoodIndicator() {
        if (this.mDogfoodIndicator == null) {
            DogfoodIndicator localDogfoodIndicator = new DogfoodIndicator(getContext());
            FrameLayout.LayoutParams localLayoutParams = new FrameLayout.LayoutParams(-2, -2);
            localLayoutParams.gravity = 51;
            addView(localDogfoodIndicator, localLayoutParams);
            new OnScrollViewHider(localDogfoodIndicator, this.mScrollView, true).setStickiness(2, true, true);
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.velvet.ui.VelvetTopLevelContainer

 * JD-Core Version:    0.7.0.1

 */