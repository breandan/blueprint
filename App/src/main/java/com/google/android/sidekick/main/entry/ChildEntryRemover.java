package com.google.android.sidekick.main.entry;

import com.google.android.sidekick.shared.util.ProtoKey;
import com.google.common.collect.Lists;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.EntryTreeNode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;

public class ChildEntryRemover
  extends EntryTreeVisitor
{
  private final ProtoKey<Sidekick.Entry> mGroupNodeKey;
  private final Collection<ProtoKey<Sidekick.Entry>> mKeysToRemove;
  @Nullable
  private final EntryProviderObservable mObservable;
  
  public ChildEntryRemover(@Nullable EntryProviderObservable paramEntryProviderObservable, ProtoKey<Sidekick.Entry> paramProtoKey, Collection<ProtoKey<Sidekick.Entry>> paramCollection)
  {
    this.mObservable = paramEntryProviderObservable;
    this.mGroupNodeKey = paramProtoKey;
    this.mKeysToRemove = paramCollection;
  }
  
  protected void process(Sidekick.EntryTreeNode paramEntryTreeNode)
  {
    if (paramEntryTreeNode.hasGroupEntry())
    {
      Sidekick.Entry localEntry1 = paramEntryTreeNode.getGroupEntry();
      if (new ProtoKey(localEntry1).equals(this.mGroupNodeKey))
      {
        Iterator localIterator = paramEntryTreeNode.getEntryList().iterator();
        ArrayList localArrayList = Lists.newArrayList();
        while (localIterator.hasNext())
        {
          Sidekick.Entry localEntry2 = (Sidekick.Entry)localIterator.next();
          if (this.mKeysToRemove.contains(new ProtoKey(localEntry2)))
          {
            localIterator.remove();
            localArrayList.add(localEntry2);
          }
        }
        if ((localArrayList.size() > 0) && (shouldNotify()) && (this.mObservable != null)) {
          this.mObservable.notifyEntryDismissed(localEntry1, localArrayList);
        }
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.entry.ChildEntryRemover
 * JD-Core Version:    0.7.0.1
 */