package com.google.android.search.core.preferences;

import android.content.Context;
import android.content.res.Resources;
import android.preference.Preference;
import com.google.android.speech.embedded.Greco3DataManager;
import com.google.android.speech.embedded.Greco3Mode;
import com.google.android.voicesearch.settings.Settings;

public class HotwordSettingController
  extends SettingsControllerBase
{
  final Greco3DataManager mGreco3DataManager;
  final Settings mVoiceSettings;
  
  public HotwordSettingController(Settings paramSettings, Greco3DataManager paramGreco3DataManager)
  {
    this.mVoiceSettings = paramSettings;
    this.mGreco3DataManager = paramGreco3DataManager;
  }
  
  public boolean filterPreference(Preference paramPreference)
  {
    this.mGreco3DataManager.waitForInitialization();
    return !this.mGreco3DataManager.hasResources(this.mVoiceSettings.getSpokenLocaleBcp47(), Greco3Mode.HOTWORD);
  }
  
  public void handlePreference(Preference paramPreference)
  {
    String str = this.mGreco3DataManager.getHotwordPrompt(this.mVoiceSettings.getSpokenLocaleBcp47());
    paramPreference.setSummary(paramPreference.getContext().getResources().getString(2131363484, new Object[] { str }));
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.preferences.HotwordSettingController
 * JD-Core Version:    0.7.0.1
 */