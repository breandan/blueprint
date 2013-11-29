package com.google.android.voicesearch.handsfree;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;
import com.google.android.search.core.Feature;
import com.google.android.search.core.service.SearchServiceImpl;
import com.google.android.velvet.VelvetServices;
import com.google.android.velvet.VelvetStrictMode;
import com.google.android.voicesearch.VoiceSearchServices;
import com.google.android.voicesearch.settings.Settings;
import com.google.android.voicesearch.util.LocalTtsManager;
import com.google.common.base.Preconditions;
import java.util.Iterator;
import java.util.List;

public class HandsFreeIntentActivity
  extends Activity
{
  private static boolean doesSystemAppExist(Context paramContext, String paramString)
  {
    Log.d("HandsFreeIntentActivity", "doesSystemAppExist: " + paramString);
    List localList = paramContext.getPackageManager().queryIntentActivities(new Intent(paramString), 65536);
    if (localList == null)
    {
      Log.d("HandsFreeIntentActivity", "resolvedActivities was null");
      return false;
    }
    Iterator localIterator = localList.iterator();
    while (localIterator.hasNext())
    {
      ResolveInfo localResolveInfo = (ResolveInfo)localIterator.next();
      if ((localResolveInfo == null) || (localResolveInfo.activityInfo == null) || (localResolveInfo.activityInfo.packageName == null) || (localResolveInfo.activityInfo.applicationInfo == null))
      {
        Log.d("HandsFreeIntentActivity", "resolveInfo is missing");
      }
      else if ((!paramContext.getPackageName().equals(localResolveInfo.activityInfo.packageName)) && ((0x1 & localResolveInfo.activityInfo.applicationInfo.flags) != 0))
      {
        Log.d("HandsFreeIntentActivity", "System app found: " + localResolveInfo.activityInfo.packageName);
        return true;
      }
    }
    Log.d("HandsFreeIntentActivity", "No system app found");
    return false;
  }
  
  private void handleIntent(String paramString)
  {
    if (Feature.EYES_FREE.isEnabled())
    {
      Log.d("HandsFreeIntentActivity", "Handing over to E100 for: " + paramString);
      Log.d("HandsFreeIntentActivity", "Bluetooth connected!");
      Intent localIntent2 = new Intent("com.google.android.search.core.VOICE_ACTION", null, this, SearchServiceImpl.class);
      boolean bool = paramString.equals("android.speech.action.VOICE_SEARCH_HANDS_FREE");
      if ((!bool) && (!paramString.equals("android.intent.action.VOICE_COMMAND")))
      {
        VelvetStrictMode.logW("HandsFreeIntentActivity", "handleIntent: Neither wired not bluetooth: " + paramString, new Error());
        finish();
      }
      localIntent2.putExtra("com.google.android.search.core.handsfreesource", bool);
      startService(localIntent2);
      finish();
      return;
    }
    Intent localIntent1 = new Intent(paramString);
    localIntent1.setClass(getBaseContext(), HandsFreeActivity.class);
    localIntent1.addFlags(268435456);
    Log.d("HandsFreeIntentActivity", "Starting activity: " + localIntent1);
    getBaseContext().startActivity(localIntent1);
    finish();
  }
  
  private static boolean shouldBeEnabled(Context paramContext, String paramString)
  {
    Log.d("HandsFreeIntentActivity", "shouldBeEnabled: locale=" + paramString);
    if ((paramString != null) && (paramString.startsWith("en"))) {}
    while ((!doesSystemAppExist(paramContext, "android.intent.action.VOICE_COMMAND")) || (!doesSystemAppExist(paramContext, "android.speech.action.VOICE_SEARCH_HANDS_FREE"))) {
      return true;
    }
    return false;
  }
  
  public static void updateEnabledState(Context paramContext, Settings paramSettings)
  {
    PackageManager localPackageManager = paramContext.getPackageManager();
    ComponentName localComponentName = new ComponentName(paramContext, HandsFreeIntentActivity.class);
    boolean bool1;
    boolean bool2;
    int i;
    if (localPackageManager.getComponentEnabledSetting(localComponentName) != 2)
    {
      bool1 = true;
      bool2 = shouldBeEnabled(paramContext, paramSettings.getSpokenLocaleBcp47());
      if (bool1 == bool2) {
        break label98;
      }
      i = 0;
      if (!bool2) {
        break label85;
      }
      label53:
      localPackageManager.setComponentEnabledSetting(localComponentName, i, 1);
      if (!bool2) {
        break label91;
      }
    }
    label85:
    label91:
    for (String str = "Enabling voice dialer";; str = "Disabling voice dialer")
    {
      Log.i("HandsFreeIntentActivity", str);
      return;
      bool1 = false;
      break;
      i = 2;
      break label53;
    }
    label98:
    Log.d("HandsFreeIntentActivity", "Voice dialer enabled state: " + bool2);
  }
  
  protected void onStart()
  {
    Log.d("HandsFreeIntentActivity", "#onStart(" + getIntent() + ")");
    Preconditions.checkNotNull(getIntent().getAction());
    super.onStart();
    String str = getIntent().getAction();
    if (str.equals("android.speech.action.VOICE_SEARCH_HANDS_FREE"))
    {
      KeyguardManager localKeyguardManager = (KeyguardManager)getBaseContext().getSystemService("keyguard");
      if ((localKeyguardManager != null) && (localKeyguardManager.isKeyguardLocked()) && (localKeyguardManager.isKeyguardSecure()))
      {
        Log.d("HandsFreeIntentActivity", "Device securely locked, playing TTS");
        VelvetServices.get().getVoiceSearchServices().getLocalTtsManager().enqueue(2131363660, new Runnable()
        {
          public void run()
          {
            Log.d("HandsFreeIntentActivity", "TTS complete");
            HandsFreeIntentActivity.this.finish();
          }
        });
        return;
      }
      handleIntent(str);
      return;
    }
    if (str.equals("android.intent.action.VOICE_COMMAND"))
    {
      handleIntent(str);
      return;
    }
    Log.i("HandsFreeIntentActivity", "Ignored intent: " + getIntent());
    finish();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.handsfree.HandsFreeIntentActivity
 * JD-Core Version:    0.7.0.1
 */