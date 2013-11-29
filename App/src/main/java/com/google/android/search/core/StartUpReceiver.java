package com.google.android.search.core;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import com.google.android.googlequicksearchbox.VoiceSearchActivity;
import com.google.android.search.core.summons.icing.InternalIcingCorporaProvider.ApplicationLaunchReceiver;
import com.google.android.velvet.VelvetBackgroundTasks;
import com.google.android.velvet.VelvetServices;
import com.google.android.velvet.VelvetUpgradeTasks;
import com.google.android.voicesearch.VoiceSearchServices;
import com.google.android.voicesearch.handsfree.HandsFreeIntentActivity;

public class StartUpReceiver
  extends BroadcastReceiver
{
  public void onReceive(Context paramContext, Intent paramIntent)
  {
    int i = 2;
    boolean bool = "android.intent.action.MY_PACKAGE_REPLACED".equals(paramIntent.getAction());
    if (("android.intent.action.BOOT_COMPLETED".equals(paramIntent.getAction())) || (bool))
    {
      VelvetServices localVelvetServices = VelvetServices.get();
      CoreSearchServices localCoreSearchServices = localVelvetServices.getCoreServices();
      SearchConfig localSearchConfig = localCoreSearchServices.getConfig();
      VelvetUpgradeTasks.maybeExecuteUpgradeTasks(paramContext, localCoreSearchServices.getSearchSettings(), localSearchConfig, localCoreSearchServices.getBackgroundTasks());
      localCoreSearchServices.getBackgroundTasks().maybeStartTasks();
      localVelvetServices.maybeRegisterSidekickAlarms();
      PackageManager localPackageManager = paramContext.getPackageManager();
      if (!localPackageManager.hasSystemFeature("android.hardware.microphone")) {
        localPackageManager.setComponentEnabledSetting(new ComponentName(paramContext, VoiceSearchActivity.class), i, 1);
      }
      ComponentName localComponentName = new ComponentName(paramContext, InternalIcingCorporaProvider.ApplicationLaunchReceiver.class);
      if (localSearchConfig.isIcingAppLaunchBroadcastHandlingEnabled()) {
        i = 1;
      }
      localPackageManager.setComponentEnabledSetting(localComponentName, i, 1);
      HandsFreeIntentActivity.updateEnabledState(paramContext, localVelvetServices.getVoiceSearchServices().getSettings());
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.StartUpReceiver
 * JD-Core Version:    0.7.0.1
 */