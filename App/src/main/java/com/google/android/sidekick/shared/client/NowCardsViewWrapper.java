package com.google.android.sidekick.shared.client;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.AnimatorSet.Builder;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewPropertyAnimator;
import com.google.android.search.shared.ui.BakedBezierInterpolator;
import com.google.android.search.shared.ui.PendingViewDismiss;
import com.google.android.search.shared.ui.PendingViewDismiss.Observer;
import com.google.android.search.shared.ui.SuggestionGridLayout;
import com.google.android.search.shared.ui.SuggestionGridLayout.LayoutParams;
import com.google.android.shared.ui.ScrollViewControl;
import com.google.android.shared.ui.ScrollViewControl.ScrollListener;
import com.google.android.shared.util.ExtraPreconditions;
import com.google.android.shared.util.ScheduledSingleThreadedExecutor;
import com.google.android.sidekick.shared.cards.EntryCardViewAdapter;
import com.google.android.sidekick.shared.cards.LoadMoreCardsAdapter;
import com.google.android.sidekick.shared.remoteapi.CardRenderingContext;
import com.google.android.sidekick.shared.training.BackOfCardAdapter;
import com.google.android.sidekick.shared.ui.CardBackTraining;
import com.google.android.sidekick.shared.ui.PredictiveCardWrapper;
import com.google.android.sidekick.shared.util.ProtoKey;
import com.google.android.sidekick.shared.util.ProtoUtils;
import com.google.android.sidekick.shared.util.Tag;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.google.geo.sidekick.Sidekick.Entry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;

