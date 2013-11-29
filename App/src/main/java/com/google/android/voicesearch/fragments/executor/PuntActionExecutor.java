package com.google.android.voicesearch.fragments.executor;

import android.content.Intent;
import com.google.android.shared.util.IntentStarter;
import com.google.android.voicesearch.fragments.action.PuntAction;

public class PuntActionExecutor
  extends IntentActionExecutor<PuntAction>
{
  public PuntActionExecutor(IntentStarter paramIntentStarter)
  {
    super(paramIntentStarter);
  }
  
  protected Intent[] getExecuteIntents(PuntAction paramPuntAction)
  {
    return new Intent[0];
  }
  
  protected Intent[] getOpenExternalAppIntents(PuntAction paramPuntAction)
  {
    Intent localIntent = paramPuntAction.getIntent();
    if (localIntent != null) {
      return new Intent[] { localIntent };
    }
    return new Intent[0];
  }
  
  protected Intent[] getProberIntents(PuntAction paramPuntAction)
  {
    return new Intent[0];
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.fragments.executor.PuntActionExecutor
 * JD-Core Version:    0.7.0.1
 */