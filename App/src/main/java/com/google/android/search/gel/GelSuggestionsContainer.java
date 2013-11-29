package com.google.android.search.gel;

import android.animation.LayoutTransition;
import android.animation.LayoutTransition.TransitionListener;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.google.android.search.shared.api.Suggestion;
import com.google.android.search.shared.ui.BakedBezierInterpolator;
import com.google.android.search.shared.ui.LinearRemapInterpolator;
import com.google.android.search.shared.ui.PathClippingView;
import com.google.android.search.shared.ui.SlideAnimator;
import com.google.android.search.shared.ui.SlideAnimator.SlideAnimatorViewGroup;
import com.google.android.search.shared.ui.SuggestionClickListener;
import com.google.android.search.shared.ui.SuggestionFormatter;
import com.google.android.search.shared.ui.SuggestionListView;
import com.google.android.search.shared.ui.SuggestionViewFactory;
import com.google.android.search.shared.ui.ViewRecycler;
import com.google.android.shared.util.UriLoader;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;

public class GelSuggestionsContainer
  extends LinearLayout
  implements GelSuggestionsController.Ui, PathClippingView, SlideAnimator.SlideAnimatorViewGroup
{
  private static long APPEAR_TRANSITION_DURATION_MS = 150L;
  private static long CLIP_ADD_DURATION_MS = 200L;
  private static long TRANSITION_DURATION_MS = 100L;
  private static TimeInterpolator mClipSlideUpInterpolator = new LinearRemapInterpolator(BakedBezierInterpolator.INSTANCE, 0.0F, 0.5F);
  private Path mClipPath;
  private GetGoogleNowView mGetGoogleNowView;
  private final LayoutTransition mLayoutTransition;
  private OnVerticalScrollListener mListener;
  private final Map<View, View> mNearestPreviousVisibleChild;
  private boolean mNearestPreviousVisibleChildMapInvalidated;
  private int mOffset;
  private boolean mPendingEnableLayoutTransitions;
  private int mSlideDistance;
  private final SlideAnimator mSlideDownAnimator;
  private final SlideAnimator mSlideUpAnimator;
  private SuggestionListView mSummonsSuggestionsView;
  private float mTouchDownYPosition;
  private final int mTouchSlop;
  private boolean mVerticalScrollDetected;
  private SuggestionListView mWebSuggestionsView;
  
  public GelSuggestionsContainer(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    setOrientation(1);
    this.mSlideDownAnimator = new SlideAnimator(false, this);
    this.mSlideUpAnimator = new SlideAnimator(true, this);
    this.mLayoutTransition = getLayoutTransition();
    setUpDefaultLayoutTransitions();
    this.mNearestPreviousVisibleChild = Maps.newHashMap();
    if (this.mLayoutTransition != null) {
      this.mLayoutTransition.addTransitionListener(new LayoutTransition.TransitionListener()
      {
        public void endTransition(LayoutTransition paramAnonymousLayoutTransition, ViewGroup paramAnonymousViewGroup, View paramAnonymousView, int paramAnonymousInt)
        {
          if (paramAnonymousInt == 3) {
            GelSuggestionsContainer.access$002(GelSuggestionsContainer.this, true);
          }
        }
        
        public void startTransition(LayoutTransition paramAnonymousLayoutTransition, ViewGroup paramAnonymousViewGroup, View paramAnonymousView, int paramAnonymousInt)
        {
          if (paramAnonymousInt == 2) {
            GelSuggestionsContainer.access$002(GelSuggestionsContainer.this, true);
          }
        }
      });
    }
    this.mTouchSlop = ViewConfiguration.get(paramContext).getScaledTouchSlop();
  }
  
  private void enableLayoutTransition(boolean paramBoolean)
  {
    if (paramBoolean) {}
    for (LayoutTransition localLayoutTransition = this.mLayoutTransition;; localLayoutTransition = null)
    {
      setLayoutTransition(localLayoutTransition);
      return;
    }
  }
  
  private void maybeRebuildNearestPreviousVisibleChildMap()
  {
    if (this.mNearestPreviousVisibleChildMapInvalidated)
    {
      this.mNearestPreviousVisibleChildMapInvalidated = false;
      Object localObject = null;
      for (int i = 0; i < getChildCount(); i++)
      {
        View localView = getChildAt(i);
        this.mNearestPreviousVisibleChild.put(localView, localObject);
        if (localView.getVisibility() == 0) {
          localObject = localView;
        }
      }
    }
  }
  
  private void setUpDefaultLayoutTransitions()
  {
    if (this.mLayoutTransition != null)
    {
      LayoutTransition localLayoutTransition = this.mLayoutTransition;
      localLayoutTransition.setDuration(TRANSITION_DURATION_MS);
      localLayoutTransition.setDuration(2, APPEAR_TRANSITION_DURATION_MS);
      localLayoutTransition.setDuration(3, APPEAR_TRANSITION_DURATION_MS);
      localLayoutTransition.setAnimator(2, this.mSlideDownAnimator);
      localLayoutTransition.setAnimator(3, this.mSlideUpAnimator);
      localLayoutTransition.setStartDelay(1, APPEAR_TRANSITION_DURATION_MS);
      localLayoutTransition.setStartDelay(2, 0L);
      localLayoutTransition.enableTransitionType(4);
      this.mSlideDownAnimator.setMaxTranslation(0);
      this.mSlideUpAnimator.setMaxTranslation(0);
      this.mSlideUpAnimator.setInterpolator(BakedBezierInterpolator.INSTANCE);
    }
  }
  
  private void setUpQuantumLayoutTransitions()
  {
    if (this.mLayoutTransition != null)
    {
      LayoutTransition localLayoutTransition = this.mLayoutTransition;
      this.mSlideUpAnimator.setMaxTranslation(this.mSlideDistance);
      this.mSlideDownAnimator.setMaxTranslation(this.mSlideDistance);
      localLayoutTransition.setDuration(2, 350L + CLIP_ADD_DURATION_MS);
      localLayoutTransition.setDuration(3, 350L);
      this.mSlideUpAnimator.setInterpolator(mClipSlideUpInterpolator);
    }
  }
  
  public void disableLayoutTransitionsUntilNextLayout()
  {
    enableLayoutTransition(false);
    this.mPendingEnableLayoutTransitions = true;
  }
  
  protected void dispatchDraw(Canvas paramCanvas)
  {
    if (this.mClipPath != null)
    {
      paramCanvas.save();
      paramCanvas.clipPath(this.mClipPath);
      super.dispatchDraw(paramCanvas);
      paramCanvas.restore();
      return;
    }
    super.dispatchDraw(paramCanvas);
  }
  
  protected boolean drawChild(Canvas paramCanvas, View paramView, long paramLong)
  {
    View localView = getNearestPreviousVisibleSibling(paramView);
    if (localView != null)
    {
      int i = Math.max(0, localView.getBottom() + (int)localView.getTranslationY() + this.mOffset);
      paramCanvas.save();
      paramCanvas.clipRect(0, i, getWidth(), getHeight());
      boolean bool = super.drawChild(paramCanvas, paramView, paramLong);
      paramCanvas.restore();
      return bool;
    }
    return super.drawChild(paramCanvas, paramView, paramLong);
  }
  
  public int getChildrenHeight()
  {
    for (int i = -1 + getChildCount(); i >= 0; i--)
    {
      View localView = getChildAt(i);
      if (localView.getVisibility() != 8) {
        return localView.getBottom();
      }
    }
    return 0;
  }
  
  public View getNearestPreviousVisibleSibling(View paramView)
  {
    maybeRebuildNearestPreviousVisibleChildMap();
    return (View)this.mNearestPreviousVisibleChild.get(paramView);
  }
  
  public void greyOutGoogleNowPromo()
  {
    this.mGetGoogleNowView.setEnabled(false);
  }
  
  public void greyOutSummonsSuggestions()
  {
    this.mSummonsSuggestionsView.hideSuggestions();
  }
  
  public void greyOutWebSuggestions()
  {
    this.mWebSuggestionsView.hideSuggestions();
  }
  
  public void hideSuggestions()
  {
    this.mWebSuggestionsView.setVisibility(8);
    this.mSummonsSuggestionsView.setVisibility(8);
    this.mGetGoogleNowView.setVisibility(8);
  }
  
  public void initialize(SuggestionViewFactory paramSuggestionViewFactory, SuggestionFormatter paramSuggestionFormatter, UriLoader<Drawable> paramUriLoader, ViewRecycler paramViewRecycler, GoogleNowPromoController paramGoogleNowPromoController)
  {
    this.mWebSuggestionsView.init(paramSuggestionViewFactory, paramSuggestionFormatter, paramUriLoader, paramViewRecycler);
    this.mSummonsSuggestionsView.init(paramSuggestionViewFactory, paramSuggestionFormatter, paramUriLoader, paramViewRecycler);
    this.mGetGoogleNowView.init(paramGoogleNowPromoController);
  }
  
  void invalidateNearestPreviousVisibleChildMap()
  {
    this.mNearestPreviousVisibleChildMapInvalidated = true;
  }
  
  protected void onFinishInflate()
  {
    this.mWebSuggestionsView = ((SuggestionListView)findViewById(2131296963));
    this.mSummonsSuggestionsView = ((SuggestionListView)findViewById(2131296964));
    this.mGetGoogleNowView = ((GetGoogleNowView)findViewById(2131296667));
  }
  
  public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent)
  {
    int i = paramMotionEvent.getActionMasked();
    if (i == 0)
    {
      this.mTouchDownYPosition = paramMotionEvent.getY();
      this.mVerticalScrollDetected = false;
    }
    do
    {
      do
      {
        return false;
      } while ((i != 2) || (this.mVerticalScrollDetected) || (Math.abs(paramMotionEvent.getY() - this.mTouchDownYPosition) <= this.mTouchSlop));
      this.mVerticalScrollDetected = true;
    } while (this.mListener == null);
    this.mListener.onVerticalScrollDetected();
    return false;
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
    if (this.mPendingEnableLayoutTransitions)
    {
      this.mPendingEnableLayoutTransitions = false;
      enableLayoutTransition(true);
    }
  }
  
  public void retainWebSuggestions(int paramInt)
  {
    this.mWebSuggestionsView.retain(paramInt);
  }
  
  public void setClipOffset(int paramInt)
  {
    this.mOffset = paramInt;
  }
  
  public void setClipPath(Path paramPath)
  {
    if ((this.mClipPath == null) && (paramPath != null))
    {
      setUpQuantumLayoutTransitions();
      setClipToPadding(false);
    }
    for (;;)
    {
      this.mClipPath = paramPath;
      invalidate();
      return;
      if ((this.mClipPath != null) && (paramPath == null))
      {
        setUpDefaultLayoutTransitions();
        setClipToPadding(true);
      }
    }
  }
  
  public void setOnVerticalScrollListener(OnVerticalScrollListener paramOnVerticalScrollListener)
  {
    this.mListener = paramOnVerticalScrollListener;
  }
  
  public void setSlideDistance(int paramInt)
  {
    this.mSlideDistance = paramInt;
  }
  
  public void setSuggestionClickListener(SuggestionClickListener paramSuggestionClickListener)
  {
    this.mWebSuggestionsView.setSuggestionClickListener(paramSuggestionClickListener);
    this.mSummonsSuggestionsView.setSuggestionClickListener(paramSuggestionClickListener);
  }
  
  public void setSummonsFooterClickListener(View.OnClickListener paramOnClickListener)
  {
    this.mSummonsSuggestionsView.setFooterClickListener(paramOnClickListener);
  }
  
  public void showGoogleNowPromo(boolean paramBoolean)
  {
    this.mGetGoogleNowView.setEnabled(paramBoolean);
    GetGoogleNowView localGetGoogleNowView = this.mGetGoogleNowView;
    if (paramBoolean) {}
    for (int i = 0;; i = 8)
    {
      localGetGoogleNowView.setVisibility(i);
      return;
    }
  }
  
  public void showSummonsFooter(boolean paramBoolean)
  {
    SuggestionListView localSuggestionListView = this.mSummonsSuggestionsView;
    if (paramBoolean) {}
    for (int i = 0;; i = 8)
    {
      localSuggestionListView.setFooterVisibility(i);
      this.mSummonsSuggestionsView.setShowAllDividers(paramBoolean);
      return;
    }
  }
  
  public void updateSummonsSuggestions(String paramString, List<Suggestion> paramList, int paramInt)
  {
    this.mSummonsSuggestionsView.showSuggestions(paramString, paramList, paramInt, true);
  }
  
  public void updateWebSuggestions(String paramString, List<Suggestion> paramList, int paramInt)
  {
    this.mWebSuggestionsView.showSuggestions(paramString, paramList, paramInt, true);
  }
  
  public static abstract interface OnVerticalScrollListener
  {
    public abstract void onVerticalScrollDetected();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.gel.GelSuggestionsContainer
 * JD-Core Version:    0.7.0.1
 */