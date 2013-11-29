package com.google.android.sidekick.main.notifications;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.ReminderData;

public class SnoozeReminderAction
  implements NotificationAction
{
  private final Sidekick.Entry mEntry;
  
  public SnoozeReminderAction(Sidekick.Entry paramEntry)
  {
    this.mEntry = ((Sidekick.Entry)Preconditions.checkNotNull(paramEntry));
  }
  
  public int getActionIcon()
  {
    return 2130837904;
  }
  
  public String getActionString(Context paramContext)
  {
    if ((this.mEntry.hasReminderData()) && (this.mEntry.getReminderData().hasSnoozeMessage()))
    {
      String str = this.mEntry.getReminderData().getSnoozeMessage();
      if (!TextUtils.isEmpty(str)) {
        return str;
      }
    }
    return paramContext.getString(2131362724);
  }
  
  public Intent getCallbackIntent(Context paramContext)
  {
    Intent localIntent = NotificationRefreshService.getDeleteNotificationIntent(paramContext, ImmutableList.of(this.mEntry));
    localIntent.putExtra("actions_to_execute", 34);
    localIntent.putExtra("invalidate_after_action", true);
    return localIntent;
  }
  
  public String getCallbackType()
  {
    return "service";
  }
  
  public String getLogString()
  {
    return "SNOOZE_REMINDER";
  }
  
  public boolean isActive()
  {
    return true;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.notifications.SnoozeReminderAction
 * JD-Core Version:    0.7.0.1
 */