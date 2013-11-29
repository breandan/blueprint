package com.google.android.speech.alternates;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.google.android.speech.logger.SuggestionLogger;
import com.google.android.velvet.VelvetServices;
import com.google.android.voicesearch.VoiceSearchServices;
import com.google.android.voicesearch.logger.EventLogger;
import com.google.android.voicesearch.logger.EventLoggerService;

public class SuggestionSpanBroadcastReceiver
  extends BroadcastReceiver
{
  private static boolean DEBUG = false;
  
  public static Intent createIntentForEasyEditSpan(Context paramContext, String paramString, int paramInt)
  {
    Intent localIntent = new Intent("com.google.android.speech.NOTIFY_TEXT_CHANGED");
    localIntent.setClass(paramContext, SuggestionSpanBroadcastReceiver.class);
    localIntent.putExtra("com.google.android.speech.REQUEST_ID", paramString);
    localIntent.putExtra("com.google.android.speech.SEGMENT_ID", paramInt);
    return localIntent;
  }
  
  public void onReceive(Context paramContext, Intent paramIntent)
  {
    if (DEBUG) {
      Log.i("SuggestionSpanBroadcastReceiver", "#onReceive " + paramIntent);
    }
    if ("android.text.style.SUGGESTION_PICKED".equals(paramIntent.getAction()))
    {
      String str2 = paramIntent.getStringExtra("before");
      String str3 = paramIntent.getStringExtra("after");
      int m = paramIntent.getIntExtra("hashcode", 0);
      if (DEBUG) {
        Log.i("SuggestionSpanBroadcastReceiver", "#onReceive " + m + " " + str2 + ">" + str3);
      }
      VelvetServices.get().getVoiceSearchServices().getSuggestionLogger().log(m, str2, str3);
      EventLoggerService.scheduleSendEvents(paramContext);
    }
    String str1;
    if ("com.google.android.speech.NOTIFY_TEXT_CHANGED".equals(paramIntent.getAction()))
    {
      str1 = paramIntent.getStringExtra("com.google.android.speech.REQUEST_ID");
      if (str1 == null) {
        Log.w("SuggestionSpanBroadcastReceiver", "Missing request id");
      }
    }
    else
    {
      return;
    }
    int i = paramIntent.getIntExtra("com.google.android.speech.SEGMENT_ID", -1);
    if (i == -1)
    {
      Log.w("SuggestionSpanBroadcastReceiver", "Missing segment id");
      return;
    }
    int j = paramIntent.getIntExtra("android.text.style.EXTRA_TEXT_CHANGED_TYPE", -1);
    if (j == -1)
    {
      Log.w("SuggestionSpanBroadcastReceiver", "Missing changedType");
      return;
    }
    switch (j)
    {
    default: 
      Log.w("SuggestionSpanBroadcastReceiver", "Unknown changedType " + j);
      return;
    }
    for (int k = 34;; k = 16)
    {
      Bundle localBundle = new Bundle();
      localBundle.putString("com.google.android.speech.REQUEST_ID", str1);
      localBundle.putInt("com.google.android.speech.SEGMENT_ID", i);
      EventLogger.recordClientEvent(k, localBundle);
      return;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.speech.alternates.SuggestionSpanBroadcastReceiver
 * JD-Core Version:    0.7.0.1
 */