package com.google.android.sidekick.main.entry;

import com.google.android.sidekick.shared.util.ProtoKey;
import com.google.geo.sidekick.Sidekick.Entry;
import java.util.Collection;
import javax.annotation.Nullable;

public class EntryRemover
  extends EntryTreeVisitor
{
  private final Collection<ProtoKey<Sidekick.Entry>> mKeysToRemove;
  @Nullable
  private final EntryProviderObservable mObservable;
  
  public EntryRemover(@Nullable EntryProviderObservable paramEntryProviderObservable, Collection<ProtoKey<Sidekick.Entry>> paramCollection)
  {
    this.mObservable = paramEntryProviderObservable;
    this.mKeysToRemove = paramCollection;
  }
  
  protected boolean shouldRemove(ProtoKey<Sidekick.Entry> paramProtoKey, Sidekick.Entry paramEntry)
  {
    if (this.mKeysToRemove.contains(paramProtoKey))
    {
      if ((shouldNotify()) && (this.mObservable != null)) {
        this.mObservable.notifyEntryDismissed(paramEntry, null);
      }
      return true;
    }
    return false;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.entry.EntryRemover
 * JD-Core Version:    0.7.0.1
 */