public class NowCardsViewWrapper
  implements PendingViewDismiss.Observer
{
  private static final String TAG = Tag.getTag(NowCardsViewWrapper.class);
  private int mBottomInset;
  private final PredictiveCardContainer mCardContainer;
  @Nullable
  private final CardsObserver mCardsObserver;
  private PredictiveCardWrapper mCurrentTrainingModeCard;
  private final SuggestionGridLayout mLayout;
  @Nullable
  private final View mRemindersFooterIcon;
  @Nullable
  private final View mRemindersPeekView;
  private CardViewCreator mRunningCardViewCreator;
  private final ScrollViewControl.ScrollListener mScrollListener = new ScrollViewControl.ScrollListener()
  {
    public void onOverscroll(int paramAnonymousInt) {}
    
    public void onOverscrollFinished() {}
    
    public void onOverscrollStarted() {}
    
    public void onScrollAnimationFinished()
    {
      NowCardsViewWrapper.this.checkTrainingCardsVisibility();
    }
    
    public void onScrollChanged(int paramAnonymousInt1, int paramAnonymousInt2) {}
    
    public void onScrollFinished()
    {
      NowCardsViewWrapper.this.checkTrainingCardsVisibility();
    }
    
    public void onScrollMarginConsumed(View paramAnonymousView, int paramAnonymousInt1, int paramAnonymousInt2) {}
  };
  private final ScrollViewControl mScrollView;
  private final View mTrainingFooterIcon;
  private final View mTrainingPeekIcon;
  private final View mTrainingPeekView;
  private final ScheduledSingleThreadedExecutor mUiThread;
  
  public NowCardsViewWrapper(ScheduledSingleThreadedExecutor paramScheduledSingleThreadedExecutor, @Nullable CardsObserver paramCardsObserver, SuggestionGridLayout paramSuggestionGridLayout, ScrollViewControl paramScrollViewControl, View paramView1, View paramView2, @Nullable View paramView3, View paramView4, @Nullable View paramView5, PredictiveCardContainer paramPredictiveCardContainer)
  {
    this.mUiThread = paramScheduledSingleThreadedExecutor;
    this.mCardsObserver = paramCardsObserver;
    this.mLayout = paramSuggestionGridLayout;
    this.mScrollView = paramScrollViewControl;
    this.mTrainingPeekView = paramView1;
    this.mTrainingPeekIcon = paramView2;
    this.mTrainingFooterIcon = paramView4;
    this.mCardContainer = paramPredictiveCardContainer;
    this.mRemindersPeekView = paramView3;
    this.mRemindersFooterIcon = paramView5;
  }
  
  private static boolean applyUpdate(Sidekick.Entry paramEntry1, Sidekick.Entry paramEntry2, ProtoKey<Sidekick.Entry> paramProtoKey)
  {
    if (paramEntry2.hasEntryUpdateId()) {
      if (paramEntry1.getEntryUpdateId() != paramEntry2.getEntryUpdateId()) {}
    }
    while ((paramEntry1.getType() == paramEntry2.getType()) && (paramProtoKey.equals(new ProtoKey(paramEntry1))))
    {
      return true;
      return false;
    }
    return false;
  }
  
  private void disableDismiss(View paramView)
  {
    if ((paramView.getLayoutParams() != null) && ((paramView.getLayoutParams() instanceof SuggestionGridLayout.LayoutParams))) {}
    for (SuggestionGridLayout.LayoutParams localLayoutParams = (SuggestionGridLayout.LayoutParams)paramView.getLayoutParams();; localLayoutParams = (SuggestionGridLayout.LayoutParams)this.mLayout.generateDefaultLayoutParams())
    {
      localLayoutParams.canDismiss = false;
      localLayoutParams.canDrag = false;
      paramView.setLayoutParams(localLayoutParams);
      return;
    }
  }
  
  private ViewPropertyAnimator fadeInView(View paramView, int paramInt)
  {
    paramView.setAlpha(0.0F);
    paramView.setVisibility(0);
    return paramView.animate().alpha(1.0F).setStartDelay(paramInt).setDuration(250L).setInterpolator(BakedBezierInterpolator.INSTANCE).withLayer();
  }
  
  private void fadeOutView(final View paramView)
  {
    paramView.animate().alpha(0.0F).setStartDelay(0L).setDuration(250L).setInterpolator(BakedBezierInterpolator.INSTANCE).withLayer().withEndAction(new Runnable()
    {
      public void run()
      {
        paramView.setVisibility(4);
      }
    });
  }
  
  private PredictiveCardWrapper findCardForAdapter(EntryCardViewAdapter paramEntryCardViewAdapter)
  {
    for (int i = 0; i < this.mLayout.getChildCount(); i++)
    {
      View localView = this.mLayout.getChildAt(i);
      if ((localView instanceof PredictiveCardWrapper))
      {
        PredictiveCardWrapper localPredictiveCardWrapper = (PredictiveCardWrapper)localView;
        if (paramEntryCardViewAdapter.equals(localPredictiveCardWrapper.getEntryCardViewAdapter())) {
          return localPredictiveCardWrapper;
        }
      }
    }
    return null;
  }
  
  private PredictiveCardWrapper findCardForEntry(Sidekick.Entry paramEntry)
  {
    ProtoKey localProtoKey = new ProtoKey(paramEntry);
    for (int i = 0; i < this.mLayout.getChildCount(); i++)
    {
      View localView = this.mLayout.getChildAt(i);
      if ((localView instanceof PredictiveCardWrapper))
      {
        PredictiveCardWrapper localPredictiveCardWrapper = (PredictiveCardWrapper)localView;
        if (localProtoKey.equals(new ProtoKey(localPredictiveCardWrapper.getEntryCardViewAdapter().getEntry()))) {
          return localPredictiveCardWrapper;
        }
      }
    }
    return null;
  }
  
  private PredictiveCardWrapper findCardToScrollTo(Sidekick.Entry paramEntry)
  {
    ProtoKey localProtoKey = new ProtoKey(paramEntry);
    Object localObject = null;
    for (int i = 0; i < this.mLayout.getChildCount(); i++)
    {
      View localView = this.mLayout.getChildAt(i);
      if ((localView instanceof PredictiveCardWrapper))
      {
        PredictiveCardWrapper localPredictiveCardWrapper = (PredictiveCardWrapper)localView;
        Sidekick.Entry localEntry = localPredictiveCardWrapper.getEntryCardViewAdapter().getEntry();
        if (localProtoKey.equals(new ProtoKey(localEntry))) {
          return localPredictiveCardWrapper;
        }
        if ((paramEntry.getType() == localEntry.getType()) && (localObject == null)) {
          localObject = localPredictiveCardWrapper;
        }
      }
    }
    return localObject;
  }
  
  private void flashPeekView(final View paramView1, final View paramView2)
  {
    showPeekView(paramView1, paramView2).withEndAction(new Runnable()
    {
      public void run()
      {
        NowCardsViewWrapper.this.hidePeekView(paramView1, paramView2, 750);
      }
    });
  }
  
  @Nullable
  private EntryCardViewAdapter getCardViewAdapter(View paramView)
  {
    if ((paramView instanceof PredictiveCardWrapper)) {
      return ((PredictiveCardWrapper)paramView).getEntryCardViewAdapter();
    }
    return null;
  }
  
  private float getPeekIconTranslation(View paramView)
  {
    return 0.25F * paramView.getResources().getDimensionPixelSize(2131689857) - this.mBottomInset;
  }
  
  private ViewPropertyAnimator hidePeekView(final View paramView1, View paramView2, int paramInt)
  {
    fadeInView(paramView2, paramInt);
    int i = paramView1.getResources().getDimensionPixelSize(2131689861);
    float f = getPeekIconTranslation(paramView1) + i;
    paramView1.animate().alpha(0.0F).setStartDelay(paramInt).scaleX(0.5F).scaleY(0.5F).translationY(f).setDuration(250L).setInterpolator(BakedBezierInterpolator.INSTANCE).withLayer().withEndAction(new Runnable()
    {
      public void run()
      {
        paramView1.setVisibility(8);
      }
    });
  }
  
  private void pulseView(View paramView)
  {
    ObjectAnimator localObjectAnimator1 = ObjectAnimator.ofFloat(paramView, "scaleX", new float[] { 1.5F });
    ObjectAnimator localObjectAnimator2 = ObjectAnimator.ofFloat(paramView, "scaleY", new float[] { 1.5F });
    AnimatorSet localAnimatorSet1 = new AnimatorSet();
    localAnimatorSet1.setDuration(150L);
    localAnimatorSet1.play(localObjectAnimator1).with(localObjectAnimator2);
    ObjectAnimator localObjectAnimator3 = ObjectAnimator.ofFloat(paramView, "scaleX", new float[] { 1.0F });
    ObjectAnimator localObjectAnimator4 = ObjectAnimator.ofFloat(paramView, "scaleY", new float[] { 1.0F });
    AnimatorSet localAnimatorSet2 = new AnimatorSet();
    localAnimatorSet1.setDuration(150L);
    localAnimatorSet2.play(localObjectAnimator3).with(localObjectAnimator4).after(50L);
    AnimatorSet localAnimatorSet3 = new AnimatorSet();
    localAnimatorSet3.playSequentially(new Animator[] { localAnimatorSet1, localAnimatorSet2 });
    localAnimatorSet3.start();
  }
  
  private ViewPropertyAnimator showPeekView(View paramView1, View paramView2)
  {
    fadeOutView(paramView2);
    paramView1.setLayerType(2, null);
    paramView1.setAlpha(0.0F);
    paramView1.setVisibility(0);
    paramView1.setScaleX(0.5F);
    paramView1.setScaleY(0.5F);
    int i = paramView1.getResources().getDimensionPixelSize(2131689861);
    float f = getPeekIconTranslation(paramView1);
    paramView1.setTranslationY(f + i);
    return paramView1.animate().alpha(1.0F).setStartDelay(0L).scaleX(1.0F).scaleY(1.0F).translationY(f).setDuration(250L).setInterpolator(BakedBezierInterpolator.INSTANCE).withLayer();
  }
  
  private static boolean viewSpansAllColumns(View paramView)
  {
    return (paramView.getLayoutParams() != null) && ((paramView.getLayoutParams() instanceof SuggestionGridLayout.LayoutParams)) && (((SuggestionGridLayout.LayoutParams)paramView.getLayoutParams()).column == -1);
  }
  
  void addCardViews(int paramInt1, View[] paramArrayOfView, ArrayList<View>[] paramArrayOfArrayList, @Nullable List<View> paramList, int paramInt2, @Nullable Sidekick.Entry paramEntry, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, CardViewCreator paramCardViewCreator)
  {
    
    if (this.mRunningCardViewCreator != paramCardViewCreator) {}
    for (;;)
    {
      return;
      this.mRunningCardViewCreator = null;
      int i = this.mLayout.getColumnCount();
      int j = 0;
      if (this.mCurrentTrainingModeCard != null) {
        toggleBackOfCard(this.mCurrentTrainingModeCard.getEntryCardViewAdapter(), false, null);
      }
      View localView3;
      int k;
      if (paramBoolean2)
      {
        removeAllPredictiveCards();
        localView3 = null;
        k = 0;
        label65:
        if (k >= paramInt1) {
          break label318;
        }
        if (paramArrayOfView[k] == null) {
          break label227;
        }
        if (!viewSpansAllColumns(paramArrayOfView[k])) {
          break label202;
        }
        this.mLayout.addView(paramArrayOfView[k]);
        label99:
        if (localView3 == null) {
          localView3 = paramArrayOfView[k];
        }
        if (!paramBoolean3) {
          disableDismiss(paramArrayOfView[k]);
        }
      }
      for (;;)
      {
        k++;
        break label65;
        View localView1 = getLoadingCardView();
        if (localView1 != null) {
          this.mLayout.removeView(localView1);
        }
        if (paramList == null) {
          break;
        }
        Iterator localIterator1 = paramList.iterator();
        while (localIterator1.hasNext())
        {
          View localView2 = (View)localIterator1.next();
          if (localView2 != null) {
            this.mLayout.removeView(localView2);
          }
        }
        break;
        label202:
        this.mLayout.addViewToColumn(paramArrayOfView[k], j);
        j = (j + 1) % i;
        break label99;
        label227:
        if (paramArrayOfArrayList[k] != null)
        {
          ArrayList<View> localArrayList = paramArrayOfArrayList[k];
          this.mLayout.addStackToColumn(localArrayList, j);
          if (localView3 == null) {
            localView3 = (View)localArrayList.get(0);
          }
          if (!paramBoolean3)
          {
            Iterator localIterator2 = localArrayList.iterator();
            while (localIterator2.hasNext()) {
              disableDismiss((View)localIterator2.next());
            }
          }
          j = (j + 1) % i;
        }
      }
      label318:
      if (paramInt2 != -1) {
        this.mScrollView.smoothScrollToY(paramInt2);
      }
      while (this.mCardsObserver != null)
      {
        this.mCardsObserver.onCardsAdded();
        return;
        if (paramEntry != null)
        {
          PredictiveCardWrapper localPredictiveCardWrapper = findCardToScrollTo(paramEntry);
          if (localPredictiveCardWrapper != null)
          {
            int n = this.mLayout.getContext().getResources().getDimensionPixelSize(2131689718);
            this.mScrollView.scrollToView(localPredictiveCardWrapper, n);
          }
        }
        else if ((paramBoolean1) && (localView3 != null))
        {
          int m = this.mLayout.getContext().getResources().getDimensionPixelSize(2131689718);
          this.mScrollView.scrollToView(localView3, m);
        }
      }
    }
  }
  
  public void addCards(Activity paramActivity, List<EntryItemStack> paramList, CardRenderingContext paramCardRenderingContext, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, @Nullable Map<Long, Bundle> paramMap, @Nullable List<View> paramList1, @Nullable Sidekick.Entry paramEntry, int paramInt)
  {
    
    if (this.mRunningCardViewCreator != null) {
      this.mRunningCardViewCreator.cancel();
    }
    this.mCardContainer.setCardRenderingContext(paramCardRenderingContext);
    this.mRunningCardViewCreator = new CardViewCreator(paramActivity, LayoutInflater.from(this.mLayout.getContext()), this.mUiThread, this, this.mCardContainer, paramList, paramBoolean1, paramBoolean2, paramBoolean3, paramMap, paramList1, paramEntry, paramInt);
    this.mUiThread.execute(this.mRunningCardViewCreator);
  }
  
  protected void checkTrainingCardsVisibility()
  {
    View localView;
    if (this.mCurrentTrainingModeCard != null)
    {
      localView = this.mCurrentTrainingModeCard.getSettingsView();
      if (localView != null) {
        break label20;
      }
    }
    label20:
    int i;
    int j;
    int k;
    do
    {
      return;
      i = this.mScrollView.getDescendantTop(localView);
      j = i + localView.getHeight();
      k = this.mScrollView.getScrollY();
    } while ((i <= k + this.mScrollView.getViewportHeight()) && (j >= k));
    toggleBackOfCard(this.mCurrentTrainingModeCard.getEntryCardViewAdapter(), false, null);
  }
  
  public boolean commitAllFeedback(boolean paramBoolean)
  {
    if (this.mCurrentTrainingModeCard != null)
    {
      toggleBackOfCard(this.mCurrentTrainingModeCard.getEntryCardViewAdapter(), paramBoolean, null);
      return true;
    }
    return false;
  }
  
  public boolean commitFeedbackFromViews(Iterable<View> paramIterable)
  {
    Iterator localIterator = paramIterable.iterator();
    while (localIterator.hasNext()) {
      if ((View)localIterator.next() == this.mCurrentTrainingModeCard)
      {
        toggleBackOfCard(this.mCurrentTrainingModeCard.getEntryCardViewAdapter(), true, null);
        return true;
      }
    }
    return false;
  }
  
  public View createCardSettingsForAdapter(BackOfCardAdapter paramBackOfCardAdapter)
  {
    Context localContext = this.mLayout.getContext();
    CardBackTraining localCardBackTraining = new CardBackTraining(localContext);
    paramBackOfCardAdapter.populateBackOfCard(localContext, this.mCardContainer, (ViewGroup)localCardBackTraining, LayoutInflater.from(localContext));
    return localCardBackTraining;
  }
  
  public PredictiveCardWrapper createPredictiveCardForAdapter(Activity paramActivity, LayoutInflater paramLayoutInflater, EntryCardViewAdapter paramEntryCardViewAdapter, ViewGroup paramViewGroup, PredictiveCardContainer paramPredictiveCardContainer)
  {
    View localView = paramEntryCardViewAdapter.getView(paramActivity, paramPredictiveCardContainer, paramLayoutInflater, paramViewGroup);
    paramEntryCardViewAdapter.registerBackOfCardMenuListener(paramActivity, paramPredictiveCardContainer, localView);
    paramEntryCardViewAdapter.registerActions(paramActivity, paramPredictiveCardContainer, localView);
    paramEntryCardViewAdapter.registerDetailsClickListener(paramPredictiveCardContainer, localView);
    paramEntryCardViewAdapter.maybeShowFeedbackPrompt(paramPredictiveCardContainer, (ViewGroup)localView, paramLayoutInflater);
    paramEntryCardViewAdapter.registerTouchListener(localView);
    PredictiveCardWrapper localPredictiveCardWrapper = new PredictiveCardWrapper(paramActivity);
    localPredictiveCardWrapper.setCardView(localView, paramEntryCardViewAdapter);
    return localPredictiveCardWrapper;
  }
  
  public void dismissEntry(Sidekick.Entry paramEntry, @Nullable Collection<Sidekick.Entry> paramCollection)
  {
    final PredictiveCardWrapper localPredictiveCardWrapper = findCardForEntry(paramEntry);
    if ((localPredictiveCardWrapper != null) && (localPredictiveCardWrapper.getVisibility() != 8))
    {
      if (paramCollection != null)
      {
        Iterator localIterator = paramCollection.iterator();
        while (localIterator.hasNext())
        {
          Sidekick.Entry localEntry = (Sidekick.Entry)localIterator.next();
          final View localView = localPredictiveCardWrapper.getEntryCardViewAdapter().findViewForChildEntry(localPredictiveCardWrapper.getCardView(), localEntry);
          if ((localView != null) && (localView.getVisibility() != 8))
          {
            final ViewParent localViewParent = localView.getParent();
            Animator localAnimator2 = AnimatorInflater.loadAnimator(this.mLayout.getContext(), 2131034112);
            localAnimator2.setStartDelay(0L);
            localAnimator2.setTarget(localView);
            localAnimator2.addListener(new AnimatorListenerAdapter()
            {
              public void onAnimationEnd(Animator paramAnonymousAnimator)
              {
                if ((localViewParent instanceof ViewGroup)) {
                  ((ViewGroup)localViewParent).removeView(localView);
                }
              }
            });
            localAnimator2.start();
          }
        }
      }
      Animator localAnimator1 = AnimatorInflater.loadAnimator(localPredictiveCardWrapper.getContext(), 2131034112);
      localAnimator1.setStartDelay(0L);
      localAnimator1.setTarget(localPredictiveCardWrapper);
      localAnimator1.addListener(new AnimatorListenerAdapter()
      {
        public void onAnimationEnd(Animator paramAnonymousAnimator)
        {
          NowCardsViewWrapper.this.mLayout.removeView(localPredictiveCardWrapper);
        }
      });
      localAnimator1.start();
    }
  }
  
  public void flashReminderIcon()
  {
    if ((this.mRemindersPeekView != null) && (this.mRemindersFooterIcon != null)) {
      flashPeekView(this.mRemindersPeekView, this.mRemindersFooterIcon);
    }
  }
  
  ViewGroup getLayout()
  {
    return this.mLayout;
  }
  
  @Nullable
  public View getLoadingCardView()
  {
    for (int i = 0; i < this.mLayout.getChildCount(); i++)
    {
      View localView = this.mLayout.getChildAt(i);
      if ((localView instanceof PredictiveCardWrapper))
      {
        PredictiveCardWrapper localPredictiveCardWrapper = (PredictiveCardWrapper)localView;
        if ((localPredictiveCardWrapper.getEntryCardViewAdapter() instanceof LoadMoreCardsAdapter)) {
          return localPredictiveCardWrapper;
        }
      }
    }
    return null;
  }
  
  @Nullable
  public Map<Long, Bundle> getPredictiveCardsState(@Nullable Bundle paramBundle)
  {
    if (paramBundle == null) {
      return null;
    }
    return (Map)paramBundle.getSerializable("card_state_map");
  }
  
  public void hideTrainingPeekViewIfVisible()
  {
    if (this.mTrainingPeekView.getVisibility() == 0) {
      hidePeekView(this.mTrainingPeekView, this.mTrainingFooterIcon, 0);
    }
  }
  
  public boolean isTrainingModeShowing()
  {
    return this.mCurrentTrainingModeCard != null;
  }
  
  public void notifyCardVisible(PredictiveCardWrapper paramPredictiveCardWrapper)
  {
    paramPredictiveCardWrapper.getEntryCardViewAdapter().onViewVisibleOnScreen(this.mCardContainer);
  }
  
  public void onCommit(PendingViewDismiss paramPendingViewDismiss)
  {
    Iterator localIterator = paramPendingViewDismiss.getDismissedViews().iterator();
    while (localIterator.hasNext())
    {
      EntryCardViewAdapter localEntryCardViewAdapter = getCardViewAdapter((View)localIterator.next());
      if (localEntryCardViewAdapter != null) {
        localEntryCardViewAdapter.onDismiss(this.mLayout.getContext(), this.mCardContainer);
      }
    }
  }
  
  public void onRestore(PendingViewDismiss paramPendingViewDismiss)
  {
    Iterator localIterator = paramPendingViewDismiss.getDismissedViews().iterator();
    while (localIterator.hasNext())
    {
      EntryCardViewAdapter localEntryCardViewAdapter = getCardViewAdapter((View)localIterator.next());
      if (localEntryCardViewAdapter != null)
      {
        Sidekick.Entry localEntry = localEntryCardViewAdapter.getDismissEntry();
        this.mCardContainer.cancelDismissEntryAction(localEntry);
      }
    }
  }
  
  public void onViewsDismissed(PendingViewDismiss paramPendingViewDismiss)
  {
    int i = 0;
    Sidekick.Entry localEntry = null;
    Iterator localIterator = paramPendingViewDismiss.getDismissedViews().iterator();
    while (localIterator.hasNext())
    {
      EntryCardViewAdapter localEntryCardViewAdapter = getCardViewAdapter((View)localIterator.next());
      if (localEntryCardViewAdapter != null)
      {
        i = 1;
        this.mCardContainer.queueDismissEntryAction(localEntryCardViewAdapter.getDismissEntry());
        if (paramPendingViewDismiss.getDismissedViews().size() == 1) {
          localEntry = localEntryCardViewAdapter.getDismissEntry();
        }
      }
    }
    if (i != 0)
    {
      paramPendingViewDismiss.addObserver(this);
      this.mCardContainer.getUndoDismissManager().showUndoToast(paramPendingViewDismiss, localEntry);
      this.mCardContainer.recordCardSwipedForDismiss();
    }
  }
  
  public void pulseTrainIcon()
  {
    pulseView(this.mTrainingPeekIcon);
  }
  
  public void registerListeners()
  {
    this.mScrollView.addScrollListener(this.mScrollListener);
  }
  
  public void removeAllPredictiveCards()
  {
    this.mLayout.removeViewsWithTag(2131296286);
  }
  
  public void removeCard(EntryCardViewAdapter paramEntryCardViewAdapter)
  {
    PredictiveCardWrapper localPredictiveCardWrapper = findCardForAdapter(paramEntryCardViewAdapter);
    if (localPredictiveCardWrapper != null) {
      this.mLayout.removeView(localPredictiveCardWrapper);
    }
  }
  
  public void savePredictiveCardsState(Bundle paramBundle)
  {
    HashMap localHashMap = Maps.newHashMap();
    for (int i = 0; i < this.mLayout.getChildCount(); i++)
    {
      View localView1 = this.mLayout.getChildAt(i);
      if ((localView1 instanceof PredictiveCardWrapper))
      {
        PredictiveCardWrapper localPredictiveCardWrapper = (PredictiveCardWrapper)localView1;
        Bundle localBundle = new Bundle();
        SparseArray localSparseArray = new SparseArray();
        localPredictiveCardWrapper.saveHierarchyState(localSparseArray);
        if (localSparseArray.size() > 0) {
          localBundle.putSparseParcelableArray("card:views", localSparseArray);
        }
        View localView2 = localPredictiveCardWrapper.findFocus();
        if ((localView2 != null) && (localView2.getId() != -1)) {
          localBundle.putInt("card:focusedViewId", localView2.getId());
        }
        if (localPredictiveCardWrapper.isExpanded()) {
          localBundle.putBoolean("card_expanded", true);
        }
        if (!localBundle.isEmpty()) {
          localHashMap.put(Long.valueOf(ProtoUtils.getEntryHash(localPredictiveCardWrapper.getEntryCardViewAdapter().getEntry())), localBundle);
        }
      }
    }
    paramBundle.putSerializable("card_state_map", localHashMap);
  }
  
  public void setCanDismiss(View paramView, boolean paramBoolean)
  {
    if ((paramView.getLayoutParams() instanceof SuggestionGridLayout.LayoutParams)) {
      ((SuggestionGridLayout.LayoutParams)paramView.getLayoutParams()).canDismiss = paramBoolean;
    }
  }
  
  public void setWindowInsets(Rect paramRect)
  {
    this.mCardContainer.setWindowInsets(paramRect);
    if (paramRect != null) {}
    for (int i = paramRect.bottom;; i = 0)
    {
      this.mBottomInset = i;
      return;
    }
  }
  
  public void showSinglePromoCard(Activity paramActivity, EntryCardViewAdapter paramEntryCardViewAdapter)
  {
    PredictiveCardWrapper localPredictiveCardWrapper = createPredictiveCardForAdapter(paramActivity, LayoutInflater.from(paramActivity), paramEntryCardViewAdapter, this.mLayout, this.mCardContainer);
    SuggestionGridLayout.LayoutParams localLayoutParams = new SuggestionGridLayout.LayoutParams(-1, -2, 0);
    localLayoutParams.canDismiss = false;
    localLayoutParams.canDrag = false;
    localLayoutParams.column = -1;
    localPredictiveCardWrapper.setLayoutParams(localLayoutParams);
    tagAsPredictiveView(localPredictiveCardWrapper);
    this.mLayout.addView(localPredictiveCardWrapper);
  }
  
  public void tagAsPredictiveView(View paramView)
  {
    paramView.setTag(2131296286, Boolean.TRUE);
  }
  
  public void toggleBackOfCard(EntryCardViewAdapter paramEntryCardViewAdapter, boolean paramBoolean, @Nullable SearchPlateSticker paramSearchPlateSticker)
  {
    PredictiveCardWrapper localPredictiveCardWrapper1 = this.mCurrentTrainingModeCard;
    boolean bool = false;
    int i = 0;
    if (localPredictiveCardWrapper1 != null)
    {
      bool = paramEntryCardViewAdapter.equals(this.mCurrentTrainingModeCard.getEntryCardViewAdapter());
      setCanDismiss(this.mCurrentTrainingModeCard, true);
      this.mCurrentTrainingModeCard.hideSettingsView(paramBoolean);
      BackOfCardAdapter localBackOfCardAdapter2 = this.mCurrentTrainingModeCard.getBackOfCardAdapter();
      if (localBackOfCardAdapter2 != null) {
        localBackOfCardAdapter2.commitFeedback(this.mLayout.getContext(), this.mCardContainer);
      }
      if (paramSearchPlateSticker != null) {
        paramSearchPlateSticker.setSearchPlateStuckToScrollingView(true);
      }
      this.mCurrentTrainingModeCard = null;
      i = 1;
    }
    int j = 0;
    int m;
    int n;
    int i3;
    int i4;
    int i5;
    int i6;
    int i7;
    int i8;
    if (!bool)
    {
      PredictiveCardWrapper localPredictiveCardWrapper2 = findCardForAdapter(paramEntryCardViewAdapter);
      j = 0;
      if (localPredictiveCardWrapper2 != null)
      {
        BackOfCardAdapter localBackOfCardAdapter1 = paramEntryCardViewAdapter.createBackOfCardAdapter(this.mUiThread);
        View localView = createCardSettingsForAdapter(localBackOfCardAdapter1);
        setCanDismiss(localPredictiveCardWrapper2, false);
        localPredictiveCardWrapper2.showSettingsView(this.mCardContainer, localView, localBackOfCardAdapter1);
        int k = this.mScrollView.getDescendantTop(localPredictiveCardWrapper2);
        m = localPredictiveCardWrapper2.getMeasuredWidth();
        if (m != 0) {
          break label357;
        }
        n = View.MeasureSpec.makeMeasureSpec(0, 0);
        int i1 = View.MeasureSpec.makeMeasureSpec(0, 0);
        localPredictiveCardWrapper2.measure(n, i1);
        int i2 = localPredictiveCardWrapper2.getMeasuredHeight();
        i3 = k + i2;
        i4 = this.mLayout.getContext().getResources().getDimensionPixelSize(2131689821);
        i5 = this.mScrollView.getScrollY();
        i6 = this.mScrollView.getViewportHeight();
        i7 = k - i4;
        i8 = -1;
        if (paramSearchPlateSticker != null) {
          break label370;
        }
        label277:
        if ((i2 <= i6 - i4 * 2) && (i7 >= i5)) {
          break label380;
        }
        i8 = i7;
        label300:
        if (i8 != -1)
        {
          if (paramSearchPlateSticker != null) {
            paramSearchPlateSticker.setSearchPlateStuckToScrollingView(true);
          }
          this.mScrollView.smoothScrollToYSyncWithTransition(i8, this.mLayout, 4);
        }
        this.mCurrentTrainingModeCard = localPredictiveCardWrapper2;
        j = 1;
      }
    }
    if ((i != 0) && (j == 0)) {
      hideTrainingPeekViewIfVisible();
    }
    label357:
    label370:
    label380:
    while ((i != 0) || (j == 0))
    {
      return;
      n = View.MeasureSpec.makeMeasureSpec(m, 1073741824);
      break;
      paramSearchPlateSticker.getSearchPlateHeight();
      break label277;
      if (i3 + i4 + this.mBottomInset <= i5 + i6) {
        break label300;
      }
      i8 = Math.min(i3 + i4 + this.mBottomInset - i6, i7);
      break label300;
    }
    showPeekView(this.mTrainingPeekView, this.mTrainingFooterIcon);
  }
  
  public void unregisterListeners()
  {
    this.mScrollView.removeScrollListener(this.mScrollListener);
  }
  
  public void updateEntry(Sidekick.Entry paramEntry1, Sidekick.Entry paramEntry2, Sidekick.Entry paramEntry3)
  {
    ProtoKey localProtoKey = new ProtoKey(paramEntry1);
    for (int i = -1 + this.mLayout.getChildCount(); i >= 0; i--)
    {
      View localView1 = this.mLayout.getChildAt(i);
      if ((localView1 instanceof PredictiveCardWrapper))
      {
        PredictiveCardWrapper localPredictiveCardWrapper = (PredictiveCardWrapper)localView1;
        EntryCardViewAdapter localEntryCardViewAdapter = localPredictiveCardWrapper.getEntryCardViewAdapter();
        if (applyUpdate(localEntryCardViewAdapter.getEntry(), paramEntry2, localProtoKey))
        {
          View localView2 = localPredictiveCardWrapper.getCardView();
          localEntryCardViewAdapter.replaceEntry(paramEntry2);
          Context localContext = this.mLayout.getContext();
          View localView3 = localEntryCardViewAdapter.updateView(localContext, this.mCardContainer, LayoutInflater.from(localContext), this.mLayout, localView2, paramEntry3);
          if (localView3 != localView2)
          {
            localEntryCardViewAdapter.registerBackOfCardMenuListener(localContext, this.mCardContainer, localView3);
            localPredictiveCardWrapper.replaceCardView(localView3);
          }
          if (localPredictiveCardWrapper.isExpanded()) {
            toggleBackOfCard(localEntryCardViewAdapter, true, null);
          }
        }
      }
    }
  }
  
  public static abstract interface CardsObserver
  {
    public abstract void onCardsAdded();
  }
  
  public static abstract interface SearchPlateSticker
  {
    public abstract int getSearchPlateHeight();
    
    public abstract void setSearchPlateStuckToScrollingView(boolean paramBoolean);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.client.NowCardsViewWrapper
 * JD-Core Version:    0.7.0.1
 */