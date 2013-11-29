package com.google.android.voicesearch.handsfree;

import com.google.android.speech.embedded.Greco3DataManager;
import com.google.android.speech.utils.SpokenLanguageUtils;
import com.google.android.voicesearch.settings.Settings;

class SpokenLanguageHelper
{
  private final Greco3DataManager mGreco3DataManager;
  private final Settings mSettings;
  private String mSpokenDisplayLanguage;
  
  public SpokenLanguageHelper(Greco3DataManager paramGreco3DataManager, Settings paramSettings)
  {
    this.mGreco3DataManager = paramGreco3DataManager;
    this.mSettings = paramSettings;
  }
  
  public String getSpokenBcp47Locale()
  {
    this.mGreco3DataManager.waitForInitialization();
    String str = this.mSettings.getSpokenLocaleBcp47();
    if (!this.mGreco3DataManager.hasResourcesForCompilation(str)) {
      str = "en-US";
    }
    return str;
  }
  
  public String getSpokenDisplayLanguageName()
  {
    try
    {
      if (this.mSpokenDisplayLanguage == null) {
        this.mSpokenDisplayLanguage = SpokenLanguageUtils.getDisplayName(this.mSettings.getConfiguration(), getSpokenBcp47Locale());
      }
      String str = this.mSpokenDisplayLanguage;
      return str;
    }
    finally {}
  }
  
  public boolean hasResources()
  {
    return this.mGreco3DataManager.hasResourcesForCompilation(getSpokenBcp47Locale());
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.handsfree.SpokenLanguageHelper
 * JD-Core Version:    0.7.0.1
 */