package com.google.android.search.core.summons.icing;

import com.google.android.search.core.summons.Sources;
import com.google.android.search.shared.api.Query;
import com.google.android.shared.util.Consumer;
import java.util.Collection;

public abstract interface IcingSources
  extends Sources<IcingSource>
{
  public abstract void addSources(Collection<IcingSource> paramCollection);
  
  public abstract void clear();
  
  public abstract void getSuggestions(Query paramQuery, int paramInt, boolean paramBoolean, Consumer<IcingSuggestionsFactory.IcingResults> paramConsumer);
  
  public abstract void removeSources(String paramString);
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.summons.icing.IcingSources
 * JD-Core Version:    0.7.0.1
 */