package com.google.android.sidekick.shared.client;

import com.google.android.sidekick.shared.cards.EntryCardViewAdapter;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.util.List;

public class EntryItemStack
{
  private final List<EntryCardViewAdapter> mEntries;
  
  public EntryItemStack(List<EntryCardViewAdapter> paramList)
  {
    this.mEntries = Lists.newArrayList(paramList);
  }
  
  public EntryItemStack(EntryCardViewAdapter... paramVarArgs)
  {
    this.mEntries = Lists.newArrayListWithCapacity(paramVarArgs.length);
    int i = paramVarArgs.length;
    for (int j = 0; j < i; j++)
    {
      EntryCardViewAdapter localEntryCardViewAdapter = paramVarArgs[j];
      this.mEntries.add(localEntryCardViewAdapter);
    }
  }
  
  public List<EntryCardViewAdapter> getEntries()
  {
    return ImmutableList.copyOf(this.mEntries);
  }
  
  public List<EntryCardViewAdapter> getEntriesToShow()
  {
    return getEntries();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.client.EntryItemStack
 * JD-Core Version:    0.7.0.1
 */