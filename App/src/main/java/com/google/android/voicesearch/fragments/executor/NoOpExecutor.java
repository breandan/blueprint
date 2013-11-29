package com.google.android.voicesearch.fragments.executor;

import com.google.android.voicesearch.fragments.action.VoiceAction;

public class NoOpExecutor
  implements ActionExecutor<VoiceAction>
{
  public boolean canExecute(VoiceAction paramVoiceAction)
  {
    return true;
  }
  
  public boolean execute(VoiceAction paramVoiceAction)
  {
    return false;
  }
  
  public boolean openExternalApp(VoiceAction paramVoiceAction)
  {
    return false;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.fragments.executor.NoOpExecutor
 * JD-Core Version:    0.7.0.1
 */