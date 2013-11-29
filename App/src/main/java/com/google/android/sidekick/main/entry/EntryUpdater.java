package com.google.android.sidekick.main.entry;

import com.google.android.sidekick.shared.util.ProtoKey;
import com.google.common.base.Function;
import com.google.geo.sidekick.Sidekick.Entry;

public class EntryUpdater
  extends EntryTreeVisitor
{
  private final EntryProviderObservable mObservable;
  private final EntryUpdaterFunc mUpdateFunc;
  
  public EntryUpdater(EntryProviderObservable paramEntryProviderObservable, EntryUpdaterFunc paramEntryUpdaterFunc)
  {
    this.mObservable = paramEntryProviderObservable;
    this.mUpdateFunc = paramEntryUpdaterFunc;
  }
  
  protected void process(ProtoKey<Sidekick.Entry> paramProtoKey, Sidekick.Entry paramEntry)
  {
    Sidekick.Entry localEntry = (Sidekick.Entry)this.mUpdateFunc.apply(paramProtoKey);
    if ((localEntry != null) && (shouldNotify())) {
      this.mObservable.notifyEntryUpdate(localEntry, paramEntry, null);
    }
  }
  
  public static abstract interface EntryUpdaterFunc
    extends Function<ProtoKey<Sidekick.Entry>, Sidekick.Entry>
  {}
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.entry.EntryUpdater
 * JD-Core Version:    0.7.0.1
 */