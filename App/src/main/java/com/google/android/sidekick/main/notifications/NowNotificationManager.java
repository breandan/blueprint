package com.google.android.sidekick.main.notifications;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import com.google.geo.sidekick.Sidekick.Entry;
import java.util.Collection;
import javax.annotation.Nullable;

public abstract interface NowNotificationManager
{
  public abstract void cancelAll();
  
  public abstract void cancelNotification(NotificationType paramNotificationType);
  
  public abstract Notification createNotification(EntryNotification paramEntryNotification, @Nullable PendingIntent paramPendingIntent, boolean paramBoolean);
  
  public abstract void dismissNotification(Collection<Sidekick.Entry> paramCollection, NotificationType paramNotificationType);
  
  public abstract long getLastNotificationTime();
  
  public abstract void sendDeliverActiveNotification(Sidekick.Entry paramEntry);
  
  public abstract void setLastNotificationTime();
  
  public abstract void showNotification(Notification paramNotification, NotificationType paramNotificationType);
  
  public static enum NotificationType
  {
    private final int mNotificationId;
    
    static
    {
      CALENDAR_TIME_TO_LEAVE_NOTIFICATION = new NotificationType("CALENDAR_TIME_TO_LEAVE_NOTIFICATION", 1, 5);
      LOW_PRIORITY_NOTIFICATION = new NotificationType("LOW_PRIORITY_NOTIFICATION", 2, 6);
      PUBLIC_ALERT_NOTIFICATION = new NotificationType("PUBLIC_ALERT_NOTIFICATION", 3, 7);
      RESTAURANT_TIME_TO_LEAVE_NOTIFICATION = new NotificationType("RESTAURANT_TIME_TO_LEAVE_NOTIFICATION", 4, 8);
      EVENT_TIME_TO_LEAVE_NOTIFICATION = new NotificationType("EVENT_TIME_TO_LEAVE_NOTIFICATION", 5, 9);
      FLIGHT_TIME_TO_LEAVE_NOTIFICATION = new NotificationType("FLIGHT_TIME_TO_LEAVE_NOTIFICATION", 6, 10);
      REMINDER_NOTIFICATION = new NotificationType("REMINDER_NOTIFICATION", 7, 11);
      LAST_TRAIN_HOME_NOTIFICATION = new NotificationType("LAST_TRAIN_HOME_NOTIFICATION", 8, 12);
      BARCODE_NOTIFICATION = new NotificationType("BARCODE_NOTIFICATION", 9, 13);
      FLIGHT_STATUS_WARNING_NOTIFICATION = new NotificationType("FLIGHT_STATUS_WARNING_NOTIFICATION", 10, 14);
      NotificationType[] arrayOfNotificationType = new NotificationType[11];
      arrayOfNotificationType[0] = TRAFFIC_NOTIFICATION;
      arrayOfNotificationType[1] = CALENDAR_TIME_TO_LEAVE_NOTIFICATION;
      arrayOfNotificationType[2] = LOW_PRIORITY_NOTIFICATION;
      arrayOfNotificationType[3] = PUBLIC_ALERT_NOTIFICATION;
      arrayOfNotificationType[4] = RESTAURANT_TIME_TO_LEAVE_NOTIFICATION;
      arrayOfNotificationType[5] = EVENT_TIME_TO_LEAVE_NOTIFICATION;
      arrayOfNotificationType[6] = FLIGHT_TIME_TO_LEAVE_NOTIFICATION;
      arrayOfNotificationType[7] = REMINDER_NOTIFICATION;
      arrayOfNotificationType[8] = LAST_TRAIN_HOME_NOTIFICATION;
      arrayOfNotificationType[9] = BARCODE_NOTIFICATION;
      arrayOfNotificationType[10] = FLIGHT_STATUS_WARNING_NOTIFICATION;
      $VALUES = arrayOfNotificationType;
    }
    
    private NotificationType(int paramInt)
    {
      this.mNotificationId = paramInt;
    }
    
    public static NotificationType typeFromIntent(Intent paramIntent)
    {
      int i = paramIntent.getIntExtra("com.google.android.apps.sidekick.FROM_NOTIFICATION", -1);
      NotificationType localNotificationType;
      if (i < 0)
      {
        localNotificationType = null;
        return localNotificationType;
      }
      NotificationType[] arrayOfNotificationType = values();
      int j = arrayOfNotificationType.length;
      for (int k = 0;; k++)
      {
        if (k >= j) {
          break label55;
        }
        localNotificationType = arrayOfNotificationType[k];
        if (localNotificationType.getNotificationId() == i) {
          break;
        }
      }
      label55:
      return null;
    }
    
    public void addToIntent(Intent paramIntent)
    {
      paramIntent.putExtra("com.google.android.apps.sidekick.FROM_NOTIFICATION", getNotificationId());
    }
    
    public int getNotificationId()
    {
      return this.mNotificationId;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.notifications.NowNotificationManager
 * JD-Core Version:    0.7.0.1
 */