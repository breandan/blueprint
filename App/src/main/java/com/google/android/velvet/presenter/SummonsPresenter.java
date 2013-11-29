package com.google.android.velvet.presenter;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import com.google.android.search.core.google.SearchBoxLogging;
import com.google.android.search.core.suggest.CachingPromoter;
import com.google.android.search.core.suggest.SuggestionList;
import com.google.android.search.core.suggest.Suggestions;
import com.google.android.search.core.suggest.SuggestionsController;
import com.google.android.search.core.suggest.SuggestionsController.Listener;
import com.google.android.search.core.suggest.SuggestionsUi;
import com.google.android.search.core.summons.Source;
import com.google.android.search.core.summons.SourceRanker;
import com.google.android.search.core.summons.SourceUtil;
import com.google.android.search.shared.api.Query;
import com.google.android.search.shared.ui.SuggestionListView;
import com.google.android.shared.util.Consumer;
import com.google.android.shared.util.Consumers;
import com.google.android.shared.util.ScheduledSingleThreadedExecutor;
import com.google.android.velvet.VelvetFactory;
import com.google.android.velvet.ui.MainContentView;
import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;

public class SummonsPresenter
  extends MainContentPresenter
  implements SuggestionsController.Listener
{
  private final int mMaxSuggestionsPerSourceIncrease;
  private final int mMaxSuggestionsPerSourceInitial;
  private View mNoResultsView;
  private final SearchBoxLogging mSearchBoxLogging;
  private final SourceRanker mSourceRanker;
  private final List<SourceState> mSourceStates;
  private final List<SuggestionListView> mSourceViews;
  private boolean mStarted;
  private final ScheduledSingleThreadedExecutor mUiThread;
  
  public SummonsPresenter(MainContentView paramMainContentView, SourceRanker paramSourceRanker, ScheduledSingleThreadedExecutor paramScheduledSingleThreadedExecutor, SearchBoxLogging paramSearchBoxLogging, int paramInt1, int paramInt2)
  {
    super("summons", paramMainContentView);
    this.mUiThread = paramScheduledSingleThreadedExecutor;
    this.mSearchBoxLogging = paramSearchBoxLogging;
    this.mSourceRanker = paramSourceRanker;
    this.mMaxSuggestionsPerSourceInitial = paramInt1;
    this.mMaxSuggestionsPerSourceIncrease = paramInt2;
    this.mSourceStates = Lists.newArrayList();
    this.mSourceViews = Lists.newArrayList();
  }
  
  private void createSourceViews(List<Source> paramList)
  {
    if ((isAttached()) && (this.mStarted))
    {
      Iterator localIterator = paramList.iterator();
      while (localIterator.hasNext())
      {
        Source localSource = (Source)localIterator.next();
        SuggestionListView localSuggestionListView = getFactory().createSummonsListView(this, getCardContainer());
        this.mSourceViews.add(localSuggestionListView);
        this.mSourceStates.add(new SourceState(localSource, localSuggestionListView));
      }
      postAddViews(0, this.mSourceViews);
    }
  }
  
  protected void onPostAttach(Bundle paramBundle)
  {
    getFactory();
    this.mNoResultsView = VelvetFactory.createNoSummonsMessageView(this, getCardContainer());
    this.mNoResultsView.setVisibility(8);
  }
  
  protected void onPreDetach() {}
  
  public void onStart()
  {
    super.onStart();
    this.mStarted = true;
    View[] arrayOfView = new View[1];
    arrayOfView[0] = this.mNoResultsView;
    postAddViews(arrayOfView);
    getVelvetPresenter().getSuggestionsController().addListener(this);
    this.mSourceRanker.getSourcesForUi(Consumers.createAsyncConsumer(this.mUiThread, new Consumer()
    {
      public boolean consume(List<Source> paramAnonymousList)
      {
        SummonsPresenter.this.createSourceViews(paramAnonymousList);
        return true;
      }
    }));
  }
  
  public void onStop()
  {
    super.onStop();
    this.mStarted = false;
    getVelvetPresenter().getSuggestionsController().removeListener(this);
    Iterator localIterator = this.mSourceStates.iterator();
    while (localIterator.hasNext()) {
      ((SourceState)localIterator.next()).dispose();
    }
    this.mSourceStates.clear();
    this.mSourceViews.clear();
    postRemoveAllViews();
    postResetScroll();
  }
  
  public void onSuggestionsUpdated(SuggestionsController paramSuggestionsController, Suggestions paramSuggestions)
  {
    switch (paramSuggestionsController.getSummonsFetchState())
    {
    default: 
      return;
    case 2: 
      postSetVisibility(this.mNoResultsView, 0);
      return;
    }
    postSetVisibility(this.mNoResultsView, 8);
  }
  
  private class SourceState
    implements View.OnClickListener, SuggestionsUi
  {
    private int mMaxDisplayed;
    private final SuggestionListView mSuggestionListView;
    
    SourceState(Source paramSource, SuggestionListView paramSuggestionListView)
    {
      this.mSuggestionListView = paramSuggestionListView;
      this.mMaxDisplayed = SummonsPresenter.this.mMaxSuggestionsPerSourceInitial;
      CachingPromoter localCachingPromoter = SummonsPresenter.this.getFactory().createSingleSourcePromoter(paramSource);
      this.mSuggestionListView.setTitle(SourceUtil.getLabel(this.mSuggestionListView.getContext(), paramSource));
      this.mSuggestionListView.setFooterClickListener(this);
      SummonsPresenter.this.getVelvetPresenter().getSuggestionsController().setMaxDisplayed(this, this.mMaxDisplayed);
      SummonsPresenter.this.getVelvetPresenter().getSuggestionsController().addSuggestionsView(this, localCachingPromoter, this);
    }
    
    void dispose()
    {
      SummonsPresenter.this.getVelvetPresenter().getSuggestionsController().removeSuggestionsView(this);
    }
    
    public void onClick(View paramView)
    {
      this.mMaxDisplayed += SummonsPresenter.this.mMaxSuggestionsPerSourceIncrease;
      SummonsPresenter.this.getVelvetPresenter().getSuggestionsController().setMaxDisplayed(this, this.mMaxDisplayed);
    }
    
    public void showSuggestions(SuggestionList paramSuggestionList, int paramInt, boolean paramBoolean)
    {
      this.mSuggestionListView.setCountText(paramSuggestionList.getCount());
      this.mSuggestionListView.showSuggestions(paramSuggestionList.getUserQuery().getQueryStringForSuggest(), paramSuggestionList.getSuggestions(), paramInt, paramBoolean);
      int i;
      SuggestionListView localSuggestionListView;
      int j;
      if (paramInt < paramSuggestionList.getCount())
      {
        i = 1;
        localSuggestionListView = this.mSuggestionListView;
        j = 0;
        if (i == 0) {
          break label78;
        }
      }
      for (;;)
      {
        localSuggestionListView.setFooterVisibility(j);
        return;
        i = 0;
        break;
        label78:
        j = 8;
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.velvet.presenter.SummonsPresenter
 * JD-Core Version:    0.7.0.1
 */