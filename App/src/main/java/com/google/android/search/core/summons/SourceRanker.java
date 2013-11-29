package com.google.android.search.core.summons;

import com.google.android.shared.util.Consumer;
import java.util.List;

public abstract interface SourceRanker
{
  public abstract void getSourcesForQuerying(Consumer<List<ContentProviderSource>> paramConsumer);
  
  public abstract void getSourcesForUi(Consumer<List<Source>> paramConsumer);
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.summons.SourceRanker
 * JD-Core Version:    0.7.0.1
 */