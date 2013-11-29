package com.google.android.sidekick.main.notifications;

import android.app.Notification.Style;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import com.google.android.googlequicksearchbox.SearchActivity;
import com.google.android.shared.util.Util;
import com.google.android.sidekick.shared.remoteapi.CardRenderingContext;
import com.google.android.sidekick.shared.util.ProtoUtils;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.Notification;
import java.util.Collection;
import java.util.Iterator;
import javax.annotation.Nullable;

public abstract class AbstractEntryNotification
  implements EntryNotification
{
  private final Collection<Sidekick.Entry> mEntries;
  
  public AbstractEntryNotification(Collection<Sidekick.Entry> paramCollection)
  {
    Preconditions.checkNotNull(paramCollection);
    if (!paramCollection.isEmpty()) {}
    for (boolean bool = true;; bool = false)
    {
      Preconditions.checkState(bool);
      this.mEntries = paramCollection;
      return;
    }
  }
  
  public boolean doNotSuppress()
  {
    Iterator localIterator = getEntries().iterator();
    while (localIterator.hasNext()) {
      if (((Sidekick.Entry)localIterator.next()).getNotification().getDoNotSuppress()) {
        return true;
      }
    }
    return false;
  }
  
  public Iterable<NotificationAction> getActions(CardRenderingContext paramCardRenderingContext)
  {
    return ImmutableList.of();
  }
  
  public final Collection<Sidekick.Entry> getEntries()
  {
    return this.mEntries;
  }
  
  public String getLoggingName()
  {
    String str = getClass().getName();
    return Util.removeTrailingSuffix(str.substring(1 + str.lastIndexOf('.')), "Notification");
  }
  
  public PendingIntent getNotificationContentIntent(Context paramContext)
  {
    Intent localIntent = new Intent(paramContext, SearchActivity.class);
    ProtoUtils.putEntriesInIntent(localIntent, "notificationEntriesKey", getEntries());
    localIntent.setAction("android.intent.action.ASSIST");
    localIntent.setData(Uri.parse("notification_content://" + getNotificationId()));
    getNotificationId().addToIntent(localIntent);
    localIntent.addFlags(268435456);
    return PendingIntent.getActivity(paramContext, 0, localIntent, 134217728);
  }
  
  @Nullable
  public Notification.Style getNotificationStyle()
  {
    return null;
  }
  
  public boolean isActiveNotification()
  {
    return getNotificationType() == 2;
  }
  
  public boolean isLowPriorityNotification()
  {
    return getNotificationType() == 4;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.notifications.AbstractEntryNotification
 * JD-Core Version:    0.7.0.1
 */