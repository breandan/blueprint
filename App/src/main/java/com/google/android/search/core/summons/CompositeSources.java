package com.google.android.search.core.summons;

import android.database.DataSetObservable;
import android.database.DataSetObserver;
import com.google.android.search.core.debug.DumpUtils;
import com.google.android.search.core.summons.icing.IcingSource;
import com.google.android.search.core.summons.icing.IcingSources;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nonnull;

public class CompositeSources
  implements Sources<Source>
{
  private ImmutableMap<String, Source> mAllCurrentSources;
  private final DedupedContentProviderSources mExternalContentProviderSources;
  private final IcingSources mIcingSources;
  private final SearchableSources mInternalSearchableSources;
  private final DataSetObservable mObservable;
  
  public CompositeSources(SearchableSources paramSearchableSources, IcingSources paramIcingSources)
  {
    this.mInternalSearchableSources = paramSearchableSources;
    this.mIcingSources = paramIcingSources;
    this.mExternalContentProviderSources = new DedupedContentProviderSources();
    this.mObservable = new DataSetObservable();
    this.mAllCurrentSources = ImmutableMap.of();
    SourcesChangedObserver localSourcesChangedObserver = new SourcesChangedObserver(null);
    if (this.mInternalSearchableSources != null) {
      this.mInternalSearchableSources.registerDataSetObserver(localSourcesChangedObserver);
    }
    this.mIcingSources.registerDataSetObserver(localSourcesChangedObserver);
    updateSnapshots();
  }
  
  public boolean containsSource(String paramString)
  {
    try
    {
      boolean bool = this.mAllCurrentSources.containsKey(paramString);
      return bool;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public void dump(String paramString, PrintWriter paramPrintWriter)
  {
    DumpUtils.println(paramPrintWriter, new Object[] { paramString, "CompositeSources state:" });
    String str = paramString + "  ";
    Object[] arrayOfObject = new Object[3];
    arrayOfObject[0] = str;
    arrayOfObject[1] = "IcingSources: ";
    arrayOfObject[2] = this.mIcingSources.getSources();
    DumpUtils.println(paramPrintWriter, arrayOfObject);
    if (this.mInternalSearchableSources == null)
    {
      DumpUtils.println(paramPrintWriter, new Object[] { str, "CP Sources: disabled" });
      return;
    }
    HashSet localHashSet1 = new HashSet(this.mExternalContentProviderSources.getSources());
    HashSet localHashSet2 = new HashSet(this.mInternalSearchableSources.getSources());
    localHashSet2.removeAll(localHashSet1);
    DumpUtils.println(paramPrintWriter, new Object[] { str, "Visible CP sources: ", localHashSet1 });
    DumpUtils.println(paramPrintWriter, new Object[] { str, "Ignored CP sources: ", localHashSet2 });
  }
  
  public Sources<ContentProviderSource> getContentProviderSources()
  {
    return this.mExternalContentProviderSources;
  }
  
  public IcingSources getIcingSources()
  {
    return this.mIcingSources;
  }
  
  @Nonnull
  public Source getSource(String paramString)
  {
    Source localSource;
    try
    {
      localSource = (Source)this.mAllCurrentSources.get(paramString);
      if (localSource == null) {
        throw new NullPointerException("source " + paramString + " does not exist.");
      }
    }
    finally {}
    return localSource;
  }
  
  public Collection<Source> getSources()
  {
    try
    {
      ImmutableCollection localImmutableCollection = this.mAllCurrentSources.values();
      return localImmutableCollection;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public void registerDataSetObserver(DataSetObserver paramDataSetObserver)
  {
    this.mObservable.registerObserver(paramDataSetObserver);
  }
  
  public void unregisterDataSetObserver(DataSetObserver paramDataSetObserver)
  {
    this.mObservable.unregisterObserver(paramDataSetObserver);
  }
  
  void updateSnapshots()
  {
    Collection localCollection = this.mIcingSources.getSources();
    HashSet localHashSet = new HashSet();
    Iterator localIterator1 = localCollection.iterator();
    while (localIterator1.hasNext()) {
      localHashSet.add(((IcingSource)localIterator1.next()).getCanonicalName());
    }
    ArrayList localArrayList = new ArrayList();
    if (this.mInternalSearchableSources != null)
    {
      Iterator localIterator4 = this.mInternalSearchableSources.getSources().iterator();
      while (localIterator4.hasNext())
      {
        ContentProviderSource localContentProviderSource2 = (ContentProviderSource)localIterator4.next();
        if (!localHashSet.contains(localContentProviderSource2.getCanonicalName())) {
          localArrayList.add(localContentProviderSource2);
        }
      }
      this.mExternalContentProviderSources.setCurrentSources(localArrayList);
    }
    ImmutableMap.Builder localBuilder = ImmutableMap.builder();
    Iterator localIterator2 = localCollection.iterator();
    while (localIterator2.hasNext())
    {
      IcingSource localIcingSource = (IcingSource)localIterator2.next();
      localBuilder.put(localIcingSource.getName(), localIcingSource);
    }
    Iterator localIterator3 = localArrayList.iterator();
    while (localIterator3.hasNext())
    {
      ContentProviderSource localContentProviderSource1 = (ContentProviderSource)localIterator3.next();
      localBuilder.put(localContentProviderSource1.getName(), localContentProviderSource1);
    }
    try
    {
      ImmutableMap localImmutableMap = localBuilder.build();
      boolean bool = this.mAllCurrentSources.equals(localImmutableMap);
      int i = 0;
      if (!bool)
      {
        this.mAllCurrentSources = localImmutableMap;
        i = 1;
      }
      if (i != 0) {
        this.mObservable.notifyChanged();
      }
      return;
    }
    finally {}
  }
  
  private static final class DedupedContentProviderSources
    implements Sources<ContentProviderSource>
  {
    private final Map<String, ContentProviderSource> mCurrentSources = new HashMap();
    private final DataSetObservable mObservable = new DataSetObservable();
    
    public boolean containsSource(String paramString)
    {
      synchronized (this.mCurrentSources)
      {
        boolean bool = this.mCurrentSources.containsKey(paramString);
        return bool;
      }
    }
    
    public ContentProviderSource getSource(String paramString)
    {
      synchronized (this.mCurrentSources)
      {
        ContentProviderSource localContentProviderSource = (ContentProviderSource)this.mCurrentSources.get(paramString);
        return localContentProviderSource;
      }
    }
    
    public Collection<ContentProviderSource> getSources()
    {
      synchronized (this.mCurrentSources)
      {
        ImmutableList localImmutableList = ImmutableList.copyOf(this.mCurrentSources.values());
        return localImmutableList;
      }
    }
    
    public void registerDataSetObserver(DataSetObserver paramDataSetObserver)
    {
      this.mObservable.registerObserver(paramDataSetObserver);
    }
    
    public void setCurrentSources(Collection<ContentProviderSource> paramCollection)
    {
      synchronized (this.mCurrentSources)
      {
        this.mCurrentSources.clear();
        Iterator localIterator = paramCollection.iterator();
        if (localIterator.hasNext())
        {
          ContentProviderSource localContentProviderSource = (ContentProviderSource)localIterator.next();
          this.mCurrentSources.put(localContentProviderSource.getName(), localContentProviderSource);
        }
      }
      this.mObservable.notifyChanged();
    }
    
    public void unregisterDataSetObserver(DataSetObserver paramDataSetObserver)
    {
      this.mObservable.unregisterObserver(paramDataSetObserver);
    }
  }
  
  private final class SourcesChangedObserver
    extends DataSetObserver
  {
    private SourcesChangedObserver() {}
    
    public void onChanged()
    {
      CompositeSources.this.updateSnapshots();
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.summons.CompositeSources
 * JD-Core Version:    0.7.0.1
 */