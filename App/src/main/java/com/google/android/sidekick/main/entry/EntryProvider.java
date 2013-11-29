package com.google.android.sidekick.main.entry;

import android.location.Location;
import com.google.android.sidekick.main.inject.BackgroundImage;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.EntryChanges;
import com.google.geo.sidekick.Sidekick.EntryResponse;
import com.google.geo.sidekick.Sidekick.EntryTree;
import com.google.geo.sidekick.Sidekick.Location;
import com.google.protobuf.micro.ByteStringMicro;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import javax.annotation.Nullable;

public abstract interface EntryProvider
{
  public abstract void appendMoreCardEntries(Sidekick.EntryResponse paramEntryResponse, Location paramLocation);
  
  public abstract void cancelDelayedRefresh();
  
  public abstract boolean entriesIncludeMore();
  
  public abstract List<BackgroundImage> getBackgroundImagePhotos();
  
  @Nullable
  public abstract Sidekick.EntryTree getEntryTree();
  
  public abstract long getLastChangeTimeMillis();
  
  @Nullable
  public abstract Sidekick.Location getLastRefreshLocation();
  
  public abstract long getLastRefreshTimeMillis();
  
  public abstract int getTotalEntryCount();
  
  public abstract void handleDismissedEntries(Collection<Sidekick.Entry> paramCollection);
  
  public abstract boolean hasLocationChangedSignificantlySinceRefresh();
  
  public abstract boolean hasPendingRefresh();
  
  public abstract void initializeFromStorage();
  
  public abstract void invalidate();
  
  public abstract boolean invalidateIfEntriesAreStale();
  
  public abstract void invalidateWithDelayedRefresh();
  
  public abstract void invalidateWithImmediateRefresh();
  
  public abstract boolean isDataForLocale(@Nullable Locale paramLocale);
  
  public abstract boolean isInitializedFromStorage();
  
  public abstract void notifyAboutGoogleNowDisabled(int paramInt);
  
  public abstract void notifyAboutRefreshFailure(int paramInt, boolean paramBoolean);
  
  public abstract void notifyAboutRefreshStarting(int paramInt);
  
  public abstract void refreshEntriesPreserveMoreState();
  
  public abstract void refreshNowIfDelayedRefreshInFlight();
  
  public abstract void registerEntryProviderObserver(EntryProviderObserver paramEntryProviderObserver);
  
  public abstract void removeGroupChildEntries(Sidekick.Entry paramEntry, Collection<Sidekick.Entry> paramCollection);
  
  public abstract void updateEntries(EntryUpdater.EntryUpdaterFunc paramEntryUpdaterFunc);
  
  public abstract void updateFromEntryResponse(Sidekick.EntryResponse paramEntryResponse, int paramInt, Location paramLocation, @Nullable Locale paramLocale, boolean paramBoolean, @Nullable ByteStringMicro paramByteStringMicro);
  
  public abstract void updateFromPartialEntries(Sidekick.EntryChanges paramEntryChanges);
  
  public abstract void updateFromPartialEntryResponse(Sidekick.EntryResponse paramEntryResponse);
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.entry.EntryProvider
 * JD-Core Version:    0.7.0.1
 */