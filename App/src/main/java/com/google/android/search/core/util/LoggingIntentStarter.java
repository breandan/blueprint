package com.google.android.search.core.util;

import android.app.Activity;
import android.content.Intent;
import com.google.android.shared.util.ActivityIntentStarter;
import com.google.android.voicesearch.logger.BugLogger;

public class LoggingIntentStarter
  extends ActivityIntentStarter
{
  public LoggingIntentStarter(Activity paramActivity, int paramInt)
  {
    super(paramActivity, paramInt);
  }
  
  protected void logSecurityException(Intent paramIntent, SecurityException paramSecurityException)
  {
    super.logSecurityException(paramIntent, paramSecurityException);
    BugLogger.record(8543193);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.util.LoggingIntentStarter
 * JD-Core Version:    0.7.0.1
 */