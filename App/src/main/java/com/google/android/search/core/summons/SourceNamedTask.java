package com.google.android.search.core.summons;

import android.content.Context;
import android.os.CancellationSignal;
import com.google.android.search.core.suggest.SuggestionList;
import com.google.android.search.shared.api.Query;
import com.google.android.shared.util.Consumer;
import com.google.android.shared.util.Consumers;
import com.google.android.shared.util.NamedTask;
import com.google.android.shared.util.ScheduledSingleThreadedExecutor;

public class SourceNamedTask
  implements NamedTask
{
  protected final Consumer<SuggestionList> mConsumer;
  protected final Context mContext;
  protected final ScheduledSingleThreadedExecutor mPublishThread;
  protected final Query mQuery;
  protected final int mQueryLimit;
  private final CancellationSignal mSignal;
  protected final ContentProviderSource mSource;
  
  public SourceNamedTask(Context paramContext, Query paramQuery, ContentProviderSource paramContentProviderSource, int paramInt, Consumer<SuggestionList> paramConsumer, ScheduledSingleThreadedExecutor paramScheduledSingleThreadedExecutor)
  {
    this.mContext = paramContext;
    this.mSource = paramContentProviderSource;
    this.mQuery = paramQuery;
    this.mQueryLimit = paramInt;
    this.mConsumer = paramConsumer;
    this.mPublishThread = paramScheduledSingleThreadedExecutor;
    this.mSignal = new CancellationSignal();
  }
  
  public void cancelExecution()
  {
    if (!this.mSignal.isCanceled()) {
      this.mSignal.cancel();
    }
  }
  
  public String getName()
  {
    return this.mSource.getName();
  }
  
  protected SuggestionList getSuggestions()
  {
    return this.mSource.getSuggestions(this.mContext, this.mQuery, this.mQueryLimit, this.mSignal);
  }
  
  public void run()
  {
    SuggestionList localSuggestionList = getSuggestions();
    Consumers.consumeAsync(this.mPublishThread, this.mConsumer, localSuggestionList);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.summons.SourceNamedTask
 * JD-Core Version:    0.7.0.1
 */