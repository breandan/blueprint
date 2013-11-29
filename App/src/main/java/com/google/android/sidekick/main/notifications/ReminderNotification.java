package com.google.android.sidekick.main.notifications;

import android.content.Context;
import android.text.TextUtils;
import com.google.android.shared.util.Clock;
import com.google.android.sidekick.main.actions.ReminderSmartActionUtil;
import com.google.android.sidekick.shared.cards.ReminderEntryAdapter;
import com.google.android.sidekick.shared.remoteapi.CardRenderingContext;
import com.google.android.sidekick.shared.util.ProtoUtils;
import com.google.common.collect.Lists;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.Notification;
import com.google.geo.sidekick.Sidekick.ReminderData;
import com.google.geo.sidekick.Sidekick.ReminderData.SmartActionData;
import com.google.geo.sidekick.Sidekick.ReminderEntry;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ReminderNotification
  extends AbstractSingleEntryNotification
{
  private final Clock mClock;
  private final Sidekick.ReminderEntry mReminder;
  private final ReminderSmartActionUtil mReminderSmartActionUtil;
  
  public ReminderNotification(Sidekick.Entry paramEntry, Clock paramClock, ReminderSmartActionUtil paramReminderSmartActionUtil)
  {
    super(paramEntry);
    this.mReminder = paramEntry.getReminderEntry();
    this.mClock = paramClock;
    this.mReminderSmartActionUtil = paramReminderSmartActionUtil;
  }
  
  public static boolean isActiveReminder(Sidekick.Notification paramNotification)
  {
    return (paramNotification.getType() == 3) || (paramNotification.getType() == 2);
  }
  
  public List<NotificationAction> getActions(CardRenderingContext paramCardRenderingContext)
  {
    Sidekick.Entry localEntry = getEntry();
    ArrayList localArrayList = Lists.newArrayList();
    if (localEntry.hasReminderData())
    {
      if (!TextUtils.isEmpty(localEntry.getReminderData().getSnoozeMessage())) {
        localArrayList.add(new SnoozeReminderAction(getEntry()));
      }
      Iterator localIterator = localEntry.getReminderData().getSmartActionDataList().iterator();
      while (localIterator.hasNext())
      {
        Sidekick.ReminderData.SmartActionData localSmartActionData = (Sidekick.ReminderData.SmartActionData)localIterator.next();
        if ((ProtoUtils.findAction(localEntry, localSmartActionData.getType(), new int[0]) != null) && (this.mReminderSmartActionUtil.isSmartActionSupportedByDevice(localSmartActionData.getType())) && (localSmartActionData.getType() == 148)) {
          localArrayList.add(new ReminderSmartAction(localSmartActionData.getType(), localSmartActionData.getQuery()));
        }
      }
    }
    return localArrayList;
  }
  
  public CharSequence getNotificationContentText(Context paramContext, CardRenderingContext paramCardRenderingContext)
  {
    return ReminderEntryAdapter.getSubtitleMessage(paramContext, this.mReminder, this.mClock.currentTimeMillis());
  }
  
  public CharSequence getNotificationContentTitle(Context paramContext, CardRenderingContext paramCardRenderingContext)
  {
    return this.mReminder.getReminderMessage();
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
  
  public CharSequence getNotificationTickerText(Context paramContext, CardRenderingContext paramCardRenderingContext)
  {
    return this.mReminder.getReminderMessage();
  }
  
  public boolean isActiveNotification()
  {
    return isActiveReminder(getEntry().getNotification());
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.notifications.ReminderNotification
 * JD-Core Version:    0.7.0.1
 */