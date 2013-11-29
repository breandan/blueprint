package com.google.android.search.core.suggest;

import android.database.DataSetObserver;
import com.google.android.search.shared.api.Query;
import com.google.android.shared.util.ExtraPreconditions;
import com.google.android.shared.util.ScheduledSingleThreadedExecutor;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;

public class SuggestionsController
{
  public static final Object CORRECTION_SUGGESTIONS = new Object();
  private static final SuggestionList NO_SUGGESTIONS = new SuggestionListImpl("", Query.EMPTY);
  public static final Object SUMMONS;
  public static final Object WEB_SUGGESTIONS = new Object();
  private Runnable mDelayTimerTask;
  private long mMaxDisplayDelayMillis;
  private boolean mStarted;
  private Suggestions mSuggestions;
  private final DataSetObserver mSuggestionsObserver = new DataSetObserver()
  {
    public void onChanged()
    {
      ExtraPreconditions.checkMainThread();
      SuggestionsController.this.onSuggestionsChanged();
    }
  };
  private final ScheduledSingleThreadedExecutor mUiExecutor;
  private final List<Listener> mUpdateListeners;
  private final Map<Object, ViewState> mViews;
  private boolean mWebSuggestionsEnabled;
  
  static
  {
    SUMMONS = new Object();
  }
  
  public SuggestionsController(ScheduledSingleThreadedExecutor paramScheduledSingleThreadedExecutor)
  {
    this.mUiExecutor = paramScheduledSingleThreadedExecutor;
    this.mUpdateListeners = Lists.newArrayList();
    this.mViews = Maps.newHashMap();
    this.mWebSuggestionsEnabled = true;
  }
  
  private ViewState getViewState(Object paramObject)
  {
    ViewState localViewState = (ViewState)this.mViews.get(paramObject);
    if (localViewState == null)
    {
      localViewState = new ViewState(paramObject.equals(WEB_SUGGESTIONS));
      this.mViews.put(paramObject, localViewState);
    }
    return localViewState;
  }
  
  private boolean isDelayingNewSuggestions()
  {
    return this.mDelayTimerTask != null;
  }
  
  private void onSuggestionsChanged()
  {
    Iterator localIterator = this.mViews.values().iterator();
    while (localIterator.hasNext())
    {
      ViewState localViewState = (ViewState)localIterator.next();
      if (localViewState.mPromoter != null) {
        localViewState.mPromoter.notifyChanged();
      }
    }
    updateViews();
  }
  
  private boolean shouldPublish(Suggestions paramSuggestions, SuggestionList paramSuggestionList)
  {
    return (!paramSuggestions.isClosed()) && ((paramSuggestionList.isFinal()) || ((!isDelayingNewSuggestions()) && (paramSuggestionList.getCount() > 0)));
  }
  
  private void showSuggestions(ViewState paramViewState, SuggestionList paramSuggestionList, boolean paramBoolean)
  {
    if ((paramViewState.mIsWebSuggest) && (!this.mWebSuggestionsEnabled)) {
      paramBoolean = false;
    }
    ViewState.access$102(paramViewState, paramSuggestionList);
    int i = Math.min(paramSuggestionList.getCount(), paramViewState.mMaxDisplayed);
    paramViewState.mUi.showSuggestions(paramSuggestionList, i, paramBoolean);
  }
  
  private void startDelayTimer(final Suggestions paramSuggestions)
  {
    stopDelayTimer();
    this.mDelayTimerTask = new Runnable()
    {
      public void run()
      {
        SuggestionsController.this.delayTimerExpired(paramSuggestions);
      }
    };
    this.mUiExecutor.executeDelayed(this.mDelayTimerTask, this.mMaxDisplayDelayMillis);
  }
  
  private void stopDelayTimer()
  {
    if (isDelayingNewSuggestions())
    {
      this.mUiExecutor.cancelExecute(this.mDelayTimerTask);
      this.mDelayTimerTask = null;
    }
  }
  
  private void updateListeners()
  {
    Iterator localIterator = this.mUpdateListeners.iterator();
    while (localIterator.hasNext()) {
      ((Listener)localIterator.next()).onSuggestionsUpdated(this, this.mSuggestions);
    }
  }
  
