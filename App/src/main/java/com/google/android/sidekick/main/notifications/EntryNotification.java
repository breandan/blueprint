package com.google.android.sidekick.main.notifications;

import android.app.Notification.Style;
import android.app.PendingIntent;
import android.content.Context;
import com.google.android.sidekick.shared.remoteapi.CardRenderingContext;
import com.google.geo.sidekick.Sidekick.Entry;
import java.util.Collection;
import javax.annotation.Nullable;

public abstract interface EntryNotification
{
  public abstract boolean doNotSuppress();
  
  public abstract Iterable<NotificationAction> getActions(CardRenderingContext paramCardRenderingContext);
  
  public abstract Collection<Sidekick.Entry> getEntries();
  
  public abstract String getLoggingName();
  
  public abstract PendingIntent getNotificationContentIntent(Context paramContext);
  
  public abstract CharSequence getNotificationContentText(Context paramContext, CardRenderingContext paramCardRenderingContext);
  
  @Nullable
  public abstract CharSequence getNotificationContentTitle(Context paramContext, CardRenderingContext paramCardRenderingContext);
  
  public abstract NowNotificationManager.NotificationType getNotificationId();
  
  public abstract int getNotificationSmallIcon();
  
  @Nullable
  public abstract Notification.Style getNotificationStyle();
  
  public abstract CharSequence getNotificationTickerText(Context paramContext, CardRenderingContext paramCardRenderingContext);
  
  public abstract int getNotificationType();
  
  public abstract boolean isActiveNotification();
  
  public abstract boolean isLowPriorityNotification();
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.notifications.EntryNotification
 * JD-Core Version:    0.7.0.1
 */