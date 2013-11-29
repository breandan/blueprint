package com.google.android.voicesearch.fragments.executor;

import com.google.android.voicesearch.fragments.action.SetReminderAction;

public class SetReminderActionExecutor
  implements ActionExecutor<SetReminderAction>
{
  public boolean canExecute(SetReminderAction paramSetReminderAction)
  {
    return true;
  }
  
  public boolean execute(SetReminderAction paramSetReminderAction)
  {
    return false;
  }
  
  public boolean openExternalApp(SetReminderAction paramSetReminderAction)
  {
    return false;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.fragments.executor.SetReminderActionExecutor
 * JD-Core Version:    0.7.0.1
 */