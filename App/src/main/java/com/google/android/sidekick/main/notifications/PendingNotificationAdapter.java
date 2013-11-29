package com.google.android.sidekick.main.notifications;

import com.google.android.apps.sidekick.notifications.Notifications.PendingNotification;
import com.google.android.apps.sidekick.notifications.Notifications.PendingRefresh;
import com.google.android.shared.util.Clock;
import com.google.android.sidekick.shared.util.ProtoKey;
import com.google.android.sidekick.shared.util.Tag;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.Notification;
import com.google.geo.sidekick.Sidekick.TriggerCondition;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;

abstract class PendingNotificationAdapter
{
  private static final PendingNotificationAdapter BASIC_NOTIFICATION = new ConditionlessNotification(null);
  private static final PendingNotificationAdapter LOCATION_NOTIFICATION = new LocationDrivenNotification(null);
  private static final String TAG = Tag.getTag(PendingNotificationAdapter.class);
  private static final PendingNotificationAdapter TIME_NOTIFICATION = new TimeDrivenNotification(null);
  
  @Nullable
  static PendingNotificationAdapter getAdapter(Notifications.PendingNotification paramPendingNotification)
  {
    return getAdapter(getNotification(paramPendingNotification));
  }
  
  @Nullable
  static PendingNotificationAdapter getAdapter(Sidekick.Notification paramNotification)
  {
    PendingNotificationAdapter localPendingNotificationAdapter;
    if (!paramNotification.hasTriggerCondition()) {
      localPendingNotificationAdapter = BASIC_NOTIFICATION;
    }
    boolean bool;
    do
    {
      Sidekick.TriggerCondition localTriggerCondition;
      int k;
      do
      {
        int m;
        do
        {
          int j;
          do
          {
            int i;
            do
            {
              return localPendingNotificationAdapter;
              localTriggerCondition = paramNotification.getTriggerCondition();
              i = localTriggerCondition.getConditionCount();
              localPendingNotificationAdapter = null;
            } while (i > 1);
            j = localTriggerCondition.getConditionCount();
            localPendingNotificationAdapter = null;
          } while (j == 0);
          k = paramNotification.getTriggerCondition().getCondition(0);
          if (k != 7) {
            break;
          }
          m = localTriggerCondition.getLocationCount();
          localPendingNotificationAdapter = null;
        } while (m == 0);
        return LOCATION_NOTIFICATION;
        localPendingNotificationAdapter = null;
      } while (k != 5);
      bool = localTriggerCondition.hasTimeSeconds();
      localPendingNotificationAdapter = null;
    } while (!bool);
    return TIME_NOTIFICATION;
  }
  
  protected static Sidekick.Notification getNotification(Notifications.PendingNotification paramPendingNotification)
  {
    return paramPendingNotification.getEntry().getNotification();
  }
  
  final long getBringDownTimeSeconds(Notifications.PendingNotification paramPendingNotification)
  {
    if (!isCurrentlyShown(paramPendingNotification)) {
      return 9223372036854775807L;
    }
    return getNotificationExpirySeconds(getNotification(paramPendingNotification));
  }
  
  final long getBringUpTimeSeconds(Notifications.PendingNotification paramPendingNotification, long paramLong)
  {
    if (!isWaitingToBeShown(paramPendingNotification, paramLong)) {}
    while (getNotificationExpirySeconds(getNotification(paramPendingNotification)) < paramLong) {
      return 9223372036854775807L;
    }
    return getTriggerTimeSeconds(paramPendingNotification);
  }
  
  protected final long getNotificationExpirySeconds(Sidekick.Notification paramNotification)
  {
    if (paramNotification.hasExpirationTimestampSeconds()) {
      return paramNotification.getExpirationTimestampSeconds();
    }
    return 9223372036854775807L;
  }
  
  protected abstract long getTriggerTimeSeconds(Notifications.PendingNotification paramPendingNotification);
  
  protected boolean isCurrentlyShown(Notifications.PendingNotification paramPendingNotification)
  {
    return (paramPendingNotification.getNotified()) && (!paramPendingNotification.getNotificationDismissed());
  }
  
  final boolean isCurrentlyShownAndValid(Notifications.PendingNotification paramPendingNotification, long paramLong)
  {
    boolean bool1 = isCurrentlyShown(paramPendingNotification);
    boolean bool2 = false;
    if (bool1)
    {
      boolean bool3 = getBringDownTimeSeconds(paramPendingNotification) < paramLong;
      bool2 = false;
      if (bool3) {
        bool2 = true;
      }
    }
    return bool2;
  }
  
  final boolean isWaitingToBeShown(Notifications.PendingNotification paramPendingNotification, long paramLong)
  {
    return (!paramPendingNotification.getNotified()) && (!paramPendingNotification.getNotificationDismissed());
  }
  
  abstract boolean shouldPruneDuringInitialization(Notifications.PendingNotification paramPendingNotification, long paramLong, List<Notifications.PendingRefresh> paramList);
  
  void updateByTriggerConditionsUpdater(Notifications.PendingNotification paramPendingNotification, NotificationStore.TriggerConditionsUpdater paramTriggerConditionsUpdater, Clock paramClock) {}
  
  private static class ConditionlessNotification
    extends PendingNotificationAdapter
  {
    protected long getTriggerTimeSeconds(Notifications.PendingNotification paramPendingNotification)
    {
      return 9223372036854775807L;
    }
    
    boolean shouldPruneDuringInitialization(Notifications.PendingNotification paramPendingNotification, long paramLong, List<Notifications.PendingRefresh> paramList)
    {
      return paramLong - paramPendingNotification.getFirstInsertTimeSeconds() > 86400L;
    }
  }
  
  private static class LocationDrivenNotification
    extends PendingNotificationAdapter
  {
    protected long getTriggerTimeSeconds(Notifications.PendingNotification paramPendingNotification)
    {
      if (paramPendingNotification.hasLastTriggerTimeSeconds()) {
        return paramPendingNotification.getLastTriggerTimeSeconds();
      }
      return 9223372036854775807L;
    }
    
    boolean shouldPruneDuringInitialization(Notifications.PendingNotification paramPendingNotification, long paramLong, List<Notifications.PendingRefresh> paramList)
    {
      ProtoKey localProtoKey = new ProtoKey(paramPendingNotification.getInterest());
      Iterator localIterator = paramList.iterator();
      do
      {
        boolean bool = localIterator.hasNext();
        i = 0;
        if (!bool) {
          break;
        }
      } while (!localProtoKey.equals(new ProtoKey(((Notifications.PendingRefresh)localIterator.next()).getInterest())));
      int i = 1;
      return (i != 0) || (i == 0);
    }
    
    void updateByTriggerConditionsUpdater(Notifications.PendingNotification paramPendingNotification, NotificationStore.TriggerConditionsUpdater paramTriggerConditionsUpdater, Clock paramClock)
    {
      paramTriggerConditionsUpdater.update(paramPendingNotification, paramClock);
    }
  }
  
  private static class TimeDrivenNotification
    extends PendingNotificationAdapter
  {
    protected long getTriggerTimeSeconds(Notifications.PendingNotification paramPendingNotification)
    {
      return getNotification(paramPendingNotification).getTriggerCondition().getTimeSeconds();
    }
    
    boolean shouldPruneDuringInitialization(Notifications.PendingNotification paramPendingNotification, long paramLong, List<Notifications.PendingRefresh> paramList)
    {
      long l = paramLong - 86400L;
      return getTriggerTimeSeconds(paramPendingNotification) < l;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.notifications.PendingNotificationAdapter
 * JD-Core Version:    0.7.0.1
 */