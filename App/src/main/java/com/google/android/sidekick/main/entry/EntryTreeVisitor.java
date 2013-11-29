package com.google.android.sidekick.main.entry;

import com.google.android.sidekick.shared.util.ProtoKey;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.EntryTreeNode;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;

public abstract class EntryTreeVisitor
{
  private boolean mShouldNotify = false;
  private boolean mVisited = false;
  
  private void visitInternal(@Nullable Sidekick.EntryTreeNode paramEntryTreeNode)
  {
    if (paramEntryTreeNode == null) {}
    for (;;)
    {
      return;
      Iterator localIterator1 = paramEntryTreeNode.getChildList().iterator();
      while (localIterator1.hasNext())
      {
        Sidekick.EntryTreeNode localEntryTreeNode = (Sidekick.EntryTreeNode)localIterator1.next();
        if (shouldRemove(new ProtoKey(localEntryTreeNode.getGroupEntry()), localEntryTreeNode.getGroupEntry()))
        {
          localIterator1.remove();
        }
        else
        {
          process(localEntryTreeNode);
          visitInternal(localEntryTreeNode);
        }
      }
      Iterator localIterator2 = paramEntryTreeNode.getEntryList().iterator();
      while (localIterator2.hasNext())
      {
        Sidekick.Entry localEntry = (Sidekick.Entry)localIterator2.next();
        ProtoKey localProtoKey = new ProtoKey(localEntry);
        if (shouldRemove(localProtoKey, localEntry)) {
          localIterator2.remove();
        } else {
          process(localProtoKey, localEntry);
        }
      }
    }
  }
  
  public boolean isVisited()
  {
    return this.mVisited;
  }
  
  protected void process(ProtoKey<Sidekick.Entry> paramProtoKey, Sidekick.Entry paramEntry) {}
  
  protected void process(Sidekick.EntryTreeNode paramEntryTreeNode) {}
  
  protected void processRoot(@Nullable Sidekick.EntryTreeNode paramEntryTreeNode)
  {
    process(paramEntryTreeNode);
  }
  
  public boolean shouldNotify()
  {
    return this.mShouldNotify;
  }
  
  protected boolean shouldRemove(ProtoKey<Sidekick.Entry> paramProtoKey, Sidekick.Entry paramEntry)
  {
    return false;
  }
  
  public final void visit(@Nullable Sidekick.EntryTreeNode paramEntryTreeNode)
  {
    this.mVisited = true;
    processRoot(paramEntryTreeNode);
    visitInternal(paramEntryTreeNode);
  }
  
  public final void visitWithNotifying(@Nullable Sidekick.EntryTreeNode paramEntryTreeNode)
  {
    this.mShouldNotify = true;
    visit(paramEntryTreeNode);
  }
  
  public final void visitWithoutNotifying(@Nullable Sidekick.EntryTreeNode paramEntryTreeNode)
  {
    this.mShouldNotify = false;
    visit(paramEntryTreeNode);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.entry.EntryTreeVisitor
 * JD-Core Version:    0.7.0.1
 */