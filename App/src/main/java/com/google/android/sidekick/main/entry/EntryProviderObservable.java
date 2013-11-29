package com.google.android.sidekick.main.entry;

import android.content.Context;
import android.content.Intent;
import android.database.Observable;
import android.os.Bundle;
import com.google.android.sidekick.shared.util.ProtoUtils;
import com.google.common.collect.Lists;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.EntryTree;
import java.util.ArrayList;
import java.util.Collection;
import javax.annotation.Nullable;

public class EntryProviderObservable
  extends Observable<EntryProviderObserver>
{
  private static final Collection<Integer> PUBLIC_BROADCAST_TYPES;
  private final Context mAppContext;
  
  static
  {
    Integer[] arrayOfInteger = new Integer[3];
    arrayOfInteger[0] = Integer.valueOf(0);
    arrayOfInteger[1] = Integer.valueOf(1);
    arrayOfInteger[2] = Integer.valueOf(2);
    PUBLIC_BROADCAST_TYPES = Lists.newArrayList(arrayOfInteger);
  }
  
  public EntryProviderObservable(Context paramContext)
  {
    this.mAppContext = paramContext;
  }
  
  private void broadcast(int paramInt, @Nullable Bundle paramBundle)
  {
    broadcast(createIntent(paramInt, null, paramBundle));
  }
  
  private void broadcast(Intent paramIntent)
  {
    this.mAppContext.sendBroadcast(paramIntent);
    int i = paramIntent.getIntExtra("type", -1);
    if (PUBLIC_BROADCAST_TYPES.contains(Integer.valueOf(i)))
    {
      Intent localIntent = new Intent("com.google.android.apps.now.cards_remote_broadcast");
      localIntent.putExtra("type", i);
      this.mAppContext.sendBroadcast(localIntent, "com.google.android.apps.now.REMOTE_ACCESS");
    }
  }
  
  private Intent createIntent(int paramInt)
  {
    return createIntent(paramInt, null, null);
  }
  
  private Intent createIntent(int paramInt, @Nullable Sidekick.Entry paramEntry, @Nullable Bundle paramBundle)
  {
    Intent localIntent = new Intent("com.google.android.apps.now.ENTRIES_UPDATED");
    if (paramBundle != null) {
      localIntent.putExtras(paramBundle);
    }
    localIntent.putExtra("type", paramInt);
    putEntry(localIntent, "entry", paramEntry);
    localIntent.setPackage(this.mAppContext.getPackageName());
    return localIntent;
  }
  
  private void putEntry(Intent paramIntent, String paramString, @Nullable Sidekick.Entry paramEntry)
  {
    if (paramEntry != null) {
      paramIntent.putExtra(paramString, paramEntry.toByteArray());
    }
  }
  
  public void notifyEntriesAdded(Sidekick.EntryTree paramEntryTree)
  {
    synchronized (this.mObservers)
    {
      for (int i = -1 + this.mObservers.size(); i >= 0; i--) {
        ((EntryProviderObserver)this.mObservers.get(i)).onEntriesAdded(paramEntryTree);
      }
      broadcast(2, null);
      return;
    }
  }
  
  public void notifyEntryDismissed(Sidekick.Entry paramEntry, @Nullable Collection<Sidekick.Entry> paramCollection)
  {
    synchronized (this.mObservers)
    {
      for (int i = -1 + this.mObservers.size(); i >= 0; i--) {
        ((EntryProviderObserver)this.mObservers.get(i)).onEntryDismissed(paramEntry, paramCollection);
      }
      Intent localIntent = createIntent(5, paramEntry, null);
      if (paramCollection != null) {
        ProtoUtils.putEntriesInIntent(localIntent, "child_entries", paramCollection);
      }
      broadcast(localIntent);
      return;
    }
  }
  
  public void notifyEntryUpdate(Sidekick.Entry paramEntry1, Sidekick.Entry paramEntry2, @Nullable Sidekick.Entry paramEntry3)
  {
    synchronized (this.mObservers)
    {
      for (int i = -1 + this.mObservers.size(); i >= 0; i--) {
        ((EntryProviderObserver)this.mObservers.get(i)).onEntryUpdate(paramEntry1, paramEntry2, paramEntry3);
      }
      Intent localIntent = createIntent(4, paramEntry1, null);
      putEntry(localIntent, "updated_entry", paramEntry2);
      putEntry(localIntent, "entry_change", paramEntry3);
      broadcast(localIntent);
      return;
    }
  }
  
  public void notifyGoogleNowDisabled(int paramInt)
  {
    Intent localIntent = createIntent(7);
    localIntent.putExtra("disabled_reason", paramInt);
    broadcast(localIntent);
  }
  
  public void notifyInvalidated()
  {
    synchronized (this.mObservers)
    {
      for (int i = -1 + this.mObservers.size(); i >= 0; i--) {
        ((EntryProviderObserver)this.mObservers.get(i)).onInvalidated();
      }
      broadcast(1, null);
      return;
    }
  }
  
  public void notifyRefreshFailed(int paramInt, boolean paramBoolean)
  {
    Intent localIntent = createIntent(3);
    localIntent.putExtra("refresh_type", paramInt);
    localIntent.putExtra("refresh_error_auth", paramBoolean);
    broadcast(localIntent);
  }
  
  public void notifyRefreshStarting(int paramInt)
  {
    Intent localIntent = createIntent(6);
    localIntent.putExtra("refresh_type", paramInt);
    broadcast(localIntent);
  }
  
  public void notifyRefreshed(Bundle paramBundle, int paramInt)
  {
    synchronized (this.mObservers)
    {
      for (int i = -1 + this.mObservers.size(); i >= 0; i--) {
        ((EntryProviderObserver)this.mObservers.get(i)).onRefreshed(paramBundle);
      }
      Intent localIntent = createIntent(0, null, paramBundle);
      localIntent.putExtra("refresh_type", paramInt);
      broadcast(localIntent);
      return;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.entry.EntryProviderObservable
 * JD-Core Version:    0.7.0.1
 */