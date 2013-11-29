package com.google.android.voicesearch.personalization;

import com.google.android.speech.helper.AccountHelper;
import com.google.android.speech.utils.NetworkInformation;
import com.google.android.voicesearch.settings.Settings;
import com.google.wireless.voicesearch.proto.GstaticConfiguration.Configuration;
import com.google.wireless.voicesearch.proto.GstaticConfiguration.Personalization;
import java.util.List;

public final class PersonalizationHelper
{
  private final AccountHelper mAccountHelper;
  private final NetworkInformation mNetworkInformation;
  private final Settings mSettings;
  
  public PersonalizationHelper(Settings paramSettings, AccountHelper paramAccountHelper, NetworkInformation paramNetworkInformation)
  {
    this.mSettings = paramSettings;
    this.mAccountHelper = paramAccountHelper;
    this.mNetworkInformation = paramNetworkInformation;
  }
  
  public boolean isPersonalizationAvailable()
  {
    boolean bool2;
    if (!this.mAccountHelper.hasAccount()) {
      bool2 = false;
    }
    do
    {
      return bool2;
      int i = this.mNetworkInformation.getSimMcc();
      boolean bool1 = this.mSettings.getConfiguration().getPersonalization().getMccCountryCodesList().contains(Integer.valueOf(i));
      bool2 = false;
      if (bool1) {
        bool2 = true;
      }
    } while (this.mSettings.getPersonalizationValue() != 0);
    Settings localSettings = this.mSettings;
    if (bool2) {}
    for (int j = 5;; j = 1)
    {
      localSettings.setPersonalizationValue(j);
      return bool2;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.personalization.PersonalizationHelper
 * JD-Core Version:    0.7.0.1
 */