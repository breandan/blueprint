package com.google.android.sidekick.shared;

import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.EntryTreeNode;
import javax.annotation.Nullable;

public abstract interface EntryAdapterFactory<T>
{
  @Nullable
  public abstract T create(Sidekick.Entry paramEntry);
  
  @Nullable
  public abstract T createForGroup(Sidekick.EntryTreeNode paramEntryTreeNode);
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.EntryAdapterFactory
 * JD-Core Version:    0.7.0.1
 */