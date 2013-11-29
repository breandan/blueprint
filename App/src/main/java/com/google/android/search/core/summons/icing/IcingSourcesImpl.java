package com.google.android.search.core.summons.icing;

import android.database.DataSetObservable;
import android.database.DataSetObserver;
import com.google.android.search.shared.api.Query;
import com.google.android.shared.util.Consumer;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nonnull;

final class IcingSourcesImpl
  implements IcingSources
{
  private final Map<String, IcingSource> mIgnoredSources;
  private final Set<String> mInternalSourceNamesToIgnore;
  private final DataSetObservable mObservable = new DataSetObservable();
  private final Map<String, IcingSource> mSourceNameToSource;
  private ImmutableSet<IcingSource> mSourceSet;
  private final IcingSuggestionsFactory mSuggestionsFactory;
  
  IcingSourcesImpl(@Nonnull IcingSuggestionsFactory paramIcingSuggestionsFactory)
  {
    this.mSuggestionsFactory = paramIcingSuggestionsFactory;
    this.mSourceNameToSource = Maps.newHashMap();
    this.mInternalSourceNamesToIgnore = Sets.newHashSet();
    this.mIgnoredSources = Maps.newHashMap();
    this.mSourceSet = ImmutableSet.of();
  }
  
  private void maybeUpdateState()
  {
    Collection localCollection = this.mSourceNameToSource.values();
    boolean bool = localCollection.equals(this.mSourceSet);
    int i = 0;
    if (!bool)
    {
      this.mSourceSet = ImmutableSet.copyOf(localCollection);
      i = 1;
    }
    if (i != 0) {
      this.mObservable.notifyChanged();
    }
  }
  
  private static void removePackageName(Iterator<IcingSource> paramIterator, String paramString)
  {
    while (paramIterator.hasNext()) {
      if (((IcingSource)paramIterator.next()).getPackageName().equals(paramString)) {
        paramIterator.remove();
      }
    }
  }
  
  public void addSources(Collection<IcingSource> paramCollection)
  {
    for (;;)
    {
      try
      {
        Iterator localIterator = paramCollection.iterator();
        if (!localIterator.hasNext()) {
          break;
        }
        IcingSource localIcingSource = (IcingSource)localIterator.next();
        if (this.mInternalSourceNamesToIgnore.contains(localIcingSource.getInternalCorpusName())) {
          this.mIgnoredSources.put(localIcingSource.getInternalCorpusName(), localIcingSource);
        } else {
          this.mSourceNameToSource.put(localIcingSource.getName(), localIcingSource);
        }
      }
      finally {}
    }
    maybeUpdateState();
  }
  
  public void clear()
  {
    try
    {
      this.mSourceNameToSource.clear();
      maybeUpdateState();
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public boolean containsSource(String paramString)
  {
    try
    {
      boolean bool = this.mSourceNameToSource.containsKey(paramString);
      return bool;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  @Nonnull
  public IcingSource getSource(String paramString)
  {
    try
    {
      IcingSource localIcingSource = (IcingSource)Preconditions.checkNotNull(this.mSourceNameToSource.get(paramString));
      return localIcingSource;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  @Nonnull
  public Collection<IcingSource> getSources()
  {
    try
    {
      ImmutableSet localImmutableSet = this.mSourceSet;
      return localImmutableSet;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public void getSuggestions(Query paramQuery, int paramInt, boolean paramBoolean, Consumer<IcingSuggestionsFactory.IcingResults> paramConsumer)
  {
    try
    {
      Preconditions.checkNotNull(paramQuery);
      Preconditions.checkNotNull(paramConsumer);
      this.mSuggestionsFactory.getSuggestions(this.mSourceSet, paramQuery, paramInt, paramBoolean, paramConsumer);
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public void ignoreInternalCorpus(String paramString)
  {
    try
    {
      Preconditions.checkNotNull(paramString, "corpusName cannot be null");
      if (this.mInternalSourceNamesToIgnore.add(paramString))
      {
        Iterator localIterator = this.mSourceNameToSource.values().iterator();
        while (localIterator.hasNext())
        {
          IcingSource localIcingSource = (IcingSource)localIterator.next();
          if (paramString.equals(localIcingSource.getInternalCorpusName()))
          {
            localIterator.remove();
            this.mIgnoredSources.put(localIcingSource.getInternalCorpusName(), localIcingSource);
          }
        }
        maybeUpdateState();
      }
      return;
    }
    finally {}
  }
  
  public void registerDataSetObserver(DataSetObserver paramDataSetObserver)
  {
    this.mObservable.registerObserver(paramDataSetObserver);
  }
  
  public void removeSources(String paramString)
  {
    try
    {
      removePackageName(this.mSourceNameToSource.values().iterator(), paramString);
      removePackageName(this.mIgnoredSources.values().iterator(), paramString);
      maybeUpdateState();
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public void unignoreInternalCorpus(String paramString)
  {
    try
    {
      Preconditions.checkNotNull(paramString, "corpusName cannot be null");
      if (this.mInternalSourceNamesToIgnore.remove(paramString))
      {
        IcingSource localIcingSource = (IcingSource)this.mIgnoredSources.remove(paramString);
        if (localIcingSource != null) {
          this.mSourceNameToSource.put(localIcingSource.getName(), localIcingSource);
        }
        maybeUpdateState();
      }
      return;
    }
    finally {}
  }
  
  public void unregisterDataSetObserver(DataSetObserver paramDataSetObserver)
  {
    this.mObservable.unregisterObserver(paramDataSetObserver);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.summons.icing.IcingSourcesImpl
 * JD-Core Version:    0.7.0.1
 */