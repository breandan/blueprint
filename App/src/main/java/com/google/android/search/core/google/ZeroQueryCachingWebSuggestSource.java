package com.google.android.search.core.google;

import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.text.TextUtils;
import com.google.android.search.core.SearchSettings;
import com.google.android.search.core.google.complete.CompleteServerConstants;
import com.google.android.search.core.google.gaia.LoginHelper;
import com.google.android.search.core.suggest.MutableSuggestionList;
import com.google.android.search.core.suggest.MutableSuggestionListImpl;
import com.google.android.search.core.suggest.SuggestionBuilder;
import com.google.android.search.core.suggest.SuggestionList;
import com.google.android.search.core.suggest.SuggestionListImpl;
import com.google.android.search.shared.api.Query;
import com.google.android.search.shared.api.Suggestion;
import com.google.android.shared.util.Consumer;
import java.util.Iterator;
import javax.annotation.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ZeroQueryCachingWebSuggestSource
  extends WebSuggestSourceWrapper
{
  final DataSetObserver mAccountsObserver;
  private final SuggestionList mEmptyResults;
  @Nullable
  private final DataSetObservable mHistoryObservable;
  private final LoginHelper mLoginHelper;
  private final SearchSettings mSettings;
  private SuggestionList mZeroQueryCache;
  
  public ZeroQueryCachingWebSuggestSource(WebSuggestSource paramWebSuggestSource, SearchSettings paramSearchSettings, LoginHelper paramLoginHelper, @Nullable DataSetObservable paramDataSetObservable)
  {
    super(paramWebSuggestSource);
    this.mSettings = paramSearchSettings;
    this.mLoginHelper = paramLoginHelper;
    this.mAccountsObserver = new DataSetObserver()
    {
      public void onChanged()
      {
        ZeroQueryCachingWebSuggestSource.this.validateCache(true);
      }
    };
    this.mEmptyResults = new SuggestionListImpl(getSourceName(), Query.EMPTY);
    this.mZeroQueryCache = loadSuggestions();
    validateCache(false);
    this.mLoginHelper.registerDataSetObserver(this.mAccountsObserver);
    this.mHistoryObservable = paramDataSetObservable;
  }
  
  private void cacheResults(SuggestionList paramSuggestionList)
  {
    try
    {
      if (isValid(paramSuggestionList))
      {
        this.mZeroQueryCache = paramSuggestionList;
        String str = serializeSuggestions(this.mZeroQueryCache);
        if (!str.equals(this.mSettings.getCachedZeroQueryWebResults()))
        {
          this.mSettings.setCachedZeroQueryWebResults(str);
          if (this.mHistoryObservable != null) {
            this.mHistoryObservable.notifyChanged();
          }
        }
      }
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  private void clearCache()
  {
    this.mZeroQueryCache = null;
    this.mSettings.setCachedZeroQueryWebResults(null);
  }
  
  private boolean isValid(SuggestionList paramSuggestionList)
  {
    return (paramSuggestionList != null) && (!paramSuggestionList.isRequestFailed()) && ((paramSuggestionList.getAccount() == null) || (paramSuggestionList.getAccount().equals(this.mLoginHelper.getAccountName())));
  }
  
  private SuggestionList loadSuggestions()
  {
    String str1 = this.mSettings.getCachedZeroQueryWebResults();
    MutableSuggestionListImpl localMutableSuggestionListImpl;
    if (str1 != null) {
      try
      {
        JSONObject localJSONObject = new JSONObject(str1);
        String str2 = localJSONObject.getString("source");
        String str3 = localJSONObject.getString("account");
        String str4 = localJSONObject.getString("q");
        localMutableSuggestionListImpl = new MutableSuggestionListImpl(str2, Query.EMPTY.withQueryChars(str4));
        localMutableSuggestionListImpl.setAccount(str3);
        JSONArray localJSONArray = localJSONObject.getJSONArray("suggestions");
        int i = 0;
        while (i < localJSONArray.length())
        {
          String str5 = localJSONArray.getString(i);
          localMutableSuggestionListImpl.add(SuggestionBuilder.builder().text1(str5).intentAction("android.intent.action.WEB_SEARCH").suggestionQuery(str5).isHistory(true).logType(CompleteServerConstants.LOG_TYPE_SEARCH_HISTORY).build());
          i++;
          continue;
          localMutableSuggestionListImpl = null;
        }
      }
      catch (JSONException localJSONException)
      {
        this.mSettings.setCachedZeroQueryWebResults(null);
      }
    }
    return localMutableSuggestionListImpl;
  }
  
  private void removeQueryFromCache(String paramString)
  {
    try
    {
      if (this.mZeroQueryCache != null)
      {
        Iterator localIterator = this.mZeroQueryCache.iterator();
        while (localIterator.hasNext()) {
          if (TextUtils.equals(((Suggestion)localIterator.next()).getSuggestionQuery(), paramString)) {
            clearCache();
          }
        }
      }
      return;
    }
    finally {}
  }
  
  static String serializeSuggestions(SuggestionList paramSuggestionList)
  {
    try
    {
      JSONObject localJSONObject = new JSONObject();
      localJSONObject.put("source", paramSuggestionList.getSourceName());
      localJSONObject.put("account", paramSuggestionList.getAccount());
      localJSONObject.put("q", paramSuggestionList.getUserQuery().getQueryStringForSuggest());
      JSONArray localJSONArray = new JSONArray();
      for (int i = 0; i < paramSuggestionList.getCount(); i++) {
        localJSONArray.put(i, paramSuggestionList.get(i).getSuggestionQuery());
      }
      localJSONObject.put("suggestions", localJSONArray);
      String str = localJSONObject.toString();
      return str;
    }
    catch (JSONException localJSONException) {}
    return null;
  }
  
  private void validateCache(boolean paramBoolean)
  {
    try
    {
      if (!isValid(this.mZeroQueryCache))
      {
        clearCache();
        if (!paramBoolean) {
          this.mZeroQueryCache = this.mEmptyResults;
        }
      }
      return;
    }
    finally {}
  }
  
  public void close()
  {
    this.mLoginHelper.unregisterDataSetObserver(this.mAccountsObserver);
    super.close();
  }
  
  /* Error */
  public SuggestionList getCachedSuggestions(Query paramQuery)
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_1
    //   3: invokevirtual 265	com/google/android/search/shared/api/Query:isEmptySuggestQuery	()Z
    //   6: ifeq +14 -> 20
    //   9: aload_0
    //   10: getfield 55	com/google/android/search/core/google/ZeroQueryCachingWebSuggestSource:mZeroQueryCache	Lcom/google/android/search/core/suggest/SuggestionList;
    //   13: astore 4
    //   15: aload_0
    //   16: monitorexit
    //   17: aload 4
    //   19: areturn
    //   20: aload_0
    //   21: aload_1
    //   22: invokespecial 267	com/google/android/search/core/google/WebSuggestSourceWrapper:getCachedSuggestions	(Lcom/google/android/search/shared/api/Query;)Lcom/google/android/search/core/suggest/SuggestionList;
    //   25: astore_3
    //   26: aload_3
    //   27: astore 4
    //   29: goto -14 -> 15
    //   32: astore_2
    //   33: aload_0
    //   34: monitorexit
    //   35: aload_2
    //   36: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	37	0	this	ZeroQueryCachingWebSuggestSource
    //   0	37	1	paramQuery	Query
    //   32	4	2	localObject1	Object
    //   25	2	3	localSuggestionList	SuggestionList
    //   13	15	4	localObject2	Object
    // Exception table:
    //   from	to	target	type
    //   2	15	32	finally
    //   20	26	32	finally
  }
  
  public void getSuggestions(Query paramQuery, final Consumer<SuggestionList> paramConsumer)
  {
    if (paramQuery.isEmptySuggestQuery()) {
      paramConsumer = new Consumer()
      {
        public boolean consume(SuggestionList paramAnonymousSuggestionList)
        {
          ZeroQueryCachingWebSuggestSource.this.cacheResults(paramAnonymousSuggestionList);
          return paramConsumer.consume(paramAnonymousSuggestionList);
        }
      };
    }
    super.getSuggestions(paramQuery, paramConsumer);
  }
  
  public boolean removeFromHistory(String paramString)
  {
    removeQueryFromCache(paramString);
    return super.removeFromHistory(paramString);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.google.ZeroQueryCachingWebSuggestSource
 * JD-Core Version:    0.7.0.1
 */