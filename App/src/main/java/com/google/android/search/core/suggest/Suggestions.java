package com.google.android.search.core.suggest;

import android.database.DataSetObserver;
import android.util.Log;
import com.google.android.search.core.google.GoogleSource;
import com.google.android.search.shared.api.Query;
import com.google.android.shared.util.SafeDataSetObservable;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nonnull;

public class Suggestions
{
  public static final Suggestions NONE = new Suggestions(Query.EMPTY, false, null)
  {
    public void registerDataSetObserver(DataSetObserver paramAnonymousDataSetObserver) {}
    
    public void unregisterDataSetObserver(DataSetObserver paramAnonymousDataSetObserver) {}
  };
  private boolean mClosed = false;
  private final SafeDataSetObservable mDataSetObservable = new SafeDataSetObservable();
  private boolean mDone = false;
  private final boolean mIsQueryingSummons;
  private final Query mQuery;
  private final List<SuggestionList> mSourceResults;
  private boolean mSummonsDone = false;
  private boolean mTimedOut = false;
  private SuggestionList mWebResult;
  private final GoogleSource mWebSource;
  
  public Suggestions(@Nonnull Query paramQuery, boolean paramBoolean, GoogleSource paramGoogleSource)
  {
    this.mQuery = paramQuery;
    this.mIsQueryingSummons = paramBoolean;
    this.mWebSource = paramGoogleSource;
    this.mSourceResults = new ArrayList();
  }
  
  private int countSourceResults()
  {
    int i = 0;
    for (int j = 0; j < this.mSourceResults.size(); j++) {
      i += ((SuggestionList)this.mSourceResults.get(j)).getCount();
    }
    return i;
  }
  
  private int countWebResults()
  {
    if (this.mWebResult != null) {
      return 1;
    }
    return 0;
  }
  
  private boolean isTimedOut()
  {
    return this.mTimedOut;
  }
  
  public void addSummonsResults(List<SuggestionList> paramList)
  {
    if ((isClosed()) || (isTimedOut()) || (paramList == null)) {
      return;
    }
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      SuggestionList localSuggestionList = (SuggestionList)localIterator.next();
      boolean bool = Query.equivalentForSuggest(this.mQuery, localSuggestionList.getUserQuery());
      Object[] arrayOfObject = new Object[2];
      arrayOfObject[0] = this.mQuery;
      arrayOfObject[1] = localSuggestionList.getUserQuery();
      Preconditions.checkState(bool, "Got result for wrong query %s != %s", arrayOfObject);
      this.mSourceResults.add(localSuggestionList);
      localSuggestionList.setSourceSuggestions(this);
    }
    notifyDataSetChanged();
  }
  
  public void addWebResult(SuggestionList paramSuggestionList)
  {
    if ((isClosed()) || (isTimedOut()) || (paramSuggestionList == null)) {
      return;
    }
    boolean bool = Query.equivalentForSuggest(this.mQuery, paramSuggestionList.getUserQuery());
    Object[] arrayOfObject = new Object[2];
    arrayOfObject[0] = this.mQuery;
    arrayOfObject[1] = paramSuggestionList.getUserQuery();
    Preconditions.checkState(bool, "Got result for wrong query %s != %s", arrayOfObject);
    this.mWebResult = paramSuggestionList;
    this.mWebResult.setSourceSuggestions(this);
    notifyDataSetChanged();
  }
  
  public boolean areSummonsDone()
  {
    return (this.mDone) || (this.mSummonsDone) || (!this.mIsQueryingSummons);
  }
  
  public boolean areWebResultsDone()
  {
    return (this.mDone) || (this.mWebSource == null) || (countWebResults() > 0);
  }
  
  public void close()
  {
    if (this.mClosed) {
      return;
    }
    this.mDataSetObservable.unregisterAll();
    this.mClosed = true;
  }
  
  public void done()
  {
    this.mDone = true;
  }
  
  protected void finalize()
  {
    if (this.mDataSetObservable.getObserverCount() > 0) {
      Log.e("QSB.Suggestions", "***LEAK *** : Some observers have not been unregistered !!");
    }
  }
  
  public Suggestions getOpenedCopy()
  {
    if ((isClosed()) && (isDone())) {}
    Suggestions localSuggestions;
    ArrayList localArrayList;
    for (boolean bool = true;; bool = false)
    {
      Preconditions.checkState(bool);
      localSuggestions = new Suggestions(this.mQuery, this.mIsQueryingSummons, this.mWebSource);
      localArrayList = Lists.newArrayList();
      Iterator localIterator = this.mSourceResults.iterator();
      while (localIterator.hasNext())
      {
        SuggestionList localSuggestionList = (SuggestionList)localIterator.next();
        if (localSuggestionList != null) {
          localArrayList.add(localSuggestionList);
        }
      }
    }
    localSuggestions.addSummonsResults(localArrayList);
    localSuggestions.addWebResult(this.mWebResult);
    localSuggestions.done();
    return localSuggestions;
  }
  
  @Nonnull
  public Query getQuery()
  {
    return this.mQuery;
  }
  
  public int getResultCount()
  {
    return this.mSourceResults.size() + countWebResults();
  }
  
  public Iterable<SuggestionList> getSourceResults()
  {
    return new ArrayList(this.mSourceResults);
  }
  
  public int getSummonsCount()
  {
    return countSourceResults();
  }
  
  public SuggestionList getWebResult()
  {
    return this.mWebResult;
  }
  
  public GoogleSource getWebSource()
  {
    return this.mWebSource;
  }
  
  public boolean isClosed()
  {
    return this.mClosed;
  }
  
  public boolean isDone()
  {
    return (this.mDone) || ((areSummonsDone()) && (areWebResultsDone()));
  }
  
  public boolean isFetchingSummons()
  {
    return this.mIsQueryingSummons;
  }
  
  void notifyDataSetChanged()
  {
    this.mDataSetObservable.notifyChanged();
  }
  
  public void registerDataSetObserver(DataSetObserver paramDataSetObserver)
  {
    if (this.mClosed) {
      return;
    }
    this.mDataSetObservable.registerObserver(paramDataSetObserver);
  }
  
  public void setSummonsDone()
  {
    if (!this.mSummonsDone)
    {
      this.mSummonsDone = true;
      notifyDataSetChanged();
    }
  }
  
  public void timedOut()
  {
    done();
    this.mTimedOut = true;
    notifyDataSetChanged();
  }
  
  public String toString()
  {
    return "Suggestions@" + hashCode() + "{webSource=" + this.mWebSource + ",getResultCount()=" + getResultCount() + "}";
  }
  
  public void unregisterDataSetObserver(DataSetObserver paramDataSetObserver)
  {
    if (this.mClosed) {
      return;
    }
    this.mDataSetObservable.unregisterObserver(paramDataSetObserver);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.suggest.Suggestions
 * JD-Core Version:    0.7.0.1
 */