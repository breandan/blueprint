package com.android.launcher3;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabContentFactory;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;
import android.widget.TextView;
import java.util.ArrayList;

public class AppsCustomizeTabHost
  extends TabHost
  implements TabHost.OnTabChangeListener, Insettable, LauncherTransitionable
{
  private FrameLayout mAnimationBuffer;
  private AppsCustomizePagedView mAppsCustomizePane;
  private LinearLayout mContent;
  private boolean mInTransition;
  private final Rect mInsets = new Rect();
  private final LayoutInflater mLayoutInflater;
  private Runnable mRelayoutAndMakeVisible;
  private boolean mResetAfterTransition;
  private ViewGroup mTabs;
  private ViewGroup mTabsContainer;
  private boolean mTransitioningToWorkspace;
  
  public AppsCustomizeTabHost(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    this.mLayoutInflater = LayoutInflater.from(paramContext);
    this.mRelayoutAndMakeVisible = new Runnable()
    {
      public void run()
      {
        AppsCustomizeTabHost.this.mTabs.requestLayout();
        AppsCustomizeTabHost.this.mTabsContainer.setAlpha(1.0F);
      }
    };
  }
  
  private void enableAndBuildHardwareLayer()
  {
    if (isHardwareAccelerated())
    {
      setLayerType(2, null);
      buildLayer();
    }
  }
  
  private void onTabChangedEnd(AppsCustomizePagedView.ContentType paramContentType)
  {
    setBackgroundColor(Color.argb((int)(255.0F * (getResources().getInteger(2131427334) / 100.0F)), 0, 0, 0));
    this.mAppsCustomizePane.setContentType(paramContentType);
  }
  
  private void onTabChangedStart() {}
  
  private void reloadCurrentPage()
  {
    this.mAppsCustomizePane.loadAssociatedPages(this.mAppsCustomizePane.getCurrentPage());
    this.mAppsCustomizePane.requestFocus();
  }
  
  private void setVisibilityOfSiblingsWithLowerZOrder(int paramInt)
  {
    ViewGroup localViewGroup = (ViewGroup)getParent();
    if (localViewGroup == null) {}
    View localView1;
    int j;
    label40:
    View localView2;
    do
    {
      return;
      localView1 = ((Launcher)getContext()).getOverviewPanel();
      int i = localViewGroup.getChildCount();
      if (isChildrenDrawingOrderEnabled()) {
        break label92;
      }
      j = 0;
      if (j >= i) {
        break;
      }
      localView2 = localViewGroup.getChildAt(j);
    } while (localView2 == this);
    if ((localView2.getVisibility() == 8) || (localView2 == localView1)) {}
    for (;;)
    {
      j++;
      break label40;
      break;
      localView2.setVisibility(paramInt);
    }
    label92:
    throw new RuntimeException("Failed; can't get z-order of views");
  }
  
  public View getContent()
  {
    View localView = this.mAppsCustomizePane.getContent();
    if (localView != null) {
      return localView;
    }
    return this.mContent;
  }
  
  public AppsCustomizePagedView.ContentType getContentTypeForTabTag(String paramString)
  {
    if (paramString.equals("APPS")) {
      return AppsCustomizePagedView.ContentType.Applications;
    }
    if (paramString.equals("WIDGETS")) {
      return AppsCustomizePagedView.ContentType.Widgets;
    }
    return AppsCustomizePagedView.ContentType.Applications;
  }
  
  public int getDescendantFocusability()
  {
    if (getVisibility() != 0) {
      return 393216;
    }
    return super.getDescendantFocusability();
  }
  
  public String getTabTagForContentType(AppsCustomizePagedView.ContentType paramContentType)
  {
    if (paramContentType == AppsCustomizePagedView.ContentType.Applications) {
      return "APPS";
    }
    if (paramContentType == AppsCustomizePagedView.ContentType.Widgets) {
      return "WIDGETS";
    }
    return "APPS";
  }
  
  boolean isTransitioning()
  {
    return this.mInTransition;
  }
  
  protected void onFinishInflate()
  {
    setup();
    ViewGroup localViewGroup = (ViewGroup)findViewById(2131296340);
    TabWidget localTabWidget = getTabWidget();
    final AppsCustomizePagedView localAppsCustomizePagedView = (AppsCustomizePagedView)findViewById(2131296342);
    this.mTabs = localTabWidget;
    this.mTabsContainer = localViewGroup;
    this.mAppsCustomizePane = localAppsCustomizePagedView;
    this.mAnimationBuffer = ((FrameLayout)findViewById(2131296344));
    this.mContent = ((LinearLayout)findViewById(2131296339));
    if ((localTabWidget == null) || (this.mAppsCustomizePane == null)) {
      throw new Resources.NotFoundException();
    }
    TabHost.TabContentFactory local2 = new TabHost.TabContentFactory()
    {
      public View createTabContent(String paramAnonymousString)
      {
        return localAppsCustomizePagedView;
      }
    };
    String str1 = getContext().getString(2131361899);
    TextView localTextView1 = (TextView)this.mLayoutInflater.inflate(2130968852, localTabWidget, false);
    localTextView1.setText(str1);
    localTextView1.setContentDescription(str1);
    addTab(newTabSpec("APPS").setIndicator(localTextView1).setContent(local2));
    String str2 = getContext().getString(2131361873);
    TextView localTextView2 = (TextView)this.mLayoutInflater.inflate(2130968852, localTabWidget, false);
    localTextView2.setText(str2);
    localTextView2.setContentDescription(str2);
    addTab(newTabSpec("WIDGETS").setIndicator(localTextView2).setContent(local2));
    setOnTabChangedListener(this);
    AppsCustomizeTabKeyEventListener localAppsCustomizeTabKeyEventListener = new AppsCustomizeTabKeyEventListener();
    localTabWidget.getChildTabViewAt(-1 + localTabWidget.getTabCount()).setOnKeyListener(localAppsCustomizeTabKeyEventListener);
    findViewById(2131296341).setOnKeyListener(localAppsCustomizeTabKeyEventListener);
    this.mTabsContainer.setAlpha(0.0F);
  }
  
  public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent)
  {
    if ((this.mInTransition) && (this.mTransitioningToWorkspace)) {
      return true;
    }
    return super.onInterceptTouchEvent(paramMotionEvent);
  }
  
  public void onLauncherTransitionEnd(Launcher paramLauncher, boolean paramBoolean1, boolean paramBoolean2)
  {
    this.mAppsCustomizePane.onLauncherTransitionEnd(paramLauncher, paramBoolean1, paramBoolean2);
    this.mInTransition = false;
    if (paramBoolean1) {
      setLayerType(0, null);
    }
    if (!paramBoolean2)
    {
      this.mAppsCustomizePane.showAllAppsCling();
      this.mAppsCustomizePane.loadAssociatedPages(this.mAppsCustomizePane.getCurrentPage());
      setVisibilityOfSiblingsWithLowerZOrder(4);
    }
  }
  
  public void onLauncherTransitionPrepare(Launcher paramLauncher, boolean paramBoolean1, boolean paramBoolean2)
  {
    this.mAppsCustomizePane.onLauncherTransitionPrepare(paramLauncher, paramBoolean1, paramBoolean2);
    this.mInTransition = true;
    this.mTransitioningToWorkspace = paramBoolean2;
    if (paramBoolean2) {
      setVisibilityOfSiblingsWithLowerZOrder(0);
    }
    for (;;)
    {
      if (this.mResetAfterTransition)
      {
        this.mAppsCustomizePane.reset();
        this.mResetAfterTransition = false;
      }
      return;
      this.mContent.setVisibility(0);
      this.mAppsCustomizePane.loadAssociatedPages(this.mAppsCustomizePane.getCurrentPage(), true);
    }
  }
  
  public void onLauncherTransitionStart(Launcher paramLauncher, boolean paramBoolean1, boolean paramBoolean2)
  {
    this.mAppsCustomizePane.onLauncherTransitionStart(paramLauncher, paramBoolean1, paramBoolean2);
    if (paramBoolean1) {
      enableAndBuildHardwareLayer();
    }
    paramLauncher.dismissWorkspaceCling(null);
  }
  
  public void onLauncherTransitionStep(Launcher paramLauncher, float paramFloat)
  {
    this.mAppsCustomizePane.onLauncherTransitionStep(paramLauncher, paramFloat);
  }
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    if (this.mTabs.getLayoutParams().width <= 0) {}
    for (int i = 1;; i = 0)
    {
      super.onMeasure(paramInt1, paramInt2);
      if (i != 0)
      {
        int j = this.mAppsCustomizePane.getPageContentWidth();
        if ((j > 0) && (this.mTabs.getLayoutParams().width != j))
        {
          this.mTabs.getLayoutParams().width = j;
          this.mRelayoutAndMakeVisible.run();
        }
        super.onMeasure(paramInt1, paramInt2);
      }
      return;
    }
  }
  
  public void onTabChanged(String paramString)
  {
    post(new Runnable()
    {
      public void run()
      {
        if ((AppsCustomizeTabHost.this.mAppsCustomizePane.getMeasuredWidth() <= 0) || (AppsCustomizeTabHost.this.mAppsCustomizePane.getMeasuredHeight() <= 0))
        {
          AppsCustomizeTabHost.this.reloadCurrentPage();
          return;
        }
        int[] arrayOfInt = new int[2];
        AppsCustomizeTabHost.this.mAppsCustomizePane.getVisiblePages(arrayOfInt);
        if ((arrayOfInt[0] == -1) && (arrayOfInt[1] == -1))
        {
          AppsCustomizeTabHost.this.reloadCurrentPage();
          return;
        }
        ArrayList localArrayList = new ArrayList();
        for (int i = arrayOfInt[0]; i <= arrayOfInt[1]; i++) {
          localArrayList.add(AppsCustomizeTabHost.this.mAppsCustomizePane.getPageAt(i));
        }
        AppsCustomizeTabHost.this.mAnimationBuffer.scrollTo(AppsCustomizeTabHost.this.mAppsCustomizePane.getScrollX(), 0);
        int j = -1 + localArrayList.size();
        if (j >= 0)
        {
          View localView = (View)localArrayList.get(j);
          if ((localView instanceof AppsCustomizeCellLayout)) {
            ((AppsCustomizeCellLayout)localView).resetChildrenOnKeyListeners();
          }
          for (;;)
          {
            PagedViewWidget.setDeletePreviewsWhenDetachedFromWindow(false);
            AppsCustomizeTabHost.this.mAppsCustomizePane.removeView(localView);
            PagedViewWidget.setDeletePreviewsWhenDetachedFromWindow(true);
            AppsCustomizeTabHost.this.mAnimationBuffer.setAlpha(1.0F);
            AppsCustomizeTabHost.this.mAnimationBuffer.setVisibility(0);
            FrameLayout.LayoutParams localLayoutParams = new FrameLayout.LayoutParams(localView.getMeasuredWidth(), localView.getMeasuredHeight());
            localLayoutParams.setMargins(localView.getLeft(), localView.getTop(), 0, 0);
            AppsCustomizeTabHost.this.mAnimationBuffer.addView(localView, localLayoutParams);
            j--;
            break;
            if ((localView instanceof PagedViewGridLayout)) {
              ((PagedViewGridLayout)localView).resetChildrenOnKeyListeners();
            }
          }
        }
        AppsCustomizeTabHost.this.onTabChangedStart();
        AppsCustomizeTabHost.this.onTabChangedEnd(this.val$type);
        ObjectAnimator localObjectAnimator1 = LauncherAnimUtils.ofFloat(AppsCustomizeTabHost.this.mAnimationBuffer, "alpha", new float[] { 0.0F });
        localObjectAnimator1.addListener(new AnimatorListenerAdapter()
        {
          private void clearAnimationBuffer()
          {
            AppsCustomizeTabHost.this.mAnimationBuffer.setVisibility(8);
            PagedViewWidget.setRecyclePreviewsWhenDetachedFromWindow(false);
            AppsCustomizeTabHost.this.mAnimationBuffer.removeAllViews();
            PagedViewWidget.setRecyclePreviewsWhenDetachedFromWindow(true);
          }
          
          public void onAnimationCancel(Animator paramAnonymous2Animator)
          {
            clearAnimationBuffer();
          }
          
          public void onAnimationEnd(Animator paramAnonymous2Animator)
          {
            clearAnimationBuffer();
          }
        });
        ObjectAnimator localObjectAnimator2 = LauncherAnimUtils.ofFloat(AppsCustomizeTabHost.this.mAppsCustomizePane, "alpha", new float[] { 1.0F });
        localObjectAnimator2.addListener(new AnimatorListenerAdapter()
        {
          public void onAnimationEnd(Animator paramAnonymous2Animator)
          {
            AppsCustomizeTabHost.this.reloadCurrentPage();
          }
        });
        AnimatorSet localAnimatorSet = LauncherAnimUtils.createAnimatorSet();
        localAnimatorSet.playTogether(new Animator[] { localObjectAnimator1, localObjectAnimator2 });
        localAnimatorSet.setDuration(this.val$duration);
        localAnimatorSet.start();
      }
    });
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    if ((this.mInTransition) && (this.mTransitioningToWorkspace)) {
      return super.onTouchEvent(paramMotionEvent);
    }
    if (paramMotionEvent.getY() < this.mAppsCustomizePane.getBottom()) {
      return true;
    }
    return super.onTouchEvent(paramMotionEvent);
  }
  
  public void onTrimMemory()
  {
    this.mContent.setVisibility(8);
    this.mAppsCustomizePane.clearAllWidgetPages();
  }
  
  public void onWindowVisible()
  {
    if (getVisibility() == 0)
    {
      this.mContent.setVisibility(0);
      this.mAppsCustomizePane.loadAssociatedPages(this.mAppsCustomizePane.getCurrentPage(), true);
      this.mAppsCustomizePane.loadAssociatedPages(this.mAppsCustomizePane.getCurrentPage());
    }
  }
  
  void reset()
  {
    if (this.mInTransition)
    {
      this.mResetAfterTransition = true;
      return;
    }
    this.mAppsCustomizePane.reset();
  }
  
  void setContentTypeImmediate(AppsCustomizePagedView.ContentType paramContentType)
  {
    setOnTabChangedListener(null);
    onTabChangedStart();
    onTabChangedEnd(paramContentType);
    setCurrentTabByTag(getTabTagForContentType(paramContentType));
    setOnTabChangedListener(this);
  }
  
  public void setCurrentTabFromContent(AppsCustomizePagedView.ContentType paramContentType)
  {
    setOnTabChangedListener(null);
    setCurrentTabByTag(getTabTagForContentType(paramContentType));
    setOnTabChangedListener(this);
  }
  
  public void setInsets(Rect paramRect)
  {
    this.mInsets.set(paramRect);
    FrameLayout.LayoutParams localLayoutParams = (FrameLayout.LayoutParams)this.mContent.getLayoutParams();
    localLayoutParams.topMargin = paramRect.top;
    localLayoutParams.bottomMargin = paramRect.bottom;
    localLayoutParams.leftMargin = paramRect.left;
    localLayoutParams.rightMargin = paramRect.right;
    this.mContent.setLayoutParams(localLayoutParams);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.launcher3.AppsCustomizeTabHost
 * JD-Core Version:    0.7.0.1
 */