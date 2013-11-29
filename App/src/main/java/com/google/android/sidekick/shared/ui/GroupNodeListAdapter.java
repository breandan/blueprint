package com.google.android.sidekick.shared.ui;

import android.content.Context;
import android.widget.ArrayAdapter;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.EntryTreeNode;
import java.util.Collection;

public abstract class GroupNodeListAdapter
  extends ArrayAdapter<Sidekick.Entry>
{
  static final int DEFAULT_MAX_ENTRIES = 10;
  private int mEntriesToShow = 0;
  private final Sidekick.EntryTreeNode mEntryTreeNode;
  private int mMaxEntries = 10;
  
  public GroupNodeListAdapter(Context paramContext, Sidekick.EntryTreeNode paramEntryTreeNode)
  {
    super(paramContext, -1, paramEntryTreeNode.getEntryList());
    this.mEntryTreeNode = paramEntryTreeNode;
  }
  
  public void add(Sidekick.Entry paramEntry)
  {
    throw new UnsupportedOperationException();
  }
  
  public void addAll(Collection<? extends Sidekick.Entry> paramCollection)
  {
    throw new UnsupportedOperationException();
  }
  
  public void addAll(Sidekick.Entry... paramVarArgs)
  {
    throw new UnsupportedOperationException();
  }
  
  public int getCount()
  {
    return this.mEntriesToShow;
  }
  
  public Sidekick.Entry getEntry(int paramInt)
  {
    return this.mEntryTreeNode.getEntry(paramInt);
  }
  
  public void insert(Sidekick.Entry paramEntry, int paramInt)
  {
    throw new UnsupportedOperationException();
  }
  
  public void remove(Sidekick.Entry paramEntry)
  {
    throw new UnsupportedOperationException();
  }
  
  public void setEntriesToShow(int paramInt)
  {
    int i = Math.min(Math.min(paramInt, this.mEntryTreeNode.getEntryCount()), this.mMaxEntries);
    if (this.mEntriesToShow != i)
    {
      this.mEntriesToShow = i;
      notifyDataSetChanged();
    }
  }
  
  public void setMaxEntries(int paramInt)
  {
    this.mMaxEntries = paramInt;
    if (this.mEntriesToShow > paramInt) {
      setEntriesToShow(this.mEntriesToShow);
    }
  }
  
  public void showAllEntries()
  {
    setEntriesToShow(this.mEntryTreeNode.getEntryCount());
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.ui.GroupNodeListAdapter
 * JD-Core Version:    0.7.0.1
 */