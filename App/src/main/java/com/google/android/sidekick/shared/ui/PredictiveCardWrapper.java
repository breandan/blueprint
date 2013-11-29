package com.google.android.sidekick.shared.ui;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import com.google.android.search.shared.ui.SuggestionGridLayout;
import com.google.android.search.shared.ui.SuggestionGridLayout.CardWrapper;
import com.google.android.search.shared.ui.SuggestionGridLayout.DismissableChildContainer;
import com.google.android.shared.util.LayoutUtils;
import com.google.android.sidekick.shared.cards.EntryCardViewAdapter;
import com.google.android.sidekick.shared.client.PredictiveCardContainer;
import com.google.android.sidekick.shared.training.BackOfCardAdapter;
import com.google.common.base.Preconditions;
import com.google.geo.sidekick.Sidekick.Entry;
import javax.annotation.Nullable;

public class PredictiveCardWrapper
  extends ViewGroup
  implements SuggestionGridLayout.CardWrapper, SuggestionGridLayout.DismissableChildContainer
{
  private BackOfCardAdapter mBackOfCardAdapter;
  private View mCardView;
  private int mCurrentHeight;
  private EntryCardViewAdapter mEntryAdapter;
  private boolean mExpanded;
  private int mForcedCardHeight;
  private View mMenuButton;
  private Bundle mPendingSettingsViewState;
  @Nullable
  private View mSettingsView;
  private int mStackOrderOverride = -1;
  private final Rect mTmpCardRect = new Rect();
  private final Rect mTmpRect = new Rect();
  
  public PredictiveCardWrapper(Context paramContext)
  {
    super(paramContext);
    init();
  }
  
  private void addMenuButtonOverlay(final PredictiveCardContainer paramPredictiveCardContainer)
  {
    if (this.mCardView.findViewById(2131296461) != null)
    {
      this.mMenuButton = ((LayoutInflater)getContext().getSystemService("layout_inflater")).inflate(2130968631, this, false);
      this.mMenuButton.setSelected(true);
      this.mMenuButton.setOnClickListener(new EntryClickListener(paramPredictiveCardContainer, this.mEntryAdapter.getEntry(), 117)
      {
        protected void onEntryClick(View paramAnonymousView)
        {
          paramPredictiveCardContainer.toggleBackOfCard(PredictiveCardWrapper.this.mEntryAdapter);
        }
      });
      this.mMenuButton.setVisibility(0);
      addView(this.mMenuButton);
    }
  }
  
  private static boolean getBackgroundRect(View paramView, Rect paramRect)
  {
    Drawable localDrawable = paramView.getBackground();
    if (localDrawable == null) {
      return false;
    }
    localDrawable.getPadding(paramRect);
    return true;
  }
  
  private boolean getSettingsOverdrawRect(Rect paramRect)
  {
    Preconditions.checkState(this.mExpanded);
    if (!getBackgroundRect(this.mSettingsView, paramRect)) {
      return false;
    }
    if (getBackgroundRect(this.mCardView, this.mTmpCardRect))
    {
      paramRect.left -= this.mTmpCardRect.left;
      paramRect.top -= this.mTmpCardRect.top;
      paramRect.right -= this.mTmpCardRect.right;
      paramRect.bottom -= this.mTmpCardRect.bottom;
    }
    return true;
  }
  
  private void init()
  {
    setId(2131296282);
  }
  
  private boolean isSettingsViewVisible()
  {
    return (this.mExpanded) && (this.mSettingsView != null) && (this.mSettingsView.getVisibility() == 0);
  }
  
  private void maybeRestoreSettingsViewState()
  {
    if (this.mPendingSettingsViewState != null)
    {
      this.mBackOfCardAdapter.restoreViewState(this.mPendingSettingsViewState);
      this.mPendingSettingsViewState = null;
    }
  }
  
  private void measureMenuButtonOverlay()
  {
    if (this.mMenuButton != null)
    {
      int i = getResources().getDimensionPixelSize(2131689724);
      this.mMenuButton.measure(View.MeasureSpec.makeMeasureSpec(i, 1073741824), View.MeasureSpec.makeMeasureSpec(i, 1073741824));
    }
  }
  
  private void removeMenuButtonOverlay()
  {
    if (this.mMenuButton != null)
    {
      removeView(this.mMenuButton);
      this.mMenuButton = null;
    }
  }
  
  protected boolean drawChild(Canvas paramCanvas, View paramView, long paramLong)
  {
    boolean bool1 = isSettingsViewVisible();
    int i = 0;
    if (bool1)
    {
      paramCanvas.save();
      i = 1;
      this.mTmpRect.top = paramView.getTop();
      this.mTmpRect.left = paramView.getLeft();
      this.mTmpRect.right = paramView.getRight();
      if (this.mCurrentHeight != getMeasuredHeight()) {
        break label107;
      }
    }
    for (this.mTmpRect.bottom = paramView.getBottom();; this.mTmpRect.bottom = this.mCurrentHeight)
    {
      paramCanvas.clipRect(this.mTmpRect);
      boolean bool2 = super.drawChild(paramCanvas, paramView, paramLong);
      if (i != 0) {
        paramCanvas.restore();
      }
      return bool2;
      label107:
      getSettingsOverdrawRect(this.mTmpCardRect);
    }
  }
  
  @Nullable
  public BackOfCardAdapter getBackOfCardAdapter()
  {
    return this.mBackOfCardAdapter;
  }
  
  public View getCardView()
  {
    if (this.mEntryAdapter != null) {}
    for (boolean bool = true;; bool = false)
    {
      Preconditions.checkState(bool, "Should invoke setCardView() first");
      return this.mCardView;
    }
  }
  
  public EntryCardViewAdapter getEntryCardViewAdapter()
  {
    if (this.mEntryAdapter != null) {}
    for (boolean bool = true;; bool = false)
    {
      Preconditions.checkState(bool, "Should invoke setCardView() first");
      return this.mEntryAdapter;
    }
  }
  
  public boolean getOverdrawRect(Rect paramRect)
  {
    if (this.mExpanded) {
      return getSettingsOverdrawRect(paramRect);
    }
    return false;
  }
  
  @Nullable
  public View getSettingsView()
  {
    return this.mSettingsView;
  }
  
  public int getStackOrderOverride()
  {
    return this.mStackOrderOverride;
  }
  
  public void hideSettingsView(boolean paramBoolean)
  {
    this.mExpanded = false;
    View localView = this.mCardView.findViewById(2131296461);
    if (localView != null) {
      localView.setSelected(false);
    }
    removeMenuButtonOverlay();
    if (paramBoolean)
    {
      Animation localAnimation = AnimationUtils.loadAnimation(getContext(), 2131034116);
      localAnimation.setAnimationListener(new Animation.AnimationListener()
      {
        public void onAnimationEnd(Animation paramAnonymousAnimation)
        {
          PredictiveCardWrapper.this.removeView(PredictiveCardWrapper.this.mSettingsView);
          PredictiveCardWrapper.access$102(PredictiveCardWrapper.this, null);
          PredictiveCardWrapper.this.mBackOfCardAdapter.onPause();
        }
        
        public void onAnimationRepeat(Animation paramAnonymousAnimation) {}
        
        public void onAnimationStart(Animation paramAnonymousAnimation) {}
      });
      this.mSettingsView.startAnimation(localAnimation);
    }
    for (;;)
    {
      requestLayout();
      return;
      removeView(this.mSettingsView);
      this.mSettingsView = null;
      this.mBackOfCardAdapter.onPause();
    }
  }
  
  public boolean isDismissableViewAtPosition(SuggestionGridLayout paramSuggestionGridLayout, MotionEvent paramMotionEvent)
  {
    if ((this.mCardView instanceof SuggestionGridLayout.DismissableChildContainer)) {
      return ((SuggestionGridLayout.DismissableChildContainer)this.mCardView).isDismissableViewAtPosition(paramSuggestionGridLayout, paramMotionEvent);
    }
    return false;
  }
  
  public boolean isExpanded()
  {
    return this.mExpanded;
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    this.mCardView.layout(0, 0, this.mCardView.getMeasuredWidth(), this.mCardView.getMeasuredHeight());
    if (this.mExpanded)
    {
      if (!getSettingsOverdrawRect(this.mTmpRect)) {
        this.mTmpRect.setEmpty();
      }
      this.mSettingsView.layout(0 - this.mTmpRect.left, 0 - this.mTmpRect.top, this.mSettingsView.getMeasuredWidth() - this.mTmpRect.left, this.mSettingsView.getMeasuredHeight() - this.mTmpRect.top);
    }
    int i;
    if (this.mMenuButton != null)
    {
      getBackgroundRect(this.mCardView, this.mTmpCardRect);
      i = this.mTmpCardRect.top;
      if (LayoutUtils.isLayoutRtl(this)) {
        this.mMenuButton.layout(0, i, this.mMenuButton.getMeasuredWidth(), i + this.mMenuButton.getMeasuredHeight());
      }
    }
    else
    {
      return;
    }
    this.mMenuButton.layout(getMeasuredWidth() - this.mMenuButton.getMeasuredWidth(), i, getMeasuredWidth(), i + this.mMenuButton.getMeasuredHeight());
  }
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    if (!isSettingsViewVisible())
    {
      this.mCardView.measure(paramInt1, paramInt2);
      setMeasuredDimension(this.mCardView.getMeasuredWidth() + getPaddingLeft() + getPaddingRight(), this.mCardView.getMeasuredHeight() + getPaddingTop() + getPaddingBottom());
      return;
    }
    measureMenuButtonOverlay();
    int i = View.MeasureSpec.getMode(paramInt1);
    int j = View.MeasureSpec.getMode(paramInt2);
    int k = View.MeasureSpec.getSize(paramInt1);
    int m = View.MeasureSpec.getSize(paramInt2);
    int n = this.mForcedCardHeight;
    int i1;
    int i2;
    int i4;
    label168:
    int i6;
    label279:
    int i8;
    int i9;
    if (n > 0)
    {
      i1 = View.MeasureSpec.makeMeasureSpec(n, 1073741824);
      this.mCardView.measure(paramInt1, i1);
      i2 = this.mCardView.getMeasuredWidth();
      int i3 = this.mCardView.getMeasuredHeight();
      i4 = 0;
      switch (i)
      {
      default: 
        int i5 = i2;
        if (getSettingsOverdrawRect(this.mTmpRect)) {
          i5 += this.mTmpRect.left + this.mTmpRect.right;
        }
        this.mSettingsView.measure(View.MeasureSpec.makeMeasureSpec(i5, 1073741824), View.MeasureSpec.makeMeasureSpec(0, 0));
        i6 = this.mTmpRect.top + this.mTmpRect.bottom;
        int i7 = i3 + i6;
        if (this.mSettingsView.getMeasuredHeight() < i7)
        {
          this.mSettingsView.measure(View.MeasureSpec.makeMeasureSpec(i5, 1073741824), View.MeasureSpec.makeMeasureSpec(i7, 1073741824));
          i8 = this.mSettingsView.getMeasuredHeight();
          i9 = 0;
          switch (j)
          {
          }
        }
        break;
      }
    }
    for (;;)
    {
      setMeasuredDimension(i4, i9);
      return;
      i1 = View.MeasureSpec.makeMeasureSpec(0, 0);
      break;
      i4 = k;
      break label168;
      i4 = i2 + (getPaddingLeft() + getPaddingRight());
      break label168;
      this.mCardView.measure(paramInt1, View.MeasureSpec.makeMeasureSpec(this.mSettingsView.getMeasuredHeight() - i6, 1073741824));
      break label279;
      i9 = m;
      continue;
      i9 = i8 + (getPaddingTop() + getPaddingBottom()) - i6;
    }
  }
  
  protected void onRestoreInstanceState(Parcelable paramParcelable)
  {
    Bundle localBundle1 = (Bundle)paramParcelable;
    super.onRestoreInstanceState(localBundle1.getParcelable("parent_state"));
    this.mCardView.restoreHierarchyState(localBundle1.getSparseParcelableArray("card_state"));
    this.mStackOrderOverride = localBundle1.getInt("stack_order_override", -1);
    Bundle localBundle2 = localBundle1.getBundle("settings_state");
    if (localBundle2 != null)
    {
      if (this.mSettingsView != null) {
        this.mBackOfCardAdapter.restoreViewState(localBundle2);
      }
    }
    else {
      return;
    }
    this.mPendingSettingsViewState = localBundle2;
  }
  
  protected Parcelable onSaveInstanceState()
  {
    Bundle localBundle1 = new Bundle();
    localBundle1.putParcelable("parent_state", super.onSaveInstanceState());
    SparseArray localSparseArray = new SparseArray();
    this.mCardView.saveHierarchyState(localSparseArray);
    localBundle1.putSparseParcelableArray("card_state", localSparseArray);
    if (this.mStackOrderOverride != -1) {
      localBundle1.putInt("stack_order_override", this.mStackOrderOverride);
    }
    if (this.mBackOfCardAdapter != null)
    {
      Bundle localBundle2 = this.mBackOfCardAdapter.saveViewState();
      if (localBundle2 != null) {
        localBundle1.putBundle("settings_state", localBundle2);
      }
    }
    return localBundle1;
  }
  
  protected void onSizeChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super.onSizeChanged(paramInt1, paramInt2, paramInt3, paramInt4);
    this.mCurrentHeight = paramInt2;
  }
  
  public void replaceCardView(View paramView)
  {
    int i = indexOfChild(this.mCardView);
    if (i == -1) {
      addView(paramView);
    }
    for (;;)
    {
      this.mCardView = paramView;
      this.mCardView.setTag(2131296280, this.mEntryAdapter.getEntry());
      return;
      removeView(this.mCardView);
      addView(paramView, i);
    }
  }
  
  public void setAllowedSwipeDirections(boolean paramBoolean1, boolean paramBoolean2)
  {
    if ((this.mCardView instanceof SuggestionGridLayout.DismissableChildContainer)) {
      ((SuggestionGridLayout.DismissableChildContainer)this.mCardView).setAllowedSwipeDirections(paramBoolean1, paramBoolean2);
    }
  }
  
  public void setCardView(View paramView, EntryCardViewAdapter paramEntryCardViewAdapter)
  {
    this.mCardView = paramView;
    this.mEntryAdapter = paramEntryCardViewAdapter;
    this.mCardView.setTag(2131296280, paramEntryCardViewAdapter.getEntry());
    ViewGroup.LayoutParams localLayoutParams = paramView.getLayoutParams();
    if (localLayoutParams != null) {
      setLayoutParams(localLayoutParams);
    }
    addView(paramView);
  }
  
  public void setForcedCardHeight(int paramInt)
  {
    if (!this.mExpanded) {
      this.mForcedCardHeight = paramInt;
    }
  }
  
  public void setStackOrderOverride(int paramInt)
  {
    this.mStackOrderOverride = paramInt;
  }
  
  public void showSettingsView(PredictiveCardContainer paramPredictiveCardContainer, View paramView, BackOfCardAdapter paramBackOfCardAdapter)
  {
    if (this.mSettingsView != null) {
      return;
    }
    this.mSettingsView = paramView;
    this.mBackOfCardAdapter = paramBackOfCardAdapter;
    this.mExpanded = true;
    addView(paramView);
    addMenuButtonOverlay(paramPredictiveCardContainer);
    maybeRestoreSettingsViewState();
    this.mBackOfCardAdapter.onResume();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.ui.PredictiveCardWrapper
 * JD-Core Version:    0.7.0.1
 */