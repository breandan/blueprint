package com.google.android.sidekick.main.contextprovider;

import com.google.android.sidekick.shared.EntryAdapterFactory;
import com.google.android.sidekick.shared.remoteapi.CardRenderingContext;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.EntryTree;
import com.google.geo.sidekick.Sidekick.EntryTreeNode;
import java.util.Iterator;
import java.util.List;

public class RenderingContextPopulator
{
  private final CardRenderingContextProviders mContextProviders;
  private final EntryAdapterFactory<EntryRenderingContextAdapter> mFactory;
  
  public RenderingContextPopulator(CardRenderingContextProviders paramCardRenderingContextProviders, EntryAdapterFactory<EntryRenderingContextAdapter> paramEntryAdapterFactory)
  {
    this.mContextProviders = paramCardRenderingContextProviders;
    this.mFactory = paramEntryAdapterFactory;
  }
  
  private void populateBranch(CardRenderingContext paramCardRenderingContext, Sidekick.EntryTreeNode paramEntryTreeNode)
  {
    if (paramEntryTreeNode.hasGroupEntry())
    {
      EntryRenderingContextAdapter localEntryRenderingContextAdapter2 = (EntryRenderingContextAdapter)this.mFactory.createForGroup(paramEntryTreeNode);
      if (localEntryRenderingContextAdapter2 != null) {
        localEntryRenderingContextAdapter2.addTypeSpecificRenderingContext(paramCardRenderingContext, this.mContextProviders);
      }
    }
    for (;;)
    {
      return;
      if (paramEntryTreeNode.getEntryCount() > 0)
      {
        Iterator localIterator2 = paramEntryTreeNode.getEntryList().iterator();
        while (localIterator2.hasNext())
        {
          Sidekick.Entry localEntry = (Sidekick.Entry)localIterator2.next();
          EntryRenderingContextAdapter localEntryRenderingContextAdapter1 = (EntryRenderingContextAdapter)this.mFactory.create(localEntry);
          if (localEntryRenderingContextAdapter1 != null) {
            localEntryRenderingContextAdapter1.addTypeSpecificRenderingContext(paramCardRenderingContext, this.mContextProviders);
          }
        }
      }
      else if (paramEntryTreeNode.getChildCount() > 0)
      {
        Iterator localIterator1 = paramEntryTreeNode.getChildList().iterator();
        while (localIterator1.hasNext()) {
          populateBranch(paramCardRenderingContext, (Sidekick.EntryTreeNode)localIterator1.next());
        }
      }
    }
  }
  
  public void populate(CardRenderingContext paramCardRenderingContext, Sidekick.EntryTree paramEntryTree)
  {
    if (paramEntryTree.hasRoot())
    {
      Iterator localIterator = paramEntryTree.getRoot().getChildList().iterator();
      while (localIterator.hasNext()) {
        populateBranch(paramCardRenderingContext, (Sidekick.EntryTreeNode)localIterator.next());
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.contextprovider.RenderingContextPopulator
 * JD-Core Version:    0.7.0.1
 */