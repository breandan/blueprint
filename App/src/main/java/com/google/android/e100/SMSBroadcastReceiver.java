package com.google.android.e100;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

public class SMSBroadcastReceiver
  extends BroadcastReceiver
{
  private Observer mObserver;
  
  public void onReceive(Context paramContext, Intent paramIntent)
  {
    if (paramIntent.getAction() != "android.provider.Telephony.SMS_RECEIVED") {
      Log.e("SMSBroadcastReceiver", "Got an intent with the wrong action (" + paramIntent.getAction() + ")");
    }
    for (;;)
    {
      return;
      Bundle localBundle = paramIntent.getExtras();
      if (localBundle != null)
      {
        Object[] arrayOfObject = (Object[])localBundle.get("pdus");
        for (int i = 0; i < arrayOfObject.length; i++)
        {
          SmsMessage localSmsMessage = SmsMessage.createFromPdu((byte[])arrayOfObject[i]);
          String str1 = localSmsMessage.getMessageBody();
          String str2 = localSmsMessage.getOriginatingAddress();
          Log.d("SMSBroadcastReceiver", "Got a message from " + str2 + ": " + str1);
          if (this.mObserver != null) {
            this.mObserver.onSMSRecieved(str1, str2);
          }
        }
      }
    }
  }
  
  public void startListening(Context paramContext, Observer paramObserver)
  {
    Log.d("SMSBroadcastReceiver", "Starting SMS listening.");
    this.mObserver = paramObserver;
    IntentFilter localIntentFilter = new IntentFilter();
    localIntentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
    paramContext.registerReceiver(this, localIntentFilter);
  }
  
  public void stopListening(Context paramContext)
  {
    Log.d("SMSBroadcastReceiver", "Stopping SMS listening.");
    paramContext.unregisterReceiver(this);
    this.mObserver = null;
  }
  
  public static abstract interface Observer
  {
    public abstract void onSMSRecieved(String paramString1, String paramString2);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.e100.SMSBroadcastReceiver
 * JD-Core Version:    0.7.0.1
 */