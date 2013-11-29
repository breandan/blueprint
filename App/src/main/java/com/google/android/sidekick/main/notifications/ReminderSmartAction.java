package com.google.android.sidekick.main.notifications;

import android.content.Context;
import android.content.Intent;
import com.google.android.search.shared.api.Query;
import com.google.android.velvet.util.IntentUtils;

public class ReminderSmartAction
  implements NotificationAction
{
  private final int mActionType;
  private final String mQuery;
  
  public ReminderSmartAction(int paramInt, String paramString)
  {
    this.mActionType = paramInt;
    this.mQuery = paramString;
  }
  
  public int getActionIcon()
  {
    switch (this.mActionType)
    {
    default: 
      throw new IllegalStateException("Invalid action type: " + this.mActionType);
    }
    return 2130837638;
  }
  
  public String getActionString(Context paramContext)
  {
    switch (this.mActionType)
    {
    default: 
      throw new IllegalStateException("Invalid action type: " + this.mActionType);
    }
    return paramContext.getString(2131363607);
  }
  
  public Intent getCallbackIntent(Context paramContext)
  {
    return IntentUtils.createResumeVelvetWithQueryIntent(paramContext, Query.EMPTY.withQueryChars(this.mQuery));
  }
  
  public String getCallbackType()
  {
    return "activity";
  }
  
  public String getLogString()
  {
    return "REMINDER_SMART_ACTION_" + this.mActionType;
  }
  
  public boolean isActive()
  {
    return true;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.notifications.ReminderSmartAction
 * JD-Core Version:    0.7.0.1
 */