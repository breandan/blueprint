package com.google.android.sidekick.main.notifications;

import android.app.Notification.InboxStyle;
import android.app.Notification.Style;
import android.content.Context;
import android.content.res.Resources;
import com.google.android.sidekick.shared.remoteapi.CardRenderingContext;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.Notification;
import com.google.geo.sidekick.Sidekick.ReminderEntry;
import java.util.Collection;
import java.util.Iterator;
import javax.annotation.Nullable;

public class MultiReminderNotification
  extends AbstractEntryNotification
{
  public MultiReminderNotification(Collection<Sidekick.Entry> paramCollection)
  {
    super(paramCollection);
  }
  
  public CharSequence getNotificationContentText(Context paramContext, CardRenderingContext paramCardRenderingContext)
  {
    return "";
  }
  
  public CharSequence getNotificationContentTitle(Context paramContext, CardRenderingContext paramCardRenderingContext)
  {
    Resources localResources = paramContext.getResources();
    int i = getEntries().size();
    Object[] arrayOfObject = new Object[1];
    arrayOfObject[0] = Integer.valueOf(getEntries().size());
    return localResources.getQuantityString(2131558436, i, arrayOfObject);
  }
  
  public NowNotificationManager.NotificationType getNotificationId()
  {
    if (isLowPriorityNotification()) {
      return NowNotificationManager.NotificationType.LOW_PRIORITY_NOTIFICATION;
    }
    return NowNotificationManager.NotificationType.REMINDER_NOTIFICATION;
  }
  
  public int getNotificationSmallIcon()
  {
    return 2130838068;
  }
  
  @Nullable
  public Notification.Style getNotificationStyle()
  {
    Notification.InboxStyle localInboxStyle = new Notification.InboxStyle();
    Iterator localIterator = getEntries().iterator();
    while (localIterator.hasNext())
    {
      Sidekick.ReminderEntry localReminderEntry = ((Sidekick.Entry)localIterator.next()).getReminderEntry();
      if ((localReminderEntry != null) && (localReminderEntry.hasReminderMessage())) {
        localInboxStyle.addLine(localReminderEntry.getReminderMessage());
      }
    }
    return localInboxStyle;
  }
  
  public CharSequence getNotificationTickerText(Context paramContext, CardRenderingContext paramCardRenderingContext)
  {
    Iterator localIterator = getEntries().iterator();
    while (localIterator.hasNext())
    {
      Sidekick.ReminderEntry localReminderEntry = ((Sidekick.Entry)localIterator.next()).getReminderEntry();
      if (localReminderEntry != null) {
        return localReminderEntry.getReminderMessage();
      }
    }
    return "";
  }
  
  public int getNotificationType()
  {
    int i = 4;
    int j = 1;
    Iterator localIterator = getEntries().iterator();
    if (localIterator.hasNext())
    {
      k = ((Sidekick.Entry)localIterator.next()).getNotification().getType();
      if (k == 2) {
        i = 2;
      }
    }
    while (j != 0)
    {
      int k;
      return i;
      if (k == i) {
        break;
      }
      j = 0;
      break;
    }
    return 3;
  }
  
  public boolean isActiveNotification()
  {
    Iterator localIterator = getEntries().iterator();
    while (localIterator.hasNext())
    {
      Sidekick.Entry localEntry = (Sidekick.Entry)localIterator.next();
      if ((localEntry.hasNotification()) && (ReminderNotification.isActiveReminder(localEntry.getNotification()))) {
        return true;
      }
    }
    return false;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.notifications.MultiReminderNotification
 * JD-Core Version:    0.7.0.1
 */