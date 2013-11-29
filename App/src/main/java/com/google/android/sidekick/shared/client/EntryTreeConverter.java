package com.google.android.sidekick.shared.client;

import com.google.android.sidekick.shared.EntryAdapterFactory;
import com.google.android.sidekick.shared.cards.EntryCardViewAdapter;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.EntryTree;
import com.google.geo.sidekick.Sidekick.EntryTreeNode;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;

public class EntryTreeConverter
  implements Function<Sidekick.EntryTree, List<EntryItemStack>>
{
  private final EntryAdapterFactory<EntryCardViewAdapter> mEntryItemFactory;
  
  public EntryTreeConverter(EntryAdapterFactory<EntryCardViewAdapter> paramEntryAdapterFactory)
  {
    this.mEntryItemFactory = paramEntryAdapterFactory;
  }
  
  private void addAdaptersForEntries(Sidekick.EntryTreeNode paramEntryTreeNode, List<EntryItemStack> paramList)
  {
    if (paramEntryTreeNode.hasGroupEntry())
    {
      EntryCardViewAdapter localEntryCardViewAdapter3 = (EntryCardViewAdapter)this.mEntryItemFactory.createForGroup(paramEntryTreeNode);
      if (localEntryCardViewAdapter3 != null) {
        paramList.add(new EntryItemStack(new EntryCardViewAdapter[] { localEntryCardViewAdapter3 }));
      }
    }
    ArrayList localArrayList1;
    do
    {
      do
      {
        ArrayList localArrayList2;
        do
        {
          return;
          if (paramEntryTreeNode.getEntryCount() <= 0) {
            break;
          }
          localArrayList2 = Lists.newArrayList();
          Iterator localIterator2 = paramEntryTreeNode.getEntryList().iterator();
          while (localIterator2.hasNext())
          {
            Sidekick.Entry localEntry = (Sidekick.Entry)localIterator2.next();
            EntryCardViewAdapter localEntryCardViewAdapter2 = (EntryCardViewAdapter)this.mEntryItemFactory.create(localEntry);
            if (localEntryCardViewAdapter2 != null) {
              localArrayList2.add(localEntryCardViewAdapter2);
            }
          }
        } while (localArrayList2.isEmpty());
        paramList.add(new EntryItemStack(localArrayList2));
        return;
      } while (paramEntryTreeNode.getChildCount() <= 0);
      localArrayList1 = Lists.newArrayListWithExpectedSize(paramEntryTreeNode.getChildCount());
      Iterator localIterator1 = paramEntryTreeNode.getChildList().iterator();
      while (localIterator1.hasNext())
      {
        Sidekick.EntryTreeNode localEntryTreeNode = (Sidekick.EntryTreeNode)localIterator1.next();
        if (localEntryTreeNode.hasGroupEntry())
        {
          EntryCardViewAdapter localEntryCardViewAdapter1 = (EntryCardViewAdapter)this.mEntryItemFactory.createForGroup(localEntryTreeNode);
          if (localEntryCardViewAdapter1 != null) {
            localArrayList1.add(localEntryCardViewAdapter1);
          }
        }
      }
    } while (localArrayList1.isEmpty());
    paramList.add(new EntryItemStack(localArrayList1));
  }
  
  public List<EntryItemStack> apply(@Nullable Sidekick.EntryTree paramEntryTree)
  {
    if ((paramEntryTree != null) && (paramEntryTree.hasRoot()))
    {
      Sidekick.EntryTreeNode localEntryTreeNode = paramEntryTree.getRoot();
      if (localEntryTreeNode != null)
      {
        localObject = Lists.newArrayList();
        Iterator localIterator = localEntryTreeNode.getChildList().iterator();
        while (localIterator.hasNext()) {
          addAdaptersForEntries((Sidekick.EntryTreeNode)localIterator.next(), (List)localObject);
        }
      }
    }
    Object localObject = ImmutableList.of();
    return localObject;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.client.EntryTreeConverter
 * JD-Core Version:    0.7.0.1
 */