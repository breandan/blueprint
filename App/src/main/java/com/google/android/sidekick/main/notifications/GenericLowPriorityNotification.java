package com.google.android.sidekick.main.notifications;

import android.content.Context;
import com.google.android.sidekick.shared.remoteapi.CardRenderingContext;
import com.google.geo.sidekick.Sidekick.Entry;

public class GenericLowPriorityNotification
  extends AbstractSingleEntryNotification
{
  private final int mIcon;
  
  public GenericLowPriorityNotification(Sidekick.Entry paramEntry, int paramInt)
  {
    super(paramEntry);
    this.mIcon = paramInt;
  }
  
  public CharSequence getNotificationContentText(Context paramContext, CardRenderingContext paramCardRenderingContext)
  {
    return null;
  }
  
  public CharSequence getNotificationContentTitle(Context paramContext, CardRenderingContext paramCardRenderingContext)
  {
    return getDefaultNotificationText();
  }
  
  public NowNotificationManager.NotificationType getNotificationId()
  {
    return NowNotificationManager.NotificationType.LOW_PRIORITY_NOTIFICATION;
  }
  
  public int getNotificationSmallIcon()
  {
    return this.mIcon;
  }
  
  public CharSequence getNotificationTickerText(Context paramContext, CardRenderingContext paramCardRenderingContext)
  {
    return null;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.notifications.GenericLowPriorityNotification
 * JD-Core Version:    0.7.0.1
 */