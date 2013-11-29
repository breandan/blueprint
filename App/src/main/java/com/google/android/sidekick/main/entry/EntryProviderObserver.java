package com.google.android.sidekick.main.entry;

import android.os.Bundle;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.EntryTree;
import java.util.Collection;
import javax.annotation.Nullable;

public abstract interface EntryProviderObserver
{
  public abstract void onEntriesAdded(Sidekick.EntryTree paramEntryTree);
  
  public abstract void onEntryDismissed(Sidekick.Entry paramEntry, @Nullable Collection<Sidekick.Entry> paramCollection);
  
  public abstract void onEntryUpdate(Sidekick.Entry paramEntry1, Sidekick.Entry paramEntry2, Sidekick.Entry paramEntry3);
  
  public abstract void onInvalidated();
  
  public abstract void onRefreshed(Bundle paramBundle);
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.entry.EntryProviderObserver
 * JD-Core Version:    0.7.0.1
 */