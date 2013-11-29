package com.google.android.search.core.google;

import android.content.Context;
import android.text.TextUtils;
import com.google.android.search.core.CoreSearchServices;
import com.google.android.search.core.suggest.MutableSuggestionList;
import com.google.android.search.core.suggest.MutableSuggestionListImpl;
import com.google.android.search.core.suggest.SuggestionList;
import com.google.android.search.core.util.Latency;
import com.google.android.search.shared.api.Query;
import com.google.android.shared.util.Consumer;
import javax.annotation.Nonnull;

public abstract class AbstractGoogleWebSource
  extends AbstractGoogleSource
  implements WebSuggestSource
{
  protected AbstractGoogleWebSource(Context paramContext, CoreSearchServices paramCoreSearchServices)
  {
    super(paramContext, paramCoreSearchServices);
  }
  
  public void close() {}
  
  @Nonnull
  protected SuggestionList doQueryExternal(String paramString)
  {
    Query localQuery = Query.EMPTY.withQueryChars(paramString);
    MutableSuggestionListImpl localMutableSuggestionListImpl = new MutableSuggestionListImpl(getSourceName(), localQuery);
    if (!TextUtils.isEmpty(paramString)) {
      query(localQuery, false, localMutableSuggestionListImpl);
    }
    return localMutableSuggestionListImpl;
  }
  
  protected void doQueryInternal(Query paramQuery, Consumer<SuggestionList> paramConsumer)
  {
    MutableSuggestionListImpl localMutableSuggestionListImpl = new MutableSuggestionListImpl(getSourceName(), paramQuery);
    query(paramQuery, true, localMutableSuggestionListImpl);
    paramConsumer.consume(localMutableSuggestionListImpl);
  }
  
  protected abstract void query(Query paramQuery, boolean paramBoolean, MutableSuggestionList paramMutableSuggestionList);
  
  @Nonnull
  public SuggestionList queryExternal(String paramString)
  {
    Latency localLatency = new Latency(getClock());
    SuggestionList localSuggestionList = doQueryExternal(paramString);
    localSuggestionList.setLatency(localLatency.getLatency());
    return localSuggestionList;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.google.AbstractGoogleWebSource
 * JD-Core Version:    0.7.0.1
 */