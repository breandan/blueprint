package com.google.android.search.core.suggest;

import android.content.Context;
import com.google.android.search.core.SearchConfig;
import com.google.android.search.core.summons.BrowserSourceFilter;
import com.google.android.search.core.summons.ContactsSourceFilter;
import com.google.android.search.core.summons.ContentProviderSource;

public class SuggestionFilterProvider
{
  private final SearchConfig mConfig;
  private final Context mContext;
  
  public SuggestionFilterProvider(Context paramContext, SearchConfig paramSearchConfig)
  {
    this.mContext = paramContext;
    this.mConfig = paramSearchConfig;
  }
  
  public SuggestionFilter getFilter(ContentProviderSource paramContentProviderSource, String paramString)
  {
    if (paramContentProviderSource != null)
    {
      if ("content://browser/bookmarks/search_suggest_query".equals(paramContentProviderSource.getSuggestUri()))
      {
        int i = this.mConfig.getMaxResultsPerSource();
        return new BrowserSourceFilter(this.mContext, this.mConfig, i);
      }
      if (paramContentProviderSource.isContactsSource()) {
        return ContactsSourceFilter.INSTANCE;
      }
    }
    return null;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.suggest.SuggestionFilterProvider
 * JD-Core Version:    0.7.0.1
 */