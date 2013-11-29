package com.google.android.voicesearch.fragments.executor;

import android.content.Intent;
import com.google.android.shared.util.IntentStarter;
import com.google.android.voicesearch.fragments.action.SetAlarmAction;
import javax.annotation.Nullable;

public class SetAlarmActionExecutor
  extends IntentActionExecutor<SetAlarmAction>
{
  public SetAlarmActionExecutor(IntentStarter paramIntentStarter)
  {
    super(paramIntentStarter);
  }
  
  private Intent getDefaultIntent(SetAlarmAction paramSetAlarmAction, boolean paramBoolean)
  {
    Intent localIntent = new Intent("android.intent.action.SET_ALARM");
    if (paramSetAlarmAction.hasTime())
    {
      localIntent.putExtra("android.intent.extra.alarm.HOUR", paramSetAlarmAction.getHour());
      localIntent.putExtra("android.intent.extra.alarm.MINUTES", paramSetAlarmAction.getMinute());
    }
    String str = paramSetAlarmAction.getLabel();
    if (str != null) {
      localIntent.putExtra("android.intent.extra.alarm.MESSAGE", str);
    }
    if (paramBoolean) {
      localIntent.putExtra("android.intent.extra.alarm.SKIP_UI", true);
    }
    return localIntent;
  }
  
  protected Intent[] getExecuteIntents(SetAlarmAction paramSetAlarmAction)
  {
    Intent[] arrayOfIntent = new Intent[1];
    arrayOfIntent[0] = getDefaultIntent(paramSetAlarmAction, true);
    return arrayOfIntent;
  }
  
  protected Intent[] getOpenExternalAppIntents(SetAlarmAction paramSetAlarmAction)
  {
    Intent[] arrayOfIntent = new Intent[1];
    arrayOfIntent[0] = getDefaultIntent(paramSetAlarmAction, false);
    return arrayOfIntent;
  }
  
  @Nullable
  protected Intent[] getProberIntents(SetAlarmAction paramSetAlarmAction)
  {
    Intent[] arrayOfIntent = new Intent[1];
    arrayOfIntent[0] = new Intent("android.intent.action.SET_ALARM");
    return arrayOfIntent;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.fragments.executor.SetAlarmActionExecutor
 * JD-Core Version:    0.7.0.1
 */