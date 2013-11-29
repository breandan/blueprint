package com.google.android.search.core.summons.icing;

import com.google.android.gms.appdatasearch.SearchResults;
import com.google.android.gms.appdatasearch.SearchResults.ResultIterator;
import com.google.android.search.core.SearchSettings;
import com.google.android.search.core.summons.SourceNameHelper;
import com.google.android.search.shared.api.Query;
import com.google.android.shared.util.Consumer;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class IcingSuggestionsFactory
{
  private final IcingConnection mConnection;
  private final SearchSettings mSettings;
  private final SourceNameHelper mSourceNameHelper;
  
  IcingSuggestionsFactory(IcingConnection paramIcingConnection, SearchSettings paramSearchSettings, SourceNameHelper paramSourceNameHelper)
  {
    this.mConnection = paramIcingConnection;
    this.mSettings = paramSearchSettings;
    this.mSourceNameHelper = paramSourceNameHelper;
  }
  
  void getSuggestions(Collection<IcingSource> paramCollection, Query paramQuery, int paramInt, boolean paramBoolean, Consumer<IcingResults> paramConsumer)
  {
    ArrayList localArrayList = new ArrayList();
    Iterator localIterator = paramCollection.iterator();
    while (localIterator.hasNext())
    {
      IcingSource localIcingSource = (IcingSource)localIterator.next();
      if (this.mSettings.isSourceEnabled(localIcingSource)) {
        localArrayList.add(localIcingSource);
      }
    }
    this.mConnection.queryGlobalSearch(paramQuery.getQueryStringForSuggest(), paramInt, localArrayList, new ResultConsumer(this.mSourceNameHelper, localArrayList, paramQuery, paramBoolean, paramConsumer));
  }
  
  public static final class IcingResults
  {
    public final List<IcingSuggestionList> suggestionLists;
    public final int totalNumResults;
    
    public IcingResults(int paramInt, List<IcingSuggestionList> paramList)
    {
      this.totalNumResults = paramInt;
      this.suggestionLists = Collections.unmodifiableList(paramList);
    }
  }
  
  static final class ResultConsumer
    implements Consumer<SearchResults>
  {
    private final Collection<IcingSource> mEnabledSources;
    private final boolean mMixResults;
    private final Query mQuery;
    private final SourceNameHelper mSourceNameHelper;
    private final Consumer<IcingSuggestionsFactory.IcingResults> mSuggestionListConsumer;
    
    public ResultConsumer(SourceNameHelper paramSourceNameHelper, Collection<IcingSource> paramCollection, Query paramQuery, boolean paramBoolean, Consumer<IcingSuggestionsFactory.IcingResults> paramConsumer)
    {
      this.mSourceNameHelper = paramSourceNameHelper;
      this.mEnabledSources = paramCollection;
      this.mQuery = paramQuery;
      this.mMixResults = paramBoolean;
      this.mSuggestionListConsumer = paramConsumer;
    }
    
    public boolean consume(SearchResults paramSearchResults)
    {
      this.mSuggestionListConsumer.consume(getSuggestionsLists(paramSearchResults));
      return true;
    }
    
    IcingSuggestionsFactory.IcingResults getSuggestionsLists(SearchResults paramSearchResults)
    {
      ArrayList localArrayList = Lists.newArrayListWithCapacity(this.mEnabledSources.size());
      if (paramSearchResults == null) {
        return new IcingSuggestionsFactory.IcingResults(0, localArrayList);
      }
      if (this.mMixResults)
      {
        SearchResults.ResultIterator localResultIterator2 = paramSearchResults.iterator();
        localArrayList.add(IcingSuggestionList.createMixedSourceList(this.mSourceNameHelper, this.mEnabledSources, this.mQuery, localResultIterator2));
        return new IcingSuggestionsFactory.IcingResults(paramSearchResults.getNumResults(), localArrayList);
      }
      Iterator localIterator = this.mEnabledSources.iterator();
      label87:
      IcingSource localIcingSource;
      String str1;
      String str2;
      if (localIterator.hasNext())
      {
        localIcingSource = (IcingSource)localIterator.next();
        str1 = localIcingSource.getPackageName();
        str2 = localIcingSource.getInternalCorpusName();
        if (str2 != null) {
          break label159;
        }
      }
      label159:
      for (SearchResults.ResultIterator localResultIterator1 = paramSearchResults.iterator(str1);; localResultIterator1 = paramSearchResults.iterator(str1, str2))
      {
        localArrayList.add(IcingSuggestionList.createSingleSourceList(this.mSourceNameHelper, localIcingSource, this.mQuery, localResultIterator1));
        break label87;
        break;
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.summons.icing.IcingSuggestionsFactory
 * JD-Core Version:    0.7.0.1
 */