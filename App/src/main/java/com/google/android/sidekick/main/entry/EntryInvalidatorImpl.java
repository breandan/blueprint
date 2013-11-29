package com.google.android.sidekick.main.entry;

import com.google.android.sidekick.main.notifications.NowNotificationManager;
import com.google.android.sidekick.shared.util.Tag;
import java.util.Locale;

public class EntryInvalidatorImpl
  implements EntryInvalidator
{
  static final long MIN_TIME_BETWEEN_REFRESH_MILLIS = 30000L;
  private static final String TAG = Tag.getTag(EntryInvalidatorImpl.class);
  private final EntryProvider mEntryProvider;
  private final LocationDisabledCardHelper mLocationDisabledCardHelper;
  private final NowNotificationManager mNowNotificationManager;
  
  public EntryInvalidatorImpl(EntryProvider paramEntryProvider, NowNotificationManager paramNowNotificationManager, LocationDisabledCardHelper paramLocationDisabledCardHelper)
  {
    this.mEntryProvider = paramEntryProvider;
    this.mNowNotificationManager = paramNowNotificationManager;
    this.mLocationDisabledCardHelper = paramLocationDisabledCardHelper;
  }
  
  private boolean invalidateIfHasRecentNotification()
  {
    long l1 = this.mNowNotificationManager.getLastNotificationTime();
    long l2 = this.mEntryProvider.getLastRefreshTimeMillis();
    if ((l2 > 0L) && (l1 - l2 > 30000L))
    {
      this.mEntryProvider.invalidate();
      return true;
    }
    return false;
  }
  
  private boolean invalidateIfLocaleChanged()
  {
    if (!this.mEntryProvider.isDataForLocale(Locale.getDefault()))
    {
      this.mEntryProvider.invalidate();
      return true;
    }
    return false;
  }
  
  private boolean invalidateIfLocationServicesStateChanged()
  {
    if (this.mLocationDisabledCardHelper.requiresRefresh())
    {
      this.mEntryProvider.invalidate();
      return true;
    }
    return false;
  }
  
  private boolean invalidateIfLocationVeryFar()
  {
    if (this.mEntryProvider.hasLocationChangedSignificantlySinceRefresh())
    {
      this.mEntryProvider.invalidate();
      return true;
    }
    return false;
  }
  
  public boolean invalidateIfNecessary()
  {
    boolean bool = invalidateIfLocationServicesStateChanged();
    if (!bool) {
      bool = invalidateIfLocationVeryFar();
    }
    if (!bool) {
      bool = invalidateIfLocaleChanged();
    }
    if (!bool) {
      bool = invalidateIfHasRecentNotification();
    }
    if (!bool) {
      bool = this.mEntryProvider.invalidateIfEntriesAreStale();
    }
    return bool;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.entry.EntryInvalidatorImpl
 * JD-Core Version:    0.7.0.1
 */