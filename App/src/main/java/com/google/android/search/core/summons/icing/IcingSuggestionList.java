package com.google.android.search.core.summons.icing;

import android.content.ComponentName;
import android.net.Uri;
import com.google.android.gms.appdatasearch.SearchResults.Result;
import com.google.android.gms.appdatasearch.SearchResults.ResultIterator;
import com.google.android.search.core.suggest.SuggestionBuilder;
import com.google.android.search.core.suggest.SuggestionListImpl;
import com.google.android.search.core.summons.SourceNameHelper;
import com.google.android.search.shared.api.Query;
import com.google.android.search.shared.api.Suggestion;
import com.google.android.shared.util.Util;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class IcingSuggestionList
  extends SuggestionListImpl
{
  private Integer mCount;
  private final List<Suggestion> mDecodedSuggestions;
  private final SearchResults.ResultIterator mResultIter;
  private final SourceNameHelper mSourceNameHelper;
  private final Map<String, IcingSource> mSourceNameToSource;
  private Boolean mTopIcingSource;
  
  private IcingSuggestionList(SourceNameHelper paramSourceNameHelper, Collection<IcingSource> paramCollection, String paramString, Query paramQuery, SearchResults.ResultIterator paramResultIterator)
  {
    super(paramString, paramQuery);
    this.mSourceNameHelper = paramSourceNameHelper;
    this.mResultIter = paramResultIterator;
    this.mSourceNameToSource = Maps.newHashMap();
    this.mDecodedSuggestions = Lists.newArrayList();
    Iterator localIterator = paramCollection.iterator();
    while (localIterator.hasNext())
    {
      IcingSource localIcingSource = (IcingSource)localIterator.next();
      this.mSourceNameToSource.put(localIcingSource.getName(), localIcingSource);
    }
  }
  
  static IcingSuggestionList createMixedSourceList(SourceNameHelper paramSourceNameHelper, Collection<IcingSource> paramCollection, Query paramQuery, SearchResults.ResultIterator paramResultIterator)
  {
    return new IcingSuggestionList(paramSourceNameHelper, paramCollection, "mixed-icing-results", paramQuery, paramResultIterator);
  }
  
  static IcingSuggestionList createSingleSourceList(SourceNameHelper paramSourceNameHelper, IcingSource paramIcingSource, Query paramQuery, SearchResults.ResultIterator paramResultIterator)
  {
    return new IcingSuggestionList(paramSourceNameHelper, Collections.singleton(paramIcingSource), paramIcingSource.getName(), paramQuery, paramResultIterator);
  }
  
  private String getIntentAction(SearchResults.Result paramResult)
  {
    String str = paramResult.getSection("intent_action");
    if (str == null) {
      str = getSource(paramResult).getDefaultIntentAction();
    }
    return str;
  }
  
  private ComponentName getIntentComponent(SearchResults.Result paramResult)
  {
    String str1 = paramResult.getSection("intent_activity");
    if (str1 == null) {
      str1 = getSource(paramResult).getDefaultIntentActivity();
    }
    if ((str1 == null) || (str1.length() == 0)) {
      return null;
    }
    String str2 = getSource(paramResult).getPackageName();
    if (str1.startsWith(".")) {
      str1 = str2 + str1;
    }
    return new ComponentName(str2, str1);
  }
  
  private String getIntentData(SearchResults.Result paramResult)
  {
    String str1 = paramResult.getSection("intent_data");
    if (str1 == null) {
      str1 = getSource(paramResult).getDefaultIntentData();
    }
    if (str1 != null)
    {
      String str2 = paramResult.getSection("intent_data_id");
      if (str2 != null) {
        str1 = str1 + "/" + Uri.encode(str2);
      }
    }
    return str1;
  }
  
  private IcingSource getSource(SearchResults.Result paramResult)
  {
    String str = this.mSourceNameHelper.getSourceNameForIcingResult(paramResult);
    IcingSource localIcingSource = (IcingSource)this.mSourceNameToSource.get(str);
    Preconditions.checkNotNull(localIcingSource, "Could not find source for " + str);
    return localIcingSource;
  }
  
  private Suggestion getSuggestion()
  {
    Preconditions.checkNotNull(this.mResultIter);
    Preconditions.checkState(this.mResultIter.hasNext());
    SearchResults.Result localResult = this.mResultIter.next();
    if (this.mTopIcingSource == null) {
      if (localResult.getPosition() != 0) {
        break label136;
      }
    }
    label136:
    for (boolean bool = true;; bool = false)
    {
      this.mTopIcingSource = Boolean.valueOf(bool);
      return SuggestionBuilder.builder().source(getSource(localResult)).text1(localResult.getSection("text1")).text2(localResult.getSection("text2")).intentAction(getIntentAction(localResult)).intentData(getIntentData(localResult)).intentExtraData(localResult.getSection("intent_extra_data")).intentComponent(getIntentComponent(localResult)).icon1(Util.toResourceUriString(localResult.getPackageName(), localResult.getSection("icon"))).isFromIcing(true).build();
    }
  }
  
  public Suggestion get(int paramInt)
  {
    if (paramInt < getCount()) {}
    for (boolean bool = true;; bool = false)
    {
      Preconditions.checkArgument(bool);
      for (int i = this.mDecodedSuggestions.size(); i <= paramInt; i++) {
        this.mDecodedSuggestions.add(getSuggestion());
      }
    }
    return (Suggestion)this.mDecodedSuggestions.get(paramInt);
  }
  
  public int getCount()
  {
    if (this.mCount == null) {
      if (this.mResultIter != null) {
        break label32;
      }
    }
    label32:
    for (int i = 0;; i = this.mResultIter.getCount())
    {
      this.mCount = Integer.valueOf(i);
      return this.mCount.intValue();
    }
  }
  
  public Iterator<Suggestion> iterator()
  {
    return new IcingSuggestionIter(null);
  }
  
  public String toString()
  {
    return "IcingSuggestionList[" + getSourceName() + ", count=" + getCount() + "]";
  }
  
  private class IcingSuggestionIter
    implements Iterator<Suggestion>
  {
    private int mCurrentPos;
    
    private IcingSuggestionIter() {}
    
    public boolean hasNext()
    {
      return this.mCurrentPos < IcingSuggestionList.this.getCount();
    }
    
    public Suggestion next()
    {
      if (!hasNext()) {
        throw new NoSuchElementException();
      }
      Suggestion localSuggestion = IcingSuggestionList.this.get(this.mCurrentPos);
      this.mCurrentPos = (1 + this.mCurrentPos);
      return localSuggestion;
    }
    
    public void remove()
    {
      throw new UnsupportedOperationException();
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.summons.icing.IcingSuggestionList
 * JD-Core Version:    0.7.0.1
 */