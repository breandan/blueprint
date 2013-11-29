package com.google.android.search.core;

import com.google.android.search.core.clicklog.ClickLog;
import com.google.android.search.core.history.SearchHistoryHelper;
import com.google.android.search.core.suggest.SuggestionsProvider;
import com.google.android.search.core.summons.ShouldQueryStrategy;
import com.google.android.search.core.summons.Source;
import com.google.android.search.core.summons.SourceRanker;
import com.google.android.search.core.summons.Sources;
import com.google.android.search.core.summons.icing.IcingFactory;
import com.google.android.search.core.summons.icing.IcingSources;
import com.google.android.search.shared.ui.SuggestionFormatter;
import java.io.PrintWriter;

public abstract interface GlobalSearchServices
{
  public abstract void dump(String paramString, PrintWriter paramPrintWriter);
  
  public abstract ClickLog getClickLog();
  
  public abstract IcingFactory getIcingFactory();
  
  public abstract IcingSources getIcingSources();
  
  public abstract SearchHistoryHelper getSearchHistoryHelper();
  
  public abstract ShouldQueryStrategy getShouldQueryStrategy();
  
  public abstract SourceRanker getSourceRanker();
  
  public abstract Sources<Source> getSources();
  
  public abstract SuggestionFormatter getSuggestionFormatter();
  
  public abstract SuggestionsProvider getSuggestionsProvider();
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.GlobalSearchServices
 * JD-Core Version:    0.7.0.1
 */