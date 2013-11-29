package com.google.android.sidekick.main.gcm;

import android.accounts.Account;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.google.android.search.core.CoreSearchServices;
import com.google.android.search.core.NowOptInSettings;
import com.google.android.search.core.google.gaia.LoginHelper;
import com.google.android.sidekick.main.entry.EntriesRefreshIntentService;
import com.google.android.sidekick.main.entry.EntryProvider;
import com.google.android.sidekick.main.inject.SidekickInjector;
import com.google.android.sidekick.main.sync.MessageMicroUtil;
import com.google.android.sidekick.shared.util.Tag;
import com.google.android.velvet.VelvetServices;
import com.google.geo.sidekick.Sidekick.SidekickPushMessage;
import com.google.protobuf.micro.InvalidProtocolBufferMicroException;

public class SidekickGCMIntentService
  extends IntentService
{
  private static final String TAG = Tag.getTag(SidekickGCMIntentService.class);
  
  public SidekickGCMIntentService()
  {
    super("638181764685");
  }
  
  /* Error */
  protected void onHandleIntent(Intent paramIntent)
  {
    // Byte code:
    //   0: aload_1
    //   1: invokevirtual 30	android/content/Intent:getExtras	()Landroid/os/Bundle;
    //   4: astore 4
    //   6: aload_0
    //   7: invokestatic 36	com/google/android/gms/gcm/GoogleCloudMessaging:getInstance	(Landroid/content/Context;)Lcom/google/android/gms/gcm/GoogleCloudMessaging;
    //   10: aload_1
    //   11: invokevirtual 40	com/google/android/gms/gcm/GoogleCloudMessaging:getMessageType	(Landroid/content/Intent;)Ljava/lang/String;
    //   14: astore 5
    //   16: aload 4
    //   18: invokevirtual 46	android/os/Bundle:isEmpty	()Z
    //   21: ifne +17 -> 38
    //   24: ldc 48
    //   26: aload 5
    //   28: invokevirtual 54	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   31: istore 7
    //   33: iload 7
    //   35: ifeq +9 -> 44
    //   38: aload_1
    //   39: invokestatic 60	com/google/android/sidekick/main/gcm/SidekickGCMBroadcastReceiver:completeWakefulIntent	(Landroid/content/Intent;)Z
    //   42: pop
    //   43: return
    //   44: ldc 62
    //   46: aload 5
    //   48: invokevirtual 54	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   51: ifeq -13 -> 38
    //   54: aload_0
    //   55: aload_0
    //   56: aload 4
    //   58: invokevirtual 66	com/google/android/sidekick/main/gcm/SidekickGCMIntentService:onMessage	(Landroid/content/Context;Landroid/os/Bundle;)V
    //   61: goto -23 -> 38
    //   64: astore_2
    //   65: aload_1
    //   66: invokestatic 60	com/google/android/sidekick/main/gcm/SidekickGCMBroadcastReceiver:completeWakefulIntent	(Landroid/content/Intent;)Z
    //   69: pop
    //   70: aload_2
    //   71: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	72	0	this	SidekickGCMIntentService
    //   0	72	1	paramIntent	Intent
    //   64	7	2	localObject	java.lang.Object
    //   4	53	4	localBundle	Bundle
    //   14	33	5	str	String
    //   31	3	7	bool	boolean
    // Exception table:
    //   from	to	target	type
    //   0	33	64	finally
    //   44	61	64	finally
  }
  
  protected void onMessage(Context paramContext, Bundle paramBundle)
  {
    String str1 = paramBundle.getString("m");
    if (str1 != null)
    {
      VelvetServices localVelvetServices;
      Sidekick.SidekickPushMessage localSidekickPushMessage;
      try
      {
        localVelvetServices = VelvetServices.get();
        CoreSearchServices localCoreSearchServices = localVelvetServices.getCoreServices();
        Account localAccount = localCoreSearchServices.getLoginHelper().getAccount();
        if (!localCoreSearchServices.getNowOptInSettings().isAccountOptedIn(localAccount)) {
          return;
        }
        String str2 = GcmUtil.accountHashFor(localAccount);
        localSidekickPushMessage = (Sidekick.SidekickPushMessage)MessageMicroUtil.decodeFromString(new Sidekick.SidekickPushMessage(), str1);
        if ((localSidekickPushMessage.hasAccountHash()) && (!str2.equals(localSidekickPushMessage.getAccountHash()))) {
          break label192;
        }
        if (localSidekickPushMessage.hasEntryChanges())
        {
          localVelvetServices.getSidekickInjector().getEntryProvider().updateFromPartialEntries(localSidekickPushMessage.getEntryChanges());
          return;
        }
      }
      catch (InvalidProtocolBufferMicroException localInvalidProtocolBufferMicroException)
      {
        Log.w(TAG, "Bad push message received over GCM", localInvalidProtocolBufferMicroException);
        return;
      }
      localVelvetServices.getSidekickInjector().getPushMessageRespository().add(localSidekickPushMessage);
      Intent localIntent = new Intent(paramContext, EntriesRefreshIntentService.class);
      localIntent.setAction("com.google.android.apps.sidekick.PARTIAL_REFRESH");
      localIntent.putExtra("com.google.android.apps.sidekick.TYPE", 5);
      paramContext.startService(localIntent);
      return;
      label192:
      Log.i(TAG, "Received a push message for another account on this phone");
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.gcm.SidekickGCMIntentService
 * JD-Core Version:    0.7.0.1
 */