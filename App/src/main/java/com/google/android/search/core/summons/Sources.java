package com.google.android.search.core.summons;

import com.google.android.shared.util.ObservableDataSet;
import java.util.Collection;
import javax.annotation.Nonnull;

public abstract interface Sources<T extends Source>
  extends ObservableDataSet
{
  public abstract boolean containsSource(String paramString);
  
  @Nonnull
  public abstract T getSource(String paramString);
  
  @Nonnull
  public abstract Collection<T> getSources();
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.summons.Sources
 * JD-Core Version:    0.7.0.1
 */