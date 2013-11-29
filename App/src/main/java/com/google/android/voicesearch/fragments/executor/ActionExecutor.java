package com.google.android.voicesearch.fragments.executor;

import com.google.android.voicesearch.fragments.action.VoiceAction;

public abstract interface ActionExecutor<T extends VoiceAction>
{
  public abstract boolean canExecute(T paramT);
  
  public abstract boolean execute(T paramT);
  
  public abstract boolean openExternalApp(T paramT);
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.fragments.executor.ActionExecutor
 * JD-Core Version:    0.7.0.1
 */