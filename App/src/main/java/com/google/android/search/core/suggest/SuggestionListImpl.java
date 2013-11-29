package com.google.android.search.core.suggest;

import com.google.android.search.shared.api.Query;
import com.google.android.search.shared.api.Suggestion;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterators;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class SuggestionListImpl
  implements SuggestionList
{
  protected String mAccount;
  private final long mCreationTime;
  protected boolean mIsFinal;
  private boolean mIsFromCache;
  protected int mLatency = -1;
  protected boolean mRequestFailed;
  private final String mSourceName;
  private Suggestions mSourceSuggestions;
  protected final List<Suggestion> mSuggestions;
  private final Query mUserQuery;
  protected boolean mWasRequestMade = true;
  
  public SuggestionListImpl(String paramString, Query paramQuery)
  {
    this.mSourceName = paramString;
    this.mUserQuery = paramQuery;
    this.mSuggestions = ImmutableList.of();
    this.mCreationTime = 0L;
  }
  
  protected SuggestionListImpl(String paramString, Query paramQuery, int paramInt)
  {
    this(paramString, paramQuery, new ArrayList(paramInt));
  }
  
  protected SuggestionListImpl(String paramString, Query paramQuery, int paramInt, long paramLong)
  {
    this(paramString, paramQuery, new ArrayList(paramInt), paramLong);
  }
  
  public SuggestionListImpl(String paramString, Query paramQuery, List<Suggestion> paramList)
  {
    this.mSourceName = paramString;
    this.mUserQuery = paramQuery;
    this.mSuggestions = new ArrayList(paramList);
    this.mCreationTime = 0L;
  }
  
  public SuggestionListImpl(String paramString, Query paramQuery, List<Suggestion> paramList, long paramLong)
  {
    this.mSourceName = paramString;
    this.mUserQuery = paramQuery;
    this.mSuggestions = new ArrayList(paramList);
    this.mCreationTime = paramLong;
  }
  
  public SuggestionListImpl(String paramString, Query paramQuery, Suggestion... paramVarArgs)
  {
    this(paramString, paramQuery, Arrays.asList(paramVarArgs));
  }
  
  public Suggestion get(int paramInt)
  {
    return (Suggestion)this.mSuggestions.get(paramInt);
  }
  
  public String getAccount()
  {
    return this.mAccount;
  }
  
  public int getCount()
  {
    return this.mSuggestions.size();
  }
  
  public long getCreationTime()
  {
    return this.mCreationTime;
  }
  
  public int getLatency()
  {
    return this.mLatency;
  }
  
  public String getSourceName()
  {
    return this.mSourceName;
  }
  
  public Suggestions getSourceSuggestions()
  {
    return this.mSourceSuggestions;
  }
  
  public List<Suggestion> getSuggestions()
  {
    return this.mSuggestions;
  }
  
  public Query getUserQuery()
  {
    return this.mUserQuery;
  }
  
  public boolean isFinal()
  {
    return this.mIsFinal;
  }
  
  public boolean isFromCache()
  {
    return this.mIsFromCache;
  }
  
  public boolean isRequestFailed()
  {
    return this.mRequestFailed;
  }
  
  public Iterator<Suggestion> iterator()
  {
    return Iterators.unmodifiableIterator(this.mSuggestions.iterator());
  }
  
  public void setFromCache(boolean paramBoolean)
  {
    this.mIsFromCache = paramBoolean;
  }
  
  public void setLatency(int paramInt)
  {
    this.mLatency = paramInt;
  }
  
  public void setRequestFailed(boolean paramBoolean)
  {
    this.mRequestFailed = paramBoolean;
  }
  
  public void setRequestMade(boolean paramBoolean)
  {
    this.mWasRequestMade = paramBoolean;
  }
  
  public void setSourceSuggestions(Suggestions paramSuggestions)
  {
    this.mSourceSuggestions = paramSuggestions;
  }
  
  public String toString()
  {
    return getClass().getSimpleName() + "{[" + getUserQuery() + "] " + this.mSuggestions + "}";
  }
  
  public boolean wasRequestMade()
  {
    return this.mWasRequestMade;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.suggest.SuggestionListImpl
 * JD-Core Version:    0.7.0.1
 */