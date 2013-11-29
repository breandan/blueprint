package com.google.android.search.core.suggest;

import android.database.DataSetObserver;
import com.google.common.base.Preconditions;
import javax.annotation.Nonnull;

public class CachingPromoter
{
  public static final String PROMOTED_SOURCE = "promoted";
  @Nonnull
  private CacheEntry mCache;
  private final int mMaxPromoted;
  @Nonnull
  private final Promoter mPromoter;
  
  public CachingPromoter(Promoter paramPromoter, int paramInt)
  {
    this.mPromoter = ((Promoter)Preconditions.checkNotNull(paramPromoter));
    this.mMaxPromoted = paramInt;
    this.mCache = new CacheEntry(null);
  }
  
  private SuggestionList buildPromoted(Suggestions paramSuggestions)
  {
    SuggestionListNoDuplicates localSuggestionListNoDuplicates = new SuggestionListNoDuplicates("promoted", paramSuggestions.getQuery());
    localSuggestionListNoDuplicates.setSourceSuggestions(paramSuggestions);
    this.mPromoter.pickPromoted(paramSuggestions, this.mMaxPromoted, localSuggestionListNoDuplicates);
    return localSuggestionListNoDuplicates;
  }
  
  public SuggestionList getPromoted()
  {
    return this.mCache.getPromoted();
  }
  
  public void notifyChanged()
  {
    this.mCache.onChanged();
  }
  
  public void setSuggestions(Suggestions paramSuggestions)
  {
    if (this.mCache != null) {
      this.mCache.dispose();
    }
    this.mCache = new CacheEntry(paramSuggestions);
  }
  
  final class CacheEntry
    extends DataSetObserver
  {
    SuggestionList mPromoted;
    final Suggestions mSuggestions;
    
    CacheEntry(Suggestions paramSuggestions)
    {
      this.mSuggestions = paramSuggestions;
      if (this.mSuggestions != null) {
        this.mSuggestions.registerDataSetObserver(this);
      }
    }
    
    public void dispose()
    {
      if (this.mSuggestions != null) {
        this.mSuggestions.unregisterDataSetObserver(this);
      }
    }
    
    SuggestionList getPromoted()
    {
      if (this.mPromoted == null) {
        this.mPromoted = CachingPromoter.this.buildPromoted(this.mSuggestions);
      }
      return this.mPromoted;
    }
    
    public void onChanged()
    {
      if ((this.mPromoted != null) && (!this.mPromoted.isFinal())) {
        this.mPromoted = null;
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.suggest.CachingPromoter
 * JD-Core Version:    0.7.0.1
 */