  private void updateView(ViewState paramViewState)
  {
    Preconditions.checkState(this.mStarted, "updateView when not started");
    if (paramViewState.mEnabled)
    {
      SuggestionList localSuggestionList1 = paramViewState.mPromoter.getPromoted();
      if (shouldPublish(this.mSuggestions, localSuggestionList1)) {
        showSuggestions(paramViewState, localSuggestionList1, true);
      }
      SuggestionList localSuggestionList2;
      do
      {
        return;
        localSuggestionList2 = paramViewState.mShownSuggestions;
      } while ((localSuggestionList2 == null) || (localSuggestionList2.getCount() <= 0));
      showSuggestions(paramViewState, localSuggestionList2, false);
      return;
    }
    showSuggestions(paramViewState, NO_SUGGESTIONS, false);
  }
  
  private void updateViews()
  {
    if ((!this.mSuggestions.isDone()) && (this.mMaxDisplayDelayMillis > 0L)) {
      startDelayTimer(this.mSuggestions);
    }
    updateViewsNow();
  }
  
  private void updateViewsNow()
  {
    Preconditions.checkState(this.mStarted, "updateViews when not started");
    Iterator localIterator = this.mViews.values().iterator();
    while (localIterator.hasNext())
    {
      ViewState localViewState = (ViewState)localIterator.next();
      if (localViewState.mPromoter != null) {
        updateView(localViewState);
      }
    }
    updateListeners();
  }
  
  public void addListener(Listener paramListener)
  {
    this.mUpdateListeners.add(paramListener);
    paramListener.onSuggestionsUpdated(this, this.mSuggestions);
  }
  
  public void addSuggestionsView(Object paramObject, CachingPromoter paramCachingPromoter, SuggestionsUi paramSuggestionsUi)
  {
    ViewState localViewState = getViewState(paramObject);
    if (localViewState.mPromoter == null) {}
    for (boolean bool = true;; bool = false)
    {
      Preconditions.checkState(bool, "Can't reset a suggestions view");
      ViewState.access$202(localViewState, paramCachingPromoter);
      ViewState.access$302(localViewState, paramSuggestionsUi);
      if (this.mSuggestions != null)
      {
        paramCachingPromoter.setSuggestions(this.mSuggestions);
        if (this.mStarted) {
          updateView(localViewState);
        }
      }
      return;
    }
  }
  
  void delayTimerExpired(Suggestions paramSuggestions)
  {
    if (paramSuggestions == this.mSuggestions)
    {
      this.mDelayTimerTask = null;
      updateViewsNow();
    }
  }
  
  public int getFetchState(Object paramObject)
  {
    if (this.mSuggestions != null)
    {
      ViewState localViewState = (ViewState)this.mViews.get(paramObject);
      if ((localViewState == null) || (!localViewState.mEnabled) || (localViewState.mPromoter == null)) {}
      do
      {
        return 1;
        if (!localViewState.showingCurrentResults()) {
          break;
        }
        if (localViewState.mShownSuggestions.getCount() > 0) {
          return 3;
        }
        if (!localViewState.showingFinalResults()) {
          break;
        }
      } while (((paramObject == WEB_SUGGESTIONS) && (this.mSuggestions.getWebSource() == null)) || ((paramObject == SUMMONS) && (!this.mSuggestions.isFetchingSummons())));
      return 2;
    }
    return 0;
  }
  
  public int getSummonsFetchState()
  {
    if (this.mSuggestions != null)
    {
      if (!this.mSuggestions.isFetchingSummons()) {
        return 1;
      }
      if (this.mSuggestions.getSummonsCount() > 0) {
        return 3;
      }
      if ((this.mSuggestions.areSummonsDone()) && (this.mSuggestions.getSummonsCount() == 0)) {
        return 2;
      }
    }
    return 0;
  }
  
  public void removeListener(Listener paramListener)
  {
    this.mUpdateListeners.remove(paramListener);
  }
  
