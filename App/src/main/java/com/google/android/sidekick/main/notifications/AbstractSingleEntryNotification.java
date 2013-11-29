package com.google.android.sidekick.main.notifications;

import com.google.android.sidekick.shared.util.ProtoUtils;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.Notification;
import java.util.Collection;
import java.util.Iterator;

public abstract class AbstractSingleEntryNotification
  extends AbstractEntryNotification
{
  public AbstractSingleEntryNotification(Sidekick.Entry paramEntry)
  {
    super(ImmutableSet.of(paramEntry));
    Preconditions.checkArgument(paramEntry.hasNotification());
    Preconditions.checkArgument(paramEntry.getNotification().hasType());
  }
  
  String getDefaultNotificationText()
  {
    Sidekick.Notification localNotification = getEntry().getNotification();
    if (localNotification.hasNotificationBarText()) {
      return localNotification.getNotificationBarText();
    }
    return null;
  }
  
  public final Sidekick.Entry getEntry()
  {
    return (Sidekick.Entry)getEntries().iterator().next();
  }
  
  public String getLoggingName()
  {
    String str1 = super.getLoggingName();
    String str2 = ProtoUtils.getGenericEntryType(getEntry());
    if (str2 != null) {
      str1 = str1 + "(" + str2 + ")";
    }
    return str1;
  }
  
  public int getNotificationType()
  {
    return getEntry().getNotification().getType();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.notifications.AbstractSingleEntryNotification
 * JD-Core Version:    0.7.0.1
 */