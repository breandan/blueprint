package com.google.android.voicesearch.fragments.executor;

import android.content.Intent;
import android.net.Uri;
import com.google.android.shared.util.IntentStarter;
import com.google.android.voicesearch.fragments.action.OpenUrlAction;
import javax.annotation.Nullable;

public class OpenUrlActionExecutor
  extends IntentActionExecutor<OpenUrlAction>
{
  protected OpenUrlActionExecutor(IntentStarter paramIntentStarter)
  {
    super(paramIntentStarter);
  }
  
  private Intent getIntent(String paramString)
  {
    return new Intent("android.intent.action.VIEW").setData(Uri.parse(paramString));
  }
  
  protected Intent[] getExecuteIntents(OpenUrlAction paramOpenUrlAction)
  {
    Intent[] arrayOfIntent = new Intent[1];
    arrayOfIntent[0] = getIntent(paramOpenUrlAction.getLink());
    return arrayOfIntent;
  }
  
  protected Intent[] getOpenExternalAppIntents(OpenUrlAction paramOpenUrlAction)
  {
    Intent[] arrayOfIntent = new Intent[1];
    arrayOfIntent[0] = getIntent(paramOpenUrlAction.getLink());
    return arrayOfIntent;
  }
  
  @Nullable
  protected Intent[] getProberIntents(OpenUrlAction paramOpenUrlAction)
  {
    Intent[] arrayOfIntent = new Intent[1];
    arrayOfIntent[0] = getIntent("http://www.google.com");
    return arrayOfIntent;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.fragments.executor.OpenUrlActionExecutor
 * JD-Core Version:    0.7.0.1
 */