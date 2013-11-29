package com.google.android.voicesearch.fragments.executor;

import android.content.Intent;
import com.google.android.shared.util.IntentStarter;
import com.google.android.voicesearch.fragments.action.VoiceAction;
import com.google.common.base.Preconditions;
import javax.annotation.Nullable;

public abstract class IntentActionExecutor<T extends VoiceAction>
  implements ActionExecutor<T>
{
  protected final IntentStarter mIntentStarter;
  
  protected IntentActionExecutor(IntentStarter paramIntentStarter)
  {
    this.mIntentStarter = paramIntentStarter;
  }
  
  private boolean resolveIntents(Intent... paramVarArgs)
  {
    int i = paramVarArgs.length;
    for (int j = 0; j < i; j++)
    {
      Intent localIntent = paramVarArgs[j];
      if (this.mIntentStarter.resolveIntent(localIntent)) {
        return true;
      }
    }
    return false;
  }
  
  public boolean canExecute(T paramT)
  {
    Intent[] arrayOfIntent = getProberIntents(paramT);
    return (arrayOfIntent == null) || (arrayOfIntent.length == 0) || (resolveIntents(arrayOfIntent));
  }
  
  public boolean execute(T paramT)
  {
    return this.mIntentStarter.startActivity((Intent[])Preconditions.checkNotNull(getExecuteIntents(paramT)));
  }
  
  protected abstract Intent[] getExecuteIntents(T paramT);
  
  protected abstract Intent[] getOpenExternalAppIntents(T paramT);
  
  @Nullable
  protected abstract Intent[] getProberIntents(T paramT);
  
  public boolean openExternalApp(T paramT)
  {
    return this.mIntentStarter.startActivity((Intent[])Preconditions.checkNotNull(getOpenExternalAppIntents(paramT)));
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.fragments.executor.IntentActionExecutor
 * JD-Core Version:    0.7.0.1
 */