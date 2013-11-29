package com.google.android.sidekick.main.notifications;

import android.app.Notification.InboxStyle;
import android.app.Notification.Style;
import android.content.Context;
import android.text.Html;
import com.google.android.sidekick.shared.remoteapi.CardRenderingContext;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.Location;
import com.google.geo.sidekick.Sidekick.PublicAlertEntry;
import javax.annotation.Nullable;

public class PublicAlertNotification
  extends AbstractSingleEntryNotification
{
  public PublicAlertNotification(Sidekick.Entry paramEntry)
  {
    super(paramEntry);
  }
  
  public CharSequence getNotificationContentText(Context paramContext, CardRenderingContext paramCardRenderingContext)
  {
    Sidekick.PublicAlertEntry localPublicAlertEntry = getEntry().getPublicAlertEntry();
    String str;
    Object localObject;
    if (localPublicAlertEntry.hasLocation())
    {
      str = localPublicAlertEntry.getLocation().getName();
      if (!localPublicAlertEntry.hasPublisher()) {
        break label86;
      }
      localObject = localPublicAlertEntry.getPublisher();
      label37:
      if ((str == null) || (localObject == null)) {
        break label92;
      }
      localObject = Html.fromHtml(str + " &ndash; " + (String)localObject);
    }
    label86:
    label92:
    do
    {
      return localObject;
      str = null;
      break;
      localObject = null;
      break label37;
      if (str != null) {
        return Html.fromHtml("<b>" + str + "</b>");
      }
    } while (localObject != null);
    return null;
  }
  
  public CharSequence getNotificationContentTitle(Context paramContext, CardRenderingContext paramCardRenderingContext)
  {
    return getDefaultNotificationText();
  }
  
  public NowNotificationManager.NotificationType getNotificationId()
  {
    if (isLowPriorityNotification()) {
      return NowNotificationManager.NotificationType.LOW_PRIORITY_NOTIFICATION;
    }
    return NowNotificationManager.NotificationType.PUBLIC_ALERT_NOTIFICATION;
  }
  
  public int getNotificationSmallIcon()
  {
    return 2130838066;
  }
  
  @Nullable
  public Notification.Style getNotificationStyle()
  {
    Sidekick.PublicAlertEntry localPublicAlertEntry = getEntry().getPublicAlertEntry();
    String str1;
    if (localPublicAlertEntry.hasLocation())
    {
      str1 = localPublicAlertEntry.getLocation().getName();
      if (!localPublicAlertEntry.hasPublisher()) {
        break label50;
      }
    }
    label50:
    for (String str2 = localPublicAlertEntry.getPublisher();; str2 = null)
    {
      if ((str1 != null) && (str2 != null)) {
        break label55;
      }
      return null;
      str1 = null;
      break;
    }
    label55:
    Notification.InboxStyle localInboxStyle = new Notification.InboxStyle();
    localInboxStyle.addLine(Html.fromHtml("<b>" + str1 + "</b>"));
    localInboxStyle.addLine(str2);
    return localInboxStyle;
  }
  
  public CharSequence getNotificationTickerText(Context paramContext, CardRenderingContext paramCardRenderingContext)
  {
    return getDefaultNotificationText();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.notifications.PublicAlertNotification
 * JD-Core Version:    0.7.0.1
 */