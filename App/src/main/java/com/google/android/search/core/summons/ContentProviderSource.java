package com.google.android.search.core.summons;

import android.content.ComponentName;
import android.content.Context;
import android.os.CancellationSignal;
import com.google.android.search.core.suggest.SuggestionList;
import com.google.android.search.shared.api.Query;

public abstract interface ContentProviderSource
  extends Source
{
  public abstract String getIconPackage();
  
  public abstract ComponentName getIntentComponent();
  
  public abstract int getQueryThreshold();
  
  public abstract String getSuggestUri();
  
  public abstract SuggestionList getSuggestions(Context paramContext, Query paramQuery, int paramInt, CancellationSignal paramCancellationSignal);
  
  public abstract boolean queryAfterZeroResults();
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.summons.ContentProviderSource
 * JD-Core Version:    0.7.0.1
 */