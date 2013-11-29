package com.google.android.search.core.google;

import android.content.Context;
import com.google.android.search.core.CoreSearchServices;
import com.google.android.search.core.SearchConfig;
import com.google.android.search.core.suggest.SuggestionList;
import com.google.android.search.core.util.Latency;
import com.google.android.search.shared.api.Query;
import com.google.android.shared.util.Clock;
import com.google.android.shared.util.Consumer;
import javax.annotation.Nonnull;

public abstract class AbstractGoogleSource
  implements GoogleSource
{
  private final Clock mClock;
  private final SearchConfig mConfig;
  private final Context mContext;
  
  public AbstractGoogleSource(Context paramContext, CoreSearchServices paramCoreSearchServices)
  {
    this.mContext = paramContext;
    this.mConfig = paramCoreSearchServices.getConfig();
    this.mClock = paramCoreSearchServices.getClock();
  }
  
  protected abstract void doQueryInternal(Query paramQuery, Consumer<SuggestionList> paramConsumer);
  
  public SuggestionList getCachedSuggestions(Query paramQuery)
  {
    return null;
  }
  
  protected Clock getClock()
  {
    return this.mClock;
  }
  
  protected SearchConfig getConfig()
  {
    return this.mConfig;
  }
  
  public void getSuggestions(Query paramQuery, final Consumer<SuggestionList> paramConsumer)
  {
    doQueryInternal(paramQuery, new Consumer()
    {
      public boolean consume(@Nonnull SuggestionList paramAnonymousSuggestionList)
      {
        paramAnonymousSuggestionList.setLatency(this.val$latency.getLatency());
        return paramConsumer.consume(paramAnonymousSuggestionList);
      }
    });
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.google.AbstractGoogleSource
 * JD-Core Version:    0.7.0.1
 */