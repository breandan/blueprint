package com.google.android.search.core;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.google.android.speech.utils.SpokenLanguageUtils;
import com.google.android.velvet.VelvetBackgroundTasks;
import com.google.android.velvet.VelvetServices;
import com.google.android.voicesearch.VoiceSearchServices;
import com.google.android.voicesearch.settings.Settings;
import com.google.wireless.voicesearch.proto.GstaticConfiguration.Dialect;
import java.util.Locale;

public class GservicesBroadcastReceiver
  extends BroadcastReceiver
{
  private void handlePhoneLocaleChange(Settings paramSettings)
  {
    if (paramSettings.isDefaultSpokenLanguage())
    {
      String str1 = Locale.getDefault().toString();
      String str2 = paramSettings.getSpokenLocaleBcp47();
      GstaticConfiguration.Dialect localDialect = SpokenLanguageUtils.getSpokenLanguageByJavaLocale(paramSettings.getConfiguration(), str1);
      if ((localDialect != null) && (!str2.equals(localDialect.getBcp47Locale()))) {
        paramSettings.setSpokenLanguageBcp47(localDialect.getBcp47Locale(), true);
      }
    }
  }
  
  public void onReceive(Context paramContext, Intent paramIntent)
  {
    String str = paramIntent.getAction();
    VelvetServices localVelvetServices = VelvetServices.get();
    if ("com.google.gservices.intent.action.GSERVICES_CHANGED".equals(str)) {
      localVelvetServices.getCoreServices().getBackgroundTasks().forceRunInterruptingOngoing("update_gservices_config");
    }
    while (!"android.intent.action.LOCALE_CHANGED".equals(str)) {
      return;
    }
    handlePhoneLocaleChange(localVelvetServices.getVoiceSearchServices().getSettings());
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.GservicesBroadcastReceiver
 * JD-Core Version:    0.7.0.1
 */