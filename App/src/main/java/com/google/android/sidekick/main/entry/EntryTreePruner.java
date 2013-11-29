package com.google.android.sidekick.main.entry;

import com.google.android.shared.util.ProtoUtils;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.EntryResponse;
import com.google.geo.sidekick.Sidekick.EntryTree;
import com.google.geo.sidekick.Sidekick.EntryTreeNode;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;

public class EntryTreePruner
{
  private final EntryValidator mEntryValidator;
  
  public EntryTreePruner(EntryValidator paramEntryValidator)
  {
    this.mEntryValidator = paramEntryValidator;
  }
  
  private void prune(Sidekick.EntryTreeNode paramEntryTreeNode)
  {
    Iterator localIterator1 = paramEntryTreeNode.getChildList().iterator();
    while (localIterator1.hasNext())
    {
      Sidekick.EntryTreeNode localEntryTreeNode = (Sidekick.EntryTreeNode)localIterator1.next();
      if (localEntryTreeNode.hasGroupEntry())
      {
        if (!this.mEntryValidator.validateGroup(localEntryTreeNode)) {
          localIterator1.remove();
        }
      }
      else
      {
        Iterator localIterator2 = localEntryTreeNode.getEntryList().iterator();
        label133:
        while (localIterator2.hasNext())
        {
          Sidekick.Entry localEntry = (Sidekick.Entry)localIterator2.next();
          if (localEntry.getType() != 73) {}
          for (int i = 1;; i = 0)
          {
            if ((i == 0) || (this.mEntryValidator.validate(localEntry))) {
              break label133;
            }
            localIterator2.remove();
            break;
          }
        }
        prune(localEntryTreeNode);
        if ((localEntryTreeNode.getEntryList().isEmpty()) && (localEntryTreeNode.getChildList().isEmpty())) {
          localIterator1.remove();
        }
      }
    }
  }
  
  public Sidekick.EntryTree copyAndPrune(Sidekick.EntryTree paramEntryTree)
  {
    Sidekick.EntryTree localEntryTree = (Sidekick.EntryTree)ProtoUtils.copyOf(paramEntryTree);
    if (localEntryTree.hasRoot()) {
      prune(localEntryTree.getRoot());
    }
    return localEntryTree;
  }
  
  public void prune(@Nullable Sidekick.EntryResponse paramEntryResponse)
  {
    if ((paramEntryResponse == null) || (paramEntryResponse.getEntryTreeCount() == 0) || (!paramEntryResponse.getEntryTree(0).hasRoot())) {
      return;
    }
    prune(paramEntryResponse.getEntryTree(0).getRoot());
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.entry.EntryTreePruner
 * JD-Core Version:    0.7.0.1
 */