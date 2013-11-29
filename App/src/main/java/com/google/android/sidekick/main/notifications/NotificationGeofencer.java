package com.google.android.sidekick.main.notifications;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;
import com.google.android.shared.util.ExtraPreconditions;
import com.google.android.sidekick.main.location.LocationOracle.LightweightGeofencer;
import com.google.android.sidekick.shared.util.Tag;
import com.google.common.base.Preconditions;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.Location;
import com.google.geo.sidekick.Sidekick.Notification;
import com.google.geo.sidekick.Sidekick.TriggerCondition;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;

public class NotificationGeofencer
  implements LocationOracle.LightweightGeofencer
{
  private static final String TAG = Tag.getTag(NotificationGeofencer.class);
  private final Context mAppContext;
  private final NotificationStore mNotificationStore;
  
  public NotificationGeofencer(Context paramContext, NotificationStore paramNotificationStore)
  {
    this.mAppContext = paramContext;
    this.mNotificationStore = paramNotificationStore;
  }
  
  public void onLocationChanged(@Nullable Location paramLocation1, @Nullable Location paramLocation2)
  {
    ExtraPreconditions.checkNotMainThread();
    this.mNotificationStore.initialize();
    GeofenceUpdater localGeofenceUpdater = new GeofenceUpdater(paramLocation2, paramLocation1);
    this.mNotificationStore.updateLocationTriggerConditions(localGeofenceUpdater);
    if (localGeofenceUpdater.hasAffectedNotifications())
    {
      Intent localIntent = NotificationRefreshService.getTriggerIntent(this.mAppContext, localGeofenceUpdater.getTriggeredNotifications(), localGeofenceUpdater.getConcludedNotifications());
      this.mAppContext.startService(localIntent);
    }
  }
  
  static class GeofenceUpdater
    extends NotificationStore.TriggerConditionsUpdater
  {
    private final Location mNewLocation;
    private final Location mPreviousLocation;
    
    public GeofenceUpdater(Location paramLocation1, Location paramLocation2)
    {
      this.mNewLocation = paramLocation1;
      this.mPreviousLocation = paramLocation2;
    }
    
    private boolean isPointInAnyGeofence(List<Sidekick.Location> paramList, Location paramLocation)
    {
      Iterator localIterator = paramList.iterator();
      while (localIterator.hasNext()) {
        if (isPointInGeofence((Sidekick.Location)localIterator.next(), paramLocation)) {
          return true;
        }
      }
      return false;
    }
    
    private boolean isPointInGeofence(Sidekick.Location paramLocation, Location paramLocation1)
    {
      float[] arrayOfFloat = new float[1];
      Location.distanceBetween(paramLocation.getLat(), paramLocation.getLng(), paramLocation1.getLatitude(), paramLocation1.getLongitude(), arrayOfFloat);
      return (arrayOfFloat[0] < paramLocation.getRadiusMeters()) && (paramLocation1.getAccuracy() < 2.0D * paramLocation.getRadiusMeters());
    }
    
    protected boolean areTriggerConditionsSatisfied(Sidekick.Entry paramEntry, boolean paramBoolean)
    {
      boolean bool1;
      if ((paramEntry.hasNotification()) && (paramEntry.getNotification().hasTriggerCondition()) && (paramEntry.getNotification().getTriggerCondition().getLocationCount() > 0))
      {
        bool1 = true;
        Preconditions.checkState(bool1);
        if (paramEntry.getNotification().getTriggerCondition().getConditionCount() <= 0) {
          break label82;
        }
      }
      Sidekick.TriggerCondition localTriggerCondition;
      label82:
      for (boolean bool2 = true;; bool2 = false)
      {
        Preconditions.checkArgument(bool2);
        localTriggerCondition = paramEntry.getNotification().getTriggerCondition();
        if (localTriggerCondition.getConditionCount() <= 1) {
          break label88;
        }
        return paramBoolean;
        bool1 = false;
        break;
      }
      label88:
      int i = localTriggerCondition.getCondition(0);
      boolean bool3;
      switch (i)
      {
      default: 
        Log.w(NotificationGeofencer.TAG, "Unknown trigger condition type: " + i);
        bool3 = paramBoolean;
      }
      label217:
      for (;;)
      {
        return bool3;
        if ((this.mNewLocation != null) && (isPointInAnyGeofence(localTriggerCondition.getLocationList(), this.mNewLocation))) {}
        for (bool3 = true;; bool3 = false)
        {
          if ((!bool3) || (paramBoolean) || (this.mPreviousLocation == null) || (!isPointInAnyGeofence(localTriggerCondition.getLocationList(), this.mPreviousLocation))) {
            break label217;
          }
          bool3 = false;
          break;
        }
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.notifications.NotificationGeofencer
 * JD-Core Version:    0.7.0.1
 */