  public void removeSuggestionsView(Object paramObject)
  {
    this.mViews.remove(paramObject);
  }
  
  public void setMaxDisplayDelayMillis(long paramLong)
  {
    this.mMaxDisplayDelayMillis = paramLong;
  }
  
  public void setMaxDisplayed(Object paramObject, int paramInt)
  {
    ViewState localViewState = getViewState(paramObject);
    if (localViewState.mMaxDisplayed != paramInt)
    {
      ViewState.access$502(localViewState, paramInt);
      if ((localViewState.mUi != null) && (this.mSuggestions != null) && (this.mStarted)) {
        updateView(localViewState);
      }
    }
  }
  
  public void setSuggestions(@Nonnull Suggestions paramSuggestions)
  {
    
    if (paramSuggestions == this.mSuggestions) {}
    do
    {
      return;
      if (this.mSuggestions != null) {
        this.mSuggestions.unregisterDataSetObserver(this.mSuggestionsObserver);
      }
      Iterator localIterator = this.mViews.values().iterator();
      while (localIterator.hasNext())
      {
        ViewState localViewState = (ViewState)localIterator.next();
        if (localViewState.mPromoter != null) {
          localViewState.mPromoter.setSuggestions(paramSuggestions);
        }
      }
      this.mSuggestions = paramSuggestions;
    } while (!this.mStarted);
    this.mSuggestions.registerDataSetObserver(this.mSuggestionsObserver);
    updateViews();
  }
  
  public void setSuggestionsViewEnabled(Object paramObject, boolean paramBoolean)
  {
    ViewState localViewState = getViewState(paramObject);
    ViewState.access$402(localViewState, paramBoolean);
    if ((localViewState.mUi != null) && (this.mSuggestions != null) && (this.mStarted)) {
      updateView(localViewState);
    }
  }
  
  public void setWebSuggestionsEnabled(boolean paramBoolean)
  {
    if (paramBoolean != this.mWebSuggestionsEnabled)
    {
      this.mWebSuggestionsEnabled = paramBoolean;
      ViewState localViewState = getViewState(WEB_SUGGESTIONS);
      SuggestionList localSuggestionList = localViewState.mShownSuggestions;
      if ((localSuggestionList != null) && (localSuggestionList.getCount() > 0) && (this.mStarted)) {
        updateView(localViewState);
      }
    }
  }
  
  public void start()
  {
    if (!this.mStarted)
    {
      this.mStarted = true;
      if (this.mSuggestions != null)
      {
        this.mSuggestions.registerDataSetObserver(this.mSuggestionsObserver);
        updateViews();
      }
    }
  }
  
  public void stop()
  {
    if (this.mStarted)
    {
      this.mStarted = false;
      if (this.mSuggestions != null) {
        this.mSuggestions.unregisterDataSetObserver(this.mSuggestionsObserver);
      }
      stopDelayTimer();
      this.mSuggestions = null;
      Iterator localIterator = this.mViews.values().iterator();
      while (localIterator.hasNext()) {
        ViewState.access$102((ViewState)localIterator.next(), null);
      }
    }
  }
  
  public static abstract interface Listener
  {
    public abstract void onSuggestionsUpdated(SuggestionsController paramSuggestionsController, Suggestions paramSuggestions);
  }
  
  private class ViewState
  {
    private boolean mEnabled;
    private final boolean mIsWebSuggest;
    private int mMaxDisplayed;
    private CachingPromoter mPromoter;
    private SuggestionList mShownSuggestions;
    private SuggestionsUi mUi;
    
    ViewState(boolean paramBoolean)
    {
      this.mIsWebSuggest = paramBoolean;
      this.mEnabled = true;
      this.mMaxDisplayed = 2147483647;
    }
    
    boolean showingCurrentResults()
    {
      return (this.mShownSuggestions != null) && (this.mShownSuggestions.getSourceSuggestions() == SuggestionsController.this.mSuggestions);
    }
    
    boolean showingFinalResults()
    {
      return (showingCurrentResults()) && (this.mShownSuggestions.isFinal());
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.suggest.SuggestionsController
 * JD-Core Version:    0.7.0.1
 */